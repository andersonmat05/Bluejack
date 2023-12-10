
public class Card {
    public int value; // 0: Flip card

    public int color; // 0: Blue, 1: Yellow, 2: Red ,3: Green

    public Card(int value, int color) {
        this.value = value;
        this.color = color;
    }

    /**
     * Return if this is a flip card or not
     */
    public boolean isFlip() {
        return (this.value == 0);
    }

}
