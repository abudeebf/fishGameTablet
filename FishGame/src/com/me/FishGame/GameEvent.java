package com.me.FishGame;




/**
 * This class records game events which can be logged The events of interest are
 * generally keypresses after a fish is released, but sometimes there will be no
 * keypress for a fish and this is indicated using a 0 for the keyCharacter The
 * elements of interest are the response time
 * 
 * @author tim
 * 
 */

// REFACTOR:  this hasn't been refactored yet

public class GameEvent {

	public static String sep = "\t";

	public String eventType = "none";

	/** the time the event occurred from System.nanoTime() **/
	public long when = System.nanoTime();

	/** the key pressed, use 0 for no key pressed **/
	public float accelerometer = 0;

	/**
	 * response time in milliseconds if key is pressed with no fish or fish is
	 * missed, this is 0
	 */
	public int responseTime = 0;

	/** the fish itself, possibly null if a key was pressed for no fish **/
	public Fish fish = null;

	/** the fish species **/
	public Species species = Species.none;

	public int congruent;

	/** thie fish side **/
	public String side = "none";

	/** whether the response was correct or not **/
	public boolean correctResponse = false;

	/** when the fish was released, 0 for no fish **/
	public long fishRelease = 0;

	/** when the key was pressed, 0 for no key press **/
	public long keyPress = 0;

	// converts from System.nanoTime units to milliseconds from game start
	@SuppressWarnings("unused")
	private int convertNanoToMSfromGS(long t) {
		int ms = (int) Math.round((t - Fish.GAME_START) / 1000000.0);
		return ms;
	}

	/**
	 * this is called for other events
	 */
	public GameEvent(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * this creates an event for a missed fish
	 * 
	 * @param keyPressed
	 */
	public GameEvent(float accelermeter) {
		this.eventType = "keypress  ";
		this.when = System.nanoTime();
		this.accelerometer = accelermeter;
		this.responseTime = 0;
		this.fish = null;
		this.species = Species.none;
		this.side = "none";
		this.correctResponse = false;
		this.fishRelease = this.when;
		this.keyPress = this.when;
	}

	/**
	 * this is called when a fish is missed, i.e. the user didn't press a key
	 * 
	 * @param fish
	 */
	public GameEvent(Fish fish) {
		this.eventType = "missedfish";
		this.when = fish.lastUpdate;
		this.accelerometer = 0;
		this.responseTime = 0;
		this.fish = fish;
		this.species = fish.species;

		this.congruent = fish.congruent;

		this.side = (fish.fromLeft) ? "left" : "right";
		this.correctResponse = false;
		this.fishRelease = fish.birthTime;
		this.keyPress = this.when;
	}

	/**
	 * this is used when the user responds to a fish while it is on the screen
	 * 
	 * @param keyPressed
	 * @param fish
	 */
	public GameEvent(float x, Fish fish,long when) {
		this.eventType = "hitfish   ";
		this.when = when; // this.when is System time in nanosecs
		this.accelerometer =x;
		this.fish = fish;
		this.species = fish.species;

		this.congruent = fish.congruent;
		this.side = (fish.fromLeft) ? "left" : "right";
		this.fishRelease = fish.birthTime;
		this.correctResponse = hitCorrectTilting(x, fish);
		this.keyPress = this.when;
		this.responseTime = (int) Math
				.round((this.when - this.fishRelease) / 1000000.0);
	}

	/**
	 * returns true if the key press was correct
	 * 
	 * @param c
	 * @param lastFish
	 * @return
	 */
	private boolean hitCorrectTilting(float c, Fish lastFish) {
		Species s = lastFish.species;
		if (s == Species.good)
			return c < -3;
		else
			return c > 3;
	}

	public String toString() {
		String response =
		// convertNanoToMSfromGS(this.when) + sep +
		this.eventType + sep + this.responseTime + sep + this.correctResponse
				+ sep + this.accelerometer + sep
		// + convertNanoToMSfromGS(this.fishRelease) + sep
		// + this.when + sep
		// + convertNanoToMSfromGS(this.keyPress) + sep;
		;
		return response;
	}
}
