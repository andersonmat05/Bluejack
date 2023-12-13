public class Player {

    private static final String ANSI_WHITE_BOLD = "\033[1;97m";
    private static final String ANSI_RESET = "\u001B[0m";

    public String name;
    public String color;

    public Deck deck;
    public Deck board;
    public Deck hand;

    private int set;
    private static boolean colorEnabled;

    public Player(String name) {
        this.name = name;
        this.deck = new Deck();
        this.board = new Deck();
        this.hand = new Deck();
        this.set = 0;
        colorEnabled = false;
    }

    public static void setColorEnabled(boolean newEnabled) {
        colorEnabled = newEnabled;
    }

    public int getSet() {
        return set;
    }

    public void winSet() {
        set++;
        System.out.print("   ");
        if (colorEnabled)
            System.out.print(color + ANSI_WHITE_BOLD);
        System.out.print("   " + name + " wins the set!   ");
        /* Reset color */
        if (colorEnabled)
            System.out.print(ANSI_RESET);
        System.out.println();
    }

    public void winGame() {
        set = 3;
        if (colorEnabled)
            System.out.print(color + ANSI_WHITE_BOLD);
        System.out.print("   " + name + " wins the game!   ");
        /* Reset color */
        if (colorEnabled)
            System.out.print(ANSI_RESET);
        System.out.println();
    }
}
