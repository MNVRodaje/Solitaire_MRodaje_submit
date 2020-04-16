package com.svi.training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameManager {
	public Scanner input = new Scanner(System.in);
	public static int moveNumber = 0, numberOfTalonDraw = 0, numberOfWins = 0;
	public static boolean isShow = false;
	
	/**
	 * Gets input from the user for the desired Number of Games
	 * @return	An Int type representing the Number of Games
	 */
	public int inputNumberOfGames() {
		int numberOfGames = 0;
		boolean isValid;

		do {
			System.out.println("Input desired number of games: ");
			if (input.hasNextInt()) {

				numberOfGames = input.nextInt();
				
				if (numberOfGames <= 0) {
					System.out.println("Enter positive integer only!");
					isValid = false;
					input.nextLine();
				} else {
					isValid = true;
				}
			} else {
				System.out.println("Wrong Data Type! Input numeric value only!");
				isValid = false;
				input.next();
			}
		} while (!isValid);
		return numberOfGames;
	}
	
	/**
	 * Gets input from the user for the desired Number of draws from the Talon.
	 * 1 for easy difficulty and 2 for hard difficulty
	 * @return	An Int type representing the Number of draws from the Talon.
	 */
	public int inputNumberOfTalonDraws() {
		boolean isValid;

		do {
			System.out.println("Choose Number of Cards to Draw from the Talon:");
			System.out.println("(1) 1 card draw only from the talon");
			System.out.println("(2) 3 cards draw from the talon");
			if (input.hasNextInt()) {
				numberOfTalonDraw = input.nextInt();
				if (numberOfTalonDraw != 1 && numberOfTalonDraw != 2) {
					System.out.println("Input 1 or 2 only!");
					isValid = false;
					input.nextLine();
				} else if (numberOfTalonDraw == 2) {
					numberOfTalonDraw++;
					isValid = true;
				} else {
					isValid = true;
				}

			} else {
				System.out.println("Wrong Data Type! Input 1 or 2 only!");
				isValid = false;
				input.next();
			}
		} while (!(isValid));
		return numberOfTalonDraw;
	}
	
	/**
	 * Gets input for if the user wants to shuffle the deck.
	 * @return	A Boolean type representing if the deck is shuffled.
	 */
	public boolean inputIsShuffled() {
		boolean isValid;
		boolean isShuffled = false;
		String choice;

		do {
			System.out.print("Shuffle the Deck? (Y/y for YES, N/n for NO): ");
			if (input.hasNext()) {
				choice = input.next();
				switch (choice) {
				case "Y":
					return true;
				case "y":
					return true;
				case "N":
					return false;
				case "n":
					return false;
				default:
					System.out.println("Invalid Input! Enter Y/y or N/n only!");
					isValid = false;
				}
			} else {
				System.out.println("Invalid Input! Enter Y/y or N/n only!");
				isValid = false;
				input.next();
			}
		} while (!(isValid));
		return isShuffled;
	}
	
	/**
	 * Gets input for if the user wants to show the supposed face-down cards from the pile.
	 * @return	A Boolean type representing if the supposed face-down cards from the pile is shown.
	 */
	public boolean inputIsShow() {
		boolean isValid;
		boolean isShow = false;
		String choice;

		do {
			System.out.print("Show supposed HIDDEN CARDS? (Y/y for YES, N/n for NO): ");
			if (input.hasNext()) {
				choice = input.next();
				switch (choice) {
				case "Y":
					return true;
				case "y":
					return true;
				case "N":
					return false;
				case "n":
					return false;
				default:
					System.out.println("Invalid Input! Enter Y/y or N/n only!");
					isValid = false;
				}
			} else {
				System.out.println("Invalid Input! Enter Y/y or N/n only!");
				isValid = false;
				input.next();
			}
		} while (!(isValid));
		return isShow;
	}
	
	
	/**
	 * Distributes cards to the Manoeuvre piles and the remaining to the Talon.
	 * @param	maindeck	A Deck type containing the initial main deck.
	 * @return				A Map type representing the Manoeuvre piles and Talon.
	 */
	public Map<Integer, Deck> distributeCardsToTable(Deck maindeck) {
		int manoeuvreNumber = 0;
		Map<Integer, Deck> manoeuvrePileMap = new HashMap<>();
		for (Integer pileNumber = 0; pileNumber <= 7; pileNumber++) {
			Deck tempPile = new Deck();
			tempPile.setCardsDeck(new ArrayList<>());
			manoeuvrePileMap.put(pileNumber, tempPile);
		}
		List<Card> tempCardList = maindeck.getCardsDeck();
		for (int numberOfRow = 0; numberOfRow <= 7; numberOfRow++) {
			manoeuvreNumber++;
			Integer manoeuvrePileNumber = manoeuvreNumber;
			boolean isFirstCard = true;
			for (int cardCount = manoeuvreNumber; cardCount <= 7; cardCount++) {

				manoeuvrePileMap.get(manoeuvrePileNumber).getCardsDeck().add(tempCardList.remove(0));

				int maxSize = manoeuvrePileMap.get(manoeuvrePileNumber).getCardsDeck().size();
				if (isFirstCard) {
					manoeuvrePileMap.get(manoeuvrePileNumber).getCardsDeck().get(maxSize - 1).setFace(true);
				} else {
					manoeuvrePileMap.get(manoeuvrePileNumber).getCardsDeck().get(maxSize - 1).setFace(false);
				}

				manoeuvrePileNumber++;
				isFirstCard = false;
			}
		}
		manoeuvrePileMap.get(0).getCardsDeck().addAll(tempCardList);
		return manoeuvrePileMap;
	}
	
	/**
	 * Displays the Talon remaining number of cards, the Hand card(s), the Foundation stack 
	 * and their respective top card, the Manoeuvre piles, Move Number and Move Description.
	 * @param piles			A Map type containing the Talon and Manoeuvre piles.
	 * @param hand			A List type containing the Hand card(s) and waste.
	 * @param foundationMap	A Map type containing the Foundation Stacks.
	 */
	public void display(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundationMap, String moveDescription) {
		moveNumber++;
		System.out.println("********************Move Number " + moveNumber + "********************");
		System.out.println(moveDescription);
		System.out.println("-----------------------------------------------------");
		displayTopLine(piles, hand, foundationMap);
		displayTableau(piles);
		System.out.println("________________End of Move Number " + moveNumber + "________________");
		System.out.println("\n");
	}
	
	/**
	 * Displays Manoeuvre piles.
	 * @param manoeuvrePiles	A Map type containing the Manoeuvre piles.
	 */
	private void displayTableau(Map<Integer, Deck> manoeuvrePiles) {
		Integer[] size = new Integer[manoeuvrePiles.size()];
		Integer highestPileSize = 0;
		int i = 0;
		for (Map.Entry<Integer, Deck> entry : manoeuvrePiles.entrySet()) {
			if (i == 0) {
				i++;
				continue;
			} else {
				size[i] = entry.getValue().getCardsDeck().size();
				if (i == 1) {
					highestPileSize = size[i];
				}
				if (i != 1) {
					if (size[i] > highestPileSize) {
						highestPileSize = size[i];
					}
				}
				i++;
			}
		}

		for (Integer rows = 1; rows <= highestPileSize; rows++) {
			Integer index = 0;
			for (Map.Entry<Integer, Deck> entry : manoeuvrePiles.entrySet()) {
				if (index == 0) {
					index++;
					continue;
				} else {
					if (entry.getValue().getCardsDeck().size() < rows) {
						System.out.print("\t");
						continue;
					}
					Card card = entry.getValue().getCardsDeck().get(rows - 1);
					if (index != 1) {
						System.out.print("\t");
					} else {
						
						boolean show = GameManager.isShow;
						if (!card.getFace() && !show ) {
							System.out.print("X_X\t");
						} else {
							if (index != 1) {
								System.out.print("\t");
							}
							System.out.print(card.getSuit().toString());
							System.out.print(card.getRank().toString());
							if (!card.getFace() && show) {
								System.out.print(" <X");
							}
							System.out.print("\t");
						}
					}
				}
			}
			System.out.println();
			index++;
		}
	}
	
	/**
	 * Displays the Talon, the Hand card(s), and the four Foundations.
	 * @param piles				A Map type containing the Talon and Manoeuvre piles.
	 * @param hand				A List type containing the Hand card(s) and waste.
	 * @param foundationStack	A Map type containing the Foundation Stacks.
	 */
	private void displayTopLine(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundationStack) {
		final Integer numberOfFoundations = 4;

		int talonSize = piles.get(0).getCardsDeck().size();
		String handCard = " ", foundationTopCard;
		System.out.print("[" + talonSize + "]\tTop "+ numberOfTalonDraw + " Hand card(s):\t");

		List<Card> handToDisplay = new ArrayList<Card>();
		
		for (int counter = 0 ; counter < numberOfTalonDraw; counter++) {
			if(!hand.isEmpty()) {
				handToDisplay.add(hand.remove(0));
			} else {
				break;
			}
		}
		int numberOfCardsToDisplay = handToDisplay.size();
		for (int counter = 0; counter < numberOfCardsToDisplay; counter++) {
			if (!handToDisplay.isEmpty()) {
					handCard = handToDisplay.get(counter).getSuit().toString() + handToDisplay.get(counter).getRank().toString();
			} else {
				handCard = " ";
			}
			System.out.print("[" + handCard + "]\t");
		}
		for (int counter = 0; counter < handToDisplay.size(); counter++) {
			hand.add(0, handToDisplay.remove(handToDisplay.size()-1));
			counter--;
		}
		
		System.out.print("\n\tFoundations:\t");
		
		for (int count = 0; count < numberOfFoundations; count++) {
			if(!foundationStack.get(count).getCardsDeck().isEmpty()) {
				String foundationTopCardSuit = foundationStack.get(count).getCardsDeck().get(0).getSuit().toString();
				String foundationTopCardRank = foundationStack.get(count).getCardsDeck().get(0).getRank().toString();
				foundationTopCard = foundationTopCardSuit + foundationTopCardRank;
			} else {
				foundationTopCard = " ";
			}
			System.out.print("[" + foundationTopCard + "]\t");
		}
		System.out.println();
	}

	
	/**
	 * Display whether the game is won or lost.
	 * @param win	A boolean type containing whether the game is won or lost.
	 */
	public void winOrLose(boolean win) {
		if (win) {
			GameManager.numberOfWins++;
			System.out.println("All cards are placed to the Foundation.\nTHE GAME IS WON.");
		} else if (!win) {
			System.out.println("There are still face-down Cards and there are no more possible moves.\nTHE GAME IS LOST.\n");
		}
	}
}
