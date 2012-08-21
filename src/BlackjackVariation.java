/**
 * <code>BlackjackVariation</code> encapsulates the specialization of the rules used.
 * 
 * @author Jesper Kristensen
 */
public interface BlackjackVariation {

	/**
	 * @return the number of decks used.
	 */
	public int numberOfDecks();

    /**
     * @return the percentage of the cards used before reshuffling.
     */
    public int deckPenetration();
    
    /**
     * By splitting n times n+1 hands will be played.
     * 
     * @return the number of splits that's allowed.
     */
    public int maxSplits();
    
    /**
     * @param cardSum the sum of the two received cards.
     * @return true iff the player is allowed to double.
     */
    public boolean doubleAllowed(int cardSum);
    
    /**
     * @return true iff the player is allowed to double after having split. 
     */
    public boolean doubleAfterSplitAllowed();

    /**
     * @return true iff the dealer have to stand on soft 17.
     */
    public boolean dealerStandsOnSoft17();
    
    /**
     * The minimum allowed bet has to be a multiplum of 2.
     * 
     * @return the minimum allowed bet.
     */
    public int minBet();
    
    /**
     * The maximum allowed bet has to be a multiplum of 2.
     * 
     * @return the maximum allowed bet.
     */
    public int maxBet();
    
    /**
     * The step between allowed bets has to be a multiplum of 2.
     * Typically it is identical to <code>minBet</code>.
     * 
     * @return the step between allowed bet.
     */
    public int betStep();
}