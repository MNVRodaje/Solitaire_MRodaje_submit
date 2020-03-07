package com.svi.enums;

public enum Suit {
	D(4, "RED"), H(3, "RED"), S(2, "BLACK"), C(1, "BLACK");
	
	private Integer suit;
	private String color;
	
	private Suit(Integer suit, String color) {
		this.suit = suit;
		this.color = color;
	}

	/** Gets the Suit character of the Card.
	 * @return An Integer type representing the suit
	 * value of the Card.
	*/
	public Integer getSuitValue() {
		return suit;
	}
	
	/**
	 * Gets the Card Color of the specified Card Suit.
	 * @return A String type representing the Card Color.
	 */
	public String getCardColor() {
		return color;
	}
}