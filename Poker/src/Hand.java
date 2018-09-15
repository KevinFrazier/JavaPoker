/**
 * Chris Sy
 * Blank/commented lines = 188 + 18 = 206
 * Total lines = 543
 */
import java.util.ArrayList;
import java.util.Collections;
/*
 * Ranking hierarchy
 * 10 - Royal flush
 * 9  - Straight flush
 * 8  - Four of a kind
 * 7  - Full house
 * 6  - Flush
 * 5  - Straight
 * 4  - Three of a kind
 * 3  - Two pair
 * 2  - Pair
 * 1  - High card 
 */
public class Hand implements Comparable<Hand>
{
	private final int CARDS_PER_HAND = 5;
	private ArrayList<Card> _hand;	
	private int _rank;
	
	/**
	 * Default constructor. Creates an empty hand of rank 0
	 */
	public Hand()
	{		
		_hand = new ArrayList<>();
		_rank = 0;
	}
	
	/**
	 * Constructor that takes a Deck and creates a hand by drawing n 
	 * cards directly from the deck.
	 * @param deck a deck of cards
	 */
	public Hand(Deck deck)
	{
		_hand = new ArrayList<>();		
		
		while(_hand.size() < CARDS_PER_HAND)
		{
			_hand.add(deck.drawCard());
		}		
		
		sort();
		evaluateRank();
	}
	
	/**
	 * Adds a card to the hand	
	 * @param card card to be added
	 * @return true if successfully added, false otherwise
	 */
	public boolean addCard(Card card)
	{
		if(_hand.size() < CARDS_PER_HAND)
		{
			_hand.add(card);
			sort();
			
			if(_hand.size() == CARDS_PER_HAND)
			{
				evaluateRank();
			}
			
			return true;
		}
		
		return false;		
	}
	
	/**
	 * Check if the hand contains the max number of cards allowed
	 * @return true if full, false if the number of cards is less than max
	 */
	public boolean full()
	{
		if(_hand.size() == CARDS_PER_HAND)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if the hand contains any cards or not.
	 * @return true if hand is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return _hand.isEmpty();
	}
	
	/**
	 * Check the size of the hand
	 * @return number of cards in hand
	 */
	public int size()
	{
		return _hand.size();
	}
	
	/**
	 * Get the ranking of the hand based on the poker hand hierarchy
	 * @return the rank of the hand
	 */
	public int getRank()
	{
		return _rank;
	}
	
	/**
	 * Get the ith card in the hand
	 * @param index which card in the hand to get
	 * @return the ith card
	 */
	public Card getCard(int index)
	{
		if( !(index > -1 && index < _hand.size()) )
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return _hand.get(index);
	}
	
	/**
	 * Clears the hand of all cards
	 */
	public void clear()
	{
		_hand.clear();
	}	
	
	/**
	 * Removes a specific card from the hand
	 * @param card the card to be removed
	 * @return true if card was found and successfully removed, false otherwise
	 */
	public boolean removeCard(Card card)
	{
		if(_hand.remove(card))
		{			
			_rank = 0;  //incomplete hand has no rank
			return true;
		}		
		
		return false;
	}
		
	@Override
	public String toString()
	{
		String str = "";
		
		for(int i = 0; i < _hand.size(); ++i)
		{
			str += _hand.get(i) + "\n";
		}
		
		return str;
	}
	
	/**
	 * Sorts the hand in order of lowest to higher rank
	 */
	private void sort()
	{
		Collections.sort(_hand);		
	}
	
	/**
	 * Gets the highest ranking card in the hand
	 * @return the card with the highest rank in the hand
	 */
	public Card getHighestCard()
	{
		int max = 0,
		    index = 0;
		
		for(int i = 0; i < _hand.size(); ++i)
		{
			if(_hand.get(i).getRank() > max)
			{
				max = _hand.get(i).getRank();
				index = i;
			}
		}
		
		return _hand.get(index);
	}
	
	/**
	 * Evaluates the rank of the hand in terms of the poker hand hierarchy
	 */
	private void evaluateRank()
	{
		ArrayList<Integer> rankDistribution = new ArrayList<>(); //a "histogram" of the ranks in a hand
		int start = 0,
		    numDupes = 0;	    
		
		while(start < _hand.size())
		{
			numDupes = numDuplicateRanks(start);			
			
			rankDistribution.add(numDupes);   //record # of duplicates of a particular rank
			start += numDupes;  //move index a distance equal to number of duplicates found			
		}
		
		evaluateDistribution(rankDistribution);		
	}
	
