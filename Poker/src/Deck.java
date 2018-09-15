/**
 * Chris Sy
 * Blank/commented lines = 46
 * Total lines = 102
 */
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class Deck 
{	
	private Stack<Card> _deck;
	
	/**
	 * Default constructor that instantiates a shuffled full deck of cards
	 */
	public Deck()
	{
		_deck = new Stack<>();		
		
		reset();
	}
	
	/**
	 * Draws and removes one card from the top of the deck
	 * @return the top card of the deck
	 */
	public Card drawCard()
	{
		if(_deck.isEmpty())
		{
			throw new EmptyStackException();
		}
		
		return _deck.pop();
	}
	
	/**
	 * Gets the size of the deck
	 * @return the number of cards in the deck
	 */
	public int getSize()
	{
		return _deck.size();
	}
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle()
	{
		if(!_deck.isEmpty())
		{
			Collections.shuffle(_deck);
		}		
	}
	
	/**
	 * Removes all cards from the deck
	 */
	public void clear()
	{
		_deck.clear();
	}
	
	/**
	 * Returns the deck to the default state. Deck will be a full deck
	 * and shuffled.
	 */
	public void reset()
	{
		clear();
		
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 13; ++j)
			{
				_deck.push(new Card(j, i));
			}
		}
		
		Collections.shuffle(_deck); //cards were pushed into stack in an ordered fashion. shuffling required
	}
	
	/**
	 * Look at the card on top of the deck without removing it
	 * @return the card on top of the deck
	 */
	public Card peekTopCard()
	{		
		return _deck.peek();
	}
	
	/**
	 * Check if the deck is empty
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return _deck.isEmpty();
	}
}