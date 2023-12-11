import java.util.Random;
import java.util.Scanner;


public class Main {

    // Color codes for terminal
    //todo: does not work in cmd or power shell
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";

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
     * @param range The range of input is between 0 and range, both inclusive.
     */
    private static int getInput(int range) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("> ");
                int input = scan.nextInt();
                if (input >= 0 && input <= range) {
                    return input;
                }
            } catch (java.util.InputMismatchException e) {
                scan.next();
            }
            System.out.print("  invalid input, please enter again.\n");
        }
    }

    // For testing purposes
    @Deprecated
    public static void printDeck(Deck deck) {
        if(deck.getLastIndex() == -1) {
            System.out.print("Empty");
            return;
        }

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
    }

    @Deprecated
    public static void printGame() {
        System.out.print("\nComputer Hand      : ");
        printDeck(computerHand);
        System.out.print("\nComputer Board (" + String.format("%02d", computerBoard.sumValues()) + "): ");
        printDeck(computerBoard);
        System.out.print("\nPlayer Board   (" + String.format("%02d", playerBoard.sumValues()) + "): ");
        printDeck(playerBoard);
        System.out.print("\nPlayer Hand        : ");
        printDeck(playerHand);
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

        /* Make sure there is nothing left on the board */
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
        //todo: the function to display the game will be replaced
        printGame();

        Scanner scan = new Scanner(System.in);

        /* Prompt the player to choose an action. */
        //todo: check for range
        int action = 0;

        System.out.println("\nChoose action\n0: End  1: Play Card  2: Stand");
        action = getInput(2);


        if (action == 0) {
            /* If player chooses to end the turn, we simply return false
            * so game knows player is not standing yet. */
            return false;
        } else if (action == 1) {
            //todo: check for range
            System.out.println("Choose card index");
            int cardIndex = getInput(playerHand.getLastIndex());
            playCard(playerHand.remove(cardIndex), playerBoard);
        } else if (action == 2) {
            return true;
        }
        return false;
    }

    /**
     * Computer actions and logic. Returns true if computer chose to stand.
     */
    public static boolean computerAction() {
        computerBoard.add(gameDeck.remove(gameDeck.getLastIndex()));
        Random rand = new Random();
        if(rand.nextFloat() < 0.1f) {
            System.out.println("computer stands");
            return true;
        }
        if(rand.nextFloat() < 0.3) {
            System.out.println("computer plays a card");
            if (computerHand.getLastIndex() != -1)
                playCard(computerHand.remove(rand.nextInt(computerHand.getLastIndex())), computerBoard);
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
        //todo: a main menu to change settings? and enter a player name
        initGame();
        int playerSet = 0;
        int computerSet = 0;

        /* The main game loop */
        while (playerSet < 3 && computerSet < 3) {
            System.out.println("\n      SET BEGIN");

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
                        System.out.println("Player busted");
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
                        System.out.println("Computer busted");
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
                    System.out.println("tie");
                } else if (20 - playerBoard.sumValues() < 20 - computerBoard.sumValues()) {
                    playerSet++;
                    System.out.println("player wins the set");
                } else {
                    computerSet++;
                    System.out.println("computer wins the set");
                }
            }

            printGame();
            System.out.println();
            System.out.println(playerSet + "-" + computerSet);
        }

        if (playerSet == 3) {
            System.out.println("PLAYER WINS");
        } else {
            System.out.println("CPU WINS");
        }

        /* Game ended */
    }
}
