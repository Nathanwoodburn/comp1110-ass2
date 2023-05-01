package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Game extends Application {

    // region Variables
    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private final Group controls = new Group();

    State currentGame;
    String message;
    Boolean messageError;

    Coord selectedTile;

    Boolean AI;

    // endregion


    @Override
    public void start(Stage stage) throws Exception {
        AI = false;
        selectedTile = new Coord(-1,-1);
        message = "Waiting for Move...";
        messageError = false;
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // From Viewer

        root.getChildren().add(controls);
        makeControls();


        stage.setScene(scene);
        stage.setTitle("Blue Lagoon");
        stage.getIcons().add(new javafx.scene.image.Image(Game.class.getResourceAsStream("favicon.png")));
        stage.setResizable(false);
        stage.show();
        newGame(2);

    }


    /**
     * Start a new game with the given number of players
     * @param numPlayers the number of players
     */
    private void newGame(int numPlayers) {
        currentGame = new State("a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;");
        switch (numPlayers){
            case 3:
                currentGame.addPlayer();
                break;
            case 4:
                currentGame.addPlayer();
                currentGame.addPlayer();
                break;
            default:
                break;
        }
        currentGame.distributeResources();
        refresh();

    }
    
    
    // region Game Play

    /**
     * Do a move of the given type
     * @param type the type of move 0 = place settler, 1 = place village
     */
    void doMove(int type){
        if (selectedTile.equals(new Coord(-1,-1))){
            sendMessage("No tile selected");
            return;
        }
        if (currentGame.isPhaseOver()){
            sendMessage("Phase is over");
            return;
        }
        else {
            char typeC = 'S';
            if (type == 1){
                typeC = 'T';
                if (currentGame.getCurrentPlayer().getVillages().length >= 5){
                    sendMessage("You have placed all your villages");
                    return;
                }
            }



            if (currentGame.isMoveValid(selectedTile,typeC)){
                currentGame.placePiece(selectedTile,typeC);
                Player player = currentGame.getCurrentPlayer();
                Coord lastMove = player.getLastMove();
                String message = "";
                if (currentGame.isStone(lastMove)){
                    for (Resource resource : currentGame.getResources()) {
                        if (resource.getCoord().equals(lastMove) ) {
                            message = "Player " + player.getPlayerID() +" picked up a " + resource.getTypeString().toLowerCase();
                        }
                    }
                }
                currentGame.nextPlayer();
                selectedTile = new Coord(-1,-1);
                if (AI) {
                    String AI = doAIMove();
                    sendMessage(message + "\n" + AI);
                }
                else {
                    sendMessage(message);
                }
            }
            else {
                sendMessage("Invalid move",true);
            }
        }

        if (currentGame.isPhaseOver()){
            sendMessage("Starting next phase");
            currentGame.scorePhase();
            if (currentGame.getCurrentPhase() == 'E') {
                currentGame.cleanBoard();
                currentGame.distributeResources();
            }
            else {
                sendMessage("Game over");
            }
        }

        refresh();
    }

    /**
     * Do an AI move for the current player
     * @return the message to be displayed
     */
    String doAIMove(){
        String message = "";
        if (!currentGame.isPhaseOver()){
            Player player = currentGame.getCurrentPlayer();
            player.doAIMove(currentGame);

            if (currentGame.isPhaseOver()){
                message = "Starting next phase";
            }
            if (!player.getLastMove().equals(new Coord(-1,-1))){
                Coord lastMove = player.getLastMove();
                if (currentGame.isStone(lastMove)){
                    for (Resource resource : currentGame.getResources()) {
                        if (resource.getCoord().equals(lastMove) ) {
                            message = "AI picked up a " + resource.getTypeString().toLowerCase();
                        }
                    }
                }
                else {
                    message = "AI placed at " + lastMove.toString();
                }
            }
        }
        if (currentGame.isPhaseOver()){
            currentGame.scorePhase();
            if (currentGame.getCurrentPhase() == 'E') {
                currentGame.cleanBoard();
                currentGame.distributeResources();
            }
            else {
                message = "Game over";
            }
        }
        refresh();
        return message;
    }
    // endregion

    // region Display

    /**
     * Send a message to the user without error
     * @param message the message to send
     */
    void sendMessage(String message){
        sendMessage(message, false);
    }

    /**
     * Send a message to the user
     * @param message the message to send
     * @param error if the message is an error
     */
    void sendMessage(String message, boolean error){        
        this.message = message;
        messageError = error;
        refresh();
    }

    void tileClick(String coordString){

        int y = Integer.parseInt(coordString.split(",")[0]);
        int x = Integer.parseInt(coordString.split(",")[1]);

        selectedTile = new Coord(y,x);
        sendMessage("Tile " + selectedTile.toString() + " selected");
        refresh();
    }

    void refresh() {
        // When refreshing, it clears the whole thing and update it
        root.getChildren().clear();
        root.getChildren().add(controls);

        // Add the message
        Label messageLabel = new Label(message);
        messageLabel.setLayoutX(0);
        messageLabel.setLayoutY(250);
        messageLabel.setFont(Font.font("Sans Serif",FontWeight.BOLD, 20));
        if (messageError){
            messageLabel.setTextFill(Color.RED);
        }
        else {
            messageLabel.setTextFill(Color.BLACK);
        }

        root.getChildren().add(messageLabel);

        // Initialization of the grid
        GridPane viewerGrid = new GridPane();
        int boardHeight = 0;
        int boardHeightPx = 0;
        int tileSize = 0;

        // Set up the board height value and the tile size
        boardHeight = currentGame.getBoardHeight();
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

        // Get current stage
        int playerId = currentGame.getCurrentPlayerID();

        // Getting the two mode of current state either Exploration
        // or settler
        char currentPhaseChar = currentGame.getCurrentPhase();
        String currentPhase = "";
        switch (currentPhaseChar) {
            case 'E':
                currentPhase = "Exploration";
                break;
            case 'S':
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
        currentStateText.setX(WINDOW_WIDTH / boardHeight * 3.5);
        currentStateText.setY(50);
        currentStateText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(currentStateText);
        currentStateText.setFill(Color.GREEN);

        // For each island render the island
        for (Island island: currentGame.getIslands()){
            for (Coord c: island.getCoords()){
                addBoardTile(viewerGrid, tileSize, c.toString(), Color.GREEN);
            }
        }

        // Render stone circles
        for (Coord stoneCircle: currentGame.getStones()){
            addStoneTileToBoard(viewerGrid, tileSize, stoneCircle.toString(), Color.GRAY);
        }

        String playerData = "Scores:";
        // For each player add their settlements and roads
        for (int i = 0; i < currentGame.getNumPlayers(); i++){
            Player currentPlayer = currentGame.getPlayer(i);
            // Add the player's score to the playerData string
            if (AI && i == 1) playerData += "\nAI: " + currentPlayer.getScore();
            else playerData += "\nPlayer " + i + ": " + currentPlayer.getScore();

            // Settler tile generator
            for (Coord c: currentPlayer.getSettlers()){
                // Tile generator
                addStoneTileToBoard(viewerGrid, tileSize, c.toString(), Color.PINK);

                // Label generator
                addLabelToTile(viewerGrid, tileSize, c.toString(), Color.BLACK, "P"+i);
            }
            // Village tile generator
            for (Coord c: currentPlayer.getVillages()){
                // Tile generator
                addStoneTileToBoard(viewerGrid, tileSize, c.toString(), Color. LIGHTGOLDENRODYELLOW);

                // Label generator
                addLabelToTile(viewerGrid, tileSize, c.toString(), Color.BLACK, "P"+i);
            }
        }
        // Adding the player Statement Text to the window
        Text playerStateText = new Text();
        playerStateText.setText(playerData);
        playerStateText.setFont(Font.font("Sans Serif",
                FontWeight.BOLD, 15));
        playerStateText.setX(0);
        playerStateText.setY(100);
        playerStateText.setFill(Color.BLACK);
        root.getChildren().add(playerStateText);

        // Relocate the grid to be more center
        viewerGrid.relocate((WINDOW_WIDTH/2-viewerGrid.getPrefWidth()/2) + (WINDOW_WIDTH/10), ((WINDOW_HEIGHT+100)/2-viewerGrid.getPrefHeight()/2));
        root.getChildren().add(viewerGrid);

        // Add selected tile
        if (!selectedTile.equals(new Coord(-1,-1))){
            addTileSelector(viewerGrid, tileSize, selectedTile.toString());
        }

        // Move buttons to front
        controls.toFront();
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label newLabel = new Label("New Game:");
        Button twoPlayer = new Button("2 Player");
        Button twoPlayerAI = new Button("2 Player with AI");
        Button threePlayer = new Button("3 Player");
        Button fourPlayer = new Button("4 Player");
        Button placeVillage = new Button("Place Village");
        Button placeSettler = new Button("Place Settler");


        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = false;
                newGame(2);
            }
        });

        twoPlayerAI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = true;
                newGame(2);
            }
        });

        threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = false;
                newGame(3);
            }
        });
        fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = false;
                newGame(4);
            }
        });

        placeVillage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                doMove(1);
            }
        });

        placeSettler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                doMove(0);
            }
        });

        HBox hb = new HBox();
        hb.getChildren().addAll(newLabel, twoPlayer,twoPlayerAI,threePlayer,fourPlayer,placeVillage,placeSettler);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(WINDOW_HEIGHT - 50);
        controls.getChildren().add(hb);
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

        // Add a mouse click event to the tile
        hex.setOnMouseClicked(event -> {
            tileClick(coordString);
        });

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

        // Add a mouse click event to the tile
        hex.setOnMouseClicked(event -> {
            tileClick(coordString);
        });
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

        // Add a mouse click event to the tile
        newLabel.setOnMouseClicked(event -> {
            tileClick(coordString);
        });
    }
    void addTileSelector(GridPane board, int tileSize, String coordString){
        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Label newLabel = new Label("X");
        newLabel.setFont(Font.font("Sans Serif", 20));

        // Following the tile's pos format
        if (Integer.parseInt(coords[0]) % 2 == 0) newLabel.setTranslateX(tileSize/2);
        newLabel.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Making the label center
        newLabel.setTranslateX(17 + newLabel.getTranslateX());
        newLabel.setTextFill(Color.RED);

        board.add(newLabel, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
        // Add a mouse click event to the tile
        newLabel.setOnMouseClicked(event -> {
            tileClick(coordString);
        });
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
    // endregion
}
