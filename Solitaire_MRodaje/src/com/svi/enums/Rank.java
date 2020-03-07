package com.svi.enums;

public enum Rank {
	_K(13), _Q(12), _J(11), _10(10), 
	_9(9), _8(8), _7(7), _6(6), _5(5), 
	_4(4), _3(3), _2(2), _A(1);

	private Integer rank;

	private Rank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * Gets the Rank character of the Card.
	 * 
	 * @return A string type representing the rank of the Card.
	 */
	public Integer getRankValue() {
		return rank;
	}
}