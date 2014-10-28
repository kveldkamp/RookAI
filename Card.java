/*
 * Card.java
 *
 * Created on February 20, 2005, 8:12 AM
 */


import java.util.*;

/**
 * Represents a single playing card; these are stored in arrays to represent each player's hand.
 */
public class Card {

	/**
	 * An enumerated type for the four suits or colors in the game of Rook: red, blue, green, and black.
	 */
    public enum Suit { RED, BLUE, GREEN, BLACK, NOSUIT };

    private Suit suit;
    private String image;
    private int value;
    private int cardVal;
    
    /** Creates a new instance of Card */
    public Card() {
		value = -1;	//initialize this because isRook() might be called on a new Card object
    }

    /**
     * Sets the suit variable of the card to a new value.
     * @param newSuit The new variable that suit will be set to.
     */
    public void setSuit (Suit newSuit) {
        suit = newSuit;
    }
    
    
    /**
     * Sets the suit variable of the card to a new value.
     * @param newSuit The new variable that suit will be set to.
     */
    public void setCardValue (int newValue) {
        cardVal = newValue;
    }
    

    /**
     * Sets both member variables of the card to new values.
     * @param newSuit The new value that suit will be set to.
     * @param newValue The integer value tells what card within a suit it is.
     */
    public void setCard (Suit newSuit, int newValue) {
        suit = newSuit;
        value = newValue;
    }
    

    /**
     * Retrieves the integer member variable value.
     * @return The integer value of the card within the deck (0-44).
     */
    public int getValue () {
        return value;
    }

    /**
	 * Retrieves the rank of the card, computed from the value member variable.
	 * @return The rank of the card, where 0-5 are five through ten, 6 is rook, 7-10 are eleven through fourteen, and 11 is one.
     */
    public int getRank () {
		if (isRook()) {
			return 6;
		}
		else {
			int temp = value % 11;
			if (temp > 5) temp += 1;
			return temp;
		}
	}

	/**
	 * Returns whether or not the card is the rook
	 * @return True if the card is the rook, false otherwise.
     */
	public boolean isRook() {
		return value == 44;
	}

    /**
     * Retrieves the member variable suit, an enum of type Suit.
     * @return suit The suit of the card.
     */
    public Suit getSuit () {
        return suit;
    }
    

    /**
	 * Determines the score for taking the card as part of a trick.
	 * @return The card's score, or 0 if the card is not a scoring card.
 	 */
    public int getScore () {
		if (isRook()) {
			return 20;
		}
		else {
			switch(getRank()) {
				case 0: //five
					return 5;
				case 5: //ten
				case 10: //fourteen
					return 10;
				case 11: //one
					return 15;
				default:
					return 0;
			}
		}
	}
    /**
     * Sets the physical number of the card based on it's "rank" just so that we have a function where we 
     * actually know what cards a player actually has
     */
    public void setCardVal(){
    	
    	switch(getRank()){
    		case 0:
    			cardVal = 5;
    		break;
    		case 1:
    			cardVal = 6;
    		break;
    		case 2:
    			cardVal = 7;
    		break;
    		case 3:
    			cardVal = 8;
    		break;
    		case 4:
    			cardVal = 9;
    		break;
    		case 5:
    			cardVal = 10;
    		break;
    		case 6:
    			cardVal = 44;
    		break;
    		case 7:
    			cardVal = 11;
    		break;
    		case 8:
    			cardVal = 12;
    		break;
    		case 9:
    			cardVal = 13;
    		break;
    		case 10:
    			cardVal = 14;
    		break;
    		case 11:
    			cardVal = 1;
    		break;
    		default:
    			cardVal = 0;
    			
    	}
    }
    
    
    /**
     * 
     * @returns the actual number that would be on the physical card
     */
    public int getCardVal(){
    	return cardVal;
    }
}


