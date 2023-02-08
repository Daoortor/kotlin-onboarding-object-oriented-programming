Next we need to create a special class to store the card information. 
Firstly, we need to create a new enum class to store a state for the cards.
Create an enum class `CardState` in the package `jetbrains.kotlin.course.codenames.card`. 
This class must store two values: `Data` and `Back`.

Secondly, create a class `Card` in the same package with two immutable fields in the 
primary constructor: `data: CardData` and `state: CardState`.

If you have any difficulties, **hints will help you solve this task**.

----

### Hints

<div class="hint" title="The type of the Card class">

The `Card` must be a [data class](https://kotlinlang.org/docs/data-classes.html), because it stores data for cards.
</div>