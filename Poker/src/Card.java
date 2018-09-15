/**
 * Chris Sy
 * Blank/commented lines = 81
 * Total lines = 166
 */

/* Suits:
 * [0] = heart
 * [1] = diamond
 * [2] = club 
 * [3] = spade
 * 
 * Ranks:
 * [0]  = 2
 * [1]  = 3
 * ...
 * [9]  = jack
 * [10] = queen
 * [11] = king
 * [12] = ace
 * 
 */
public class Card implements Comparable<Card>
{
	private static String[] _ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
			                          "jack", "queen", "king", "ace"};
	private static String[] _suits = {"hearts", "diamonds", "clubs", "spades"};	
	private int _rank,
		        _suit;
	
	/**
	 * Default constructor that instantiates a two of hearts
	 */
	public Card()
	{
		_rank = 0;
		_suit = 0;
	}
	
	/**
	 * Constructor that instantiates a given rank/suit
	 * @param rank the rank
	 * @param suit the suit
	 */
	public Card(int rank, int suit)
	{
		if( !( (rank > -1 && rank < _ranks.length) && (suit > -1 && suit < _suits.length) ) )
		{			
			throw new ArrayIndexOutOfBoundsException();
		}
		
		_rank = rank;
		_suit = suit;
	}
	
	/**
	 * Gets the suit of the card
	 * @return the suit
	 */
	public int getSuit()
	{
		return _suit;
	}
	
	/**
	 * Gets the rank of the card
	 * @return the rank
	 */
	public int getRank()
	{
		return _rank;
	}
	
	/**
	 * Gets the String expression of the card's suit
	 * @return the suit as a String
	 */
	public String getSuitString()
	{
		return _suits[_suit];
	}
	
	/**
	 * Gets the String expression of the ith rank of the Card class
	 * @param i the rank to express as a String
	 * @return the rank as a String
	 */
	static String whatRankString(int i)
	{
		if( !(i > -1 && i < _ranks.length) )
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return _ranks[i];
	}
	
	/**
	 * Gets the String expression of the ith suit of the Card class
	 * @param i the suit to express as a String
	 * @return the suit as a String
	 */
	static String whatSuitString(int i)
	{
		if ( !(i > -1 && i < _suits.length) )
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return _suits[i];
	}
	
	/**
	 * Sets the rank of the card
	 * @param rank the rank to set to
	 */
	public void setRank(int rank)
	{
		if( !(rank > -1 && rank < _ranks.length) )
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		_rank = rank;
	}
	
	/**
	 * Sets the suit of the card
	 * @param suit the suit to set to
	 */
	public void setSuit(int suit)
	{
		if ( !(suit > -1 && suit < _suits.length) )
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		_suit = suit;
	}
	
	/**
	 * Gets the card's rank expressed as a String
	 * @return the card's rank expressed as a String
	 */
	public String getRankString()
	{
		return _ranks[_rank];
	}
	
	@Override
	public String toString()
	{
		return _ranks[_rank] + " of " + _suits[_suit];
	}	

	@Override
	public int compareTo(Card o) 
	{		
		if(o != null)
		{
			return Integer.compare(_rank, o._rank);
		}
		
		return -1;
	}
}