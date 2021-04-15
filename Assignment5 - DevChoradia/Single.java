/**
 * This class is a subclass of the Hand class, and are used to model a hand of Single. 
 * 
 * @author Dev Choradia
 *
 */
public class Single extends Hand
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the Single type hand. Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of cards played by the player
	 */	
	public Single(CardGamePlayer player, CardList cards) 
	{
		super(player,cards);
	}
	
	
	/**
	 * 
	 * Returns the top card of the hand.
	 *
	 */
	public Card getTopCard() 
	{
		return this.getCard(0);
	}
	
	
	/**
	 * a method for checking if this is a valid single.
	 * 
	 */
	public boolean isValid() 
	{
		if(this.size()==1) 
			return true;
		return false;
	}
	
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 */
	
	public String getType() 
	{
		return "Single";
	}
	
}
