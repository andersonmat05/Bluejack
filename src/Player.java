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
        if (!(isCpu() || opponent.isCpu())) {
            SystemHelper.clear();
        }
        if (board.sumValues() > 20) {
            SystemHelper.println("  " + name + " busted!", SystemHelper.ANSI_WHITE_BOLD);
            opponent.winSet();
            return true;
        }
        if (checkBluejack(board)) {
            SystemHelper.println("  Bluejack!", SystemHelper.ANSI_BLUE_BOLD);
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

    /**
     * Prompts user to select action. Returns true if user chose to stand.
     */
    public boolean inputAction() {
        /* Put the user in a loop until action completes */
        while (true) {

            /* Prompt the player to choose an action. If they don't have
             * any cards in their hand, do not show the play card option */
            int action;
            if (hand.getLastIndex() == -1) {
                System.out.println("Choose action\n1: End  2: Stand");
                action = SystemHelper.scanIntRange(1, 2);
            } else {
                System.out.println("Choose action\n1: End  2: Stand  3: Play Card");
                action = SystemHelper.scanIntRange(1, 3);
            }

            if (action == 1) {
                /* If player chooses to end the turn, we simply return false
                 * so game knows player is not standing yet. */
                return false;
            } else if (action == 2) {
                /* Return true so game loop knows player is standing. */
                return true;
            } else if (action == 3) {
                /* Prompt user to choose a card index */
                System.out.println("Choose card index (0 to cancel)");
                int cardIndex = SystemHelper.scanIntRange(0, hand.getLastIndex()+1);
                if(cardIndex == 0) {
                    /* Play card canceled, return back to action selection */
                    continue;
                }
                /* User chose a valid card to play, play the card and end the turn. */
                playCard(cardIndex-1);
                return false;
            }
        }
    }

    /**
     * Returns the new sum if this card is played.
     */
    private int getPlayResult(Card card) {
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
    public boolean logicAction(Deck opponentBoard) {
        /* Stand if winning is guaranteed */
        if (board.sumValues() == 20)
            return true;

        /* DEFENSIVE BEHAVIOUR */
        if (board.sumValues() > 20) {
            // There is nothing we can do...
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
                    int currentSpecialResult = getPlayResult(card);
                    if (currentSpecialResult <= 20) {
                        // Check if it is better than last card
                        if (getPlayResult(hand.get(cardIndex)) < currentSpecialResult || !playCard) {
                            cardIndex = i;
                            playCard = true;
                        }
                    }
                } else {
                    /* Not special card */
                    if (getPlayResult(card) <= 20 &&
                            card.value < hand.get(cardIndex).value) {
                        cardIndex = i;
                        playCard = true;
                    }
                }
            }
            /* Found a card to play */
            if(playCard) {
                playCard(cardIndex);
            }
            return false;
        }
        /* OFFENSIVE BEHAVIOUR */
        if (opponentBoard.sumValues() < board.sumValues()) {
            /* Check if there is a risk of getting busted */
            if(board.sumValues() > 14) {
                /* Check cards at hand */
                if (hand.getLastIndex() != -1) {
                    /* Check if there is a flip card in hand */
                    boolean haveFlip = false;
                    for (int i = 0; i <= hand.getLastIndex(); i++) {
                        if (hand.get(i).isFlip()) {
                            haveFlip = true;
                        }
                    }
                    if (haveFlip) {
                        /* New card can be flipped, risk is low */
                        return false;
                    }

                    // Check regular cards
                    int minCard = hand.minIndex();
                    if (minCard != -1) {
                        /* There is a strong negative, risk is low */
                        return hand.get(minCard).value >= -3;
                    }
                }
                /* Risk is too high */
                return true;
            }
        }
        if (board.sumValues() < 10) {
            /* Check if there is a double card in hand */
            int doubleIndex = -1;
            for (int i = 0; i <= hand.getLastIndex(); i++) {
                if (hand.get(i).isDouble()) {
                    doubleIndex = i;
                }
            }
            if (doubleIndex != -1) {
                /* Check the risk */
                if (getPlayResult(hand.get(doubleIndex)) < 15) {
                    /* Safe to play double */
                    playCard(doubleIndex);
                    return false;
                }
            }
        }
        /* Too early to make a meaningful decision */
        return false;
    }
}
