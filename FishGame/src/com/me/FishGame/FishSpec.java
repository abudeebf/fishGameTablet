package com.me.FishGame;
/**
 * this stores a complete specification of how the fish are to be rendered
 * visually and aurally
 * 
 * @author tim
 * 
 */

// REFACTOR:  this hasn't been refactored yet...

public class FishSpec {
	public String soundFile = "sounds/fish6hz0p";
	public String imageFile = "images/fish1";

	public int throbRate = 3;
	private String sep = ScriptGenerator.SEP;

	public FishSpec() {
		// creates a default fish whose fields we set directly.
	}

	private String scriptLine(String prop, String val) {
		return "-1" + sep + prop + sep + val + "\n";
	}

	public String toScript(String type) {
		String s = "";
		s += scriptLine(type + "soundFile", soundFile);
		s += scriptLine(type + "imageFile", imageFile);
		s += scriptLine(type + "throbRate", "" + throbRate);
		return s;
	}

	public boolean update(String prop, String val) {

		if (prop.equals("soundFile")) {
			this.soundFile = val;
		} else if (prop.equals("imageFile")) {
			this.imageFile = val;
		} else if (prop.equals("throbRate")) {
			throbRate = Integer.parseInt(val);
		} else
			return false;

		return true;
	}

}
