public class Game {

    public static Deck deck;
    public static Player player1;
    public static Player player2;
    private static int set;

    /**
     * Set up game and player decks.
     */
    public static void init(boolean cpu1, boolean cpu2) {
        /* initialize static variables */
        deck = new Deck();
        player1 = new Player("Player 1", SystemHelper.ANSI_POSITIVE_BG, cpu1);
        player2 = new Player("Player 2", SystemHelper.ANSI_NEGATIVE_BG, cpu2);
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

    /**
     * Returns true if set ended
     */
    public static void playerTurn(Player player, Player opponent) {
        if (Game.deck.getLastIndex() != -1)
            player.board.add(deck.remove(deck.getLastIndex()));

        if (!player.isCpu())
            Game.display(player, opponent, false);

        player.stand = player.action(opponent.board);
    }

    public static void setLoop() {
        player1.stand = false;
        player2.stand = false;
        /* Game loop for a set */
        do {
            if(!(player1.isCpu() || player2.isCpu()))
                SystemHelper.pressEnter();

            /* Player 1 turn */
            if (!player1.stand) {
                playerTurn(player1, player2);
                if (player1.checkBoard(player2))
                    return;
            }

            if(!(player1.isCpu() || player2.isCpu()))
                SystemHelper.pressEnter();

            /* Player 2 turn */
            if (!player2.stand) {
                playerTurn(player2, player1);
                if (player2.checkBoard(player1))
                    return;
            }

            if (player1.isCpu() && player2.isCpu())
                Game.display(player1, player2, true);
        } while (!(player1.stand && player2.stand));
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
            if (set == 10)
                break;
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
