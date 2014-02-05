package com.me.FishGame;

//Fatima
import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;

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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
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
    TextButton audioToggle,visualToggle;
    CheckBoxStyle checkStyle;
    Skin skin;
    SpriteBatch batch;
    Game game;
    Music music;
    Sound sound;
    String dwnload_file_path,dest_file_path;
    boolean audioGame;
    public static FileHandle scan;
    public static FileHandle audioFileHandle,visualFileHandle;
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
    public FileHandle returnScan()
    {
    	if(audioGame) {
    		return audioFileHandle;
    	} else {
    		return visualFileHandle;
    	}
    	
    }
	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}
	
	// this method to download file script from the server 
	public FileHandle downloadFile(String url, String dest_file_path) 
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
            
          
            return scan;
        } catch(Exception e) {
          System.out.println(e);
            return null; 
        }
  }
	 
	@Override
	// this will call only one  
	public void show() 
	{
		Texture.setEnforcePotImages(false);
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_Audio.txt";
	    dest_file_path = "scriptv3_Audio.txt";
	    audioFileHandle = downloadFile(dwnload_file_path,dest_file_path);
	    
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_visual.txt";
	    dest_file_path = "scriptv3_visual.txt";
	    visualFileHandle = downloadFile(dwnload_file_path,dest_file_path);
	    splashTexture =new Texture(Gdx.files.internal("images/backmenue.png"));
	    
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
		 
		 
		 button=new TextButton("Play Random",buttonStyle);
		 stage.addActor(button);
		 Gdx.input.setInputProcessor(stage); // to know if mouse over or not 
		 button.addListener(new InputListener(){
             @Override
             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            	  
            	   if(Math.random()<.5) {
            		   audioGame = true;
            	   } else {
            		   audioGame = false;
            	   }
                   game.setScreen(new GameView(game,audioGame?audioFileHandle:visualFileHandle)); 
                     return true;
             }
     });
		 
	 button.setWidth(Gdx.graphics.getWidth()/3);
	 button.setHeight(Gdx.graphics.getHeight()/6);
	 button.setPosition(Gdx.graphics.getWidth()/2-button.getWidth()/2,0);
	 
	 audioToggle = new TextButton("Play Audio",buttonStyle);
	 stage.addActor(audioToggle);
	 audioToggle.addListener(new InputListener(){
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
        	  
        	   
               	
               	audioGame = true;
               	game.setScreen(new GameView(game,audioFileHandle)); 
                 return true;
         }
 });
	 audioToggle.setWidth(Gdx.graphics.getWidth()/4);
	 audioToggle.setHeight(Gdx.graphics.getHeight()/6);
	 audioToggle.setPosition(Gdx.graphics.getWidth()/2-button.getWidth()/2-audioToggle.getWidth()-20,0);
	 
	 
	 visualToggle = new TextButton("Play Visual",buttonStyle);
	 stage.addActor(visualToggle);
	 visualToggle.addListener(new InputListener(){
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
        	  
        	   
        	 
            	audioGame = false;
            	game.setScreen(new GameView(game,visualFileHandle)); 
                 return true;
         }
 });
	 visualToggle.setWidth(Gdx.graphics.getWidth()/4);
	 visualToggle.setHeight(Gdx.graphics.getHeight()/6);
	 visualToggle.setPosition(Gdx.graphics.getWidth()/2+button.getWidth()/2+20,0);
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
