package com.svi.training.objects;

import com.svi.training.enums.CardColor;
import com.svi.training.enums.Rank;
import com.svi.training.enums.Suit;

public class Card {
	private Suit suit;
	private Rank rank;
	private CardColor cardColor;
	private boolean isFaceUp = true;
	private String cardString;

	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public void setRank (Rank rank) {
		this.rank = rank;
	}
	
	public Rank getRank () {
		return rank;
	}
	
	public void setCardColor (CardColor cardColor) {
		this.cardColor = cardColor;
	}
	
	public CardColor getcardColor () {
		return cardColor;
	}
	
	public void setFace (boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}
	
	public boolean getFace () {
		return isFaceUp;
	}
	
	public String toCardString () {
		cardString = suit.toString() + rank.toString();
		return cardString;
	}
}
