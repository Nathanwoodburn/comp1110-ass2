package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
     * e.g. "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;"
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

        // Part 3 (Many Island Statements)
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
