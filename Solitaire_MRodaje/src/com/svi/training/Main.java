package com.svi.training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		GameManager gameManager = new GameManager();
		Player player = new Player();

		// input
		int numberOfGames = gameManager.inputNumberOfGames();
		int numberOfTalonDraws = gameManager.inputNumberOfTalonDraws();
		boolean isShuffled = gameManager.inputIsShuffled();
		GameManager.isShow = gameManager.inputIsShow();

		for (int gameCounter = 1; gameCounter <= numberOfGames; gameCounter++) {
			GameManager.moveNumber = 0;
			
			// create deck
			Deck maindeck = new Deck();
			List<Card> cardList = maindeck.createDeck();
			maindeck.setCardsDeck(cardList);

//			// output initial (non-shuffled) deck
//			System.out.println("Deck: ");
//			for (Card card : maindeck.getCardsDeck()) {
//				System.out.print(card.getSuit());
//				System.out.println(card.getRank());
//			}

			maindeck.shuffleDeck(isShuffled);

//			// output either shuffled or not
//			System.out.println("\nDeck: ");
//			for (Card card : maindeck.getCardsDeck()) {
//				System.out.print(card.getSuit());
//				System.out.println(card.getRank());
//			}

			//create 4 empty foundations
			Map<Integer, Deck> foundationMap = new HashMap<>();
			for (int counter = 0; counter < 4; counter++) {
				Deck foundationDeck = new Deck();
				List<Card> cards = new ArrayList<>();
				foundationDeck.setCardsDeck(cards);

				foundationMap.put(counter, foundationDeck);
			}
			
			// distribute to tableau
			Map<Integer, Deck> piles = gameManager.distributeCardsToTable(maindeck);
			GameManager.moveNumber--;

			List<Card> hand = new ArrayList<Card>();
			String moveDescription = "Distribute Cards to Manoeuvre.";
			gameManager.display(piles, hand, foundationMap, moveDescription);

			player.playSolitaire(piles, hand, foundationMap, numberOfTalonDraws);
			
		}
		int numberOfWins = GameManager.numberOfWins;
		//float winrate = numberOfWins / numberOfGames * 100;
		System.out.println("For " + numberOfGames + " total number of games:\n" 
				+ numberOfWins + " total number of wins.\n");
	}
}