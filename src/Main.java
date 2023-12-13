import java.util.Scanner;


public class Main {

    public static boolean colorEnabled = false;

    public static final String ANSI_POSITIVE = "\033[42m";
    public static final String ANSI_NEGATIVE = "\033[41m";

    public static final String PLAYER_SET = "  Player wins the set  ";
    public static final String CPU_SET = "  CPU wins the set  ";

    public static final int BOARD_SIZE = 9;

    public static Deck gameDeck = new Deck();

    public static Player user = new Player("Player");

    public static Player cpu = new Player("CPU");

    private static void printColor(String string, String code) {
        if (colorEnabled)
            System.out.print(code);
        System.out.print(string);
        /* Reset color */
        if (colorEnabled)
            System.out.print("\u001B[0m");
        System.out.println();
    }

    /**
     * Prompts user to enter an integer within a range.
     * @param min Minimum value expected from user to enter, inclusive.
     * @param max Maximum value expected from user to enter, inclusive.
     */
    private static int getInput(int min, int max) {
        Scanner scan = new Scanner(System.in);
        /* Keep user in the loop until valid input is entered */
        while (true) {
            try {
                System.out.print("> ");
                int input = scan.nextInt();
                /* Check for range */
                if (input >= min && input <= max) {
                    return input;
                }
            } catch (java.util.InputMismatchException e) {
                /* Consume the input */
                scan.next();
            }
            System.out.print("  Invalid input, please enter again.\n");
        }
    }

    /**
     * Print out boards and hands of both the computer and player.
     * @param showComputerHand Whether to hide or show computer hand.
     */
    public static void displayGame(boolean showComputerHand) {
        System.out.print("\nCPU Hand         : ");
        if (showComputerHand) {
            cpu.hand.print(colorEnabled);
        } else {
            for (int i = 0; i <= cpu.hand.getLastIndex(); i++) {
                if (colorEnabled) {
                    System.out.print("[??]");
                } else {
                    System.out.print("[???]");
                }
            }
        }
        System.out.print("\nCPU Board    (" + String.format("%02d", cpu.board.sumValues()) + "): ");
        cpu.board.print(colorEnabled);
        System.out.print("\nPlayer Board (" + String.format("%02d", user.board.sumValues()) + "): ");
        user.board.print(colorEnabled);
        System.out.print("\nPlayer Hand      : ");
        user.hand.print(colorEnabled);
    }


    /**
     * Set up game and player decks.
     */
    public static void initGame() {

        user.color = ANSI_POSITIVE;
        cpu.color = ANSI_NEGATIVE;

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
    public static boolean playerAction() {
        /* Draw the top card from the game deck and place it on player board */
        user.board.add(gameDeck.remove(gameDeck.getLastIndex()));

        /* Display the game so player can act accordingly. */
        displayGame(false);
        System.out.println();

        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
            * any cards in their hand, do not show the play card option */
            int action;
            if (user.hand.getLastIndex() == -1) {
                System.out.println("Choose action\n0: End  1: Stand");
                action = getInput(0, 1);
            } else {
                System.out.println("Choose action\n0: End  1: Stand  2: Play Card");
                action = getInput(0, 2);
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
                int cardIndex = getInput(-1, user.hand.getLastIndex());
                if(cardIndex == -1) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                playCard(user.hand.remove(cardIndex), user.board);
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

    /**
     * Handles the special conditions and adds the card to the board.
     * @param card The card to be played.
     * @param board The board to place the card.
     */
    public static void playCard(Card card, Deck board) {
        if(card == null) return;
        if (card.isFlip())
            board.set(new Card(board.get(board.getLastIndex()).value * -1,
                board.get(board.getLastIndex()).type), board.getLastIndex());
        else if (card.isDouble())
            board.set(new Card(board.get(board.getLastIndex()).value * 2,
                board.get(board.getLastIndex()).type), board.getLastIndex());
        else board.add(card);
    }

    public static boolean checkPlayer(Player player, Player opponent) {
        if (player.board.sumValues() > 20) {
            System.out.println("Player busted!");
            opponent.winSet();
            return true;
        }
        if (checkBluejack(player.board)) {
            System.out.println("Bluejack!");
            player.winGame();
            return true;
        }
        /* Player sum is obviously less or equal to 20 if they
         * did not get caught by the statements above */
        if (player.board.getLastIndex()+1 == BOARD_SIZE) {
            player.winSet();
            return true;
        }
        return false;
    }

    /**
     * Returns true if sum of values is 20 and all cards are blue
     */
    public static boolean checkBluejack(Deck board) {
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

    public static void main(String[] args) {

        System.out.println("0: Disable colors  1: Enable colors");
        colorEnabled = getInput(0, 1) == 1;

        Player.setColorEnabled(colorEnabled);
        Deck.setColorEnabled(colorEnabled);

        printColor("   WELCOME TO BLUEJACK", "\033[1;94m");

        Scanner nameScanner = new Scanner(System.in);
        System.out.print("Enter player name\n> ");
        user.name = nameScanner.next().trim();

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
                    playerStand = playerAction();
                    if (checkPlayer(user, cpu))
                        break;
                }

                /* Computer turn */
                if (!computerStand) {
                    computerStand = computerAction();
                    if (checkPlayer(cpu, user))
                        break;
                }
            } while (!(playerStand && computerStand));
            /* Set ended */

            /* Check boards if the set ended with both players standing */
            if(playerStand && computerStand) {
                System.out.println(20 - user.board.sumValues());
                System.out.println(20 - cpu.board.sumValues());
                System.out.print("   ");
                if (user.board.sumValues() == cpu.board.sumValues()) {
                    System.out.println("Tie");
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
