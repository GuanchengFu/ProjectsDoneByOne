package editor.HelperClass;

public class Position {
    private int xPos;
    private int yPos;

    public Position(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }
}
