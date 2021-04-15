/** 
 * 
 * The BigTwoDeck class is a subclass of the Deck class,
 *  and is used to model a deck of cards used in a Big Two card game. 
 * It overrides the initialize() method it inherited from the Deck class
 *  to create a deck of Big Two cards.
 *  
 * @author Dev Choradia
 *
 */
public class BigTwoDeck extends Deck
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * A method for initializing a deck of Big Two cards.
	 * It should remove all cards from the deck,
	 * create 52 Big Two cards and add them to the deck.
	 *   
	 */
	public void initialize()
	{
		int i,j;
		//remove all cards from the deck
		removeAllCards();
		//add cards
		for (i = 0; i < 4; i++) 
		{
			for (j = 0; j < 13; j++) 
			{
				BigTwoCard card = new BigTwoCard(i, j);
				this.addCard(card);
			}
		}
	}
}
