/**
 * This class is a subclass of the Hand class, and are used to model a hand of Full House. 
 * 
 * @author Dev Choradia
 *
 */
public class FullHouse extends Hand
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for FullHouse type hand. Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public FullHouse(CardGamePlayer player, CardList cards) 
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
		if(this.getCard(2).getRank() == this.getCard(4).getRank()) 
			return this.getCard(4);
		else if(this.getCard(0).getRank()== this.getCard(2).getRank()) 
			return this.getCard(2);
		return null;
		
	}
	
	/**
	 * 
	 * Checks if the hand is a Flush.
	 * 
	 */
	public boolean isValid() 
	{
		if(this.size() != 5) 
		return false;
		this.sort();
		if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank() == this.getCard(2).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank()))
			return true;
		else if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(2).getRank() == this.getCard(3).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())) 
			return true;
		else 
			return false;
	}
	
	/**
	 * Returns FullHouse (string)
	 * 
	 */
	public String getType()
	{
		return "FullHouse";	
	}

}