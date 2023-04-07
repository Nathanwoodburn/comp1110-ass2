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

        GridPane viewerGrid = new GridPane();
        String[] parts = stateString.split("; ?");
        int boardHeight = 0;
        int boardHeightPx = 0;
        for ( String part : parts) {
            String[] parseSplit = part.split(" ");
            String stateCases = parseSplit[0];
            switch (stateCases) {
                case "a":
                    boardHeight = Integer.parseInt(parseSplit[1]);
                    boardHeightPx = 575;
                    viewerGrid.setPrefWidth(boardHeightPx);
                    viewerGrid.setPrefHeight(boardHeightPx);

                    // row major order
                    for(int i = 0; i < boardHeight; i++){
                        for(int j = 0; j < boardHeight; j++){
                            Rectangle boardMap = new Rectangle(boardHeightPx/boardHeight,
                                    boardHeightPx/boardHeight, Color.DARKBLUE);
                            viewerGrid.add(boardMap, i, j);
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
                        viewerGrid.add(island, xCoord, yCoord);
                    }
                    break;
                case "s":
                    for(int i = 1; i < parseSplit.length; i++){
                        Rectangle theRock = new Rectangle(boardHeightPx/boardHeight
                                - 15, boardHeightPx/boardHeight - 15, Color.GRAY);
                        String[] coords = parseSplit[i].split(",");
                        theRock.setTranslateX(5);
                        viewerGrid.add(theRock, Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[1]));
                    }
                    break;
                case "r":
                    for(int i = 1; i < parseSplit.length; i++){
                        switch(parseSplit[i]) {
                            case "C":
                                i++;
                                while(!parseSplit[i].equals("B")){
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
                    break;
                case "p":
                    String container = "";
                    container += "player " + parseSplit[1] + " with score: " +
                            parseSplit[2] + ", coconuts: " + parseSplit[3] +
                            ", bambooo: " + parseSplit[4] + ", water: " +
                            parseSplit[5] + ", precious stone: " + parseSplit[6] +
                            ", statuettes: " + parseSplit[7] + ", placed settlers at ";
                    int i = 9;
                    int counter = 0;
                        while(!parseSplit[i].equals("T")){
                            counter++;
                            String[] coords = parseSplit[i].split(",");
                            Rectangle settlers = new Rectangle(boardHeightPx/boardHeight
                                    - 15, boardHeightPx/boardHeight - 15, Color.PINK);
                            settlers.setTranslateX(5);
                            Label settlerLabel = new Label("Settlers");
                            settlerLabel.setTextFill(Color.BLACK);
                            settlerLabel.setFont(Font.font("Sans Serif", 9));
                            settlerLabel.setTranslateX(5);
                            viewerGrid.add(settlers, Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1]));
                            viewerGrid.add(settlerLabel, Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1]));
                            if(counter > 1) container += " and ";
                            container += Integer.parseInt(coords[0]) + "," +
                                    Integer.parseInt(coords[1]);
                            i++;
                        }
                        i++;

                        container += ", placed villages at ";
                        int counterT = 0;
                        while(i < parseSplit.length){
                            counterT++;
                            String[] coords = parseSplit[i].split(",");
                            Rectangle village = new Rectangle(boardHeightPx/boardHeight
                                    - 15, boardHeightPx/boardHeight - 15, Color.LIGHTGOLDENRODYELLOW);
                            village.setTranslateX(5);
                            Label villageLabel = new Label("Village");
                            villageLabel.setTextFill(Color.BLACK);
                            villageLabel.setFont(Font.font("Sans Serif", 9));
                            villageLabel.setTranslateX(5);
                            viewerGrid.add(village, Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1]));
                            viewerGrid.add(villageLabel, Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1]));
                            if(counterT > 1) container += " and ";
                            container += Integer.parseInt(coords[0]) + "," +
                                    Integer.parseInt(coords[1]);
                            i++;
                        }

                    Text playerStateText = new Text();
                    playerStateText.setText(container);
                    playerStateText.setFont(Font.font("Sans Serif",
                            FontWeight.BOLD, 12));
                    playerStateText.setX(200);
                    playerStateText.setY(645);
                    playerStateText.setTextAlignment(TextAlignment.CENTER);
                    root.getChildren().add(playerStateText);
                    playerStateText.setFill(Color.BLACK);
//                    }
            }
        }
        viewerGrid.relocate((VIEWER_WIDTH/2-viewerGrid.getPrefWidth()/2), (VIEWER_HEIGHT/2-viewerGrid.getPrefHeight()/2));
        root.getChildren().add(viewerGrid);
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
