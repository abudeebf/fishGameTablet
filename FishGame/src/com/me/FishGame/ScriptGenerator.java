package com.me.FishGame;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class will generate a script based on specifications from the
 * experimenter. For now we just look at min/max spawning intervals.
 * 
 * @author tim
 * 
 */

// Refactor: this has not be refactored yet


public class ScriptGenerator {

//	private BufferedWriter scriptFile;
	private Random rand = new Random();
	public final static String SEP = "\t";
	public int fishNum = 0;
	public String scriptname;
	public int mode = 0;

	public ArrayList<Trial> trials = new ArrayList<Trial>();

	public ScriptGenerator() {
		this(makeScriptFilename());
	}

	public ScriptGenerator(String scriptname) {
		this.scriptname = scriptname;
	}

	private static String makeScriptFilename() {
		return "scripts/scriptv3_" + System.currentTimeMillis();
	}

	/**
	 * the user can specify what to use for the separator for script values
	 */
	public String sep = "\t";

	/**
	 * this generates N script events where the inter-event interval is between
	 * min/10 and max/10 seconds and is expressed in milliseconds
	 * 
	 * @param N
	 * @throws IOException 
	 */
	public void generate(GameSpec g) throws IOException {
//		try {
//			if (this.scriptname == null) {
//				this.scriptname = makeScriptFilename();
//			}
//			if (this.scriptFile == null) {
//				try {
//					this.scriptname="script3"; // i add it
//					this.scriptFile = new BufferedWriter(new FileWriter(
//							new File(this.scriptname)));
//
//					//this.scriptFile.write("-1" + sep + "version" + sep
//
//					//+ MainMenue + "\n");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					System.out.println("Error Opening Script File");
//					e.printStackTrace();
//				}
	//}

			//scriptFile.write(g.toScript());

			mode = g.avmode;

			// generate good, congruent trials
			for (int i = 0; i < g.numCon / 2; i++) {

				trials.add(new Trial(getInterval(g.interval), g.good.soundFile,
						g.good.throbRate, 0, (rand.nextInt(2) == 1),
						Species.good));

			}
			// congruent bad fishes
			for (int i = 0; i < g.numCon / 2; i++) {

				trials.add(new Trial(getInterval(g.interval), g.bad.soundFile,
						g.bad.throbRate, 0, (rand.nextInt(2) == 1), Species.bad));

			}
			// incongruent bad fishes
			for (int i = 0; i < g.numIncon / 2; i++) {
				if (mode == 0) {
					trials.add(new Trial(getInterval(g.interval),
							g.good.soundFile, g.bad.throbRate, 1, (rand
									.nextInt(2) == 1), Species.bad));
				} else if (mode == 1) {
					trials.add(new Trial(getInterval(g.interval),
							g.bad.soundFile, g.good.throbRate, 1, (rand
									.nextInt(2) == 1), Species.bad));
				}
			}
			// incongruent good fishes

			for (int i = 0; i < g.numIncon / 2; i++) {
				if (mode == 0) {
					trials.add(new Trial(getInterval(g.interval),
							g.bad.soundFile, g.good.throbRate, 1, (rand
									.nextInt(2) == 1), Species.good));
				} else if (mode == 1) {
					trials.add(new Trial(getInterval(g.interval),
							g.good.soundFile, g.bad.throbRate, 1, (rand
									.nextInt(2) == 1), Species.good));
				}
			}
			// good non-modulated fish
			for (int i = 0; i < g.numNeutral / 2; i++) {
				if (mode == 0) {
					trials.add(new Trial(getInterval(g.interval),
							g.nonModulatedSound , g.good.throbRate, 2, (rand
									.nextInt(2) == 1), Species.good));
				} else if (mode == 1) {
					trials.add(i, new Trial(getInterval(g.interval),
							g.good.soundFile, 0, 2, (rand.nextInt(2) == 1),
							Species.good));
				}
			}
			// bad non-modulated fish
			for (int i = 0; i < g.numNeutral / 2; i++) {
				if (mode == 0) {
					trials.add(new Trial(getInterval(g.interval),
							g.nonModulatedSound, g.bad.throbRate, 2, (rand
									.nextInt(2) == 1), Species.bad));
				} else if (mode == 1) {
					trials.add(i, new Trial(getInterval(g.interval),
							g.bad.soundFile, 0, 2, (rand.nextInt(2) == 1),
							Species.bad));
				}
			}

			// shuffle

			Collections.shuffle(trials);
			for (int j = 0; j < trials.size(); j++) {
				trials.get(j).trial = j + 1; // this stores the fish number inside the trial, starting with fish 1

				System.out.print(trials.get(j).toScriptString());
			//	scriptFile.write(trials.get(j).toScriptString());

			}
		

	}

	// chooses randomly inbetween min fish release and max
	public Long getInterval(int[] interval) {
		int index = rand.nextInt(3);

		long m = interval[index] * 100;
		return m;
	}

	public void close() {
	
		
			scriptname = null;
		
	}

	public static void main(String[] args) throws IOException {
		GameSpec g = new GameSpec();
		ScriptGenerator gs = new ScriptGenerator();
		gs.generate(g);
		gs.close();
	}

}
