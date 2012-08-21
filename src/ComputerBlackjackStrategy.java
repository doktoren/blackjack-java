// This class plays perfect according to a simplified view on split:
// No resplit is possible. Also, only one of the split hands receives cards.
// The outcome of this hand is copied to the other hand.


import java.util.Hashtable;

public class ComputerBlackjackStrategy implements BlackjackStrategy {
	
	private BlackjackVariation variation;
	private BlackjackCardCounter c;
	
	public ComputerBlackjackStrategy(BlackjackVariation variation, BlackjackCardCounter c)
	throws UnsupportedBlackjackVariationException {
		if (variation.getClass().getCanonicalName().compareTo("AarhusCasinoVariation") != 0)
			throw new UnsupportedBlackjackVariationException(this, variation);
		this.variation = variation;
		this.c = c;
	}
	
	public int bet(int capitalLeft) {
		// Todo!
		return 50;
	}
	
	private boolean allow_split_during_init = true;
	public void init(int upcard, int card1, int card2) {
		dealer_vectors.clear();
		strategies.clear();
		if (print) System.out.println("ComputerBlackjackStrategy.init(c, " + to_char[upcard]
		                                                                             + ", [" + to_char[card1] + " " + to_char[card2] + "]) :");
		
		first_player_card = card1;
		cards_out = primes[card1] * primes[card2];
		
		// Construct a deck according to the information c
		BlackjackDecks decks = new BlackjackDecks(c);
		
		
		double split_value = -5.0;
		if (allow_split_during_init  &&  card1 == card2) {
			// Split possible.
			if (print) System.out.println("    considering split:");
			
			split_value = 0.0;
			boolean soft = card1 == 1;
			int player_total = soft ? 11 : card1;
			
			// Try all possibilities of next card
			for (int i=1; i<=10; i++) if (decks.cardsLeft(i) != 0) {
				
				double f = 1.0*decks.cardsLeft(i)/decks.cardsLeft();
				
				int new_total = player_total + i;
				boolean new_soft = soft;
				if (i==1   &&  new_total<12) {
					new_soft = true;
					new_total += 10;
				}
				
				// No bust
				if (card1 == 1) {
					// Resplit is ignored by this class and split aces only receives 1 extra card.
					decks.removeCard(i);
					DealerVector dv = lookupDealerVector(decks, primes[i],
							upcard == 1 ? 11 : upcard, upcard == 1, 1);
					decks.putBackCard(i);
					split_value += calcBalance(dv, player_total);
					
				} else {
					
					decks.removeCard(i);
					Strategy s = lookupStrategy(decks, primes[i], upcard, new_total, new_soft, false);
					decks.putBackCard(i);
					split_value += f*s.getBalance();
				}
			}
			
			// Assume (incorrectly) that playing the hand twice has twice expectation.
			split_value += split_value;
			
			dealer_vectors.clear();
			strategies.clear();
		}
		
		int player_total = card1 + card2;
		boolean soft = card1 == 1  ||  card2 == 1;
		if (soft) player_total += 10;
		
		if (print) System.out.println("    considering non-split:");
		double non_split_value = lookupStrategy(decks, 1, upcard, player_total, soft, true).getBalance();
		
		do_split = split_value > non_split_value;
		
		balance = non_split_value > split_value ? non_split_value : split_value;
		if (Blackjack.print) System.out.println("(balance = " + balance + ")");
		
		recalculate = false;
	}
	
	public void clear() {
		dealer_vectors.clear();
		strategies.clear();
	}
	
	private boolean do_split;
	public boolean split(int upcard, int pair) {
		if (do_split) recalculate = true;
		return do_split;
	}
	
	private double balance;
	public boolean surrender(int upcard,
			int total, boolean soft) {
		boolean result = balance < -0.5;
		
		if (result) recalculate = true;
		return result;
	}
	
	public boolean doubleUp(int upcard,
			int total, boolean soft) {
		boolean result = findStrategy(upcard, total, soft).doubleUp();
		
		if (result) recalculate = true;
		return result;
	}
	
	public boolean hit(int upcard, int total, boolean soft) {
		boolean result = findStrategy(upcard, total, soft).doHit();
		
		if (!result) recalculate = true;
		return result;
	}
	
	public boolean insure(int total, boolean soft) {
		return 3*c.getCardsLeft(10) > c.getCardsLeft();
	}
	
	// ########################################################
	// ############       IMPLEMENTATION        ###############
	// ########################################################
	
	
	private boolean print = true;
	private boolean p_all = true;
	
