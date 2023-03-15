# Simple draft skeleton:


# Overview
## Board Setup
* Create a grid of tiles
* Create islands and assign size and location (on tiles)
* Create "stone circles" 
  
## On Game start functions
* Create player data (maybe using enums)
* Assign villages to players
* Assign settlers to players
* Randomly Assign resources and statuettes to map "stone circles" 

## Exploration Phase
1. Pick player
2. Player places piece (use function to check placement rules)
3. Repeat with next player (while not Exploration Phase over) 

## Check Placement
If piece is settler, check if one of:
* on unoccupied water
* on unoccupied land adjacent to one of their pieces

If piece is village, check on unoccupied land adjacent to one of their pieces

## Piece Placing
* Check placement (using function)
* Assign piece to tile
* If piece on "stone circle" > get resources and statuettes 

## Exploration Phase over
If one of:
* All resources (not including statuettes) have been collected
* No player has any remaining moves available



# Scoring Exploration Phase

## Island Scoring
* If player has pieces on > 7 islands, score 20 points
* If player has pieces on 7 islands, score 10 points
* Otherwise, score 0 points

## Links Scoring
1. For each player get largest link
2. Score 5 points per island in largest link

## Majorities Scoring
* Player with most pieces on each island type scores points assigned to that island
* Ties are distributed evenly rounding down

## Resources Scoring
* For each player
  * For each resource
    * If >3 of resource, score 20 points
    * If 3 of resource, score 10 points
    * If 2 of resource, score 5 points
  * If player has all 4 resources, score 10 points

## Statuettes Scoring
* Each statuette is worth 4 points

# Classes/Fuctions

## Setup board
## Create player (requires input data, probably from player number and colour)
## Exploration Phase
## Check Placement (requires input data, probably piece annd returns bool)
## Place Piece (requires input data, probably from player and piece type)
## Exploration Phase over (bool)


# Objects

## Piece
* Owner (Player)
* Type (Settler, Village)
* Placed on (Tile)

## Tile
* Location (x,y)
* Type (Land, Water)
* Pieces (List of Pieces)

## Island
* Size (x,y)
* Location (x,y)
* Point value (int)
* Tiles (List of Tiles)

## Stone Circle
* Location (x,y)
* Resources (List of Resources)
* Statuettes (List of Statuettes)
* Claimed (bool)

## Player
* Name (string)
* Age (int)
* Score (int)

## Board
* Islands (List of Islands)
* Stone Circles (List of Stone Circles)