It is time for practice!
In this task, you will create some utility to store the base game settings!

### Task

Create an object `Utils` in the `jetbrains.kotlin.course.codenames.utils` package to store the general game settings:

**TODO: change name for N**

- add several consts into the `Utils` object to store the common constants:
  - `N = 5`, 
  - `TOTAL_AMOUNT = N * N`, 
  - `PINK_CARDS_NUMBER = 8`, 
  - `VIOLET_CARDS_NUMBER = 9`, 
  - `GRAY_CARDS_NUMBER = 7`, 
  - `BLACK_CARDS_NUMBER = 1`.
  
  The `N` variable will be used only inside the `Utils` object.
- add the `init` block to the `Utils` object to check the sum of `PINK_CARDS_NUMBER`, `VIOLET_CARDS_NUMBER`, `GRAY_CARDS_NUMBER`, and `BLACK_CARDS_NUMBER` is exactly `TOTAL_AMOUNT`.
  If the conditions is false, you need to throw an `IllegalArgumentException` error.

If you have any difficulties, **hints will help you solve this task**.

----

### Hints

<div class="hint" title="Access modifiers">
  
  Only the `N` const variable must be `private`, because we will use other variables in the future tasks, 
  e.g. for building the key card in the game.
</div>

<div class="hint" title="The require built-in function">
  
Don't forget about [`require`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/require.html) built-in function, that helps to check some conditions and throws an `IllegalArgumentException` error:

```kotlin
object Utils {
    ...
  
    init {
      val sum = ...
      if (sum != TOTAL_AMOUNT) {
          throw IllegalArgumentException("The total amount in the game must be: $TOTAL_AMOUNT")
      }
    }
}
```

is the same with 

```kotlin
object Utils {
    ...
  
    init {
      require(sum == TOTAL_AMOUNT) { "The total amount in the game must be: $TOTAL_AMOUNT" }
    }
}
```

</div>