	/**
	 * Takes a ArrayList<Integer> that serves as a tally of the frequency of ranks in the hand
	 * and evaluates what type of poker hand it is
	 * @param rankDistrib histogram of a hand
	 */
	private void evaluateDistribution(ArrayList<Integer> rankDistrib)
	{
		Collections.sort(rankDistrib);		
		
		switch(rankDistrib.size())  //size = how many unique ranks are in the hand
		{			
			case 2:  //2 unique ranks found
				if(rankDistrib.get(1) == 4 && rankDistrib.get(0) == 1)  //four of a kind
				{
					_rank = 8;
				}
				else if(rankDistrib.get(1) == 3 && rankDistrib.get(0) == 2) //full house
				{
					_rank = 7;
				}
				break;
				
			case 3:  //3 unique ranks
				if(rankDistrib.get(2) == 3 && rankDistrib.get(1) == 1 && rankDistrib.get(0) == 1)  //three of a kind
				{
					_rank = 4;
				}
				else if(rankDistrib.get(2) == 2 && rankDistrib.get(1) == 2 && rankDistrib.get(0) == 1) //two pairs
				{
					_rank = 3;
				}
				break;
			
			case 4:  //4 ranks, pair is only possibility
				_rank = 2;  
				break;
				
			default:  //if 5 unique ranks are found, it must be one of the other ranks
				if(isFlush())
				{
					_rank = 6;
					
					if(isStraight())  //straight flush
					{
						_rank = 9;
					}
				}
				else if(isStraight())  //only straight
				{
					_rank = 5;
				}
				else
				{
					_rank = 1;
				}				
		}
	}
	
