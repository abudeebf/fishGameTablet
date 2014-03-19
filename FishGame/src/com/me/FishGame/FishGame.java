package com.me.FishGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

// this the first class that runs the program
public class FishGame extends Game {
	Game game;
    String init,age,mode;
    public FishGame(){
    	
    }
   public FishGame(String init, String age , String mode){
	  this. init=init;
	  this. age=age;
	 this.  mode=mode;
	 game=this;
	setScreen(new MainMenue(game,init,age, mode));

   }
	@Override
	public void create() 
	{	
		
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public void render()
	{	
		Gdx.graphics.setTitle("Fish Police!");
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
