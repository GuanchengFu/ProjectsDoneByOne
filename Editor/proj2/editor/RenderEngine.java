package editor;

import editor.HelperClass.Print;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.basic.BasicTreeUI;
import java.util.ArrayList;
import java.util.List;

import editor.HelperClass.Position;
import org.junit.Test;

/** Display the text in a format which is suitable for the window with current size.
 *  requirement:
 *  window size: 500 * 500 at the beginning.
 *  bottom margin and top margin: 0
 *  left margin and right margin: 5*/
public class RenderEngine {

    /** This two variables should represent the actually space that can be used to
     *  render the text.*/
    private int windowWidth = 500;
    private int windowHeight;

    private static final int TOP_MARGIN = 0;
    private static final int BOTTOM_MARGIN = 0;
    private static final int LEFT_MARGIN = 5;
    private static final int RIGHT_MARGIN = 5;

    private Group rootRef;

    // The beginning position for displaying the character.
    private Position currentPos = new Position(LEFT_MARGIN, TOP_MARGIN);

    /** Use a data structure to store the text so that
     *  characters can be added to or deleted from the current cursor position in constant time*/
    private List<Text> text;

    public RenderEngine(final Group root) {
        rootRef = root;
        text = new ArrayList<>();
    }

    /** Use a data structure to store the render information where updates take linear time*/

    /** Render the user view based on the width and height of the window.
     *  This class may need to interact with the KeyEventHandler:
     *  For instance, when a key is pressed, the related text object should be
     *  stored inside the data structure.*/
    public void render() {

    };



    /** Check whether the current line has enough space to contain the next character.
     *  This method should do the word wrapping job.
     *  If word wrapping happens, the render engine should re-render the entire
     *  text.*/
    private void availableSpace() {

    }

    private class KeyEventHandler implements EventHandler<KeyEvent> {
        private int fontSize = 20;

        private String fontName = "Verdana";

        KeyEventHandler(final Group root) {

        }

        private void textSetUp(Text t) {
            t.setTextOrigin(VPos.TOP);
            t.setFont(Font.font(fontName, fontSize));
            t.setX(currentPos.getX());
            t.setY(currentPos.getY());
            t.toFront();
            text.add(t);
            rootRef.getChildren().add(t);
        }

        /** Update the variable currentPos so that it advanced to the next available position.
         * @param t The latest text input by the user.
         * This will update the position in a row.*/
        private void NextPos(Text t) {
            double textWidth = t.getLayoutBounds().getWidth();
            double textHeight = t.getLayoutBounds().getHeight();

            int width = (int) Math.round(textWidth);
            int height = (int) Math.round(textHeight);
            currentPos = new Position(currentPos.getX() + width, currentPos.getY());
        }

        /** Update the current position to the next line based on the font size
         *  and the text input by the user.
         *  The logic is:
         *  1. The user has input a text.
         *  2. The text is too big to be stored in this line.
         *  3. Find the next line
         *  4. set up the text.*/
        private void NextLine(Text t) {
            double textHeight = t.getLayoutBounds().getHeight();
            int height = (int) Math.round(textHeight);
            currentPos = new Position(LEFT_MARGIN, currentPos.getY() + height + 5);
        }

        /** Check whether word wrap occurs.
         *  This is the way to check whether the word wrap is occurred.
         *  The condition where word wrap occurred:
         *  最后一个字符输入，即使算上margin也无法放下，那么将整个单词放入下一行。
         *  空格的处理
         *  */
        private boolean checkWordWrap(Text t) {
            double textWidth = t.getLayoutBounds().getWidth();
            int width = (int) Math.round(textWidth);
            int expectedWidth = currentPos.getX() + width;
            if (expectedWidth > windowWidth - RIGHT_MARGIN) {
                if (text.size() == 0) {
                    Print.print("This condition should only happens when the available space is not enough for even " +
                            "one letter.");
                    return false;
                } else {
                    Text lastText = text.get(text.size() - 1);
                    if (lastText.getText().equals(" ")) {
                        //lastText is the " "
                    } else {
                        //lastText is a text.
                        if (expectedWidth <= windowWidth) {
                            return false;
                        } else {
                            //Move the entire word to the next line.
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        /** Provide a view of the first word before index.
         *  If the text at index is space, return the space.
         *  Otherwise, return the first word.*/
        private List<Text> getWord(int index) {
            int wordLength = getWordLength(index);
            if (wordLength == 0) {

            }
        }

        /** @return 0: if the char at index is a whitespace.
         *  @return word length: otherwise.*/
        private int getWordLength(int index) {
            Text temp = text.get(index);
            int length = 0;
            while(!temp.getText().equals(" ") && !temp.getText().equals("\r") && index >= 0) {
                length += 1;
                index -= 1;
                if (index < 0) {
                    return length;
                }
                temp = text.get(index);
            }
            return length;
        }

        private void test() {
            System.out.println(getWordLength(text.size() - 1));
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
                    if (checkWordWrap(temp)) {
                        //Word wrap happens.
                    } else {
                        textSetUp(temp);
                        NextPos(temp);
                        keyEvent.consume();
                    }
                    //test();
                }

            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {
                    fontSize += 5;
                    //displayText.setFont(Font.font(fontName, fontSize));
                } else if (code == KeyCode.DOWN) {
                    fontSize = Math.max(0, fontSize - 5);
                    //displayText.setFont(Font.font(fontName, fontSize));
                }
            }
        }
    }

    public EventHandler<KeyEvent> getKeyEventHandler() {
        return new KeyEventHandler(rootRef);
    }
}
