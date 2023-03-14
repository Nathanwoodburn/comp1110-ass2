# Simple draft skeleton:


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