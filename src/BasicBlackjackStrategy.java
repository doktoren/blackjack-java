public class BasicBlackjackStrategy implements BlackjackStrategy {
	
	private BlackjackVariation variation;
	
	public BasicBlackjackStrategy(BlackjackVariation variation)
	throws UnsupportedBlackjackVariationException {
		if (variation.getClass().getCanonicalName().compareTo("AarhusCasinoVariation") != 0)
			throw new UnsupportedBlackjackVariationException(this, variation);
		this.variation = variation;
	}
	
	public int bet(int capitalLeft) {
		return variation.minBet();
	}
	
	public void init(int upcard, int card1, int card2) {}
	
	// split_choice[pair][upcard]
	private static boolean[][] split_choice =
	{{false, false, false, false, false, false, false, false, false, false, false}, // unused
		{false, false, true,  true,  true,  true,  true,  true,  true,  true,  true }, // Ace
		{false, false, true,  true,  true,  true,  true,  true,  false, false, false}, // 2
		{false, false, true,  true,  true,  true,  true,  true,  false, false, false}, // 3
		{false, false, false, false, false, true,  true,  false, false, false, false}, // 4
		{false, false, false, false, false, false, false, false, false, false, false}, // 5
		{false, false, true,  true,  true,  true,  true,  true,  false, false, false}, // 6
		{false, false, true,  true,  true,  true,  true,  true,  true,  false, false}, // 7
		{false, false, true,  true,  true,  true,  true,  true,  true,  true,  false}, // 8
		{false, false, true,  true,  true,  true,  true,  false, true,  true,  false}, // 9
		{false, false, false, false, false, false, false, false, false, false, false}};// 10
	
	
	public boolean split(int upcard, int pair) {
		return split_choice[pair][upcard];
	}
	
	public boolean surrender(int upcard, int total, boolean soft) {
		return !soft  &&  ((total == 16  &&  (upcard == 10  ||  upcard == 1))  ||
				(total == 15  &&  upcard == 10));
	}
	
	public boolean doubleUp(int upcard, int total, boolean soft) {
		if (upcard == 1) return false;
		if (soft) {
			return upcard <= 6  &&  total <= 18  &&  total + 2*upcard >= 23;
		} else {
			return total <= 11  &&  total >= 9  &&  upcard != 10  &&
			(total != 9  ||  upcard >= 7);
		}
	}
	
	public boolean hit(int upcard, int total, boolean soft) {
		if (soft) {
			return total < 18  ||  (total == 18  &&  (upcard >= 9  ||  upcard == 1));
		} else {
			if (total < 12) return true;
			if (upcard > 6  ||  upcard == 1) return total <= 16;
			if (total > 12) return false;
			return upcard <= 3;
		}
	}
	
	public boolean insure(int total, boolean soft) {
		return false;
	}
}