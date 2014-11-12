/*
 * Player.java
 *
 * Created on February 20, 2005, 8:13 AM
 */


import java.util.*;

/**
 * Superclass containing variables and methods in common between human and NPC players.
 */
public class Player {

    protected Card.Suit trumpSuit;
    protected ArrayList<Card> RedSeen = new ArrayList<Card>();
    protected ArrayList<Card> BlackSeen = new ArrayList<Card>();
    protected ArrayList<Card> BlueSeen = new ArrayList<Card>();
    protected ArrayList<Card> GreenSeen = new ArrayList<Card>();
    protected int numTrump = 0;
    protected boolean winningBiddingTeam = false;
    protected boolean rookPlayed = false;
    protected Card.Suit strongestSuit = Card.Suit.RED;
    protected Card.Suit secondLongestSuit = Card.Suit.BLUE;
    protected int redLength = 0;
    protected int blueLength = 0;
    protected int greenLength = 0;
    protected int blackLength = 0;
    protected boolean isRookPresent = false;
    protected boolean enemiesOutOfTrump[] = new boolean [2];
    protected Player myPartner = null;


    /**
     * Array of Card objects used as the internal representation of a players hand
     */
    protected Card[] hand = new Card[15];
    protected Card[] discards = new Card[5];
    /**
     * States true if the player is still bidding and false if the player has passed
     */
    protected boolean bidding = true;
    int bidAmount;
    /**
     * The name of the player that is displayed on the GUI
     */
    protected String name;

    /** Creates a new instance of Player */
    public Player() {
    }

    /**
     * Sets the name variable of Player to a new name.
     * @param newName A new name value for the specific player.
     */
    public void setName (String newName) {
        name = newName;
    }

    /**
     * Retrieves the current name variable of a player.
     * @return The current name variable for the specific Player object
     */
    public String getName () {
        return name;
    }

    /**
     * Fills the a players hand[] array with card objects.
     * @param newHand An array of card objects meant to be stored in hand[]
     */
    public void setHand(Card[] newHand) {
        for(int i = 0; i < 10; i++) {
            hand[i] = newHand[i];
        }
    }

    /**
     * Adds the kitty to a player's hand.  The player object must be the human player and must have won the kitty for this to be called.
     * @param newHand a 5 card array that contains the cards from the kitty which are to be added to the players hand.
     */
    public void addKittyToHand (Card[] newHand) {
        for(int i = 0; i < 5; i++) {
            hand[i + 10] = newHand[i];
        }
        
        sortHand(15);
    }

    /**
     * Retrieves the current hand[] array of a player
     * @return The entire hand[] array
     */
    public Card[] getHand () {
        return hand;
    }

    /**
     * Sets the boolean variable bidding belonging to a player, to either false for if they pass or true if they're still bidding.
     * @param newStatus The new boolean value for Player's bidding variable
     */
    public void setBidStatus (boolean newStatus) {
        bidding = newStatus;
    }

    /**
     * Retrieves the current bidding status of player
     * @return The boolean variable bidding from Player
     */
    public boolean getBidStatus () {
        return bidding;
    }

    /**
     * Retrieves a specific card from a player objects hand array using a index that is passed into the function.
     * @param handIndex index used in a player's hand array which specifies the location of the card object requested
     * @return The card object that is specified by the handIndex value.
     */
    public Card getSpecificCard (int handIndex) {
        return hand[handIndex];
    }

    /**
     * Condenses a player objects hand array after kitty cards have been discarded.  A player must have won the kitty for this to be called.
     * @param discarded An array the same size as the player's hand with boolean values for whether each card is to be discarded (true) or not (false).
     */
    public void reorganizeHand (boolean[] discarded) {
        int i, count = 0, discardCount = 0;
        Card[] tempHand = new Card[15];
        discards = new Card[5];
        for (i=0; i<15; i++)
        {
			if (!discarded[i]) {
				tempHand[count] = hand[i];
				count++;
			}
			//Populate another card array to hold discards
			else{
				discards[discardCount] = hand[i];
				discardCount++;
			}
		  }
        
        //copy all of the contents of tempHand (10) cards into the player's hand
        for(int k=0;k<10;k++){
        	hand[k] = tempHand[k];
        }
        //put 5 blank cards at the end of the player's hand so that it matches all the other hands
        for(int k=10;k<15;k++){
        	Card card = new Card();
        	card.setCard(Card.Suit.BLANK,100);
        	hand[k] = card;
        }

    }

