package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.LightsOutModel;
import model.Observer;

public class LightOutGUI extends Application implements Observer<LightsOutModel, String> {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();

        Label topLabel = new Label("Moves: " + "Message:");
        topLabel.setAlignment(Pos.TOP_LEFT);
        topLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setTop(topLabel);

        Pane mainPane = this.makeButtonPane();
        mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setCenter(mainPane);

        Pane bottomPane = this.makeActionButtons();
        bottomPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        border.setBottom(bottomPane);

        stage.setTitle("Lights Out");
        stage.setScene(new Scene(border));
        stage.show();
    }

    private Pane makeButtonPane() {

        GridPane grid = new GridPane();
        int rows = 5;
        int cols = 5;

        ColumnConstraints[] cc = new ColumnConstraints[cols];
        for (int i = 0; i < cols; i++) {
            cc[i] = new ColumnConstraints();
            cc[i].setPercentWidth(100.0 / cols);
        }
        grid.getColumnConstraints().addAll(cc);

        RowConstraints[] rc = new RowConstraints[rows];
        for (int i = 0; i < rows; i++) {
            rc[i] = new RowConstraints();
            rc[i].setPercentHeight(100.0 / rows);
        }
        grid.getRowConstraints().addAll(rc);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button btn = new Button("(" + row + "," + col + ")");
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                grid.add(btn, col, row);
            }
        }

        return grid;
    }

    private Pane makeActionButtons() {
        Pane pane = new HBox();
        pane.getChildren().add(new Button ("New Game"));
        pane.getChildren().add(new Button ("Load Game"));
        pane.getChildren().add(new Button ("Hint"));

        return pane ;
    }

    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void update(LightsOutModel tiles, String s) {}
}
