package editor;

import editor.HelperClass.Print;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.basic.BasicTreeUI;
import java.util.ArrayList;
import java.util.List;

import editor.HelperClass.Position;
import javafx.util.Duration;
import org.junit.Test;


public class KeyEventHandler implements EventHandler<KeyEvent> {

    RenderEngine render;
    Group rootRef;

    public KeyEventHandler(RenderEngine render) {
        this.render = render;
        rootRef = render.getRootRef();
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
            // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
            // the KEY_TYPED event, javafx handles the "Shift" key and associated
            // capitalization.
            String characterTyped = keyEvent.getCharacter();
            if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                // Ignore control keys, which have non-zero length, as well as the backspace
                // key, which is represented as a character of value = 8 on Windows.
                Text temp = new Text(characterTyped);
                render.text.add(temp);
                render.size += 1;
                render.disposeSingleText(temp);
                rootRef.getChildren().add(temp);
            }

        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
            // events have a code that we can check (KEY_TYPED events don't have an associated
            // KeyCode).
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.UP) {
                render.fontSize += 5;
                //displayText.setFont(Font.font(fontName, fontSize));
                render.render();
            } else if (code == KeyCode.DOWN) {
                render.fontSize = Math.max(0, render.fontSize - 5);
                render.render();
            } else if (code == KeyCode.BACK_SPACE) {
                System.out.println("Backspace");
            } else if (code == KeyCode.ENTER) {
                System.out.println("Enter");
            }
        }
    }

}
