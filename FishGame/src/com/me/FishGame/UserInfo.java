package com.me.FishGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.InputListener;


public class UserInfo implements Screen {
	Stage stage;
	Game game;
	FileHandle scriptHandle;
	Label nameLabel,ageLabel;
	LabelStyle stylel;
	TextFieldStyle stylet;
	Skin skin;
	TextField nameText,ageText;
	BitmapFont font;
	SpriteBatch batch;
	
	boolean vmode;
	Table table;
	private Texture splashTexture;
	public UserInfo(Game game ,FileHandle scriptHandle,boolean vmode){
		this.game=game;
		this.scriptHandle=scriptHandle;
		this.vmode=vmode;
		
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(splashTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
		stage.draw();

		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		splashTexture =new Texture(Gdx.files.internal("images/0107.jpg"));
		stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;

		font= new BitmapFont(Gdx.files.internal("font.fnt"),
				false);
		stylel=new LabelStyle(font,Color.BLACK);
		stylet=new TextFieldStyle();
		skin=new Skin(Gdx.files.internal("data/uiskin.json"));
		stylet.font=font;
		stylet.fontColor=Color.BLACK;
		// stylet.background = skin.getDrawable("textbox");
		Gdx.input.setInputProcessor(stage);
		nameLabel = new Label("Initial:", skin);
		//  nameLabel.setPosition(Gdx.graphics.getWidth()/2-nameLabel.getText().length(), Gdx.graphics.getHeight()-(nameLabel.getHeight()*2));
		nameText = new TextField("", skin);
		//nameText.setPosition(Gdx.graphics.getWidth()/2+ (2*nameLabel.getWidth()),Gdx.graphics.getHeight()-(nameLabel.getHeight()*2));
		ageLabel = new Label("Age:",skin);
		//ageLabel.setPosition(Gdx.graphics.getWidth()/2-nameLabel.getText().length(),Gdx.graphics.getHeight()-(nameLabel.getHeight()*4) );
		ageText = new TextField("",skin);
		//ageText.setPosition(Gdx.graphics.getWidth()/2+ (2*nameLabel.getWidth()),Gdx.graphics.getHeight()-(nameLabel.getHeight()*4) );
		TextButton button = new TextButton("Play", skin);
		TextButton quite=new TextButton("Quit", skin);
		// button.setWidth(Gdx.graphics.getWidth()/4);
		// button.setHeight(Gdx.graphics.getHeight()/10);
		stage.addActor(nameLabel);
		stage.addActor(nameText);
		stage.addActor(ageLabel);
		stage.addActor(ageText);
		stage.addActor(quite);
		stage.addActor(button);
		//button.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-(nameLabel.getHeight()*6+button.getHeight()));
		button.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(ageText.getText().length()>0 && nameText.getText().length()>0)
				{
					Gdx.input.setOnscreenKeyboardVisible(false);
					Player p=new Player(nameText.getText(),vmode,0,Integer.parseInt(ageText.getText()));
					game.setScreen(new GameView(game,scriptHandle,p)); }
				else
				{

					Gdx.app.error("MyTag", "Please write your intial and your age");
				}

				return false;
			}
		});
		
			quite.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					
					Gdx.app.exit();
					Gdx.app.exit();
					return false;
		
		}
			});

		table = new Table(skin);
		table.setFillParent(true);
        
		stage.addActor( table );
		Label welcome;
		if (vmode)
		{    System.out.println(vmode);
			 welcome=new Label("Welecome to Fish Police! \n Please Enter your information to play visual mode" ,skin);
		}
		else
			 welcome=new Label("Welecome to Fish Police! \n Please Enter your information to play Audio mode" ,skin);
		stage.addActor(welcome);
        table.top();
        
		table.add(welcome).width(300).colspan(2).center().height(50).spaceBottom(10).spaceTop(15);
		table.row();
		table.add(nameLabel).width(100);
		table.add(nameText).width(200).spaceBottom(10);;
		table.row();
		table.add(ageLabel).width(100);
		table.add(ageText).width(200).spaceBottom(15);;
		table.row();
		table.add(button).width(150).center().height(50);
		table.add(quite).width(150).center().height(50);
		table.row();

		batch=new SpriteBatch();		

		// add the table to the stage and retrieve its layout


	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
}
