package com.me.FishGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {
	GameModel gm;
	float px,py;
	int m, n=0;
	
    public MyGestureListener(GameModel gm){
    	this.gm=gm;
    
    }
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		px=(Gdx.graphics.getWidth() - m) / 2;
    	py=0-n/2;
    	py=(-1)*py;
    
		//Gdx.app.log("fatima", " count yay " +count);
		
		//Gdx.app.log("fatima", " " +x + "," +y + ","+"px+m=" + (px+m/2) + ",py+n="+(n+py) +",py="+py+ ","+ "px" + px);
		 if ( (x>=px && x<=px+m) && (y>=py && y<=(n+py) ))
	    if (count>2){
	    	
	    	this.gm.setGameOver(true);
			return true;
	    }
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		
	
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	 

	
	}