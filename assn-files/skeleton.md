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

