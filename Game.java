/*
 * Game.java
 *
 * 10/17/14
 * Game Class includes functions for gameplay
 */
public class Game {
	Player [] players = new Player[4];
	
	
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
		       	  	if(bidders == 1){
		       		  bidwon = true;
		       		  System.out.println("last bidder won");
		       		  System.out.println("High bid is this at fin: " + highBid);
		       		  players[i].setHighBidder(highBid);
		       		  System.out.println(players[i].bidAmount);
		       		  break;
		       	  	}  
		     		  
		     	}

		 	  
		   }
		  
		  System.out.println("Done with bidding"); 
		}
}
