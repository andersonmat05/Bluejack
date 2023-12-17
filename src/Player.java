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
                    board.get(board.getLastIndex()).type),
                    board.getLastIndex());
        else if (card.isDouble())
            board.set(new Card(board.get(board.getLastIndex()).value * 2,
                    board.get(board.getLastIndex()).type),
                    board.getLastIndex());
        else board.add(card);
    }

    /**
     * Checks the board of the player.
     * Returns true if handled.
     */
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

    /**
     * Player actions. Returns true if player chose to stand.
     */
    public boolean inputAction() {
        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
             * any cards in their hand, do not show the play card option */
            int action;
            if (hand.getLastIndex() == -1) {
                System.out.println("Choose action\n0: End  1: Stand");
                action = SystemHelper.scanIntRange(0, 1);
            } else {
                System.out.println("Choose action\n0: End  1: Stand  2: Play Card");
                action = SystemHelper.scanIntRange(0, 2);
            }

            if (action == 0) {
                /* If player chooses to end the turn, we simply return false
                 * so game knows player is not standing yet. */
                return false;
            } else if (action == 1) {
                /* Return true so game loop knows player is standing. */
                return true;
            } else if (action == 2) {
                /* Prompt user to choose a card index */
                System.out.println("Choose card index (-1 to cancel)");
                int cardIndex = SystemHelper.scanIntRange(-1, hand.getLastIndex());
                if(cardIndex == -1) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                playCard(cardIndex);
                return false;
            }
        }
    }

    public boolean logicAction() {
        //todo: add actual logic
        /*
        Random rand = new Random();
        if(rand.nextFloat() < 0.1f) {
            System.out.println("computer stands");
            return true;
        }
        if(rand.nextFloat() < 0.3) {
            System.out.println("computer plays a card");
            if (computerHand.getLastIndex() > 0) {
                playCard(computerHand.remove(rand.nextInt(computerHand.getLastIndex())), computerBoard);
            } else if (computerHand.getLastIndex() == 0) {
                playCard(computerHand.remove(0), computerBoard);
            }
        }
         */
        System.out.println("CPU ends the turn");
        return false;
    }
}
