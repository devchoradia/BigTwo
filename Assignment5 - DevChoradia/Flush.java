import java.util.Arrays;
/**
 * This class is a subclass of the Hand class, and are used to model a hand of Flush. 
 * 
 * @author Dev Choradia
 *
 */
public class Flush extends Hand
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for Flush type hand. Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 */
	public Card getTopCard()
	{
		int [] l = {this.getCard(0).getRank(), this.getCard(1).getRank(), this.getCard(2).getRank(), this.getCard(3).getRank(), this.getCard(4).getRank()}; // store the ranks of each card inside an array
		int j = 0;
		
		for(int i = 0; i < 5;i++)
		{
			if(l[i] == 0)
				l[i] = 13;
			if(l[i] == 1)
				l[i] = 14;
		}
		
		Arrays.sort(l);
		
		
		for(int i = 0; i < l.length;i++)
			if(this.getCard(i).getRank() == l[4])
				j = i;
		
		return this.getCard(j);
	}
	
	/**
	 * Checks whether the hand is a flush.
	 * 
	 */
	public boolean isValid() 
	{
		
		
		if(this.size()!=5)
		return false;
		boolean f = true;
		int s = this.getCard(0).getSuit();
		for(int i=1;i<this.size();i++) 
		{
			if(s != this.getCard(i).getSuit()) 
			{
				f = false;
				break;
			}
		}
		return f;
	}
	
	/**
	 * Returns type of string.
	 * 
	 */
	public String getType() 
	{
		return "Flush";	
	}
	
	

}