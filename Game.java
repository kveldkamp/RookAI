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
	Card.Suit trumpcolor = null; 
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
		     				  System.out.println("Current High is " + highBid);
		     				  //System.out.println("Bid Made");
		     			  
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
			     			  System.out.println("winning bidder is player " + b);
				       		  //System.out.println("Are they bidding?: " + players[b].bidding);
				       		  System.out.println("Highest bid: " + highBid);
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
		
	
	/*
	* PlayRound is the game play for a single round 
	* play starts with the bid winner leading, and cycles through
	*/
	public void PlayRound(){
     
	  //Call lead on whoever won the bid
     System.out.println("The player that leads is "+winner);
     //players[winner].lead();
 
	  
	  //Cycle through players and have them Play
	  int counter=0;
     
	  //Start i at whoever won bid, cycle through rest of players
	  for(int i=winner;counter<4;i=(i+1)%4)
	  {
	     currentTrick=players[i].Play(currentTrick,i);
        //Add the card that was played to AI's intelligence
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
				
		for(int i=0;i<44;i++){
					
			Card card = new Card();
			
			int temp = i % 4;
			
			if(temp==0){
				card.setCard(Card.Suit.RED,i);
			}
					
			else if(temp==1){
			card.setCard(Card.Suit.BLUE,i);
			}
					
			else if(temp==2){
				card.setCard(Card.Suit.GREEN,i);
			}
					
			else if(temp==3){
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
	Card[] hand1 = new Card[10];
	Card[] hand2 = new Card[10];
	Card[] hand3 = new Card[10];
	Card[] hand4 = new Card[10];
	
	Card[] deckArray = new Card[deck.size()];
	
	deckArray = deck.toArray(deckArray);
	
	int h1 = 0;
	int h2 = 0;
	int h3 = 0;
	int h4 = 0;
	for(int i=0;i<39;){
		
		hand1[h1]=deckArray[i];
		System.out.println(" ");
		System.out.print(hand1[h1].getCardVal());
		System.out.print(hand1[h1].getSuit());
		i++;
		h1++;
		hand2[h2]=deckArray[i];
		i++;
		h2++;
		hand3[h3] = deckArray[i];
		i++;
		h3++;
		hand4[h4]= deckArray[i];
		i++;	
		h4++;
		
	}
	
	
	

	
}

}


