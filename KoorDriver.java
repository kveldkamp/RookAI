import java.util.ArrayList;


public class KoorDriver {
   
   public static void main(String [] args) {

      
	   Game game = new Game();
	   
	   while(game.currentTeamScores[0] < 500 && game.currentTeamScores[1] < 500){
		   game.makeDeck();
		   game.dealCards();
		   game.Bidding();
		   game.sendKitty();
		   game.playGame();
	   }
	   
	   game.displayFinalScore();
	   

   }
}