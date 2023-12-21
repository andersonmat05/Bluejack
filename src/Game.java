public class Game {

    public static Deck deck;
    public static Player player1;
    public static Player player2;
    private static int set;
    private static final int CLEAR_LENGHT = 32;

    /**
     * Set up game and player decks.
     * @param cpu1 Whether player 1 controlled by user or cpu.
     * @param cpu2 Whether player 2 controlled by user or cpu.
     */
    public static void init(boolean cpu1, boolean cpu2) {
        /* initialize static variables */
        deck = new Deck();
        player1 = new Player("Player1", SystemHelper.ANSI_POSITIVE_BG, cpu1);
        player2 = new Player("Player2", SystemHelper.ANSI_NEGATIVE_BG, cpu2);
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
     */
    public static void display(Player player, Player opponent, boolean showHand1, boolean showHand2) {
        int labelLength = Math.max(player.name.length(), opponent.name.length());

        /* Print opponent hand */
        System.out.print("\n" + opponent.name + "'s Hand");
        for (int i = 0; i < (labelLength - opponent.name.length() + 6); i++) {
            System.out.print(" ");
        }
        System.out.print(": ");

        if (showHand2) {
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

        /* Print opponent board */
        System.out.print("\n" + opponent.name + "'s Board");
        for (int i = 0; i < (labelLength - opponent.name.length()); i++) {
            System.out.print(" ");
        }
        System.out.print(" (" + String.format("%02d", opponent.board.sumValues()) + "): ");
        opponent.board.print();

        /* Print player board */
        System.out.print("\n" + player.name + "'s Board");
        for (int i = 0; i < (labelLength - player.name.length()); i++) {
            System.out.print(" ");
        }
        System.out.print(" (" + String.format("%02d", player.board.sumValues()) + "): ");
        player.board.print();

        /* Print player hand */
        System.out.print("\n" + player.name + "'s Hand");
        for (int i = 0; i < (labelLength - player.name.length() + 6); i++) {
            System.out.print(" ");
        }
        System.out.print(": ");
        if (showHand1) {
            player.hand.print();
        } else {
            for (int i = 0; i <= player.hand.getLastIndex(); i++) {
                if (SystemHelper.getColorEnabled()) {
                    System.out.print("[??]");
                } else {
                    System.out.print("[ ? ]");
                }
            }
        }
        System.out.println();
    }

    /**
     * Handle player turn.
     */
    public static void playerTurn(Player player, Player opponent) {
        /* If game is PvP, wait for players to switch,
        so they don't see each other's hand. */
        if(!(player.isCpu() || opponent.isCpu())) {
            for (int i = 0; i < CLEAR_LENGHT; i++) {
                System.out.println();
            }
            System.out.print(player.name + "'s turn\n");
            SystemHelper.scanEnter();
        }

        if (Game.deck.getLastIndex() != -1)
            player.board.add(deck.remove(deck.getLastIndex()));

        if (!player.isCpu())
            Game.display(player, opponent, true, false);

        player.stand = player.action(opponent.board);
        if (player.stand)
            SystemHelper.println("  " + player.name + " stands.", SystemHelper.ANSI_WHITE_BOLD);
    }

    /**
     * Game loop for a set
     */
    public static void setLoop() {
        /* Loop until both players stand */
        do {
            /* Cancel set if no cards remain in game deck */
            if (Game.deck.getLastIndex() == -1)
                break;


            /* Player 1 turn */
            if (!player1.stand) {
                playerTurn(player1, player2);
                if (player1.checkBoard(player2))
                    /* End set */
                    return;
            }

            if (Game.deck.getLastIndex() == -1)
                break;

            /* Player 2 turn */
            if (!player2.stand) {
                playerTurn(player2, player1);
                if (player2.checkBoard(player1))
                    return;
            }

            /* If game is CvC display the game after both
            players' turn so viewers can see what is going on. */
            if (player1.isCpu() && player2.isCpu())
                Game.display(player1, player2, true, true);

        } while (!(player1.stand && player2.stand));
        /* Set ended with both players standing */

        /* Check boards */
        System.out.print("   ");
        if (player1.board.sumValues() == player2.board.sumValues()) {
            /* Tie condition */
            System.out.print("\n   ");
            SystemHelper.println("   Tie   ", SystemHelper.ANSI_WHITE_BOLD + SystemHelper.ANSI_GRAY_BG);
        } else if (20 - player1.board.sumValues() < 20 - player2.board.sumValues()) {
            /* Player 1 wins */
            player1.winSet();
        } else {
            /* Player 2 wins */
            player2.winSet();
        }
    }

    private static boolean gameLoopCondition() {
        return Game.deck.getLastIndex() != -1 && (player1.getSet() < 3 && player2.getSet() < 3);
    }

    public static void gameLoop() {
        /* Loop until a player reaches score of 3 */
        while (gameLoopCondition()) {
            /* Display set number */
            SystemHelper.println("\n   SET " + ++set, SystemHelper.ANSI_WHITE_BOLD);

            /* Clean the board for the new set */
            player1.board.clear();
            player2.board.clear();
            player1.stand = false;
            player2.stand = false;

            setLoop();

            /* Display the end result of the board after set ended, hide hands if game continues */
            if (gameLoopCondition()) {
                display(player1, player2, false, false);
            } else {
                display(player1, player2, true, true);

            }

            SystemHelper.println("   " + player1.getSet() + " - " + player2.getSet(),
                    SystemHelper.ANSI_CYAN_BOLD);

            if (!(player1.isCpu() || player2.isCpu()))
                SystemHelper.scanEnter();
        }
        /* Game ended */

        /* Check who wins */
        if (player1.getSet() > player2.getSet()) {
            player1.winGame();
        } else if (player2.getSet() > player1.getSet()) {
            player2.winGame();
        } else {
            /* Tie condition */
            System.out.print("\n   ");
            SystemHelper.println("   Tie   ", SystemHelper.ANSI_WHITE_BOLD + SystemHelper.ANSI_GRAY_BG);
        }

        SystemHelper.saveResult(player1, player2);
    }
}
