package com.orczuk.greedygobo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter{
    //Screen Dimensions
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    //Visual objects
    private SpriteBatch batch = new SpriteBatch();			 //Batch that holds all of the textures
    private Viewport viewport;
    private Camera camera;

    //The game object that keeps track of the settings
    private GreedyGobo greedyGobo;

    //Logo that's displayed when the game first loads
    private Texture logoTexture;

    //Timing variables, keeps the logo on for at least 2 second
    private boolean logoDoneFlag = false;
    private static final float LOGO_TIME = 2F;
    private float logoTimer = LOGO_TIME;


    /*
    Input: SpaceHops
    Output: Void
    Purpose: Grabs the info from main screen that holds asset manager
    */
    LoadingScreen(GreedyGobo greedyGobo) { this.greedyGobo = greedyGobo;}

    /*
    Input: Dimensions
    Output: Void
    Purpose: Resize the screen when window size changes
    */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    /*
    Input: Void
    Output: Void
    Purpose: Set up the the textures and objects
    */
    @Override
    public void show() {
        //Sets up the camera
        showCamera();           //Sets up camera through which objects are draw through
        loadAssets();           //Loads the stuff into the asset manager
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the camera through which all the objects are view through
    */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);		//
    }

    /*
    Input: Void
    Output: Void
    Purpose: Loads all the data needed for the assetmanager
    */
    private void loadAssets(){
        //Logo that's displayed during loading
        logoTexture = new Texture(Gdx.files.internal("Sprites/Logo.png"));

        //Load the font
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "font_assets.atlas";
        greedyGobo.getAssetManager().load("Fonts/GreedyGobo.fnt", BitmapFont.class, bitmapFontParameter);

        //Loading the music
        greedyGobo.getAssetManager().load("Music/GoboLevelTheme.wav", Music.class);
        greedyGobo.getAssetManager().load("Music/GoboMainMenuTheme.wav", Music.class);

        //Loading all of the SFX
        greedyGobo.getAssetManager().load("SFX/Abbot.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Knight.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Coin.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Cross.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Slap.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Bag.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Drop.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Button.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/HighScore.mp3", Sound.class);
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: What gets drawn
    */
    @Override
    public void render(float delta) {
        update(delta);       //Update the variables
        draw();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
    */
    private void update(float delta) {
        updateTimer(delta);
        if (greedyGobo.getAssetManager().update() && logoDoneFlag) { greedyGobo.setScreen(new MenuScreen(greedyGobo)); }
        else {greedyGobo.getAssetManager().getProgress();}
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Counts down until the logo has been on screen long enough
    */
    private void updateTimer(float delta) {
        logoTimer -= delta;
        if (logoTimer <= 0) {
            logoTimer = LOGO_TIME;
            logoDoneFlag = true;
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the progress
    */
    private void draw() {
        clearScreen();
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture before drawing buttons
        batch.begin();
        batch.draw(logoTexture, WORLD_WIDTH/2f - 64, WORLD_HEIGHT/2f - 64, 128, 128);
        batch.end();

    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets screen color
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Gets rid of all visuals
    */
    @Override
    public void dispose() {
        logoTexture.dispose();
    }
}