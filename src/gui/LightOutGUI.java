package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.LightsOutModel;
import model.Observer;
import model.Tile;
import java.io.File;

/**
 * A graphic user interface for Lights Out
 *
 * @author Maimuna Ullah (mnu2234)
 */
public class LightOutGUI extends Application implements Observer<LightsOutModel, String> {
    private LightsOutModel model;
    private Label moves  = new Label("Moves: 0");
    private Label message = new Label("Message: ");
    private Button[][] board = new Button[5][5];

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();
        border.setPrefHeight(500);
        border.setPrefWidth(500);

        //Creates and sets the top pane where the moves and message is shown
        FlowPane topPane = new FlowPane();
        topPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        topPane.getChildren().add(moves);
        topPane.getChildren().add(message);
        topPane.setHgap(5);
        border.setTop(topPane);

        //Creates and sets the center pane of a grid of buttons representing lights
        Pane mainPane = this.makeButtonPane();
        mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setCenter(mainPane);

        //Creates and sets the bottom pane of the three action buttons
        Pane bottomPane = new HBox();
        Button newGame = new Button ("New Game");
        Button loadGame = new Button ("Load Game");
        Button hint = new Button ("Hint");
        bottomPane.getChildren().add(newGame);
        bottomPane.getChildren().add(loadGame);
        bottomPane.getChildren().add(hint);
        newGame.setOnAction(event -> model.generateRandomBoard());
        //Allows the user to choose their own file
        loadGame.setOnAction(event -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Load a game board.");
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.lob"));
                    File selectedFile = fileChooser.showOpenDialog(stage);
                    model.loadBoardFromFile(selectedFile);
                });
        hint.setOnAction(event -> model.getHint());
        bottomPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setBottom(bottomPane);

        stage.setTitle("Lights Out");
        stage.setScene(new Scene(border));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Creates a 5x5 grid of buttons representing the lights
     * @return a button grid pane
     */
    private Pane makeButtonPane() {
        GridPane grid = new GridPane();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Button btn = new Button("");
                btn.setPrefHeight(100);
                btn.setPrefWidth(100);
                final int rows = row;
                final int cols = col;
                board[row][col] = btn;
                btn.setOnAction(event -> model.toggleTile(rows, cols));
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                grid.add(btn, col, row);
            }
        }

        return grid;
    }

    /**
     * Creates a model and adds a view to the model
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
        model = new LightsOutModel();
        model.addObserver(this);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Updates the grid of lights after each move.
     * Also updates the number of moves made and the message for different situations.
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(LightsOutModel model, String msg) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Tile tile = model.getTile(row, col);
                if (tile.isOn()) {
                    board[row][col].setStyle("-fx-background-color: white;");
                }
                else {
                    board[row][col].setStyle("-fx-background-color: black;");
                }
            }
        }
        moves.setText("Moves: " + model.getMoves());
        message.setText("Message: " + msg);
        if (msg.equals(LightsOutModel.LOADED)){
            message.setText("Message: Game Loaded");
            return;
        }
        else if (msg.equals(LightsOutModel.LOAD_FAILED)){
            message.setText("Message: Error Loading Game");
            return;
        }
        else if (msg.startsWith(LightsOutModel.HINT_PREFIX)) {
            message.setText("Message: " + msg);
            return;
        }

        if (model.gameOver()) {
            message.setText("Message: You Win!");
        }
    }
}
