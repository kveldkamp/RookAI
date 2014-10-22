/*
 * NPC.java
 *
 * Created on February 20, 2005, 8:13 AM
 *
 * Edited February/March 2011
 */


import java.util.*;


/**
 * A subclass of Player containing variables and methods specific to the computer-controlled players.
 */
public class NPC extends Player {
    static int RookValue = 44;
    static int RookRank = 6;
    static int notFound = -1;
    int handValue = 0;
    int longestSuitLength = 0;
    int maxBid = 200;
    boolean[] discarded = new boolean[15];
    int numberPossibleCards = 15;
    
    private boolean testCheck = true;

    /**
     * The amount that we bid, if we were the winning bidder.
     */

    //begin methods

    /** Creates a new instance of NPC
     * Perhaps need to initialize/reset ArrayLists and trumpSuit/numTrump.
     */
    public NPC() {
    }

    /**
     * Choose a suit to be the trump (called if we win the bidding).
     * @return The chosen suit.
     */
    @Override
    public Card.Suit chooseTrump() {
        // just choose the suit of the first card in our hand
        //needs to be altered to choose best suit (highest value/deepest)

        //these two commands need to be paired in this order so trump data is stored
    	//STRONGEST SUIT IS ALWAYS RED
        setTrump(this.strongestSuit);
        return this.strongestSuit;
    }
    
    /**
     * Choose five cards to discard (called if we win the bidding).
     * @return An array the size of the hand with boolean values for whether or
     *         not each card is to be discarded.
     */
     
    public boolean[] chooseDiscards() {
	numberPossibleCards = 15;

        determineSuitLengths();
        determineStrongestSuit();
        secondLongestSuit = determineSecondLongestSuit();

        System.out.println("strongestSuit = " + strongestSuit); //debug
        System.out.println("secondLongestSuit = " + secondLongestSuit); //debug

        resetDiscardedValues();

        setStrongestSuitToFalse();

        setRankOneCardsToFalse();

        if(numberPossibleCards < 5)
        {
            for(int k = 0; k < 15; k++)
            {
                if(discarded[k] &&
                   hand[k].getRank() == 11 &&
                   hand[k].getSuit() != strongestSuit)
                {
                    discarded[k] = false;
                    numberPossibleCards--;
                }
            }
            for(int j = 0; j < 15; j++)
            {
                if(!discarded[j] &&
                   hand[j].getRank() != 11 &&
                   numberPossibleCards < 5)
                {
                    discarded[j] = true;
                    numberPossibleCards++;
                }
            }
        }
        
        attemptToShortSuit();

        if(numberPossibleCards > 5)
        {
            choosePossibleWinnersToKeep();
        }

        return discarded;
    }

    /**
     * Choose whether to make a bid or pass (sets our bidding flag rather than returning a value).
     * New system:  Count rank of cards, boost for multiplicity of suit, and decide then.
     */
    public void bidOrPass(boolean bid) {
    	//50/50 chance of bid or pass
        Random generator = new Random();
        int num = generator.nextInt(2);
        if (num == 1) {
            bidding = false;
        }
        //handValue = determineHandStrength();
        //longestSuitLength = longestSuit();
        else{
        	bidding = bid;
        }
        
    }

    /**
     * Choose an amount to bid.
     * @param highBid The current high bid.
     * @return The amount to bid.
     */
    public int bid(int highBid) {
        // bid 100 if we're the first to bid, otherwise bid the high bid + 5
        if(highBid == 0) {
            return 100;
        }
        //Make sure highbid won't exceed 200
        else{
            return highBid + 5;
        }
    }

    /**
     * Called if we win the bidding process - sets our member variables so that we know we won and know how much we bid, which the AI might want to take into account later.
     * @param amount The amount of the winning bid.
     */
    public void setHighBidder(int amount) {
        //setBidWinner();
        bidAmount = amount;
    }

    /**
     * Choose a card to lead with - most of the AI grunt work will happen here.
     * @return The position in our hand of the card chosen.
     */
    public int lead() {
        Card[] validCards = new Card[15];
        int maxIndex = -1;
        ArrayList<Card> prospWinners = new ArrayList<Card>();
		// Tally cards in hand that are not null, put in list
		// calculate the winners left in the game and play one
                // More rules will need to be called heavily here

                
            //if(bidWinner)
             //{
                   validCards = getValidLeadCards();
                   maxIndex = getValidLeadMaxIndex();
                //get valid cards (non-null)
		
		if (maxIndex == -1) {
			System.err.println("Error: NPC hand empty in NPC.lead() - shouldn't happen");
			System.exit(1);
		}
                
                prospWinners = winningCards();

                if(winningBiddingTeam)
                    System.out.println("Bidding Team player leading");
                else
                    System.out.println("Setting Team player leading");
                //gameplay if all trump is not accounted for
                if(!checkOthersOutOfTrump() && winningBiddingTeam  && stillHasTrump()) //need to include check for if only partner has trump
                {
                    //play high trump card if you have it
                    if(!(hasHighTrump(prospWinners) == notFound))
                        return hasHighTrump(prospWinners);
                    else if(!rookPlayed && !(hasRookCatchers() == notFound))
                    {
                        if(testCheck)
                            System.out.println("PLAYING ROOK CATCHER");
                        return hasRookCatchers();
                    }
                    else if(!(playerHasTrump() == notFound))
                    {
                        if(testCheck)
                            System.out.println("LEADING OUT OF TRUMP");
                        return playerHasTrump();
                    }
                }
                //if others out of trump or on the not-winning team, go to else
                else
                {
                    if(hasNonTrumpWinners(prospWinners) != notFound)
                    {
                        if(testCheck)
                            System.out.println("PLAYING A NON-TRUMP PROSPECTIVE WINNER");
                        return hasNonTrumpWinners(prospWinners);
                    }
                    else if(winningBiddingTeam && GetThrowAwayBidTeamLeadCard() != notFound)
                    {
                        if(testCheck)
                            System.out.println("Playing a throw away card for bid winning team,"
                                    + " the " + hand[GetThrowAwayBidTeamLeadCard()].getSuit() + " "
                                    + hand[GetThrowAwayBidTeamLeadCard()].getRank());
                        return GetThrowAwayBidTeamLeadCard();
                    }
                    else if(!winningBiddingTeam && getThrowAwaySetTeamLeadCard() != notFound)
                    {
                        if(testCheck)
                            System.out.println("Playing a throw away card for setting team,"
                                    + " the " + hand[getThrowAwaySetTeamLeadCard()].getSuit() + " "
                                    + hand[getThrowAwaySetTeamLeadCard()].getRank());
                        return getThrowAwaySetTeamLeadCard();
                    }
                    //need an else method to play a non-point, non trump if possible.  Defaulting to first valid card
                    //is not good. a "GetThrowAway" method perhaps.  
                }
             // }
                if(testCheck)
                    System.out.println("Default, playing first valid card");
                return getHandIndexOf(validCards[0]); // valid play if other rules don't fire. (first card, probable loser, whatever suit is first)
        }

