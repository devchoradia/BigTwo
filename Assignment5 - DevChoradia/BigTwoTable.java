import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author Dev Choradia
 */
public class BigTwoTable implements CardGameTable {
	
	//BigTwoClient variable.
	private BigTwoClient game; 
	
	//A boolean array indicating which cards are being selected.	
	private boolean[] selected;
	
	//An integer specifying the index of the active player.
	private int activePlayer;
		
	//The main window of the application.
	private JFrame frame;
	
	//A panel for showing the cards of each player and the cards played on the table.
	private JPanel bigTwoPanel;
		
	///A “Play” button for the active player to play the selected cards.
	private JButton playButton;
	
	//A “Pass” button for the active player to pass his/her turn to the next player.
	private JButton passButton;
	
	//A text area for showing the current game status as well as end of game messages.
	private JTextArea msgArea;
	
	//Another variable for JTextArea, for the message area.
	private JTextArea mA; 
	
	//A 2D array storing the images for the faces of the cards.
	private Image[][] cardImages;
	
	//An image for the backs of the cards.
	private Image cardBackImage;
	
	//An array storing the images for the avatars.
	private Image[] avatars;
	
	//arr that carries only T/F needed for the program
	private boolean[] arr;
	
	//JTextField variable, representing message box.
	private JTextField msgBox;
	
	
	
	/**
	 * A constructor for creating a BigTwoTable. The parameter game is a reference
	 *  to a card game associates with this table.
	 * 
	 * @param game A Card Game of BigTwoClient type to play through this GUI.
	 */
	public BigTwoTable(BigTwoClient game) 
	{
		this.game = game;
		this.guiSetup();
		frame.setSize(1200, 900);
		frame.setVisible(true);
	}
	
	
	
	
	//A private method to setup the GUI of the application.
	private void guiSetup()
	{
		
		// Loading images
		setActivePlayer(game.getPlayerID()); 
		selected = new boolean[13];
		resetSelected();
		bigTwoPanel = new BigTwoPanel();
	
		cardImages = new Image [4][13];
		avatars = new Image[4];
		avatars[0] = new ImageIcon("src/avatars/superman_128.png").getImage();
		avatars[1] = new ImageIcon("src/avatars/flash_128.png").getImage();
		avatars[2] = new ImageIcon("src/avatars/wonder_woman_128.png").getImage();
		avatars[3] = new ImageIcon("src/avatars/batman_128.png").getImage();		
		cardBackImage = new ImageIcon("src/cards/b.gif").getImage();
		char[] suit = {'d','c','h','s'};		
		char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};		
		cardImages = new Image[4][13];
		for(int j=0;j<13;j++) {
			for(int i=0;i<4;i++) {
				String location = new String();
				location="src/cards/" + rank[j] + suit[i] + ".gif";
				cardImages[i][j] = new ImageIcon(location).getImage();
			}
		}
		
		arr = new boolean[4];
		for (int i = 0; i < 4; i++)
			arr[i] = false;
		
		//initializing the frame
		frame = new JFrame();
		frame.setTitle("Big Two Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.getContentPane().setBackground(new Color(25, 115, 25));
		
		
		// adding left panel.
		final JSplitPane lp = new JSplitPane();
		lp.setDividerLocation(805); lp.setDividerSize(5);
		frame.add(lp, BorderLayout.CENTER);
		
		
		JMenuBar menuBar = new JMenuBar();
		JMenu Game = new JMenu("Game");
		JMenu Message = new JMenu("Message");
		JMenuItem Connect = new JMenuItem("Connect");
		
		
		// Adding action listeners
		Connect.addActionListener(new ConnectMenuItemListener());
		JMenuItem Quit = new JMenuItem("Quit");
		Quit.addActionListener(new QuitMenuItemListener());
		JMenuItem clearInfo = new JMenuItem("Clear Infomation Board");
		clearInfo.addActionListener(new ClearMenuItemListener());
		JMenuItem clearChat = new JMenuItem("Clear Chat Board");
		clearChat.addActionListener(new ClearChatMenuItemListener());
		
		
		Game.add(Connect);   Game.add(Quit);
		Message.add(clearInfo);   Message.add(clearChat);
		menuBar.add(Game);   menuBar.add(Message);
		frame.setJMenuBar(menuBar);
		
		
		msgArea = new JTextArea(100, 30);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea.append("Message Board\n");
		msgArea.setEditable(false);
		
		JScrollPane scp1 = new JScrollPane(msgArea);   
		msgArea.setLineWrap(true); 
		scp1.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);    
		
		
		// making a panel for game messages and chat
		JPanel message = new JPanel();
		message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
		
