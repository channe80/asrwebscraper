package com.as.app.trademe.beans;

public enum ListingDuration {

	EndDate(0), 
			Two(2),
			Three(3), 
			Four(4), 
			Five(5), 
			Six(6), 
			Seven(7), 
			Ten(10), 
			Fourteen(14), 
			TwentyOne(21), 
			TwentyEight(28), 
			Thirty(30), 
			FortyTwo(42), 
			FiftySix(56), 
			Ninety(90); 
	
	private int duration;
	ListingDuration(int duration) {
		this.duration=duration;
	}
	
	public int duration() {
		return this.duration;
	}
}
