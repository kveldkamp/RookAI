import java.util.*;

public class RPC extends Player {

   Scanner scan = new Scanner(System.in);
   
   
   /**
    * Choose a suit to be the trump (called if we win the bidding).
    * @return The chosen suit.
    */

   
   /**
    * Choose an amount to bid.
    * @param highBid: The current high bid.
    * @return The amount to bid.
    */
   public int bid(int highBid) {
	   System.out.println("Enter your bid, your bid needs to be between 100-200 and higher than the current high in multiples of 5: " + highBid);
	   int bid = 0;
	   
	   boolean validinput = false;
	   //loop until they give a valid input within the range & multiple of 5 & greater than highbid
	    while (!validinput) {
	        try {
	        	Scanner scanbid = new Scanner(System.in);
	            bid = scanbid.nextInt();
	            if (bid < 100 || bid > 200 || bid%5 != 0 || bid <= highBid) {
	                System.out.println("Not in range/multiple of 5/higher than highbid, try again:");
	            }
	            else {
	               validinput = true;
	            }
	        }
	        //Throws exception if a number isn't inputted
	        catch (InputMismatchException I) {
	            System.out.println("Not a number, try again:");
	        }
	    }   
	    return bid;
	   
   }
   
   /**
    * Give player option to bid or pass
    * @param bid
    * @return none
    */
   public void bidOrPass(boolean bid) {
	   //This does not throw exceptions for out of range (0-1) or InputMismatch: shouldn't matter with a GUI
	   if(bid == true){
		   System.out.println("Input 1 if you'd like to bid, 0 if you'd like to pass");
       int num = scan.nextInt(2);
       if (num == 0) {
           bidding = false;
       	}
	   }

   }

   /*
    * (non-Javadoc)
    * @see NPC#setHighBidder(int)
    */
   public void setHighBidder(int amount) {
       //setBidWinner();
       bidAmount = amount;
       System.out.println("bidamount is" + bidAmount);
   }
   
   
   /**
    * Give player option of choosing trump
    * @param none
    * @return the Trump Card Suit
    */
   public Card.Suit chooseTrump() {
      System.out.println("Please pick the trump color: RED, BLUE, GREEN, BLACK");
      boolean trumpvalid = false;
      Card.Suit trumpcolor = null;
      while(!trumpvalid){
	        try {
	        	Scanner scantrump = new Scanner(System.in);
	        	trumpcolor = Card.Suit.valueOf(scantrump.nextLine());
	        	trumpvalid = true;
      
	        }
	        catch(IllegalArgumentException I) {
	            System.out.println("Not a suit color, try again:");
	        }
      }
      return trumpcolor;
   }
   
   // Returns the index of the card to be led in the hand
   // called only by winner of the bid(1st round)
   // or of last trick (remaining rounds) by human player
   public int lead(){
      System.out.println("Please enter index of card to play");
      int index = scan.nextInt();
      return index;
   }
   
   
   /**
     * Allow player to choose a card to play, only if valid
     * @param trick The cards that have been played already in the current trick.
     * @return The position in our hand of the card chosen.
     */
      public int playFollowLeadCard(Card trick[]) {
        // track acceptable responses and figure out play.
        Card[] validCards = new Card[15];
        int maxIndex = -1;

        // compile a list of playable cards, have rules based on this list
        //getValidFollowCards doesnt need to be overridden, can be used as is
        validCards = getValidFollowCards(trick);
        //Same for getValidFollowMaxIndex
        maxIndex = getValidFollowMaxIndex(trick);
        
        boolean isValid=false;
        int cardToPlay;
        do
        {
         System.out.println("Please enter the index of the card you want to play");
         cardToPlay = scan.nextInt();
         for(int i=0; i<15;i++){
            if(validCards[i].getValue()==hand[cardToPlay].getValue())
               isValid = true;
         }
        }while(!isValid);
        
        return cardToPlay;
     }


   }
