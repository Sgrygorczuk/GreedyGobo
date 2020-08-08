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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

public class LoadingScreen extends ScreenAdapter{
    //Screen Dimensions
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    //Visual objects
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;			 //Batch that holds all of the textures
    private Viewport viewport;
    private Camera camera;

    private GreedyGobo greedyGobo;

    //State of the progress bar
    private float progress = 0;

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
        //Load the font
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "font_assets.atlas";
        greedyGobo.getAssetManager().load("Fonts/GreedyGobo.fnt", BitmapFont.class, bitmapFontParameter);

        greedyGobo.getAssetManager().load("Music/GoboLevelTheme.wav", Music.class);
        greedyGobo.getAssetManager().load("Music/GoboMainMenuTheme.wav", Music.class);

        greedyGobo.getAssetManager().load("SFX/Abbot.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Knight.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Coin.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Cross.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Slap.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Bag.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Drop.wav", Sound.class);
        greedyGobo.getAssetManager().load("SFX/Button.wav", Sound.class);


        //Load all the sound effects
        //paladins.getAssetManager().load("SoundEffects/PowerDownButton.wav", Sound.class);
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: What gets drawn
    */
    @Override
    public void render(float delta) {
        update();       //Update the variables
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
    */
    private void update() {
        if (greedyGobo.getAssetManager().update()) { greedyGobo.setScreen(new MenuScreen(greedyGobo)); }
        else { progress = greedyGobo.getAssetManager().getProgress(); }
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
    Purpose: Draws the progress
    */
    private void draw() {
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture before drawing buttons
        batch.begin();
        batch.end();

    }


    /*
    Input: Void
    Output: Void
    Purpose: Gets rid of all visuals
    */
    @Override
    public void dispose() {
    }


}