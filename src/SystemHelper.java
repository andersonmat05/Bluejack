import java.util.Scanner;

public class SystemHelper {

    /* Escape codes used in print method. */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_BOLD = "\033[1;97m";
    public static final String ANSI_POSITIVE_BG = "\033[42m";
    public static final String ANSI_NEGATIVE_BG = "\033[41m";
    public static final String ANSI_BLUE_BOLD = "\033[1;94m";

    public static boolean colorEnabled;

    public static void setColorEnabled(boolean newEnabled) {
        colorEnabled = newEnabled;
    }

    public static void print(String x, String color) {
        if (colorEnabled)
            System.out.print(color);
        System.out.print(x);
        /* Reset color */
        if (colorEnabled)
            System.out.print(ANSI_RESET);
    }

    public static void println(String x, String color) {
        print(x, color);
        System.out.println();
    }

    /**
     * Prompts user to enter an integer within a range.
     * @param min Minimum value expected from user to enter, inclusive.
     * @param max Maximum value expected from user to enter, inclusive.
     */
    public static int scanIntRange(int min, int max) {
        Scanner scan = new Scanner(System.in);
        /* Keep user in the loop until valid input is entered */
        while (true) {
            try {
                System.out.print("> ");
                int input = scan.nextInt();
                /* Check for range */
                if (input >= min && input <= max) {
                    return input;
                }
            } catch (java.util.InputMismatchException e) {
                /* Consume the input */
                scan.next();
            }
            System.out.print("  Invalid input, please enter again.\n");
        }
    }

    public static String scanString() {
        Scanner scan = new Scanner(System.in);
        return scan.next().trim();
    }
}
