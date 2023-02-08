Congratulations! Your game is almost ready, what remains is only to add the display of the leaderboard for the current game
and the for the previous rounds.

In this task, you need to implement several things in the already defined class `GameResultsService` in
the `jetbrains.kotlin.course.words.generator.results` package. Note, this class is the same as `GameResultsService` from the Alias game, so you can either practice again or just copy the previous solution.

- Add the type alias `GameResult` to `List<Team>` in the `jetbrains.kotlin.course.alias.results` package.
- Add a companion object to `GameResultsService`
  and declare the `gameHistory` variable to store the list of game results (`MutableList<GameResult>`).
  By default, it must be initialized as an empty list.
- Implement the `saveGameResults` method, which adds `result` to `gameHistory`.
  Before adding `result`, you need to check for two requirements and throw an error if they are broken: 1) `result` must
  not be empty; 2) all team ids in `result` must be present in `TeamService.teamsStorage`.
- Implement the `getAllGameResults` method, which returns the reversed `gameHistory` list.

If you have any difficulties, **hints will help you solve this task**.

----

### Hints

<div class="hint" title="The `isNotEmpty` built-in function">

If you need to check whether a list is empty or not, you can either check its size or use the built-in [isNotEmpty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/is-not-empty.html) function:

  ```kotlin
  val numbers = listOf(1, 2, 3)
  if (numbers.size != 0) {
      TODO()
  }
  ```
It is the **same** as

  ```kotlin
  val numbers = listOf(1, 2, 3)
  if (numbers.isNotEmpty()) {
      TODO()
  }
  ```
</div>

<div class="hint" title="The `reversed` built-in function">

If you need to get a list in which the elements are in reverse order,
you can either loop through the elements of the original list from the end to the beginning and
return the new list or use the built-in [`reversed`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/reversed.html) function:

  ```kotlin
  val numbers = listOf(1, 2, 3, 4)
  val reversedList = mutableListOf<Int>()
  for (i in numbers.size - 1 downTo 0) {
    reversedList.add(numbers[i])
  }
  println(reversedList) // [4, 3, 2, 1]
  ```

It is the **same** as
  ```kotlin
  val numbers = listOf(1, 2, 3, 4)
  val reversedList = numbers.reversed()
  println(reversedList) // [4, 3, 2, 1]
  ```
</div>