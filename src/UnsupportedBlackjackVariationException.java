
public class UnsupportedBlackjackVariationException extends Exception {
	
	static final long serialVersionUID = 0x3231879123649874L; 
	
	private String errorMessage;
	
	public UnsupportedBlackjackVariationException(BlackjackStrategy s, BlackjackVariation v) {
		errorMessage = "The strategy " + s.getClass().getCanonicalName() + " can't play the variation " +
			v.getClass().getCanonicalName() + ".";
	}
	
	public String toString() {
		return errorMessage; 
	}
}