	private String cardsOutToString(int cards_out) {
		// May look forever if cards_out contains prime > 29
		String result = "";
		while (cards_out > 1) {
			for (int i=1; i<=10; i++) {
				if ((cards_out % primes[i]) == 0) {
					if (result != "") result += " ";
					result += (result == "" ? "" : " ") + to_char[i];
					cards_out /= primes[i];
					break;
				}
			}
		}
		return result;
	}
	
	
	private int cards_out;
	private int first_player_card;
	private boolean recalculate;
	private Strategy findStrategy(int upcard, int total, boolean soft) {
		if (print) System.out.println("    findStrategy(c, " + to_char[upcard] +
				", " + total + ", " + soft + ")");
		
		if (recalculate) {
			if (print) System.out.println("        RECALCULATE!!!");
			// This part should only be reached when a pair is splitted!
			allow_split_during_init = false;
			int card2 = total - first_player_card - (soft ? 10 : 0);
			init(upcard, first_player_card, card2);
			allow_split_during_init = true;
			
			cards_out = primes[first_player_card] * primes[card2];
			
			recalculate = false;
		}
		
		if (!strategies.containsKey(new Integer(cards_out))) {
			System.out.println("Argh");
		}
		
		return strategies.get(new Integer(cards_out));
	}
	
	
	
	// cards_out is a product of primes[cards]
	// the upcard and the first player card is not used in cards_out.
	private static int[] primes = {0, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
	private static int[] bj_prime = {1, 31};
	private static char[] to_char = {'#', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'X'};
	
	private Hashtable<Integer, DealerVector> dealer_vectors = new Hashtable<Integer, DealerVector>();
	
	private DealerVector lookupDealerVector(BlackjackDecks decks, int cards_out,
			int dealer_total, boolean soft, int bj_possible) {
		if (dealer_total > 16) return new DealerVector(dealer_total);
		
		if (dealer_vectors.containsKey(new Integer(cards_out * bj_prime[bj_possible])))
			return dealer_vectors.get(new Integer(cards_out * bj_prime[bj_possible]));
		
		DealerVector dv = new DealerVector(decks, cards_out, dealer_total, soft, bj_possible);
		dealer_vectors.put(new Integer(cards_out * bj_prime[bj_possible]), dv);
		return dv;
	}
	
	// Assume dealer black jack resolved first.
	private class DealerVector {
		
		// May not be called with dealer_total > 16
		public DealerVector(BlackjackDecks decks, int cards_out, int dealer_total,
				boolean soft, int bj_possible) {
			if (p_all  &&  dealer_total > 16)
				System.out.println("ERROR: dealerVector called with dealer_total = " + dealer_total);
			
			hv = cards_out;
			
			// Try all possibilities of next card
			for (int i=1; i<=10; i++) if (decks.cardsLeft(i) != 0) {
				
				double f = (1.0*decks.cardsLeft(i))/decks.cardsLeft();
				
				int new_total = dealer_total + i;
				boolean new_soft = soft;
				if (i==1   &&  new_total<12) {
					new_soft = true;
					new_total += 10;
				} else if (new_soft  &&  new_total > 21) {
					new_soft = false;
					new_total -= 10;
				}
				
				if (new_total > 21) {
					// Busted
					value[0] += f;
				} else {
					// No bust
					if (dealer_total > 16) {
						// Stand
						
						if (bj_possible != 0) {
							// BLACKJACK!
							value[22-16] += f;
						} else {
							value[dealer_total-16] += f;
						}
					} else {
						// Hit
						decks.removeCard(i);
						DealerVector dv = lookupDealerVector(decks, primes[i]*cards_out, new_total, new_soft, 0);
						decks.putBackCard(i);
						for (int j=0; j<7; j++)
							value[j] += f*dv.value[j];
					}
				}
			}
		}
		
		// If dealer_total > 16. To do: remove this method and optimize elsewhere.
		public DealerVector(int dealer_total) {
			if (dealer_total > 22) System.out.println("ARGH ARGAShDLKHD");
			
			value[dealer_total - 16] = 1.0;
		}
		
		public boolean initialized() { return hv != 0; }
		
		// value[0] is busted.
		// value[n-16] is value n.
		// sum value[0..6] should be 1
		double[] value = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}; //new double[7];
		
		private int hv = 0;
		
		public int hashCode() { return hv; }
	}
	
	// -1 <= balance <= 1
	// May not be called with a busted hand
	// Todo: 3:2 ved blackjack
	private double calcBalance(DealerVector dv, int player_total) {
		if (player_total > 22)
			System.out.println("ERROR: calcBalance called with a busted hand!");
		
		// dealer bust
		double result = -1.0 + 2*dv.value[0];
		
		if (player_total > 16)  result += dv.value[player_total - 16];
		
		for (int i=17; i<player_total; i++)
			result += 2*dv.value[i - 16];
		
		return result;
	}
	
