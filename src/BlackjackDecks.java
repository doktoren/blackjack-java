/**
 *  WARNING: No error checking!
 *  
 *  1 is ace, 10 is 10,knight,queen or king
 */
import java.util.Random;

public class BlackjackDecks {
	
	public BlackjackDecks(int numDecks) {
		r = new Random();
		num_decks = numDecks;
		init();
	}
	
	public BlackjackDecks(BlackjackCardCounter c) {
		r = new Random();
		num_decks = c.getNumDecks();
		cards_left[0] = c.getCardsLeft();
		for (int i=1; i<=10; i++)
			cards_left[i] = c.getCardsLeft(i);
	}
	
	public void init() {
		cards_left[0] = 52*num_decks;
		for (int i=1; i<10; i++)
			cards_left[i] = 4*num_decks;
		cards_left[10] = 4*4*num_decks;
	}
	
	public int getNumDecks() { return num_decks; }
	
	public void setNumDecks(int numDecks) {
		num_decks = numDecks;
		init();
	}
	
	public boolean empty() { return cards_left[0] == 0; }
	
	public int cardsLeft() { return cards_left[0]; }
	
	public int cardsLeft(int value) { return cards_left[value]; }
	
	public int drawRandomCard() {
		int tmp = r.nextInt(cards_left[0]);
		int i = 0;
		do {
			tmp -= cards_left[++i];
		} while (tmp >= 0);
		--cards_left[i];
		--cards_left[0];
		return i;
	}
	
	public boolean removeCard(int value) {
		if (cards_left[value] == 0) return false;
		--cards_left[value];
		--cards_left[0];
		return true;
	}
	
	public void putBackCard(int value) {
		++cards_left[value];
		++cards_left[0];
	}

	private int num_decks;
	
	// cards_left[0] is sum of cards_left[1..10]
	private int[] cards_left = new int[11];
	
	private Random r;
}