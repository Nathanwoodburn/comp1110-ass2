package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
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

import java.util.Objects;

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
    int AI;

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
     * @throws Exception if there is an error
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Set some variables and create the scene
        AI = 0;
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
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(
                Game.class.getResourceAsStream("favicon.png"))));
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
        String DEFAULT_GAME = GameData.DEFAULT_GAME;
        switch (game_selected) {
            case 1 -> currentGame = new State(GameData.WHEELS_GAME);
            case 2 -> currentGame = new State(GameData.FACE_GAME);
            case 3 -> currentGame = new State(GameData.SIDES_GAME);
            case 4 -> currentGame = new State(GameData.SPACE_INVADERS_GAME);
            default -> currentGame = new State(DEFAULT_GAME);
        }

        // Add additional players as needed
        switch (numPlayers) {
            case 3 -> currentGame.addPlayer();
            case 4 -> {
                currentGame.addPlayer();
                currentGame.addPlayer();
            }
            default -> {
            }
        }

        if (AI >= numPlayers){
            for (int i = 0; i < numPlayers; i++){
                currentGame.getPlayer(i).setAI(true);
            }
        } else if (AI > 0) {
            for (int i = 0; i < AI; i++){
                currentGame.getPlayer(i+1).setAI(true);
            }
        }

        // Distribute resources
        currentGame.distributeResources();

        // Send intro message
        message = "Welcome to Blue Lagoon\nYou have started a new game for " + numPlayers + " players.";
        if (AI != 0) message += "\nAI is playing";
        sendMessage(message);
        // Refresh the GUI (render the game)
        refresh();

        // Play the game if all players are AI
        if (AI >= numPlayers){
            AIGame();
        }
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
                StringBuilder message = new StringBuilder();
                if (currentGame.isStone(lastMove)){
                    for (Resource resource : currentGame.getResources()) {
                        if (resource.getCoord().equals(lastMove) ) {
                            message = new StringBuilder("Player " + currentGame.getCurrentPlayerID() + " picked up a " + resource.getTypeString().toLowerCase());
                        }
                    }
                }

                // Go to the next player and if it is an AI, do a move for it
                currentGame.nextPlayer();
                selectedTile = new Coord(-1,-1);
                while (currentGame.getCurrentPlayer().isAI()) {
                    message.append("\n").append(doAIMove());
                }

                // Send the message to the user
                sendMessage(message.toString());
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
                StringBuilder AI = new StringBuilder();
                while (currentGame.getCurrentPlayer().isAI()) {
                    AI.append("\n").append(doAIMove());
                }
                sendMessage("Next phase!\n" + AI);
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
                            message = "AI "+ player.getPlayerID() + " picked up " + resource.getTypeString().toLowerCase();
                        }
                    }
                }
                else {
                    message = "AI " + player.getPlayerID() + " placed at " + lastMove.toString();
                }
            }
        }
        if (currentGame.isPhaseOver()){
            currentGame.scorePhase();
            if (currentGame.getCurrentPhase() == 'E') {
                currentGame.cleanBoard();
                currentGame.distributeResources();
                currentGame.nextPhase();
                StringBuilder AI = new StringBuilder("Next phase!\n");
                while (currentGame.getCurrentPlayer().isAI()) {
                    AI.append("\n").append(doAIMove());
                }
                message = AI.toString();
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
     * @param button the button that was clicked
     */

    private void tileClick(String coordString, MouseButton button){
        int y = Integer.parseInt(coordString.split(",")[0]);
        int x = Integer.parseInt(coordString.split(",")[1]);

        selectedTile = new Coord(y,x);

        if (button == MouseButton.PRIMARY) doMove(0);
        else if (button == MouseButton.SECONDARY) doMove(1);

        selectedTile = new Coord(-1,-1);
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
        String currentPhase = switch (currentPhaseChar) {
            case 'E' -> "Exploration";
            case 'S' -> "Settlement";
            default -> "";
        };

        // Making the Current State Statement text on the window
        Text currentStateText = new Text();
        currentStateText.setText("The current player to move is player " +
                playerId + "\nCurrent Phase: " + currentPhase);
        currentStateText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 20));
        currentStateText.setX((double) WINDOW_WIDTH / 2 + ((double) WINDOW_WIDTH /5) - 175);
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
        StringBuilder playerData = new StringBuilder("Scores:");
        // For each player add their settlements and roads
        for (int i = 0; i < currentGame.getNumPlayers(); i++){
            Player currentPlayer = currentGame.getPlayer(i);
            // Add the player's score to the playerData string
            if (currentPlayer.isAI()) playerData.append("\nAI ").append(i).append(": ").append(currentPlayer.getScore());
            else playerData.append("\nPlayer ").append(i).append(": ").append(currentPlayer.getScore());

            // Settler tile generator
            for (Coord c: currentPlayer.getSettlers()){
                // Tile generator
                addStoneTileToBoard(viewerGrid, tileSize, c.toString(), Color.PINK);

                // Label generator
                if (currentPlayer.isAI())
                    addLabelToTile(viewerGrid, tileSize, c.toString(), Color.GREEN, "AI "+i);
                else
                    addLabelToTile(viewerGrid, tileSize, c.toString(), Color.BLACK, "P "+i);
            }
            // Village tile generator
            for (Coord c: currentPlayer.getVillages()){
                // Tile generator
                addStoneTileToBoard(viewerGrid, tileSize, c.toString(), Color. LIGHTGOLDENRODYELLOW);

                // Label generator
                if (currentPlayer.isAI())
                    addLabelToTile(viewerGrid, tileSize, c.toString(), Color.BLACK, "AI "+i);
                else
                    addLabelToTile(viewerGrid, tileSize, c.toString(), Color.BLACK, "P "+i);
            }
        }
        // Adding the player Statement Text to the window
        Text playerStateText = new Text();
        playerStateText.setText(playerData.toString());
        playerStateText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 25));
        playerStateText.setX(0);
        playerStateText.setY(100);
        playerStateText.setFill(Color.BLACK);
        root.getChildren().add(playerStateText);

        // Add the grid to the root
        viewerGrid.relocate(((double) WINDOW_WIDTH /2-viewerGrid.getPrefWidth()/2) + ((double) WINDOW_WIDTH /5),
                ((double) (WINDOW_HEIGHT + 100) /2-viewerGrid.getPrefHeight()/2));
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
        Label newLabel = new Label("Start New Game:");
        Button twoPlayer = new Button("2 Player");
        Button threePlayer = new Button("3 Player");
        Button fourPlayer = new Button("4 Player");
        Label mapLabel = new Label("Select Map:");
        Label aiLabel = new Label("How many AI players:");

        // Numeric select for AI
        ComboBox aiSelector = new ComboBox();
        aiSelector.getItems().add("0");
        aiSelector.getItems().add("1");
        aiSelector.getItems().add("2");
        aiSelector.getItems().add("3");
        aiSelector.getItems().add("4");
        aiSelector.setValue("0");

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
                switch (mapSelector.getValue().toString()) {
                    case "Default" -> game_selected = 0;
                    case "Wheels" -> game_selected = 1;
                    case "Face" -> game_selected = 2;
                    case "Sides" -> game_selected = 3;
                    case "Space Invaders" -> game_selected = 4;
                    default -> game_selected = 0;
                }
            }
        });

        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = Integer.parseInt(aiSelector.getValue().toString());
                newGame(2);
            }
        });

        threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = Integer.parseInt(aiSelector.getValue().toString());
                newGame(3);
            }
        });
        fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AI = Integer.parseInt(aiSelector.getValue().toString());
                newGame(4);
            }
        });

        HBox hb = new HBox();
        hb.getChildren().addAll(mapLabel,mapSelector,aiLabel,aiSelector,newLabel, twoPlayer,threePlayer,fourPlayer);
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
        if (Integer.parseInt(coords[0]) % 2 == 0) hex.setTranslateX((double) tileSize /2);

        // Translate the whole tile's Y axis downwards so they connect and there's no gap
        hex.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);
        board.add(hex, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));

        // Add a mouse click event to the tile
        hex.setOnMouseClicked(event -> {
            tileClick(coordString,event.getButton());
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
        if (Integer.parseInt(coords[0]) % 2 == 0) hex.setTranslateX((double) tileSize /2);
        // Translate the whole tile's Y axis downwards so they connect and there's no gap
        hex.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Translate the tile so they look center
        hex.setTranslateX(7 + hex.getTranslateX());
        board.add(hex, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));

        // Add a mouse click event to the tile
        hex.setOnMouseClicked(event -> {
            tileClick(coordString,event.getButton());
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
        if (Integer.parseInt(coords[0]) % 2 == 0) newLabel.setTranslateX((double) tileSize /2);
        newLabel.setTranslateY(Integer.parseInt(coords[0]) * -0.25 * tileSize);

        // Making the label center
        newLabel.setTranslateX(18.5 + newLabel.getTranslateX());
        board.add(newLabel, Integer.parseInt(coords[1]), Integer.parseInt(coords[0]));

        // Add a mouse click event to the tile
        newLabel.setOnMouseClicked(event -> {
            tileClick(coordString,event.getButton());
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
            newLabel.setTranslateX((double) tileSize /2 + width);
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
            tileClick(coordString,event.getButton());
        });
    }

    /**
     * A hexagon shape with a given side length and fill.
     * Used to create the tiles on the board.
     */
    static class Hexagon extends Polygon {
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
