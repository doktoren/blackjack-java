public class Blackjack {
	
	public static boolean print = false;
	public static char[] M = {'#', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'X'};
	
	public static void simulateVisitingAarhusCasino() {
		/**
		 * Initial player capital ...
		 * 
		 * BlackjackVariation
		 * BlackjackCardCounter
		 * BlackjackStrategy
		 * BlackjackDecks
		 * BlackjackDeal
		 * 
		 * Continue playing while ...
		 * 
		 * Remember capital during play (to make a graph) ?
		 * (or maximal capital during 2^k successive games, for increasingly k)
		 * 
		 * Remember other stuff?
		 * 
		 */
	}
	
	
	public static void main(String args[]) {
		int num_deals = 1000000;
		int num_decks = 6;
		int least_deck_size = (40*num_decks*52)/100; // reshuffle when 40% left.
		
		System.out.println("Simulating " + num_deals + " deals.");
		
		BlackjackDecks decks = new BlackjackDecks(num_decks);
		BlackjackCardCounter counter = new BlackjackCardCounter(num_decks);
		BlackjackVariation variation = new AarhusCasinoVariation();
		BlackjackStrategy strategy;
		try {
			strategy = new BasicBlackjackStrategy(variation);
		} catch(UnsupportedBlackjackVariationException e) {
			System.out.println(e);
			return;
		}
		BlackjackDeal dealer = new BlackjackDeal();
		
		// Bet size is always 2
		int[] bankrolls = new int[105];
		int[] counts = new int[105];
		for (int i=0; i<105;i++) counts[i] = bankrolls[i] = 0;
		
		for (int i=0; i<num_deals; i++) {
			
			if (decks.cardsLeft() < least_deck_size) {
				// Reshuffle
				decks.init();
				counter.init();
			}
			
			int balance = decks.getBalance();
			bankrolls[balance] += dealer.deal(decks, counter, strategy);
			counts[balance]++;
		}
		System.out.println("Finished!");
		
		int bankroll = 0;
		for (int i=0; i<105; i++)
			if (counts[i] != 0) {
				System.out.println("Balance " + (i-52) + " (count " + counts[i] + "). Payback = " +
						((50.0/counts[i])*(2*counts[i] + bankrolls[i])));
				bankroll += bankrolls[i];
			}
		System.out.println("TOTAL: Result = " + bankroll);
		System.out.println("TOTAL: Payback = " + ((50.0/num_deals)*(2*num_deals + bankroll)));
	}
	
}