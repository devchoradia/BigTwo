import java.util.Arrays;
/**
 * This class is a subclass of the Hand class, and are used to model a hand of Straight. 
 * 
 * @author Dev Choradia
 *
 */
public class Straight extends Hand
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for the Straight type hand. Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Straight(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 */
	public Card getTopCard() 
	{
		int [] l = {this.getCard(0).getRank(), this.getCard(1).getRank(), this.getCard(2).getRank(), this.getCard(3).getRank(), this.getCard(4).getRank()};
		int j = 0;
		
		for(int i = 0; i < 5;i++)
		{
			if(l[i] == 0)
				l[i] = 13;
			if(l[i] == 1)
				l[i] = 14;
		}
		Arrays.sort(l);

		for(int i = 0; i < l.length ; i++)
		{
			if(this.getCard(i).getRank() == l[4])
				j = i;
		}
		
		return this.getCard(j);
	}
	
	
	/** 
	 * Checks whether the hand is a valid Straight hand.
	 * 
	 */
	public boolean isValid() 
	{
		if(this.size() == 5) 
		{
			int[] l = {this.getCard(0).getRank(),this.getCard(1).getRank(),this.getCard(2).getRank(),this.getCard(3).getRank(),this.getCard(4).getRank()};
			for(int i =0;i<l.length; i++) 
			{
				if(l[i] == 0) 
				{
					l[i] = 13;
				}
				else if(l[i]==1) 
				{
					l[i] = 14;
				}
			}
			Arrays.sort(l);
			int ct = l[0];
			boolean f = true;
			for(int i=1;i<l.length;i++) 
			{
				if(l[i] == ct+1) 
				{
					ct = l[i];
				}
				else 
				{
					f = false;
					break;
				}
			}
			return f;
			
		}
		return false;
	}
	
	/**
	 * Returns type of string.
	 * 
	 */
	public String getType()
	{
		return "Straight";	
	}

}