import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

// TODO: Split into multiple files, Card.java, Dealer.java, Player.java and import

public class Main {
    // "Globals" of this app, used by both main() and by the player class
    public static ArrayList<Card> deck = initDeck();
    public static ArrayList<Card> graveyard = new ArrayList<Card>();
    public static Scanner sc = new Scanner(System.in);
    public static Dealer dealer = new Dealer();

    public static void main(String[] args) {
        // Init players and cards (2 each)
        Player player = new Player();
        shuffleDeck();

        // TODO: Make an initGame method, this should also check if player or dealer has instant blackjack
        giveCards(player, 2);
        giveCards(dealer, 2);
        player.calculateCardTotal();

        dealer.calculateCardTotal();

        // This starts the game and keeps things looping until the player decides to quit
        askUserForInput(player);
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
            restartAndReshuffle(p);
        }
    }

    public static void askUserForInput(Player p){
        System.out.println("Your cards are the following:");
        for (Card c : p.ownCards) {
            System.out.println(c.value + " of " + c.type + " ");
        }
        System.out.println("Your total score right now is: " + p.currentTotal);

        System.out.println("Dealer's cards are the following:");
        for (Card c : dealer.ownCards) {
            System.out.println(c.value + " of " + c.type + " ");
        }
        System.out.println("Dealer's total score right now is: " + dealer.currentTotal);

        System.out.println("There are " + deck.size() + " cards left in the deck");
        System.out.println("Enter \"g\" to get another card. Enter \"f\" to fold. Enter \"q\" to quit the program. Then press \"Enter\"");
        String userInput = sc.nextLine();

        switch (userInput) {
            case "g":
                p.getAnotherCard();
                break;
            case "f":
                p.fold();
                break;
            case "q":
                quit();
                break;
        }
    }

    public static void checkForWinner(Player p){
        dealer.dealerPlays();

        if (p.currentTotal > 21) {
            System.out.println("Awhh.. You lost because you went over 21 (" + p.currentTotal + "). Maybe try again!");
        } else if (p.currentTotal > dealer.currentTotal || dealer.currentTotal > 21) {
            System.out.println("YOU WIN!!! Congratulations!!");
        } else {
            System.out.println("Awhh, you lost, score not high enough: " + p.currentTotal);
        }

        System.out.println("You have three choices, play again \"p\", play again and shuffle deck \"s\" or quit the game \"q\"");
        String userInput = sc.nextLine();
        switch (userInput) {
            case "p":
                Main.restartGame(p);
                break;
            case "s":
                Main.restartAndReshuffle(p);
                break;
            case "q":
                quit();
                break;
        }
    }

    public static void restartGame(Player p){
        for (Card c : p.ownCards) {
            graveyard.add(c);
        }
        p.ownCards.clear();
        giveCards(p, 2);
        p.calculateCardTotal();

        for (Card c : dealer.ownCards) {
            graveyard.add(c);
        }
        dealer.ownCards.clear();
        giveCards(dealer, 2);
        dealer.calculateCardTotal();

        askUserForInput(p);
    }

    public static void restartAndReshuffle(Player p){
        // 1) Get rid of the cards and put them in deck
        for (Card c : p.ownCards) {
            deck.add(c);
        }
        p.ownCards.clear();

        for (Card c : dealer.ownCards) {
            deck.add(c);
        }
        dealer.ownCards.clear();

        for (Card c : graveyard) {
            deck.add(c);
        }
        graveyard.clear();
        shuffleDeck();
        restartGame(p);
    }

    public static void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public static void quit(){
        System.out.println("Game ended. Thanks for playing!");
        return;
    }
}

class Player {
    public ArrayList<Card> ownCards = new ArrayList<Card>();
    public int currentTotal;

    public void calculateCardTotal(){
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
            if (total >= 11) {
                total += 1;
            } else {
                total += 11;
            }
            amountOfAces--;
        }
        this.currentTotal = total;
    }

    public void fold(){
        Main.checkForWinner(this);
    }

    public void getAnotherCard(){
        Main.giveCards(this, 1);
        this.calculateCardTotal();
        if (this.currentTotal > 21) {
           Main.checkForWinner(this);
        }
        Main.askUserForInput(this);
    }
}

class Dealer extends Player{
    public void dealerPlays(){
        while(this.currentTotal < 17) {
            Main.giveCards(this, 1);
            this.calculateCardTotal();
            Card lastCard = this.ownCards.get(this.ownCards.size()-1);
            System.out.println("Dealer drew a new card: " + lastCard.value + " of " + lastCard.type);
            System.out.println("His total is now at: " + this.currentTotal);
        }
    }
}

class Card {
    String type;
    String value;

    Card(String type, String value){
        this.type = type;
        this.value = value;
    }
}