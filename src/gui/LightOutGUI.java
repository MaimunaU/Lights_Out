package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.LightsOutModel;
import model.Observer;

public class LightOutGUI extends Application implements Observer<LightsOutModel, String> {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane border = new BorderPane();

        Label topLabel = new Label( "Moves: " + "Message:");
        topLabel.setAlignment( Pos.TOP_LEFT );
        border.setTop(topLabel);

        Pane mainPane = this.makeButtonPane();
        border.setCenter(mainPane);

        stage.setTitle( "Lights Out" );
        stage.setScene( new Scene(border) );
        stage.show();
    }

    private Pane makeButtonPane() {

        GridPane grid = new GridPane();

        final int rows = 5;
        final int cols = 5;

        Button btn;
        for ( int r = 0; r < rows; ++r ) {
            for ( int c = 0; c < cols; ++c ) {
                btn = new Button( Integer.toString( r * cols + c ) );
                btn.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
                grid.add( btn, c, r );
            }
        }

        return grid;
    }

    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void update(LightsOutModel tiles, String s) {

    }
}
