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