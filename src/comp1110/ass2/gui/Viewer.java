package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Paint;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField stateTextField;


    /**
     * Given a state string, draw a representation of the state
     * on the screen.
     * <p>
     * This may prove useful for debugging complex states.
     *
     * @param stateString a string representing a game state
     *                    The combined game state is made up of the following in order:
     *
     * 1 Game Arrangement Statement
     * 1 Current State Statement
     * Many Island Statements (as many as there are Islands on the map) - sorted ascending numerically by the island bonus (ties broken by numerically ascending coordinates)
     * 1 Stones Statement
     * 1 Unclaimed Resources Statement
     * Many Player Statements (as many as there are Players) - sorted ascending numerically by player number
     * Formally this is:
     *
     * gameState = gameArrangementStatement, " ", currentStateStatement, {" ", islandStatement}, " ", stonesStatement, " ", unclaimedResourcesAndStatuettesStatement, {" ", playerStatement}
     *
    a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;
     *
     * ^ The initial board. Two players, player 0 to start.
     *
     *                    [Game statement]
     *                    gameArrangementStatement = "a ", boardHeight, " ", numPlayers, ";"
     *
     * where boardHeight and numPlayers are both positive integers.
     *
     * e.g. "a 13 2;"
     *
     * ^ The standard map layout - 13 high, 2 players
     *
     *                    [Current State]
     *                    currentStateStatement = "c ", playerId, " ", phase, ";"
     *
     * phase = "E" | "S"
     *
     * and where playerId is a non-negative integer that represents the ID of the current player whose turn it is.
     *
     * e.g. "c 0 E;"
     *
     * ^ The current player to move is player 0 in the Exploration phase
     *
     *                    [Many Island Statements]
     *                    islandStatement = "i ", bonus, {" ", coordinate}, ";"
     *
     * where bonus is a non-negative integer.
     *
     * e.g. "i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1;"
     *
     * ^ The first island (top left) of the standard map
     *
     * e.g. "i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8;"
     *
     * ^ The second island (top middle) of the standard map
     *
     * e.g. "i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2;"
     *
     * ^ A sequence of three island statements appearing in the standard game string
     *
     *                    [Stones Statement]
     *                    There will always be exactly 32 stone circles. Coordinates are sorted in numerically ascending order. hint: parse all island statements before the stones statement
     *
     * stonesStatement = "s", {" ", coordinate}, ";"
     *
     * e.g. "s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11;"
     *
     * ^ The stone circles on the base map
     *
     *                    [Unclaimed Resource statement]
     *                    unclaimedResourcesAndStatuettesStatement = "r C", {" ", coordinate}, " B", {" ", coordinate}, " W", {" ", coordinate}, " P", {" ", coordinate}, " S", {" ", coordinate}, ";"
     *
     * e.g. "r C 1,1 B 1,2 W P 1,4 S;"
     *
     * ^ Coconut at 1,1, Bamboo at 1,2, Precious Stone at 1,4. No Water or Statuettes
     *
     *                    [Player Statement]
     *                    playerStatement = "p ", playerId, " ", score, " ", coconut, " ", bamboo, " ", water, " ", preciousStone, " ", statuette, " S", {" ", coordinate}, " T", {" ", coordinate}, ";"
     *
     * where coconut, bamboo, water, preciousStone, statuette are non-negative integers representing the number of each resource or statuettes the player has collected during this phase. score is the total score of the player.
     *
     * e.g. "p 1 42 1 2 3 4 5 S 5,6 8,7 T 1,2;"
     *
     *                    private static final int VIEWER_WIDTH = 1200;
     *     private static final int VIEWER_HEIGHT = 700;
     *
     * ^ player 1 with score: 42, coconuts: 1, bamboo: 2, water: 3, precious stone: 4, statuettes: 5, placed settlers at 5,6 and 8,7, placed villages at 1,2
     *
     *   a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7
     */



    void displayState(String stateString) {
        // When refreshing, it clears the whole thing and update it
        root.getChildren().clear();
        root.getChildren().add(controls);

        // Intizialiation of the grid
        GridPane viewerGrid = new GridPane();
        String[] parts = stateString.split("; ?");
        int boardHeight = 0;
        int boardHeightPx = 0;
        int tileSize = 0;

        for ( String part : parts) {
            String[] parseSplit = part.split(" ");
            String stateCases = parseSplit[0];

            // Splitting of the states of the game as per the encoded string
            switch (stateCases) {

                // Game Arrangement Statement
                case "a":

                    // Settting up the board height value and the tile size
                    boardHeight = Integer.parseInt(parseSplit[1]);
                    boardHeightPx = 650;
                    tileSize = boardHeightPx / boardHeight;
                    viewerGrid.setPrefWidth(boardHeightPx);
                    viewerGrid.setPrefHeight(boardHeightPx);

                    // Generating the water tiles ( the background water map )
                    for(int i = 0; i < boardHeight; i++){
                        for(int j = 0; j < boardHeight - (-1 * i % 2 + 1); j++){
                            addBoardTile(viewerGrid, boardHeightPx/boardHeight,
                                    String.format("%s,%s", i, j), Color.DARKBLUE);
                        }
                    }
                    break;

                    // Current state statement
                case "c":

                    // Getting the player ID from the string
                    int playerId = Integer.parseInt(parseSplit[1]);

                    // Getting the two mode of current state either Exploration
                    // or settler
                    String currentPhase = parseSplit[2];
                    switch (currentPhase) {
                        case "E":
                            currentPhase = "Exploration";
                            break;
                        case "S":
                            currentPhase = "Settler";
                            break;
                    }

                    // Making the Current State Statement text on the window
                    Text currentStateText = new Text();
                    currentStateText.setText("The current player to move is player " +
                            playerId + " in the " + currentPhase +
                            " phase");
                    currentStateText.setFont(Font.font("Sans Serif",
                            FontWeight.BOLD, 20));
                    currentStateText.setX(VIEWER_WIDTH / boardHeight * 3.5);
                    currentStateText.setY(50);
                    currentStateText.setTextAlignment(TextAlignment.CENTER);
                    root.getChildren().add(currentStateText);
                    currentStateText.setFill(Color.GREEN);
                    break;

                    // Generating the Island tiles from the Island State Statement
                case "i":
                    for(int i = 2; i < parseSplit.length; i++){
                        addBoardTile(viewerGrid, tileSize, parseSplit[i], Color.GREEN);
                    }
                    break;

                    // Generating the Stone Tiles from the Stone Statement
                case "s":
                    for(int i = 1; i < parseSplit.length; i++){
                        addStoneTileToBoard(viewerGrid, tileSize, parseSplit[i], Color.GRAY);
                    }
                    break;

                    // Generating the Resources Tiles from the Resource Statement
                case "r":
                    for(int i = 1; i < parseSplit.length; i++){
                        switch(parseSplit[i]) {

                            // Generating Resource: Coconut Tiles
                            case "C":
                                i++; // To Skip the "C" itself and go to the numbers in the string
                                while(!parseSplit[i].equals("B")){

                                    // Tile generation
                                    addTileToBoard(viewerGrid, tileSize, parseSplit[i], Color.BROWN);

                                    // Label for the tiles generation
                                    addLabelToTile(viewerGrid,tileSize,parseSplit[i], Color.WHITE, "C");
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                    // i stop at the letter "B"
                                break;

                                // Generating Resource: Bamboo Tiles
                            case "B":
                                i++; // To Skip the "B" itself and go to the numbers in the string
                                while(!parseSplit[i].equals("W")){

                                    // Tile generation
                                    addTileToBoard(viewerGrid, tileSize, parseSplit[i], Color.YELLOW);

                                    // Label for the tiles generation
                                    addLabelToTile(viewerGrid,tileSize,parseSplit[i], Color.BLACK, "B");
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "W"
                                break;

                                // Generating Resource: Water tiles
                            case "W":
                                i++; // To Skip the "W" itself and go to the numbers in the string
                                while(!parseSplit[i].equals("P")){
                                    // Tile Generation
                                    addTileToBoard(viewerGrid,tileSize, parseSplit[i], Color.CYAN);

                                    // Label for the tiles generation
                                    addLabelToTile(viewerGrid,tileSize,parseSplit[i], Color.BLACK, "W");
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "P"
                                break;

                                // Generating Resource: Precious Stone tiles
                            case "P":
                                i++; // To Skip the "P" itself and go to the numbers in the string
                                while(!parseSplit[i].equals("S")){

                                    // Tile Generation
                                    addTileToBoard(viewerGrid,tileSize,parseSplit[i], Color.GOLD);

                                    // Label for the tiles generation
                                    addLabelToTile(viewerGrid, tileSize, parseSplit[i], Color.BLACK, "P");
                                    i++;  // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "S"
                                break;

                                // Generating Resource: Statuettes tiles
                            case "S":
                                i++; // To Skip the "P" itself and go to the numbers in the string
                                while(i < parseSplit.length){

                                    // Tile Generation
                                    addTileToBoard(viewerGrid, tileSize, parseSplit[i], Color.SILVER);

                                    // Label for the tiles generation
                                    addLabelToTile(viewerGrid, tileSize, parseSplit[i], Color.BLACK, "S");
                                    i++; // To continue the iteration for the while loops
                                }
                                break;
                        }
                    }
                    break;

                    // Player Statement
                case "p":

                    // A container for the String that is printed at the end
                    String container = "";
                    container += "player " + parseSplit[1] + " with score: " +
                            parseSplit[2] + ", coconuts: " + parseSplit[3] +
                            ", bambooo: " + parseSplit[4] + ", water: " +
                            parseSplit[5] + ", precious stone: " + parseSplit[6] +
                            ", statuettes: " + parseSplit[7] + ", placed settlers at ";

                    // Setller tile generator
                    int i = 9;
                    int counter = 0;
                        while(!parseSplit[i].equals("T")){
                            counter++;
                            String[] coords = parseSplit[i].split(",");

                            // Tile generator
                            addStoneTileToBoard(viewerGrid, tileSize, parseSplit[i], Color.PINK);

                            // Label generator
                            addSmallLabelToTile(viewerGrid, tileSize, parseSplit[i], Color.BLACK, "Settlers");

                            // if there's more than 1 settler placed
                            if(counter > 1) container += " and ";
                            container += Integer.parseInt(coords[0]) + "," +
                                    Integer.parseInt(coords[1]);
                            i++; // To iterate the while loop
                        }
                        i++; // To continue past the "T" keyword

                        // Village tile generator
                        container += ", placed villages at ";
                        int counterT = 0;
                        while(i < parseSplit.length){
                            counterT++;
                            String[] coords = parseSplit[i].split(",");

                            // Tile generator
                            addStoneTileToBoard(viewerGrid, tileSize, parseSplit[i], Color. LIGHTGOLDENRODYELLOW);

                            // Label generator
                            addSmallLabelToTile(viewerGrid, tileSize, parseSplit[i], Color.BLACK, "Village");

                            // If there's more than 1 village placed
                            if(counterT > 1) container += " and ";
                            container += Integer.parseInt(coords[0]) + "," +
                                    Integer.parseInt(coords[1]);
                            i++;
                        }

                    // Adding the player Statement Text to the window
                    Text playerStateText = new Text();
                    playerStateText.setText(container);
                    playerStateText.setFont(Font.font("Sans Serif",
                            FontWeight.BOLD, 15));
                    playerStateText.setX(125);
                    playerStateText.setY(600);
                    playerStateText.setTextAlignment(TextAlignment.CENTER);
                    root.getChildren().add(playerStateText);
                    playerStateText.setFill(Color.BLACK);
//                    }
            }
        }

        // Relocate the grid to be more center
        viewerGrid.relocate((VIEWER_WIDTH/2-viewerGrid.getPrefWidth()/2), ((VIEWER_HEIGHT+100)/2-viewerGrid.getPrefHeight()/2));
        root.getChildren().add(viewerGrid);
            // FIXME Task 5
        }

        // Generating the big board tiles such as the water tiles and the island tiles
    void addBoardTile(GridPane board, int tileSize, String coordString, Color color) {
        // If the string empty, stop the function
        if (coordString.equals("")) return;

        String[] coords = coordString.split(",");
        Hexagon hex = new Hexagon(tileSize, color);

        // if the row is even, translate the tile to the right by tileSize/2
        if (Integer.parseInt(coords[0]) % 2 == 0) hex.setTranslateX(tileSize/2);

        // Translate the whole tile's Y axis downwards so they connect and there's no gap
        hex.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);
        board.add(hex, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
    }

    // Generating the small tiles such as resources, stones, etc
    void addStoneTileToBoard(GridPane board, int tileSize, String coordString, Color color) {
        int tileSize2 = tileSize;
        tileSize2 -= 15;

        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Hexagon hex = new Hexagon(tileSize2, color);

        // if the row is even, translate the tile to the right by tileSize/2
        if (Integer.parseInt(coords[0]) % 2 == 0) hex.setTranslateX(tileSize/2);
        // Translate the whole tile's Y axis downwards so they connect and there's no gap
        hex.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Translate the tile so they look center
        hex.setTranslateX(7 + hex.getTranslateX());
        board.add(hex, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
    }

    void addTileToBoard(GridPane board, int tileSize, String coordString, Color color) {
        int tileSize2 = tileSize;
        tileSize2 -= 25;

        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Hexagon hex = new Hexagon(tileSize2, color);

        // if the row is even, translate the tile to the right by tileSize/2
        if (Integer.parseInt(coords[0]) % 2 == 0) hex.setTranslateX(tileSize/2);
        // Translate the whole tile's Y axis downwards so they connect and there's no gap
        hex.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Translate the tile so they look center
        hex.setTranslateX(12 + hex.getTranslateX());
        board.add(hex, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
    }

    // Adding labels to the resources tiles and stones tiles
    void addLabelToTile(GridPane board, int tileSize, String coordString, Color color, String labelName){
        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Label newLabel = new Label(labelName);
        newLabel.setTextFill(color);
        newLabel.setFont(Font.font("Sans Serif", 12));

        // Following the tile's pos format
        if (Integer.parseInt(coords[0]) % 2 == 0) newLabel.setTranslateX(tileSize/2);
        newLabel.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Making the label center
        newLabel.setTranslateX(19.5 + newLabel.getTranslateX());
        board.add(newLabel, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
    }

    // Adding labels to the villagers and settlers
    void addSmallLabelToTile(GridPane board, int tileSize, String coordString, Color color, String labelName){
        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Label newLabel = new Label(labelName);
        newLabel.setTextFill(color);
        newLabel.setFont(Font.font("Sans Serif", 9));

        // Following the tile's pos format
        if (Integer.parseInt(coords[0]) % 2 == 0) newLabel.setTranslateX(tileSize/2);
        newLabel.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Making the label center
        newLabel.setTranslateX(11 + newLabel.getTranslateX());
        board.add(newLabel, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
    }

    // A hexagon nested class shape
    class Hexagon extends Polygon {
        public Hexagon(double side, Paint fill) {
            super(
                    0.0, side / 2.0,
                    side / 2.0, side / 4.0,
                    side / 2.0, -side / 4.0,
                    0.0, -side / 2.0,
                    -side / 2.0, -side / 4.0,
                    -side / 2.0, side / 4.0
            );
            this.setFill(fill);
        }
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label playerLabel = new Label("Game State:");
        stateTextField = new TextField();
        stateTextField.setPrefWidth(200);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                displayState(stateTextField.getText());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(playerLabel, stateTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Blue Lagoon Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();
        primaryStage.getIcons().add(new javafx.scene.image.Image(Viewer.class.getResourceAsStream("favicon.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
