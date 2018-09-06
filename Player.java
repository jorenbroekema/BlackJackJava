import java.util.ArrayList;

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
        Main.checkForWinner();
    }

    public void getAnotherCard(){
        Main.giveCards(this, 1);
        this.calculateCardTotal();
        if (this.currentTotal > 21) {
            Main.checkForWinner();
        }
        Main.askUserForInput();
    }
}