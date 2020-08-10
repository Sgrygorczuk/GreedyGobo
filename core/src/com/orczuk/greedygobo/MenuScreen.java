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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.orczuk.greedygobo.Objects.Leaf;
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

    //The buttons used to move around the menus
    private Stage menuStage;
    private ImageButton[] menuButtons;

    //Textures
    private TextureRegion[][] leavesTextures;           //Textures of the leaves flying between foreground and background
    private Texture backgroundTexture;                  //Foreground texture
    private Texture foregroundTexture;                  //Background texture
    private Texture popUpTexture;                       //Pop-up screen that the Credits and Help are displayed on
    private Texture scoreBoardTexture;                  //ScoreBoard texture n the pig where the high score is shown
    private Texture touchControlTexture;                //The texture for touch interaction
    private Texture movementControlTexture;             //The Texture for movement interaction
    private Texture blueGoboTexture;                    //The unlock texture
    private Texture toxicGoboTexture;                    //The unlock texture
    private Texture holyGoboTexture;                    //The unlock texture
    private Texture haloTexture;                        //The unlocked texture for -20 score
    private TextureRegion[][] coinTextures;             //Coin Textures used in Help
    private TextureRegion[][] goboSpriteSheetTexture;   //Gobo Textures used in Help
    private TextureRegion[][] abbotSpriteSheetTexture;  //Abbot textures used in help
    private TextureRegion[][] knightSpriteSheetTexture; //Knight textures used in help

    //String used on the buttons
    private String[] buttonText = new String[]{"Play", "Help", "Credits"};
    //Font used to write in
    private BitmapFont bitmapFont = new BitmapFont();

    //The leaves that blow by
    private Vector<Leaf> leaves = new Vector<>();

    //Music player
    private Music music;

    //Game object that keeps track of settings
    private GreedyGobo greedyGobo;

    private boolean helpFlag;      //Tells if help menu is up or not
    private boolean creditsFlag;   //Tells if credits menu is up or not
    boolean letGo = true;
    int skinCounter = 0;

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
        showTextures();         //Sets up the textures
        showButtons();          //Sets up the buttons
        showMusic();            //Sets up the music
        showObjects();          //Sets up the font
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
    Purpose: Sets textures that will be drawn
    */
    private void showTextures(){
        //Basic single image textures
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/MainScreenBackground.png"));
        foregroundTexture = new Texture(Gdx.files.internal("Sprites/MainScreenForeground.png"));
        popUpTexture = new Texture(Gdx.files.internal("UI/PopUpBoarder.png"));
        scoreBoardTexture = new Texture(Gdx.files.internal("Sprites/ScoreBoard.png"));
        blueGoboTexture = new Texture(Gdx.files.internal("Sprites/BlueGobo.png"));
        toxicGoboTexture = new Texture(Gdx.files.internal("Sprites/ToxicGobo.png"));
        holyGoboTexture = new Texture(Gdx.files.internal("Sprites/BlueGoboHoly.png"));
        haloTexture = new Texture(Gdx.files.internal("Sprites/Halo.png"));

        Texture leavesTexturePath = new Texture(Gdx.files.internal("Sprites/Leaves.png"));
        leavesTextures = new TextureRegion(leavesTexturePath).split(40, 40); //Breaks down the texture into tiles
        Texture coinTexturePath = new Texture(Gdx.files.internal("Sprites/ObjectSpriteSheet.png"));
        coinTextures = new TextureRegion(coinTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheet.png"));
        goboSpriteSheetTexture  = new TextureRegion(goboTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture knightTexturePath = new Texture(Gdx.files.internal("Sprites/KnightSpriteSheet.png"));
        knightSpriteSheetTexture = new TextureRegion(knightTexturePath).split(420, 420);
        Texture abbotTexturePath = new Texture(Gdx.files.internal("Sprites/AbbotSpriteSheet.png"));
        abbotSpriteSheetTexture = new TextureRegion(abbotTexturePath).split(420, 420);

        //Depending on if we're on phone or PC the touch/movement images should be different
        if(greedyGobo.getPhone()){
            touchControlTexture = new Texture(Gdx.files.internal("Sprites/ClickerHand.png"));
            movementControlTexture  = new Texture(Gdx.files.internal("Sprites/PhoneMovement.png"));
        }
        else{
            touchControlTexture = new Texture(Gdx.files.internal("Sprites/MouseClick.png"));
            movementControlTexture  = new Texture(Gdx.files.internal("Sprites/WASD.png"));
        }

    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets buttons and their interactions
    */
    private void showButtons(){
        menuStage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(menuStage); //Give power to the menuStage

        //Set up all the buttons used by the stage
        menuButtons = new ImageButton[4];

        setUpMainButtons(); //Places the three main Play|Help|Credits buttons on the screen
        setUpExitButton();  //Palaces the exit button that leaves the Help and Credits menus
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets main three main Play|Help|Credits buttons on the screen
    */
    private void setUpMainButtons(){
        //Get the texures of the buttons
        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/BigButtonBlank.png"));
        TextureRegion[][] buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(118, 47); //Breaks down the texture into tiles

        //Places the three main Play|Help|Credits buttons on the screen
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
                    //Launches the game
                    if(finalI == 0){
                        music.stop();
                        greedyGobo.setScreen(new MainScreen(greedyGobo));
                    }
                    //Turns on the help menu
                    else if(finalI == 1){
                        for (ImageButton imageButton : menuButtons) { imageButton.setVisible(false); }
                        helpFlag = true;
                        menuButtons[3].setVisible(true);
                    }
                    //Turns on the credits menu
                    else{
                        for (ImageButton imageButton : menuButtons) { imageButton.setVisible(false); }
                        creditsFlag = true;
                        menuButtons[3].setVisible(true);
                    }
                }
            });
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets exit button that leaves the Help and Credits menus
    */
    private void setUpExitButton(){
        //Gets the textures
        Texture exitButtonTexturePath = new Texture(Gdx.files.internal("UI/ExitButton.png"));
        TextureRegion[][] exitButtonSpriteSheet = new TextureRegion(exitButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        //Places the button and adds it to the stage
        menuButtons[3] =  new ImageButton(new TextureRegionDrawable(exitButtonSpriteSheet[0][0]), new TextureRegionDrawable(exitButtonSpriteSheet[0][1]));
        menuButtons[3].setPosition(WORLD_WIDTH - 50, WORLD_HEIGHT - 50);
        menuButtons[3].setWidth(20);
        menuButtons[3].setHeight(20);
        menuStage.addActor(menuButtons[3]);
        menuButtons[3].setVisible(false);
        //If tapped turn of any menu and turn back the main three buttons
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

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the music that will play when screen is started
    */
    private void showMusic(){
        music = greedyGobo.getAssetManager().get("Music/GoboMainMenuTheme.wav", Music.class);
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
    }

    /*
    Input: Void
    Output: Void
    Purpose: SFX will be played any time a button is clicked
    */
    private void playButtonFX() { greedyGobo.getAssetManager().get("SFX/Button.wav", Sound.class).play(1/2f); }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the font
    */
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
    Purpose: Updates the leaves and gobo's color
    */
    private void update() {
        updateGobo();
        updateLeaves();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Lets user click on gobo to change his color
    */
    private void updateGobo(){
        float touchedY = WORLD_HEIGHT - Gdx.input.getY() * WORLD_HEIGHT / Gdx.graphics.getHeight();
        float touchedX = Gdx.input.getX() * WORLD_WIDTH / Gdx.graphics.getWidth();
        if(letGo && Gdx.input.isTouched() && touchedY >= 0 && touchedY <= 150
                && touchedX >= 300 && touchedX <= WORLD_WIDTH) {
            //Switches between skins if the player has unlocked them
            if(!greedyGobo.getBlueInUse() && greedyGobo.getBlue() && skinCounter < 1){
                greedyGobo.setBlueInUse(true);
                greedyGobo.setHolyInUse(false);
                greedyGobo.setToxicInUse(false);
                skinCounter = 1;
            }
            else if(!greedyGobo.getHolyInUse() && greedyGobo.getHoly() && skinCounter < 2){
                greedyGobo.setBlueInUse(false);
                greedyGobo.setToxicInUse(false);
                greedyGobo.setHolyInUse(true);
                skinCounter = 2;
            }
            else if(!greedyGobo.getToxicInUse() && greedyGobo.getToxic() && skinCounter < 3){
                greedyGobo.setBlueInUse(false);
                greedyGobo.setToxicInUse(true);
                greedyGobo.setHolyInUse(false);
                skinCounter = 3;
            }
            else if(skinCounter != 0){
                skinCounter = 0;
                greedyGobo.setBlueInUse(false);
                greedyGobo.setToxicInUse(false);
                greedyGobo.setHolyInUse(false);
            }

            letGo = false;
        }
        else if(!letGo && !Gdx.input.isTouched()){letGo = true;} //Make sure you only click once
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the leaves
    */
    private void updateLeaves(){
        updateLeavesPosition();                         //Moves each leaf
        if(leaves.size() < 6){ createNewLeaves(); }     //Creates new leaves if there is space
        removeLeaves();                                 //Remvoes leaves that left the screen
    }

    /*
    Input: Void
    Output: Void
    Purpose: Moves the leaves
    */
    private void updateLeavesPosition(){
        for(Leaf leaf : leaves){ leaf.updateLeaf();}
    }

    /*
    Input: Void
    Output: Void
    Purpose: If there's space for new leaves make them
    */
    private void createNewLeaves(){
        //Gives each leaf random size and speed
        Leaf leaf = new Leaf(MathUtils.random(3, 7), leavesTextures);
        leaves.add(leaf);
    }

    /*
    Input: Void
    Output: Void
    Purpose: If any of the leaves leave the screen delete them
    */
    private void removeLeaves() {
        Vector<Leaf> removedLeaves = new Vector<>();
        for(Leaf leaf : leaves) { if (leaf.getX() < -leaf.getWidth()){ removedLeaves.add(leaf); } }
        for(Leaf leaf: removedLeaves){ leaves.remove(leaf); }
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
        //Draw background
        batch.draw(backgroundTexture, 0,0);
        //Draw the leaves
        for(ParentObject leaf : leaves){leaf.draw(batch);}
        //Draw the foreground
        batch.draw(foregroundTexture, 0,0);
        //If the player clicked on gobo they will be able to have a blue one
        if(greedyGobo.getBlueInUse()){batch.draw(blueGoboTexture, 293, 0);}
        else if(greedyGobo.getToxicInUse()){batch.draw(toxicGoboTexture, 293, 0);}
        else if(greedyGobo.getHolyInUse()){
            batch.draw(haloTexture, 365, 140);
            batch.draw(holyGoboTexture, 293, 0);
        }
        //Draw the boarder and high score on the pig
        drawHighScore();
        //Draw the pop up menu
        if(helpFlag  || creditsFlag){batch.draw(popUpTexture, 10, 10, WORLD_WIDTH - 20, WORLD_HEIGHT-20);}
        batch.end();

        menuStage.draw(); // Draws the buttons

        batch.begin();
        //Draws the Play|Help|Credits text on buttons
        if(!helpFlag && !creditsFlag){drawButtonText();}
        //Draws the Help Text
        else if(helpFlag){drawHelpScreen();}
        //Draws the credits text
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

    /*
    Input: Void
    Output: Void
    Purpose: Draws the boarder and the current high score
    */
    private void drawHighScore(){
        bitmapFont.getData().setScale(0.2f);
        //Boarder
        batch.draw(scoreBoardTexture, 375, 30, scoreBoardTexture.getWidth()/3f - 5, scoreBoardTexture.getHeight()/2f);
        //Text
        centerText(bitmapFont, greedyGobo.getCurrentHighScore(), 375 + scoreBoardTexture.getWidth()/6f - 2.5f, 30 + scoreBoardTexture.getHeight()/4f);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the text on the Play|Help|Credits buttons
    */
    private void drawButtonText(){
        bitmapFont.getData().setScale(0.5f);
        for(int i = 0; i < 3; i ++) {
            centerText(bitmapFont, buttonText[i], WORLD_WIDTH / 2,
                    WORLD_HEIGHT / 3 + 118/5f + 3 - (118/ 2f - 10) * i);
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the help screen
    */
    private void drawHelpScreen(){
        //Sets up offset if we are on PC
        float offSet = 0;
        //If we are on Phone changes offset
        if(greedyGobo.getPhone()){
            offSet = 20;
            bitmapFont.getData().setScale(0.3f);
            //Tells user how to hold the phone
            centerText(bitmapFont, "Hold the phone down as if it were laying on a table.", WORLD_WIDTH/2f, WORLD_HEIGHT - 70);
        }

        //Title
        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "How to Play", WORLD_WIDTH/2f, WORLD_HEIGHT-40);

        //Starting the game
        bitmapFont.getData().setScale(0.3f);
        batch.draw(goboSpriteSheetTexture[0][5], 35, WORLD_HEIGHT - 100, 30, 30);
        batch.draw(goboSpriteSheetTexture[3][3], WORLD_WIDTH-65, WORLD_HEIGHT - 100, 30, 30);
        batch.draw(touchControlTexture, 45, WORLD_HEIGHT - 110, 30, 30);
        centerText(bitmapFont, "When you start the game, grab and hold onto Gobo", WORLD_WIDTH/2f, WORLD_HEIGHT - 75- offSet);
        centerText(bitmapFont, "then release him onto the field.", WORLD_WIDTH/2f, WORLD_HEIGHT - 90- offSet);

        //Controlling Gobo's movement
        batch.draw(movementControlTexture, 35, WORLD_HEIGHT - 140, 30, 30);
        batch.draw(goboSpriteSheetTexture[2][1], WORLD_WIDTH-65, WORLD_HEIGHT - 140, 30, 30);
        //Different text based on if you're on PC or Android
        if(greedyGobo.getPhone()) { centerText(bitmapFont, "Tilt the phone to move Gobo.", WORLD_WIDTH / 2f, WORLD_HEIGHT - 125 - offSet); }
        else{ centerText(bitmapFont, "Use WASD to move Gobo.", WORLD_WIDTH / 2f, WORLD_HEIGHT - 125 - offSet); }

        //Collectibles
        batch.draw(coinTextures[0][2],  35, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[0][1], 45, WORLD_HEIGHT - 180, 30, 30);
        batch.draw(coinTextures[0][0], 55, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[1][2], WORLD_WIDTH - 65, WORLD_HEIGHT - 190, 30, 30);
        batch.draw(coinTextures[1][1], WORLD_WIDTH - 75, WORLD_HEIGHT - 180, 30, 30);
        batch.draw(coinTextures[1][0], WORLD_WIDTH - 85, WORLD_HEIGHT - 190, 30, 30);
        centerText(bitmapFont, "Collect the coins and avoid the crosses.", WORLD_WIDTH/2f, WORLD_HEIGHT - 160 - offSet);

        //Enemies
        batch.draw(knightSpriteSheetTexture[0][0],  35, WORLD_HEIGHT - 240, 30, 30);
        batch.draw(abbotSpriteSheetTexture[0][0], WORLD_WIDTH-65, WORLD_HEIGHT - 240, 30, 30);
        centerText(bitmapFont, "Beware the abbots and the knights", WORLD_WIDTH/2f, WORLD_HEIGHT - 195 - offSet);
        centerText(bitmapFont, "Abbots will stun you and the knights will kill you.", WORLD_WIDTH/2f, WORLD_HEIGHT - 210- offSet);

        //Stun
        batch.draw(goboSpriteSheetTexture[1][3],  35, WORLD_HEIGHT - 280, 30, 30);
        batch.draw(touchControlTexture, 45, WORLD_HEIGHT - 290, 30, 30);
        centerText(bitmapFont, "If you get stunned tap on Gobo to wake him up.", WORLD_WIDTH/2f, WORLD_HEIGHT - 245- offSet);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the credits screen
    */
    private void drawCredits(){
        //Draws cross to fill out empty space
        batch.draw(coinTextures[1][1], WORLD_WIDTH/2 + 100, WORLD_HEIGHT - 160, 60, 60);
        batch.draw(coinTextures[1][1], WORLD_WIDTH/2 - 150, WORLD_HEIGHT - 160, 60, 60);

        //Title
        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "Credits", WORLD_WIDTH/2f, WORLD_HEIGHT-45);
        bitmapFont.getData().setScale(0.32f);

        centerText(bitmapFont, "Programming & Art", WORLD_WIDTH/2f, WORLD_HEIGHT - 80);
        centerText(bitmapFont, "Sebastian Grygorczuk", WORLD_WIDTH/2f, WORLD_HEIGHT - 95);

        centerText(bitmapFont, "Music", WORLD_WIDTH/2f, WORLD_HEIGHT - 125);
        centerText(bitmapFont, "Yuriy Lehki", WORLD_WIDTH/2f, WORLD_HEIGHT - 140);

        centerText(bitmapFont, "SFX - Freesound", WORLD_WIDTH/2f, WORLD_HEIGHT - 170);
        centerText(bitmapFont, "Bratish", WORLD_WIDTH/2f - 120, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "pedrocnbp", WORLD_WIDTH/2f, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "msavioti", WORLD_WIDTH/2f + 120, WORLD_HEIGHT - 190);
        centerText(bitmapFont, "InspectorJ", WORLD_WIDTH/2f - 120, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "LiamG_SFX", WORLD_WIDTH/2f, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "Deatlev", WORLD_WIDTH/2f + 120, WORLD_HEIGHT - 210);
        centerText(bitmapFont, "joe_anderson22", WORLD_WIDTH/2f - 120, WORLD_HEIGHT - 230);
        centerText(bitmapFont, "thefsoundman", WORLD_WIDTH/2f, WORLD_HEIGHT - 230);
        centerText(bitmapFont, "FunWithSound", WORLD_WIDTH/2f + 120, WORLD_HEIGHT - 230);

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
        menuStage.dispose();

        music.dispose();

        backgroundTexture.dispose();
        foregroundTexture.dispose();
        popUpTexture.dispose();
        scoreBoardTexture.dispose();
        touchControlTexture.dispose();
        movementControlTexture.dispose();
    }


}