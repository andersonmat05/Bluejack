public class Main {

    // For testing purposes
    @Deprecated
    public static void printDeck(Deck deck) {
        Card[] a = deck.getArray();
        for(Card c : a)
        {
            System.out.print("[" + c.value + "," + c.color + "]");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Deck gameDeck = new Deck();
        for (int i = 0; i <= 3; i++) {
            for (int j = 1; j <= 10; j++) {
                gameDeck.append(new Card(j, i));
            }
        }

        Deck d = new Deck();
        Deck q = new Deck(10);

        d.append(new Card(1,3));
        d.append(new Card(2,2));
        d.append(new Card(3,1));

        printDeck(d);
        d.remove(1);
        printDeck(d);
        printDeck(q);
        q.clear();
        System.out.println();
        printDeck(gameDeck);
        gameDeck.shuffle();
        printDeck(gameDeck);
    }
}