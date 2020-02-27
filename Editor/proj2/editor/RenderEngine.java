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

    //Store the working procedure for rendering.
    private int size = 0;
    private int fontSize = 20;

    private String fontName = "Verdana";

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
    private void render() {
        currentPos = new Position(LEFT_MARGIN, TOP_MARGIN);
        size = 0;
        for (int i = 0; i < text.size(); i ++) {
            //System.out.println("**************************************");
            //System.out.print("before:");
            //System.out.println(text.get(i));
            size += 1;
            disposeSingleText(text.get(i));
            //System.out.print("after:");
            //System.out.println(text.get(i));
            //System.out.println("**************************************");

            //System.out.println();
        }
    }

    /** Set the font size and font and some default settings.*/
    private void textSetUp(Text t) {
        t.setTextOrigin(VPos.TOP);
        t.setFont(Font.font(fontName, fontSize));
        t.toFront();
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

    private void PrePos(Text t) {
        double textWidth = t.getLayoutBounds().getWidth();
        double textHeight = t.getLayoutBounds().getHeight();

        int width = (int) Math.round(textWidth);
        int height = (int) Math.round(textHeight);
        currentPos = new Position(currentPos.getX() - width, currentPos.getY());
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
     *  word wrap means move the current word to the next line entirely.
     *  t is not whitespace.
     *  */
    private boolean checkWordWrap(Text t) {
        double textWidth = t.getLayoutBounds().getWidth();
        int width = (int) Math.round(textWidth);
        int expectedWidth = currentPos.getX() + width;

        Text lastText = text.get(size - 2);
        if (lastText.getText().equals(" ")) {
            return true;
        } else {
            if (checkLongWord(size - 1)) {
                return false;
            } else {
                if (expectedWidth <= windowWidth) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /** Provide a view of the first word before index.
     *  If the text at index is space, return the space.
     *  Otherwise, return the first word.*/
    private List<Text> getWord(int index) {
        int wordLength = getWordLength(index);
        if (wordLength == 0) {
            return null;
        } else {
            return text.subList(index - wordLength + 1, index + 1);
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



    /** @return true if the word before index is longer than the available space of a line.*/
    private boolean checkLongWord(int index) {
        int length = 0;
        Text temp = text.get(index);
        while(!temp.getText().equals(" ") && !temp.getText().equals("\r") && index >= 0) {
            length += (int) Math.round(temp.getLayoutBounds().getWidth());
            if (length >= windowWidth - LEFT_MARGIN - RIGHT_MARGIN) {
                return true;
            }
            if (index == 0) {
                return false;
            }
            index -= 1;
            temp = text.get(index);
        }
        return length >= windowWidth - LEFT_MARGIN - RIGHT_MARGIN;
    }


    /** Move the entire word to the next line when word wrap happens.*/
    private void handleWordWrap(List<Text> word) {
        NextLine(word.get(0));
        for (Text t:word) {
            t.setX(currentPos.getX());
            t.setY(currentPos.getY());
            NextPos(t);
        }
    }

    /** Dispose a single text input by the user.*/
    private void disposeSingleText(Text temp) {
        textSetUp(temp);
        double textWidth = temp.getLayoutBounds().getWidth();
        int width = (int) Math.round(textWidth);
        int expectedWidth = currentPos.getX() + width;


        //Either word wrap, or change to a new line immediately.
        if (expectedWidth > windowWidth - RIGHT_MARGIN - LEFT_MARGIN) {
            if (temp.getText().equals(" ")) {
                //If it is the whitespace, don't move to the next position.
                temp.setX(currentPos.getX());
                temp.setY(currentPos.getY());
            } else {
                if (checkWordWrap(temp)) {
                    handleWordWrap(getWord(size - 1));
                } else {
                    /** Word wrap not happen, this includes two conditions:
                     *  1.The word is too long (longer than the line), in this condition, simply move to the next line.
                     *  2.There is enough space for another letter.*/
                    if(checkLongWord(size - 1)) {
                        NextLine(temp);
                    }
                    temp.setX(currentPos.getX());
                    temp.setY(currentPos.getY());
                    NextPos(temp);
                }
            }
        } else {
            // Has enough space, just display as normal.

            temp.setX(currentPos.getX());
            temp.setY(currentPos.getY());
            NextPos(temp);
        }
        //test();
    }

    private class KeyEventHandler implements EventHandler<KeyEvent> {


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
                    text.add(temp);
                    size += 1;
                    disposeSingleText(temp);
                    rootRef.getChildren().add(temp);
                }

            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {
                    fontSize += 5;
                    //displayText.setFont(Font.font(fontName, fontSize));
                    render();
                } else if (code == KeyCode.DOWN) {
                    fontSize = Math.max(0, fontSize - 5);
                    render();
                } else if (code == KeyCode.BACK_SPACE) {
                    System.out.println("Backspace");
                } else if (code == KeyCode.ENTER) {
                    System.out.println("Enter");
                }
            }
        }
    }

    public EventHandler<KeyEvent> getKeyEventHandler() {
        return new KeyEventHandler();
    }



    private class CursorTracker {

    }
}