	/**
	 * Evaluates if the hand is flush
	 * @return true if the hand is a flush, false otherwise
	 */
	private boolean isFlush()
	{
		int suit = _hand.get(0).getSuit();  //suit of first card
		
		for(int i = 1; i < _hand.size(); ++i)
		{
			if(suit != _hand.get(i).getSuit())
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Evaluates if the hand is a straight
	 * @return true if the hand is a straight, false otherwise
	 */
	private boolean isStraight()
	{
		if(_hand.get(_hand.size() - 1).getRank() == 12 && _hand.get(_hand.size() - 2).getRank() == 5)  //A2345
		{
			return true;
		}
		else if(_hand.get(_hand.size() - 1).getRank() - _hand.get(0).getRank() == 4)  //distance between bottom card and top card is 4 (straight)
		{
			return true;
		}		
		
		return false;
	}
	
	/**
	 * Checks for how many other cards in the hand that are of identical rank to [start]
	 * @param start the position of the card with which to check for duplicates
	 * @return the number of duplicates
	 */
	private int numDuplicateRanks(int start)
	{
		int rank = _hand.get(start).getRank(),
			count = 1; //the starting card itself counts as 1
		
		while(start < _hand.size() - 1)
		{
			if(_hand.get(start + 1).getRank() == rank)
			{
				++count;							
			}
			else
			{
				break;
			}
			
			start += 1;				
		}
		
		return count;
	}

	@Override
	public int compareTo(Hand arg0) 
	{
		if(Integer.compare(_rank, arg0._rank) != 0)
		{
			return Integer.compare(_rank, arg0._rank);
		}
		else //attempt to break the tie
		{
			return getTieBreaker(arg0);
		}		
	}
	
	/**
	 * Evaluates and compares the sub ranks to compare two hands whose ranks are identical
	 * @param other other hand to compare to
	 * @return -1 if this hand loses, 0 if tie, 1 if this hand wins
	 */
	private int getTieBreaker(Hand other) 
	{
		switch(_rank)
		{
			case 9: //straight flush				
					return getHighestCard().compareTo(other.getHighestCard()); //winner determined by highest card in hand
					
			case 8:	//4 of a kind
					return getHighestSetRank(4).compareTo(other.getHighestSetRank(4));  //winner is the highest ranked set
					
			case 7: //full house
					return getHighestSetRank(3).compareTo(other.getHighestSetRank(3)); //winner is highest set of 3
					
			case 6: //flush
					return compareSortedSequence(other);
					
			case 5: //straight
					return getHighestCard().compareTo(other.getHighestCard());
					
			case 4: //3 of a kind
					return getHighestSetRank(3).compareTo(other.getHighestSetRank(3));
					
			case 3: //2 pairs
					return tieBreakPairs(other);
					
			case 2: //pair					
					return tieBreakPairs(other);
		
			case 1: //high card
					return compareSortedSequence(other);
					
			default:
				//empty
		}
		
		return 0;
	}
	
	/**
	 * Constructs lists of all the pairs in this hand and the other hand
	 * @param myPairs list of this hand's pairs
	 * @param otherPairs list of other hand's pairs
	 * @param other the other hand
	 */
	private void getPairsList(ArrayList<Integer> myPairs, ArrayList<Integer> otherPairs, Hand other)
	{
		for(int i = 0; i < _hand.size() - 1; ++i)
		{
			for(int j = i + 1; j < _hand.size(); ++j)
			{
				if(_hand.get(i).compareTo(_hand.get(j)) == 0)
				{
					myPairs.add(_hand.get(i).getRank());					
				}			
				
				if(other.getCard(i).compareTo(other.getCard(j)) == 0)
				{
					otherPairs.add(other.getCard(i).getRank());
				}
			}
		}
	}
	
	/**
	 * Evaluates the kicker cards to resolve a tie
	 * @param myPairs this hand's pairs
	 * @param otherPairs other hand's pairs
	 * @param other the other hand
	 * @return -1 if this hand loses, 0 if tie, 1 if this hand wins
	 */
	private int evaluateKicker(ArrayList<Integer> myPairs, ArrayList<Integer> otherPairs, Hand other)
	{	
		ArrayList<Integer> myNonPairs = new ArrayList<>(),
				           otherNonPairs = new ArrayList<>();
		int compare = 0;
		
		for(int i = 0; i < size(); ++i)
		{
			if( !myPairs.contains(getCard(i).getRank()) )
			{
				myNonPairs.add(getCard(i).getRank());
			}
			
			if( !otherPairs.contains(other.getCard(i).getRank()) )
			{
				otherNonPairs.add(getCard(i).getRank());
			}								
		}
		
		Collections.sort(myNonPairs);
		Collections.sort(otherNonPairs);		
		
		for(int i = myNonPairs.size() - 1; i > -1; --i)
		{
			compare = myNonPairs.get(i).compareTo(otherNonPairs.get(i));
			
			if(compare != 0)
			{
				return compare;
			}
		}
		
		return compare;
	}
	
	/**
	 * Break a tie between two hands that have a pair
	 * @param other the other hand
	 * @return -1 if this hand loses, 0 if tie, 1 if this hand wins
	 */
	private int tieBreakPairs(Hand other)
	{		
		ArrayList<Integer> myPairRanks = new ArrayList<>(),
				        otherPairRanks = new ArrayList<>();
        int compare;
        
		getPairsList(myPairRanks, otherPairRanks, other);
		Collections.sort(myPairRanks);
		Collections.sort(otherPairRanks);
		
		for(int i = myPairRanks.size() - 1; i > -1; --i)
		{
			compare = myPairRanks.get(i).compareTo(otherPairRanks.get(i));
			
			if(compare != 0)
			{
				return compare;
			}
		}
				
		return evaluateKicker(myPairRanks, otherPairRanks, other);  //compare kicker cards if all pairs are identical
	}
	
	private int compareSortedSequence(Hand other)
	{
		int comparison = 0;
		
		for(int i = _hand.size() - 1; i > -1; --i)
		{
			comparison = _hand.get(i).compareTo(other.getCard(i));
			
			if(comparison != 0)
			{
				return comparison;  //stop as soon as inequality is found
			}
		}
		
		return comparison;  //both hands are the same
	}
	
	/**
	 * Gets a card of highest ranking set in the hand
	 * @param setSize size of the set to look for
	 * @return  card of the highest rank of the set
	 */
	public Card getHighestSetRank(int setSize)  
	{
		if(setSize >= _hand.size())
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Card high = null;		
		int count = 1;
		
		for(int i = 0; i < _hand.size() - 1; ++i)
		{
			if(_hand.get(i).compareTo(_hand.get(i + 1)) == 0)
			{
				++count;
			}
			else
			{
				count = 1;
			}
			
			if(count == setSize && _hand.get(i).compareTo(high) == 1)
			{
				high = _hand.get(i);				
			}
		}
		
		return high;  
	}
}