    /**
     * Choose a card to play - most of the AI grunt work will happen here.
     * @param trick The cards that have been played already in the current trick.
     * @return The position in our hand of the card chosen.
     */
    public int playFollowLeadCard(Card trick[]) {
        // track acceptable responses and figure out play.
        Card[] validCards = new Card[15];
        int maxIndex = -1;

        // compile a list of playable cards, have rules based on this list
        validCards = getValidFollowCards(trick);
        maxIndex = getValidFollowMaxIndex(trick);
        Card currentHigh = getCurrentHighCard(trick);
        boolean partnerIsHigh = partnerCurrentlyWinning(trick);
        ArrayList<Card> prospWinners = new ArrayList<Card>();
        prospWinners = winningCards();
        boolean trumpLed;
        if(trick[0].getSuit() == trumpSuit)
            trumpLed = true;
        else
            trumpLed = false;

        if(testCheck)
            System.out.println("Current high card is " + currentHigh.getRank() + " of " + currentHigh.getSuit());
        if(partnerIsHigh == true && testCheck)
            System.out.println("PARTNER IS CURRENTLY WINNING");
        else if(testCheck)
            System.out.println("Partner is currently losing");
        
        //need to replace code, currently plays the first card that is greater
        //than the current card being led.
        //if prospectivewinner and card > highestcard and suits are == then take
        //possible exception: if you're last player and you don't have winner of your own?
        //if(winningBiddingTeam)
        //{
            if(partnerIsHigh)
            {
                if(currentHighShouldWin(trick, currentHigh) || trick[2] != null)
                {
                    if(testCheck)
                        System.out.println("Partner should win!");
                    if(trumpLed && hasRook() != -1)
                    {
                        System.out.println("Dumping rook to partner");
                        return hasRook();
                    }
                    if(getNonWinnerPointsCard(validCards, maxIndex, trumpLed) != notFound)
                    {
                        if(testCheck)
                            System.out.println("Partner will win, playing a non-high points card");
                        return getNonWinnerPointsCard(validCards, maxIndex, trumpLed);
                    }
                    if(getAnyPointsCard(validCards, maxIndex, trumpLed) != notFound)
                    {
                        if(testCheck)
                            System.out.println("Partner will win, playing first point card found");
                        return getAnyPointsCard(validCards, maxIndex, trumpLed);
                    }
                    //return low card/nonpoint/nontrump if possible
                    if(findThrowAwayCard(validCards, maxIndex, trumpLed)!= notFound)
                    {
                        System.out.println("Playing first throw away card found");
                        return findThrowAwayCard(validCards, maxIndex, trumpLed);
                    }
                }
                else if(partnerMayLose(trick, currentHigh))
                {
                    if(testCheck)
                        System.out.println("Partner may lose");
                    if(getHandWinner(trick, currentHigh, validCards, maxIndex) != notFound)
                    {
                        if(testCheck)
                            System.out.println("Playing winning card");
                        return getHandWinner(trick, currentHigh, validCards, maxIndex);
                    }
                    //check for trump in
                    if(canTrumpIn(validCards, maxIndex) != notFound && trickHasPoints(trick) && notLastTrump())
                    {
                        System.out.println("Trumping in, points at stake");
                        return canTrumpIn(validCards, maxIndex);
                    }

                    //find throw away
                    if(findThrowAwayCard(validCards, maxIndex, trumpLed)!= notFound)
                    {
                        System.out.println("Playing first throw away card found");
                        return findThrowAwayCard(validCards, maxIndex, trumpLed);
                    }
                }
            }
            else //!partnerIsHigh
            {
                //same suit winner check
               if(getHandWinner(trick, currentHigh, validCards, maxIndex) != notFound)
               {
                   if(testCheck)
                       System.out.println("Playing winning card");
                   return getHandWinner(trick, currentHigh, validCards, maxIndex);
               }
               //trump in check
               if(canTrumpIn(validCards, maxIndex) != notFound && !trumpLed && (trickHasPoints(trick) || trick[1] == null) && notLastTrump())
               {
                   if(winningBiddingTeam)
                   {
                        System.out.println("Trumping in, points at stake or a enemy yet to play");
                   }
                   else
                       System.out.println("Trumping in, points at stake or a partner yet to play");
                   return canTrumpIn(validCards, maxIndex);
               }

               //can't win options, toss points if partner hasn't played.
               if(trick[1] == null && !winningBiddingTeam && !(currentHighShouldWin(trick, currentHigh)))
               {
                    if(getNonWinnerPointsCard(validCards, maxIndex, trumpLed) != notFound)
                    {
                        if(testCheck)
                            System.out.println("Partner has yet to play, playing a non-high points card");
                        return getNonWinnerPointsCard(validCards, maxIndex, trumpLed);
                    }
                    if(getAnyPointsCard(validCards, maxIndex, trumpLed) != notFound)
                    {
                        if(testCheck)
                            System.out.println("Partner has yet to play, playing first point card found");
                        return getAnyPointsCard(validCards, maxIndex, trumpLed);
                    }
               }

               //if need throw away card
               if(findThrowAwayCard(validCards, maxIndex, trumpLed)!= notFound)
               {
                   System.out.println("Playing first throw away card found");
                   return findThrowAwayCard(validCards, maxIndex, trumpLed);
               }
            }
        //}
        //else if(!winningBiddingTeam)
        //{
        //}
        /*
        for(int i =0; i < maxIndex; i++)
        {
            if(validCards[i].getRank() > currentHigh.getRank() && (validCards[i].getSuit() == trumpSuit || validCards[i].getSuit() == currentHigh.getSuit()))
            {
                if(testCheck)
                    System.out.println("Playing a card greater than current high card, card played is "
                            + validCards[i].getRank() + " of " + validCards[i].getSuit());
                return getHandIndexOf(validCards[i]);
            }
        }
         *
         */


        //play first valid card found as default
        if(testCheck)
            System.out.println("Playing first valid card found as default");
	return getHandIndexOf(validCards[0]);
    }
    /* Method moved to Player.java
    /**
     * Store the data from a card played, received from GamePlayController,
     * data is used to determine what cards to play in each situation.
     * @param played the card which has been played and thus is out of play.
     
    public void cardPlayed(Card played)
    {
        if(played.getSuit() == Suit.RED)
            RedSeen.add(played);
        if(played.getSuit() == Suit.BLACK)
            BlackSeen.add(played);
        if(played.getSuit() == Suit.BLUE)
            BlueSeen.add(played);
        if(played.getSuit() == Suit.GREEN)
            GreenSeen.add(played);
        if(played.getSuit() == getTrump())
            numTrump++;
    }
     */

