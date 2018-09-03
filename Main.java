import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;


public class Main {

    // "Globals" of this app, used by both main() and by the player class

    // TODO: Change deck to be an ArrayList of Card objects, instead of strings.
    public static ArrayList<String> deck = initDeck();
    public static ArrayList<String> graveyard;
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Init players and cards (2 each)
        Player player = new Player(false);
        giveCards(player, 2);
        player.addCards();

        System.out.println(player.ownCards);
        System.out.println(deck.size());
        System.out.println(player.currentTotal);

        // TODO: Add user interaction for triggering fold/quit/keep actions

        // TODO: Add dealer and show the user the first card of the dealer
        /* Player dealer = new Player(true);
        giveCards(dealer, 2); */
    }

    private static ArrayList<String> initDeck(){
        ArrayList<String> deck = new ArrayList<String>();
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
                if (j > 10) {
                    switch (j) {
                        case 11:
                            deck.add(cardType + "-Jack");
                            break;
                        case 12:
                            deck.add(cardType + "-Queen");
                            break;
                        case 13:
                            deck.add(cardType + "-King");
                            break;
                        case 14:
                            deck.add(cardType + "-Ace");
                            break;
                    }
                } else {
                    deck.add(cardType + "-" + Integer.toString(j));
                }
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

    public ArrayList<String> ownCards = new ArrayList<String>();
    public int currentTotal;

    private boolean isDealer;

    Player(boolean isDealer){
        this.isDealer = isDealer;
    }

    public void addCards(){
        int total = 0;
        int amountOfAces = 0;
        for (int i = 0; i < ownCards.size(); i++){
            String stringValue = ownCards.get(i).split("-")[1];
            int value = 0;
            if (stringValue.equals("Ace")) {
                amountOfAces++;
            } else if (stringValue.equals("Jack") || stringValue.equals("Queen") || stringValue.equals("King")) {
                value = 10;
            } else {
                value = Integer.parseInt(stringValue);
            }
            total += value;
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