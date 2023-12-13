public class Player {

    public String name;
    private final String color;

    public Deck deck;
    public Deck board;
    public Deck hand;

    private int set;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.deck = new Deck();
        this.board = new Deck();
        this.hand = new Deck();
        this.set = 0;
    }

    public int getSet() {
        return set;
    }

    public void winSet() {
        set++;
        System.out.print("\n   ");
        SystemManager.println("   " + name + " wins the set!   ", color + SystemManager.ANSI_WHITE_BOLD);
    }

    public void winGame() {
        set = 3;
        System.out.print("\n   ");
        SystemManager.println("   " + name + " wins the game!   ", color + SystemManager.ANSI_WHITE_BOLD);
    }
}
