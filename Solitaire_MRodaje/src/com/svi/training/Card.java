package com.svi.training;

import com.svi.enums.Rank;
import com.svi.enums.Suit;

public class Card {
	private Suit suit;
	private Rank rank;
	private boolean isFaceUp;
	
	/** Gets the Suit of the Card.
	 * @return An Enum type representing the suit of the Card.
	*/
	public Suit getSuit() {
		return suit;
	}

	/** Sets the Suit of the Card.
	 * @param	suit	An Enum Suit type containing the suit.
	*/
	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	/** Gets the Rank of the Card.
	 * @return An Enum type representing the rank of the Card.
	*/
	public Rank getRank() {
		return rank;
	}

	/** Sets the Rank of the Card.
	 * @param	rank	An Enum Rank type containing the suit.
	*/
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	/** Sets whether the Card is facing up or down.
	 * @param	isFaceUp	A boolean type containing the direction 
	 * where the Card is facing.
	*/
	public void setFace(boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}
	
	/**
	 * Get the direction of where the Card is facing.
	 * @param	card	A Card object containing the card.
	 * @return	A CardColor Enum type representing the color of the Card.
	 */
	public boolean getFace () {
		return isFaceUp;
	}
}