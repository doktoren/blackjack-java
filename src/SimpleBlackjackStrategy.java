public class SimpleBlackjackStrategy implements BlackjackStrategy {

	private BlackjackVariation variation;
	
	public SimpleBlackjackStrategy(BlackjackVariation variation)
	throws UnsupportedBlackjackVariationException {
		if (variation.getClass().getCanonicalName().compareTo("AarhusCasinoVariation") != 0)
			throw new UnsupportedBlackjackVariationException(this, variation);
		this.variation = variation;
	}
	
	public int bet(int capitalLeft) {
		return variation.minBet();
	}
	
	public void init(int upcard, int card1, int card2) {}
	
	public boolean split(int upcard, int pair) {
		return false;
	}
	
	public boolean surrender(int upcard, int total, boolean soft) {
		return false;
	}
	
	public boolean doubleUp(int upcard,	int total, boolean soft) {
		return false;
	}
	
	public boolean hit(int upcard, int total, boolean soft) {
		if (soft) {
			return total < 18;
		} else {
			if (upcard == 1  ||  upcard > 6) return total < 17;
			return total < 12;
		}
	}
	
	public boolean insure(int total, boolean soft) {
		return false;
	}
}