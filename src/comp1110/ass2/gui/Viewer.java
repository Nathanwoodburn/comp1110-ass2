package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

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

        // To group the root and adjust their Px sizes accordingly
        // to each other
        // StackPane rootGroup = new StackPane();

        // a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,
        //  String gameArrangementState = parts[i];
        //                String[] gameArrangementStateParts = gameArrangementState.split(" ");
        //                int boardHeight = Integer.parseInt(gameArrangementStateParts[1]);
        //                int boardHeightPx = boardHeight * 45;

        GridPane viewerGrid = new GridPane();
        String[] parts = stateString.split("; ?");
        int boardHeight = 0;
        int boardHeightPx = 0;
//        int tileSize = 500;
        for ( String part : parts) {
            String[] parseSplit = part.split(" ");
            String stateCases = parseSplit[0];
            switch (stateCases) {
                case "a":
                    boardHeight = Integer.parseInt(parseSplit[1]);
                    boardHeightPx = 575;
                    viewerGrid.setPrefWidth(boardHeightPx);
                    viewerGrid.setPrefHeight(boardHeightPx);
                    // viewerGrid.relocate((VIEWER_WIDTH/2-viewerGrid.getPrefWidth()/2), (VIEWER_HEIGHT/2-viewerGrid.getPrefHeight()/2));

                    // row major order
                    for(int i = 0; i < boardHeight; i++){
                        for(int j = 0; j < boardHeight; j++){
                            Rectangle boardMap = new Rectangle(boardHeightPx/boardHeight,
                                    boardHeightPx/boardHeight, Color.DARKBLUE);
                            viewerGrid.add(boardMap, i, j);
//                            Label boardLabel = new Label(i + "," + j);
//                            boardLabel.setFont(Font.font("Sans Serif", 12));
//                            boardLabel.setTextFill(Color.WHITE);
//                            viewerGrid.add(boardLabel, i, j);
                        }
                    }
                    break;
                case "c":
                    int playerId = Integer.parseInt(parseSplit[1]);
                    String currentPhase = parseSplit[2];
                    switch (currentPhase) {
                        case "E":
                            currentPhase = "Exploration";
                            break;
                        case "S":
                            currentPhase = "Settler";
                            break;
                    }
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
                case "i":
                    for(int i = 2; i < parseSplit.length; i++){
                        Rectangle island = new Rectangle(boardHeightPx/boardHeight,
                                boardHeightPx/boardHeight, Color.GREEN);
                        String[] coords = parseSplit[i].split(",");
                        int xCoord = Integer.parseInt(coords[0]);
                        int yCoord = Integer.parseInt(coords[1]);
//                        Label islandLabel = new Label(xCoord + "," + yCoord +
//                                "\n b " + parseSplit[1]);
//                        islandLabel.setTextFill(Color.GREENYELLOW);
//                        islandLabel.setFont(Font.font("Sans Serif", 12));
//                        viewerGrid.add(islandLabel, xCoord, yCoord);
//                        viewerGrid.setAlignment(Pos.CENTER);
                        viewerGrid.add(island, xCoord, yCoord);

                        // root.getChildren().add(viewerGrid);
                    }
                    break;
                case "s":
                    for(int i = 1; i < parseSplit.length; i++){
                        Rectangle theRock = new Rectangle(boardHeightPx/boardHeight
                                - 15, boardHeightPx/boardHeight - 15, Color.GRAY);
                        String[] coords = parseSplit[i].split(",");
                        theRock.setTranslateX(5);
//                        theRock.setTranslateY(0);
                        viewerGrid.add(theRock, Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[1]));
                    }
                    break;
                case "r":
                    for(int i = 1; i < parseSplit.length; i++){
                        switch(parseSplit[i]) {
                            case "C":
//                                System.out.println("Start of C " + parseSplit[i]);
                                i++;
//                                System.out.println("First iteration of C" + parseSplit[i]);
                                while(!parseSplit[i].equals("B")){
//                                    System.out.println("C loop" + parseSplit[i]);
                                    String[] coords = parseSplit[i].split(",");
                                    Rectangle coconut = new Rectangle(boardHeightPx/boardHeight
                                            - 15, boardHeightPx/boardHeight - 15, Color.BROWN);
                                    coconut.setTranslateX(5);
                                    Label coconutLabel = new Label("C");
                                    coconutLabel.setTextFill(Color.WHITE);
                                    coconutLabel.setFont(Font.font("Sans Serif", 12));
                                    coconutLabel.setTranslateX(15);
                                    viewerGrid.add(coconut, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    viewerGrid.add(coconutLabel, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    i++;
//                                    System.out.println("End of C loop" + parseSplit[i]);
                                }
                                i--;
                                break;
                            case "B":
                                i++;
                                while(!parseSplit[i].equals("W")){
                                    String[] coords = parseSplit[i].split(",");
                                    Rectangle bamboo = new Rectangle(boardHeightPx/boardHeight
                                            - 15, boardHeightPx/boardHeight - 15, Color.YELLOW);
                                    bamboo.setTranslateX(5);
                                    Label bambooLabel = new Label("B");
                                    bambooLabel.setTextFill(Color.BLACK);
                                    bambooLabel.setFont(Font.font("Sans Serif", 12));
                                    bambooLabel.setTranslateX(15);
                                    viewerGrid.add(bamboo, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    viewerGrid.add(bambooLabel, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    i++;
                                }
                                i--;
                                break;
                            case "W":
                                i++;
                                while(!parseSplit[i].equals("P")){
                                    String[] coords = parseSplit[i].split(",");
                                    Rectangle water = new Rectangle(boardHeightPx/boardHeight
                                            - 15, boardHeightPx/boardHeight - 15, Color.CYAN);
                                    water.setTranslateX(5);
                                    Label waterLabel = new Label("W");
                                    waterLabel.setTextFill(Color.BLACK);
                                    waterLabel.setFont(Font.font("Sans Serif", 12));
                                    waterLabel.setTranslateX(15);
                                    viewerGrid.add(water, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    viewerGrid.add(waterLabel, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    i++;
                                }
                                i--;
                                break;
                            case "P":
                                i++;
                                while(!parseSplit[i].equals("S")){
                                    String[] coords = parseSplit[i].split(",");
                                    Rectangle precious = new Rectangle(boardHeightPx/boardHeight
                                            - 15, boardHeightPx/boardHeight - 15, Color.GOLD);
                                    precious.setTranslateX(5);
                                    Label preciousLabel = new Label("P");
                                    preciousLabel.setTextFill(Color.BLACK);
                                    preciousLabel.setFont(Font.font("Sans Serif", 12));
                                    preciousLabel.setTranslateX(15);
                                    viewerGrid.add(precious, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    viewerGrid.add(preciousLabel, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    i++;
                                }
                                i--;
                                break;
                                //
                            case "S":
                                i++;
                                while(i < parseSplit.length){
                                    String[] coords = parseSplit[i].split(",");
                                    Rectangle precious = new Rectangle(boardHeightPx/boardHeight
                                            - 15, boardHeightPx/boardHeight - 15, Color.SILVER);
                                    precious.setTranslateX(5);
                                    Label preciousLabel = new Label("S");
                                    preciousLabel.setTextFill(Color.BLACK);
                                    preciousLabel.setFont(Font.font("Sans Serif", 12));
                                    preciousLabel.setTranslateX(15);
                                    viewerGrid.add(precious, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    viewerGrid.add(preciousLabel, Integer.parseInt(coords[0]),
                                            Integer.parseInt(coords[1]));
                                    i++;
                                }
                                break;
                        }
                    }
//                    String[] parseSplit = part.split(" ");
//                    String stateCases = parseSplit[0];
//                    String[] parseSplit = part.split(" ");
//                        for (String parse : parseSplit) {
//                            switch (parse) {
//                                case "C":
//                                Rectangle coconut = new Rectangle(boardHeightPx/boardHeight
//                                        - 15, boardHeightPx/boardHeight - 15,
//                                        Color.BROWN);
//                                    String[] coords = parseSplit[i].split(",");
//                                    viewerGrid.add(coconut, Integer.parseInt(coords[0]),
//                                            Integer.parseInt(coords[1]));
//                            }
//                        }
//                    }
                    // parts[i].matches(" c \\d [ES]")
                    //String[] parseSplit = part.split(" ");
//                    break;
                    //r C 1,1 B 1,2 W P 1,4 S;
            }
        }
        viewerGrid.relocate((VIEWER_WIDTH/2-viewerGrid.getPrefWidth()/2), (VIEWER_HEIGHT/2-viewerGrid.getPrefHeight()/2));
        root.getChildren().add(viewerGrid);

        // String currentArrangementState = parts[i];
        ////                String[] currentArrangementStateParts = currentArrangementState.split(" ");
        ////                int playerId = Integer.parseInt(currentArrangementStateParts[2]);
        ////                String currentArrangementPhaseParts = currentArrangementStateParts[3];
        ////                String currentArrangementActualPhase = "";
        ////                switch (currentArrangementPhaseParts) {
        ////                    case "E": {
        ////                        currentArrangementActualPhase = "Exploration";
        ////                        break;
        ////                    }
        ////                    case "S": {
        ////                        currentArrangementActualPhase = "Settler";
        ////                        break;
        ////                    }
        ////                }
        ////
        ////                Text currentStateText = new Text();
//                        currentStateText.setText("The current player to move is player " +
//                                playerId + " in the " + currentArrangementActualPhase +
//                                " phase");
//                        currentStateText.setFont(Font.font("Sans Serif",
//                                FontWeight.BOLD, 20));
//                        currentStateText.setX(VIEWER_WIDTH / boardHeight * 3.5);
//                        currentStateText.setY(50);
//                        currentStateText.setTextAlignment(TextAlignment.CENTER);
//                        root.getChildren().add(currentStateText);
//                        currentStateText.setFill(Color.GREEN);
//
//        String[] parts = stateString.split(";");
//        String gameArrangementState = parts[0];
//        String[] gameArrangementStateParts = gameArrangementState.split(" ");
//        int boardHeight = Integer.parseInt(gameArrangementStateParts[1]);
//        int boardHeightPx = boardHeight * 45;
//        int tilePxSize = (boardHeightPx-45) / boardHeight;
//
//        Rectangle board = new Rectangle();
//        board.setWidth(boardHeightPx); // width (assume for right now the map is a square
//        board.setHeight(boardHeightPx); // height
//        board.setX((VIEWER_WIDTH / 2) - board.getWidth() / 2); // starting x loc
//        board.setY((VIEWER_HEIGHT / 2) - board.getHeight() / 2); // starting y loc
//        board.setFill(Color.DARKBLUE);
//        root.getChildren().add(board);
//
//        for (int i = 0; i < parts.length; i++) {
////            if(parts[i].matches("a \\d{1,2} \\d")){
////                Rectangle board = new Rectangle();
////                board.setWidth(boardHeightPx); // width (assume for right now the map is a square
////                board.setHeight(boardHeightPx); // height
////                board.setX((VIEWER_WIDTH / 2) - board.getWidth()/2); // starting x loc
////                board.setY((VIEWER_HEIGHT / 2) - board.getHeight()/2); // starting y loc
////                board.setFill(Color.DARKBLUE);
////                root.getChildren().add(board);
////            }
//
//            if (parts[i].matches(" c \\d [ES]")) {
//                String currentArrangementState = parts[i];
//                String[] currentArrangementStateParts = currentArrangementState.split(" ");
//                int playerId = Integer.parseInt(currentArrangementStateParts[2]);
//                String currentArrangementPhaseParts = currentArrangementStateParts[3];
//                String currentArrangementActualPhase = "";
//                switch (currentArrangementPhaseParts) {
//                    case "E": {
//                        currentArrangementActualPhase = "Exploration";
//                        break;
//                    }
//                    case "S": {
//                        currentArrangementActualPhase = "Settler";
//                        break;
//                    }
//                }
//
//                Text currentStateText = new Text();
//                currentStateText.setText("The current player to move is player " +
//                        playerId + " in the " + currentArrangementActualPhase +
//                        " phase");
//                currentStateText.setFont(Font.font("Sans Serif",
//                        FontWeight.BOLD, 20));
//                currentStateText.setX(VIEWER_WIDTH / boardHeight * 3.5);
//                currentStateText.setY(50);
//                currentStateText.setTextAlignment(TextAlignment.CENTER);
//                root.getChildren().add(currentStateText);
//                currentStateText.setFill(Color.GREEN);
//            }
//
//            // i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1;
//            //a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1
////            a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1;
////     *                    i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8
//
//            if (parts[i].matches(" i \\d{1,2} (\\d{1,2},\\d{1,2} )*\\d{1,2},\\d{1,2}")) {
//                GridPane islandGrid = new GridPane();
//                String[] islandString = parts[i].split(" ");
//                for(int j = 3; j < islandString.length; j++){
//                    Rectangle islandTiles = new Rectangle(tilePxSize, tilePxSize, Color.GREEN);
//                    String[] coords = islandString[j].split(",");
//                    islandGrid.add(islandTiles, Integer.parseInt(coords[0]),
//                            Integer.parseInt(coords[1]));
//                    islandGrid.setTranslateX(board.getX());
//                    islandGrid.setTranslateY(board.getY());
//                }
//                root.getChildren().add(islandGrid);
//            }
//        }
//                String[] islandString = parts[i].split(" ");
//                for (int j = i; j < parts.length; j++) {
//                String[] islandString = parts[i].split(" ");
//                Text bonusPointIslandText = new Text();
//                bonusPointIslandText.setText("The bonus point for this island is " +
//                        islandString[2]);
//                bonusPointIslandText.setFont(Font.font("Sans Serif",
//                        FontWeight.BOLD, 10));
//                bonusPointIslandText.setX(50);
//                bonusPointIslandText.setY(150);
//                bonusPointIslandText.setTextAlignment(TextAlignment.CENTER);
//                root.getChildren().add(bonusPointIslandText);
//                bonusPointIslandText.setFill(Color.RED);
////                    System.out.println("Parts[i] : " + parts[j]);
//                    for (int k = 3; k < islandString.length; k++) {
//                            String[] islandCoords = islandString[k].split(",");
//                            System.out.println("IslandString: " + islandString[k]);
//                            Rectangle island = new Rectangle(
//                                    Integer.parseInt(islandCoords[0]) * boardHeight,
//                                    Integer.parseInt(islandCoords[1]) * boardHeight,
//                                    (boardHeightPx - 100) / boardHeight,
//                                    (boardHeightPx - 100) / boardHeight
//                            );
////                island.setX((VIEWER_WIDTH / 2) - (board.getWidth()/2) + 50);
////                island.setY((VIEWER_HEIGHT / 2) - (board.getHeight()/2) + 50);
//                            island.setTranslateX((VIEWER_WIDTH / 2) - board.getWidth() / 2 + 50);
//                            island.setTranslateY((VIEWER_HEIGHT / 2) - board.getHeight() / 2 + 50);
//                            island.setFill(Color.GREEN);
//                            root.getChildren().add(island);
//                        }
//                    }
//                }

        //                        if (k == 2) {
//                            Text bonusPointIslandText = new Text();
//                            bonusPointIslandText.setText("The bonus point for this island is " +
//                                    islandString[2]);
//                            bonusPointIslandText.setFont(Font.font("Sans Serif",
//                                    FontWeight.BOLD, 10));
//                            bonusPointIslandText.setX(50);
//                            bonusPointIslandText.setY(150);
//                            bonusPointIslandText.setTextAlignment(TextAlignment.CENTER);
//                            root.getChildren().add(bonusPointIslandText);
//                            bonusPointIslandText.setFill(Color.RED);
//                        } else {
//                        Label rectangleLabel = new Label();

//                for(int j = i+1; j < islandString.length; j++){
//                    Label rectangleLabel = new Label();
//                    if(j == 3) {
//                        Text bonusPointIslandText = new Text();
//                        bonusPointIslandText.setText("The bonus point for this island is " +
//                                islandString[j]);
//                        bonusPointIslandText.setFont(Font.font("Sans Serif",
//                                FontWeight.BOLD, 20));
//                        bonusPointIslandText.setX(50);
//                        bonusPointIslandText.setY(150);
//                        bonusPointIslandText.setTextAlignment(TextAlignment.CENTER);
//                        root.getChildren().add(bonusPointIslandText);
//                        bonusPointIslandText.setFill(Color.GREEN);
//                    }
//                    String[] islandCoords = islandString[j].split(",");
//                    Rectangle island = new Rectangle(
//                            Integer.parseInt(islandCoords[0]) * boardHeight,
//                            Integer.parseInt(islandCoords[1]) * boardHeight,
//                            boardHeightPx - 100 / boardHeight,
//                            boardHeightPx - 100 / boardHeight
//                    );
//                    island.setX((VIEWER_WIDTH / 2) - (board.getWidth()/2) + 50);
//                    island.setY((VIEWER_HEIGHT / 2) - (board.getHeight()/2) + 50);
//                    island.setTranslateX((VIEWER_WIDTH / 2) - board.getWidth() / 2 + 50);
//                    island.setTranslateY((VIEWER_HEIGHT / 2) - board.getHeight() / 2 + 50);
//                    island.setFill(Color.GREEN);
//                    root.getChildren().add(island);
//                }
//                System.out.println("tod");
//                for (int j = 2; j < parts.length; j++) {
//                    String[] islandString = parts[j].split(" ");
//                    System.out.println("Parts[j] : " + parts[j]);
//                    for (int k = 3; k < islandString.length; k++) {
//                        Label rectangleLabel = new Label();
//                        String[] islandCoords = islandString[k].split(",");
//                        System.out.println("IslandString: " + islandString[k]);
//                        Rectangle island = new Rectangle(
//                                Integer.parseInt(islandCoords[0]) * boardHeight,
//                                Integer.parseInt(islandCoords[1]) * boardHeight,
//                                (boardHeightPx - 100) / boardHeight,
//                                (boardHeightPx - 100) / boardHeight
//                        );
////                island.setX((VIEWER_WIDTH / 2) - (board.getWidth()/2) + 50);
////                island.setY((VIEWER_HEIGHT / 2) - (board.getHeight()/2) + 50);
//                        island.setTranslateX((VIEWER_WIDTH / 2) - board.getWidth() / 2 + 50);
//                        island.setTranslateY((VIEWER_HEIGHT / 2) - board.getHeight() / 2 + 50);
//                        island.setFill(Color.GREEN);
//                        root.getChildren().add(island);
//                    }
//                }
//            }
//            switch(parts[i]){
//                if()
//                // case "a": {
//                    String gameArrangementState = parts[i];
//                    String[] gameArrangementStateParts = gameArrangementState.split(" ");
//                    int boardHeight = Integer.parseInt(gameArrangementStateParts[1]);
//                    int boardHeightPx = boardHeight * 45;
//
//                    Rectangle board = new Rectangle();
//                    board.setWidth(boardHeightPx); // width (assume for right now the map is a square
//                    board.setHeight(boardHeightPx); // height
//                    board.setX((VIEWER_WIDTH / 2) - board.getWidth()/2); // starting x loc
//                    board.setY((VIEWER_HEIGHT / 2) - board.getHeight()/2); // starting y loc
//                    board.setFill(Color.DARKBLUE);
//                    root.getChildren().add(board);
//                }
//            }
            // }
/*
        // Part 1 done (Game Arrangement State)
        String[] parts = stateString.split(";");
        String gameArrangementState = parts[0];
        String[] gameArrangementStateParts = gameArrangementState.split(" ");
        int boardHeight = Integer.parseInt(gameArrangementStateParts[1]);
        int boardHeightPx = boardHeight * 45;

        Rectangle board = new Rectangle();
        board.setWidth(boardHeightPx); // width (assume for right now the map is a square
        board.setHeight(boardHeightPx); // height
        board.setX((VIEWER_WIDTH / 2) - board.getWidth()/2); // starting x loc
        board.setY((VIEWER_HEIGHT / 2) - board.getHeight()/2); // starting y loc
        board.setFill(Color.DARKBLUE);
        root.getChildren().add(board);

        // Part 2 (Current Arrangement State)
        String currentArrangementState = parts[1];
        String[] currentArrangementStateParts = currentArrangementState.split(" ");
        int playerId = Integer.parseInt(currentArrangementStateParts[2]);
        String currentArrangementPhaseParts = currentArrangementStateParts[3];
        String currentArrangementActualPhase = "";
        switch(currentArrangementPhaseParts) {
            case "E": {
                currentArrangementActualPhase = "Exploration";
                break;
            }
            case "S": {
                currentArrangementActualPhase = "Settler";
                break;
            }
        }

        Text currentStateText = new Text();
        currentStateText.setText("The current player to move is player " +
                playerId + " in the " + currentArrangementActualPhase +
                " phase");
        currentStateText.setFont(Font.font("Sans Serif",
                FontWeight.BOLD, 20));
        currentStateText.setX(VIEWER_WIDTH/boardHeight*3.5);
        currentStateText.setY(50);
        currentStateText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(currentStateText);
        currentStateText.setFill(Color.GREEN);

        // rootGroup.getChildren().addAll(currentStateText,board);

        //Before doing part 3, make the board and the current state adjustable to each
        // by that I mean, when the board size adjusted, the current state is also
        // adjusted

        // The input is below
        // a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1;

        // statement = parts
        // Part 3 (Many Island Statements)
        for(int i = 2; i < parts.length; i++){
            String[] islandString = parts[i].split(" ");
             System.out.println("Parts[i] : " + parts[i]);
            for(int j = 3; j < islandString.length; j++){
                Label rectangleLabel = new Label();
                String[] islandCoords = islandString[j].split(",");
                System.out.println("IslandString: " + islandString[j]);
                Rectangle island = new Rectangle(
                        Integer.parseInt(islandCoords[0]) * boardHeight,
                        Integer.parseInt(islandCoords[1]) * boardHeight,
                        (boardHeightPx - 100) / boardHeight,
                        (boardHeightPx - 100) / boardHeight
                );
//                island.setX((VIEWER_WIDTH / 2) - (board.getWidth()/2) + 50);
//                island.setY((VIEWER_HEIGHT / 2) - (board.getHeight()/2) + 50);
                island.setTranslateX((VIEWER_WIDTH / 2) - board.getWidth()/2 + 50);
                island.setTranslateY((VIEWER_HEIGHT / 2) - board.getHeight()/2 + 50);
                island.setFill(Color.GREEN);
                root.getChildren().add(island);
            }
        }

 */


//        TilePane pane = new TilePane();
//        pane.setTileAlignment(Pos.CENTER);
//
//        for(int i = 1; i <= boardHeight; i++){
//            for(int j = 1; j <= boardHeight; j++){
//                pane.getChildren().add(new Button(i + "," + j));
//            }
//        }
//        root.getChildren().add(pane);
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500,500);
//        frame.setVisible(true);
//        frame.setLayout(new GridLayout(boardHeight, boardHeight));
//
//        for(int i = 1; i <= boardHeight; i++){
//            for(int j = 1; j <= boardHeight; j++){
//                frame.add(new JButton(i + "," + j));
//            }
//        }
//
//        frame.setVisible(true);
            //root.getChildren().add(frame);


//        TilePane tile = new TilePane();
//        tile.setHgap(8);
//        tile.setPrefColumns(13);
//        for (int i = 0; i < 20; i++){
//            root.getChildren().add(tile);
//        }

//        TilePane tileVer = new TilePane(Orientation.VERTICAL);
//        tile.setTileAlignment(Pos.CENTER_LEFT);
//        tile.setPrefRows(10);
//        for (int i = 0; i < 50; i++) {
//            tileVer.getChildren().add(new ImageView(...));
//        }
            // Make hexagonal tiles inside the board
            // CoordsPx < coords * 45 as "*45" is the px constant for the board

        /*
        [Many Island Statements]
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
         */
            // FIXME Task 5
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