		//making a game message area
		mA = new JTextArea(100, 30);
		DefaultCaret caret1 = (DefaultCaret)mA.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		mA.append("This is the chat area!\n\n");
		mA.setEditable(false);
		
		JScrollPane scp2 = new JScrollPane(mA);   
		mA.setLineWrap(true); 
		scp2.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel label = new JLabel("Message: ");
		msgBox = new MyTextField(30);
		msgBox.setMinimumSize(new Dimension(30, 10));
		
		
		
		JPanel mi = new JPanel();  mi.setLayout(new FlowLayout(FlowLayout.LEFT));
		mi.add(label);  mi.add(msgBox);
		
		
		message.add(scp1);
		message.add(scp2);
		message.add(mi);
		
		lp.setRightComponent(message);
		lp.getRightComponent().setMinimumSize(new Dimension(100, 60));
		
		JPanel butnArea = new JPanel();
		butnArea.setLayout(new FlowLayout());
		playButton = new JButton(" Play "); playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton(" Pass "); passButton.addActionListener(new PassButtonListener());
		butnArea.add(playButton); butnArea.add(passButton);
	    butnArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); butnArea.setSize(890, 30);
		
		if (game.getCurrentIdx() != activePlayer){butnArea.setEnabled(false);playButton.setEnabled(false);passButton.setEnabled(false);}
		else{butnArea.setEnabled(true);playButton.setEnabled(true);passButton.setEnabled(true);} 
		
		//rp is the right panel.
		final JSplitPane rp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rp.setDividerSize(5); rp.setDividerLocation(800);;
		rp.setBottomComponent(butnArea);rp.setTopComponent(bigTwoPanel);
		rp.setBackground(Color.green.darker().darker().darker());
		rp.getBottomComponent().setMinimumSize(new Dimension(800, 35));
		lp.setLeftComponent(rp);
	}
	
	
	
	
	/**
	 * A setter method that sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {this.activePlayer = activePlayer;}

	
	
	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected by the user.
	 */
	public int[] getSelected() 
	{
		int ct = 0;
		for(int i = 0; i < 13;i++)
			if(selected[i] == true)
				ct++;
		
		int [] u = null;
		int ctr = 0;
		
		if(ct != 0)
		{	
			u = new int[ct];
			for(int i = 0; i < 13;i++)
				if(selected[i] == true)
					{u[ctr] = i; ctr++;}		
		}
			return u;
	}
	

	
	/**
	 * A method that resets the list of selected cards to an empty list.
	 */
	public void resetSelected() 
	{		
		for(int i = 0; i < 13;i++)		
			selected[i] = false;				
	}

	
	
	/**
	 * A method that repaints the GUI.
	 */
	public void repaint() {resetSelected(); frame.repaint();}
	
	
	
	/**
	 * A method that prints the specified string to the message area of the card game table.
	 * 
	 * @param msg the string to be printed to the message area of the card game table.
	 */
	public void printMsg(String msg) {msgArea.append(msg+"\n");}
	
	
	
	/**
	 * A method that prints the message for end of game.
	 */
	public void printEndGameMsg()
	{
		String endOfGame="";
		
		for(int i=0; i<game.getNumOfPlayers(); ++i)
		{
			endOfGame+=game.getPlayerList().get(i).getName()+": ";
			
			if(game.getPlayerList().get(i).getNumOfCards()!=0)
			{
				for(int j=0; j<game.getPlayerList().get(i).getNumOfCards(); ++j)			
					endOfGame+=" ["+game.getPlayerList().get(i).getCardsInHand().getCard(j).toString()+"] ";				
				endOfGame+="\n";
			}		
			else		
				endOfGame+=" wins\n";		
		}
		JOptionPane.showMessageDialog(null, "GameOver!!\n"+endOfGame);
	}
	
	
	
	/**
	 * A method that prints the chat message.
	 * 
	 * @param msg A string to be appended in the chat area.
	 */
	public void printChatMsg(String msg) {	mA.append(msg+"\n"); }

	
	
	/**
	 * A method that clears the message area of the card game table.
	 */
	public void clearMsgArea() {msgArea.setText(null);}

	
	
	/**
	 * A method that clears the chat message area.
	 */
	public void clearChatMsgArea() {this.mA.setText("");}
	
	
	
	/**
	 *  indeces of arr are set to true.
	 * 
	 * @param playerID an integer which is the index of instance variable arr
	 */
	public void setExistence(int playerID){arr[playerID] = true;}
	
	
	/**
	 *  indeces of arr are set to false.
	 * 
	 * @param playerID an integer which is the index of instance variable arr
	 */
	public void setNotExistence(int playerID){arr[playerID] = false;}

	
	
	
	/**
	 * A method that resets the GUI.
	 */
	public void reset() {this.resetSelected();this.clearMsgArea();this.enable();}
	
	
	
	
	/**
	 * A method that enables user interactions.
	 */
	public void enable() {playButton.setEnabled(true);passButton.setEnabled(true);bigTwoPanel.setEnabled(true);}
	
	
	
	
	/**
	 * A method that disables user interactions.
	 */
	public void disable() {playButton.setEnabled(false);passButton.setEnabled(false);bigTwoPanel.setEnabled(false);}
	
	
	
	/**
	 * A method to quit.
	 */
	public void quit() {System.exit(0);}
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface. 
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. 
	 * Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * 
	 * @author Dev Choradia
	 */
	class BigTwoPanel extends JPanel implements MouseListener 
	{
			
		private static final long serialVersionUID = 1L;
		
		/**
		 * BigTwoPanel default constructor which adds the Mouse Listener
		 *  and sets background of the card table.
		 *  
		 */
		public BigTwoPanel() {this.addMouseListener(this);}
		
		
		//A private method to get the suit of a particular card of a particular player
		private int getSuitofPlayer(int Player, int Suit) {return game.getPlayerList().get(Player).getCardsInHand().getCard(Suit).getSuit();}
				
				
				
		//A private method to get the rank of a particular card of a particular player
		private int getRankofPlayer(int Player, int Rank) {return game.getPlayerList().get(Player).getCardsInHand().getCard(Rank).getRank();}
				
		

		/**
		 * Draws the avatars, text and cards on card table.
		 * 
		 * @param g Provided by system to allow drawing.
		 */
		public void paintComponent(Graphics g) 
		{
			int x = game.getPlayerList().get(0).getNumOfCards();
			int y = game.getPlayerList().get(1).getNumOfCards();
			int z = game.getPlayerList().get(2).getNumOfCards();
			int f = game.getPlayerList().get(3).getNumOfCards();
			int s = game.getHandsOnTable().size();
			
			//this.setBackground(Color.green.darker().darker().darker());
			
			this.setOpaque(true);
			Graphics2D g2 = (Graphics2D) g;
	        if (arr[0]) 
	        {
		        if (game.getCurrentIdx() == 0 && x != 0)		        
		        		g.setColor(Color.BLUE);		        		        		
		        if (activePlayer == 0)		        
		        		g.drawString("You", 10, 20);		        		        		
		        else		        
		        		g.drawString(game.getPlayerList().get(0).getName(), 10, 20);		        		        
		        g.setColor(Color.BLACK);
				g.drawImage(avatars[0], 10, 20, this);
			}

	        g2.drawLine(0, 160, 1600, 160);    
	        if (activePlayer == 0) 
	        {
	        	
	        	
		        	for (int i = 0; i < x; i++) 
		        	{
		        		int suit = getSuitofPlayer(0, i);
			    		int rank = getRankofPlayer(0, i);
			    		
			    		if (!selected[i])		    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 43, this);			    					    			
			    		else			    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 23, this);			    					    			
			    	}
	        }     
	        else
	        {
	        	
	        		for (int i = 0; i <x; i++)	        		
	        			g.drawImage(cardBackImage, 155 + 40*i, 43, this);	        		
		    }
		    if (arr[1])
		    {
			    if (game.getCurrentIdx() == 1 && y != 0)			    
			    		g.setColor(Color.BLUE);			    			    		
		        	if (activePlayer == 1)		        	
		        		g.drawString("You", 10, 180);		        			        		
		        else		        
		        		g.drawString(game.getPlayerList().get(1).getName(), 10, 180);		        		        	
		        	g.setColor(Color.BLACK);
        			g.drawImage(avatars[1], 10, 180, this);		        		
			} 
		    g2.drawLine(0, 320, 1600, 320);	    
		    if (activePlayer == 1) 
		    {
			    	for (int i = 0; i < y; i++)
			    	{
			    		int suit = getSuitofPlayer(1, i);
			    		int rank = getRankofPlayer(1, i);
			    		if (!selected[i])			    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 203, this);			    					    		
			    		else			    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 183, this);			    		
			    	}
		    } 
		    else
		    {
		    		for (int i = 0; i < y; i++) 		    		
		    			g.drawImage(cardBackImage, 155 + 40*i, 203, this);		    		
		    }  
		    if (arr[2])
		    {
			    if (game.getCurrentIdx() == 2 && z != 0)			    
			       	g.setColor(Color.BLUE);			    		 
		        if (activePlayer == 2)		        
		        		g.drawString("You", 10, 340);		        		        
		        else		        
		        		g.drawString(game.getPlayerList().get(2).getName(), 10, 340);		        		        
		        g.setColor(Color.BLACK);
			    g.drawImage(avatars[2], 10, 340, this);
			}
		    g2.drawLine(0, 480, 1600, 480); 
		    if (activePlayer == 2) 
		    {
			    	for (int i = 0; i < z; i++) 
			    	{
			    		int suit = getSuitofPlayer(2, i);
			    		int rank = getRankofPlayer(2, i);
			    		if (!selected[i])			    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 363, this);	    		
			    		else			    		
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 343, this);		    		
			    	}
		    }    
		    else	
			    	for (int i = 0; i < z; i++)	    	
			    		g.drawImage(cardBackImage, 155 + 40*i, 363, this);	    		    
		    if (arr[3])
		    {
			    if (game.getCurrentIdx() == 3 && f != 0)			    
			    		g.setColor(Color.BLUE);			   		        	
		        if (activePlayer == 3)		        
		          	g.drawString("You", 10, 500);	        		      
		        else		        
		        		g.drawString(game.getPlayerList().get(3).getName(), 10, 500);		        		        
		        g.setColor(Color.BLACK);
			    g.drawImage(avatars[3], 10, 500, this);		        	
			}    
		    g2.drawLine(0, 640, 1600, 640);   
		    if (activePlayer == 3)
		    {
			    	for (int i = 0; i < f; i++)
			    	{
			    		int suit = getSuitofPlayer(3, i);
			    		int rank = getRankofPlayer(3, i);
			    		if (!selected[i])			    	
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 523, this);		    					    		
			    		else			   	
			    			g.drawImage(cardImages[suit][rank], 155+40*i, 503, this);			    		
			    	}
		    } 		    
		    else
		    {
			    	for (int i = 0; i < f; i++)		  
			    		g.drawImage(cardBackImage, 155 + 40*i, 523, this);		    	
		    }
		    g.drawString("Current Hand on Table", 10, 665);	    
		    if (game.getHandsOnTable().size() == 0)
		     	g.drawString("No Hand on Table.", 10, 690);		   
		    else
		    {
		    		Hand handOnTable = game.getHandsOnTable().get(s-1);
		    		g.drawString("Hand Type:\n ", 10, 720);
			    
		    		if (game.getPlayerList().get(game.getCurrentIdx()) != game.getHandsOnTable().get(s-1).getPlayer()) 
			    	{
			    		g.drawString(game.getHandsOnTable().get(s-1).getPlayer().getName(), 10, 725);			    		
			    		for (int i = 0; i < handOnTable.size(); i++)	    		
			    			g.drawImage(cardImages[handOnTable.getCard(i).getSuit()][handOnTable.getCard(i).getRank()], 160 + 40*i, 700, this);	    							
			    	}
		    }		    
		    g2.drawLine(0, 800, 1600, 800);
		}
		
		
		/**
		 * A method used to catch all the mouse click events and perform events/functions accordingly.
		 * It overrides the MouseClicked method of JPanel.
		 * 
		 * @param e This is a MouseEvent object which has been used to get the coordinates of the mouseClick
		 */
		public void mouseClicked(MouseEvent e) 
		{
			boolean flag = false; 
			int numC = game.getPlayerList().get(activePlayer).getNumOfCards();
			int cc = numC-1;
			
			if (e.getX() >= (155+cc*40) && e.getX() <= (155+cc*40+73)) 
			{
				if(!selected[cc] && e.getY() >= (43 + 160*activePlayer) && e.getY() <= (43 + 160*activePlayer+97))
				{
					selected[cc] = true;
					flag = true;
				} 
				else if (selected[cc] && e.getY() >= (23 + 160*activePlayer) && e.getY() <= (23 + 160*activePlayer+97))
				{
					selected[cc] = false;
					flag = true;
				}
			}
			for (cc = numC-2; cc >= 0 && !flag; cc--) 
			{
				if (e.getX() >= (155+cc*40) && e.getX() <= (155+(cc+1)*40)) 
				{
					if(!selected[cc] && e.getY() >= (43+160*activePlayer) && e.getY() <= (43+160*activePlayer+97))
					{
						selected[cc] = true;
						flag = true;
					} 
					else if (selected[cc] && e.getY() >= (23+160*activePlayer) && e.getY() <= (23+160*activePlayer+97))
					{
						selected[cc] = false;
						flag = true;
					}
				}
				else if (e.getX() >= (155+(cc+1)*40) && e.getX() <= (155+cc*40+73) && e.getY() >= (43+160*activePlayer) && e.getY() <= (43+160*activePlayer+97)) 
				{
					if (selected[cc+1] && !selected[cc]) 
					{
						selected[cc] = true;
						flag = true;
					}
				}
				else if (e.getX() >= (155+(cc+1)*40) && e.getX() <= (155+cc*40+73) && e.getY() >= (23 + 160*activePlayer) && e.getY() <= (23 + 160*activePlayer+97))
				{
					if (!selected[cc+1] && selected[cc])
					{
						selected[cc] = false;
						flag = true;
					}
				}
			}
		
		frame.repaint();
		}
		
		
		
		/**
		 * Mouse pressed method
		 * 
		 * @param e Mouseevent e
		 */
		public void mousePressed(MouseEvent e) {}
		
		
		/**
		 * Mouse pressed method
		 * 
		 * @param e Mouseevent e
		 */
		public void mouseReleased(MouseEvent e) {}
		
		
		/**
		 * Mouse pressed method
		 * 
		 * @param e Mouseevent e
		 */
		public void mouseEntered(MouseEvent e) {}
		
		
		/**
		 * Mouse pressed method
		 * 
		 * @param e Mouseevent e
		 */
		public void mouseExited(MouseEvent e) {}
	}
	
	
	
	/**
	 *  This inner class extends the JTextField
	 *   and implements the actionListener interface for the message field in the game.
	 * 
	 * @author Dev Choradia
	 */
	class MyTextField extends JTextField implements ActionListener
	{

		private static final long serialVersionUID = 1L;

		//  The contructor for this class.		 
		public MyTextField(int i) 
		{
			super(i);
			addActionListener(this);
		}
		
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e) {
			String chatMsg = getText();
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,-1,chatMsg);
			game.sendMessage(message);
			this.setText("");
		}
		
	}
	
	
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener 
	 * interface to handle button-click events for the “Play” button. 
	 * When the “Play” button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * 
	 * @author Dev Choradia
	 */
	class PlayButtonListener implements ActionListener
	{
		/**
		 * The function is overridden from the ActionListener Interface 
		 * and is used to perform the requisite function when the button is clicked.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object
		 */
		public void actionPerformed(ActionEvent event)
		{
			
			
			if (game.getCurrentIdx() == activePlayer)
			{ 
				if (getSelected().length == 0) 
				{	
					int [] cardIdx = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					game.makeMove(activePlayer, cardIdx);
				}
				
				else		
					game.makeMove(activePlayer, getSelected());		
					
				resetSelected();
				repaint();
			}
			else 		
				printMsg("It is not your turn\n");resetSelected();repaint();		
		}
	}
	
	
	
	
	/**
	 * This inner class implements the ActionListener interface and is used to detect
	 *  the clicks on the passButton makeMove function based on the click.
	 *
	 * @author Dev Choradia
	 **/
	class PassButtonListener implements ActionListener
	 	{
		/**
		 * The function is overridden from the ActionListener Interface 
		 * and is used to perform the requisite function when the button is clicked.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx() == activePlayer)
			{ 
				int[] cardIdx = null;
				game.makeMove(activePlayer, cardIdx);
				resetSelected();
				repaint();
			} 
			
			else 
			{	printMsg("It is not your turn!\n");	resetSelected();	repaint(); }
		}
	}
	
	
	
	/**
	 * This inner class implements the actionListener interface for the Restart Menu Item in the JMenuBar to restart the game on click.
	 * 
	 * @author Dev Choradia
	 */
	class ConnectMenuItemListener implements ActionListener
	{
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e) 
		{
			if (game.getPlayerID() == -1) 	
				game.makeConnection();						
			else if (game.getPlayerID() >= 0 && game.getPlayerID() <= 3)			
				printMsg("Connection already established!\n");							
		}
	}
	
	
	
	/**
	 * This inner class implements the actionListener interface for 
	 * the Quit Menu Item in the JMenuBar to quit the game on click. 
	 * 
	 * @author Dev Choradia
	 */
	class QuitMenuItemListener implements ActionListener
	{

		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 *  @param e This is a ActionEvent object to detect if some user interaction was performed on the given object
		 */
		public void actionPerformed(ActionEvent e) {System.exit(0);}	
	}
	
	
	
	/**
	 * This inner class implements the actionListener interface for the 
	 * Clear  Board item in the JMenuBar to quit the game on click. 
	 * 
	 * @author Dev Choradia
	 */
	class ClearMenuItemListener implements ActionListener
	{	
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e){clearMsgArea();}
	}
	
	
	
	/**
	 * This inner class implements the actionListener interface for the Clear
	 *  Chat Board item in the JMenuBar to quit the game on click
	 * 
	 * @author Dev Choradia
	 */
	class ClearChatMenuItemListener implements ActionListener
	{
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		public void actionPerformed(ActionEvent e){clearChatMsgArea();}
	}

}