    /**
     * This function Retrieves the index within a player objects hand array which indicates the location of the rook card.
     * @return an integer which is either the index of the rook card in the hand array or a -1 if not found.
     */
    public int findRook ()
    {
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null && hand[i].isRook())
            {
				   return i;
			   }
        }
        return -1;
    }

    /**
     * Sorts the cards in a player's hand by suit and numerical order.
     * @param HandSize The number of cards in Player's hand[].
       This can vary depending on if the Player is holding the kitty or not.
     */
    public void sortHand(int HandSize) {
        int handsize = HandSize;
        int min = 0;
        Card temp;

        for(int index = 0; index < handsize-1; index++) {
            min = index;
            for(int scan = index+1; scan < handsize; scan++) {
            	
                if(hand[scan].getValue() < hand[min].getValue()) {
                    min = scan;
                }
            }

            // swap the values
            temp = hand[min];
            hand[min] = hand[index];
            hand[index] = temp;
        }
    }

    /**
     * Store knowledge of what is trump for AI algorithm purposes.
     * @param color the Color of trump suit.
     */
    public void setTrump(Card.Suit color) {
        trumpSuit = color;
    }

    /**
     * Access knowledge of what is trump for AI algorithm purposes.
     * @return trumpSuit as a Suit enumeration
     */
    public Card.Suit getTrump() {
        return trumpSuit;
    }
    
    //Empty method: RPC and NPC override
    public Card.Suit chooseTrump() {
        System.out.println("This is in generic player class");
        return Card.Suit.BLANK;
    }
    
    public boolean[] chooseDiscards(){
    	
    	System.out.println("Called the PlayerClass chooseDiscards");
    	
    	boolean discarded[] = new boolean[15];
    	Arrays.fill(discarded,false);
    	
    	return discarded;
    	
    };

    /**
     * Store the data from a card played, received from GamePlayController,
     * data is used to determine what cards to play in each situation.
     * @param played the card which has been played and thus is out of play.
     */
     public void cardPlayed(Card played)
    {
        if(played.getSuit() == Card.Suit.RED)
            RedSeen.add(played);
        if(played.getSuit() == Card.Suit.BLACK)
            BlackSeen.add(played);
        if(played.getSuit() == Card.Suit.BLUE)
            BlueSeen.add(played);
        if(played.getSuit() == Card.Suit.GREEN)
            GreenSeen.add(played);
        if(played.getSuit() == getTrump())
        {
            numTrump++;
        }
        if(played.isRook())
            setRookPlayed(true);
    }
     
     /**
     * Remove the data from SuitSeen with regards to whether or not the card has been seen.
     * Simulates returning a card back into ply
     * @param played the card which has been unplayed and thus is put into play.
     */
     public void cardUnPlayed(Card played)
    {
        if(played.getSuit() == Card.Suit.RED)
            RedSeen.remove(played);
        if(played.getSuit() == Card.Suit.BLACK)
            BlackSeen.remove(played);
        if(played.getSuit() == Card.Suit.BLUE)
            BlueSeen.remove(played);
        if(played.getSuit() == Card.Suit.GREEN)
            GreenSeen.remove(played);
        if(played.getSuit() == getTrump())
        {
            numTrump--;
        }
        if(played.isRook())
            setRookPlayed(true);
    }
     /**
      * Reinitialize values of SuitsSeen for other methods at beginning of new hand.
      */
     public void resetSeen()
    {
         RedSeen = new ArrayList<Card>();
         BlackSeen = new ArrayList<Card>();
         BlueSeen = new ArrayList<Card>();
         GreenSeen = new ArrayList<Card>();
         numTrump = 0;
         setRookPlayed(false);
         enemiesOutOfTrump[0] = false;
         enemiesOutOfTrump[1] = false;
    }

     /**
      * Returns the summed ranks of all the cards dealt to the player
      * @return int handStrength - total of all ranks of cards in the players hand
      */
     public int determineHandStrength(){
        int handStrength = 0;
        for(int i = 0; i < 10; i++){
            handStrength += hand[i].getRank();
        }
        return handStrength;
     }

    public int longestSuit(){
        int longSuit = 0;
        int nextSuit = 0;

        for(Card.Suit suit : Card.Suit.values()){
            for(int j = 0; j < 10; j++){
                if(suit == hand[j].getSuit()){
                    nextSuit++;
                }
            }
            if(nextSuit > longSuit){
                longSuit = nextSuit;
            }
        }
        return longSuit;
    }


    /**
     * method to set player as a bid winning team member.
     * @param setter is the value winningBiddingTeam is set to.
     */
    public void setBidWinner(boolean setter)
    {
        winningBiddingTeam = setter;
    }

    /**
     * Method to
     * @param setter
     */
    public void setRookPlayed(boolean setter)
    {
        rookPlayed = setter;
    }

     /**
      * Determines the number of cards of each suit in a hand including the Rook
      */
     public void determineSuitLengths(){
        redLength = 0;
        blueLength = 0;
        greenLength = 0;
        blackLength = 0;
        isRookPresent = false;
        for(int i = 0; i < 15; i++){
            if(hand[i].getSuit() == Card.Suit.RED){
                 redLength++;
            }
            else if(hand[i].getSuit() == Card.Suit.BLUE){
                 blueLength++;
            }
            else if(hand[i].getSuit() == Card.Suit.GREEN){
                greenLength++;
            }
            else if(hand[i].getSuit() == Card.Suit.BLACK){
                blackLength++;
            }
            else{
                isRookPresent = true;
            }
        }
    }

    /**
     * Computes the strength of a given suit by summing the rank of each card in the suit
     * @param suit - Parameter of type suit used to determine which suit the strength is being computed for
     * @return int - Returns an integer representation of the sum of the ranks of the given suit
     */
    public int computeSuitStrength(Card.Suit suit){
        int suitStrength = 0;

        for(int i = 0; i < 15; i++){
             if(hand[i].getSuit() == suit){
                suitStrength = suitStrength + hand[i].getRank();
             }
        }

        return suitStrength;
    }

    public void determineStrongestSuit(){
        int maxSuitStrength = 0;
        /*maxSuitStrength = Math.max(computeSuitStrength(Suit.RED), computeSuitStrength(Suit.BLUE));
        maxSuitStrength = Math.max(maxSuitStrength, computeSuitStrength(Suit.GREEN));
        maxSuitStrength = Math.max(maxSuitStrength, computeSuitStrength(Suit.BLACK));*/
        if(redLength > blueLength &&
           redLength > greenLength &&
           redLength > blackLength)
        {
           strongestSuit = Card.Suit.RED;
        }
        else if(blueLength > redLength &&
                blueLength > greenLength &&
                blueLength > blackLength)
        {
            strongestSuit = Card.Suit.BLUE;
        }
        else if(greenLength > redLength &&
                greenLength > blueLength &&
                greenLength > blackLength)
        {
             strongestSuit = Card.Suit.GREEN;
        }
        else if(blackLength > redLength &&
                blackLength > blueLength &&
                blackLength > greenLength)
        {
             strongestSuit = Card.Suit.BLACK;
        }
        else
        {
            if(redLength == blueLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.RED),
                                           computeSuitStrength(Card.Suit.BLUE));
            }
            else if(redLength == greenLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.RED),
                                           computeSuitStrength(Card.Suit.GREEN));
            }
            else if(redLength == blackLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.RED),
                                           computeSuitStrength(Card.Suit.BLACK));
            }
            else if(blueLength == greenLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.BLUE),
                                           computeSuitStrength(Card.Suit.GREEN));
            }
            else if(blueLength == blackLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.BLUE),
                                           computeSuitStrength(Card.Suit.BLACK));
            }
            else if(greenLength == blackLength)
            {
                maxSuitStrength = Math.max(computeSuitStrength(Card.Suit.GREEN),
                                           computeSuitStrength(Card.Suit.BLACK));
            }
            if(maxSuitStrength == computeSuitStrength(Card.Suit.RED))
            {
                strongestSuit = Card.Suit.RED;
            }
            else if(maxSuitStrength == computeSuitStrength(Card.Suit.BLUE))
            {
                strongestSuit = Card.Suit.BLUE;
            }
            else if(maxSuitStrength == computeSuitStrength(Card.Suit.GREEN))
            {
                strongestSuit = Card.Suit.GREEN;
            }
            else if(maxSuitStrength == computeSuitStrength(Card.Suit.BLACK))
            {
                strongestSuit = Card.Suit.BLACK;
            }
        }
    }

    public  boolean determineCardHighestInSuit(int index)
    {
        boolean isHighest = true;

        for(int i = 0; i < 15; i++)
        {
            if(hand[i].getSuit() == hand[index].getSuit() &&
               hand[i].getRank() > hand[index].getRank())
            {
                isHighest = false;
            }
        }

        return isHighest;
    }

    public boolean determineNotLowestCard(int index)
    {
        boolean isLowest = true;

        for(int i = 0; i < 15; i++)
        {
            if(hand[i].getRank() < hand[index].getRank())
            {
                isLowest = false;
            }
        }
        return isLowest;
    }

    public int getSuitLength(Card.Suit suit)
    {
        if(suit == Card.Suit.RED){return redLength;}
        else if(suit == Card.Suit.BLUE){return blueLength;}
        else if(suit == Card.Suit.GREEN){return greenLength;}
        else if(suit == Card.Suit.BLACK){return blackLength;}
        return 0;
    }

    /**
     * to keep track of whether enemies out of trump.
     * @param partnerNumber
     */
    public void setEnemyOutOfTrump(int partnerNumber)
    {
        if(partnerNumber <4 && partnerNumber >=0 )
            enemiesOutOfTrump[partnerNumber/2] = true;
    }

    /**
     * for short circuiting, find out if enemy out of trump
     * @return true if enemies are out of trump, false otherwise
     */
    public boolean getEnemiesOutOfTrump()
    {
        for(int i = 0; i < 2; i++)
            if(enemiesOutOfTrump[i] == false)
                return false;
        return true;
    }
    
    /**
     * Calls lead on player that won the bid
     * @return index of the card in their hand they want to play
     */
   public int lead(){return 0;}
   
    /**
     * For playing a card once its been checked to be valid
     * @param current card trick
     * @return index of card in their hand to be played
     */
   public int playFollowLeadCard(Card[] trick) {return 0;}
    
    /**
     * For making sure the card the player wants to play is valid
     * @param an array representing the current card trick(whats been played)
     * @return an array of valid cards they can play
     */
    public Card[] getValidFollowCards(Card[] trick){
    	return new Card[15];
    	}
    
    public void bidOrPass(boolean bid){
    	
    }
    public int bid(int highBid){
    	return 0;
    }

    public void setHighBidder(int amount){}
   
    //Play method to check validity of and play card
    //remove card from players hand,
    //add what was played to current trick array
    //reorganize hand
    public Card[] Play(Card[] trick, int indexOfPlaceInTrick)
    {
      // check that a hand is being dealt
      System.out.println("Current players hand before they discard: ");
      for(int i=0;i<hand.length;i++)
      {
         if(hand[i]==null)
            System.out.print("null\n");
         if(hand[i].getCardVal()!=0)
         {
            System.out.print(i + ". " +hand[i].getCardVal() 
                        + " " + hand[i].getSuit()+"\n");
         }
      }

      //Index in Players hand of the card to play
      int indexToPlay=playFollowLeadCard(trick);
      
      //Set the corresponding spot in the trick to
      //the card at indexToPlay in current players hand
      Card cardToPlay = new Card();
      cardToPlay.setCard(hand[indexToPlay].getSuit(),
                                 hand[indexToPlay].getValue());
      cardToPlay.setCardValue(hand[indexToPlay].getCardVal());
      trick[indexOfPlaceInTrick]=cardToPlay;
      
      //"Discard" card at indexToPlay in hand
      hand[indexToPlay].setCard(Card.Suit.BLANK, 100);
      //After the card is played, setting the hidden value to 100 so it is sorted to the bottom. 
      hand[indexToPlay].setCardValue(0); //Showing the value is 0 so it isn't displayed.
      
      sortHand(hand.length);
      
      //Check that hand is sorted
      System.out.println("Sorted hand of player "+indexOfPlaceInTrick+
                              " places after the trickWinner(after they discard)");
      for(int i=0;i<hand.length;i++)
      {
         if(hand[i].getCardVal()!=0) //Stops the card from being displayed if the cardValue is equal to zero.
         {
            System.out.println(i +". "+ hand[i].getCardVal() 
                        + " " + hand[i].getSuit());
         }
      }
         
      //Check that card is added to trick
      System.out.println("Trick so far: ");
      for(int i=0;i<=indexOfPlaceInTrick;i++)
      {
         if(trick[i]!=null)
            System.out.println(trick[i].getCardVal() 
                        + " " + trick[i].getSuit()+"\n");
      }
      return trick;
    }

   

}
