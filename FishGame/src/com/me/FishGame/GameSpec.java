package com.me.FishGame;



/**
 * This class keeps track of all of the state of a game except for the launching
 * of the fish. These parameters will be written to the script file and the log
 * file to make the game play reproducible..
 * 
 * @author tim
 * 
 */

// REFACTOR:  haven't refactored this yet
// in particular, make sure that everything stored here is used in the game
// and that everything in the script file is copied into the GameSpec when read


public class GameSpec {
	public FishSpec good = new FishSpec(), bad = new FishSpec();

	public boolean changed = true;
	public boolean requireGameViewUpdate = true;

	public boolean stereo = true;
	
	/** 
	 * delay inserted before playing clips, in milliseconds
	 * This is used in the EEG mode so that the EEG signal for events
	 * is not confounded by the brain's response to a sound....
	 */
	public long audioDelay = 200;

	/**
	 * this represents the type of experiment being run, whether the correct
	 * response is tied to the visual rate or the auditory rate. 0=visual,
	 * 1=auditory
	 */
	public int avmode = 0;

	public boolean hasAvatar = true;

	public String bgSound = "sounds/water/mid.wav";

	private String sep = ScriptGenerator.SEP;

	public String backgroundImage = "images/streamB.jpg";

	public int numCon = 10;
	public int numIncon = 10;
	public int numNeutral = 10;

	public String goodResponseSound = "sounds/good.wav";
	public String badResponseSound = "sounds/bad.wav";
	public String silentResponseSound = "sounds/fish-silence/fish.wav";
	public String nonModulatedSound = "sounds/2hz/fish.wav";
	// we can expand to more sounds later ...
	/*
	 * public String eatGood = goodSound; public String eatBad = badSound;
	 * public String killGood = badSound; public String killBad = goodSound;
	 * public String missGood = badSound; public String missBad = badSound;
	 * public String pushKey = badSound;
	 */

	public int minFishRelease = 30, maxFishRelease = 60;

	public int minThrobSize = 100, maxThrobSize = 125;
	public int interval[] = { 35, 45, 55 };
	public int minBrightness = 10;
	public int maxBrightness = 14;
	
	public GameSpec() {

	}

	private String scriptLine(String prop, String val) {

		return "-1" + sep + prop + sep + val + "\n";

	}

	public String toScript() {
		String s = "";

		s += scriptLine("stereo", "" + stereo);
		s += scriptLine("minThrobSize", "" + minThrobSize);
		s += scriptLine("maxThrobSize", "" + maxThrobSize);
		s += scriptLine("minBrightness", "" + minBrightness);
		s += scriptLine("maxBrightness", "" + maxBrightness);
		s += scriptLine("bgSound", "" + this.bgSound);
		s += good.toScript("good");
		s += bad.toScript("bad");
		s += scriptLine("backgroundImage", backgroundImage);
		s += scriptLine("goodSound", "" + goodResponseSound);
		s += scriptLine("badSound", "" + badResponseSound);
		s += scriptLine("nonModulatedSound", "" + nonModulatedSound);

		s += scriptLine("totalCongruentTrials", "" + numCon);
		s += scriptLine("totalInCongruentTrials", "" + numIncon);
		s += scriptLine("totalNeutralTrials", "" + numNeutral);
		s += scriptLine("hasAvatar", "" + hasAvatar);
		s += scriptLine("avmode", "" + avmode);
		return (s);

	}

	/**
	 * this will change the value in the specified property, if it exists We
	 * could replace this whole class with a HashMap though.... and maybe we
	 * should!
	 * 
	 * @param prop
	 * @param value
	 * @return
	 */
	public boolean update(String prop, String value) {
		this.changed = true;
		if (prop.equals("backgroundImage")) {
			this.backgroundImage = value;
			this.requireGameViewUpdate = true;
		} else if (prop.equals("maxFishRelease")) {
			this.maxFishRelease = Integer.parseInt(value);
		} else if (prop.equals("minFishRelease")) {
			this.minFishRelease = Integer.parseInt(value);
		} else if (prop.equals("stereo")) {
			this.stereo = "true".equals(value);
		} else if (prop.equals("maxThrobSize")) {
			this.maxThrobSize = Integer.parseInt(value);
		} else if (prop.equals("minThrobSize")) {
			this.minThrobSize = Integer.parseInt(value);
		} else if (prop.startsWith("good")) {
			return this.good.update(prop.substring(4), value);
		} else if (prop.startsWith("bad")) {
			return this.bad.update(prop.substring(3), value);
		} else if (prop.equals("bgSound")) {
			this.bgSound = value;
			this.requireGameViewUpdate = true;
		} else if (prop.equals("minBrightness")) {
			this.minBrightness = Integer.parseInt(value);
		} else if (prop.equals("maxBrightness")) {
			this.maxBrightness = Integer.parseInt(value);
		} else if (prop.equals("hasAvatar")) {
			this.hasAvatar = (value.contains("true") ? true : false);
		} else if (prop.equals("avmode")) {
			this.avmode = Integer.parseInt(value);
		} else if (prop.equals("nonModulatedSound")){
			this.nonModulatedSound = value;
		} else
			return false;
		return true;
	}

}
