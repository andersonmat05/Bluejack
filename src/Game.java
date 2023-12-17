public class Game {

    public static Deck deck;
    public static Player user;
    public static Player cpu;
    private static int set;

    /**
     * Set up game and player decks.
     */
    public static void init() {
        /* initialize static variables */
        deck = new Deck();
        user = new Player("Player", SystemHelper.ANSI_POSITIVE_BG);
        cpu = new Player("CPU", SystemHelper.ANSI_NEGATIVE_BG);
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
            user.deck.add(deck.remove(0));
            cpu.deck.add(deck.remove(deck.getLastIndex()));
        }

        /* First 3 random cards without spacial probability */
        for (int i = 0; i < 3; i++) {
            user.deck.add(Card.randomSignedCard(0));
            cpu.deck.add(Card.randomSignedCard(0));
        }

        /* Last 2 cards with special probability */
        for (int i = 1; i <= 2; i++) {
            user.deck.add(Card.randomSignedCard(i));
            cpu.deck.add(Card.randomSignedCard(i));
        }

        /* Shuffle player decks so the cards drawn are random */
        user.deck.shuffle();
        cpu.deck.shuffle();

        /* Draw the first 4 cards from each players deck to be their hand */
        for (int i = 0; i < 4; i++) {
            user.hand.add(user.deck.remove(i));
            cpu.hand.add(cpu.deck.remove(i));
        }
    }

    /**
     * Print out boards and hands of both players.
     * @param showCPUHand Whether to hide or show computer hand.
     */
    public static void display(boolean showCPUHand) {
        System.out.print("\nCPU Hand         : ");
        if (showCPUHand) {
            cpu.hand.print();
        } else {
            for (int i = 0; i <= cpu.hand.getLastIndex(); i++) {
                if (SystemHelper.getColorEnabled()) {
                    System.out.print("[??]");
                } else {
                    System.out.print("[ ? ]");
                }
            }
        }
        System.out.print("\nCPU Board    (" + String.format("%02d", cpu.board.sumValues()) + "): ");
        cpu.board.print();
        System.out.print("\nPlayer Board (" + String.format("%02d", user.board.sumValues()) + "): ");
        user.board.print();
        System.out.print("\nPlayer Hand      : ");
        user.hand.print();
    }

    public static boolean setLoop() {
        boolean userStand = false;
        boolean cpuStand = false;
        /* Game loop for a set */
        do {
            Game.display(false);
            System.out.println();

            /* User turn */
            if (!userStand) {
                user.board.add(deck.remove(deck.getLastIndex()));
                userStand = user.inputAction();
                if (user.checkBoard(cpu))
                    return false;
            }

            /* CPU turn */
            if (!cpuStand) {
                cpu.board.add(deck.remove(deck.getLastIndex()));
                cpuStand = cpu.logicAction();
                if (cpu.checkBoard(user))
                    return false;
            }
        } while (!(userStand && cpuStand));
        /* Set ended with both players standing */
        return true;
    }

    public static void gameLoop() {
        /* The main game loop */
        while (user.getSet() < 3 && cpu.getSet() < 3) {
            System.out.println("\n   SET " + set+1 + "   ");

            /* Clean the board for the new set */
            user.board.clear();
            cpu.board.clear();

            if(setLoop()) {
                System.out.print("   ");
                /* Check boards if the set ended with both players standing */
                if (user.board.sumValues() == cpu.board.sumValues()) {
                    System.out.println("\n   Tie");
                    SystemHelper.println("\n   Tie", SystemHelper.ANSI_WHITE_BOLD + SystemHelper.ANSI_GRAY_BG);
                } else if (20 - user.board.sumValues() < 20 - cpu.board.sumValues()) {
                    user.winSet();
                } else {
                    cpu.winSet();
                }
            }

            display(true);
            System.out.println();
            System.out.println(user.getSet() + "-" + cpu.getSet());
            set++;
        }

        if (user.getSet() == 3) {
            user.winGame();
        } else {
            cpu.winGame();
        }
        /* Game ended */
    }

}
