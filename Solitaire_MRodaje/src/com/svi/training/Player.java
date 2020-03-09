package com.svi.training;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Player {
	private GameManager display = new GameManager();
	private int talonRepeat;
	private boolean possibleAceFromManoeuvreToFoundation = true, 
			possibleDeuceFromManoeuvreToFoundation = true, 
			posibbleManoeuvreToManoeuvre = true, 
			aceInHand = true, 
			deuceInHand = true,
			possibleHandToManoeuvre = true, 
			possibleKing = true,
			mustDraw = true, 
			mustRepeatTalon = true,
			possibleManoeuvreToFoundation = true;
	
	public boolean win = false, lose = false;

	/**
	 * Execute Playing Logic for Solitaire.
	 * @param piles			A Map type containing the List of Card piles.
	 * @param hand			A List type containing the Hand cards.
	 * @param foundation	A Map type containing the List of Card Foundation piles.
	 * @param numberOfDraws	An Int type containing the Number of Cards to be drawn from the Talon.
	 */
	public void playSolitaire(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation, int numberOfDraws) {
		
		List<Card> talon = piles.get(0).getCardsDeck();
		
		//do until there are no more possible moves
		do {
			do {
				//draw cards
				drawCards(talon, hand, numberOfDraws, piles, foundation);
				do {
					checkAce(piles, hand, foundation);					//possibleAceFromManoeuvreToFoundation = false
					checkDeuce(piles, hand, foundation);				//possibleDeuceFromManoeuvreToFoundation = false
					manoeuvreToManoeuvre(piles, hand, foundation);		//posibbleManoeuvreToManoeuvre = false
					aceInHandToFoundation(piles, hand, foundation);		//aceInHand = false
					deuceInHandToFoundation(piles, hand, foundation);	//deuceInHand = false
					handToManoeuvre(piles, hand, foundation);			//possibleHandToManoeuvre = false
					kingToEmptyPile(piles, hand, foundation);			//possibleKing
				} while (possibleAceFromManoeuvreToFoundation 
						|| possibleDeuceFromManoeuvreToFoundation 
						|| posibbleManoeuvreToManoeuvre 
						|| aceInHand 
						|| deuceInHand
						|| possibleHandToManoeuvre
						|| possibleKing);
				if (talon.isEmpty()) {
					if (talonRepeat>1) {
						mustDraw = false;
					} else if (talonRepeat <= 1) {
						if (mustRepeatTalon) {
							Collections.reverse(hand);
							talon.addAll(hand);
							hand.clear();
							talonRepeat++;
						}
					}
				}
			} while(mustDraw);
			
			checkManoeuvreToFoundation(piles, hand, foundation);
			
			if (talon.isEmpty()) {
				if (talonRepeat>3) {
					System.out.println(talonRepeat + " times repeatedly Drawing from Talon without any changes.");
					mustDraw = false;
					win = false;
					display.winOrLose(win);
					break;
				} else if (talonRepeat <= 3) {
					if (mustRepeatTalon) {
						Collections.reverse(hand);
						talon.addAll(hand);
						hand.clear();
						talonRepeat++;
					}
				} 
			}
			
			int numberOfEmptyPiles = 0;
			for (Map.Entry<Integer, Deck> entry : piles.entrySet()) {
				if(entry.getValue().getCardsDeck().isEmpty()) {
					numberOfEmptyPiles++;
				}
			}
			
			if (numberOfEmptyPiles == piles.size()) {
				win = true;
				display.winOrLose(win);
				break;
			}
		}while(possibleManoeuvreToFoundation || mustRepeatTalon);
		
	}
	
	/**
	 * Check and move possible Ace from the Manoeuvre piles.
	 * @param piles			A Map type containing the List of Card piles.
	 * @param hand			A List type containing the Hand cards.
	 * @param foundation	A Map type containing the List of Card Foundation piles.
	 */
	private void checkAce(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		int startPile = 1;
		for (int manoeuvreCounter = startPile; manoeuvreCounter < piles.size(); manoeuvreCounter++) {
			if (piles.get(manoeuvreCounter).getCardsDeck().isEmpty()) {
				continue;
			}

			List<Card> manoeuvrePileCardList = piles.get(manoeuvreCounter).getCardsDeck();
			Card manoeuvreCard = manoeuvrePileCardList.get(manoeuvrePileCardList.size() - 1);
			if (manoeuvreCard.getRank().getRankValue() == 1) {

				for (int foundationCounter = 0; foundationCounter < foundation.size(); foundationCounter++) {
					if (!foundation.get(foundationCounter).getCardsDeck().isEmpty()) {
						continue;
					} else {
						List<Card> source = piles.get(manoeuvreCounter).getCardsDeck();
						List<Card> destination = foundation.get(foundationCounter).getCardsDeck();
						int index = source.size() - 1;
						Card sourceCard = source.get(index);
						
						moveCardManoeuvreToFoundation(source, index, destination);
						
						String moveDescription = "Moved " 
								+ sourceCard.getSuit().toString()
								+ sourceCard.getRank().toString() 
								+ " from Manouvre to Foundation.";
						display.display(piles, hand, foundation, moveDescription);
						break;
					}
				}
			} else {
				possibleAceFromManoeuvreToFoundation = false;
			}
		}
	}

	/**
	 * Check and move possible Deuce from the Manoeuvre piles.
	 * @param piles			A Map type containing the List of Card piles.
	 * @param hand			A List type containing the Hand cards.
	 * @param foundation	A Map type containing the List of Card Foundation piles.
	 */
	private void checkDeuce(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		int startPile = 1;
		for (int manoeuvreCounter = startPile; manoeuvreCounter < piles.size(); manoeuvreCounter++) {
			List<Card> sourceCardList = piles.get(manoeuvreCounter).getCardsDeck();

			if (sourceCardList.isEmpty()) {
				continue;
			} else if (!sourceCardList.isEmpty()) {
				int sourceCardListSize = sourceCardList.size();
				Card sourceCard = sourceCardList.get(sourceCardListSize - 1);
				int sourceCardRank = sourceCard.getRank().getRankValue();
				int sourceCardSuit = sourceCard.getSuit().getSuitValue();

				if (sourceCardRank == 2) {
					for (int foundationCounter = 0; foundationCounter < foundation.size(); foundationCounter++) {
						List<Card> foundationCardList = foundation.get(foundationCounter).getCardsDeck();
						if (foundationCardList.isEmpty()) {
							continue;
						} else if (!foundationCardList.isEmpty()) {
							Card foundationTopCard = foundationCardList.get(0);
							int foundationTopCardSuitValue = foundationTopCard.getSuit().getSuitValue();
							int foundationTopCardRankValue = foundationTopCard.getRank().getRankValue();

							if (foundationTopCardRankValue == 1) {
								if (foundationTopCardSuitValue == sourceCardSuit) {
									List<Card> destinationCardList = foundationCardList;
									int index = sourceCardListSize - 1;
									
									moveCardManoeuvreToFoundation(sourceCardList, index, destinationCardList);

									possibleAceFromManoeuvreToFoundation = true;
									String moveDescription = "Moved " + sourceCard.getSuit().toString()
											+ sourceCard.getRank().toString() + " from Manouvre to Foundation.";
									display.display(piles, hand, foundation, moveDescription);
									break;
								} else {
									continue;
								}
							} else {
								continue;
							}
						}
					}
					possibleDeuceFromManoeuvreToFoundation = false;
				} else {
					possibleDeuceFromManoeuvreToFoundation = false;
				}
			}
		}
	}

	/**
	 * Check and move possible cards from Manoeuvre to Manoeuvre.
	 * @param piles			A Map type containing the List of Card piles.
	 * @param hand			A List type containing the Hand cards.
	 * @param foundation	A Map type containing the List of Card Foundation piles.
	 */
	private void manoeuvreToManoeuvre(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		int indexOfFirstPile = 0, indexOfSecondPile = 0;
		// comparing cards to move
		// first pile to be compared
		for (int firstPile = 1; firstPile <= 7; firstPile++) {
			Integer sizeFirstPile = piles.get(firstPile).getCardsDeck().size();

			if (sizeFirstPile == 0) {
				continue;
			}

			indexOfFirstPile = findIndexOfFirstFaceUpCard(piles.get(firstPile).getCardsDeck());

			// second pile to compare from first pile
			for (int secondPile = firstPile + 1; secondPile <= 7; secondPile++) {
				if (firstPile == secondPile) {
					continue;
				}
				Integer sizeSecondPile = piles.get(secondPile).getCardsDeck().size();

				if (sizeSecondPile == 0) {
					continue;
				}

				indexOfSecondPile = findIndexOfFirstFaceUpCard(piles.get(secondPile).getCardsDeck());

				Card firstCardSource = piles.get(firstPile).getCardsDeck().get(indexOfFirstPile);
				Card firstCardDestination = piles.get(firstPile).getCardsDeck().get(sizeFirstPile - 1);

				Card secondCardSource = piles.get(secondPile).getCardsDeck().get(indexOfSecondPile);
				Card secondCardDestination = piles.get(secondPile).getCardsDeck().get(sizeSecondPile - 1);

				if (isPossible(firstCardSource, secondCardDestination)) {
					moveCardsFromManoeuvreToManoeuvre(piles.get(firstPile).getCardsDeck(), indexOfFirstPile,
							piles.get(secondPile).getCardsDeck());
					firstPile--;

					possibleAceFromManoeuvreToFoundation = true;
					possibleDeuceFromManoeuvreToFoundation = true;

					String moveDescription = "Moved " 
							+ firstCardSource.getSuit().toString()
							+ firstCardSource.getRank().toString() 
							+ " to " 
							+ secondCardDestination.getSuit().toString()
							+ secondCardDestination.getRank().toString();
					
					display.display(piles, hand, foundation, moveDescription);
					break;
				} else if (isPossible(secondCardSource, firstCardDestination)) {
					moveCardsFromManoeuvreToManoeuvre(piles.get(secondPile).getCardsDeck(), indexOfSecondPile,
							piles.get(firstPile).getCardsDeck());
					firstPile--;

					possibleAceFromManoeuvreToFoundation = true;
					possibleDeuceFromManoeuvreToFoundation = true;

					String moveDescription = "Moved " 
							+ secondCardSource.getSuit().toString()
							+ secondCardSource.getRank().toString() 
							+ " to " 
							+ firstCardDestination.getSuit().toString()
							+ firstCardDestination.getRank().toString();
					
					display.display(piles, hand, foundation, moveDescription);
					break;
				} else {
					posibbleManoeuvreToManoeuvre = false;
				}
			}
		}
	}

	 /**
	 * Check and move possible Ace from hand to foundation.
	 * @param piles 
	 * @param hand			A List type containing the cards in Hand from the Talon.
	 * @param foundation	A Map type containing the List of cards in the foundation.
	 */
	private void aceInHandToFoundation(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		if (hand.isEmpty()) {
			return;
		}
		Card handTopCard = hand.get(0);
		int handTopCardRankValue = handTopCard.getRank().getRankValue();
		if (handTopCardRankValue == 1) {
			for (int counter = 0; counter < foundation.size(); counter++) {
				List<Card> foundationCardList = foundation.get(counter).getCardsDeck();
				if(foundationCardList.isEmpty()) {
					
					foundationCardList.add(0, hand.remove(0));
					possibleHandToManoeuvre = true;
					mustRepeatTalon = true;
					talonRepeat = 0;
					
					String moveDescription = "Moved " 
							+ handTopCard.getSuit().toString()
							+ handTopCard.getRank().toString()
							+ " to an empty Foundation.";
					
					display.display(piles, hand, foundation, moveDescription);
					break;
				} else if (!foundationCardList.isEmpty()) {
					continue;
				}
			}
		} else {
			aceInHand = false;
		}
	}
	
	/**
	 * Check and move possible Deuce from hand to foundation.
	 * @param piles 
	 * @param hand			A List type containing the cards in Hand from the Talon.
	 * @param foundation	A Map type containing the List of cards in the foundation.
	 */
	private void deuceInHandToFoundation(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		if (hand.isEmpty()) {
			return;
		}
		Card handTopCard = hand.get(0);
		int handTopCardRankValue = handTopCard.getRank().getRankValue();
		int handTopCardSuitValue = handTopCard.getSuit().getSuitValue();
		
		if (handTopCardRankValue == 2) {
			for (int counter = 0; counter < foundation.size(); counter++) {
				List<Card> foundationCardList = foundation.get(counter).getCardsDeck();
				if(!foundationCardList.isEmpty()) {
					Card foundationTopCard = foundationCardList.get(0);
					int foundationTopCardRankValue = foundationTopCard.getRank().getRankValue();
					int foundationTopCardSuitValue = foundationTopCard.getSuit().getSuitValue();
					
					if (foundationTopCardRankValue == 1) {
						if (foundationTopCardSuitValue == handTopCardSuitValue) {
							foundationCardList.add(0, hand.remove(0));
							
							aceInHand = true;
							mustRepeatTalon = true;
							talonRepeat = 0;
							
							String moveDescription = "Moved " 
									+ handTopCard.getSuit().toString()
									+ handTopCard.getRank().toString()
									+ " to "
									+ foundationTopCard.getSuit().toString()
									+ foundationTopCard.getRank().toString()
									+ " in Foundation.";
							
							display.display(piles, hand, foundation, moveDescription);
							break;
						} else {
							continue;
						}
					} else {
						continue;
					}
				} else if (foundationCardList.isEmpty()) {
					deuceInHand = false;
					continue;
				}
			}
		} else {
			deuceInHand = false;
		}
	}
	
	/**
	 * Check and move possible Card from Hand To Manoeuvre
	 * @param piles			A Map type containing the List of Card piles
	 * @param hand			A List type containing the hand Cards
	 * @param foundation	A Map type containing the List of Foundation stacks
	 */
	private void handToManoeuvre(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		if (hand.isEmpty()) {
			possibleHandToManoeuvre = false;
			return;
		}
		Card handTopCard = hand.get(0);
		int startPile = 1;
		for (int counter = startPile; counter < piles.size(); counter++) {
			List<Card> manoeuvreCardList = piles.get(counter).getCardsDeck();
			if (!manoeuvreCardList.isEmpty()) {
				int manoeuvreIndex = manoeuvreCardList.size() - 1;
				Card destination = manoeuvreCardList.get(manoeuvreIndex);
				if(isPossible(handTopCard, destination)) {
					hand.get(0).setFace(true);
					manoeuvreCardList.add(hand.remove(0));
					aceInHand = true;
					deuceInHand = true;
					posibbleManoeuvreToManoeuvre = true;
					mustRepeatTalon = true;
					talonRepeat = 0;
					
					String moveDescription = "Moved " 
							+ handTopCard.getSuit().toString()
							+ handTopCard.getRank().toString()
							+ " from Hand to "
							+ destination.getSuit().toString()
							+ destination.getRank().toString()
							+ " in Manoeuvre pile.";
					
					display.display(piles, hand, foundation, moveDescription);
					break;
				} else {
					continue;
				}
			}
			else {
				continue;
			}
		}
		possibleHandToManoeuvre = false;
	}
	
	/**
	 * Check and move possible King from Hand or Manoeuvre to an Empty Manoeuvre Pile.
	 * @param piles			A Map type containing the List of Cards per Manoeuvre Pile
	 * @param hand			A List type containing the Hand Cards
	 * @param foundation	A Map type containing the List of Cards per Foundation Stack
	 */
	private void kingToEmptyPile(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		int startPile = 1;
		
		for(int counter = startPile; counter < piles.size(); counter++) {
			List<Card> emptyPile = piles.get(counter).getCardsDeck();
			if (emptyPile.isEmpty()) {
				for (int counter1 = startPile; counter1 < piles.size(); counter1++) {
					List<Card> kingPile = piles.get(counter1).getCardsDeck();
					if (counter == counter1) {
						continue;
					}
					if(kingPile.isEmpty()) {
						continue;
					}
					
					int kingIndex = findIndexOfFirstFaceUpCard(kingPile);
					Card king = kingPile.get(kingIndex);
					int kingRankValue = king.getRank().getRankValue();
					
					//if King is already in an Empty Pile to avoid infinite loop
					if (kingIndex == 0) {
						continue;
					}
					
					if (kingRankValue == 13) {
						moveCardsFromManoeuvreToManoeuvre(kingPile, kingIndex, emptyPile);
						
						possibleAceFromManoeuvreToFoundation = true;
						possibleDeuceFromManoeuvreToFoundation = true;
						posibbleManoeuvreToManoeuvre = true;
						possibleHandToManoeuvre = true;
						
						String moveDescription = "Moved "
								+ king.getSuit().toString()
								+ king.getRank().toString()
								+ " to an Empty Manoeuvre Pile.";
						
						display.display(piles, hand, foundation, moveDescription);
						break;
					} else {
						continue;
					}
				}
			} else {
				continue;
			}
		}
		if (!hand.isEmpty()) {
			Card handTopCard = hand.get(0);
			int handTopCardRankValue = handTopCard.getRank().getRankValue();
			if (handTopCardRankValue == 13) {
				for (int counter = startPile; counter < piles.size(); counter++) {
					List<Card> emptyPile = piles.get(counter).getCardsDeck();
					if (emptyPile.isEmpty()) {
						handTopCard.setFace(true);
						emptyPile.add(hand.remove(0));
						
						aceInHand = true;
						deuceInHand = true;
						posibbleManoeuvreToManoeuvre = true;
						possibleHandToManoeuvre = true;
						mustRepeatTalon = true;
						talonRepeat = 0;
						
						String moveDescription = "Moved "
								+ handTopCard.getSuit().toString()
								+ handTopCard.getRank().toString()
								+ " from Hand to an Empty Manoeuvre Pile.";
						
						display.display(piles, hand, foundation, moveDescription);
						break;
					} else {
						continue;
					}
				}
			}
		} else {
			aceInHand = false;
			deuceInHand = false;
		}
		possibleKing = false;
	}
	
	/**
	 * Check any possible Card from Manoeuvre piles to be move to the Foundations.
	 * @param piles			A Map type containing the List of Cards per Manoeuvre Pile
	 * @param hand			A List type containing the Hand Cards
	 * @param foundation	A Map type containing the List of Cards per Foundation Stack
	 */
	private void checkManoeuvreToFoundation(Map<Integer, Deck> piles, List<Card> hand, Map<Integer, Deck> foundation) {
		int startPile = 1;
		for (int foundationCounter = 0; foundationCounter < foundation.size(); foundationCounter++) {
			List<Card> destination = foundation.get(foundationCounter).getCardsDeck();
			
			if(destination.isEmpty()) {
				continue;
			}
			
			Card destinationTopCard = destination.get(0);
			int destinationTopCardRankValue = destinationTopCard.getRank().getRankValue();
			int destinationTopCardSuitValue = destinationTopCard.getSuit().getSuitValue();
			
			for (int manoeuvreCounter = startPile; manoeuvreCounter < piles.size(); manoeuvreCounter++) {
				List<Card> source = piles.get(manoeuvreCounter).getCardsDeck();
				
				if (source.isEmpty()) {
					continue;
				}
				
				int sourceIndex = source.size() - 1;
				Card sourceCard = source.get(sourceIndex);
				int sourceCardRankValue = sourceCard.getRank().getRankValue();
				int sourceCardSuitValue = sourceCard.getSuit().getSuitValue();
				
				if (sourceCardSuitValue == destinationTopCardSuitValue) {
					if (sourceCardRankValue == destinationTopCardRankValue + 1) {
						moveCardManoeuvreToFoundation(source, sourceIndex, destination);
						
						mustDraw = true;
						possibleManoeuvreToFoundation = true;
						
						talonRepeat = 0;
						
						String moveDescription = "Moved " 
								+ sourceCard.getSuit().toString()
								+ sourceCard.getRank().toString() 
								+ " from Manouvre to Foundation.";
						display.display(piles, hand, foundation, moveDescription);
						break;
					}
				} else {
					possibleManoeuvreToFoundation = false;
				}
				
			}
		}
		
	}
	
	/**
	 * Draw desired number of cards from the Talon.
	 * @param talon			A List type containing the Talon.
	 * @param hand			A List type containing the Hand Cards.
	 * @param numberOfDraws	An Int type containing the number of cards to be drawn from the Talon.
	 * @param foundation 
	 * @param piles 
	 */
	private void drawCards(List<Card> talon, List<Card> hand, int numberOfDraws, Map<Integer, Deck> piles, Map<Integer, Deck> foundation) {
		aceInHand = true;
		deuceInHand = true;
		possibleHandToManoeuvre = true;
		
		if(talon.isEmpty()) {
			return;
		}
		for (int counter = 0; counter < numberOfDraws; counter++) {
			hand.add(0, talon.remove(0));
			if(talon.isEmpty()) {
				break;
			}
		}
		
		String moveDescription = "Draw " + GameManager.numberOfTalonDraw + " card(s) from the Talon.";
		display.display(piles, hand, foundation, moveDescription);
	}

	/**
	 * Move a card.
	 * @param source		A List type containing the card(s) to be move.
	 * @param index			An Integer type containing the index of the first face-up card of the card(s) to be move.
	 * @param destination	A List type containing the card(s) where the card(s) will be moved.
	 */
	private void moveCardsFromManoeuvreToManoeuvre(List<Card> source, Integer index, List<Card> destination) {

		List<Card> removedCards = new ArrayList<>();

		for (int i = index; i < source.size(); i++) {
			removedCards.add(source.remove(i));
			i--;
		}

		if (!source.isEmpty()) {
			int maxSize = source.size();

			Card newFaceUpCard = source.get(maxSize - 1);
			newFaceUpCard.setFace(true);
		}

		destination.addAll(removedCards);
	}

	/**
	 * Move Card from a Manoeuvre pile to appropriate Foundation. 
	 * @param source		A List type containing the Card to be move.
	 * @param index			An Int type containing the index of the Card to be move.
	 * @param destination	A List type containing the Cards of the foundation.
	 */
	private void moveCardManoeuvreToFoundation(List<Card> source, int index, List<Card> destination) {
		destination.add(0, source.remove(index));
		if (!source.isEmpty()) {
			int maxSize = source.size();

			Card newFaceUpCard = source.get(maxSize - 1);
			newFaceUpCard.setFace(true);
		}
	}
	
	/**
	 * Gets the index of the first face-up card of the List of cards.
	 * @param cards	A List type containing the card(s).
	 * @return		An Int type representing the index number of the first face-up card.
	 */
	private int findIndexOfFirstFaceUpCard(List<Card> cards) {
		int index = 0;
		int size = cards.size();
		// find index of face up highest rank in first pile
		for (int counter = 0; counter < size; counter++) {
			if (cards.get(counter).getFace()) {
				index = counter;
				break;
			}
		}
		return index;
	}
	
	/**
	 * Check if a card(s) is possible to be put on another card or card pile.
	 * @param source		A Card type containing the card to be move.
	 * @param destination	A Card type containing the card where the card(s) will be moved.
	 * @return				A Boolean type representing if the card(s) is possible to be move.
	 */
	private boolean isPossible(Card source, Card destination) {
		if (source.getSuit().getCardColor() != destination.getSuit().getCardColor()) {
			int sourceValue = source.getRank().getRankValue();
			int destinationValue = destination.getRank().getRankValue();
			if (destinationValue == sourceValue + 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
