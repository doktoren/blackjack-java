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
	
	private int num_decks;
	
	// cards_left[0] is sum of cards_left[1..10]
	private int[] cards_left = new int[11];
}