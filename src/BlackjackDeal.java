import java.util.concurrent.ArrayBlockingQueue;

public class BlackjackDeal {

	// Returns outcome (bet is 2)
	public int deal(BlackjackDecks decks, BlackjackCardCounter counter,
			BlackjackStrategy strategy) {
		num_splits = 0;
		
		// First draw cards for the dealer. This way the dealer value can
		// be sent to the postSplit method.
		
		// First draw the hidden card
		int dealer_card = decks.drawRandomCard();
		boolean dealer_soft = dealer_card == 1;
		int dealer_total = dealer_soft ? 11 : dealer_card;
		ArrayBlockingQueue<Integer> dealer_hand = new ArrayBlockingQueue<Integer>(8);
		dealer_hand.offer(new Integer(dealer_card));
		
		
		// Now draw the upcard
		dealer_card = decks.drawRandomCard();
		if (dealer_card == 1  &&  dealer_total < 11) {
			dealer_soft = true;
			dealer_total += 11;
		} else {
			dealer_total += dealer_card;
			// Now dealer_total <= 21
		}
		// Do not store upcard in dealer hand.
		
		
		if (Blackjack.print)
			System.out.println("Dealing cards. Upcard: " + Blackjack.M[dealer_card]);
		
		// Upcard is visible
		counter.seenCard(dealer_card);
		
		if (dealer_total == 21) {
			// Dealer Blackjack
			dealer_total = 22; // Blackjack value
			
		} else {
			
			// Blackjack not possible with more than 3 cards
			while (dealer_total < 17) {
				
				int next = decks.drawRandomCard();
				if (next == 1  &&  dealer_total < 11) {
					dealer_soft = true;
					dealer_total += 11;
				} else {
					dealer_total += next;
					// Now dealer_total <= 21
				}
				dealer_hand.offer(next);
				
				if (dealer_soft  &&  dealer_total > 21) {
					dealer_soft = false;
					dealer_total -= 10;
				}
			}
			
			if (dealer_total > 21) dealer_total = 0;
		}
		
		
		// Draw a card for the player
		int player_card = decks.drawRandomCard();
		counter.seenCard(player_card);
		if (Blackjack.print) System.out.print("    Player:  ");
		
		int result = postSplit(decks, counter, strategy, dealer_total, dealer_card, player_card);
		
		if (Blackjack.print) 
			System.out.print("    Dealer:  " + Blackjack.M[dealer_card]);
		
		// Now the dealers cards will be shown
		while (!dealer_hand.isEmpty()) {
			if (Blackjack.print)
				System.out.print(" " + Blackjack.M[dealer_hand.peek()]);
			counter.seenCard(dealer_hand.remove());
		}
		if (Blackjack.print) {
			System.out.println(dealer_total == 0 ? " - busted" :
				(dealer_total == 22 ? " - Blackjack!" : (" - sum " + dealer_total)));
			System.out.print("    Result: " + result + "\n");
		}
		
		return result;
	}
	
	
	private int postSplit(BlackjackDecks decks, BlackjackCardCounter counter,
			BlackjackStrategy strategy, int dealer_hand_value,
			int upcard, int card) {
		
		int card2 = decks.drawRandomCard();
		boolean soft = card == 1  ||  card2 == 1;
		counter.seenCard(card2);
		int total = card + card2 + (soft ? 10 : 0);
		
		// INITIALIZE STRATEGY
		if (num_splits == 0) strategy.init(upcard, card, card2);
		
		// BLACKJACK
		if (total == 21) {
			// Only Blackjack if no split.
			
			if (num_splits == 0) {
				
				if (Blackjack.print) 
					System.out.println(Blackjack.M[card] + " " + Blackjack.M[card2] + " - Blackjack!");
				
				if (dealer_hand_value == 22) return 0;
				return 3; // Blackjack pays 3:2
			} else {
				if (dealer_hand_value < 21) return 2;
				if (dealer_hand_value == 21) return 0;
				return -2;
			}
		}
		
		// SPLIT
		if (card == card2  &&  num_splits<3  &&  strategy.split(upcard, card)) {
			
			if (card == 1) {
				// Cannot resplit aces and only one card per ace!
				
				int ace1card = decks.drawRandomCard();
				int ace2card = decks.drawRandomCard();
				
				if (Blackjack.print) {
					System.out.println("A " + Blackjack.M[ace1card] + " - sum " + (11+ace1card));
					System.out.println("\t    A " + Blackjack.M[ace1card] + " - sum " + (11+ace1card));
				}
				
				int result = 0;
				dealer_hand_value -= 10;
				if (ace1card < dealer_hand_value) result -= 2;
				else if (ace1card > dealer_hand_value) result += 2;
				if (ace2card < dealer_hand_value) result -= 2;
				else if (ace2card > dealer_hand_value) result += 2;
				return result;
			}
			
			
			++num_splits;
			int result = postSplit(decks, counter, strategy, dealer_hand_value, upcard, card);
			
			if (Blackjack.print) System.out.print("\t    ");
			
			return result + postSplit(decks, counter, strategy, dealer_hand_value, upcard, card);
		}
		
		if (Blackjack.print) System.out.print(Blackjack.M[card] + " " + Blackjack.M[card2]);
		
		// SURRENDER  (can't surrender against upcard ace)
		if (upcard != 1  &&  strategy.surrender(upcard, total, soft)) {
			if (Blackjack.print) System.out.println(" - surrender");
			return -1;
		}
		
		// DOUBLE
		if (strategy.doubleUp(upcard, total, soft)) {
			
			int last = decks.drawRandomCard();
			if (last == 1  &&  total < 11) {
				soft = true;
				total += 11;
			} else {
				if ((total += last) > 21  &&  soft) {
					total -= 10;
					soft = false;
				}
			}
			counter.seenCard(last);
			
			if (Blackjack.print) System.out.println(" " + Blackjack.M[last] + " - double");
			
			if (total > 21) {
				// Broke
				return -2*2;
			} else {
				if (total < dealer_hand_value) return -2*2;
				if (total > dealer_hand_value) return 2*2;
				return 0;
			}
		}
		
		// HIT
		while (strategy.hit(upcard, total, soft)) {
			int next = decks.drawRandomCard();
			if (next == 1  &&  total < 11) {
				soft = true;
				total += 11;
			} else {
				if ((total += next) > 21  &&  soft) {
					total -= 10;
					soft = false;
				}
			}
			counter.seenCard(next);
			
			if (Blackjack.print) System.out.print(" " + Blackjack.M[next]);
		}
		
		
		if (Blackjack.print)
			System.out.println(total > 21 ? " - busted" : (" - sum " + total));
		
		// DETERMINE WINNER
		if (total > 21) {
			// Broke
			return -2;
		} else {
			if (total < dealer_hand_value) return -2;
			if (total > dealer_hand_value) return 2;
			return 0;
		}
		
	}
	
	private int num_splits;
}