    /**
     * Calculate the top card of each of the 4 suits based on cardsSeen
     * @return winners which is an arrayList<Card>
     */
    public ArrayList<Card> winningCards()
    {
        Card deck[] = new Card[45];
        int noSuitLeft = -1;
        ArrayList<Card> winners = new ArrayList<Card>();
        for(int i = 0; i <= 44; i++) {
            deck[i] = new Card();
            if(i <= 10) {
                deck[i].setCard(Card.Suit.RED, i);
            } else if(i <= 21){
                deck[i].setCard(Card.Suit.BLUE, i);
            } else if (i <=32) {
                deck[i].setCard(Card.Suit.GREEN, i);
            } else if (i <= 43) {
                deck[i].setCard(Card.Suit.BLACK, i);
            } else if (i == 44)
                deck[i].setCard(trumpSuit, i);
        }

        //indices(and value) 0-10 red, 11-21 blue, 22-32 green, 33-43 black, 44 rook
        //remove from "deck" all cards that have been played
        for(int i = 0; i < BlackSeen.size(); i++)
        {
            deck[BlackSeen.get(i).getValue()] = null;
        }
        for(int i = 0; i < BlueSeen.size(); i++)
        {
            deck[BlueSeen.get(i).getValue()] = null;
        }
        for(int i = 0; i < RedSeen.size(); i++)
        {
            deck[RedSeen.get(i).getValue()] = null;
        }
        for(int i = 0; i < GreenSeen.size(); i++)
        {
            deck[GreenSeen.get(i).getValue()] = null;
        }

        int highestBlack = noSuitLeft, highestBlue = noSuitLeft, highestGreen = noSuitLeft, highestRed = noSuitLeft;
        for(int i = 0; i <= 44; i++) {
            if(i <= 10 && deck[i] != null) {
                highestRed = i;
            } else if(i <= 21 && deck[i] != null){
                highestBlue = i;
            } else if (i <=32 && deck[i] != null) {
                highestGreen = i;
            } else if (i <= 43 && deck[i] != null) {
                highestBlack = i;
            } else if (i == 44 && deck[i] != null)
                deck[i].setCard(trumpSuit, i);
        }
        /*
        System.out.println("Highest Black" + highestBlack);
        System.out.println("Highest Blue" + highestBlue);
        System.out.println("Highest Red" + highestRed);
        System.out.println("Highest Green" + highestGreen);
         */

        //check rook against the trump suit highest
        if(deck[44] != null)
        {
        if(highestGreen != noSuitLeft && trumpSuit == Card.Suit.GREEN && deck[highestGreen].getRank() <  RookRank)
            highestGreen = RookValue;
        if(highestRed != noSuitLeft && trumpSuit == Card.Suit.RED && deck[highestRed].getRank() <  RookRank)
            highestRed = RookValue;
        if(highestBlue != noSuitLeft && trumpSuit == Card.Suit.BLUE && deck[highestBlue].getRank() <  RookRank)
            highestBlue = RookValue;
        if(highestBlack != noSuitLeft && trumpSuit == Card.Suit.BLACK && deck[highestBlack].getRank() <  RookRank)
            highestBlack = RookValue;
        }

        //add highest to winners
        if(highestBlack != noSuitLeft)
        {
            winners.add(deck[highestBlack]);
        }
        if(highestRed != noSuitLeft)
        {
            winners.add(deck[highestRed]);
        }
        if(highestBlue != noSuitLeft)
        {
            winners.add(deck[highestBlue]);
        }
        if(highestGreen != noSuitLeft)
        {
            winners.add(deck[highestGreen]);
        }
        return winners;
    }

