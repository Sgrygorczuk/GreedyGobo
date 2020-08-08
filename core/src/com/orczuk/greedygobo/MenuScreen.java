package com.orczuk.greedygobo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.orczuk.greedygobo.Objects.Coin;
import com.orczuk.greedygobo.Objects.ParentObject;

import java.util.Vector;

public class MenuScreen extends ScreenAdapter{
    //Screen Dimensions
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    //Visual objects
    private SpriteBatch batch = new SpriteBatch();			 //Batch that holds all of the textures
    private Viewport viewport;
    private Camera camera;

    //The buttons used to randomly generate a deck or open the deck building menu
    private Stage menuStage;
    private ImageButton[] menuButtons;
    private ImageButton[] exit;

    private TextureRegion[][] leavesTextures;
    private Texture backgroundTexture;
    private Texture foregroundTexture;
    private Texture popUpTexture;
    private Texture scoreBoardTexture;
    private Texture touchControlTexture;
    private Texture movementControlTexture;
    private TextureRegion[][] coinTextures;
    private TextureRegion[][] goboSpriteSheetTexture;
    private TextureRegion[][] abbotSpriteSheetTexture;
    private TextureRegion[][] knightSpriteSheetTexture;

    private String[] buttonText = new String[]{"Play", "Help", "Credits"};
    private BitmapFont bitmapFont = new BitmapFont();

    private Vector<ParentObject> leaves = new Vector<>();

    private Music music;

    private GreedyGobo greedyGobo;

    private boolean helpFlag;
    private boolean creditsFlag;

    //State of the progress bar

    /*
    Input: SpaceHops
    Output: Void
    Purpose: Grabs the info from main screen that holds asset manager
    */
    MenuScreen(GreedyGobo greedyGobo) { this.greedyGobo = greedyGobo;}

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
        showTextures();
        showButtons();
        showMusic();
        showObjects();
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

    private void showTextures(){
        Texture leavesTexturePath = new Texture(Gdx.files.internal("Sprites/Leaves.png"));
        leavesTextures = new TextureRegion(leavesTexturePath).split(40, 40); //Breaks down the texture into tiles
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/MainScreenBackground.png"));
        foregroundTexture = new Texture(Gdx.files.internal("Sprites/MainScreenForeground.png"));
        popUpTexture = new Texture(Gdx.files.internal("UI/PopUpBoarder.png"));
        scoreBoardTexture = new Texture(Gdx.files.internal("Sprites/ScoreBoard.png"));

        Texture coinTexturePath = new Texture(Gdx.files.internal("Sprites/ObjectSpriteSheet.png"));
        coinTextures = new TextureRegion(coinTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheet.png"));
        goboSpriteSheetTexture  = new TextureRegion(goboTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture knightTexturePath = new Texture(Gdx.files.internal("Sprites/KnightSpriteSheet.png"));
        knightSpriteSheetTexture = new TextureRegion(knightTexturePath).split(420, 420);
        Texture abbotTexturePath = new Texture(Gdx.files.internal("Sprites/AbbotSpriteSheet.png"));
        abbotSpriteSheetTexture = new TextureRegion(abbotTexturePath).split(420, 420);

        touchControlTexture = new Texture(Gdx.files.internal("Sprites/ClickerHand.png"));
        movementControlTexture  = new Texture(Gdx.files.internal("Sprites/PhoneMovement.png"));

    }

    private void showButtons(){
        menuStage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(menuStage);

        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/BigButtonBlank.png"));
        TextureRegion[][] buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(118, 47); //Breaks down the texture into tiles

        menuButtons = new ImageButton[4];

        for(int i = 0; i < 3; i ++){
            menuButtons[i] =  new ImageButton(new TextureRegionDrawable(buttonSpriteSheet[0][0]), new TextureRegionDrawable(buttonSpriteSheet[0][1]));
            menuButtons[i].setPosition(WORLD_WIDTH/2 - buttonSpriteSheet[0][0].getRegionWidth()/2f,
                    WORLD_HEIGHT/3 - ( buttonSpriteSheet[0][0].getRegionWidth()/2f - 10) * i);
            menuStage.addActor(menuButtons[i]);

            final int finalI = i;
            menuButtons[i].addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    playButtonFX();
                    if(finalI == 0){
                        music.stop();
                        greedyGobo.setScreen(new MainScreen(greedyGobo));
                    }
                    else if(finalI == 1){
                        for (ImageButton imageButton : menuButtons) { imageButton.setVisible(false); }
                        helpFlag = true;
                        menuButtons[3].setVisible(true);
                    }
                    else{
                        for (ImageButton imageButton : menuButtons) { imageButton.setVisible(false); }
                        creditsFlag = true;
                        menuButtons[3].setVisible(true);
                    }
                }
            });
        }

