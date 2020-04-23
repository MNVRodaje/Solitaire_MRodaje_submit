package com.svi.training.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.svi.training.enums.CardColor;
import com.svi.training.enums.Rank;
import com.svi.training.enums.Suit;

public class Deck {
	private List<Card> deck;

	private void setCardsDeck(List<Card> deck) {
		this.deck = deck;
	}
	
	public List<Card> getDeck() {
		return deck;
	}

	public List<Card> createDeck() {
		List<Card> cardList = new ArrayList<>();
		for (Integer suitCounter = 0; suitCounter < 4; suitCounter++) {
			for (Integer rankCounter = 0; rankCounter < 13; rankCounter++) {
				Card card = new Card();
				Suit suit = Suit.values()[suitCounter];
				Rank rank = Rank.values()[rankCounter];
				CardColor color = null;
				if (suit.toString() == "D" || suit.toString() == "H") {
					color = CardColor.RED;
				} else if (suit.toString() == "S" || suit.toString() == "C") {
					color = CardColor.BLACK;
				}
				card.setSuit(suit);
				card.setRank(rank);
				card.setCardColor(color);
				cardList.add(card);
			}
		}
		setCardsDeck (cardList);
		Collections.reverse(deck);
		return deck;
	}

	public List<Card> shuffleDeck(boolean isShuffled) {
		createDeck();
		if (!isShuffled) {
			return deck;
		} else {
			Collections.shuffle(deck);
		}
		return deck;
	}
}