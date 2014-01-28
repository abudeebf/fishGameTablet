package com.me.FishGame;



/**
 * A Trial is a Fish Event, e.g. Good Fish from Right Bad Fish from Left and it
 * has a fixed amount of time. In fMRI mode, the previous trial ends with the
 * fish disappearing from the screen The new trial begins with no fish, and the
 * after an inter-fish interval the fish appears
 * 
 * @author tim
 * 
 */

// REFACTOR: this hasn't been refactored yet.
// we may want to use Trial to read in the script file into an array of Trials
// rather than reading each line during the experiment. It might make the code
// cleaner...

public class Trial {
	/**
	 * an ArrayList storing the properties of the trial
	 */
	public Long interval;
	public String soundFile;
	public int visualHz;
	// congruent = 0, incongruent = 1, silent = 3
	public int congruent;
	public int trial;
	public boolean fromLeft;
	public Species spec;

	/**
	 * 
	 * @param interval
	 *            time in nanoseconds from beginning of the trial
	 * @param soundFile
	 *            name of the sound file in the sounds/ folder
	 * @param visualHz
	 *            the throb rate
	 * @param congruent
	 *            0 if visual/audio are congruent
	 *            1 if they are incongruent
	 *            2 if the not-attended-to sense is non-modulated (depends on avmode)
	 * @param fromLeft
	 *            true if fish comes from the left
	 * @param spec
	 *            the Species of fish (i.e. good or bad) correlated with the
	 *            visualHz field sort of ...
	 */
	public Trial(Long interval, String soundFile, int visualHz, int congruent,
			Boolean fromLeft, Species spec) {
		this.interval = interval;
		this.soundFile = soundFile;
		this.visualHz = visualHz;
		this.congruent = congruent;
		this.fromLeft = fromLeft;
		this.spec = spec;
	}

	public String toScriptString() {
		return interval.toString() + " " + soundFile + " " + visualHz + " "
				+ congruent + " " + trial + " " + (fromLeft ? "left" : "right")
				+ " " + spec.toString() + "\n";

	}
}
