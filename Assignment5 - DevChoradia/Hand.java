/** 
 * 
 * The Hand class is a subclass of the CardList class, 
 * and is used to model a hand of cards.
 *  
 * @author Dev Choradia
 *
 */
public class Hand extends CardList
{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Player who plays the hand.
	 */
	private CardGamePlayer player;
	/**
	 * Constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player Player who played the hand.
	 * @param cards list of card that the player played.
	 */
	public Hand(CardGamePlayer player, CardList cards) 
	{
		this.player = new CardGamePlayer();
		this.player = player;
		for(int i=0;i<cards.size();i++)
			this.addCard(cards.getCard(i));
	}

	/**
	 * Getter method for the player who played this hand.
	 * 
	 * @return player of the current hand object.
	 */
	public CardGamePlayer getPlayer() 
	{
		return this.player;
	}
	
	/**
	 * Getter method for the top card of the hand.
	 * 
	 * @return returns the top card of the particular hand formed.
	 */
	public Card getTopCard() 
	{
		return null;
	}
	
	
	
	/**
	 * A boolean method for checking if this hand beats a specified hand.
	 * 
	 * @param hand
	 * @return true if the current hand beats the previous hand else false
	 */
	
	public boolean beats(Hand hand)
	{
		// The List[] is primarily used to compare hands of length 5.
		String[] List = {"Straight", "Flush","FullHouse","Quad", "StraightFlush"};
		/*
		 * When sizes of hand is 1/2/3, its either a single/pair/triple.
		 * their isValid() method is done in their respective classes.
		 */
		if(hand.size() == 1)
		{
			if(this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
				return true;
		}
		else if(hand.size() == 2)
		{
			
			
			if(this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
				return true;
		}
		
		else if(hand.size() == 3)
		{
			
			if(this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1)
				return true;
		}
		/*
		 * When Hand size is 5, we have 5 options :"Straight", "Flush","FullHouse","Quad", "StraightFlush".
		 * Based on whichever ranks higher we must decide which beats what
		 */
		else if(hand.size() == 5 && this.size()==5)
		{
			if(this.getType() == hand.getType())
			{
				if(this.getTopCard().compareTo(hand.getTopCard())==1)
					return true;
				else
					return false;
			}
			else 
			{
				if(this.getType()==List[4]) 
				{
					return true;
				}
				else if(this.getType()==List[3]) 
				{
					if(hand.getType()!=List[4]) 
						return true;
					else 	
						return false;
				}
				else if(this.getType()==List[2]) 
				{
					if(hand.getType()!=List[4] && hand.getType()!=List[3]) 
						return true;
					else 
						return false;
				}
				else if(this.getType()==List[1]) 
				{
					if(hand.getType()!=List[4] && hand.getType()!=List[3] && hand.getType()==List[2]) 
						return true;
					else 	
						return false;					
				}
				else if(this.getType()==List[0]) 
					return false;
			}
	}
	return false;
	}
	
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true if hand is valid, false otherwise
	 */
	public boolean isValid() 
	{
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @returnR the name of the type of hand in string
	 */
	public String getType() 
	{
		return "";
	}
}
