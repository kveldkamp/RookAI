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
   }
   
   
   /**
    * Give player option of choosing trump
    * @param none
    * @return the Trump Card Suit
    */
   public Card.Suit chooseTrump() {
      
      boolean trumpvalid = false;
      Card.Suit trumpcolor = null;
      while(!trumpvalid){
    	  System.out.println("Please pick the trump color: RED, BLUE, GREEN, BLACK");
	        try {
	        	Scanner scantrump = new Scanner(System.in);
	        	
	        	trumpcolor = Card.Suit.valueOf(scantrump.nextLine());
	        	if(trumpcolor == Card.Suit.NOSUIT){
	        		System.out.println("You cannot enter NOSUIT, try again");
	        		trumpvalid = false;
	        	}
	        	else{
	        		trumpvalid = true;
	        	}
      
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
    * Code to gather cards that follow suit in an array
    * @param trick
    * @return validFollowCards[] which is the valid card play options.
    */
    public Card[] getValidFollowCards(Card[] trick)
    {
        Card card = null;
        Card[] validFollowCards = new Card[15];
        int maxIndex = -1;

        //code to get valid cards while following suit
        for(int i = 0; i < 15; i++)
        {
          if(trick[0]!=null)
          {
            if(hand[i] != null && hand[i].getSuit() == trick[0].getSuit())
            {
			      card = hand[i];
               maxIndex++;
               validFollowCards[maxIndex] = hand[i];
            }
          }
		  }

        if (card == null)
        {	// we don't have a card of the same suit
			   // compile list of cards in hand, have rules based on list
			for (int i = 0; i<15; i++)
         {
				if (hand[i] != null)
            {
                 maxIndex++;
                 validFollowCards[maxIndex] = hand[i];
                 card = hand[i];
				}
			}
        }
        
        if(maxIndex == -1)
            System.out.println("ERROR.  No Card to be played");
        return validFollowCards;
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
