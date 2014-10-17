
public class KoorDriver {
   
   public static void main(String [] args) {

   
      Player [] players = new Player[4];
      players[0] = new NPC();
      players[1] = new NPC();
      players[2] = new NPC();
      players[3] = new RPC();
      
      //make the 44 cards with assigned values
      //deal the cards to the players
      Card [] currentTrick = new Card[4];
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
      
 }


//      Player [] players = new Player[4];
//      players[0] = new NPC();
//      players[1] = new NPC();
//      players[2] = new NPC();
//      players[3] = new RPC();
	   
	   Game game = new Game();
	   game.Bidding();
      
   }
   
   

}

