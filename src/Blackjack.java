public class Blackjack {
	
	public static boolean print = false;
	public static char[] M = {'#', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'X'};

	public static void main(String args[]) {
		int num_deals = 10000000;
		System.out.println("Simulating " + num_deals + " deals.");
    BlackjackVariation variation = new AarhusCasinoVariation();
		BlackjackDecks decks = new BlackjackDecks(variation.numberOfDecks());
		BlackjackCardCounter counter = new BlackjackCardCounter(variation.numberOfDecks());
		BlackjackStrategy strategy;
		try {
		  //strategy = new SimpleBlackjackStrategy(variation);
			//strategy = new StaticBlackjackStrategy(variation);
      strategy = new CardCountingStrategy(variation);
		} catch(UnsupportedBlackjackVariationException e) {
			System.out.println(e);
			return;
		}
		BlackjackDeal dealer = new BlackjackDeal();
		
		// The card counting balance cannot exceed max_balance
		final int max_balance = 16 * variation.numberOfDecks();
		final int balance_range = 2 * max_balance + 1;

		long[] bankrolls = new long[balance_range];
		long[] counts = new long[balance_range];
		long[] betSums = new long[balance_range];
		for (int i=0; i<balance_range;i++)
		  betSums[i] = counts[i] = bankrolls[i] = 0;
		
		for (int i=0; i<num_deals; i++) {
			if (decks.cardsLeft() < variation.leastDeckSize()) {
				// Reshuffle
				decks.init();
				counter.init();
			}
			
			int balance = counter.getWeightedBalance();
			int bet = strategy.bet(1000000000, counter);
			betSums[max_balance + balance] += bet;
			bankrolls[max_balance + balance] += (bet * dealer.deal(decks, counter, strategy))/2;
			counts[max_balance + balance]++;
		}
		System.out.println("Finished!");
		
		long bankroll = 0;
		long betSum = 0;
		for (int i=0; i<balance_range; i++)
			if (counts[i] != 0) {
				System.out.println("Balance " + (i - max_balance) + " (count " + counts[i] + "). Payback = " +
						((float)(bankrolls[i] + betSums[i]) / betSums[i]));
				bankroll += bankrolls[i];
				betSum += betSums[i];
			}
		System.out.println("TOTAL: Result = " + bankroll);
		System.out.println("TOTAL: Payback = " + ((float)(bankroll + betSum) / betSum));
	}
	
}