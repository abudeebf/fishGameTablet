package com.me.FishGame;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameView implements Screen {
	public static SpriteBatch batch;
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
    LabelStyle style;
    BitmapFont font;
	String lastbgSound;
	public Sound goodclip, badclip;
	public String goodclipfile,badclipfile;
    public int width = Gdx.graphics.getWidth();
	public int height = Gdx.graphics.getHeight();
	float x;
	public boolean response;
	long delay=0L;
	public boolean gameActive = false; // shouldn't this be in the model???
	public Texture streamImage, streamImage2, fish1, boat, coin,fixationMark,bubble,gameOverimg;
	String upLoadServerUri = "http://moore.cs-i.brandeis.edu/UploadToServer.php";
	// these arrays store the sprite images used to adjust brightness. the
	// default brightness is in image, 12, accesible using fishL[12] or
	// fishR[12]
	public TextureRegion[] fishL, fishR;
	public long indicatorUpdate;
	public long soundIndicatorUpdate;
	public int thisFrame = 12;
    final String uploadFilePath = Gdx.files.getExternalStoragePath();
     String uploadFileName = "";
     int serverResponseCode = 0;
     private FileHandle scriptHandle;
	
	public GameView(Game game, FileHandle scriptHandle){
		this.game=game;
		this.scriptHandle = scriptHandle;
		
	}
	// this method is same as paint method in desktop version 
	// this called multiple time 
	public void render(float delta) 
	{
       
		gm.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if (gm.isGameOver()) {
			this.bgSound.stop();
			batch.begin();
			gameOverWindow();
			batch.end();
			stage.draw();
		    stage.act();
		    Gdx.graphics.setContinuousRendering(false);

		    try {
		    	   uploadFile(gm.retrunLogfile().name());
			    } 
		    
		    catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    return;
		    
		}
		batch.begin();
		drawBackground(); // I have to think how to make it move in the oppsite side
		drawHud();
		drawFish();
		if (hasAvatar)
			drawAvatar();
		batch.end();
		stage.draw();
	    stage.act(); 
		drawTimeBar();
		
		updateScore();
		handleAccelerometer();
	}
		
	// this method to render the gameOver window
	 private void gameOverWindow(){
		 int hits = gm.getHits();
		 int misses = gm.getMisses();
		 int nokey = gm.getNoKeyPress();
		 int total=hits-misses-nokey;
		 batch.draw(gameOverimg, 0, 0 , width, height );
		 right.setText("Right: " +hits);
		 right.setPosition((width)/2-(right.getMinWidth())/2,(height)/2+(right.getMinHeight()) );
		 wrong.setText("Wrong: " +misses);
		 wrong.setPosition((width)/2-(right.getMinWidth())/2,(height)/2 - (right.getMinHeight()));
		 missed.setText("Missed: " +nokey);
		 missed.setPosition((width)/2-(right.getMinWidth())/2,(height)/2-(right.getMinHeight())*2);
		 total1.setText("Total Point: " + total);
		 total1.setPosition((width)/2-(right.getMinWidth())/2,(height)/2-right.getMinHeight()*3);
		 label.setText("");
	 }
	 
	 // handle the tilting of the tablet 
     private  void handleAccelerometer(){
    		x=Gdx.input.getAccelerometerX();
    		  
    	   
    		if (x>3 || x<-3)
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
    			{ boolean correctResponse = gm.handleKeyPress(x, now );
 
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
	
		this.gm=new GameModel(gs,scriptHandle);
		Fish.GAME_START = System.nanoTime();
		gm.start();
		updateGameState(gs);
		x=0;
		response=false;
		batch=new SpriteBatch();
		font= new BitmapFont(Gdx.files.internal("font.fnt"),
				 false);
	    style=new LabelStyle(font,Color.BLACK);
		label= new Label("0",style);
	    right=new Label("",style);
	    wrong=new Label(" ",style);
		missed=new Label("" ,style);
		total1=new Label("",style);
		stage=new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true)	;	
		sr=new ShapeRenderer();
		//stage.addActor(gameOver);
		stage.addActor(right);
		stage.addActor(wrong);
		stage.addActor(missed);
		stage.addActor(total1);
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

		batch.draw(coin,0 , Gdx.graphics.getHeight()-120, 80, 80);
		int hits = gm.getHits();
		int misses = gm.getMisses();
		int nokey = gm.getNoKeyPress();
		int total=hits-misses-nokey;
		 style=new LabelStyle(font,Color.BLACK);
		 label.setStyle(style);
		 label.setText(total+"");
		 label.setPosition(80, Gdx.graphics.getHeight()-100);
		 stage.addActor(label);
		 
		
	}

	// method to draw the boat avatar
	private void drawAvatar()
	{
		// This draws the boat in the middle
		int x = (Gdx.graphics.getWidth() - boat.getWidth()) / 2;
		int y = 0-boat.getHeight()/2;
		batch.draw(boat, x, y, boat.getWidth(), boat.getHeight());
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
			gameOverimg=new Texture(Gdx.files.internal("images/GameOver.png"));
			
			streamImage2 = new Texture(Gdx.files.internal("images/streamB2.jpg"));

			boat = new Texture(Gdx.files.internal("images/boat1.png"));
			
			fish1 = new Texture(Gdx.files.internal("images/fish/fish.png"));
			fishL = spriteImageArray(fish1, 5, 5);
			fishR = flibHorizantly(fish1, 5, 5);
			
			coin = new Texture(Gdx.files.internal("images/wealth.png"));
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
	
	// method to upload to the server moors 
	 public int uploadFile(String filePath) throws IOException {
         
		 String fileName;
		 HttpURLConnection connection = null;
		 DataOutputStream outputStream = null;
		
		 if (filePath.equals(""))
			 fileName="/scriptv3_1388666174820.txt";
			 else
				 fileName=filePath;
		 
		 String pathToOurFile =Gdx.files.getExternalStoragePath()+fileName ;
		 String urlServer = upLoadServerUri;
		 String lineEnd = "\r\n";
		 String twoHyphens = "--";
		 String boundary =  "*****";

		 int bytesRead, bytesAvailable, bufferSize;
		 byte[] buffer;
		 int maxBufferSize = 1*1024*1024;

		 try
		 {
		 FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

		 URL url = new URL(urlServer);
		 connection = (HttpURLConnection) url.openConnection();

		 // Allow Inputs & Outputs
		 connection.setDoInput(true);
		 connection.setDoOutput(true);
		 connection.setUseCaches(false);

		 // Enable POST method
		 connection.setRequestMethod("POST");

		 connection.setRequestProperty("Connection", "Keep-Alive");
		 connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

		 outputStream = new DataOutputStream( connection.getOutputStream() );
		 outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		 outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
		 outputStream.writeBytes(lineEnd);

		 bytesAvailable = fileInputStream.available();
		 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		 buffer = new byte[bufferSize];

		 // Read file
		 bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		 while (bytesRead > 0)
		 {
		 outputStream.write(buffer, 0, bufferSize);
		 bytesAvailable = fileInputStream.available();
		 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		 }

		 outputStream.writeBytes(lineEnd);
		 outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		 // Responses from the server (code and message)
		 serverResponseCode = connection.getResponseCode();
		

		 fileInputStream.close();
		 outputStream.flush();
		 outputStream.close();
		 }
		 catch (Exception ex)
		 {
		System.out.print(ex);
		 }
		return maxBufferSize;	 }
}
