import java.util.*;

public class RPC extends Player {
	
	boolean discarded[] = new boolean[15];

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
   
   
   @Override
   public boolean[] chooseDiscards(){
	   
	  Scanner scanDiscard = new Scanner(System.in);
	  
	  Arrays.fill(discarded,false);
	  
	  System.out.println("Here are your cards: "); 
	  determineSuitLengths();
      determineStrongestSuit();
      

      System.out.println("strongestSuit = " + strongestSuit); //debug
     
	  
	  for(int i=0;i<15;i++){
		  System.out.println(i+".      "+ hand[i].getCardVal() + "  "+hand[i].getSuit());
		  
	  }
	  
	  
	  for(int i=0;i<5;i++){
	System.out.println("select the index of the card you want to get rid of");
	  int discard;
	  discard = scanDiscard.nextInt();
	  
	  discarded[discard] = true;
	  }
	   
	   return discarded;
	   
   }
   
   /**
     * Allows bidWinner or trick Winner to lead card of their choice
     * @return The position in players hand of the card chosen.
     */
   public int lead()
   {
      int index;
      //Allow the bid or Trick winner to lead any card
      //That is in their hand(in one of the indexes 0-9)
      do
      {
         System.out.println("Please enter index of card to play");
         index = scan.nextInt();
      }while(index<0||index>=10);
      
      return index;
   }
   
   
   /**
     * Allow player to choose a card to play, only if valid
     * @param trick The cards that have been played already in the current trick.
     * @return The position in our hand of the card chosen.
     */
      public int playFollowLeadCard(Card[] trick) {
        // track acceptable responses and figure out play.
        Card[] validCards = new Card[15];

        // compile a list of playable cards, have rules based on this list
        // getValidFollowCards doesnt need to be overridden, can be used as is
        validCards = getValidFollowCards(trick);
        
        if(validCards != null)
            System.out.println("ValidCards is not null");
        else
            System.out.println("ValidCards is null");
        //See whats in the valid card array
        System.out.println("Valid follow card array:\n");
        System.out.println(validCards.length+"\n");

        for(int i=0;i<15;i++)
        {
            if(validCards[i]==null)
               System.out.print("null\n");
            else
               System.out.println(validCards[i].getCardVal() 
                  + " " + validCards[i].getSuit()+"\n");
        }
        
        //Check to see if the card they want to play is in validCards array
        boolean isValid=false;
        int cardToPlay;
        do
        {
         System.out.println("Please enter the index of the card you want to play");
         cardToPlay = scan.nextInt();
         for(int i=0; i<15;i++)
         {
            if(validCards[i] != null)
            {
               if(validCards[i].getValue()==hand[cardToPlay].getValue())
                  isValid = true;
            }
         }
        }while(!isValid);
        
        return cardToPlay;
     }


   }
