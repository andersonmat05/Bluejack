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
        SystemHelper.println("   " + name + " wins the set!   ", color + SystemHelper.ANSI_WHITE_BOLD);
    }

    public void winGame() {
        set = 3;
        System.out.print("\n   ");
        SystemHelper.println("   " + name + " wins the game!   ", color + SystemHelper.ANSI_WHITE_BOLD);
    }

    public void playCard(int cardIndex) {
        Card card = hand.remove(cardIndex);
        if(card == null) return;
        if (card.isFlip())
            board.set(new Card(board.get(board.getLastIndex()).value * -1,
                    board.get(board.getLastIndex()).type), board.getLastIndex());
        else if (card.isDouble())
            board.set(new Card(board.get(board.getLastIndex()).value * 2,
                    board.get(board.getLastIndex()).type), board.getLastIndex());
        else board.add(card);
    }

    public boolean checkBoard(Player opponent) {
        if (board.sumValues() > 20) {
            System.out.println(name + " busted!");
            opponent.winSet();
            return true;
        }
        if (checkBluejack(board)) {
            System.out.println("Bluejack!");
            set = 3;
            return true;
        }
        /* Player sum is obviously less or equal to 20 if they
         * did not get caught by the statements above */
        if (board.getLastIndex()+1 == 9) {
            winSet();
            return true;
        }
        return false;
    }

    /**
     * Returns true if sum of values is 20 and all cards are blue
     */
    private static boolean checkBluejack(Deck board) {
        if(board == null) return false;
        if(board.sumValues() != 20) {
            return false;
        }
        boolean isAllBlue = true;
        for (int i = 0; i < board.getLastIndex() + 1; i++) {
            if (board.get(i).type != 0)
                isAllBlue = false;
        }
        return isAllBlue;
    }
}
