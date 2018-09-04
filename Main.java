import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;


public class Main {
    // "Globals" of this app, used by both main() and by the player class

    // TODO: Change deck to be an ArrayList of Card objects, instead of strings.
    public static ArrayList<Card> deck = initDeck();
    public static ArrayList<String> graveyard;
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Init players and cards (2 each)
        Player player = new Player(false);
        giveCards(player, 2);
        player.addCards();

        for (Card c : player.ownCards) {
            System.out.println(c.value + " of " + c.type + " ");
        }
        System.out.println();

        System.out.println("Decksize: " + deck.size());
        System.out.println("Player Total Score: " + player.currentTotal);

        // TODO: Add user interaction for triggering fold/quit/keep actions

        // TODO: Add dealer and show the user the first card of the dealer
        /* Player dealer = new Player(true);
        giveCards(dealer, 2); */
    }

    private static ArrayList<Card> initDeck(){
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            String cardType = "";
            switch (i){
                case 0:
                    cardType = "Spades";
                    break;
                case 1:
                    cardType = "Hearts";
                    break;
                case 2:
                    cardType = "Diamonds";
                    break;
                case 3:
                    cardType = "Clubs";
            }

            for (int j = 2; j < 15; j++) {
                String cardValue = "";
                if (j > 10) {
                    switch (j) {
                        case 11:
                            cardValue = "Jack";
                            break;
                        case 12:
                            cardValue = "Queen";
                            break;
                        case 13:
                            cardValue = "King";
                            break;
                        case 14:
                            cardValue = "Ace";
                            break;
                    }
                } else {
                    cardValue = Integer.toString(j);
                }
                deck.add(new Card(cardType, cardValue));
            }
        }

        Collections.shuffle(deck);
        return deck;
    }

    public static void giveCards(Player p, int amountCards){
        for (int i = 0; i < amountCards; i++){
            p.ownCards.add(deck.get(0));
            deck.remove(0);
        }

    }

}

class Player {
    public ArrayList<Card> ownCards = new ArrayList<Card>();
    public int currentTotal;

    private boolean isDealer;

    Player(boolean isDealer){
        this.isDealer = isDealer;
    }

    public void addCards(){
        int total = 0;
        int amountOfAces = 0;
        for (int i = 0; i < ownCards.size(); i++) {
            String value = ownCards.get(i).value;
            int intValue = 0;
            if (value.equals("Ace")) {
                amountOfAces++;
            } else if (value.equals("Jack") || value.equals("Queen") || value.equals("King")) {
                intValue = 10;
            } else {
                intValue = Integer.parseInt(value);
            }
            total += intValue;
        }

        // Choosing optimal value for Ace (1 or 11) depending on what the total becomes
        while(amountOfAces > 0){
            if (total > 11) {
                total += 1;
            } else {
                total += 11;
            }
            amountOfAces--;
        }

        this.currentTotal = total;
    }

    // Able to fold or choose another card
    // If player, quit the game entirely
    // If Dealer, needs to stop at 17 or continue below 17
    // TODO: Add methods for these actions

}

class Card {
    String type;
    String value;

    Card(String type, String value){
        this.type = type;
        this.value = value;
    }
}