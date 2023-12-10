public class Main {

    // Color codes for terminal
    //todo: does not work in cmd or power shell
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static Deck gameDeck = new Deck();
    public static Deck playerDeck = new Deck();
    public static Deck playerHand = new Deck();
    public static Deck computerDeck = new Deck();
    public static Deck computerHand = new Deck();

    // For testing purposes
    @Deprecated
    public static void printDeck(Deck deck) {
        Card[] a = deck.getArray();
        for(Card c : a)
        {
            switch (c.type) {
                case 0:
                    // Blue
                    System.out.print(ANSI_BLUE);
                    break;
                case 1:
                    // Yellow
                    System.out.print(ANSI_YELLOW);
                    break;
                case 2:
                    // Red
                    System.out.print(ANSI_RED);
                    break;
                case 3:
                    // Green
                    System.out.print(ANSI_GREEN);
                    break;
                default:
                    System.out.print(ANSI_RESET);
                    break;
            }
            if(c.isFlip()) {
                System.out.print(ANSI_WHITE + "[" + "+-" + "]");
            } else if (c.isDouble()) {
                System.out.print(ANSI_WHITE + "[" + "x2" + "]");
            } else {
                System.out.print("[" + String.format("%02d", c.value) + "]");
            }
        }
        System.out.print(ANSI_RESET);
        System.out.println();
    }

    /**
     * Set up game and player decks.
     */
    public static void initGame() {
        /* Create the game deck and shuffle */
        for (int color = 0; color <= 3; color++) {
            for (int value = 1; value <= 10; value++) {
                gameDeck.add(new Card(value, color));
            }
        }
        gameDeck.shuffle();

        /* Give players cards from top and bottom of game deck */
        for (int i = 0; i < 5; i++) {
            computerDeck.add(gameDeck.remove(gameDeck.getLastIndex()));

            playerDeck.add(gameDeck.remove(0));
        }

        /* First 3 random cards without spacial probability */
        for (int i = 0; i < 3; i++) {
            computerDeck.add(Card.randomSignedCard(0));
            playerDeck.add(Card.randomSignedCard(0));
        }

        /* Last 2 cards with special probability */
        for (int i = 1; i <= 2; i++) {
            computerDeck.add(Card.randomSignedCard(i));
            playerDeck.add(Card.randomSignedCard(i));
        }

        playerDeck.shuffle();
        computerDeck.shuffle();

        /* Draw 4 random cards from each players deck to be their hand */
        for (int i = 0; i < 4; i++) {
            playerHand.add(playerDeck.remove(i));
            computerHand.add(computerDeck.remove(i));
        }
    }

    public static boolean playerTurn() {
        return true;
    }

    public static void main(String[] args) {
        initGame();

        printDeck(gameDeck);
        printDeck(computerDeck);
        printDeck(computerHand);
        printDeck(playerDeck);
        printDeck(playerHand);


    }
}