    /**
     * Method to account for other people being out of trump.
     * @return true if all other trump is accounted for.
     */
    public boolean checkOthersOutOfTrump() //checks to see if other players have trump left.
    {
        //short-circuit for if both opponents out of trump.
        if(getEnemiesOutOfTrump())
            return true;
     int totalTrump = 12 - numTrump;
     for(int i = 0; i < 15; i++) {
            if(hand[i] != null && hand[i].getSuit() == trumpSuit) {
				totalTrump--;
			}
        }
     //error check
     if(totalTrump == 0 && testCheck)
                System.out.println("OTHER PLAYERS OUT OF TRUMP");
     return (totalTrump == 0);
    }

    /**
     * Searches through hand[] and gathers cards in hand (non-null) into validLeadCards
     * @return validLeadCards a Card array.
     */
    public Card[] getValidLeadCards()
    {
        Card[] validLeadCards = new Card[15];
        int maxIndex = -1;
        //code to get valid cards
                for(int i = 0; i < 15; i++) {
                    if(hand[i] != null)
                    {
                        maxIndex++;
                        validLeadCards[maxIndex] = hand[i];
                    }
		}
                    return validLeadCards;
    }

    /**
     * Returns the max index of valid cards array
     * @return maxIndex an integer which marks the index of the last card in hand
     */
    public int getValidLeadMaxIndex()
    {
        Card[] validLeadCards = new Card[15];
        int maxIndex = -1;
        //code to get valid cards
                for(int i = 0; i < 15; i++) {
                    if(hand[i] != null)
                    {
                        maxIndex++;
                        validLeadCards[maxIndex] = hand[i];
                    }
		}
                    return maxIndex;
    }


    /**
     * Method that searches to find if player has winning cards.
     * @param potentialWinners is an ArrayList<Card> essentially the same as prospWinners
     * @return -1 if not found, if found, then return the trumpIgnoredWinner (== handIndex)
     */
   public int hasNonTrumpWinners(ArrayList<Card> potentialWinners)
    {
       for(int i = 0; i < potentialWinners.size(); i++)
                {
                    for(int handIndex = 0; handIndex < 15; handIndex++)
                    {
                        if(hand[handIndex] != null && potentialWinners.get(i) != null && hand[handIndex].getValue() == potentialWinners.get(i).getValue() && hand[handIndex].getSuit() != trumpSuit)
                        {
                            //System.out.println("Playing Winner from hand index " + handIndex + " value of " + hand[handIndex].getValue());
                            return handIndex;
                        }
                    }
                }
       return -1;
    }

