package com.me.FishGame;
import java.io.IOException;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameView implements Screen {
	

	
	
	public static SpriteBatch batch;
	MyGestureListener m;
	ShapeRenderer sr;
	Game game;
	Music music;
	public GameModel gm = null;
	boolean hasAvatar = true;
	boolean flash = false;
	boolean soundflash = false;
	boolean blank=true;
	public Music bgSound;
	Stage stage;
	Label label,gameOver,right,wrong,missed,total1;
	Label fishCount;
	LabelStyle style,style2;
	BitmapFont font;
	String lastbgSound;
	public Sound goodclip, badclip;
	public String goodclipfile,badclipfile;
	public int width = Gdx.graphics.getWidth();
	public int height = Gdx.graphics.getHeight();
	float x;
	Skin skin;
	Table table;
	String s;
	public boolean response;
	long delay=0L;
	ImageButton boat1;
	public boolean gameActive = false; // shouldn't this be in the model???
	public Texture streamImage, streamImage2, fish1, boat, coin,treasure,fixationMark,bubble,gameOverimg;
	Player p;
	public TextureRegion[] fishL, fishR;// these arrays store the sprite images used to adjust brightness. the
	// default brightness is in image, 12, accesible using fishL[12] or
	// fishR[12]
	public long indicatorUpdate;
	TextButton Q;
	public long soundIndicatorUpdate;
	public int thisFrame = 12;
	boolean printresult=false;
	private FileHandle scriptHandle;
	InputMultiplexer im;
	boolean playable ;
	public GameView(Game game, FileHandle scriptHandle,Player p,boolean playable){
		this.game=game;
		this.scriptHandle = scriptHandle;
		this.p=p;
		this.playable=playable;
	}
	// this method is same as paint method in desktop version 
	// this called multiple time 
	public void render(float delta) 
	{

		gm.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (gm.isGameOver() ) {
			
			this.bgSound.stop();
			if(playable)
			{batch.begin();
		
			gameOverWindow(s);
			batch.end();
			stage.draw();
			stage.act();
			if(printresult==false)
			{  try {
				printresult=true;
				double hits = gm.getHits();
				int misses = gm.getMisses();
				int nokey = gm.getNoKeyPress();
				double total=hits+misses+nokey;
				p.score=(hits/total) *100;
				 s=SqliteUploader.pre_post(p);
				gm.writeToLog(System.nanoTime(), p);
				
				gm.uploadFile(gm.retrunLogfile().name());
				gm.uploadFile(gm.getTiltLog().name());

			} 

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			
			return;

		}
			else
				if ( p.vmode)
				game.setScreen(new MainMenue(game,p.intial,p.age+"","Visual"));
				else
					game.setScreen(new MainMenue(game,p.intial,p.age+"","Audio"));
					
			}
		
		batch.begin();
		drawBackground(); // I have to think how to make it move in the oppsite side
		drawFish();
		if (hasAvatar)
			drawAvatar();
		batch.end();
		drawHud();
		stage.draw();
		stage.act(); 
		drawTimeBar();

		updateScore();
		handleAccelerometer();
	}

	// this method to render the gameOver window
	private void gameOverWindow(String s){
		int hits = gm.getHits();
		int misses = gm.getMisses();
		int nokey = gm.getNoKeyPress();
		int total=hits-misses-nokey;
		batch.draw(gameOverimg, 0, 0 , width, height );
		right.setText("Right: " +hits);
		total=getScore();
		right.setPosition((right.getMinWidth()),(height)/2+(right.getMinHeight())*4 );
		wrong.setText("Wrong: " +misses);
		wrong.setPosition((right.getMinWidth()),(height)/2 + (right.getMinHeight())*2);
		missed.setText("Missed: " +nokey);
		missed.setPosition((right.getMinWidth()),(height)/2+ (right.getMinHeight()));
		total1.setText("Total Point: " + total  + "\n" + s);
		total1.setPosition((right.getMinWidth()),(height)/2-(right.getMinHeight()));
		label.setText("");
		fishCount.setText("");
		Q = new TextButton("Quit Game",skin);
		Q.setWidth(200);
		Q.setHeight(50);
		Q.setPosition((right.getMinWidth()),(height)/2-(right.getMinHeight()*4));
	    stage.addActor(Q);
	    Gdx.input.setInputProcessor(im);
		Q.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				Gdx.app.exit();
				
				return false;
	
	}
		});
	}

	// handle the tilting of the tablet 
	private  void handleAccelerometer(){
		//x=Gdx.input.getAccelerometerX(); //unused
		
		
		float roll = Gdx.input.getRoll();
		float netRoll = (float)(roll - gm.getNeutralRoll());
	    float pitch = Gdx.input.getPitch();
		float netPitch = (float)(pitch - gm.getNeutralPitch());
		
		String logEntry = System.nanoTime() + " " + netRoll + " " + netPitch + "\n";
		gm.writeToTiltLog(logEntry);
		
		if (netRoll>GameEvent.answerThresholdGood || netRoll<GameEvent.answerThresholdBad)
		{ 
			flash = true;
			// we set the update time to be 50 ms after the keypress, so the
			// indicator stays lit for 50 ms
			long now=System.nanoTime();
			indicatorUpdate =  now+ 50000000l;
			// first check to see if they pressed
			// when there are no fish!!
			if (gm.getNumFish() == 0 ) {
				return;}
			else
			{ boolean correctResponse = gm.handleKeyPress(netRoll, now );

			try {
				Thread.sleep(gm.gameSpec.audioDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (correctResponse)
			{	
				goodclip.play();
				gm.writeToLog(System.nanoTime(), "playFeedback: "+goodclipfile);

			}
			else 
			{
				badclip.play();
				gm.writeToLog(System.nanoTime(), "playFeedback: "+badclipfile);
			}	 
			return;
			}

		}		

	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	@Override
	// like constructor in desktop model , this will call only ones 
	public void show() {
		GameSpec gs = new GameSpec();
		ScriptGenerator sg = new ScriptGenerator();
		try {
			sg.generate(gs);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("error");
		}
		
		this.gm=new GameModel(gs,scriptHandle,p);
		Fish.GAME_START = System.nanoTime();
		gm.start();
		updateGameState(gs);
		x=0;
		response=false;
		batch=new SpriteBatch();
		font= new BitmapFont(Gdx.files.internal("font.fnt"),
				false);
		skin=new Skin(Gdx.files.internal("data/uiskin.json"));
		style=new LabelStyle(font,Color.BLACK);
		label= new Label("0",style);
		fishCount = new Label("0 Fish",style);
		style2=new LabelStyle(font,Color.WHITE);
		right=new Label("",style2);
		wrong=new Label(" ",style2);
		missed=new Label("" ,style2);
		total1=new Label("",style2);
		stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;	
		sr=new ShapeRenderer();
		//stage.addActor(gameOver);
		stage.addActor(right);
		stage.addActor(wrong);
		stage.addActor(missed);
		stage.addActor(total1);
		m=new MyGestureListener(gm);
		 im = new InputMultiplexer(stage,new GestureDetector(m )); // Order matters here!
		Gdx.input.setInputProcessor(im);
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		Gdx.app.exit();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Gdx.app.exit();
	}
	@Override
	public void dispose() {
		Gdx.app.exit();
	}
	private void drawBackground() 
	{
		double seconds = System.nanoTime() / 1000000000.0;
		double frames = seconds * 0.1;
		double framePart = frames - Math.floor(frames);
		int y_offset = (int) Math.round(framePart * height);
		if (gm.isPaused() || gm.isGameOver()) {

			if (gm.getNumFish() > 0) {
				gm.removeLastFish();
			}
		}
		if (gm.isGameOver()){
			this.bgSound.stop();
		}
		// draw image on screen tiled
		batch.draw(streamImage, 0, y_offset - height, width, height / 2 + 2);
		batch.draw(streamImage2, 0, y_offset - height / 2, width,
				height / 2 + 2);
		batch.draw(streamImage, 0, y_offset, width, height / 2 + 2);
		batch.draw(streamImage2, 0, y_offset + height / 2, width,
				height / 2 + 2);
	}

	private void drawFish()
	{
		Fish f = gm.getCurrentFish();

		if (f != null) {
			drawActor( f, Color.WHITE);
		}

	}

	/**
	 * drawActor(a,c) - draws a single actor a.
	 * The color c is the default color used for new species, but is ignored for
	 * avatars, wasps, and fireflies
	 * 

	 * @param aFish
	 *            - the Actor to be drawn
	 * @param c
	 *            - the default color for actors of unknown species
	 */
	private void drawActor( Fish aFish, Color c) {
		if (!aFish.active)
			return;
		int x = toXViewCoords(aFish.x);
		int y = toYViewCoords(aFish.y);
		int visualHz = 1;
		// set the default visual hertz for the fish
		// this should be done when the fish is created!
		// the visualhz should be a field of the fish...
		switch (aFish.species) {
		case good:
			visualHz = gm.gameSpec.good.throbRate;
			break;
		case bad:
			visualHz = gm.gameSpec.bad.throbRate;
			break;
		default:
			break;
		}
		// handle the exception conditions...
		if (gm.gameSpec.avmode == 1) { // auditory determines good/bad, so
			// switch the visual hertz in
			// incongruent case
			if (aFish.congruent == 1) {
				switch (aFish.species) {
				case bad:
					visualHz = gm.gameSpec.good.throbRate;
					break;
				case good:
					visualHz = gm.gameSpec.bad.throbRate;
					break;
				default:
					break;
				}
			} else if (aFish.congruent == 2) {
				visualHz = 0;
			}
		}

		int theSize = interpolateSize(gm.gameSpec.minThrobSize,
				gm.gameSpec.maxThrobSize, aFish.birthTime, System.nanoTime(),
				visualHz);

		thisFrame = interpolateBrightness(gm.gameSpec.minBrightness,
				gm.gameSpec.maxBrightness, aFish.birthTime, System.nanoTime(),
				visualHz);

		int theWidth = gm.gameSpec.minThrobSize; // theSize; //(int) (theSize);
		int theHeight = theSize * fishL[12].getTexture().getHeight() / fishL[12].getTexture().getWidth();// (int)
		// ((theSize
		// *
		Animation a1=new Animation(1f,fishL);																		// aspectRatio)/100);
		Animation a2=new Animation(1f,fishR);	
		if (aFish.fromLeft) {
			batch.draw(a1.getKeyFrame(thisFrame), x - theWidth / 2, y - theHeight / 2,
					theWidth, theHeight);
		} else {
			TextureRegion tr=a2.getKeyFrame(thisFrame);

			batch.draw(tr, x - theWidth / 2, y - theHeight / 2	,theWidth, theHeight);
		}

	}

	// brightness works by cycling through a sprite image that has 25 different
	// levels of brightness.
	private int interpolateBrightness(int min, int max, long birth, long now,
			double freq) {
		// t is the number of cycles so far; take the time in seconds that the
		// actor has been active, multiply by the frequency
		double t = ((now - birth) / 1000000000.0) * freq;
		// y is the sinusoidal position of the cycle
		double y = 0.5 * (Math.sin(Math.PI * 2 * t) + 1);
		// frame oscillates between min and max, as y oscillates from 0 to 1.
		int range = (int) (max - min);
		// int segment = (int) range/25;
		int frame = (int) (range * (y - (y % 0.04)));
		return frame;
	}
	// method to adjust the size of the fish 
	private int interpolateSize(double min, double max, long birth, long now,
			double freq) {
		double t = ((now - birth) / 1000000000.0) * freq;
		double y = 1 - 0.5 * (Math.sin(Math.PI * 2 * t) + 1);
		double s = min * y + max * (1 - y);
		int size = (int) Math.round(s);
		return size;
	}

	public int toXViewCoords(double x) {
		int width = Gdx.graphics.getWidth();
		return (int) Math.round(x / GameModel.SIZE * width);
	}

	public int toYViewCoords(double x) {
		int height =Gdx.graphics.getHeight();
		return (int) Math.round(x / GameModel.SIZE * height);
	}

	private void drawHud() 
	{
        sr.begin(ShapeType.Filled);
		sr.setColor(new Color(.75f,.75f,.25f,1f));
		sr.rect(0, Gdx.graphics.getHeight()-140, 160, 40); //lower left money sign
		sr.rect(0, Gdx.graphics.getHeight()-100, Gdx.graphics.getWidth(),100); //main rectangle
		sr.triangle(160,Gdx.graphics.getHeight()-140,250,Gdx.graphics.getHeight()-100,160,Gdx.graphics.getHeight()-100); //lower left triangle
		sr.triangle(Gdx.graphics.getWidth() - 160 , Gdx.graphics.getHeight()-140, Gdx.graphics.getWidth()-250, Gdx.graphics.getHeight()-100, Gdx.graphics.getWidth()-160, Gdx.graphics.getHeight()-100); //lower right triangle
		sr.rect(Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight()-140, 160, 40); // lower right fish count
		sr.end();
		batch.begin();
		int displayScore=getScore();
		int bagScore = displayScore;
		int xOffset = 20;
		while(bagScore > 500) {
			batch.draw(treasure,xOffset , Gdx.graphics.getHeight()-90, 54, 50);
			bagScore-=500;
			xOffset += 59;
		}
		
		while(bagScore>100) {
			batch.draw(coin,xOffset , Gdx.graphics.getHeight()-90, 30, 40);
			bagScore-=100;
			xOffset += 35;
		}
		batch.draw(coin,xOffset,Gdx.graphics.getHeight()-90,30,(int)(40*(bagScore/100.0)));
		 style=new LabelStyle(font,Color.BLACK);
		 label.setStyle(style);
		 label.setText("$" + displayScore+"");
		 label.setPosition(10, Gdx.graphics.getHeight()-140);
		 stage.addActor(label);
		 fishCount.setText(gm.getFishSpawnedCount() + " Fish");
		 fishCount.setPosition(Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()-140);
		 stage.addActor(fishCount);
		 batch.end();
	}
    private int getScore(){
    	int score = gm.getScore();
		int displayScore = score;
		long tickupStart = gm.getTickupStart();
		long tickupEnd = tickupStart + 500;
		long currentTime = System.currentTimeMillis();
		if(currentTime < tickupEnd) {
			int prevScore = gm.getPreviousScore();
			System.out.println(currentTime);
			System.out.println(tickupStart);
			System.out.println(currentTime - tickupStart);
			double tickupRatio = ((currentTime - tickupStart) + 0.0)/(tickupEnd - tickupStart);
			displayScore = prevScore + (int)((score - prevScore) * tickupRatio);
		}
		return displayScore;
    }
	// method to draw the boat avatar
	private void drawAvatar()
	{
		// This draws the boat in the middle
		int x = (Gdx.graphics.getWidth() - boat.getWidth()) / 2;
		int y = 0-boat.getHeight()/2;
		batch.draw(boat, x, y, boat.getWidth(), boat.getHeight());
	    m.m=boat.getWidth();
	    m.n=boat.getHeight();
	    
	}
	private void updateGameState(GameSpec gs) 
	{

		if (gs == null) {
			System.out.println("gs is null!!!");
			return;
		}

		goodclip = Gdx.audio.newSound(Gdx.files.internal(gs.goodResponseSound));
		goodclipfile=gs.goodResponseSound;
		badclip = Gdx.audio.newSound(Gdx.files.internal(gs.badResponseSound));
		badclipfile=gs.badResponseSound;
		// here we read in the background image which tiles the scene
		try {
			streamImage =new Texture(Gdx.files.internal(gs.backgroundImage));
			gameOverimg=new Texture(Gdx.files.internal("images/0053.jpg"));
			streamImage2 = new Texture(Gdx.files.internal("images/streamB2.jpg"));
			boat = new Texture(Gdx.files.internal("images/boat1.png"));
			fish1 = new Texture(Gdx.files.internal("images/fish/fish.png"));
			fishL = spriteImageArray(fish1, 5, 5);
			fishR = flibHorizantly(fish1, 5, 5);
			coin = new Texture(Gdx.files.internal("images/money_bag.png"));
			treasure = new Texture(Gdx.files.internal("images/treasurechest.png"));
			hasAvatar = gs.hasAvatar;
			if (!gs.bgSound.equals(this.lastbgSound)) {
				this.lastbgSound = gs.bgSound;
				if (bgSound != null)
					bgSound.stop();
				bgSound =  Gdx.audio.newMusic(Gdx.files.internal(gs.bgSound));
				bgSound.setLooping(true);
				bgSound.play();
			}
		} catch (Exception e) {
			System.out.println("can't find background images" + e);
		}

	}
	private void drawTimeBar() 
	{
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(0,Gdx.graphics.getHeight()-30,Gdx.graphics.getWidth(),30);
		sr.setColor(Color.GREEN);
		sr.rect(0,Gdx.graphics.getHeight()-30,toXViewCoords(gm.timeRemaining), 30);
		sr.end();
		// TODO Auto-generated method stub	
	}
	private void updateScore()
	{
		style=new LabelStyle(font,Color.BLUE);
		label.setStyle(style);	 
	}

	// this method to split the image to multiple images 
	public static TextureRegion[] spriteImageArray(Texture img, int cols,
			int rows) {
		TextureRegion[][] tmp=TextureRegion.split(img, img.getWidth()/cols, img.getHeight()/rows);
		TextureRegion[]frames=new TextureRegion[cols*rows];
		int index=0;
		for (int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				frames[index++]=tmp[i][j];
			}}
		return frames;
	}
	// this method used to make the fish come from the opposite  direction
	public static TextureRegion[] flibHorizantly(Texture img, int cols,
			int rows) {
		TextureRegion[][] tmp=TextureRegion.split(img, img.getWidth()/cols, img.getHeight()/rows);
		TextureRegion[]frames=new TextureRegion[cols*rows];
		int index=0;
		for (int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				TextureRegion x=tmp[i][j];
				x.flip(true, false);
				frames[index++]=x;
			}}
		return frames;
	}


}
