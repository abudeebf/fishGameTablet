package com.me.FishGame;

import com.badlogic.gdx.audio.Sound;




/**
 * a Fish has a position and a velocity and a speed they also have a
 * species and they keep track of whether they are active or not.
 * This class contains public fields and an update method...
 * 
 * This class should contain all the information about the fish which is
 * need to draw the fish on the screen and control its audio clip
 * 
 * @author tim
 * 
 */
public class Fish {
	/*
	 * REFACTOR -- this code needs to be refactored more ...
	 */
	public static long GAME_START = 0; // System.nanoTime();

	// these make it easier to avoid errors converting among ms,ns, and sec
	public static final double billionD = 1000000000.0;
	public static final long millionL = 1000000L;


	/**
	 * the default constructor for a fish
	 */
	public Fish() {
		// create a new fish
	}


	/*************************************************************************
	 * Below are all of the instance variables that describe the state of the Fish
	 * These are used by the model to update its position and by the View
	 * to draw it on the screen.
	 * *************************************************************************
	 */
	/**
	 * this is the inter-fish-interval in milliseconds
	 * i.e. the delay between the death of the last fish and the birth of this fish
	 * the death of a fish, it the beginning of the trial,
	 * so this is also the launch time relative the the beginning of the trial
	 */
	public long interval;

	
	/**
	 * x position of the fish in the 100x100 GameModel space
	 */
	public double x;
	
	/**
	 * y position of the fish in the 100x100 GameModel space
	 */
	public double y;
	
	/**
	 *  x velocity of the fish in model units per second
	 *  so 100 would take it all the way across the screen in 1 second
	 */
	 
	public double vx;
	
	/**
	 *  y velocity of the fish in model units per second
	 *  so 100 would take it all the way across the screen in 1 second
	 */
	public double vy;
	
	/**
	 * true if the fish has not been removed from the game
	 */
	public boolean active;



	/**
	 * speed of the fish in model units per second (screen is 100x100 model units)
	 */
	public double speed = 40;
	
	/**
	 * true if the fish is launched from the left (rather than the right)
	 */
	public boolean fromLeft; 
	
	/**
	 * true if the fish audio and video cues have the same oscillation frequency
	 * 0=congruent,  1=incongruent, 2=noaudio, 3=novideo
	 */
	public int congruent;
	
	/**
	 * the trial number (starting at 1)
	 */
	public int trial;
	
	/**
	 * the System time, in nanoseconds, that the fish was born
	 */
	long birthTime;
	
	/**
	 * the System time, in nanoseconds, that the fish was last updated
	 * this is used to calculate dt in the update formula position += dt*velocity
	 */
	long lastUpdate;
	
	
	/**
	 *  this is the time it stays on screen, in milliseconds of a second
	 *  REFACTOR: this should be stored in the GameSpec and read from there....
	 */
	public static double maxTimeOnScreen = 2000;
	
	/**
	 * this is used in GameModel to update fields that are used in GameView
	 * REFACTOR:  this shouldn't be here!!
	 */
	public long lifeSpan;


	/**
	 * the sound clip that should be played when the fish is launched
	 */
	//public AudioClip ct;
	public Sound ct;

	/**
	 * the type of fish - good, bad, ..
	 */
	public Species species;

	/** 
	 * determines whether visual(0) or audio(1) specifies good or bad 
	 * 
	 */
	// REFACTOR
	// this should be read from the GameSpec as it doesn't change from fish to fish...

	public int avmode = 0;

	

	public java.util.Random rand = new java.util.Random();

	/**
	 * print the public fields for the fish
	 */
	public String toString() {
		return "fish[x=" + x + ",y=" + y + ",vx=" + vx + ",vy=" + vy + 
				",active=" + active + ",fromLeft="
				+ fromLeft + ",congruent=" + congruent + 
				",trial=" + trial + ",birthtime=" + birthTime
				+ ",lastUpdate=" + lastUpdate + ",lifeSpan="  + lifeSpan + "]";

	}

	/**
	 * actors change their velocity slightly at every step but their speed
	 * remains the same. Update slightly modifies their velocity and uses that
	 * to compute their new position. Note that velocity is in model units per second.
	 * 
	 */
	public void update() {
		long now = System.nanoTime();
		// REFACTOR: create and use a field deathTime in Fish class
		// so we don't have to recompute it every time in this "if"
		// and so it is clearer...

		if (now < birthTime + maxTimeOnScreen * millionL) {
			this.lifeSpan = now - birthTime;
			double dt = (now - this.lastUpdate) / billionD;

			// this is the rate at which the y-velocity changes per frame
			double turnspeed = 0.1;

			vy += (rand.nextDouble() - 0.5) * turnspeed;

			if (vy > speed / 2)
				vy = speed / 2;
			if (vy < -speed / 2)
				vy = -speed / 2;


			double tmpSpeed = Math.sqrt(vx * vx + vy * vy);

			if (tmpSpeed < 0.1)
				tmpSpeed = 0.1;

			vx /= tmpSpeed;
			vy /= tmpSpeed;

			double dx = vx * speed * dt;
			double dy = vy * speed * dt;

			x += dx;
			y += dy;

			this.lastUpdate = now;
		} else {
			this.active = false;
			if (congruent != 2)
				this.ct.stop();
		}



	}



}
