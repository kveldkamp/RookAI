import java.util.*;

public class RPC extends NPC {

   Scanner scan = new Scanner(System.in);

   public Card.Suit chooseTrump() {
      System.out.println("Hello");
      return Card.Suit.NOSUIT;
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
