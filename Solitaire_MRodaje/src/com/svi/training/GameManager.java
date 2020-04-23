package com.svi.training;

import java.util.Scanner;

public class GameManager {
	private Scanner input = new Scanner(System.in);
	
	public int inputNumberOfGames() {
		int games = 1;
		boolean isValid;
		
		do {
			System.out.print("Input desired number of games: ");
			if (input.hasNextInt()) {
				games = input.nextInt();
				if (games <= 0) {
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
		return games;
	}

	public int inputNumberOfDraw() {
		int draws = 1;
		boolean isValid;
		do {
			System.out.println("Choose Number of Cards to Draw from the Talon:");
			System.out.println("(1) 1 card draw only from the talon");
			System.out.println("(2) 3 cards draw from the talon");
			System.out.print("Choice: ");;
			if (input.hasNextInt()) {
				draws = input.nextInt();
				if (draws != 1 && draws != 2) {
					System.out.println("Input 1 or 2 only!");
					isValid = false;
					input.nextLine();
				} else if (draws == 2) {
					draws++;
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
		return draws;
	}

	public boolean yesOrNo(String string) {
		boolean isShow = false, isValid;
		String choice;
		do {
			System.out.print(string);
			if (input.hasNext()) {
				choice = input.next();
				switch (choice.toUpperCase()) {
				case "Y":
					return true;
				case "N":
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

}