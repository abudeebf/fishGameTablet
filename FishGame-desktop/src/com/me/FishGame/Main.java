package com.me.FishGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Fish Game";
		cfg.useGL20 = false;
		cfg.width = 1080;
		cfg.height = 720;
		cfg.useGL20=false;
				new LwjglApplication(new FishGame(), cfg);
	}
}
