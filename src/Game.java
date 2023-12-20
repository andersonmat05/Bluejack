public class Game {

    public static Deck deck;
    public static Player player1;
    public static Player player2;
    private static int set;

    /**
     * Set up game and player decks.
     */
    public static void init() {
        /* initialize static variables */
        deck = new Deck();
        player1 = new Player("Player", SystemHelper.ANSI_POSITIVE_BG, true);
        player2 = new Player("CPU", SystemHelper.ANSI_NEGATIVE_BG, true);
        set = 0;

        /* Create the game deck and shuffle */
        for (int color = 0; color <= 3; color++) {
            for (int value = 1; value <= 10; value++) {
                deck.add(new Card(value, color));
            }
        }
        deck.shuffle();

        /* Give players cards from top and bottom of game deck */
        for (int i = 0; i < 5; i++) {
            player1.deck.add(deck.remove(0));
            player2.deck.add(deck.remove(deck.getLastIndex()));
        }

        /* First 3 random cards without spacial probability */
        for (int i = 0; i < 3; i++) {
            player1.deck.add(Card.randomSignedCard(0));
            player2.deck.add(Card.randomSignedCard(0));
        }

        /* Last 2 cards with special probability */
        for (int i = 1; i <= 2; i++) {
            player1.deck.add(Card.randomSignedCard(i));
            player2.deck.add(Card.randomSignedCard(i));
        }

        /* Shuffle player decks so the cards drawn are random */
        player1.deck.shuffle();
        player2.deck.shuffle();

        /* Draw the first 4 cards from each players deck to be their hand */
        for (int i = 0; i < 4; i++) {
            player1.hand.add(player1.deck.remove(i));
            player2.hand.add(player2.deck.remove(i));
        }
    }

    /**
     * Print out boards and hands of both players.
     * @param showOpponentHand Whether to hide or show opponent hand.
     */
    public static void display(Player player, Player opponent, boolean showOpponentHand) {
        //todo: print names
        System.out.print("\nOpponent Hand      : ");
        if (showOpponentHand) {
            opponent.hand.print();
        } else {
            for (int i = 0; i <= opponent.hand.getLastIndex(); i++) {
                if (SystemHelper.getColorEnabled()) {
                    System.out.print("[??]");
                } else {
                    System.out.print("[ ? ]");
                }
            }
        }
        System.out.print("\nOpponent Board (" + String.format("%02d", opponent.board.sumValues()) + "): ");
        opponent.board.print();
        System.out.print("\nYour Board     (" + String.format("%02d", player.board.sumValues()) + "): ");
        player.board.print();
        System.out.print("\nYour Hand          : ");
        player.hand.print();
        System.out.println();
    }

    public static void setLoop() {
        boolean player1Stand = false;
        boolean player2Stand = false;
        /* Game loop for a set */
        do {

            /* Player 1 turn */
            if (!player1Stand) {
                player1.board.add(deck.remove(deck.getLastIndex()));

                Game.display(player1, player2, false);

                player1Stand = player1.action();
                if (player1.checkBoard(player2))
                    /* Set ended */
                    return;
            }

            /* Player 2 turn */
            if (!player2Stand) {
                player2.board.add(deck.remove(deck.getLastIndex()));
                player2Stand = player2.action();
                if (player2.checkBoard(player1))
                    /* Set ended */
                    return;
            }
        } while (!(player1Stand && player2Stand));
        /* Set ended with both players standing */

        /* Check boards */
        System.out.print("   ");
        if (player1.board.sumValues() == player2.board.sumValues()) {
            System.out.print("\n   ");
            SystemHelper.println("   Tie   ", SystemHelper.ANSI_WHITE_BOLD + SystemHelper.ANSI_GRAY_BG);
        } else if (20 - player1.board.sumValues() < 20 - player2.board.sumValues()) {
            player1.winSet();
        } else {
            player2.winSet();
        }
    }

    public static void gameLoop() {
        /* The main game loop */
        while (player1.getSet() < 3 && player2.getSet() < 3) {
            System.out.println("\n   SET " + ++set + "   ");

            /* Clean the board for the new set */
            player1.board.clear();
            player2.board.clear();

            setLoop();

            display(player1, player2, true);

            //todo: make fancier
            System.out.println(player1.getSet() + "-" + player2.getSet());
        }

        if (player1.getSet() == 3) {
            player1.winGame();
        } else {
            player2.winGame();
        }
        /* Game ended */
    }

}
