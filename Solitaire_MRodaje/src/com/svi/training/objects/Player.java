package com.svi.training.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.svi.training.GameManager;

public class Player {
	private int desiredNumberOfGames = 0, gameNumber = 0, desiredNumberOfDraw = 1, numberOfWins = 0; 
	private boolean shuffleDeck = false, checkManoeuvre = true, checkHand = true, mustDraw = true, repeatTalon = true, hasNotMoved = false, win = false;
	Table table = new Table();
	
	private int indexOfFirstFaceUpCard(LinkedList<Card> sourcePile) {
		for (Card card: sourcePile) {
			if (card.getFace()) {
				return sourcePile.indexOf(card);
			}
		}
		return 0;
	}

	private void checkAndMoveAce() {
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.isEmpty()) {
				for (LinkedList<Card> pile: Table.manoeuvrePileMap.values()) {
					if (pile.isEmpty()) {
						continue;
					}
					if (pile.getLast().getRank().ordinal() == 0) {
						Table.moveDescription = "Moved " + pile.getLast().toCardString() + " to Foundation.";
						foundation.add(pile.removeLast());
						if (!pile.isEmpty()) {
							pile.getLast().setFace(true);
						}
						checkManoeuvre = true; 
						mustDraw = true;
						repeatTalon = true;
						hasNotMoved = false;
						table.showTable(gameNumber);
						break;
					}
				}
			}
		}
	}

	private void checkAndMoveDeuce() {
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.size() == 1) {
				for (LinkedList<Card> pile: Table.manoeuvrePileMap.values()) {
					if (pile.isEmpty()) {
						continue;
					}
					if (pile.getLast().getRank().ordinal() == 1) {
						if (pile.getLast().getSuit() == foundation.getLast().getSuit()) {
							Table.moveDescription = "Moved " + pile.getLast().toCardString() + " to Foundation.";
							foundation.add(pile.removeLast());
							if (!pile.isEmpty()) {
								pile.getLast().setFace(true);
							}
							checkManoeuvre = true;
							mustDraw = true;
							repeatTalon = true;
							hasNotMoved = false;
							table.showTable(gameNumber);
							break;
						}
					}
				}
			}
		}
	}

	private void checkAndMoveManoeuvrePiles() {
		int sourceIndex = -1;
		outer:
		for (LinkedList<Card> sourcePile: Table.manoeuvrePileMap.values()) {
			sourceIndex++;
			int destinationIndex = -1;
			if (sourcePile.isEmpty()) {
				continue;
			}
			for (LinkedList<Card> destinationPile: Table.manoeuvrePileMap.values()) {
				destinationIndex++;
				if (destinationPile.isEmpty()) {
					continue;
				}
				if (sourceIndex == destinationIndex) {
					continue;
				}
				int index = indexOfFirstFaceUpCard(sourcePile);
				if (sourcePile.get(index).getRank().ordinal() == destinationPile.getLast().getRank().ordinal() - 1) {
					if (sourcePile.get(index).getcardColor() != destinationPile.getLast().getcardColor()) {
						Table.moveDescription = "Moved " + sourcePile.get(index).toCardString() + " to " + destinationPile.getLast().toCardString();
						List<Card> toBeAdd = new ArrayList<Card>();
						toBeAdd = sourcePile.subList(index, sourcePile.size());
						destinationPile.addAll(toBeAdd);
						toBeAdd.clear();
						if (!sourcePile.isEmpty()) {
							sourcePile.getLast().setFace(true);
						}
						checkManoeuvre = true;
						mustDraw = true;
						repeatTalon = true;
						hasNotMoved = false;
						table.showTable(gameNumber);
						break outer;
					}
				}
			}
		}
	}

	private void checkAndMoveKing() {
		int destinationIndex = -1;
		outer:
		for (LinkedList<Card> destinationPile: Table.manoeuvrePileMap.values()) {
			destinationIndex++;
			int sourceIndex = -1;
			if (!destinationPile.isEmpty()) {
				continue;
			}
			for (LinkedList<Card> sourcePile: Table.manoeuvrePileMap.values()) {
				sourceIndex++;
				if (sourcePile.isEmpty() || sourceIndex == destinationIndex || sourcePile.peekFirst().getFace()) {
					continue;
				}
				int index = indexOfFirstFaceUpCard(sourcePile);
				if (sourcePile.get(index).getRank().ordinal() == 12) {
					Table.moveDescription = "Moved " + sourcePile.get(index).toCardString() + " to Empty Manoeuvre Pile.";
					List<Card> toBeAdd = new ArrayList<Card>();
					toBeAdd = sourcePile.subList(index, sourcePile.size());
					destinationPile.addAll(toBeAdd);
					toBeAdd.clear();;
					if (!sourcePile.isEmpty()) {
						sourcePile.getLast().setFace(true);
					}
					checkManoeuvre = true;
					mustDraw = true;
					repeatTalon = true;
					hasNotMoved = false;
					table.showTable(gameNumber);
					break outer;
				}
			}
		}
	}

	private void aceInHand() {
		if (Table.hand.isEmpty()) {
			return;
		}
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.isEmpty() && Table.hand.peek().getRank().ordinal() == 0) {
				Table.moveDescription = "Moved " + Table.hand.peek().toCardString() + " to an Empty Foundation.";
				foundation.add(Table.hand.remove(0));
				table.showTable(gameNumber);
				checkHand = true;
				mustDraw = true;
				repeatTalon = true;
				hasNotMoved = false;
				break;
			}
		}
	}

	private void deuceInHand() {
		if (Table.hand.isEmpty()) {
			return;
		}
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.size() == 1 
				&& foundation.peek().getRank().ordinal() == Table.hand.peek().getRank().ordinal() - 1 
				&& foundation.peek().getSuit()==Table.hand.peek().getSuit()) {
				
				Table.moveDescription = "Moved " + Table.hand.peek().toCardString() + " to Foundation";
				foundation.add(Table.hand.remove(0));
				table.showTable(gameNumber);
				checkHand = true;
				mustDraw = true;
				repeatTalon = true;
				hasNotMoved = false;
				break;
			}
		}
	}

	private void handToManoeuvre() {
		for (LinkedList<Card> pile: Table.manoeuvrePileMap.values()) {
			if (Table.hand.isEmpty()) {
				return;
			}
			if (!pile.isEmpty()) {
				if (pile.peekLast().getRank().ordinal() == Table.hand.peek().getRank().ordinal() + 1
						&& pile.peekLast().getcardColor() != Table.hand.peek().getcardColor()) {
					Table.moveDescription = "Moved " + Table.hand.peek().toCardString() + " from hand to " + pile.peekLast().toCardString();
					pile.add(Table.hand.remove(0));
					table.showTable(gameNumber);
					checkHand = true;
					mustDraw = true;
					repeatTalon = true;
					hasNotMoved = false;
					break;
				}
			} else {
				if (Table.hand.peek().getRank().ordinal() == 12) {
					Table.moveDescription = "Moved " + Table.hand.peek().toCardString() + " from hand to Empty Pile.";
					pile.add(Table.hand.remove(0));
					table.showTable(gameNumber);
					checkHand = true;
					mustDraw = true;
					repeatTalon = true;
					hasNotMoved = false;
					break;
				}
			}
		}
	}

	private void cardToFoundation() {
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.isEmpty()) {
				continue;
			}
			for (LinkedList<Card> pile: Table.manoeuvrePileMap.values()) {
				if (pile.isEmpty()) {
					continue;
				}
				if (foundation.peekLast().getSuit() == pile.peekLast().getSuit()
						&& foundation.peekLast().getRank().ordinal() == pile.peekLast().getRank().ordinal() - 1) {
					Table.moveDescription = "Moved " + pile.peekLast().toCardString() + " to Foundation.";
					foundation.add(pile.removeLast());
					if (!pile.isEmpty()) {
						pile.getLast().setFace(true);
					}
					checkManoeuvre = true; 
					mustDraw = true;
					repeatTalon = true;
					hasNotMoved = false;
					table.showTable(gameNumber);
					break;
				}
			}
			if (Table.hand.isEmpty()) {
				continue;
			}
			if (foundation.peekLast().getSuit() == Table.hand.peek().getSuit()
					&& foundation.peekLast().getRank().ordinal() == Table.hand.peek().getRank().ordinal() - 1) {
				Table.moveDescription = "Moved " + Table.hand.peek().toCardString() + " from Hand to an Foundation.";
				foundation.add(Table.hand.remove(0));
				table.showTable(gameNumber);
				checkHand = true;
				mustDraw = true;
				repeatTalon = true;
				hasNotMoved = false;
				break;
			}
		}
	}

	private void drawCards() {
		for (int counter = 0; counter < desiredNumberOfDraw; counter++) {
			if (Table.talon.isEmpty()) {
				break;
			}
			Table.hand.push(Table.talon.pop());
		}
		Table.moveDescription = "Draw from talon.";
		table.showTable(gameNumber);
		repeatTalon = false;
	}

	private void strategy() {
		do {
			hasNotMoved = true;
			do {
				mustDraw = false;
				do {
					checkHand = false;
					do {
						checkManoeuvre = false;
						checkAndMoveAce();
						checkAndMoveDeuce();
						checkAndMoveManoeuvrePiles();
						checkAndMoveKing();
					} while (checkManoeuvre);
					if (Table.hand.isEmpty()) {
						break;
					}
					if (Table.hand.isEmpty()) {
						break;
					}
					aceInHand();
					deuceInHand();
					handToManoeuvre();
				}while(checkHand);
				if (Table.talon.isEmpty()) {
					break;
				} else {
					mustDraw = true;
				}
				drawCards();
			} while (mustDraw);
			cardToFoundation();
			if (Table.talon.isEmpty()) {
				if (Table.hand.isEmpty()) {
					if (hasNotMoved) {
						break;
					} else {
						repeatTalon = true;
						mustDraw = false;
						checkHand = false;
						checkManoeuvre = false;
					}
				} else {
					Table.talon.addAll(Table.hand);
					Table.hand.clear();
					Table.moveDescription = "Return Hand Cards to Talon.";
					table.showTable(gameNumber);
					repeatTalon = true;
					mustDraw = false;
					checkHand = false;
					checkManoeuvre = false;
				}
			}
		} while (repeatTalon && !hasNotMoved);
		int completeFoundation = 0;
		for (LinkedList<Card> foundation: Table.foundationPileMap.values()) {
			if (foundation.size() == 13) {
				completeFoundation++;
			}
		}
		if (completeFoundation == 4) {
			win = true;
			table.showResult(win);
			numberOfWins++;
		} else {
			win = false;
			table.showResult(win);
		}
	}

	public void playSolitaire() {
		GameManager gameManager = new GameManager();
		
		desiredNumberOfGames = gameManager.inputNumberOfGames();
		desiredNumberOfDraw = gameManager.inputNumberOfDraw();
		table.setNumberOfDraw(desiredNumberOfDraw);
		shuffleDeck = gameManager.yesOrNo("Shuffle the Deck? (Y/y for YES, N/n for NO): ");
		table.setMustShowCards(gameManager.yesOrNo("Show supposed HIDDEN CARDS? (Y/y for YES, N/n for NO): "));
		
		for (int counter = 1; counter <= desiredNumberOfGames; counter++) {
			gameNumber = counter;
			Deck deck = new Deck();
			List<Card> mainDeck = deck.shuffleDeck(shuffleDeck);
			table.clearTable();
			table.distributeCards(mainDeck);
			table.showTable(gameNumber);
			strategy();
		}
		
		float winRate = ((float)numberOfWins / desiredNumberOfGames) * 100;
		System.out.println("The total Win Rate is " + winRate + "%.");
	}

}