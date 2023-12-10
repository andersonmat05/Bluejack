import java.util.Random;


public class Card {
    public int value;

    /* Used for storing both the color and special card type.
    * 0: Blue, 1: Yellow, 2: Red, 3: Green, 4 Flip, 5, Double */
    public int type;

    /**
     * Default constructor
     */
    public Card(int value, int type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Returns whether this is a flip card or not.
     */
    public boolean isFlip() {
        return (this.type == 4);
    }

    /**
     * Returns whether this is a double card or not.
     */
    public boolean isDouble() {
        return (this.type == 5);
    }

    /**
     *  Returns a card with random signed value and color or a special card.
     * @param special 0: No special cards, 1: Flip card, 2: Double card
     */
    public static Card randomSignedCard(int special) {
        Random rand = new Random();

        /* %20 percent chance for a special card is checked using
        * nextFloat method that returns a value between 0-1 */
        if (special != 0 && rand.nextFloat() < 0.2f) {
            /* Color types end at 3, any value greater than that means
            * special card. In this implementation there is no need for
            * checking what special card we return, therefore adding cards
            * just require to update the playCard method in Main class. */
            return new Card(0, special+3);
        } else {
            int value = rand.nextInt(6) + 1;
            int color = rand.nextInt(4);
            int sign = rand.nextInt(2);
            /* Sign is either 0 or 1; by multiplying with 2 and
            * subtracting 1 we get -1 or 1, which is multiplied with
            * the actual value to get negative and positive random values. */
            return new Card(value * (sign * 2 - 1), color);
        }
    }
}
