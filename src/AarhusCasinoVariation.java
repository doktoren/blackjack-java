public class AarhusCasinoVariation extends BlackjackVariation {

  @Override
  public int numberOfDecks() {
    return 6;
  }

  @Override
  public int deckPenetration() {
    return 60;
  }

  @Override
  public int maxSplits() {
    return 3;
  }

  @Override
  public boolean doubleAllowed(int cardSum) {
    return true;
  }

  @Override
  public boolean doubleAfterSplitAllowed() {
    return true;
  }

  @Override
  public boolean dealerStandsOnSoft17() {
    return true;
  }

  @Override
  public int minBet() {
    return 50;
  }

  @Override
  public int maxBet() {
    return 2500;
  }

  @Override
  public int betStep() {
    return 50;
  }
}