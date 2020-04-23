package com.svi.training.objects;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
	private static int moveNumber;
	private int numberOfDraw;
	private boolean mustShow;
	public static Stack<Card> talon;
	public static LinkedList<Card> hand;
	public static Map<Integer, LinkedList<Card>> manoeuvrePileMap;
	public static Map<Integer, LinkedList<Card>> foundationPileMap;
	public static String moveDescription;

	private void showTalonAndHand(int numberOfDraws) {
		System.out.print("[" + talon.size() + "]\tHand:\t\t");
		for (int counter = 0; counter < numberOfDraws; counter++) {
			System.out.print("[");
			if (hand.size() != 0 && counter < hand.size()) {
				System.out.print(hand.get(counter).toCardString());
			} else if ( (hand.size() != 0 && counter > hand.size()) || hand.isEmpty()) {
				System.out.print(" ");
			}
			System.out.print("]\t");
		}
	}

	private void showFoundation() {
		System.out.print("\n\tFoundation:\t");
		for (int counter = 0; counter < foundationPileMap.size(); counter++) {
			System.out.print("[");
			if (foundationPileMap.get(counter).size() != 0) {
				System.out.print(foundationPileMap.get(counter).getLast().toCardString());
			} else {
				System.out.print(" ");
			}
			System.out.print("]\t");
		}
		System.out.print("\n\n");
	}

	private void showManoeuvrePiles(boolean mustShow) {
		int highestSize = getHighestPileSize();
		for (int row = 0; row < highestSize; row++) {
			for (int column = 0; column < manoeuvrePileMap.size(); column++) {
				if (manoeuvrePileMap.get(column).size() <= row) {
					System.out.print("\t");
					continue;
				}
				if(manoeuvrePileMap.get(column).get(row).getFace()) {
					System.out.print("[" + manoeuvrePileMap.get(column).get(row).toCardString() + "]\t");
				} else {
					if (mustShow) {
						System.out.print("*[" + manoeuvrePileMap.get(column).get(row).toCardString() + "]\t");
					} else {
						System.out.print("[X_X]\t");
					}
				}
			}
			System.out.println();
		}
	}

	private int getHighestPileSize() {
		int highestSize = 0;
		for (int counter = 0; counter < manoeuvrePileMap.size(); counter++) {
			 if (counter==0) {
				 highestSize = manoeuvrePileMap.get(counter).size();
				 continue;
			 } 
			 if (manoeuvrePileMap.get(counter).size() > highestSize) {
				 highestSize = manoeuvrePileMap.get(counter).size();
			 }
		}
		return highestSize;
	}

	public void distributeCards(List<Card> mainDeck) {
		for (int cardIndex = 0; cardIndex < 7; cardIndex++) {
			for (int pileIndex = 0; pileIndex < 7; pileIndex++) {
				if (cardIndex > pileIndex) {
					continue;
				}
				if (pileIndex == cardIndex) {
					mainDeck.get(0).setFace(true);
				} else {
					mainDeck.get(0).setFace(false);
				}
				manoeuvrePileMap.get(pileIndex).add(mainDeck.remove(0));
			}
		}
		Collections.reverse(mainDeck);
		talon.addAll(mainDeck);
	}

	public void showTable (int game) {
		System.out.println("*************************GAME " + game + "*************************");
		System.out.println("MOVE "+ moveNumber + ": " + moveDescription);
		showTalonAndHand(numberOfDraw);
		showFoundation();
		showManoeuvrePiles(mustShow);
		System.out.println("********************* END OF MOVE " + moveNumber + "*********************\n");
		moveNumber++;
	}

	public void clearTable() {
		moveNumber = 0;
		talon = new Stack<Card>();
		hand = new LinkedList<Card>();
		manoeuvrePileMap = new HashMap<>();
		foundationPileMap = new HashMap<>();
		moveDescription = "Initial Cards' distribution to table.";
		for (int index = 0; index < 4; index++) {
			LinkedList<Card> emptyPile = new LinkedList<Card>();
			foundationPileMap.put(index, emptyPile);
		}
		for (int index = 0; index < 7; index++) {
			LinkedList<Card> emptyPile = new LinkedList<Card>();
			manoeuvrePileMap.put(index, emptyPile);
		}
	}
	
	public void setNumberOfDraw(int inputNumberOfDraw) {
		numberOfDraw = inputNumberOfDraw;
	}
	
	public void setMustShowCards(boolean mustShow) {
		this.mustShow = mustShow;
	}

	public void showResult(boolean win) {
		String result;
		if (win) {
			result = "This round is WON!";
		} else {
			result = "This round is a LOSS!";
		}
		System.out.println(result);
	}
}