   /**
    * Code to gather cards that follow suit in an array
    * @param trick
    * @return validFollowCards[] which is the valid card play options.
    */
    public Card[] getValidFollowCards(Card trick[])
    {
        Card card = null;
        Card[] validFollowCards = new Card[15];
        int maxIndex = -1;
        
        //code to get valid cards while following suit
                for(int i = 0; i < 15; i++) {
                    if(hand[i] != null && hand[i].getSuit() == trick[0].getSuit())
                    {
			               card = hand[i];
                        maxIndex++;
                        validFollowCards[maxIndex] = hand[i];
                    }
		}

                    if (card == null) {	// we don't have a card of the same suit
			// compile list of cards in hand, have rules based on list
			for (int i = 0; i<15; i++) {
				if (hand[i] != null) {
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
     *
     * @param trick the cards in trick already played
     * @return the max index of the validFollowCards array.
     */
    public int getValidFollowMaxIndex(Card trick[])
    {
        Card card = null;
        Card[] validFollowCards = new Card[15];
        int maxIndex = -1;

        //code to get valid cards while following suit
                for(int i = 0; i < 15; i++) {
                    if(hand[i] != null && hand[i].getSuit() == trick[0].getSuit())
                    {
			card = hand[i];
                        maxIndex++;
                        validFollowCards[maxIndex] = hand[i];
                    }
		}

                    if (card == null) {	// we don't have a card of the same suit
			// compile list of cards in hand, have rules based on list
			for (int i = 0; i<15; i++) {
				if (hand[i] != null) {
                                        maxIndex++;
                                        validFollowCards[maxIndex] = hand[i];
                                        card = hand[i];
				}
			}
        }
        if(maxIndex == -1)
            System.out.println("ERROR.  No Card to be played");
        return maxIndex;
    }

    /**
     * method to look through hand[] for a specific card.
     * @param searchCard to be searched for
     * @return index of card if found, else, -1
     */
    public int getHandIndexOf(Card searchCard)
    {
        for (int i = 0; i<15; i++) {
            if (hand[i] != null) {
                if(hand[i].getValue() == searchCard.getValue() && hand[i].getSuit() == searchCard.getSuit())
                    return i;
            }
        }
        return -1;
    }


    /**
     * Method that searches hand for the high Trump
     * @param potentialWinners the high cards of every suit
     * @return the index of the high card if found, otherwise -1
     */
    public int hasHighTrump(ArrayList<Card> potentialWinners)
    {
        for(int i = 0; i < potentialWinners.size(); i++)
                {
                    for(int handIndex = 0; handIndex < 15; handIndex++)
                    {
                        if(hand[handIndex] != null && potentialWinners.get(i) != null && hand[handIndex].getValue() == potentialWinners.get(i).getValue()
                                && hand[handIndex].getSuit() == getTrump())
                        {
                            if(testCheck)
                                System.out.println("Playing High Trump from hand index " + handIndex + " value of " + hand[handIndex].getValue());
                            cardPlayed(hand[handIndex]);
                            ArrayList<Card> prospWinners = new ArrayList<Card>();
                            prospWinners = winningCards();
                            int returner = hasHighTrump(prospWinners);
                            cardUnPlayed(hand[handIndex]);
                            if(returner != notFound)
                                return returner;
                            else

                                return handIndex;
                        }
                    }
                }
        return -1;
    }


    /**
     * This method returns the Card which is the current high card of the trick,
     * the  card to beat, etc.
     * @param trick
     * @return current which is the currentHighCard of the trick in play.
     */
    public Card getCurrentHighCard(Card trick[])
    {
        Card current = trick[0];
        Card.Suit suit = trick[0].getSuit();
        //trump led
        if(suit == trumpSuit)
        {
            for(int i = 1; i < 3; i++)
            {
                if(trick[i]!= null)
                {
                    if(trick[i].getRank() > current.getRank() && trick[i].getSuit() == trumpSuit)
                        current = trick[i];
                }
            }
        }
        //nontrump led
        else
        {
            for(int i = 1; i < 3; i++)
            {
                if(trick[i]!= null)
                {
                    if(trick[i].getSuit() == trumpSuit && current.getSuit() != trumpSuit)
                    {
                        current = trick[i];
                        suit = trumpSuit;
                    }
                    /*
                    else if(trick[i].getSuit() == trumpSuit && current.getSuit() == trumpSuit)
                    {
                        if(trick[i].getRank() > current.getRank())
                            current = trick[i];
                    }
                     */
                    else if(trick[i].getRank() > current.getRank() && trick[i].getSuit() == current.getSuit())
                    {
                        current = trick[i];
                    }
                }

            }
    }
        
        return current;
    }

    /**
     *
     * @param trick
     * @return true if the person with current high card in trick is your partner, false otherwise
     */
    public Boolean partnerCurrentlyWinning(Card trick[])
    {
        Card currentHigh = getCurrentHighCard(trick);
        int foundHigh = 0;
        int foundEnd = 1;
        boolean finder = false;
        for(int i = 0; i < 3; i++)
            {
                if(trick[i]!= null)
                {
                    if(trick[i]== currentHigh)
                    {
                        foundHigh = i;
                    }
                }
                else if (!finder){
                    foundEnd = i;
                    finder = true;
                }
            }
        return (((foundEnd - foundHigh) % 2) == 0);
    }

    /**
     * Search through hand for trump.  If it finds trump that is not worth points,
     * return true.  If it finds trump worth points, return that after the others
     * if it doesn't find trump, return notFound, aka -1
     * @return worthPoints which is an index of a valid trump play.
     */
    public int playerHasTrump()
    {
        int worthPoints = notFound;
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
                if(hand[i].getSuit() == trumpSuit)
                {
                    if(hand[i].getScore() == 0 )
                    {
                        return i;
                    }
                     else
                        worthPoints = i;
                }
        }
        return worthPoints;
    }

    /**
     *
     * @return index of a card > rook to prevent it being tossed too easily.
     */
    public int hasRookCatchers()
    {
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
                if(hand[i].getSuit() == trumpSuit)
                {
                    if(hand[i].getRank() > 6)
                        return i;
                }
        }
        return -1;
    }

    /**
     * Method that returns a non-point, non-trump, low card for the bid winning
     * team for the lead() method.
     * @return
     */
    public int GetThrowAwayBidTeamLeadCard()
    {
        int lowestRed = notFound;
        int lowestBlue = notFound;
        int lowestGreen = notFound;
        int lowestBlack = notFound;
        int numRed = 0;
        int numBlue = 0;
        int numGreen = 0;
        int numBlack = 0;

        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
            {
                if(hand[i].getSuit() != trumpSuit)
                {
                        if(hand[i].getSuit() == Card.Suit.RED)
                            numRed++;
                        if(hand[i].getSuit() == Card.Suit.BLUE)
                            numBlue++;
                        if(hand[i].getSuit() == Card.Suit.GREEN)
                            numGreen++;
                        if(hand[i].getSuit() == Card.Suit.BLACK)
                            numBlack++;
                    if(hand[i].getScore() == 0)
                    {
                        if(lowestRed == -1 && hand[i].getSuit() == Card.Suit.RED)
                            lowestRed = i;
                        if(lowestBlue == -1 && hand[i].getSuit() == Card.Suit.BLUE)
                            lowestBlue = i;
                        if(lowestGreen == -1 && hand[i].getSuit() == Card.Suit.GREEN)
                            lowestGreen = i;
                        if(lowestBlack == -1 && hand[i].getSuit() == Card.Suit.BLACK)
                            lowestBlack = i;
                    }
                }
            }
        }
        if(lowestRed != notFound&& (numRed >= numBlue || lowestBlue == notFound) && (numRed >= numGreen || lowestGreen == notFound) && (numRed >= numBlack || lowestBlack == notFound) )
            return lowestRed;
        if(lowestBlue != notFound&& (numBlue >= numRed || lowestRed == notFound) && (numBlue >= numGreen  || lowestGreen == notFound) && (numBlue >= numBlack || lowestBlack == notFound) )
            return lowestBlue;
        if(lowestGreen != notFound&& (numGreen >= numBlue || lowestBlue == notFound) && (numGreen >= numRed || lowestRed == notFound) && (numGreen >= numBlack || lowestBlack == notFound))
            return lowestGreen;
        if(lowestBlack != notFound&& (numBlack >= numBlue || lowestBlue == notFound) && (numBlack >= numGreen || lowestGreen == notFound) && (numBlack >= numRed || lowestRed == notFound) )
            return lowestBlack;
        //search through hand for first valid card of non-trump if prior search failed.
        
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
            {
                if(hand[i].getSuit() != trumpSuit)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Method that returns a point, non-trump, low card for the setting team
     * for the lead() method.  Returns hand index of the lowest point card
     * for the suit you have the most cards of that same suit that is not trump.
     * @return
     */
    public int getThrowAwaySetTeamLeadCard()
    {
        int lowestRed = notFound;
        int lowestBlue = notFound;
        int lowestGreen = notFound;
        int lowestBlack = notFound;
        int numRed = 0;
        int numBlue = 0;
        int numGreen = 0;
        int numBlack = 0;

        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
            {
                if(hand[i].getSuit() != trumpSuit)
                {
                        if(hand[i].getSuit() == Card.Suit.RED)
                            numRed++;
                        if(hand[i].getSuit() == Card.Suit.BLUE)
                            numBlue++;
                        if(hand[i].getSuit() == Card.Suit.GREEN)
                            numGreen++;
                        if(hand[i].getSuit() == Card.Suit.BLACK)
                            numBlack++;
                    if(hand[i].getScore() != 0)
                    {
                        if(lowestRed == -1 && hand[i].getSuit() == Card.Suit.RED)
                            lowestRed = i;
                        if(lowestBlue == -1 && hand[i].getSuit() == Card.Suit.BLUE)
                            lowestBlue = i;
                        if(lowestGreen == -1 && hand[i].getSuit() == Card.Suit.GREEN)
                            lowestGreen = i;
                        if(lowestBlack == -1 && hand[i].getSuit() == Card.Suit.BLACK)
                            lowestBlack = i;
                    }
                }
            }
        }
        if(lowestRed != notFound&& (numRed >= numBlue || lowestBlue == notFound) && (numRed >= numGreen || lowestGreen == notFound) && (numRed >= numBlack || lowestBlack == notFound) )
            return lowestRed;
        if(lowestBlue != notFound&& (numBlue >= numRed || lowestRed == notFound) && (numBlue >= numGreen  || lowestGreen == notFound) && (numBlue >= numBlack || lowestBlack == notFound) )
            return lowestBlue;
        if(lowestGreen != notFound&& (numGreen >= numBlue || lowestBlue == notFound) && (numGreen >= numRed || lowestRed == notFound) && (numGreen >= numBlack || lowestBlack == notFound))
            return lowestGreen;
        if(lowestBlack != notFound&& (numBlack >= numBlue || lowestBlue == notFound) && (numBlack >= numGreen || lowestGreen == notFound) && (numBlack >= numRed || lowestRed == notFound) )
            return lowestBlack;
        return -1;
    }

    /**
     * Returns true if partner should win trick, false otherwise.  Compares partner's
     * cards to the arrayLists of prospWinners.  Assume others will not trump.
     * @param trick the cards in the current trick
     * @param currentHighCard the current high card
     * @return true if partner is guaranteed to win, false otherwise
     */
    public boolean currentHighShouldWin(Card trick[], Card currentHighCard)
    {
        ArrayList<Card> prospWinners = new ArrayList<Card>();
        prospWinners = winningCards();
        //need to finish
        if(prospWinners.size() > 0)
        for(int i = 0; i < prospWinners.size(); i++)
        {
            if(prospWinners.get(i) != null && prospWinners.get(i).getSuit() == currentHighCard.getSuit())
            {
                if(prospWinners.get(i).getRank() <= currentHighCard.getRank())
                {
                    return true;
                }

                for(int handIndex = 0; handIndex < 15; handIndex++)
                {
                    if(hand[handIndex] != null && hand[handIndex].getValue() == prospWinners.get(i).getValue())
                    {
                       Card forwarding = new Card();
                       forwarding.setCard(prospWinners.get(i).getSuit(), prospWinners.get(i).getValue());
                       cardPlayed(forwarding);
                       boolean found = currentHighShouldWin(trick, currentHighCard);
                       cardUnPlayed(forwarding);
                       if(found)
                           return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a card that is not the current high card of a suit that is worth points
     * @param validCards
     * @param maxIndex
     * @param trumpLed
     * @return
     */
    public int getNonWinnerPointsCard(Card validCards[], int maxIndex, boolean trumpLed)
    {

        ArrayList<Card> prospWinners = new ArrayList<Card>();
        prospWinners = winningCards();
        //return first a points card that is not a winner
        for(int i = 0; i <= maxIndex; i++)
        {
            if(validCards[i].getScore() != 0 && ((validCards[i].getSuit() != trumpSuit) || trumpLed))
            {
                for(int checkPotentialWinner = 0; checkPotentialWinner < prospWinners.size(); checkPotentialWinner++)
                {
                    if(prospWinners.get(checkPotentialWinner) != null)
                        if(prospWinners.get(checkPotentialWinner).getSuit() == validCards[i].getSuit())
                            if(prospWinners.get(checkPotentialWinner).getValue() != validCards[i].getValue())
                                return getHandIndexOf(validCards[i]);
                }
            }
        }
        return -1;
    }

    /**
     * returns a non-trump points card.
     * @param validCards
     * @param maxIndex
     * @param trumpLed
     * @return any non-trump points card.
     */
    public int getAnyPointsCard(Card validCards[], int maxIndex, boolean trumpLed)
    {
        for(int i = 0; i <= maxIndex; i++)
        {
            if(validCards[i].getScore() != 0 && (validCards[i].getSuit() != trumpSuit || trumpLed))
            {
                return getHandIndexOf(validCards[i]);
            }
        }
        return -1;
    }

    /**
     * returns false if player is out of trump.
     * @return true if player not out of trump.
     */
    public boolean stillHasTrump()
    {
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null)
                if(hand[i].getSuit() == trumpSuit)
                    return true;
        }
        return false;
    }

    /**
     * check to see if currentHighCard is lower
     * @param trick
     * @param currentHighCard
     * @return
     */
    public boolean partnerMayLose(Card trick[], Card currentHighCard)
    {
        ArrayList<Card> prospWinners = new ArrayList<Card>();
        prospWinners = winningCards();
        //need to finish
        if(prospWinners.size() > 0)
        for(int i = 0; i < prospWinners.size(); i++)
        {
            if(prospWinners.get(i) != null && prospWinners.get(i).getSuit() == currentHighCard.getSuit())
            {
                if(prospWinners.get(i).getRank() > currentHighCard.getRank())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Use the winner of trick, the highest card playable.
     * @param trick
     * @param currentHighCard
     * @return
     */
    public int getHandWinner(Card trick[], Card currentHighCard, Card validCards[], int maxIndex)
    {
        ArrayList<Card> prospWinners = new ArrayList<Card>();
        prospWinners = winningCards();
        //need to finish
        if(prospWinners.size() > 0)
        {
            for(int i = 0; i < prospWinners.size(); i++)
            {
                if(prospWinners.get(i).getSuit() == currentHighCard.getSuit())
                {
                    for(int handIndex = 0; handIndex <= maxIndex; handIndex++)
                    {
                        if(validCards[handIndex] != null && validCards[handIndex].getValue() == prospWinners.get(i).getValue()
                                && validCards[handIndex].getRank() > currentHighCard.getRank())
                        {
                            return getHandIndexOf(validCards[handIndex]);
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Looks through hand for a non-potential winner, non point card
     * and non winner/point card trump only if trump led.
     * @param validCards
     * @param maxIndex
     * @param trumpLed
     * @return
     */
    public int findThrowAwayCard(Card validCards[], int maxIndex, boolean trumpLed)
    {
        ArrayList<Card> prospWinners = new ArrayList<Card>(4);
        prospWinners = winningCards();
        for(int i = 0; i <= maxIndex; i++)
        {
            //check to see if the valid card is a prospective winner
            if((prospWinners.size() < 1 ||validCards[i].getValue() != prospWinners.get(0).getValue())  && (prospWinners.size() < 2 ||validCards[i].getValue() != prospWinners.get(1).getValue())
                    && (prospWinners.size() < 3 ||validCards[i].getValue() != prospWinners.get(2).getValue()) && (prospWinners.size() < 4 ||validCards[i].getValue() != prospWinners.get(3).getValue())  )
                {
                    //check to see if the valid card is trump or worth points.
                    if(validCards[i].getScore() == 0 && (trumpLed ||validCards[i].getSuit() != trumpSuit))
                        return getHandIndexOf(validCards[i]);
                }
        }
        return -1;
    }

    /**
     * Looks for the first trump card available and returns its index.
     * @param validCards
     * @param maxIndex
     * @return the index of first trump found of validCards
     */
    public int canTrumpIn(Card validCards[], int maxIndex)
    {
        for(int i = 0; i <= maxIndex; i++)
        {
            if(validCards[i].getSuit() == trumpSuit)
                return getHandIndexOf(validCards[i]);
        }
        return -1;
    }

    /**
     * Check to see if trick has points already at stake.
     * @param trick
     * @return
     */
    public boolean trickHasPoints(Card trick[])
    {
        for(int i = 0; i < 4; i++)
        {
            if(trick[i] != null && trick[i].getScore() != 0)
                return true;
        }
        return false;
    }

    /**
     * check to make sure player has more than 1 trump left, always save a trump
     * for the last trick, it's worth more points.
     * @return
     */
    public boolean notLastTrump()
    {
        int numTrump = 0;
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null && hand[i].getSuit() == trumpSuit)
                numTrump++;
        }
        if(numTrump >= 2) return true;
        else
            return false;
    }

    /**
     * check to see if player has rook in hand to dump to partner.
     * @return
     */
    public int hasRook()
    {
        for(int i = 0; i < 15; i++)
        {
            if(hand[i] != null && hand[i].isRook())
                return i;
        }
        return -1;
    }

    /**
	 * Sets all trump (strongestSuit) indices to false and the rook if present
     */
    private void setStrongestSuitToFalse()
    {
        for(int i = 0; i < 15; i++)
        {
            //System.out.println(hand[i].getSuit() + ":" + hand[i].getRank());
            if(hand[i].getSuit() == strongestSuit)
            {
                discarded[i] = false;
                numberPossibleCards--;
            }
            if(hand[i].getRank() == 6 && isRookPresent)
            {
                discarded[i] = false;
                numberPossibleCards--;
            }
        }
    }

    /**
     * Resets the discarded[] array values to true;
     */
    private void resetDiscardedValues()
    {
        for(int i = 0; i < 15; i++)
        {
            discarded[i] = true;
        }
    }

    /**
     * Sets all  1's to not be discarded
     */
    private void setRankOneCardsToFalse()
    {
        for(int i = 0; i < 15; i++)
        {
            if(discarded[i] && hand[i].getRank() == 11 &&
               numberPossibleCards > 5)
            {
                discarded[i] = false;
                numberPossibleCards--;
            }
        }
    }

    /**
     * Determines second longest Suit
     * @Return the suit that is second in longest length
     */
    private Card.Suit determineSecondLongestSuit()
    {
        int second = 0;
        Card.Suit secondLongest = Card.Suit.NOSUIT;
        boolean suitsOfEqualLength = determineSuitsOfEqualLengthPresent();
            if(strongestSuit == Card.Suit.RED)
            {
                if(!suitsOfEqualLength)
                {
                    second = Math.max(blueLength, greenLength);
                    second = Math.max(blackLength, second);
                }
                else
                {
                    if(blueLength == greenLength)
                        second = Math.max(computeSuitStrength(Card.Suit.BLUE), computeSuitStrength(Card.Suit.GREEN));
                    else if(blueLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.BLUE), computeSuitStrength(Card.Suit.BLACK));
                    else if(greenLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.GREEN), computeSuitStrength(Card.Suit.BLACK));
                }

            }
            else if(strongestSuit == Card.Suit.GREEN)
            {
                if(!suitsOfEqualLength)
                {
                    second = Math.max(redLength, blueLength);
                    second = Math.max(blackLength, second);
                }
                else
                {
                    if(redLength == blueLength)
                        second = Math.max(computeSuitStrength(Card.Suit.BLUE), computeSuitStrength(Card.Suit.RED));
                    else if(redLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.RED), computeSuitStrength(Card.Suit.BLACK));
                    else if(blueLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.BLUE), computeSuitStrength(Card.Suit.BLACK));
                }
            }
            else if(strongestSuit == Card.Suit.BLUE)
            {
                if(!suitsOfEqualLength)
                {
                    second = Math.max(redLength, greenLength);
                    second = Math.max(blackLength, second);
                }
                else
                {
                    if(redLength == greenLength)
                        second = Math.max(computeSuitStrength(Card.Suit.GREEN), computeSuitStrength(Card.Suit.RED));
                    else if(redLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.RED), computeSuitStrength(Card.Suit.BLACK));
                    else if(greenLength == blackLength)
                        second = Math.max(computeSuitStrength(Card.Suit.GREEN), computeSuitStrength(Card.Suit.BLACK));
                }
            }
            else if(strongestSuit == Card.Suit.BLACK)
            {
                if(!suitsOfEqualLength)
                {
                    second = Math.max(redLength, greenLength);
                    second = Math.max(blueLength, second);
                }
                else
                {
                    if(redLength == greenLength)
                        second = Math.max(computeSuitStrength(Card.Suit.GREEN), computeSuitStrength(Card.Suit.RED));
                    else if(redLength == blueLength)
                        second = Math.max(computeSuitStrength(Card.Suit.RED), computeSuitStrength(Card.Suit.BLUE));
                    else if(greenLength == blueLength)
                        second = Math.max(computeSuitStrength(Card.Suit.GREEN), computeSuitStrength(Card.Suit.BLUE));
                }
            }
        if(!suitsOfEqualLength)
        {
            if(redLength == second && strongestSuit != Card.Suit.RED)
                secondLongest = Card.Suit.RED;
            else if(greenLength == second && strongestSuit != Card.Suit.GREEN)
                secondLongest = Card.Suit.GREEN;
            else if(blueLength == second && strongestSuit != Card.Suit.BLUE)
                secondLongest = Card.Suit.BLUE;
            else if(blackLength == second && strongestSuit != Card.Suit.BLACK)
                secondLongest = Card.Suit.BLACK;
        }
        else
        {
            if(computeSuitStrength(Card.Suit.RED) == second && strongestSuit != Card.Suit.RED)
                secondLongest = Card.Suit.RED;
            else if(computeSuitStrength(Card.Suit.GREEN) == second && strongestSuit != Card.Suit.GREEN)
                secondLongest = Card.Suit.GREEN;
            else if(computeSuitStrength(Card.Suit.BLUE) == second && strongestSuit != Card.Suit.BLUE)
                secondLongest = Card.Suit.BLUE;
            else if(computeSuitStrength(Card.Suit.BLACK) == second && strongestSuit != Card.Suit.BLACK)
                secondLongest = Card.Suit.BLACK;
        }
        return secondLongest;
    }

    /**
     * Determines if suits of equal length are present
     * @Return - true if suits of equal length are present false otherwise
     */
    private boolean determineSuitsOfEqualLengthPresent()
    {
        if(redLength == blueLength || redLength == greenLength ||
           redLength == blackLength || blueLength == greenLength ||
           blueLength == blackLength || greenLength == blackLength )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *  Attempts to short suit the hand by keeping the secondLongestSuit Suited
     *  cards
     */
    private void attemptToShortSuit()
    {
        for(int i = 14; i >= 0; i--)
        {
            if(discarded[i] && numberPossibleCards > 5 &&
               hand[i].getSuit() == secondLongestSuit)
            {
                discarded[i] = false;
                numberPossibleCards--;
            }
        }
    }

    /**
     * Attempts to choose possible winners by selecting the highest cards of the
     *  non-strongest suits.
     */
    private void choosePossibleWinnersToKeep()
    {
        Card.Suit lastWinnerSuit = strongestSuit;
        int lastWinnerRank = 0;
        int i = 14;
        while(numberPossibleCards > 5)
        {
            if(i < 0){i=14;}
            if(discarded[i] && numberPossibleCards > 5 &&
               hand[i].getSuit() != secondLongestSuit &&
               hand[i].getSuit() != strongestSuit &&
               lastWinnerSuit != hand[i].getSuit() &&
               lastWinnerRank <= hand[i].getRank())
            {
                discarded[i] = false;
                numberPossibleCards--;
                lastWinnerSuit = hand[i].getSuit();
                lastWinnerRank = hand[i].getRank();
            }
            i--;
        }
    }
}
