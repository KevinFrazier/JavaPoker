/**
 * Chris Sy
 * Blank lines and comments = 132
 * Total lines = 605
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PokerGame extends JPanel
{
	private final int HAND_SIZE = 5, MAX_EXCHANGES_ALLOWED = 3;
	private JPanel northPanel,
	               southPanel,
	               centerPanel,
	               cardPilesPanel,
	               controlsPanel,
	               playerCardsPanel;
	private JPanel[] aiCardPanels;	               
	private JButton discardPlayBtn,
					foldBtn,					
					dealBtn;
	private JButton[] playerCards;
	private JLabel deckLabel,
				   discardLabel,
				   msgLabel;
	private JLabel[] aiCards; 	               
	private Hand playerHand,
	             aiHand;
	private Deck deck;
	private BufferedImage backImage;
	private BufferedImage[][] cardImages;
	private Stack<Card> discardPile;
	private boolean roundResolved;
	private boolean[] whichCardSelected;
	private ArrayList<Card> cardsToBeRemoved;
	private int numCardsSelected;
	
	/**
	 * Default constructor
	 */
	public PokerGame()
	{		
		deck = new Deck();
		discardPile = new Stack<>();
		playerHand = new Hand();
		aiHand = new Hand();
		cardsToBeRemoved = new ArrayList<>();
		numCardsSelected = 0;		
		initializeBooleanStates();
		readImageFiles();
		createButtons();	
		createLabels();		
		renderCards(false);
		createPanels();
		wireBtns();		
		setMsgLabel("");
	}	
	
	/**
	 * Reads all image files and stores them in 2D array
	 */
	private void readImageFiles() 
	{
		cardImages = new BufferedImage[13][4];
		
		try
		{
			for(int i = 0; i < 13; ++i)  //stores all images in a [rank][suit] structure in the 2D array
			{
				for(int j = 0; j < 4; ++j)
				{					
					String fileName = Card.whatRankString(i) + "_of_" + Card.whatSuitString(j) + ".png";
					cardImages[i][j] = ImageIO.read(new File(fileName));  //rows = ranks, columns = suits. each of the 13 ranks has 4 suits
				}
			}	
			
			backImage = ImageIO.read(new File("playing-card-back.png"));  
		}
		catch(IOException e)
		{
			System.err.println("Image files not found.");
			System.exit(0);
		}				
	}
	
	/**
	 * Draws all card images on the panel(s)
	 * @param showAIHand true draws the AI's cards face up, false draws
	 *                   AI's cards face down
	 */
	private void renderCards(boolean showAIHand)
	{			
		if(playerHand != null && !playerHand.isEmpty())
		{
			for(int i = 0; i < playerHand.size(); ++i)				
			{
				/*
				 * the correct card image corresponds to the [rank][suit] element of cardImages 2D array
				 * passes the rank/suit of the card to the array indices and sets that image as the icon 
				 */
				playerCards[i].setIcon(new ImageIcon(cardImages[playerHand.getCard(i).getRank()][playerHand.getCard(i).getSuit()].getScaledInstance(80, 90, Image.SCALE_SMOOTH) ) );							
			}	
		}
					
		if(aiHand != null && !aiHand.isEmpty())
		{
			for(int i = 0; i < aiHand.size(); ++i)
			{
				if(showAIHand) //draw the AI's cards face up
				{
					aiCards[i].setIcon(new ImageIcon(cardImages[aiHand.getCard(i).getRank()][aiHand.getCard(i).getSuit()].getScaledInstance(80, 90, Image.SCALE_SMOOTH) ) );
				}
				else //hide AI's hand. draw the back side image for all of the AI's cards
				{
					aiCards[i].setIcon(new ImageIcon(backImage.getScaledInstance(80, 90, Image.SCALE_SMOOTH)));
				}	
			}
		}				
		
		if(!discardPile.isEmpty()) //draw the top card of the discard pile if it's not empty
		{
			discardLabel.setIcon(new ImageIcon(cardImages[discardPile.peek().getRank()][discardPile.peek().getSuit()].getScaledInstance(80, 90, Image.SCALE_SMOOTH) ));
			discardLabel.setVisible(true);
		}
		else
		{
			discardLabel.setVisible(false);
		}
	}
	
	/**
	 * Sets the text for the JLabel located at the bottom of the JPanel
	 * @param newMsg the String to be set
	 */
	private void setMsgLabel(String newMsg)
	{
		msgLabel.setText(newMsg);
	}
	
	/**
	 * Instantiates and configures all the panels used
	 */
	private void createPanels()
	{
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		cardPilesPanel = new JPanel();
		controlsPanel = new JPanel();
		playerCardsPanel = new JPanel();
		aiCardPanels = new JPanel[HAND_SIZE];		
		
		this.setLayout(new GridLayout(3, 1));
		this.add(northPanel);
		this.add(centerPanel);
		this.add(southPanel);
		northPanel.setLayout(new GridLayout(1, 5));
		southPanel.setLayout(new GridLayout(2, 1));
		southPanel.add(playerCardsPanel);
		southPanel.add(msgLabel);
		playerCardsPanel.setLayout(new GridLayout(1, 5));
		cardPilesPanel.setLayout(new GridLayout(1, 2));
		controlsPanel.setLayout(new GridLayout(1, 3));		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(cardPilesPanel, BorderLayout.CENTER);
		centerPanel.add(controlsPanel, BorderLayout.SOUTH);
		
		for(int i = 0; i < HAND_SIZE; ++i)
		{			
			playerCardsPanel.add(playerCards[i]);
			aiCardPanels[i] = new JPanel();
			aiCardPanels[i].add(aiCards[i]);
			northPanel.add(aiCardPanels[i]);
		}		
		
		cardPilesPanel.add(deckLabel);
		cardPilesPanel.add(discardLabel);
		controlsPanel.add(discardPlayBtn);
		controlsPanel.add(foldBtn);		
		controlsPanel.add(dealBtn);		
	}
	
	/**
	 * Instantiates and configures all JButtons
	 */
	private void createButtons()
	{
		discardPlayBtn = new JButton("Play hand");
		foldBtn = new JButton("Fold");
		dealBtn = new JButton("Deal");
		playerCards = new JButton[HAND_SIZE];
		discardPlayBtn.setEnabled(false);
		foldBtn.setEnabled(false);		
		
		for(int i = 0; i < HAND_SIZE; ++i)
		{
			playerCards[i] = new JButton();
			playerCards[i].setOpaque(false);
			playerCards[i].setContentAreaFilled(false);	
			playerCards[i].setBorderPainted(false);
			playerCards[i].setBackground(Color.GRAY);
		}				
	}
	
	/**
	 * Instantiates and configures all JLabels
	 */
	private void createLabels()
	{
		deckLabel = new JLabel();
		discardLabel = new JLabel();
		msgLabel = new JLabel();
		aiCards = new JLabel[HAND_SIZE];
		
		for(int i = 0; i < HAND_SIZE; ++i)
		{
			aiCards[i] = new JLabel();
		}	
		
		deckLabel.setIcon(new ImageIcon(backImage.getScaledInstance(80, 90, Image.SCALE_SMOOTH)));
	}
	
	/**
	 * Initializes all boolean variables
	 */
	private void initializeBooleanStates()
	{
		roundResolved = true;
		whichCardSelected = new boolean[HAND_SIZE];
		
		for(int i = 0; i < HAND_SIZE; ++i)
		{
			whichCardSelected[i] = false;
		}
	}
	
	/**
	 * Resets the playing field and instantiates a fresh deck
	 */
	private void reshuffleCards()
	{
		deck = new Deck();
		playerHand = new Hand();
		aiHand = new Hand();
		discardPile.clear();
	}
	
	/**
	 * Attaches an ActionListener to define the behavior of the deal JButton
	 */
	private void wireDealBtn()
	{		
		dealBtn.addActionListener(new ActionListener()
			    {			
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						if(roundResolved)
						{														
							for(int i = 0; i < HAND_SIZE; ++i)  //deal cards to players one by one, starting with the user
							{
								playerHand.addCard(deck.drawCard());  
								aiHand.addCard(deck.drawCard());
							}
							
							//update states of all relevant components	
							roundResolved = false;  	
							renderCards(false);
							dealBtn.setEnabled(false);
							discardPlayBtn.setText("Play hand");	
							discardPlayBtn.setEnabled(true);
							foldBtn.setEnabled(true);
							enableAllPlayerCards(true);
							performAIBehavior();						
						}
					}			
				 });				
	}
	
	/**
	 * Attaches an ActionListener to define the behavior of the fold JButton
	 */
	private void wireFoldBtn()
	{
		foldBtn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						resolveRound("You folded!");												
					}			
				});
	}
	
	/**
	 * Calls all helper functions to wire all JButtons
	 */
	private void wireBtns()
	{
		wireDealBtn();
		wirePlayerCardBtns();
		wireDiscardBtn();
		wireFoldBtn();
	}
	
	/**
	 * Enables and disables the JButtons that allow the player to select cards from the hand
	 * @param enable true makes all player cards interactable, false disables them
	 */
	private void enableAllPlayerCards(boolean enable)
	{
		for(int i = 0; i < playerCards.length; ++i)
		{
			playerCards[i].setEnabled(enable);			
		}		
	}
	
	/**
	 * Attaches an ActionListener to define the behavior of the discard/play hand JButton
	 */
	private void wireDiscardBtn()
	{
		discardPlayBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!cardsToBeRemoved.isEmpty()) //if user has clicked on and selected any card, button will discard
				{
					for(int i = 0; i < cardsToBeRemoved.size(); ++i) //remove all cards selected from hand
					{
						Card card = cardsToBeRemoved.get(i);
						playerHand.removeCard(card);
						discardPile.push(card);
						playerHand.addCard(deck.drawCard());
					}
					
					resetCardSelections();					
					cardsToBeRemoved.clear();
					renderCards(false); //update visuals but do not reveal AI's hand
					discardPlayBtn.setText("Play hand");					
					aiFoldBehavior();
				}
				else //with no cards are selected, hand will be played against the AI's
				{					
					int winner = playerHand.compareTo(aiHand);
					String winMsg = "";
					
					switch(winner)
					{
						case -1:
							winMsg = "You lose!";
							break;
						
						case 0:
							winMsg = "Tie!";
							break;
							
						default:
							winMsg = "You win!";
					}
					
					resolveRound(winMsg); //resolve the round normally if AI doesnt fold										
				}
			}			
		});
	}
	
	/**
	 * Sets states of all relevant components to a post show down state
	 * @param message String to evoke desired message
	 */
	private void resolveRound(String message)
	{		
		resetCardSelections();
		renderCards(true);
		setMsgLabel(message);
		roundResolved = true;
		cardsToBeRemoved.clear();
		numCardsSelected = 0;									
		dealBtn.setEnabled(true);
		enableAllPlayerCards(false);
		discardPlayBtn.setEnabled(false);
		foldBtn.setEnabled(false);
		reshuffleCards();
	}
	
	/**
	 * Reset visual states of card buttons and all relevant boolean variables
	 */
	private void resetCardSelections()
	{
		for(int i = 0; i < HAND_SIZE; ++i)  
		{
			playerCards[i].setBorderPainted(false);
			playerCards[i].setOpaque(false);
			whichCardSelected[i] = false;
		}
	}
	
	/**
	 * Attaches an ActionListener to all player cards to allow them to be selected
	 */
	private void wirePlayerCardBtns()
	{
		class PlayerCardsListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JButton btn = (JButton)(arg0.getSource());
				
				for(int i = 0; i < HAND_SIZE; ++i)
				{
					if(btn == playerCards[i])
					{
						selectDeselectCard(i);
						break;
					}
				}
			}			
		}
		
		PlayerCardsListener lstnr = new PlayerCardsListener();
		
		for(int i = 0; i < HAND_SIZE; ++i)
		{
			playerCards[i].addActionListener(lstnr);
		}
	}
	
	/**
	 * Defines how the AI chooses to fold
	 * @param compared the hand comparison result (-1, 9, or 1)
	 */
	private boolean aiFoldBehavior()	
	{
		int compared = playerHand.compareTo(aiHand);
		
		if(aiHand.getRank() == 1 && compared == 1) //chance of folding on high card
		{
			if((int)(Math.random() * 100) <= 40) // 40% chance of folding
			{
				resolveRound("Opponent folded!");
				return true;
			}
		}
		
		if(compared == -1) //if AI has losing hand
		{
			if((int)(Math.random() * 100) <= 33) // 1/3 chance of folding
			{
				resolveRound("Opponent folded!");
				return true;
			}
		}		
		
		return false;
	}
	
	/**
	 * Selects or removes selection of cards in the player's hand
	 * @param whichCard the card to be selected
	 */
	private void selectDeselectCard(int whichCard)
	{
		if(whichCardSelected[whichCard])  //undo card selection
		{
			whichCardSelected[whichCard] = false;
			playerCards[whichCard].setBorderPainted(false);
			playerCards[whichCard].setOpaque(false);
			cardsToBeRemoved.remove(playerHand.getCard(whichCard));
			--numCardsSelected;
			
			if(numCardsSelected <= 0)
			{
				discardPlayBtn.setText("Play hand");
			}
		}
		else if(numCardsSelected < MAX_EXCHANGES_ALLOWED) //select card if number of cards selected is less than the max allowed
		{
			whichCardSelected[whichCard] = true;
			playerCards[whichCard].setBorderPainted(true);			
			playerCards[whichCard].setOpaque(true);
			cardsToBeRemoved.add(playerHand.getCard(whichCard));
			++numCardsSelected;
			discardPlayBtn.setText("Exchange");
		}
	}
	
	/**
	 * Executes AI decisions regarding on how to exchange or play its hand
	 */
	private void performAIBehavior()
	{
		switch(aiHand.getRank())
		{
			case 1: //high card. discards 3 lowest ranking cards
				aiHighCardBehavior();  
				break;
			
			case 2: //pair. will attempt to make three of a kind or two pairs by randomly discarding 1-3 off-set cards
				aiSetBehavior(2);
				break;
				
			case 3: //two pairs. will keep the highest ranking set and will randomly risk exchanging cards to make 3 of a kind or full house
				aiSetBehavior(2);
				break;
				
			case 4: //three of a kind. will attempt to make full house by randomly discarding one or both of the off-set cards
				aiSetBehavior(3);
				break;
			
			default:
				setMsgLabel("Opponent did not exchange any cards");						
		}
		
		renderCards(false);
	}
	
	/**
	 * AI discards the 3 lowest ranked cards in hand
	 */
	private void aiHighCardBehavior()
	{		
		for(int i = 0; i < 3; ++i)
		{
			discardPile.push(aiHand.getCard(0));
			aiHand.removeCard(aiHand.getCard(0));					
		}
		
		for(int i = 0; i < 3; ++i)
		{
			aiHand.addCard(deck.drawCard());
		}
		
		setMsgLabel("Opponent exchanged 3 cards");
	}
	
	/**
	 * Exchanges a random number of cards in the hand that aren't matching sets
	 * @param setSize what size of the set to look for
	 */
	private void aiSetBehavior(int setSize)
	{
		if(setSize >= aiHand.size())
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int numToExchange = (int)(Math.random() * (HAND_SIZE - setSize) + 1),  //randomly choose how many cards to discard
			count = 0,
			index = 0;
		Card rankOfPair = aiHand.getHighestSetRank(setSize);		
		
		while(count != numToExchange) //remove n cards from hand that are not part of the set
		{
			if(aiHand.getCard(index).compareTo(rankOfPair) != 0)				
			{
				discardPile.push(aiHand.getCard(index));
				aiHand.removeCard(aiHand.getCard(index));
				++count;				
			}
			else
			{
				++index;
			}						
		}
		
		for(int i = 0; i < numToExchange; ++i) //draw the same number of cards as discarded
		{
			aiHand.addCard(deck.drawCard());
		}
		
		setMsgLabel("Opponent exchanged " + numToExchange + " cards");
	}
	
	public static void main(String[] args) 
	{		
		PokerGame game = new PokerGame();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.setPreferredSize(new Dimension(600, 600));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		
	}		
}