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

    //todo: not implemented yet
    public static final int BOARD_SIZE = 9;

    public static Deck gameDeck = new Deck();

    public static Deck playerDeck = new Deck();
    public static Deck playerHand = new Deck();
    public static Deck playerBoard = new Deck();

    public static Deck computerDeck = new Deck();
    public static Deck computerHand = new Deck();
    public static Deck computerBoard = new Deck();

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
        System.out.print("\nComputer Hand : ");
        printDeck(computerHand);
        System.out.print("\nComputer Board: ");
        printDeck(computerBoard);
        System.out.print("\nPlayer Board  : ");
        printDeck(playerBoard);
        System.out.print("\nPlayer Hand   : ");
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
    public static boolean playerTurn() {
        /* Draw the top card from the game deck and place it on player board */
        playerBoard.add(gameDeck.remove(gameDeck.getLastIndex()));

        /* Display the game so player can act accordingly. */
        //todo: the function to display the game will be replaced
        printGame();

        Scanner sc = new Scanner(System.in);

        /* Prompt the player to choose an action. */
        //todo: formatting may be updated, change numbers to letters?
        //todo: check for range
        System.out.print("\nChoose action\n0: End  1: Play Card  2: Stand\n> ");
        int input = sc.nextInt();

        if (input == 0) {
            /* If player chooses to end the turn, we simply return false
            * so game knows player is not standing yet. */
            return false;
        } else if (input == 1) {
            //todo: check for range
            System.out.print("Choose card index\n> ");
            int cardIndex = sc.nextInt();
            playCard(playerHand.remove(cardIndex), playerBoard);
        } else if (input == 2) {
            return true;
        }
        return false;
    }

    public static boolean computerTurn() {
        computerBoard.add(gameDeck.remove(gameDeck.getLastIndex()));
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

    public static void main(String[] args) {
        //todo: a main menu to change settings? and enter a player name
        initGame();

        boolean playerStand = false;
        boolean computerStand = false;

        do {
            if (!playerStand)
                playerStand = playerTurn();
            if (!computerStand)
                computerStand = computerTurn();
        } while (!playerStand || !computerStand);
    }
}