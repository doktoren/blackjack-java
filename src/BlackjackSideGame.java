// Surrender(?) and insure can be regarded as sidegames
// Perfect pairs
// Sevens
// Prague bonus

public interface BlackjackSideGame {
	
	public void init();
	
	public void dealPlayerCard(int card);
	
	public void dealDealerCard(int card);

	public boolean hasResult();
	
	public int getResult();
	
}
