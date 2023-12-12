import java.util.Random;
import java.util.Scanner;


public class Main {

    public static boolean colorEnabled = false;

    public static final int BOARD_SIZE = 9;

    public static Deck gameDeck = new Deck();

    public static Deck playerDeck = new Deck();
    public static Deck playerHand = new Deck();
    public static Deck playerBoard = new Deck();

    public static Deck computerDeck = new Deck();
    public static Deck computerHand = new Deck();
    public static Deck computerBoard = new Deck();

    /**
     * Prompts user to enter an integer within a range.
     * @param min Minimum value expected from user to enter, inclusive.
     * @param max Maximum value expected from user to enter, inclusive.
     */
    private static int getInput(int min, int max) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("> ");
                int input = scan.nextInt();
                if (input >= min && input <= max) {
                    return input;
                }
            } catch (java.util.InputMismatchException e) {
                scan.next();
            }
            System.out.print("  invalid input, please enter again.\n");
        }
    }

    /**
     * Print out boards and hands of both the computer and player.
     * @param showComputerHand Whether to hide or show computer hand.
     */
    public static void displayGame(boolean showComputerHand) {
        System.out.print("\nCPU Hand         : ");
        if (showComputerHand) {
            computerHand.print(colorEnabled);
        } else {
            for (int i = 0; i <= computerHand.getLastIndex(); i++) {
                if (colorEnabled) {
                    System.out.print("[??]");
                } else {
                    System.out.print("[???]");
                }
            }
        }
        System.out.print("\nCPU Board    (" + String.format("%02d", computerBoard.sumValues()) + "): ");
        computerBoard.print(colorEnabled);
        System.out.print("\nPlayer Board (" + String.format("%02d", playerBoard.sumValues()) + "): ");
        playerBoard.print(colorEnabled);
        System.out.print("\nPlayer Hand      : ");
        playerHand.print(colorEnabled);
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
            playerDeck.add(gameDeck.remove(0));
            computerDeck.add(gameDeck.remove(gameDeck.getLastIndex()));
        }

        /* First 3 random cards without spacial probability */
        for (int i = 0; i < 3; i++) {
            playerDeck.add(Card.randomSignedCard(0));
            computerDeck.add(Card.randomSignedCard(0));
        }

        /* Last 2 cards with special probability */
        for (int i = 1; i <= 2; i++) {
            playerDeck.add(Card.randomSignedCard(i));
            computerDeck.add(Card.randomSignedCard(i));
        }

        /* Shuffle the decks so the cards drawn are random */
        playerDeck.shuffle();
        computerDeck.shuffle();

        /* Draw the first 4 cards from each players deck to be their hand */
        for (int i = 0; i < 4; i++) {
            playerHand.add(playerDeck.remove(i));
            computerHand.add(computerDeck.remove(i));
        }

        /* Make sure there is nothing on the board */
        playerBoard.clear();
        computerBoard.clear();
    }


    /**
     * Player actions. Returns true if player chose to stand.
     */
    public static boolean playerAction() {
        /* Draw the top card from the game deck and place it on player board */
        playerBoard.add(gameDeck.remove(gameDeck.getLastIndex()));

        /* Display the game so player can act accordingly. */
        displayGame(false);
        System.out.println();

        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
            * any cards in their hand, do not show the play card option */
            int action;
            if (playerHand.getLastIndex() == -1) {
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
                int cardIndex = getInput(-1, playerHand.getLastIndex());
                if(cardIndex == -1) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                playCard(playerHand.remove(cardIndex), playerBoard);
                return false;
            }
        }
    }

    /**
     * Computer actions and logic. Returns true if computer chose to stand.
     */
    public static boolean computerAction() {
        computerBoard.add(gameDeck.remove(gameDeck.getLastIndex()));
        //todo: add actual logic
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
        System.out.println("computer ends the turn");
        return false;
    }

    public static void playCard(Card card, Deck board) {
        if (card.isFlip()) {
            board.set(new Card(board.get(board.getLastIndex()).value * -1,
                    board.get(board.getLastIndex()).type), board.getLastIndex());
        } else if (card.isDouble()) {
            board.set(new Card(board.get(board.getLastIndex()).value * 2,
                    board.get(board.getLastIndex()).type), board.getLastIndex());
        } else {
            board.add(card);
        }
    }

    /**
     * Returns true if sum of values is 20 and all cards are blue
     */
    public static boolean checkBluejack(Deck board) {
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

        if (colorEnabled)
            System.out.print("\033[1;94m");
        System.out.println("   WELCOME TO BLUEJACK");
        if (colorEnabled)
            System.out.print("\u001B[0m");

        Scanner nameScanner = new Scanner(System.in);
        System.out.print("Enter player name\n> ");
        String playerName = nameScanner.next().trim();

        System.out.println("Dealing hands...");

        initGame();
        int playerSet = 0;
        int computerSet = 0;
        int setNum = 1;

        /* The main game loop */
        while (playerSet < 3 && computerSet < 3) {
            System.out.println("\n ==== SET " + setNum + " ==== ");

            playerBoard.clear();
            computerBoard.clear();
            boolean playerStand = false;
            boolean computerStand = false;
            /* Game loop for a set */
            do {
                /* Player turn */
                if (!playerStand) {
                    playerStand = playerAction();
                    if (playerBoard.sumValues() > 20) {
                        computerSet++;
                        System.out.println("Player busted!");
                        break;
                    }
                    if (checkBluejack(playerBoard)) {
                        playerSet = 3;
                        System.out.println("Bluejack!");
                        break;
                    }
                    /* Player sum is obviously less or equal to 20 if they
                    * did not get caught in the statement above */
                    if (playerBoard.getLastIndex()+1 == BOARD_SIZE) {
                        playerSet++;
                        break;
                    }
                }

                /* Computer turn */
                if (!computerStand) {
                    computerStand = computerAction();
                    if (computerBoard.sumValues() > 20) {
                        playerSet++;
                        System.out.println("CPU busted!");
                        break;
                    }
                    if (checkBluejack(computerBoard)) {
                        computerSet = 3;
                        System.out.println("Bluejack!");
                        break;
                    }
                    if (computerBoard.getLastIndex()+1 == BOARD_SIZE) {
                        computerSet++;
                        break;
                    }
                }
            } while (!(playerStand && computerStand));

            /* Set ended */

            if(playerStand && computerStand) {
                System.out.println(20 - playerBoard.sumValues());
                System.out.println(20 - computerBoard.sumValues());
                if (playerBoard.sumValues() == computerBoard.sumValues()) {
                    System.out.println("Tie");
                } else if (20 - playerBoard.sumValues() < 20 - computerBoard.sumValues()) {
                    playerSet++;
                    System.out.print("   ");
                    if (colorEnabled)
                        System.out.print("\033[1;90m" + "\033[42m");
                    System.out.print("   Player wins the set");
                    if (colorEnabled)
                        System.out.println("\u001B[0m");
                } else {
                    computerSet++;
                    System.out.print("   ");
                    if (colorEnabled)
                        System.out.print("\033[1;90m" + "\033[41m");
                    System.out.print("CPU wins the set");
                    if (colorEnabled) {
                        System.out.println("\u001B[0m");
                    }
                }
            }

            displayGame(true);
            System.out.println();
            System.out.println(playerSet + "-" + computerSet);
            setNum++;
        }

        if (playerSet == 3) {
            System.out.println("PLAYER WINS");
        } else {
            System.out.println("CPU WINS");
        }

        /* Game ended */
    }
}
