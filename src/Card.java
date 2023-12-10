import java.util.Random;


public class Card {
    public int value; // 0: Null

    public int type; // 0: Blue, 1: Yellow, 2: Red, 3: Green, 4 Flip, 5, Double

    /**
     * Default constructor
     */
    public Card(int value, int type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Returns whether this is a double card or not.
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

        if (special != 0 && rand.nextFloat() < 0.2f) {
            return new Card(0, special+3);
        } else {
            int value = rand.nextInt(6) + 1;
            int color = rand.nextInt(4);
            int sign = rand.nextInt(2);
            return new Card(value * (sign * 2 - 1), color);
        }
    }
}
