package com.me.FishGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

// this the first class that runs the program
public class FishGame extends Game {
	Game game;


	@Override
	public void create() 
	{	
		game=this;
		setScreen(new MainMenue(game));

	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public void render()
	{	Gdx.graphics.setTitle("Fish Police!");
	super.render();

	}

	@Override
	public void resize(int width, int height) 
	{
		super.resize(width, height);
	}

	@Override
	public void pause() 
	{
		super.pause();
	}

	@Override
	public void resume() 
	{
		super.resume();
	}
}
