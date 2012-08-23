public class BlackjackCardCounter {
	
	public BlackjackCardCounter(int numDecks) {
		num_decks = numDecks;
		init();
	}

	public void init() {
		cards_left[0] = 52*num_decks;
		for (int i=1; i<10; i++)
			cards_left[i] = 4*num_decks;
		cards_left[10] = 4*4*num_decks;
	}
	
	public void setNumDecks(int numDecks) {
		num_decks = numDecks;
		init();
	}
	
	public int getNumDecks() { return num_decks; }
	
	public int getCardsLeft() { return cards_left[0]; }
	
	public int getCardsLeft(int value) { return cards_left[value]; }
	
	public void seenCard(int value) {
		--cards_left[value];
		--cards_left[0];
	}
	
	public void forgetCard(int value) {
		++cards_left[value];
		++cards_left[0];
	}

  /**
   * HiLo card counting stuff.
   * Returns the number of good cards left minus the number of bad cards left.
   */
  public int getBalance() {
    int goodCardsLeft = cards_left[1] + cards_left[10];
    int badCardsLeft = cards_left[2] + cards_left[3] + cards_left[4] + cards_left[5] + cards_left[6];
    return goodCardsLeft - badCardsLeft;
  }

  /**
   * HiLo card counting stuff.
   * Returns a measurement of the overweight of good cards left in the deck.
   */
  public int getWeightedBalance() {
    if (cards_left[0] == 0)
      return 0;
    return 52 * getBalance() / cards_left[0];
  }

	private int num_decks;
	
	// cards_left[0] is sum of cards_left[1..10]
	private int[] cards_left = new int[11];
}