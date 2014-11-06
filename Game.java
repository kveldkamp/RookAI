import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
 * Game.java
 *
 * 10/17/14
 * Game Class includes functions for gameplay
 */
public class Game {
	Player [] players = new Player[4];
	int bidWinner = 0; 
	int trickWinner = 0;
	Card [] currentTrick = new Card[4];
	Card.Suit trumpColor = null; 
	protected ArrayList<Card> deck = new ArrayList<Card>();
	protected Card kitty[] = new Card[5];
	protected int highBid = 0;
	protected boolean [] playerActive = new boolean[4];
	protected int[] currentTeamScores = new int[2];
	
	public Game(){
	      players[0] = new NPC();
	      players[1] = new NPC();
	      players[2] = new NPC();
	      players[3] = new RPC();
	      players[0].myPartner = players[2];
	      players[1].myPartner = players[3];
	      players[2].myPartner = players[0];
	      players[3].myPartner = players[1];
	}
	
	/*
	* This is the bidding process. 
	* bidWon is set to true if only one bidder is left
	* 
	*/

	public void Bidding(){
			//Resetting value of highest bid.
		   highBid = 0;
			
			//Resetting all values in playerActive to false.
			for(int i=0; i<4;i++){
				playerActive[i] = true;
			}
			
		   boolean bidWon = false;
		   //Print the player's hand
		   System.out.println("This is your hand");
		   for(int k=0;k<15;k++){
				if(players[3].hand[k].getCardVal()!=0){
				
			   System.out.println(k+".      "+ players[3].hand[k].getCardVal() + "  "+players[3].hand[k].getSuit());
				}
			}
		
		   
		   bidLoop:
		   while(bidWon == false){
		 	  int bidders = 4;
		 	  	for(int i = 0; i < players.length; i++){
		 	  		
		 	  		
		 	  		
		 		  if(highBid <= 195 && bidders > 1){
		 			 players[i].bidOrPass(players[i].bidding);
		 			  
		 			
		 			  
		     		  if(players[i].bidding == true){
		     				  highBid = players[i].bid(highBid);
		     				  bidWinner = i;
		     				  System.out.println("Current High is " + highBid);
		     		  }
		     		  else{
					  	  //Removing player from bidding.
						  playerActive[i] = false;
		     			  bidders--;
		     			  
		     		  }
		 		  }
     			  
		 		 
		 		  if(highBid > 195 || bidders == 1){
		       		  bidWon = true;
			     			  if(highBid == 0){ //All NPCs Passed
			     				  highBid = players[3].bid(highBid);
			     				  bidWinner = 3;
			     			  }
			     			  
			     			  System.out.println("winning bidder is player " + bidWinner);
				       		  System.out.println("Highest bid: " + highBid);
				       		  players[bidWinner].setHighBidder(highBid);
				       		  players[bidWinner].winningBiddingTeam = true;
				       		  players[bidWinner].myPartner.winningBiddingTeam = true;
				       		  //Need to set their teammate as winning as well		  
				       		  break bidLoop;
		       	  }   
		     }
		  }
		  System.out.println("Done with bidding");
        trickWinner=bidWinner; 
	}
		
