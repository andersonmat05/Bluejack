import java.util.Random;


public class Deck {
    private Card[] cards;

    /* Escape codes used in print method. */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Generates empty deck.
     */
    public Deck() {
        cards = new Card[0];
    }

    /**
     * Get card at specified index.
     * @param index Index of the card.
     */
    public Card get(int index) {
        return cards[index];
    }

    /**
     * Replace card at specified index.
     * @param card Card object.
     * @param index Index of the card.
     */
    public void set(Card card, int index) {
        cards[index] = card;
    }

    /**
     * Returns index of the last card in the deck.
     * Returns -1 if deck is empty.
     */
    public int getLastIndex() {
        return cards.length-1;
    }

    // For testing purposes
    @Deprecated
    public Card[] getArray() {
        return cards;
    }

    /**
     * Append a new card to the end of the deck.
     */
    public void add(Card card) {
        /* Size of the original array */
        int n = cards.length;
        Card[] newCards = new Card[n+1];
        /* Copy contents of original to new */
        System.arraycopy(cards, 0, newCards, 0, n);
        /* Set the last element to specified card */
        newCards[n] = card;
        /* Reflect result to original array */
        cards = newCards;
    }

    /**
     * Remove the card at specified index.
     * Returns the card removed.
     * @param index Index of the card to remove
     */
    public Card remove(int index) {
        /* Size of the original array */
        int n = cards.length;
        Card[] newCards = new Card[n-1];
        /* Store the card to remove */
        Card card = cards[index];
        /* Iterate for every element of old array */
        for (int i = 0, k = 0; i < n; i++) {
            /* Skip the removed element */
            if(i != index) {
                newCards[k] = cards[i];
                k++;
            }
        }
        /* Reflect result to original array */
        cards = newCards;
        return card;
    }

    /**
     * Remove all cards from the deck.
     */
    public void clear() {
        cards = new Card[0];
    }

    /**
     * Shuffle cards in the deck using Fisher–Yates shuffle algorithm.
     */
    public void shuffle() {
        Random rand = new Random();

        /* Iterate through the array starting from the last element */
        for (int i = cards.length-1; i > 0; i--) {

            /* Pick a random index from 0 to i */
            int j = rand.nextInt(i+1);

            /* Swap element at i with random index */
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    /**
     * Returns the sum of all values inside the deck.
     */
    public int sumValues() {
        int sum = 0;
        for (Card card : cards) {
            sum += card.value;
        }
        return sum;
    }

    /**
     * Prints out all the cards present in the deck.
     * @param color Whether to use ANSI color codes.
     */
    public void print(boolean color) {

        /* End the method here if no cards are present */
        if(cards.length == 0) {
            System.out.print("Empty");
            return;
        }

        for(Card card : cards)
        {
            if (color) {
                switch (card.type) {
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
                        // Special
                        System.out.print(ANSI_RESET);
                        break;
                }
            }
            if (card.isFlip()) {
                if (color) {
                    System.out.print("[+-]");
                } else {
                    System.out.print("[+- ]");
                }
            } else if (card.isDouble()) {
                if (color) {
                    System.out.print("[x2]");
                } else {
                    System.out.print("[2x ]");
                }
            } else {
                System.out.print("[" + String.format("%02d", card.value));
                if (!color) {
                    switch (card.type) {
                        case 0:
                            // Blue
                            System.out.print("B");
                            break;
                        case 1:
                            // Yellow
                            System.out.print("Y");
                            break;
                        case 2:
                            // Red
                            System.out.print("R");
                            break;
                        case 3:
                            // Green
                            System.out.print("G");
                            break;
                        default:
                            break;
                    }
                }
                System.out.print("]");
            }
        }
        if (color) {
            System.out.print(ANSI_RESET);
        }
    }
}
