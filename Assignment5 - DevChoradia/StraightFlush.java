import java.util.Arrays;
/**
 * This class is a subclass of the Hand class, and are used to model a hand of Straight Flush. 
 * 
 * @author Dev Choradia
 *
 */
public class StraightFlush extends Hand
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for the StraightFlush type hand.
	 *  Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) 
	{
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 */
	public Card getTopCard() 
	{
		int[] l = new int[5];
		for(int i=0;i<5;i++) 
			if(this.getCard(i).getRank()==0) 
				l[i] = 13;
			else if(this.getCard(i).getRank()==1) 
				l[i] = 14;		
			else 
				l[i] = this.getCard(i).getRank();		
		Arrays.sort(l);
		if(l[4]>=13) 
			l[4]-=13;
		int j = 0;
		for(int i=1;i<5;i++) 	
			if(this.getCard(i).getRank() == l[4])
				j = i;		
		return this.getCard(j);
	}
	
	/**
	 * Checks whether the hand is a Straight Flush.
	 * 
	 */
	public boolean isValid()
	{
		if(this.size() != 5)
			return false;
		int [] l = {this.getCard(0).getRank(), this.getCard(1).getRank(), this.getCard(2).getRank(), this.getCard(3).getRank(), this.getCard(4).getRank()};
		for(int i = 0; i < 5;i++)
		{
			if(l[i] == 0)		
				l[i] = 13;		
			if(l[i] == 1)
				l[i] = 14;
		}
		Arrays.sort(l);
		boolean k = false;
		for (int i = 1; i < l.length; i++) 
		{
			if(this.getCard(0).getSuit() == this.getCard(1).getSuit() && this.getCard(1).getSuit() == this.getCard(2).getSuit() && this.getCard(2).getSuit() == this.getCard(3).getSuit() && this.getCard(3).getSuit() == this.getCard(4).getSuit())
			{
				if (l[i] == l[i-1] + 1) 
					k = true;				
				else				
					return false;				
			}
		}
		
		return k;
	}
	
	
	/**
	 * Returns type of string.
	 * 
	 */
	public String getType() 
	{
		return "StraightFlush";	
	}

}