        Texture exitButtonTexturePath = new Texture(Gdx.files.internal("UI/ExitButton.png"));
        TextureRegion[][] exitButtonSpriteSheet = new TextureRegion(exitButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        menuButtons[3] =  new ImageButton(new TextureRegionDrawable(exitButtonSpriteSheet[0][0]), new TextureRegionDrawable(exitButtonSpriteSheet[0][1]));
        menuButtons[3].setPosition(WORLD_WIDTH - 50, WORLD_HEIGHT - 50);
        menuButtons[3].setWidth(20);
        menuButtons[3].setHeight(20);
        menuStage.addActor(menuButtons[3]);
        menuButtons[3].setVisible(false);
        menuButtons[3].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                playButtonFX();
                helpFlag = false;
                creditsFlag = false;
                for (ImageButton imageButton : menuButtons) { imageButton.setVisible(true); }
                menuButtons[3].setVisible(false);
            }
        });
    }

    private void showMusic(){
        music = greedyGobo.getAssetManager().get("Music/GoboMainMenuTheme.wav", Music.class);
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
    }

    private void playButtonFX() { greedyGobo.getAssetManager().get("SFX/Button.wav", Sound.class).play(1/2f); }

    private void showObjects(){
        if(greedyGobo.getAssetManager().isLoaded("Fonts/GreedyGobo.fnt")){bitmapFont = greedyGobo.getAssetManager().get("Fonts/GreedyGobo.fnt");}
        bitmapFont.getData().setScale(0.6f);
    }


    /*
    Input: Delta, timing
    Output: Void
    Purpose: What gets drawn
    */
    @Override
    public void render(float delta) {
        update();       //Update the variables
        draw();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
    */
    private void update() {
        updateLeaves();
    }

    private void updateLeaves(){
        updateLeavesPosition();
        if(leaves.size() < 6){ createNewLeaves(); }
        removeLeaves();
    }

    private void updateLeavesPosition(){
        for(ParentObject leaf : leaves){ leaf.updateLeaf();}
    }

    private void createNewLeaves(){
        ParentObject leaf = new ParentObject(MathUtils.random(3, 7));
        leaf.setUpLeafTexture(leavesTextures);
        leaves.add(leaf);
    }

    private void removeLeaves() {
        Vector<ParentObject> removedLeaves = new Vector<>();
        for(ParentObject leaf : leaves) { if (leaf.getX() < -leaf.getWidth()){ removedLeaves.add(leaf); } }
        for(ParentObject leaf: removedLeaves){ leaves.remove(leaf); }
        removedLeaves.removeAllElements();
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
        batch.draw(backgroundTexture, 0,0);
        for(ParentObject leaf : leaves){leaf.draw(batch);}
        batch.draw(foregroundTexture, 0,0);
        drawHighScore();
        if(helpFlag  || creditsFlag){batch.draw(popUpTexture, 10, 10, WORLD_WIDTH - 20, WORLD_HEIGHT-20);}
        batch.end();

        menuStage.draw();

        batch.begin();
        if(!helpFlag && !creditsFlag){drawButtonText();}
        else if(helpFlag){drawHelpScreen();}
        else{drawCredits();}
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

    private void drawHighScore(){
        bitmapFont.getData().setScale(0.2f);
        batch.draw(scoreBoardTexture, 375, 30, scoreBoardTexture.getWidth()/3f - 5, scoreBoardTexture.getHeight()/2f);
        centerText(bitmapFont, greedyGobo.getCurrentHighScore(), 375 + scoreBoardTexture.getWidth()/6f, 30 + scoreBoardTexture.getHeight()/4f);
    }

    private void drawButtonText(){
        bitmapFont.getData().setScale(0.5f);
        for(int i = 0; i < 3; i ++) {
            centerText(bitmapFont, buttonText[i], WORLD_WIDTH / 2,
                    WORLD_HEIGHT / 3 + 118/5f + 3 - (118/ 2f - 10) * i);
        }
    }

    private void drawHelpScreen(){
        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "How to Play", WORLD_WIDTH/2f, WORLD_HEIGHT-40);
        bitmapFont.getData().setScale(0.3f);
        batch.draw(goboSpriteSheetTexture[0][5], 35, WORLD_HEIGHT - 100, 30, 30);
        batch.draw(goboSpriteSheetTexture[3][3], WORLD_WIDTH-65, WORLD_HEIGHT - 100, 30, 30);
        batch.draw(touchControlTexture, 45, WORLD_HEIGHT - 110, 30, 30);
        centerText(bitmapFont, "When you start the game, grab and hold onto Gobo", WORLD_WIDTH/2f, WORLD_HEIGHT - 75);
        centerText(bitmapFont, "then release him onto the field.", WORLD_WIDTH/2f, WORLD_HEIGHT - 90);

        batch.draw(movementControlTexture, 35, WORLD_HEIGHT - 140, 30, 30);
        batch.draw(goboSpriteSheetTexture[2][1], WORLD_WIDTH-65, WORLD_HEIGHT - 140, 30, 30);
        centerText(bitmapFont, "Tilt the phone to move Gobo.", WORLD_WIDTH/2f, WORLD_HEIGHT - 125);

        batch.draw(coinTextures[0][2],  35, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[0][1], 45, WORLD_HEIGHT - 180, 30, 30);
        batch.draw(coinTextures[0][0], 55, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[1][2], WORLD_WIDTH - 65, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[1][1], WORLD_WIDTH - 75, WORLD_HEIGHT - 180, 30, 30);
        batch.draw(coinTextures[1][0], WORLD_WIDTH - 85, WORLD_HEIGHT - 190, 30, 30);
        centerText(bitmapFont, "Collect the coins and avoid the crosses.", WORLD_WIDTH/2f, WORLD_HEIGHT - 170);

        batch.draw(knightSpriteSheetTexture[0][0],  35, WORLD_HEIGHT - 240, 30, 30);
        batch.draw(abbotSpriteSheetTexture[0][0], WORLD_WIDTH-65, WORLD_HEIGHT - 240, 30, 30);
        centerText(bitmapFont, "Beware the abbots and the knights", WORLD_WIDTH/2f, WORLD_HEIGHT - 215);
        centerText(bitmapFont, "Abbots will stun you and the knights will kill you.", WORLD_WIDTH/2f, WORLD_HEIGHT - 230);

        batch.draw(goboSpriteSheetTexture[1][3],  35, WORLD_HEIGHT - 280, 30, 30);
        batch.draw(touchControlTexture, 45, WORLD_HEIGHT - 290, 30, 30);
        centerText(bitmapFont, "If you get stunned tap on Gobo to wake him up.", WORLD_WIDTH/2f, WORLD_HEIGHT - 265);
    }

    private void drawCredits(){
        batch.draw(coinTextures[1][1], WORLD_WIDTH/2 + 100, WORLD_HEIGHT - 160, 60, 60);
        batch.draw(coinTextures[1][1], WORLD_WIDTH/2 - 150, WORLD_HEIGHT - 160, 60, 60);

        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "Credits", WORLD_WIDTH/2f, WORLD_HEIGHT-45);
        bitmapFont.getData().setScale(0.32f);
        centerText(bitmapFont, "Programming & Art", WORLD_WIDTH/2f, WORLD_HEIGHT - 80);
        centerText(bitmapFont, "Sebastian Grygorczuk", WORLD_WIDTH/2f, WORLD_HEIGHT - 95);

        centerText(bitmapFont, "Music", WORLD_WIDTH/2f, WORLD_HEIGHT - 125);
        centerText(bitmapFont, "Yuriy Lehki", WORLD_WIDTH/2f, WORLD_HEIGHT - 140);

        centerText(bitmapFont, "SFX - Freesound", WORLD_WIDTH/2f, WORLD_HEIGHT - 170);
        centerText(bitmapFont, "Bratish", WORLD_WIDTH/2f - 100, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "pedrocnbp", WORLD_WIDTH/2f, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "msavioti", WORLD_WIDTH/2f + 100, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "InspectorJ", WORLD_WIDTH/2f - 100, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "LiamG_SFX", WORLD_WIDTH/2f, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "Deatlev", WORLD_WIDTH/2f + 100, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "joe_anderson22", WORLD_WIDTH/2f - 60, WORLD_HEIGHT - 230);
        centerText(bitmapFont, "thefsoundman", WORLD_WIDTH/2f + 60, WORLD_HEIGHT - 230);

        centerText(bitmapFont, "Font - 1001fonts", WORLD_WIDTH/2f, WORLD_HEIGHT - 255);
        centerText(bitmapFont, "RM Albion Font - p2pnut", WORLD_WIDTH/2f, WORLD_HEIGHT - 275);



    }

    /*
    Input: BitmapFont for size and font of text, string the text, and x and y for position
    Output: Void
    Purpose: General purpose function that centers the text on the position
    */
    private void centerText(BitmapFont bitmapFont, String string, float x, float y){
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, string);
        bitmapFont.draw(batch, string,  x - glyphLayout.width/2, y + glyphLayout.height/2);
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