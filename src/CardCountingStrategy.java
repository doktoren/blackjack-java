/**
 * Adding card counting to the static strategy raises the payback to around 100.5%
 */
public class CardCountingStrategy extends StaticBlackjackStrategy {

  public CardCountingStrategy(BlackjackVariation variation)
      throws UnsupportedBlackjackVariationException {
    super(variation);
  }

  public int bet(int capitalLeft, BlackjackCardCounter cardCounter) {
    if (35 * cardCounter.getBalance() >= cardCounter.getCardsLeft()) {
      return variation.maxBet();
    } else {
      return variation.minBet();
    }
  }

}
