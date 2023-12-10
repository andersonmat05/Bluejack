import javax.lang.model.type.NullType;
import java.util.Random;


public class Deck {
    private Card[] cards;

    /**
     * Generate empty deck.
     */
    public Deck() {
        cards = new Card[0];
    }

    /**
     * Get a card at specified index.
     * @param index Index of the card.
     */
    public Card get(int index) {
        return cards[index];
    }

    /**
     * Set a card at specified index.
     * @param card New card object.
     * @param index Index of the card.
     */
    public void set(Card card, int index) {
        cards[index] = card;
    }

    /**
     * Returns number of cards in the deck. Returns -1 if deck is empty
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
     * Append a new card to the end of the deck. Returns index of the new card.
     * @param card Card to append
     */
    public int add(Card card) {
        /* Size of the original array */
        int n = cards.length;
        Card[] newCards = new Card[n+1];
        /* Copy contents of original to new */
        System.arraycopy(cards, 0, newCards, 0, n);
        /* Set the last element to specified card */
        newCards[n] = card;
        /* Reflect result to original array */
        cards = newCards;
        return n;
    }

    /**
     * Remove the card at specified index. Returns the card removed.
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
     * Remove all cards from the deck
     */
    public void clear() {
        cards = new Card[0];
    }

    /**
     * Shuffle existing cards in the deck using Fisher–Yates shuffle algorithm
     */
    public void shuffle() {
        Random rand = new Random();

        for (int i = cards.length-1; i > 0; i--) {

            /* Pick a random index from 0 to i */
            int j = rand.nextInt(i+1);

            /* Swap element at i with random index */
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }
}
