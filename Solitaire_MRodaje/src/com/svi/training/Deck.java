package com.svi.training;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.svi.enums.Rank;
import com.svi.enums.Suit;
import com.svi.training.Card;

public class Deck {
	private List<Card> cardsDeck;

	/**
	 * Gets the Cards to from the Deck.
	 * 
	 * @return A List type representing each of the Cards of the Deck.
	 */
	public List<Card> getCardsDeck() {
		return cardsDeck;
	}

	/**
	 * Sets the Cards of the Deck.
	 * 
	 * @param cardsDeck A List type containing the Cards of the Deck.
	 */
	public void setCardsDeck(List<Card> cardsDeck) {
		this.cardsDeck = cardsDeck;
	}

	/**
	 * Create the Cards of the Deck.
	 * 
	 * @param cardsDeck A List type containing the Cards of the Deck.
	 * @return A List representing the initial or non-shuffled deck.
	 */
	public List<Card> createDeck() {
		List<Card> cardList = new ArrayList<>();
		Map<Integer, String> rankMap = new HashMap<>();
		Map<Integer, String> suitMap = new HashMap<>();

		for (Rank rank : Rank.values()) {
			rankMap.put(rank.getRankValue(), rank.toString());
		}
		for (Suit suit : Suit.values()) {
			suitMap.put(suit.getSuitValue(), suit.toString());
		}

		for (Integer suitCounter = 1; suitCounter < 5; suitCounter++) {
			for (Integer rankCounter = 1; rankCounter < 14; rankCounter++) {
				Card card = new Card();
				Suit suit = Suit.valueOf(suitMap.get(suitCounter));
				Rank rank = Rank.valueOf(rankMap.get(rankCounter));

				card.setSuit(suit);
				card.setRank(rank);

				cardList.add(0, card);
			}
		}
		return cardList;
	}

	/**
	 * Shuffle the deck depending on the user's decision
	 * 
	 * @param isShuffled A boolean type containing the decision whether the deck
	 *                   will be shuffled.
	 */
	public void shuffleDeck(boolean isShuffled) {
		if (!isShuffled) {
			return;
		} else {
			Collections.shuffle(cardsDeck);
		}
	}
}