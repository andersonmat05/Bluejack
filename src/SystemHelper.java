import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SystemHelper {

    /* Escape codes used in print method. */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_BOLD = "\033[1;97m";
    public static final String ANSI_CYAN_BOLD = "\033[1;96m";
    public static final String ANSI_POSITIVE_BG = "\033[42m";
    public static final String ANSI_NEGATIVE_BG = "\033[41m";
    public static final String ANSI_BLUE_BOLD = "\033[1;94m";
    public static final String ANSI_GRAY_BG = "\033[0;100m";

    private static final int HISTORY_SIZE = 10;
    private static final String HISTORY_FILE = "history.txt";

    private static boolean colorEnabled;

    public static void setColorEnabled(boolean newEnabled) {
        colorEnabled = newEnabled;
    }

    public static boolean getColorEnabled() {
        return colorEnabled;
    }

    /**
     * Prints a string in color.
     */
    public static void print(String x, String color) {
        if (colorEnabled)
            System.out.print(color);
        System.out.print(x);
        /* Reset color */
        if (colorEnabled)
            System.out.print(ANSI_RESET);
    }

    /**
     * Prints a string in color and terminates current line.
     */
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
        Scanner scanner = new Scanner(System.in);
        /* Keep user in the loop until valid input is entered */
        while (true) {
            try {
                System.out.print("> ");
                int input = scanner.nextInt();
                /* Check for range */
                if (input >= min && input <= max) {
                    return input;
                }
            } catch (java.util.InputMismatchException e) {
                /* Consume the input */
                scanner.next();
            }
            System.out.print("  Invalid input, please enter again.\n");
        }
    }

    /**
     * Prompt user to enter a string.
     */
    public static String scanString() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        return scanner.next();
    }

    /**
     * Prompt user to press enter
     */
    public static void scanEnter() {
        System.out.print("Press enter to continue");
        try {
            System.in.read();
        } catch (Exception ignored) {}
    }

    /**
     * Attempts to save the game result to the history file.
     */
    public static void saveResult(Player player1, Player player2) {
        Scanner reader = null;
        String[] oldHistory = new String[HISTORY_SIZE];
        String[] newHistory = new String[HISTORY_SIZE];
        try {
            /* Read history file */
            reader = new Scanner(Paths.get("history.txt"));

            int i = 0;
            while (reader.hasNextLine() && i < HISTORY_SIZE) {
                oldHistory[i++] = reader.nextLine();
            }
        } catch (IOException e) {
            System.out.println("History file not found, creating new file...");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        /* Copy old data to new array without the last element */
        for (int j = 1; j < HISTORY_SIZE; j++) {
            newHistory[j] = oldHistory[j-1];
        }

        /* Get date and time */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        /* Set first element of new data as current game result */
        newHistory[0] = player1.name + " " + player1.getSet()
                + " - " + player2.getSet() + " " + player2.name
                + " (" + dateTimeFormatter.format(now) + ")";

        /* Write to history file */
        try {
            FileWriter fw = new FileWriter(HISTORY_FILE);
            for (int i = 0; i < HISTORY_SIZE; i++) {
                if (newHistory[i] != null) {
                    fw.write(newHistory[i]);
                    if (i < HISTORY_SIZE - 1)
                        if (newHistory[i+1] != null)
                            fw.write(System.lineSeparator());
                }
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("History file could not be written.");
        }
    }
}