   /*
	* playGame is the game play for 10 tricks 
	* play starts with the bidWinner leading, and cycles through
	*/
	public void playGame(){
   
      //Allow 10 rounds of the game to be played
      for(int i=1;i<11;i++)
      {
         System.out.println("\nPLAYING ROUND: "+i);
         if(i==1)
            System.out.println("Bid Winner is: " + bidWinner);
         else
            System.out.println("Trick Winner is: " + trickWinner);
         
//		 System.out.println("These are the discards");
//		   for(int k=0;k<5;k++){
//				if(players[bidWinner].discards[k].getCardVal()!=0){
//				
//			   System.out.println(k+".      "+ players[bidWinner].discards[k].getCardVal() + "  "+players[bidWinner].discards[k].getSuit());
//				}
//			}
         
         playRound();
         
         // Find index of card that won the trick, represented by MAX
         // Should either be highest trump if there is a trump on the table
         // Or the highest card of the suit that was led
         
         int MAX =0;
         boolean trumpFound = false;
         
         // Find highest outright card value that follows suit
         for(int j=1;j<4;j++)
         {
            if(currentTrick[j].getCardVal()>currentTrick[MAX].getCardVal()
                     && currentTrick[0].getSuit()== currentTrick[j].getSuit())
            MAX=j;
         }
         
         // If trump wasn't led, check to see if anyone trumped in
         // and won the suit
         if(currentTrick[0].getSuit() != trumpColor)
         {
            for(int k=0;k<4;k++)
            {
               if(currentTrick[k].getSuit() == trumpColor && !trumpFound)
               {
                  MAX=k;
                  trumpFound = true;
               }
               else if(currentTrick[k].getSuit() == trumpColor &&
                   currentTrick[k].getCardVal()>currentTrick[MAX].getCardVal())
               {
                  MAX=k;
               }
            }
         }
         
         // The trickWinner is set to the player that won the bid
         System.out.println("WINNER WAS PLAYER IN TRICK AT: "+MAX);
         trickWinner = (trickWinner+MAX)%4;
         System.out.println("trickWinner was player: "+ trickWinner);
         
      } //for loop end
      
      addDiscardToScore();
      displayScore();
      
   } //playGame end
   
   
   /*
	* playRound is the game play for a single round 
	* play starts with the bidWinner leading, and cycles through
	*/
	public void playRound(){
     
	  //Call lead on whoever won the bid
     System.out.println("The player that leads is "+trickWinner);
     
     for(int i=0;i<15;i++)
     {
      if(players[trickWinner].hand[i].getCardVal()!=0)
         System.out.println(i+". "+players[trickWinner].hand[i].getCardVal() + " "
               + players[trickWinner].hand[i].getSuit());
     }
     
     //Play the card that the leader of the trick wants to play, at index 0
     int indexToPlay = players[trickWinner].lead();
     
     //Get the different values of the card that is to be played
     Card.Suit setSuit = players[trickWinner].hand[indexToPlay].getSuit();
     int setVal = players[trickWinner].hand[indexToPlay].getCardVal();
     int setHiddenValue = players[trickWinner].hand[indexToPlay].getValue();
     
     //Set the first spot in the trick to the card that was led
     currentTrick[0] = new Card();
     currentTrick[0].setCard(setSuit,setHiddenValue);
     currentTrick[0].setCardValue(setVal);
     
     //Discard the card from the players hand(replace with empty card)
     players[trickWinner].hand[indexToPlay].setCard(Card.Suit.NOSUIT, 100);
     players[trickWinner].hand[indexToPlay].setCardValue(0);
     
     //Sort whoever won's hand
     players[trickWinner].sortHand(players[trickWinner].hand.length);

	  //Print out trick once the lead player has played
     System.out.println("Trick so far: ");
     System.out.println(currentTrick[0].getCardVal() 
                        + " " + currentTrick[0].getSuit()+"\n");
     
     //Cycle through players and have them Play
	  int counter=1;
     int placeInTrick=1;
     
	  //Start i at whoever won bid, cycle through rest of players
	  for(int i=(trickWinner+1)%4;counter<4;i=(i+1)%4)
	  {
        System.out.println("Place in trick: "+(placeInTrick+1));
        currentTrick=players[i].Play(currentTrick,placeInTrick);
        //Add the card that was played to AI's intelligence
	     players[0].cardPlayed(currentTrick[placeInTrick]);
	     players[1].cardPlayed(currentTrick[placeInTrick]);
	     players[2].cardPlayed(currentTrick[placeInTrick]);
	     counter++;
        placeInTrick++;
	  }
  
	}
	
/** 
 * makeDeck method iterates through all suits (skipping NOSUIT) and creates 1-14 in each suit
 * deck is filled and shuffled after completion of this method
 */
	public void makeDeck(){
				
		for(int i=0;i<44;i++){
					
			Card card = new Card();
			
			
			
			if(i <= 10){
				card.setCard(Card.Suit.RED,i);
			}
					
			else if(i <= 21){
			card.setCard(Card.Suit.BLUE,i);
			}
					
			else if(i <= 32){
				card.setCard(Card.Suit.GREEN,i);
			}
					
			else if(i <= 43){
				card.setCard(Card.Suit.BLACK,i);
				}
			
			card.setCardVal();
			deck.add(card);
		}
		
		//specific code for rook card
		Card card = new Card();
		card.setCard(Card.Suit.NOSUIT,44);
		card.setCardVal();
		deck.add(card);
		
		//"shuffling" the deck
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
		  
		  
	  }	  
	
	
	
public void dealCards(){
	
	int handIndex = 0;
	
	//deal out the 40 cards to the players
	for(int i=0;i<40;){
		
		players[0].hand[handIndex]=deck.get(i);
		i++;
		
		players[1].hand[handIndex]=deck.get(i);
		i++;
	
		players[2].hand[handIndex] = deck.get(i);
		i++;
	
		players[3].hand[handIndex]= deck.get(i);
		i++;	
		handIndex++;
		}
	
	//set blank card objects to the last 5 cards in hand
	for(int i=0;i<5;i++){
		
		Card nullCardPlaceholder=new Card();
		nullCardPlaceholder.setCard(Card.Suit.BLANK,100); //Using 100 as the value so it is sorted to the bottom of the hand later.
		
      /*Card card2=new Card();
		card2.setCard(Card.Suit.NOSUIT,0);
		Card card3=new Card();
		card3.setCard(Card.Suit.NOSUIT,0);
		Card card4=new Card();
		card4.setCard(Card.Suit.NOSUIT,0);*/
		
		players[0].hand[handIndex]=nullCardPlaceholder;
		players[1].hand[handIndex]=nullCardPlaceholder;
		players[2].hand[handIndex]=nullCardPlaceholder;
		players[3].hand[handIndex]=nullCardPlaceholder;
		handIndex++;
	}
	
	//add the last 5 cards to the kitty
	for(int i=40;i<45;i++){
		int temp = i % 5;
		kitty[temp] = deck.get(i);
		

	}
	
   for(int i = 0; i < 4; i++){
      players[i].sortHand(10);
   }
	
}

/**
 * addKittyToHand selects the winner within the player array and replaces their 5 null card values with the 5 cards from the 
 * kitty. After that chooseDiscards() is called for the players hand, which returns a boolean array of cards to get rid of.
 * That boolean array is used as the argument for reorganizeHand(), which is called for computer players to determine
 * which cards are strongest/ and which to get rid of. For RPC, you can simply choose which cards to get rid of.
 */

public void sendKitty(){

	
			players[bidWinner].addKittyToHand(kitty);
			
			//Print their cards if the real player won
			
			System.out.println("This is your hand with the Kitty");
			for(int k=0;k<15;k++){
				if(players[bidWinner].hand[k].getCardVal()!=0){
				
			   System.out.println(k+".      "+ players[bidWinner].hand[k].getCardVal() + "  "+players[bidWinner].hand[k].getSuit());
				}
			   }
			
		
		//Player.chooseTrump depends on determineStrongestSuit being called beforehand. 
		//This resolves the issue of the player not being able to see the kitty before making the trump decision.
		
     		players[bidWinner].determineSuitLengths();
     		players[bidWinner].determineStrongestSuit();
     		trumpColor = players[bidWinner].chooseTrump();
     		System.out.println("Trump card: " + trumpColor);
     		for(int a = 0; a < players.length; a++){
     			  players[a].setTrump(trumpColor);
     		}		
     		
     		//debug//
     		System.out.println("red length"+players[bidWinner].redLength);
     		System.out.println("green length"+players[bidWinner].greenLength);
     		System.out.println("blue length"+players[bidWinner].blueLength);
     		System.out.println("black length"+players[bidWinner].blackLength);
     		//
     		
     		
			players[bidWinner].reorganizeHand((players[bidWinner].chooseDiscards()));
			
	//ERROR OCCURS HERE
			
//used for debugging to make sure actually got rid of cards
		System.out.println("player"+" "+bidWinner+"'s cards after reorganizing: ");
		
for(int k=0;k<15;k++){
		if(players[bidWinner].hand[k].getCardVal()!=0){
		
	   System.out.println(k+".      "+ players[bidWinner].hand[k].getCardVal() + "  "+players[bidWinner].hand[k].getSuit());
		}
	}
}

/** 
 * addDiscardToScore() adds the values of the originally discarded cards to the score 
 * of the team that won the last trick
 */
public void addDiscardToScore(){
	/*How are current team scores decided? This assumes that players 0 and 2 are currentTeamScores[0]
	 * and players 1 and 3 are currentTeamScores[1]
	 */
	if(trickWinner == 0 || trickWinner == 2){
		currentTeamScores[0] += discardScore();
	}
	if(trickWinner == 1 || trickWinner == 3){
		currentTeamScores[1] += discardScore();
	}
}
/** 
 * discardScore() grabs the discards array from the original bidWinner and adds the scores
 * of the cards up and returns the total score value of the discards
 */
public int discardScore(){
	int discardScore = 0;
	//Need to get card values from discarded array in the Player
	//Winner of last trick is not necessarily the one who discarded
	for(int i = 0; i < players[bidWinner].discards.length; i++){
		discardScore += players[bidWinner].discards[i].getScore();
	}
	return discardScore;
}

/** 
 * displayScore() shows the final scores and the resultant winner
 */
public void displayScore(){
	if(currentTeamScores[0] > currentTeamScores[1]){
		System.out.println("The winning team is team one, Players 0 and 2, with a total of : " + currentTeamScores[0]);
		System.out.println("Second place goes to team two, Players 1 and 3 (you), with a total of : " + currentTeamScores[1]);
	}
	if(currentTeamScores[0] < currentTeamScores[1]){
		System.out.println("The winning team is team two, Players 1 and 3 (you), with a total of : " + currentTeamScores[1]);
		System.out.println("Second place goes to team one, Players 0 and 2, with a total of : " + currentTeamScores[0]);
	}
}

//***
//ACCESSORS AND MUTATORS NECESSARY FOR GUI FUNCTIONALITY.
//***

//Precondition: Bidding has begun and highBid has been given a value.
//Postcondition: Returns the value of highBid when called.
public int getHighestBidValue()
{
	return highBid;
}
	
//Precondition: Gets the current state of bidding. Bidding must have occurred.
//Postcondition: Returns an array of players still active in bidding.
public boolean[] getCurrentBidState()
{ 
	return playerActive;
}

//Precondition: Player's hand has been instantiated.
//Postcondition: Returns the cards that are within the playerHand array.
public Card[] getPlayerHand()
{
	return players[3].hand;
}

//Precondition: currentTeamScores has been instantiated.
//Postcondition: currentTeamScores is returned as an array of two values.
public int[] getCurrentTeamScores()
{
	return currentTeamScores;
}

//Precondition: getCurrentTrick has been instantiated.
//Postcondition: currentTrick is returned as an array of cards. This will include any cards that have been played so far.
public Card[] getCurrentTrick()
{
	return currentTrick;
}

//Precondition: kitty has been instantiated.
//Postcondition: kitty is returned as an array of cards.
public Card[] getKitty()
{
	return kitty;
}

//Precondition: newKitty is a card array of five cards.
//Postcondition: kitty will be set to point to the values located in newKitty.
public void setKitty(Card[] newKitty)
{
	kitty = newKitty;
}

}//end of class


