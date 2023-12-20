public class Player {

    /* Player related variables */
    public String name;
    private final String color;
    private final boolean cpu;

    /* Game related variables */
    public Deck deck;
    public Deck board;
    public Deck hand;
    public boolean stand;
    private int set;


    public Player(String name, String color, boolean cpu) {
        this.name = name;
        this.color = color;
        this.cpu = cpu;

        this.deck = new Deck();
        this.board = new Deck();
        this.hand = new Deck();
        this.stand = false;
        this.set = 0;
    }

    public boolean isCpu() {
        return cpu;
    }

    public void setName(String name) {
        if(!name.isEmpty())
            this.name = name.trim();
    }

    public int getSet() {
        return set;
    }

    public void winSet() {
        set++;
        System.out.print("\n   ");
        SystemHelper.println("   " + name + " wins the set!   ", color + SystemHelper.ANSI_WHITE_BOLD);
    }

    public void winGame() {
        System.out.print("\n   ");
        SystemHelper.println("   " + name + " wins the game!   ", color + SystemHelper.ANSI_WHITE_BOLD);
    }

    /**
     * Play a card from player's hand at specified index.
     * @param cardIndex Index of the card to play.
     */
    public void playCard(int cardIndex) {
        Card card = hand.remove(cardIndex);
        if (card.isFlip())
            board.set(new Card(board.get(board.getLastIndex()).value * -1,
                    board.get(board.getLastIndex()).type),
                    board.getLastIndex());
        else if (card.isDouble())
            board.set(new Card(board.get(board.getLastIndex()).value * 2,
                    board.get(board.getLastIndex()).type),
                    board.getLastIndex());
        else board.add(card);
    }

    /**
     * Checks the board of the player.
     * Returns true if handled.
     */
    public boolean checkBoard(Player opponent) {
        if (board.sumValues() > 20) {
            System.out.println(name + " busted!");
            opponent.winSet();
            return true;
        }
        if (checkBluejack(board)) {
            System.out.println("Bluejack!");
            set = 3;
            return true;
        }
        /* Player sum is obviously less or equal to 20 if they
         * did not get caught by the statements above */
        if (board.getLastIndex()+1 == 9) {
            winSet();
            return true;
        }
        return false;
    }

    /**
     * Returns true if sum of values is 20 and all cards are blue
     */
    private static boolean checkBluejack(Deck board) {
        if(board == null) return false;
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

    public boolean action(Deck opponentBoard) {
        if(cpu)
            return logicAction(opponentBoard);
        return inputAction();
    }

    /**
     * Prompts user to select action. Returns true if user chose to stand.
     */
    private boolean inputAction() {
        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
             * any cards in their hand, do not show the play card option */
            int action;
            if (hand.getLastIndex() == -1) {
                System.out.println("Choose action\n0: End  1: Stand");
                action = SystemHelper.scanIntRange(0, 1);
            } else {
                System.out.println("Choose action\n0: End  1: Stand  2: Play Card");
                action = SystemHelper.scanIntRange(0, 2);
            }

            if (action == 0) {
                /* If player chooses to end the turn, we simply return false
                 * so game knows player is not standing yet. */
                return false;
            } else if (action == 1) {
                /* Return true so game loop knows player is standing. */
                return true;
            } else if (action == 2) {
                /* Prompt user to choose a card index */
                System.out.println("Choose card index (-1 to cancel)");
                int cardIndex = SystemHelper.scanIntRange(-1, hand.getLastIndex());
                if(cardIndex == -1) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                playCard(cardIndex);
                return false;
            }
        }
    }

    /**
     * Returns the new sum if this card is played.
     */
    private int playResult(Card card) {
        if (card.type == 4) {
            return board.get(board.getLastIndex()).value * -1
                    + board.sumValues() - board.get(board.getLastIndex()).value;
        }
        if (card.type == 5) {
            return board.get(board.getLastIndex()).value * 2
                    + board.sumValues() - board.get(board.getLastIndex()).value;
        }
        /* Return addition if not special card */
        return board.sumValues() + card.value;
    }

    /**
     * CPU actions. Returns true if cpu decides to stand.
     */
    private boolean logicAction(Deck opponentBoard) {
        /* Stand if winning is guaranteed */
        if (board.sumValues() == 20)
            return true;

        /* DEFENSIVE BEHAVIOUR */
        if (board.sumValues() > 20) {
            if (hand.getLastIndex() == -1)
                return false;

            /* Select the best card to play */
            boolean playCard = false;
            int cardIndex = 0;
            /* Iterate through all cards in hand */
            for (int i = 1; i <= hand.getLastIndex(); i++) {
                Card card = hand.get(i);
                if (card.value == 0) {
                    /* Special card found */
                    int currentSpecialResult = playResult(card);
                    if (currentSpecialResult <= 20) {
                        // Check if it is better than last card
                        if (playResult(hand.get(cardIndex)) < currentSpecialResult || !playCard) {
                            cardIndex = i;
                            playCard = true;
                        }
                    }
                } else {
                    /* Not special card */
                    if (playResult(card) <= 20 &&
                            card.value < hand.get(cardIndex).value) {
                        cardIndex = i;
                        playCard = true;
                    }
                }
            }
            if(playCard) {
                playCard(cardIndex);
            }
            return false;
        }
        /* OFFENSIVE BEHAVIOUR */
        //todo: improve
        if (opponentBoard.sumValues() < board.sumValues()) {
            if(board.sumValues() > 15)
                return true;
        }
        return false;
    }
}
