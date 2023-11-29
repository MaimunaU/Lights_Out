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

        FlowPane topPane = new FlowPane();
        topPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        topPane.getChildren().add(moves);
        topPane.getChildren().add(message);
        topPane.setHgap(5);
        border.setTop(topPane);

        Pane mainPane = this.makeButtonPane();
        mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setCenter(mainPane);

        Pane pane = new HBox();
        Button newGame = new Button ("New Game");
        Button loadGame = new Button ("Load Game");
        Button hint = new Button ("Hint");
        pane.getChildren().add(newGame);
        pane.getChildren().add(loadGame);
        pane.getChildren().add(hint);
        newGame.setOnAction(event -> model.generateRandomBoard());
        loadGame.setOnAction(event -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Load a game board.");
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.lob"));
                    File selectedFile = fileChooser.showOpenDialog(stage);
                    model.loadBoardFromFile(selectedFile);
                });
        hint.setOnAction(event -> model.getHint());
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setBottom(pane);

        stage.setTitle("Lights Out");
        stage.setScene(new Scene(border));
        stage.setResizable(false);
        stage.show();
    }

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

    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
        model = new LightsOutModel();
        model.addObserver(this);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

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
