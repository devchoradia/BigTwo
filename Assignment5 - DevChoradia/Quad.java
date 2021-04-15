/**
 * This class is a subclass of the Hand class, and are used to model a hand of Quad. 
 * 
 * @author Dev Choradia
 *
 */
public class Quad extends Hand
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for the Quad type hand. Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Quad(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand
	 * 
	 */
	public Card getTopCard() 
	{
		this.sort();
		if(this.getCard(4).getRank() == this.getCard(1).getRank())
			return this.getCard(4);
		else if(this.getCard(0).getRank() == this.getCard(3).getRank()) 
			return this.getCard(3);
		return null;
	}
	/**
	 * Checks whether the hand is a Quad.
	 * 
	 */
	
	public boolean isValid()
	{
		if(this.size() != 5)
			return false;
		this.sort();
		if(this.getCard(0).getRank() == this.getCard(1).getRank())
		{
			if(this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank())
				return true;
		}
		
		else if(this.getCard(1).getRank() == this.getCard(2).getRank())
		{
			if(this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(2).getRank() == this.getCard(4).getRank())
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns type of string.
	 * 
	 */
	public String getType() 
	{
		return "Quad";	
	}
	
}