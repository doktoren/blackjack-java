/**
 * <code>BlackjackStrategy</code> implements a blackjack player.
 * All memory of previously played cards is encapsulated by the class {@link BlackjackCardCounter}.<BR>
 * An implementation of <code>BlackjackStrategy</code> should have a constructor with the arguments
 * {@link BlackjackVariation} and {@link BlackjackCardCounter} and throw an error if these arguments
 * provides insufficient support.<BR>
 * The methods are called in the order split, surrender, doubleUp, hit.
 * 
 * @author Jesper Kristensen
 * @see BlackjackCardCounter
 * @see BlackjackVariation
 */
public interface BlackjackStrategy {
	
	/**
	 * Determines the amount of money to bet. The amount should be valid according to the instance of
	 * <code>BlackjackVariation</code> applied at construction time. Also, it should be no larger than
	 * <code>capitalLeft</code>. If an invalid bet is made it will be replaced by the minimum allowed bet.
	 * 
	 * @param capitalLeft the money available to the player.
	 * @return the amount of money to bet
	 */
	public int bet(int capitalLeft);
	
    /**
     * <code>init</code> is called after cards have been dealt and before any
     * decisions have been maken.
     * 
     * @param upcard the card in front of the dealer.
     * @param card1 the players first card.
     * @param card2 the players second card.
     */
    public void init(int upcard, int card1, int card2);

    /**
     * Todo: This decision should also be based on the amount of money the player has left. 
     * 
     * @param upcard the card in front of the dealer.
     * @param pair the card that the players has paired.
     * @return true iff the pair should be split.
     */
    public boolean split(int upcard, int pair);

    /**
     * @param upcard the card in front of the dealer.
     * @param total the (best) sum of the players cards.
     * @param soft true iff it's a soft total.
     * @return true iff the hand should be surrendered.
     */
    public boolean surrender(int upcard, int total, boolean soft);
    
    /**
     * @param upcard the card in front of the dealer.
     * @param total the (best) sum of the players cards.
     * @param soft true iff it's a soft total.
     * @return true iff the hand should be doubled.
     */
    public boolean doubleUp(int upcard, int total, boolean soft);

    /**
     * @param upcard the card in front of the dealer.
     * @param total the (best) sum of the players cards.
     * @param soft true iff it's a soft total.
     * @return true iff another card should be received.
     */
    public boolean hit(int upcard, int total, boolean soft);

    
    /**
     * @param total the (best) sum of the players cards.
     * @param soft true iff it's a soft total.
     * @return true iff an insurance wish to be made.
     */
    public boolean insure(int total, boolean soft);
}