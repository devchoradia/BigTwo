/**
 * This class is a subclass of the Hand class, and are used to model a hand of Pair. 
 * 
 * @author Dev Choradia
 *
 */
public class Pair extends Hand
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for the Pair type hand. 
	 * Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Pair(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	
	
	/**
	 * Returns the top card of the hand.
	 * 
	 */
	public Card getTopCard()
	{
		this.sort();
		return this.getCard(1);
		
	}
	
	
	/**
	 * a method for checking if this is a valid pair hand.
	 * 
	 */
	public boolean isValid()
	{
		if(this.size() != 2)
			return false;
		if(this.getCard(0).getRank() == this.getCard(1).getRank())
			return true;
		return false;	
	}
	
	
	/**
	 * 
	 * a method for returning a string specifying the type of this hand.
	 * 
	 */
	public String getType() 
	{
		return "Pair";
	}
	

}