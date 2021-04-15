import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface.
 *  It is used to model a Big Two card game that supports 4 players playing over the internet.
 * @author Dev Choradia
 * 
 */
public class BigTwoClient implements CardGame, NetworkGame
{
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID; 
	private String playerName; 
	private String serverIP; 
	private int serverPort; 
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx; 
	private BigTwoTable table; 
	
	private ObjectInputStream ois;

	
	/**
	 *  a constructor for creating a Big Two client.
	 */
	public BigTwoClient()
	{
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++)
			playerList.add(new CardGamePlayer());		
		handsOnTable = new ArrayList<Hand>();
		table = new BigTwoTable(this);
		table.disable();		
		playerName = (String) JOptionPane.showInputDialog("Please enter your name: ");		
		if (playerName == null)		
			playerName = "Default_name";					
		makeConnection();
		table.repaint();
	}
	
	
	/**
	 * Inner Class to handle the server. It implements Runnable
	 *  whose instances are intended to be executed by a thread.
	 * 
	 * @author devchoradia
	 *
	 */
	class server_handler implements Runnable
	{	
		/**
		 * a compulsory method in a class that implements Runnable.
		 */
		public void run() 
		{
			CardGameMessage message = null;
			
			// reads incoming messages from the server	
			try
			{
				while ((message = (CardGameMessage) ois.readObject()) != null)
				{
					parseMessage(message);
					System.out.println("Receiving messages");
				}
			} 		
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}		
			table.repaint();
		}
	}
	

	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 * 
	 * @return an integer playerID (i.e , index) of the local player.
	 */
	public int getPlayerID(){return playerID;}
	
	
	/**
	 * a method for setting the playerID (i.e., index) of the local player. 
	 * 
	 * @param playerID an integer which holds the ID.
	 */
	public void setPlayerID(int playerID){this.playerID = playerID;}
	
	
	/**
	 * a method for getting the name of the local player.
	 * 
	 * @return playerName returning name of the player
	 */
	public String getPlayerName(){return playerName;}
	
	
	/**
	 * a method for setting the name of the local player.
	 * 
	 * @param a string to set Player name.
	 */
	public void setPlayerName(String playerName){playerList.get(playerID).setName(playerName);}

	
	/**
	 * a method for getting the IP address of the game server.
	 * 
	 * @return serverIP which is a string
	 */
	public String getServerIP(){return serverIP;}

	
	/**
	 *  a method for setting the IP address of the game server.
	 * 
	 * @param serverIP string is  set.
	 */
	public void setServerIP(String serverIP){this.serverIP = serverIP;}

	
	/**
	 *  a method for getting the TCP port of the game server.
	 * 
	 * @return serverPort is returned.
	 */
	public int getServerPort(){return serverPort;}
	
	
	/**
	 *a method for setting the TCP port of the game server.
	 * 
	 * @param an integer serverPort 
	 */
	public void setServerPort(int serverPort){this.serverPort = serverPort;}

	
	/**
	 * a method for making a socket connection with the game server.
	 * 
	 */
	public void makeConnection()
	{
		serverIP = "127.0.0.1"; serverPort = 2396;
		
		try {sock = new Socket(this.serverIP, this.serverPort);} 		
		catch (Exception e){e.printStackTrace();}

		try
		{
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
		}
		
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		
		Runnable x = new server_handler();
		Thread y = new Thread(x);
		y.start();
		
		sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
		sendMessage(new CardGameMessage(4, -1, null));		
		table.repaint();
	}

	/**
	 * a method for parsing the messages received from the game server. 
	 * This method is called from the thread responsible for receiving messages from the game server.
	 *
	 *@param message is a variable of GameMessage, which is used as a model the message for the network game.
	 */
	public void parseMessage(GameMessage message)
	{
		if(message.getType() == CardGameMessage.START)
		{
			deck = (BigTwoDeck) message.getData();
			start(deck);
			table.printMsg("Game has started !!\n\n");
			table.enable();
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.PLAYER_LIST)
		{
			playerID = message.getPlayerID();
			table.setActivePlayer(playerID);
			
			for (int i = 0; i < 4; i++)
			{
				if (((String[])message.getData())[i] != null)
				{
					this.playerList.get(i).setName(((String[])message.getData())[i]);
					table.setExistence(i);
				}
			}
			
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.FULL)
		{
			playerID = -1;
			table.printMsg("The game is full and cannot be joined!\n");
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.JOIN)
		{
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.setExistence(message.getPlayerID());
			table.repaint();
			table.printMsg("Player " + playerList.get(message.getPlayerID()).getName() + " joined the game!!!\n");
		}
		else if(message.getType() == CardGameMessage.QUIT)
		{
			table.printMsg("Player " + message.getPlayerID() + ", " + playerList.get(message.getPlayerID()).getName() + " left the game.\n");
			playerList.get(message.getPlayerID()).setName("");
			table.setNotExistence(message.getPlayerID());
			if (!this.endOfGame())
			{
				table.disable(); 
				CardGameMessage msg = new CardGameMessage(CardGameMessage.READY, -1, null);
				sendMessage(msg);			
				for (int i = 0; i < 4; i++)	
					playerList.get(i).removeAllCards();						
				table.repaint();
			}			
			table.repaint();
		}	
		else if(message.getType() == CardGameMessage.MOVE)
		{checkMove(message.getPlayerID(), (int[])message.getData());table.repaint();}
		
		else if(message.getType() == CardGameMessage.READY)
		{
			table.printMsg("Player " + message.getPlayerID() + " is ready now!\n");
			handsOnTable = new ArrayList<Hand>();
			table.repaint();
		}	
		else if(message.getType() == CardGameMessage.MSG)
			table.printChatMsg((String)message.getData());	
		else
		{ table.printMsg("Incorrect message type: " + message.getType()); table.repaint(); }
	}

	
	
	/**
	 * a method for sending the specified message to the game server.
	 * This method should be called whenever the client wants to communicate 
	 * with the game server or other clients.
	 *
	 *@param message is a variable of GameMessage, which is used as a model the message for the network game.
	 */
	public void sendMessage(GameMessage message)
	{
		try {oos.writeObject(message);}		
		catch (IOException exception){exception.printStackTrace();}
	}


	/** A getter method to get the number of players.
	 * 
	 * @return an integer that is the number of players.
	 */
	public int getNumOfPlayers() {return numOfPlayers;}

	
	
	/**
	 * A getter method to retrieve the deck of cards being played.
	 * 
	 * @return Returns a deck object containing the deck of cards currently being played.
	 */
	public Deck getDeck(){return this.deck;}

	
		
	/**
	 * A getter method to retrieve the list of players.
	 * 
	 * @return an Arraylist containing the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){return playerList;}
	
	

	/**
	 * A getter method the current cards that have been played on the table by the previous player.
	 * 
	 * @return An ArrayList containing the cards played by the previous player.
	 */
	public ArrayList<Hand> getHandsOnTable(){return handsOnTable;}
	
	
	
	
	/**
	 * A getter method which retrieves the index of the active player.
	 * 
	 * @return an Int type which could either be 0, 1, 2 or 3. 
	 */
	public int getCurrentIdx(){return currentIdx;}
	
	
	
	/**
	 * A method to start to the game with a shuffled deck of cards as given in the BigTwoDeck object deck.
	 * 
	 * @param Deck the BigTwoDeck object supplied as a shuffled deck of cards that is used to play the game. 
	 */
	public void start(Deck deck)
	{		
		this.deck = deck;	
		for (int i = 0; i < 4; i++)
			playerList.get(i).removeAllCards();				
		for (int i = 0; i < 4; i++)		
			for (int j = 0; j < 13; j++)			
				getPlayerList().get(i).addCard(this.deck.getCard(i*13+j));							
		for (int i = 0; i < 4; i++)		
			getPlayerList().get(i).getCardsInHand().sort();				
		for (int i = 0; i < 4; i++)		
			if (playerList.get(i).getCardsInHand().contains(new BigTwoCard(0,2)))
			{ currentIdx = i; break;}							
		table.repaint();
		table.setActivePlayer(playerID);
	}

	
	
	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID the playerID of the player who makes the move.
	 * @param cardIdx the list of the indices of the cards selected by the player.
	 */
	public void makeMove(int playerID, int[] cardIdx) 
	{	
		CardGameMessage message = new CardGameMessage(6, playerID, cardIdx);
		sendMessage(message);
	}

	
	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerID the playerID of the player who makes the move.
	 * @param cardIdx the list of the indices of the cards selected by the player.
	 */
	public void checkMove(int playerID, int[] cardIdx)
	{
		
		int numOfHandsPlayed=handsOnTable.size();	
		if(cardIdx==null)
		{
			if(handsOnTable.isEmpty())			
				table.printMsg("Not a legal move!!!\n");						
			else if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName() == playerList.get(currentIdx).getName())			
				table.printMsg("Not a legal move!!!\n");						
			else
			{
				table.printMsg(playerList.get(currentIdx).getName()+": " + "{pass}\n");				
				if (currentIdx!=3)				
					++currentIdx;								
				else				
					currentIdx=0;
				table.printMsg(this.getPlayerList().get(currentIdx).getName()+" please make a move.\n");
			}
		}		
		else 
		{
			if(handsOnTable.isEmpty())
			{
				CardList selcard =new CardList();  Hand playerHand;	
				int l = cardIdx.length;
				for(int i=0; i< l ; i++)				
					selcard.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));									
				playerHand=composeHand(playerList.get(currentIdx), selcard);				
				if(playerHand==null)				
					table.printMsg("Not a legal move!!!\n");								
				else
				{
					playerHand.sort();					
					if(playerHand.getCard(0).getRank()!=2||playerHand.getCard(0).getSuit()!=0)					
						table.printMsg("Not a legal move!!!\n");										
					else
					{
						table.printMsg(playerList.get(currentIdx).getName()+": "+" {"+playerHand.getType()+"} ");						
						for (int j=0; j<playerHand.size(); j++)						
							table.printMsg(" ["+playerHand.getCard(j).toString()+"] ");												
						table.printMsg("\n");
						playerList.get(currentIdx).removeCards(playerHand);						
						if (currentIdx!=3)
						{
							++currentIdx;
						}
						
						else
						{
							currentIdx=0;
						}						handsOnTable.add(playerHand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+" please make a move.\n");
					}
				}
			}
			
			else
			{
				CardList selcard =new CardList();
				Hand playerHand;
				int l = cardIdx.length;
				for(int i=0 ; i < l; i++)		
					selcard.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));													
				playerHand=composeHand(playerList.get(currentIdx), selcard);				
				if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName())
				{
					if (playerHand==null)					
						table.printMsg("Not a legal move!!!\n");										
					else
					{
						playerHand.sort();
						table.printMsg(playerList.get(currentIdx).getName()+": "+" {"+playerHand.getType()+"} ");					
						for (int j=0; j<playerHand.size(); j++)					
							table.printMsg(" ["+playerHand.getCard(j).toString()+"] ");		
						
						table.printMsg("\n");
						playerList.get(currentIdx).removeCards(playerHand);					
						if (currentIdx!=3)
						{
							++currentIdx;
						}
						
						else
						{
							currentIdx=0;
						}						handsOnTable.add(playerHand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+" please make a move.\n");
					}
				}
				
				else
				{
					if(playerHand!=null)
					{
						if (playerHand.size()==handsOnTable.get(handsOnTable.size()-1).size())
						{
							if (handsOnTable.get(handsOnTable.size()-1).beats(playerHand)==true)					
								table.printMsg("Not a legal move!!!\n");														
							else if(playerHand!=null)
							{
								playerHand.sort();
								table.printMsg(playerList.get(currentIdx).getName()+": "+" {"+playerHand.getType()+"} ");
								
								for (int j=0; j<playerHand.size(); j++)							
									table.printMsg(" ["+playerHand.getCard(j).toString()+"] ");	
								
								table.printMsg("\n");
								playerList.get(currentIdx).removeCards(playerHand);							
								if (currentIdx!=3)
								{
									++currentIdx;
								}
								
								else
								{
									currentIdx=0;
								}								handsOnTable.add(playerHand);
								table.printMsg(this.getPlayerList().get(currentIdx).getName()+" please make a move.\n");
							}
						}
						else						
							table.printMsg("Not a legal move!!!\n");						
					}					
					else					
						table.printMsg("Not a legal move!!!\n");					
				}}} 
		
		
		// checking for end of game
		if(endOfGame()) 
		{
			table.repaint();
			table.printEndGameMsg();
			handsOnTable.clear();
			for (int i=0; i<4; ++i)			
				playerList.get(i).removeAllCards();		
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}	else  {
			playerList.get(playerID).getCardsInHand().sort();
			table.resetSelected();
			if(this.playerID==currentIdx)			
				table.enable();						
			else		
				table.disable();						
			table.repaint();  }}

	
	
	
	/**
	 * a method that checks for the end of the game.
	 * 
	 * @return boolean true if the game ends; false otherwise.
	 */
	public boolean endOfGame() 
	{
		for(int i=0;i<playerList.size();i++)
			if(playerList.get(i).getNumOfCards() == 0) 
				return true;
		return false;
	}
	
		
	
	
	/**
	 * A method to return a valid hand from all the list of cards played by the player. 
	 * 
	 * @param player A CardGamePlayer object which contains the list of players in the game.
	 * @param cards A CardList object which contains list of cards played by the active player.
	 * 
	 * @return the type of hand 
	 */
	public Hand composeHand(CardGamePlayer player, CardList cards)
	{
		StraightFlush straightflush = new StraightFlush(player,cards); // declaring a new straight-flush object
		Triple triple = new Triple(player,cards); // declaring a new triple object 
		Single single = new Single(player,cards); // declaring a new single object
		Pair pair = new Pair(player,cards); // declaring a new pair object
		Quad quad = new Quad(player,cards); // declaring a new quad object
		Flush flush = new Flush(player,cards); // declaring a new flush object 
		Straight straight = new Straight(player,cards); // declaring a new straight object 
		FullHouse fullhouse = new FullHouse(player,cards); // declaring a new full-house object 
		
		if(straightflush.isValid())
			return straightflush; // returns a straight-flush object 
		if(quad.isValid())
			return quad; // returns a quad object 
		if(fullhouse.isValid())
			return fullhouse; // returns a full-house object 
		if(flush.isValid())
			return flush; // returns a flush object
		if(straight.isValid())
			return straight; // returns a straight object 
		if(triple.isValid())
			return triple; // returns a triple object 
		if(pair.isValid())
			return pair; // returns a pair object 
		if(single.isValid())
			return single; // returns a single object 
		
		return new Hand(player,cards); 
	}

	
	
	
	/**
	 * Main helps in creating BigTwoClient object 
	 * 
	 * @param args unused
	 */
	public static void main(String[] args){BigTwoClient client = new BigTwoClient();}
}