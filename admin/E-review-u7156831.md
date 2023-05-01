## Code Review

Reviewed by: Nathan Woodburn, u7156831

Reviewing code written by: Immanuel Alvaro Bhirawa, u7280427

Component: `isMoveValid` from [BlueLagoon.java L145-L311](https://gitlab.cecs.anu.edu.au/u7156831/comp1110-ass2/-/blob/b8487c3c0826bef4e676a13f8ea05c578c73d2de/src/comp1110/ass2/BlueLagoon.java#L145-L311)

### Comments

- Good practices used:  
  - Using other functions to help with the main function, such as `isStateStringWellFormed` and `isMoveStringWellFormed`.
  - Using good variable names such as `currentPhase` and `boardHeight`
  - Using a `switch case` statement instead of multiple `if` statements.
  - Using `.addAll()` to add multiple elements to an arraylist instead of iterating through each element and adding them
  - Good use of `for` loops to iterate through arrays

- Places to improve:  
  - Some comments are not needed, such as commenting about well named variables.
    ```java
    int numberOfPlayer = 0; // Number of player
    String playerId = ""; // Player ID
    ```
  - Duplicate code could be avoided. For example [this switch](https://gitlab.cecs.anu.edu.au/u7156831/comp1110-ass2/-/blob/b8487c3c0826bef4e676a13f8ea05c578c73d2de/src/comp1110/ass2/BlueLagoon.java#L231-L256) could be shortened to the below to avoid duplicate code.
    ```java
    switch (numberOfPlayer) {
        case 4 -> numberOfSettlersPerPlayer = 20;
        case 3 -> numberOfSettlersPerPlayer = 25;
        case 2 -> numberOfSettlersPerPlayer = 30;
    }
    if (pieceType.equals("S")) {
        if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
    } else if (pieceType.equals("T")) {
        if (villageCounter + 1 > numberOfVillagesPerPlayer) return false;
    }
    ```
    - A few parts of code could be moved outside of the loop to improve efficiency and speed. For example the above snippet could be moved to the part of the code where numberOfPlayer is set. 

- Possible errors
  - `numberOfSettlersPerPlayer -= 10;` (and the `-5` version) could create the incorrect number as this is inside a loop. This could be avoided by setting the variable to a constant value instead of subtracting 10 each time.