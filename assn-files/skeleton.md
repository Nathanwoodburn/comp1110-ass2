# Simple draft skeleton:


# Game play flow
1. Game Setup
2. Phase Setup
3. While phase isn't over do phase
4. Score
5. Phase Setup
6. While phase isn't over do phase
7. Score
8. Game End


## Game Setup
* Create player data (maybe using enums)
## Phase Setup
* Clear board
* Create a grid of tiles
* Create islands and assign size and location (on tiles)
* Create stone circles
* Assign villages to players
* Assign settlers to players
* Randomly Assign resources and statuettes to stone circles

## Phase
* Advance to next player
* Player places piece (use function piece plaging)
## Piece Placing
* Check placement (using function)
* Assign piece to tile
* If piece on stone circle, get resources and statuettes
## Valid Placement (bool)
* If piece is settler, if either of:
  * on unoccupied water
  * on unoccupied land adjacent to one of their pieces
* If piece is village, if both:
  * on unoccupied land
  * is adjacent to one of their pieces
## Phase over (bool)
If either of:
* All resources (not including statuettes) have been collected
* No player has any remaining moves available
## Scoring
* Island Scoring
  * If player has pieces on > 7 islands, score 20 points
  * If player has pieces on 7 islands, score 10 points
  * Otherwise, score 0 points
* Links Scoring
  * Score 5 points per island in players largest link
* Majorities Scoring
  * Player with most pieces on each island type scores points assigned to that island
  * Ties are distributed evenly rounding down
* Resources Scoring
  * For each player
    * For each resource
      * If >3 of resource, score 20 points
      * If 3 of resource, score 10 points
      * If 2 of resource, score 5 points
    * If player has all 4 resources, score 10 points
## Statuettes Scoring
* Each statuette is worth 4 points


# Functions
* Setup Functions
  * Clear Board
  * Board Setup
  * Player Setup
* Game Functions
  * Player Turn
  * Place Piece
  * Claim Stone Circle
  * Valid placement (bool)
  * Phase Over (bool)

* Scoring Functions
  * Island Scoring
  * Links Scoring
  * Majorities Scoring
  * Resources Scoring
  * Statuettes Scoring
# Objects

## Board
* Islands (List of Islands)
* Stone Circles (List of Stone Circles)
## Island
* Size (x,y)
* Location (x,y)
* Point value (int)
* Tiles (List of Tiles in an grid)
## Tile
* Location (x,y)
* Type (Land, Water)
* Pieces (List of Pieces, set during game)
## Piece
* Type (Settler, Village)
* Owner (Player)
* Placed on (Tile)

## Stone Circle
* Location (x,y)
* Resources (List of Resources)
* Number of Statuettes (int)
* Claimed (bool, set during game)

## Player
* Name (string)
* Age (int)
* Score (int, set during game)
* Pieces (List of Pieces, set during game)