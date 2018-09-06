import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

// TODO: Split into multiple files, Card.java, Dealer.java, Player.java and import

public class Main {
    // "Globals"
    public static ArrayList<Card> deck = initDeck();
    public static ArrayList<Card> graveyard = new ArrayList<Card>();
    public static Scanner sc = new Scanner(System.in);
    public static Dealer dealer = new Dealer();
    public static Player player = new Player();

    public static void main(String[] args) {
        shuffleDeck();
        initGame();

        // This starts the game and keeps things looping until the player decides to quit
        askUserForInput();
    }

    private static void initGame(){
        giveCards(player, 2);
        giveCards(dealer, 2);
        player.calculateCardTotal();
        dealer.calculateCardTotal();
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
        return deck;
    }

    public static void giveCards(Player p, int amountCards){
        // TODO: Change "20" to a configurable deck penetration percentage
        if (deck.size() > 20) {
            for (int i = 0; i < amountCards; i++){
                p.ownCards.add(deck.get(0));
                deck.remove(0);
            }
        } else {
            restartAndReshuffle();
        }
    }

    public static void askUserForInput(){
        System.out.println("Your cards are the following:");
        for (Card c : player.ownCards) {
            System.out.println(c.value + " of " + c.type + " ");
        }
        System.out.println("Your total score right now is: " + player.currentTotal);
        System.out.println("Dealer has a: " + dealer.ownCards.get(0).value + " of " + dealer.ownCards.get(0).type);
        System.out.println("There are " + deck.size() + " cards left in the deck");
        System.out.println("Enter \"g\" to get another card. Enter \"f\" to fold. Enter \"q\" to quit the program. Then press \"Enter\"");
        String userInput = sc.nextLine();

        switch (userInput) {
            case "g":
                player.getAnotherCard();
                break;
            case "f":
                player.fold();
                break;
            case "q":
                quit();
                break;
        }
    }

    public static void checkForWinner(){
        dealer.dealerPlays();

        if (player.currentTotal > 21) {
            System.out.println("Awhh.. You lost because you went over 21 (" + player.currentTotal + "). Maybe try again!");
        } else if (player.currentTotal > dealer.currentTotal || dealer.currentTotal > 21) {
            System.out.println("YOU WIN!!! Congratulations!!");
        } else {
            System.out.println("Awhh, you lost, score not high enough: " + player.currentTotal);
        }

        System.out.println("You have three choices, play again \"p\", play again and shuffle deck \"s\" or quit the game \"q\"");
        String userInput = sc.nextLine();
        switch (userInput) {
            case "p":
                Main.restartGame();
                break;
            case "s":
                Main.restartAndReshuffle();
                break;
            case "q":
                quit();
                break;
        }
    }

    public static void restartGame(){
        for (Card c : player.ownCards) {
            graveyard.add(c);
        }
        player.ownCards.clear();
        giveCards(player, 2);
        player.calculateCardTotal();

        for (Card c : dealer.ownCards) {
            graveyard.add(c);
        }
        dealer.ownCards.clear();
        giveCards(dealer, 2);
        dealer.calculateCardTotal();

        askUserForInput();
    }

    public static void restartAndReshuffle(){
        // 1) Get rid of the cards and put them in deck
        for (Card c : player.ownCards) {
            deck.add(c);
        }
        player.ownCards.clear();

        for (Card c : dealer.ownCards) {
            deck.add(c);
        }
        dealer.ownCards.clear();

        for (Card c : graveyard) {
            deck.add(c);
        }
        graveyard.clear();
        shuffleDeck();
        restartGame();
    }

    public static void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public static void quit(){
        System.out.println("Game ended. Thanks for playing!");
    }
}