	// key is not based on allow_double_up, but that's not nescessary either.
	private Hashtable<Integer, Strategy> strategies = new Hashtable<Integer, Strategy>();
	
	private Strategy lookupStrategy(BlackjackDecks decks, int cards_out, int upcard,
			int player_total, boolean soft, boolean allow_double_up) {
		if (p_all) System.out.print("\tlookupStrategy : set [" + cardsOutToString(cards_out) + "] exists?");
		if (strategies.containsKey(new Integer(cards_out))) {
			if (p_all) System.out.println(" Yes!");
			return strategies.get(new Integer(cards_out));
		}
		if (p_all) System.out.println(" No!");
		
		Strategy s = new Strategy(decks, cards_out, upcard, player_total, soft, allow_double_up);
		strategies.put(new Integer(cards_out), s);
		return s;
	}
	
	// Always hit on hard 11 and soft 16 (and below)
	// Always stand on hard 18 and soft 19 (and above)
	private class Strategy {
		
		public Strategy(BlackjackDecks decks, int cards_out, int upcard,
				int player_total, boolean soft, boolean allow_double_up) {
			
			// Try to stand?
			if ((!soft  &&  player_total > 11)  ||  player_total > 16) {
				DealerVector dv = lookupDealerVector(decks, cards_out, upcard == 1 ? 11 : upcard,
						upcard == 1, (upcard == 1  ||  upcard == 10) ? 1 : 0);
				double stand_value = calcBalance(dv, player_total);
				if (stand_value > balance) {
					balance = stand_value;
					hit = false;
				}
				
				if (p_all) System.out.println("\t    stand value [" + cardsOutToString(cards_out) + "] = " + stand_value);
			}
			
			// Try to hit?
			if (player_total < 18  ||  (soft  &&  player_total == 18)) {
				
				// Try to double up?
				if (allow_double_up) {
					
					double double_up_value = 0.0;
					
					// Try all possibilities of next card
					for (int i=1; i<=10; i++) if (decks.cardsLeft(i) != 0) {
						
						double f = 1.0*decks.cardsLeft(i)/decks.cardsLeft();
						
						int new_total = player_total + i;
						boolean new_soft = soft;
						if (i==1   &&  new_total<12) {
							new_soft = true;
							new_total += 10;
						} else if (new_soft  &&  new_total > 21) {
							new_soft = false;
							new_total -= 10;
						}
						
						if (new_total > 21) {
							// Busted
							double_up_value -= f;
						} else {
							// Stand
							decks.removeCard(i);
							DealerVector dv = lookupDealerVector(decks, cards_out, upcard == 1 ? 11 : upcard,
									upcard == 1, (upcard == 1 || upcard == 10) ? 1 : 0);
							decks.putBackCard(i);
							double_up_value += f*calcBalance(dv, new_total == 21 ? 22 : new_total);
						}
					}
					
					double_up_value += double_up_value;
					
					if (double_up_value > balance) {
						balance = double_up_value;
						double_up = true;
					}
					
					if (p_all) System.out.println("\t    double up value [" + cardsOutToString(cards_out) + "] = " + double_up_value);
				}
				
				
				// Try normal hit
				double hit_value = 0.0;
				
				// Try all possibilities of next card
				for (int i=1; i<=10; i++) if (decks.cardsLeft(i) != 0) {
					
					double f = 1.0*decks.cardsLeft(i)/decks.cardsLeft();
					
					int new_total = player_total + i;
					boolean new_soft = soft;
					if (i==1   &&  new_total<12) {
						new_soft = true;
						new_total += 10;
					} else if (new_soft  &&  new_total > 21) {
						new_soft = false;
						new_total -= 10;
					}
					
					if (new_total > 21) {
						// Busted
						hit_value -= f;
					} else {
						// No bust
						decks.removeCard(i);
						Strategy s = lookupStrategy(decks, primes[i]*cards_out, upcard, new_total, new_soft, false);
						decks.putBackCard(i);
						hit_value += f*s.getBalance();
					}
				}
				
				if (hit_value > balance) {
					balance = hit_value;
					hit = true;
				}
				
				if (p_all) System.out.println("\t    hit value [" + cardsOutToString(cards_out) + "] = " + hit_value);
			}
		}
		
		private boolean hit = false;
		public boolean doHit() { return hit; }
		
		private double balance = -1.0;
		public double getBalance() { return balance; }
		
		private boolean double_up = false;
		public boolean doubleUp() { return double_up; }
	}
}