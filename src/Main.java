public class Main {

    public static Deck gameDeck = new Deck();

    public static Player user = new Player("Player", SystemHelper.ANSI_POSITIVE_BG);

    public static Player cpu = new Player("CPU", SystemHelper.ANSI_NEGATIVE_BG);

    /**
     * Print out boards and hands of both the computer and player.
     * @param showComputerHand Whether to hide or show computer hand.
     */
    public static void displayGame(boolean showComputerHand) {
        System.out.print("\nCPU Hand         : ");
        if (showComputerHand) {
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
            user.deck.add(gameDeck.remove(0));
            cpu.deck.add(gameDeck.remove(gameDeck.getLastIndex()));
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

        /* Shuffle the decks so the cards drawn are random */
        user.deck.shuffle();
        cpu.deck.shuffle();

        /* Draw the first 4 cards from each players deck to be their hand */
        for (int i = 0; i < 4; i++) {
            user.hand.add(user.deck.remove(i));
            cpu.hand.add(cpu.deck.remove(i));
        }

        /* Make sure there is nothing on the board */
        user.board.clear();
        cpu.board.clear();
    }


    /**
     * Player actions. Returns true if player chose to stand.
     */
    public static boolean playerAction(Player player) {

        /* Draw the top card from the game deck and place it on player board */
        player.board.add(gameDeck.remove(gameDeck.getLastIndex()));

        /* Display the game so player can act accordingly. */
        displayGame(false);
        System.out.println();

        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
            * any cards in their hand, do not show the play card option */
            int action;
            if (player.hand.getLastIndex() == -1) {
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
                int cardIndex = SystemHelper.scanIntRange(-1, player.hand.getLastIndex());
                if(cardIndex == -1) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                player.playCard(cardIndex);
                return false;
            }
        }
    }

    /**
     * Computer actions and logic. Returns true if computer chose to stand.
     */
    public static boolean computerAction() {
        cpu.board.add(gameDeck.remove(gameDeck.getLastIndex()));
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


    public static void main(String[] args) {

        System.out.println("0: Disable colors  1: Enable colors");
        SystemHelper.setColorEnabled(SystemHelper.scanIntRange(0, 1) == 1);

        SystemHelper.println("   WELCOME TO BLUEJACK", SystemHelper.ANSI_BLUE_BOLD);

        System.out.print("Enter player name\n> ");
        user.name = SystemHelper.scanString();

        System.out.println("Dealing hands...");

        initGame();
        int set = 1;

        /* The main game loop */
        while (user.getSet() < 3 && cpu.getSet() < 3) {
            System.out.println("\n ==== SET " + set + " ==== ");

            user.board.clear();
            cpu.board.clear();

            boolean playerStand = false;
            boolean computerStand = false;

            /* Game loop for a set */
            do {
                /* Player turn */
                if (!playerStand) {
                    playerStand = playerAction(user);
                    if (user.checkBoard(cpu))
                        break;
                }

                /* Computer turn */
                if (!computerStand) {
                    computerStand = playerAction(cpu);
                    if (cpu.checkBoard(user))
                        break;
                }
            } while (!(playerStand && computerStand));
            /* Set ended */

            /* Check boards if the set ended with both players standing */
            if(playerStand && computerStand) {
                System.out.print("   ");
                if (user.board.sumValues() == cpu.board.sumValues()) {
                    System.out.println("\n   Tie");
                } else if (20 - user.board.sumValues() < 20 - cpu.board.sumValues()) {
                    user.winSet();
                } else {
                    cpu.winSet();
                }
            }

            displayGame(true);
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
