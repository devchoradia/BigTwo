/**
 * 
 * The BigTwoCard class is a subclass of the Card class, and is used to
 * model a card used in a Big Two card game.
 * It overrides the compareTo() method it inherited from the Card class
 *  to reflect the ordering of cards used in a Big Two card game. 
 * 
 * @author Dev Choradia
 *
 */
public class BigTwoCard extends Card 

{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * a constructor for building a card with the specified suit and rank
	 * 
	 * @param suit
	 *            an int value between 0 and 3 representing the suit of a card where:
	 *             <p>
	 *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 *            
	 * @param rank
	 *            an int value between 0 and 12 representing the rank of a card where:
	 *             <p>
	 *            0 = 'A', 1 = '2', 2 = '3', ... , 9 = '10', 10 = 'J', 11= 'Q', 12 = 'K'
	 */

	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	
	
	/**
	 * 
	 * Overriding method for comparing the order of this card with the specified card.
	 * Used for comparing the order of this card with the specified card.
	 * 
	 * @param card The card with which user wants to compare. 
	 * 
	 */
	public int compareTo(Card card)
	{
		int a = card.rank;
		int b = this.rank;
		
		//Since A and 2 hold a higher rank thank K in the game.
		if(b == 0) 
			b = 13;
		else if(b == 1) 
			b = 14;
		if(a == 0) 
			a = 13;
		else if(a == 1) 
			a = 14;
		
		// Returns -1/0/1 based on requirement.
		if(b < a)
			return -1;
		else if(b > a) 
			return 1;
		else if(b==a) 
		{
			if(this.getSuit() > card.getSuit()) 
				return 1;
			else if (this.getSuit() < card.getSuit()) 
				return -1;
		}
		return 0;
	}
}
