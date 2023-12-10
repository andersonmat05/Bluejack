import java.util.Random;


public class Deck {
    private Card[] cards;

    /**
     * Generate empty deck
     */
    public Deck() {
        cards = new Card[0];
    }

    /**
     * Generate random cards while creating the array
     * @param length Number of cards to be generated
     */
    public Deck(int length) {
        Random rand = new Random();
        cards = new Card[length];

        for (int i = 0; i < length; i++) {
            cards[i] = new Card(1, 0);
        }
    }

    /**
     * Get a card at specified index
     * @param index Index of the card
     */
    public Card get(int index) {
        return cards[index];
    }

    /**
     * Check if there are any cards in the deck
     */
    public boolean isEmpty() {
        return (cards.length == 0);
    }

    // For testing purposes
    @Deprecated
    public Card[] getArray() {
        return cards;
    }

    /**
     * Append a new card to the end of the deck
     * @param card Card to append
     */
    public void append(Card card) {
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
     * Remove the card at specified index
     * @param index Index of the card to remove
     */
    public void remove(int index) {
        /* Size of the original array */
        int n = cards.length;
        Card[] newCards = new Card[n-1];
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
    }

    /**
     * Remove all cards from the deck
     */
    public void clear() {
        cards = new Card[0];
    }

    /**
     * Shuffle existing cards in the deck
     */
    public void shuffle() {
        //todo
    }
}
