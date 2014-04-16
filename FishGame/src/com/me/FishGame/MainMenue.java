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
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	TextButton fastToggle,slowToggle,StartToggle,QuitToggle,practiseToggle;
	CheckBoxStyle checkStyle;
	Skin skin;
	SpriteBatch batch;
	Game game;
	Music music;
	Sound sound;
	String dwnload_file_path,dest_file_path;
	boolean vGame;
	public  FileHandle scan;
	public  FileHandle audioFileHandle,visualFileHandle, file;
	public String servername="http://moore.cs-i.brandeis.edu/Scripts/";
	private Texture splashTexture;


//	public static String versionNum = "3";
	  String init,age,mode;
	public MainMenue(Game game, String init, String age , String mode)
	{
		
		this.game=game;
		this.init=init;
		this.age=age;
		this.mode=mode;
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(splashTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
		stage.act(); // to update 
		
	}
	public FileHandle returnScan()
	{
		if(mode.equals("Audio")) {
			vGame=false;
			return audioFileHandle;
			
		} else {
			vGame=true;
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
		//dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_1388666174820.txt";
		dest_file_path = "scriptv3_Audio.txt";
		//dest_file_path = "scriptv3_1388666174820.txt";
		audioFileHandle = downloadFile(dwnload_file_path,dest_file_path);
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_visual.txt";
		dest_file_path = "scriptv3_visual.txt";
		visualFileHandle = downloadFile(dwnload_file_path,dest_file_path);
		splashTexture =new Texture(Gdx.files.internal("images/0015.jpg"));
		stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;
		skin=new Skin(Gdx.files.internal("data/uiskin.json"));
		 // to know if mouse over or not 
		Table table=new Table();
		 file=returnScan();
		fastToggle = new TextButton("Try Good Fish",skin);
		stage.addActor(fastToggle);
		fastToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				Player p=new Player(init,vGame,0,Integer.parseInt(age));
				FileHandle script=downloadFile(servername+"tryGood"+mode+".txt","tryGood"+mode+".txt");
				game.setScreen(new GameView(game,script,p,false));
				return true;
			}
		});

		slowToggle = new TextButton("Try Bad Fish",skin);
		stage.addActor(slowToggle);
		slowToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				FileHandle script=downloadFile(servername+"trybad"+mode+".txt","trybad"+mode+".txt");
				Player p=new Player(init,vGame,0,Integer.parseInt(age));
				game.setScreen(new GameView(game,script,p,false));
				return true;
			}
		});
		
		practiseToggle = new TextButton("Practise now",skin);
		stage.addActor(practiseToggle);
		practiseToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				FileHandle script=downloadFile(servername+"practice"+mode+".txt","practise"+mode+".txt");
				Player p=new Player(init,vGame,0,Integer.parseInt(age));
				game.setScreen(new GameView(game,script,p,false,true));
				return true;
			}
		});
		StartToggle = new TextButton("Start Playing",skin);
		stage.addActor(StartToggle);
		StartToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				
				Player p=new Player(init,vGame,0,Integer.parseInt(age));
				game.setScreen(new GameView(game,file,p,true));
				return true;
			}
		});
		QuitToggle = new TextButton("Quit",skin);
		stage.addActor(QuitToggle);
		QuitToggle.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			   Gdx.app.exit(); // this will call pause method
			   return true;
	}
		});
		stage.addActor(table);
		table.setFillParent(true);
		Label welcome;
		if (vGame)
		 welcome=new Label("Welecome to Fish Police! Visual Mode " + init +"\n Please choose one of the following",skin);
		else
			 welcome=new Label("Welecome to Fish Police! Audio Mode " + init +"\n Please choose one of the following",skin);
		stage.addActor(welcome);
		table.add(welcome).width(300).colspan(2).center().height(50).spaceBottom(10);
		table.row();
		table.add(fastToggle).width(150).center().spaceBottom(15).height(50);
		table.row();
		table.add(slowToggle).width(150).center().spaceBottom(15).height(50);
		table.row();
		table.add(practiseToggle).width(150).center().spaceBottom(15).height(50);
		table.row();
		table.add(StartToggle).width(200).center().spaceBottom(15).height(50);
		table.row();
		table.add(QuitToggle).width(100).center().spaceBottom(15).height(50);
		batch=new SpriteBatch();		
		Gdx.input.setInputProcessor(stage);
        
	}

	@Override
	public void hide() {	
	}

	@Override
	public void pause() {
	
    Gdx.app.exit(); // this will call resume 

	}
	@Override
	public void resume() {
		
		 Gdx.app.exit(); // so that is why I put this here 

	}
	@Override
	public void dispose() {
		
		 Gdx.app.exit();
		}


}
