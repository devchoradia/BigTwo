/**
 * This class is a subclass of the Hand class, and are used to model a hand of Triple. 
 * 
 * @author Dev Choradia
 *
 */

public class Triple extends Hand
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the Triple type hand. 
	 * Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 */
	public Card getTopCard() 
	{
		this.sort();
		return this.getCard(2);
	}
	
	/**
	 * a method for checking if this is a valid triple hand.
	 * 
	 */
	public boolean isValid() 
	{
		if(this.size() == 3) 
		{
			if(this.getCard(0).getRank() == this.getCard(1).getRank()) 
			{
				if(this.getCard(1).getRank() == this.getCard(2).getRank()) 
					return true;
				else 
					return false;	
			}
			else 
				return false;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 */
	public String getType()
	{
		return "Triple";	
	}
}