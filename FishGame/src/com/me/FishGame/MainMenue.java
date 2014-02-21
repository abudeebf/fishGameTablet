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
	TextButton audioToggle,visualToggle;
	CheckBoxStyle checkStyle;
	Skin skin;
	SpriteBatch batch;
	Game game;
	Music music;
	Sound sound;
	String dwnload_file_path,dest_file_path;
	boolean vGame;
	public static FileHandle scan;
	public static FileHandle audioFileHandle,visualFileHandle;
	private Texture splashTexture;


	public static String versionNum = "3";

	public MainMenue(Game game)
	{
		double x=Double.parseDouble("-61");
		System.out.print(x);
		this.game=game;
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
		if(vGame) {
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
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_1388666174820.txt";
		dest_file_path = "scriptv3_Audio.txt";
		dest_file_path = "scriptv3_1388666174820.txt";
		audioFileHandle = downloadFile(dwnload_file_path,dest_file_path);
		dwnload_file_path = "http://moore.cs-i.brandeis.edu/Scripts/scriptv3_visual.txt";
		dest_file_path = "scriptv3_visual.txt";
		visualFileHandle = downloadFile(dwnload_file_path,dest_file_path);
		splashTexture =new Texture(Gdx.files.internal("images/0015.jpg"));
		stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;
		skin=new Skin(Gdx.files.internal("data/uiskin.json"));
		 // to know if mouse over or not 
		Table table=new Table();
		audioToggle = new TextButton("Play Audio",skin);
		stage.addActor(audioToggle);
		audioToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				vGame = false;
				game.setScreen(new UserInfo(game,audioFileHandle,vGame)); 
				return true;
			}
		});

		visualToggle = new TextButton("Play Visual",skin);
		stage.addActor(visualToggle);
		visualToggle.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				vGame = true;
				game.setScreen(new UserInfo(game,visualFileHandle,vGame)); 
				return true;
			}
		});
		stage.addActor(table);
		table.setFillParent(true);
		Label welcome=new Label("Welecome to Fish Police! \n Please Choose the mode you want to play",skin);
		stage.addActor(welcome);
		table.add(welcome).width(300).colspan(2).center().height(50).spaceBottom(10);
		table.row();
		table.add(audioToggle).width(200).center().spaceBottom(15).height(50);
		table.row();
		table.add(visualToggle).width(200).center().spaceBottom(15).height(50);
		batch=new SpriteBatch();		
		Gdx.input.setInputProcessor(stage);

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
		Gdx.app.exit();
	}


}
