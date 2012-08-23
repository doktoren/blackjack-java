/**
 * <code>BlackjackVariation</code> encapsulates the specialization of the rules used.
 * 
 * @author Jesper Kristensen
 */
public abstract class BlackjackVariation {

  public final int leastDeckSize() {
    return 52 * numberOfDecks() * (100 - deckPenetration()) / 100;
  }

  /**
   * @return the number of decks used.
   */
  public abstract int numberOfDecks();

  /**
   * @return the percentage of the cards used before reshuffling.
   */
  public abstract int deckPenetration();

  /**
   * By splitting n times n+1 hands will be played.
   * 
   * @return the number of splits that's allowed.
   */
  public abstract int maxSplits();

  /**
   * @param cardSum the sum of the two received cards.
   * @return true iff the player is allowed to double.
   */
  public abstract boolean doubleAllowed(int cardSum);

  /**
   * @return true iff the player is allowed to double after having split. 
   */
  public abstract boolean doubleAfterSplitAllowed();

  /**
   * @return true iff the dealer have to stand on soft 17.
   */
  public abstract boolean dealerStandsOnSoft17();

  /**
   * The minimum allowed bet has to be a multiple of 2.
   * 
   * @return the minimum allowed bet.
   */
  public abstract int minBet();

  /**
   * The maximum allowed bet has to be a multiple of 2.
   * 
   * @return the maximum allowed bet.
   */
  public abstract int maxBet();

  /**
   * The step between allowed bets has to be a multiple of 2.
   * Typically it is identical to <code>minBet</code>.
   * 
   * @return the step between allowed bet.
   */
  public abstract int betStep();
}