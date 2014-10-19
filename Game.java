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
	int winner = 0;  
	Card [] currentTrick = new Card[4];
	Card.Suit trumpcolor; 
	protected ArrayList<Card> deck = new ArrayList<Card>();
	
	
	public Game(){
	      players[0] = new NPC();
	      players[1] = new NPC();
	      players[2] = new NPC();
	      players[3] = new RPC();
	}
	
	/*
	* This is the bidding process. 
	* bidwon is set to true if only one bidder is left
	*/

	public void Bidding(){
		   //Issue 1: You have to bid again if every other player passes
		   int highBid = 0;
		   boolean bidwon = false;
		   bidwonloop:
		   while(bidwon == false){
		 	  int bidders = 4;
		 	  for(int i = 0; i < players.length; i++){
		 		  	  //This is called even if all the others passed: a bit should be set by default
		 		  if(highBid <= 195 && bidders > 1){
		 			  players[i].bidOrPass(players[i].bidding);
		 			  //sets high bid or decrements bidders based on their bidding value
		     		  if(players[i].bidding == true){
		     				  highBid = players[i].bid(highBid);
		     				  winner = i; //Holds winner of bid
		     				  //System.out.println("Bid Made");
		     				  //System.out.println("highBid: " + highBid);
		     			  
		     		  }
		     		  else{
		     			  //System.out.println("This player passed...");
		     			  bidders--;
		     		  }
		 		  }
     			  
		 		  if(highBid > 195 || bidders == 1){
		     		//Only one bidder left; set winning bidder
		       	  	//if(bidders == 1){
		       		  bidwon = true;
		       		  //Set the high bidder with the highBid; winner holds the index of winner
		       		  for(int b = 0; b < players.length; b++){
			     		  if(players[b].bidding == true){
			     			  //If everyone passed but one, set highBid to default to 100
			     			  if(highBid == 0){
			     				  highBid = 100;
			     			  }
			     			  System.out.println("last bidder won: player " + b);
				       		  System.out.println("Are they bidding?: " + players[b].bidding);
				       		  System.out.println("High bid is this at end: " + highBid);
				       		  players[b].setHighBidder(highBid);
				       		  players[b].winningBiddingTeam = true;
				       		  //Set trump color
				       		  trumpcolor = players[b].chooseTrump();
				       		  System.out.println("Trump card: " + trumpcolor);
				       		  //Set trump card for all players
				       		  for(int a = 0; a < players.length; a++){
				       			  players[a].setTrump(trumpcolor);
				       			  //System.out.println("Player " + a + "knows that trump is " + players[a].getTrump());
				       		  }
				       		  
				       		  
				       		  //Need to set their teammate as winning as well
				       		  //winner = b; //This holds the index of the winning player
				       		  break bidwonloop;
			     		  }
		       		  }
		       	  }   
		     }
		  }
		  System.out.println("Done with bidding"); 
	}
		
	

	public void PlayRound(){
	players[0].lead();
	  
	  //Call lead on whoever won the bid
	  
	  //Cycle through players and have them Play
	  int counter=0;
	  //Start i at whoever won bid
	  for(int i=0;counter<4;i=(i+1)%4)
	  {
	     currentTrick=players[i].Play(currentTrick,i);
	     players[0].cardPlayed(currentTrick[i]);
	     players[1].cardPlayed(currentTrick[i]);
	     players[2].cardPlayed(currentTrick[i]);
	     counter++;
	  }
	}
	
/** 
 * makeDeck method iterates through all suits (skipping NOSUIT) and creates 1-14 in each suit
 * deck is filled and shuffled after completion of this method
 */
	public void makeDeck(){
		
		
		//reminder, add in some code to add the value from 0-44 but also keep the card number
		  for (Card.Suit suit : Card.Suit.values()){
			if(suit!=Card.Suit.NOSUIT){
				
				for(int i=1;i<=14;i++){
					if(i==1){
				  	Card card = new Card();
				  	card.setCard(suit,i);
				  	deck.add(card);
					}
						if(i>4){
						Card card = new Card();
					  	card.setCard(suit,i);
					  	deck.add(card);
						}
				  	}
				  	
			 }
		  }
		 
		  System.out.println(deck.get(0).getValue());
	  System.out.println(deck.get(0).getScore());
	
		  long seed = System.nanoTime();
		  Collections.shuffle(deck, new Random(seed));
		  
	  }	  
	
	
	
}


