
public class AarhusCasinoVariation implements BlackjackVariation {

    public int numberOfDecks() {
    	return 6;
    }
   
    public int deckPenetration() {
    	return 60;
    }
    
    public int maxSplits() {
    	return 3;
    }
    
    public boolean doubleAllowed(int cardSum) {
    	return true;
    }
    
    public boolean doubleAfterSplitAllowed() {
    	return true;
    }

    public boolean dealerStandsOnSoft17() {
    	return true;
    }
    
    public int minBet() {
    	return 50;
    }
    
    public int maxBet() {
    	return 2500;
    }
    
    public int betStep() {
    	return 50;
    }
}