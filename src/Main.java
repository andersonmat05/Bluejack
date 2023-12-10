public class Main {
    public static void main(String[] args) {

        Deck gameDeck = new Deck();
        for (int i = 0; i <= 3; i++) {
            for (int j = 1; j <= 10; j++) {
                gameDeck.append(new Card(j, i));
            }
        }

        // Testing class functionality

        Deck d = new Deck();
        Deck q = new Deck(10);

        d.append(new Card(1,3));
        d.append(new Card(2,2));
        d.append(new Card(3,1));

        Card[] a = d.getArray();
        for(Card c : a)
        {
            System.out.print("[" + c.value + "," + c.color + "]");
        }
        d.remove(0);
        System.out.println();
        Card[] b = d.getArray();
        for(Card c : b)
        {
            System.out.print("[" + c.value + "," + c.color + "]");
        }
    }
}