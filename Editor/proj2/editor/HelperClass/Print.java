package editor.HelperClass;

public class Print {

    private static boolean debug_mode = false;

    public static void setMode(boolean flag) {
        debug_mode = flag;
    }

    public static void print(String toPrint) {
        if (debug_mode) {
            System.out.println(toPrint);
        } else {
            // Do nothing
        }
    }
}
