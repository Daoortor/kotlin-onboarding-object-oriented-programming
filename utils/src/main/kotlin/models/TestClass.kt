package models

import throwInternalCourseError
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.jvm.internal.DefaultConstructorMarker

enum class ClassType(val key: String) {
    CLASS("class"),
    INTERFACE("interface"),
    ENUM("enum class"),
    OBJECT("object"),
    ;
}

data class TestClass(
    val name: String = "MainKt",
    val classPackage: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
    val classType: ClassType = ClassType.CLASS,
    val declaredFields: List<Variable> = emptyList(),
    val customMethods: List<TestMethod> = emptyList(),
    val isDataClass: Boolean = false,
) {
    fun getFullName() = classPackage?.let {
        "$it.$name"
    } ?: name

    private fun getBaseDefinition() = "${visibility.key} ${classType.key} $name"

    private fun getFieldsListPrettyString() = declaredFields.joinToString { it.prettyString() }

    fun checkBaseDefinition(): Class<*> {
        val clazz = this.findClassSafe()
        val errorMessage = "You need to add: ${this.getBaseDefinition()}"
        assert(clazz != null) { errorMessage }
        assert(
            clazz!!.isSameWith(this)
        ) {
            "$errorMessage, but currently you added: ${
                clazz.toTestClass(this.name, this.classPackage).getBaseDefinition()
            }"
        }
        if (isDataClass) {
            clazz.checkIfIsDataClass(this)
        }
        return clazz
    }

    private fun checkFields(clazz: Class<*>) {
        clazz.declaredFields.forEach { field ->
            val currentField = declaredFields.find { it.name == field.name }
            assert(currentField != null) { "Can not find the field with name ${field.name}" }
            currentField!!.checkField(field)
        }
    }

    fun checkFieldsDefinition(clazz: Class<*>) {
        assert(clazz.declaredFields.size == this.declaredFields.size) { "You need to declare the following fields: ${this.getFieldsListPrettyString()}" }
        this.checkFields(clazz)
    }

    fun getJavaClass(): Class<*> {
        val clazz = this.findClassSafe()
        assert(clazz != null) { "You need to add: ${this.getBaseDefinition()}" }
        return clazz!!
    }

    fun checkConstructorWithDefaultArguments(
        clazz: Class<*>,
        parameterTypes: List<Class<*>> = emptyList(),
        defaultParameterTypes: List<Class<*>> = emptyList()
    ): Constructor<out Any>? {
        try {
            val parameters = (parameterTypes + defaultParameterTypes.map { listOf(it, Int::class.java) }
                .flatten()).toMutableList()
            if (defaultParameterTypes.isNotEmpty()) {
                parameters.add(DefaultConstructorMarker::class.java)
            }
            return clazz.getConstructor(*parameters.toTypedArray())
        } catch (e: NoSuchMethodException) {
            assert(false) { "You don't have any constructors with ${parameterTypes.size} arguments in the class $name. Please, check the arguments, probably you need to add the default values." }
        }
        return null
    }

    fun checkDeclaredMethods(clazz: Class<*>) {
        val methods = clazz.methods
        customMethods.forEach {
            val method = methods.findMethod(it)
            it.checkMethod(method)
        }
    }

    fun findMethod(clazz: Class<*>, method: TestMethod): Method {
        assert(method in customMethods) { "The method ${method.name} was not found in the class ${getFullName()}" }
        return clazz.methods.findMethod(method)
    }

    fun <T> invokeMethodWithoutArgs(clazz: Class<*>, instance: T, javaMethod: Method) =
        javaMethod.invokeWithoutArgs(clazz, obj = instance)
}

fun TestClass.findClass(): Class<*> = Class.forName(this.getFullName())

fun TestClass.findClassSafe() = try {
    this.findClass()
} catch (e: ClassNotFoundException) {
    null
}

private fun Class<*>.getVisibility(): Visibility? {
    if (Modifier.isPublic(this.modifiers)) {
        return Visibility.PUBLIC
    }
    if (Modifier.isPrivate(this.modifiers)) {
        return Visibility.PRIVATE
    }
    return null
}

private fun Class<*>.getClassType(): ClassType {
    if (this.isInterface) {
        return ClassType.INTERFACE
    }
    if (this.isEnum) {
        return ClassType.ENUM
    }
    // TODO: think about object and companion object
    return ClassType.CLASS
}

private fun Class<*>.checkIfIsDataClass(testClass: TestClass) {
    val methods = this.methods
    val methodsNames = methods.map { it.name }
    val dataClassMethods = listOf(
        "copy",
        "equals",
        "hashCode",
        "toString",
    )
    dataClassMethods.forEach {
        assert(it in methodsNames) { "${testClass.getFullName()} must be a data class and must have the $it method" }
    }
    val (primary, _) = testClass.declaredFields.partition { it.isInPrimaryConstructor }
    val componentNFunctions = methodsNames.filter { "component" in it }
    val componentNErrorMessage =
        "You must put only ${primary.size} fields into the primary constructor: ${primary.joinToString(", ") { it.name }}."
    assert(componentNFunctions.size == primary.size) { componentNErrorMessage }
    primary.forEachIndexed { index, _ ->
        assert("component${index + 1}" in methodsNames) { componentNErrorMessage }
    }
}

private fun Class<*>.hasSameVisibilityWith(testClass: TestClass) =
    this.getVisibility() == testClass.visibility

private fun Class<*>.hasSameClassTypeWith(testClass: TestClass) =
    this.getClassType() == testClass.classType

fun Class<*>.isSameWith(testClass: TestClass) =
    this.hasSameVisibilityWith(testClass) && this.hasSameClassTypeWith(testClass)

fun Class<*>.toTestClass(name: String, classPackage: String?): TestClass {
    val visibility = this.getVisibility() ?: throwInternalCourseError()
    return TestClass(name, classPackage, visibility, this.getClassType())
}
