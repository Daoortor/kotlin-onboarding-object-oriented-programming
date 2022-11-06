import jetbrains.kotlin.course.alias.util.words
import models.ConstructorGetter
import models.findMethod
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun identifierFactoryClassTest() {
        val clazz = identifierFactoryClass.checkBaseDefinition()
        identifierFactoryClass.checkFieldsDefinition(clazz)
        identifierFactoryClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(Int::class.java)),
            )
        )
        identifierFactoryClass.checkDeclaredMethods(clazz)
    }

    @Test
    fun uniqueIdentifierMethodTest() {
        val clazz = identifierFactoryClass.getJavaClass()
        val method = identifierFactoryClass.findMethod(clazz, uniqueIdentifierMethod)
        val instance = clazz.getConstructor().newInstance()
        for (i in 0..100) {
            val id = identifierFactoryClass.invokeMethodWithoutArgs(clazz, instance, method)
            assert(id == i) { "The ${uniqueIdentifierMethod.name} works incorrect. Try to get id $i-th time, it should be $i, but was $id" }
        }
    }

    @Test
    fun teamClassTest() {
        val clazz = teamClass.checkBaseDefinition()
        teamClass.checkFieldsDefinition(clazz)
        val constructor = teamClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(
                    parameterTypes = listOf(Int::class.java),
                    defaultParameterTypes = listOf(Int::class.java),
                )
            )
        )
        // Check the name field value
        constructor?.let {
            val method = clazz.methods.findMethod(getNameFromTeamMethod)
            for (i in 0..100) {
                val instance = try {
                    it.newInstance(i, 0, 0, null)
                } catch (e: Exception) {
                    assert(false) { "Can not create an instance of the class ${teamClass.getFullName()} with id = $i" }
                }
                val name = teamClass.invokeMethodWithoutArgs(clazz, instance, method)
                val teamName = "Team#${i + 1}"
                assert(teamName == name) { "For the team with id $i the name must be $teamName" }
            }

        }
    }

    @Test
    fun teamTeamServiceTest() {
        val clazz = teamServiceTestClass.checkBaseDefinition()
        teamServiceCompanionTestClass.checkBaseDefinition()

        teamServiceTestClass.checkFieldsDefinition(clazz, false)
        val identifierFactoryClazz = identifierFactoryClass.getJavaClass()
        teamServiceTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(identifierFactoryClazz)),
            )
        )
        teamServiceTestClass.checkDeclaredMethods(clazz)
    }

    @Test
    fun generateTeamsForOneRoundMethodTest() {
        val clazz = teamServiceTestClass.getJavaClass()
        val method = teamServiceTestClass.findMethod(clazz, generateTeamsForOneRoundMethod)
        val instance = clazz.getConstructor().newInstance()
        val n = 5
        val teamsStorageMethod = clazz.methods.findMethod(getTeamsStorageMethod)
        val teamsStorageSb = StringBuilder()
        repeat(n) {
            val teams = teamServiceTestClass.invokeMethodWithArgs(
                n,
                clazz = clazz,
                instance = instance,
                javaMethod = method,
            )
            val expected = teamsOutput(it * n, n)
            assert(expected == teams.toString()) { "${generateTeamsForOneRoundMethod.name} must return $expected for teamsNumber = $n and $it-th attempt." }
            if (it > 0) {
                teamsStorageSb.append(", ")
            }
            teamsStorageSb.append(generateTeamsStringRepresentation(it * n, n, true).joinToString(", "))
            val teamsStorageRes = teamServiceTestClass.invokeMethodWithoutArgs(
                clazz = clazz,
                instance = instance,
                javaMethod = teamsStorageMethod
            ).toString()
            val expectedTeamsStorage = "{$teamsStorageSb}"
            assert(expectedTeamsStorage == teamsStorageRes) { "You need to save generated teams into the teamsStorage after each generation." }
        }
    }

    private fun generateTeamsStringRepresentation(startId: Int, n: Int, toAddId: Boolean = false): List<String> {
        var id = startId
        val teams = mutableListOf<String>()
        repeat(n) {
            val prefix = if (toAddId) {
                "$id="
            } else {
                ""
            }
            teams.add("${prefix}Team(id=$id, points=0)")
            id++
        }
        return teams
    }

    private fun teamsOutput(startId: Int, n: Int) =
        "[${generateTeamsStringRepresentation(startId, n).joinToString(", ")}]"

    @Test
    fun cardTest() {
        val clazz = cardTestClass.checkBaseDefinition()
        cardTestClass.checkFieldsDefinition(clazz)
        val constructor = cardTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(
                    parameterTypes = listOf(Int::class.java, List::class.java),
                )
            )
        )
        // Just check if the constructor works well
        constructor?.newInstance(1, listOf("dog"))
    }

    @Test
    fun cardServiceTest() {
        val clazz = cardServiceTestClass.checkBaseDefinition()
        cardServiceCompanionTestClass.checkBaseDefinition()

        cardServiceTestClass.checkFieldsDefinition(clazz, false)
        val identifierFactoryClazz = identifierFactoryClass.getJavaClass()
        cardServiceTestClass.checkConstructors(
            clazz,
            listOf(
                ConstructorGetter(),
                ConstructorGetter(defaultParameterTypes = listOf(identifierFactoryClazz)),
            )
        )

        // Check WORDS_IN_CARD and cardsAmount values
        val instance = clazz.getConstructor().newInstance()
        val getCardsAmountJavaMethod = clazz.methods.findMethod(getCardsAmountMethod)
        val field = clazz.declaredFields.find { it.name == wordsInCardTestVariable.name }
            ?: error("Can not find the field ${wordsInCardTestVariable.name}")
        field.isAccessible = true
        val wordsInCardVariable = field.get(instance)
        assert(4 == wordsInCardVariable as Int) { "The value of the field ${wordsInCardTestVariable.name} must be 4." }
        val cardsAmount = identifierFactoryClass.invokeMethodWithoutArgs(clazz, instance, getCardsAmountJavaMethod)
        val expectedCardsAmount = words.size / wordsInCardVariable
        assert(expectedCardsAmount == cardsAmount as Int) { "The value in the field cardsAmount must be calculated as: words.size / WORDS_IN_CARD" }

        cardServiceTestClass.checkDeclaredMethods(clazz)
    }

    // TODO: check methods implementations
}