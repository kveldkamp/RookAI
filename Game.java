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
		   while(bidwon == false){
		 	  int bidders = 4;
		 	  for(int i = 0; i < players.length; i++){
		 			  players[i].bidOrPass(players[i].bidding);
		     		  if(players[i].bidding == true){
		     			  highBid = players[i].bid(highBid);
		     			  System.out.println("Bid Made");
		     			  System.out.println("highBid: " + highBid);
		     		  }
		     		  else{
		     			  System.out.println("This player passed...");
		     			  bidders--;
		     			  
		     		  }
		     		//Only one bidder left; set winning bidder
		       	  	if(bidders == 1){
		       		  bidwon = true;
		       		  System.out.println("last bidder won");
		       		  System.out.println("High bid is this at fin: " + highBid);
		       		  players[i].setHighBidder(highBid);
		       		  players[i].winningBiddingTeam = true;
		       		  winner = i; //This holds the winning bidder
		       		  System.out.println(players[i].bidAmount); //This does not output the high bid correctly
		       		  break;
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
		
		  for (Card.Suit suit : Card.Suit.values()){
			if(suit!=Card.Suit.NOSUIT){
				
				for(int i=1;i<=14;i++){
				  	Card card = new Card();
				  	card.setCard(suit,i);
				  	deck.add(card);
				  	
				  	}
			 }
		  }
		 
//		  System.out.println(deck.get(9).getValue());
//	  System.out.println(deck.get(9).getScore());
	
		  long seed = System.nanoTime();
		  Collections.shuffle(deck, new Random(seed));
		  
	  }	  
	
	
	
}


