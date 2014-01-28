package com.me.FishGame;

//Fatima
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


public class MainMenue implements Screen {
	Stage stage;
    Label label;
    LabelStyle style;
    BitmapFont font;
    TextureAtlas buttonAtlas;
    TextButtonStyle buttonStyle;
    TextButton button;
    Skin skin;
    SpriteBatch batch;
    Game game;
    Music music;
    Sound sound;
    String dwnload_file_path,dest_file_path;
    public static FileHandle scan;
    private Texture splashTexture;
    
  
    public static String versionNum = "3";
    
    public MainMenue(Game game)
    {
    	this.game=game;
    }
    
    
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(splashTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
	    stage.act(); // to update 
	   
	}
    public static FileHandle returnScan()
    {
    	return scan;
    }
	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}
	
	// this method to download file script from the server 
	public void downloadFile(String url, String dest_file_path) 
	{
        try {
          
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());
            
            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();
             scan = Gdx.files.external(dest_file_path);
           
            System.out.println( Gdx.files.getExternalStoragePath());
            scan.writeBytes(buffer,false);
            
          
             
        } catch(Exception e) {
          System.out.println(e);
            return; 
        }
  }
	 
	@Override
	// this will call only one  
	public void show() 
	{
		Texture.setEnforcePotImages(false);
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_1388666174820.txt";
	    dest_file_path = "scriptv3_1388666174820.txt";
	    splashTexture =new Texture(Gdx.files.internal("images/backmenue.png"));
	    downloadFile(dwnload_file_path,dest_file_path);
	    stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;
	    
		font= new BitmapFont(Gdx.files.internal("font.fnt"),
				 false);
		 style=new LabelStyle(font,Color.BLACK);
		 
		 
		 skin=new Skin();
		 buttonAtlas=new TextureAtlas("buttons/button.pack");
		
		 skin.addRegions(buttonAtlas);
		 buttonStyle=new TextButtonStyle();
		 buttonStyle.up=skin.getDrawable("button");
		 buttonStyle.over=skin.getDrawable("pressedbutton");
		 buttonStyle.down=skin.getDrawable("pressedbutton");
		 buttonStyle.font=font;
		 button=new TextButton("play",buttonStyle);
		 stage.addActor(button);
		 Gdx.input.setInputProcessor(stage); // to know if mouse over or not 
		 button.addListener(new InputListener(){
             @Override
             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            	  
            	   
                   game.setScreen(new GameView(game)); 
                     return true;
             }
     });
	 button.setWidth(Gdx.graphics.getWidth()/3);
	 button.setHeight(Gdx.graphics.getHeight()/6);
	 button.setPosition(Gdx.graphics.getWidth()/2-button.getWidth()/2,0);
	 batch=new SpriteBatch();		
		    
		
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void dispose() {
		
		
	}
	

}
