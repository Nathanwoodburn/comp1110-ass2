package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
    private final String DEFAULT_GAME = "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    private final String WHEELS_GAME = "a 13 2; c 0 E; i 5 0,1 0,2 0,3 0,4 1,1 1,5 2,0 2,5 3,0 3,6 4,0 4,5 5,1 5,5 6,1 6,2 6,3 6,4; i 5 0,8 0,9 0,10 1,8 1,11 2,7 2,11 3,8 3,11 4,8 4,9 4,10; i 7 8,8 8,9 8,10 9,8 9,11 10,7 10,11 11,8 11,11 12,8 12,9 12,10; i 7 10,0 10,1 10,4 10,5 11,0 11,2 11,3 11,4 11,6 12,0 12,1 12,4 12,5; i 9 2,2 2,3 3,2 3,4 4,2 4,3; i 9 2,9; i 9 6,6 6,7 6,8 6,9 6,10 6,11 7,6 8,0 8,1 8,2 8,3 8,4 8,5; i 9 10,9; s 0,1 0,4 0,10 2,2 2,3 2,9 2,11 3,0 3,2 3,4 3,6 4,2 4,3 4,10 6,1 6,4 6,6 6,11 8,0 8,5 8,8 8,10 10,0 10,5 10,7 10,9 10,11 11,3 12,1 12,4 12,8 12,10; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    private final String FACE_GAME = "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 0,4 0,5 0,6 0,7 0,8 0,9 0,10 0,11 1,0 1,12 2,0 2,11 3,0 3,12 4,0 4,11 5,0 5,12 6,0 6,11 7,0 7,12 8,0 8,11 9,0 9,12 10,0 10,11 11,0 11,12 12,0 12,1 12,2 12,3 12,4 12,5 12,6 12,7 12,8 12,9 12,10 12,11; i 6 2,4 2,5 2,6 2,7; i 9 4,4 4,5 4,6 4,7; i 9 6,5 6,6 7,5 7,7 8,5 8,6; i 12 2,2 3,2 3,3 4,2 5,2 5,3 6,2 7,2 7,3; i 12 2,9 3,9 3,10 4,9 5,9 5,10 6,9 7,9 7,10; i 12 9,2 9,10 10,2 10,3 10,4 10,5 10,6 10,7 10,8 10,9; s 0,3 0,8 1,0 1,12 2,2 2,4 2,7 2,9 4,2 4,5 4,6 4,9 5,0 5,12 6,2 6,5 6,6 6,9 8,0 8,5 8,6 8,11 9,2 9,10 10,3 10,5 10,6 10,8 11,0 11,12 12,4 12,7; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    private final String SIDES_GAME =  "a 7 2; c 0 E; i 4 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 2,0 2,1 2,2 2,3 3,0 3,1 3,2 3,3 4,0 4,1 4,2 4,3 5,0 5,1 5,2 5,3 6,0 6,1 6,2 6,3; i 20 0,5 1,5 1,6 2,5 3,5 3,6 4,5 5,5 5,6 6,5; s 0,0 0,1 0,2 0,3 1,1 1,2 1,3 1,5 1,6 2,0 2,1 2,2 2,3 3,0 3,1 3,2 3,3 3,5 3,6 4,0 4,1 4,2 4,3 5,1 5,2 5,3 5,5 5,6 6,0 6,1 6,2 6,3; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    private final String SPACE_INVADERS_GAME = "a 23 2; c 0 E; i 6 0,2 0,7 1,3 1,7 2,2 2,3 2,4 2,5 2,6 2,7 3,2 3,4 3,5 3,6 3,8 4,0 4,1 4,2 4,3 4,4 4,5 4,6 4,7 4,8 4,9 5,0 5,1 5,3 5,4 5,5 5,6 5,7 5,9 5,10 6,0 6,2 6,7 6,9 7,3 7,4 7,6 7,7; i 6 0,14 0,19 1,15 1,19 2,14 2,15 2,16 2,17 2,18 2,19 3,14 3,16 3,17 3,18 3,20 4,12 4,13 4,14 4,15 4,16 4,17 4,18 4,19 4,20 4,21 5,12 5,13 5,15 5,16 5,17 5,18 5,19 5,21 5,22 6,12 6,14 6,19 6,21 7,15 7,16 7,18 7,19; i 6 17,9 18,8 18,9 19,6 19,7 19,8 19,9 19,10 19,11 19,12 20,5 20,6 20,7 20,8 20,9 20,10 20,11 20,12 21,5 21,6 21,7 21,8 21,9 21,10 21,11 21,12 21,13 22,5 22,6 22,7 22,8 22,9 22,10 22,11 22,12; i 8 12,3 12,5 13,3 13,4 13,5 13,6 14,1 14,2 14,3 14,4 14,5 15,1 15,2 15,3 16,1 16,2; i 8 12,17 12,18 12,19 13,17 13,18 13,19 13,20 14,17 14,18 14,19 14,20 15,19 15,20 15,21 16,19 16,20; i 8 13,14 14,13 14,14 15,13 15,14 15,15 16,13 16,14; i 8 14,7 15,7 15,8 16,7; i 10 8,9 9,9 10,9 11,9; i 10 8,12 9,13 10,12 11,13; i 10 9,1 10,1 11,1 12,1; i 10 9,22 10,21 11,22 12,21; i 10 13,10 14,10 15,10; i 10 17,0 18,0 19,0 20,0; i 10 17,16 18,16 19,16 20,16; s 0,2 0,7 0,14 0,19 3,5 3,17 6,0 6,9 6,12 6,21 7,4 7,6 7,16 7,18 11,9 11,13 12,1 12,19 12,21 13,10 15,2 15,8 15,14 15,20 17,9 18,8 18,9 20,0 20,16 21,6 21,9 21,12; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    // Store the selected map 0 = default, 1 = wheels, 2 = face, 3 = sides, 4 = space invaders
    private int game_selected = 0;
    Boolean game_over;
    // endregion

    // region Setup

    /**
     * This runs when the program is started. It should initialize the GUI and start the game.
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Set some variables and create the scene
        AI = false;
        selectedTile = new Coord(-1,-1);
        message = "";
        messageError = false;
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Create the control section
        root.getChildren().add(controls);
        makeControls();

        // Set some app properties
        stage.setScene(scene);
        stage.setTitle("Blue Lagoon");
        stage.getIcons().add(new javafx.scene.image.Image(Game.class.getResourceAsStream("favicon.png")));
        stage.setResizable(false);
        stage.show();
        // Create a new game
        newGame(2);

    }


    /**
     * Start a new game with the given number of players
     * @param numPlayers the number of players
     */
    private void newGame(int numPlayers) {
        game_over = false;

        // Get selected map
        switch (game_selected){
            case 1:
                currentGame = new State(WHEELS_GAME);
                break;
            case 2:
                currentGame = new State(FACE_GAME);
                break;
            case 3:
                currentGame = new State(SIDES_GAME);
                break;
            case 4:
                currentGame = new State(SPACE_INVADERS_GAME);
                break;
            default:
                currentGame = new State(DEFAULT_GAME);
                break;
        }

        // Add additional players as needed
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

        // Distribute resources
        currentGame.distributeResources();

        // Send intro message
        message = "Welcome to Blue Lagoon\nYou have started a new game for " + numPlayers + " players.";
        if (AI) message += "\nAI is playing";
        sendMessage(message);

        // Refresh the GUI (render the game)
        refresh();
    }
    // endregion
    // region Game Play

    /**
     * Do a move of the given type. Using the selected tile as the location
     * @param type the type of move 0 = place settler, 1 = place village
     */
    void doMove(int type){
        // Make sure the game is not over and a tile is selected
        if (game_over) return;
        if (selectedTile.equals(new Coord(-1,-1))){
            sendMessage("No tile selected");
            refresh();
            return;
        }

        // If the phase is not over, do the move
        if (!currentGame.isPhaseOver()){
            // Get the type of move
            char typeC = 'S';
            if (type == 1){
                typeC = 'T';
                if (currentGame.getCurrentPlayer().getVillages().length >= 5){
                    sendMessage("You have placed all your villages");
                    return;
                }
            }

            // If the move is valid, do it
            if (currentGame.isMoveValid(selectedTile,typeC)){

                // Place the piece
                currentGame.placePiece(selectedTile,typeC);

                // If the move was a stone, send a message about it
                Coord lastMove = selectedTile;
                String message = "";
                if (currentGame.isStone(lastMove)){
                    for (Resource resource : currentGame.getResources()) {
                        if (resource.getCoord().equals(lastMove) ) {
                            message = "Player " + currentGame.getCurrentPlayerID() +" picked up a " + resource.getTypeString().toLowerCase();
                        }
                    }
                }

                // Go to the next player and if it is an AI, do a move for it
                currentGame.nextPlayer();
                selectedTile = new Coord(-1,-1);
                if (AI && currentGame.getCurrentPlayerID() == 1) {
                    message += "\n"+ doAIMove();
                }

                // Send the message to the user
                sendMessage(message);
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
                if (AI && currentGame.getCurrentPlayerID() == 1){
                    String AI = doAIMove();
                    sendMessage("Next phase!\n" + AI);
                }
            }
            else {
                sendMessage("Game over!",true);
                game_over = true;
            }
        }
        refresh();
    }

    /**
     * Do an AI move for the current player
     * @return the message to be displayed
     */
    String doAIMove(){
        if (game_over) return "GAME OVER";
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
                currentGame.nextPhase();
                if (AI && currentGame.getCurrentPlayerID() == 1){
                    String AI = doAIMove();
                    sendMessage("Next phase!\n" + AI);
                }
            }
            else {
                message = "Game over!";
                game_over = true;
            }
        }
        refresh();
        return message;
    }

    /**
     * Do a full AI game. This is good to visualize the AI
     */
    void AIGame(){
        while (!currentGame.isPhaseOver()){
            currentGame.getCurrentPlayer().doAIMove(currentGame);
        }
        currentGame.scorePhase();
        currentGame.cleanBoard();
        currentGame.distributeResources();
        currentGame.nextPhase();
        while (!currentGame.isPhaseOver()){
            currentGame.getCurrentPlayer().doAIMove(currentGame);
        }
        currentGame.scorePhase();
        sendMessage("Game over!",true);
        game_over = true;
        refresh();

    }
    // endregion

    // region Display

    /**
     * Send a message to the user without error
     * @param message the message to send
     */
    private void sendMessage(String message){
        sendMessage(message, false);
    }

    /**
     * Send a message to the user.
     * If the message is an error it will display as red
     * @param message the message to send
     * @param error if the message is an error
     */
    private void sendMessage(String message, boolean error){
        this.message = message;
        messageError = error;
        refresh();
    }

    /**
     * When a tile is clicked, it will be selected
     * @param coordString the coordinate of the tile
     */

    private void tileClick(String coordString){

        int y = Integer.parseInt(coordString.split(",")[0]);
        int x = Integer.parseInt(coordString.split(",")[1]);

        selectedTile = new Coord(y,x);
        sendMessage("Tile " + selectedTile.toString() + " selected");
        refresh();
    }

    /**
     * Refresh the display
     * It will clear the whole thing and then render it again
     */
    private void refresh() {
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
                currentPhase = "Settlement";
                break;
        }

        // Making the Current State Statement text on the window
        Text currentStateText = new Text();
        currentStateText.setText("The current player to move is player " +
                playerId + "\nCurrent Phase: " + currentPhase);
        currentStateText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 20));
        currentStateText.setX(WINDOW_WIDTH / 2 + (WINDOW_WIDTH/5) - 175);
        currentStateText.setY(25);
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
        playerStateText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 25));
        playerStateText.setX(0);
        playerStateText.setY(100);
        playerStateText.setFill(Color.BLACK);
        root.getChildren().add(playerStateText);

        // Add the grid to the root
        viewerGrid.relocate((WINDOW_WIDTH/2-viewerGrid.getPrefWidth()/2) + (WINDOW_WIDTH/5),
                ((WINDOW_HEIGHT+100)/2-viewerGrid.getPrefHeight()/2));
        root.getChildren().add(viewerGrid);

        // Add selected tile
        if (!selectedTile.equals(new Coord(-1,-1))){
            addTileSelector(viewerGrid, tileSize, selectedTile.toString());
        }

        // Move buttons to front, so they are on top of the grid and can be clicked
        controls.toFront();
    }

    /**
     * Add a control panel to the window
     * It will create the controls variable
     */
    private void makeControls() {
        Label newLabel = new Label("New Game:");
        Button twoPlayer = new Button("2 Player");
        Button threePlayer = new Button("3 Player");
        Button fourPlayer = new Button("4 Player");
        Label mapLabel = new Label("Select Map:");
        Label playLabel = new Label("Place piece:");
        Button placeVillage = new Button("Village");
        Button placeSettler = new Button("Settler");
        CheckBox isAI = new CheckBox("AI");

        // Store the selected map 0 = default, 1 = wheels, 2 = face, 3 = sides, 4 = space invaders
        ComboBox mapSelector = new ComboBox();
        mapSelector.getItems().add("Default");
        mapSelector.getItems().add("Wheels");
        mapSelector.getItems().add("Face");
        mapSelector.getItems().add("Sides");
        mapSelector.getItems().add("Space Invaders");
        mapSelector.setPromptText("Default");

        // Set the map when the map is selected
        mapSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (mapSelector.getValue().toString()){
                    case "Default":
                        game_selected = 0;
                        break;
                    case "Wheels":
                        game_selected = 1;
                        break;
                    case "Face":
                        game_selected = 2;
                        break;
                    case "Sides":
                        game_selected = 3;
                        break;
                    case "Space Invaders":
                        game_selected = 4;
                        break;
                    default:
                        game_selected = 0;
                        break;
                }
            }
        });

        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = isAI.isSelected();
                newGame(2);
            }
        });

        threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = isAI.isSelected();
                newGame(3);
            }
        });
        fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = isAI.isSelected();
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

        // Run AI Game if keypress is a
        isAI.setOnKeyPressed(event ->  {
            if (event.getCode().toString() == "A"){
                    newGame(currentGame.getNumPlayers());
                    AIGame();
            }
        });


        HBox hb = new HBox();
        hb.getChildren().addAll(newLabel, twoPlayer,threePlayer,fourPlayer, isAI,mapLabel,mapSelector,playLabel,placeVillage,placeSettler);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(WINDOW_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    /**
     * Add a tile to the board
     * @param board The board to add the tile to
     * @param tileSize The size of the tile
     * @param coordString The coordinates of the tile
     * @param color The color of the tile
     */
    private void addBoardTile(GridPane board, int tileSize, String coordString, Color color) {
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

    /**
     * Add a tile to the board
     * @param board The board to add the tile to
     * @param tileSize The size of the tile
     * @param coordString The coordinates of the tile
     * @param color The color of the tile
     */
    private void addStoneTileToBoard(GridPane board, int tileSize, String coordString, Color color) {
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

    /**
     * Add a label to the board
     * @param board The board to add the label to
     * @param tileSize The size of the tile
     * @param coordString The coordinates of the tile to add the label to
     * @param color The color of the tile
     * @param labelName The name of the label
     */
    private void addLabelToTile(GridPane board, int tileSize, String coordString, Color color, String labelName){
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

    /**
     * Add a X to mark the tile as selected
     * @param board The board to add the label to
     * @param tileSize The size of the tile
     * @param coordString The coordinates of the tile to add the label to
     */
    private void addTileSelector(GridPane board, int tileSize, String coordString){
        // If the string empty, stop the function
        if (coordString.equals("")) return;
        String[] coords = coordString.split(",");
        Label newLabel = new Label("X");
        int width = 0;
        if (game_selected == 3){
            // Increase the font
            newLabel.setFont(Font.font("Sans Serif", 40));
            width = 30;
        } else if (game_selected == 4){
            // Decrease the font
            newLabel.setFont(Font.font("Sans Serif", 15));
            width = 10;
        } else {
            newLabel.setFont(Font.font("Sans Serif", 25));
            width = 15;
        }

        // Adjust the label's position
        if (Integer.parseInt(coords[0]) % 2 == 0){
            newLabel.setTranslateX(tileSize/2 + width);
        } else
        {
            newLabel.setTranslateX(width);
        }
        newLabel.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);
        // Set the color to red and add it to the board
        newLabel.setTextFill(Color.RED);
        board.add(newLabel, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));
        // Add a mouse click event to the tile
        newLabel.setOnMouseClicked(event -> {
            tileClick(coordString);
        });
    }

    /**
     * A hexagon shape with a given side length and fill.
     * Used to create the tiles on the board.
     */
    class Hexagon extends Polygon {
        /**
         * Create a hexagon with a given side length and fill.
         * @param side double The length of a side of the hexagon.
         * @param fill Paint The fill of the hexagon.
         */
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
