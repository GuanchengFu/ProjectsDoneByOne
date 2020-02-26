package editor;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import editor.HelperClass.Print;


public class Editor extends Application {
    /** When the editor first opens, the window size should be 500 by 500 pixels
     *  (including the scroll bar)*/
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;


    @Override
    public void start(Stage primaryStage) {



        // Create a Node that will be the parent of all things displayed on the screen.
        Group root = new Group();
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        RenderEngine render = new RenderEngine(root);

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler = render.getKeyEventHandler();
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);



        primaryStage.setTitle("Single letter display simple");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            String inputFIlename = args[0];

            if (args.length >= 2) {
                if (args[1].equals("debug")) {
                    Print.setMode(true);
                }
            }
        }
        launch(args);
    }
}