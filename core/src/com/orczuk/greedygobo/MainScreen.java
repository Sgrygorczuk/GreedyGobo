package com.orczuk.greedygobo;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.orczuk.greedygobo.Objects.Bishop;
import com.orczuk.greedygobo.Objects.Coin;
import com.orczuk.greedygobo.Objects.Knight;
import com.orczuk.greedygobo.Objects.PlayerCharacter;

import java.util.Vector;

class MainScreen extends ScreenAdapter {

    /*
    Dimensions -- Units the screen has
     */
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    /*
    Image processing -- Objects that modify the view and textures
    */
    private Viewport viewport;			 //The screen where we display things
    private Camera camera;				 //The camera viewing the viewport
    private SpriteBatch batch = new SpriteBatch();			 //Batch that holds all of the textures

    private ShapeRenderer shapeRendererEnemy;       //Creates the wire frames for enemies
    private ShapeRenderer shapeRendererUser;        //Creates the wire frame for user
    private ShapeRenderer shapeRendererBackground;  //Creates the wire frame for background objects
    private ShapeRenderer shapeRendererCollectible; //Creates wireframe for collectibles

    //The buttons that will be used in the menu
    private Stage menuStage;
    private ImageButton[] menuButtons;

    //Game object that holds the settings
    private GreedyGobo greedyGobo;

    //Music that will start
    private Music music;

    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();
    //Font for viewing phone stats in developer mode
    private BitmapFont bitmapFontDeveloper = new BitmapFont();

    //Moving objects
    //Player character
    PlayerCharacter player;
    //All the coins/crosses on screen
    private Vector<Coin> coins = new Vector<>();
    //All the Abbots on screen
    private Vector<Bishop> bishops = new Vector<>();
    //All the Knights on screen
    private Vector<Knight> knights = new Vector<>();

    //Textures
    private TextureRegion[][] coinTextures;             //The coin texture spire sheet
    private TextureRegion[][] goboSpriteSheetTexture;   //The gobo texture spire sheet
    private TextureRegion[][] abbotSpriteSheetTexture;  //The abbot texture sprite sheet
    private TextureRegion[][] knightSpriteSheetTexture; //The knight texture sprite sheet
    private Texture backgroundTexture;                  //Background
    private Texture UITexture;                          //The UI beam on the right of the screen
    private Texture scoreBoardTexture;                  //The score board in the middle of the background
    private Texture goboLifeHeadTexture;                //Gobo alive head
    private Texture goboDeadHeadTexture;                //Gobo dead head
    private Texture goboSpawnTexture;                   //Gobo spawn point on UI beam
    private Texture popUpTexture;                       //Pop up menu to show menu buttons and Help screen
    private Texture touchControlTexture;                //The touch controls image
    private Texture movementControlTexture;             //The movement controls image

    //Names of buttons
    private String[] menuButtonText = new String[]{"Main Menu", "Restart", "Help", "Sound Off", "Sound On"};
    private float amountOfKnights = 1;  //Amount of knights currently allowed on screen
    private float amountOfAbbots = 3;   //Amount of abbots currently allowed on screen
    private float score = 0;            //Current score
    private float speed = 1;            //Current movement speed of abbots/knights/coins/crosses
    private float maxScore = 5;         //Current max score to increased difficulty
    private float size = 1;             //Current size of the objects in game
    private float coinBoost = 0f;       //Current increase in coin value range
    private int lives = 3;              //How many lives player has left
    private float currentPlayerWidth = 10;  //Current width of the player used by knights/abbots/coins/crosses to determine their width

    //Flags
    private boolean blueUnlocked;
    private boolean holyUnlocked;
    private boolean toxicUnlocked;
    private boolean developerMode = false;      //Developer mode shows hit boxes and phone data
    private boolean pausedFlag = false;         //Stops the game from updating
    private boolean endFlag = false;            //Tells us game has been lost
    private float sfxVolume = 1f;               //Current sfx volume
    private boolean helpFlag = false;           //Tells us if help flag is on or off
    private boolean newHighScoreFlag = false;   //Tells us if user beat their last high score

    //Timing variable used to stop the abbot bounce effect from stacking
    private boolean abbotSFXFlag = false;
    private static final float ABBOT_SFX_TIME = 0.5F;
    private float abbotSfxTimer = ABBOT_SFX_TIME;

    /*
    Input: SpaceHops
    Output: Void
    Purpose: Grabs the info from main screen that holds asset manager
    */
    MainScreen(GreedyGobo greedyGobo) { this.greedyGobo = greedyGobo;}


    /*
    Input: The width and height of the screen
    Output: Void;
    Purpose: Updates the dimensions of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Initializes all the variables that are going to be displayed
    */
    @Override
    public void show() {
        showCamera();       //Set up the camera
        showTextures();     //Sets up textures
        showObjects();      //Sets up player and font
        showButtons();      //Sets up the buttons
        showMusic();        //Sets up music
        if(developerMode){showRender();}    //If in developer mode sets up the redners
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
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);  //Stretches the image to fit the screen
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up all of the textures
    */
    private void showTextures(){
        //All the sprite sheet textures
        Texture coinTexturePath = new Texture(Gdx.files.internal("Sprites/ObjectSpriteSheet.png"));
        coinTextures = new TextureRegion(coinTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture goboTexturePath;
        if(greedyGobo.getBlueInUse()){ goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheetBlue.png"));}
        else if(greedyGobo.getHolyInUse()){ goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheetHoly.png"));}
        else if(greedyGobo.getToxicInUse()){ goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheetGreen.png"));}
        else{ goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheet.png")); }
        goboSpriteSheetTexture  = new TextureRegion(goboTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture abbotTexturePath = new Texture(Gdx.files.internal("Sprites/AbbotSpriteSheet.png"));
        abbotSpriteSheetTexture = new TextureRegion(abbotTexturePath).split(420, 420);
        Texture knightTexturePath = new Texture(Gdx.files.internal("Sprites/KnightSpriteSheet.png"));
        knightSpriteSheetTexture = new TextureRegion(knightTexturePath).split(420, 420);backgroundTexture = new Texture(Gdx.files.internal("Sprites/Background.png"));

        //One image texutres
        UITexture = new Texture(Gdx.files.internal("Sprites/UI.png"));
        scoreBoardTexture = new Texture(Gdx.files.internal("Sprites/ScoreBoard.png"));
        if(greedyGobo.getBlueInUse()){goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadBlue.png")); }
        else if(greedyGobo.getToxicInUse()){goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadToxic.png")); }
        else if(greedyGobo.getHolyInUse()){goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadHoly.png")); }
        else{goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHead.png")); }
        goboDeadHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadOff.png"));
        goboSpawnTexture = new Texture(Gdx.files.internal("Sprites/GoboSpawn.png"));
        popUpTexture = new Texture(Gdx.files.internal("UI/PopUpBoarder.png"));

        //Diffrent texture based on if we're on PC or Android
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
    Purpose: Sets up the button
    */
    private void showButtons(){
        menuStage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(menuStage); //Gives controll to the stage for clicking on buttons
        //Sets up 6 Buttons
        menuButtons = new ImageButton[6];

        setUpOpenMenuButton();  //Sets up button used to open the menu
        setUpMenuButtons();     //Sets up the button in the menu
        setUpExitButton();      //Sets up the button used to exit Help
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up button used to open the menu
    */
    private void setUpOpenMenuButton(){
        //Set up the texture
        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/MenuButton.png"));
        TextureRegion[][] buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        //Place the button
        menuButtons[0] =  new ImageButton(new TextureRegionDrawable(buttonSpriteSheet[0][0]), new TextureRegionDrawable(buttonSpriteSheet[0][1]));
        menuButtons[0].setPosition(430 - buttonSpriteSheet[0][0].getRegionWidth()/2f, WORLD_HEIGHT - 10 - buttonSpriteSheet[0][0].getRegionHeight());
        menuStage.addActor(menuButtons[0]);

        //If button has not been clicked turn on menu and pause game,
        //If the menu is up turn it off and un-pause the game
        menuButtons[0].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if (!endFlag) {
                    playButtonSFX();
                    pausedFlag = !pausedFlag;
                    for (int i = 1; i < 5; i++) {
                        if (pausedFlag) { menuButtons[i].setVisible(true); }  //Turns on 1-5 buttons
                        else{menuButtons[i].setVisible(false);}               //Turns off 1-5 buttons
                    }
                }
            }
        });
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the button in the menu
    */
    private void setUpMenuButtons(){
        //Sets up the texture
        Texture popUpButtonTexturePath = new Texture(Gdx.files.internal("UI/ButtonSpriteSheet.png"));
        final TextureRegion[][] popUpButtonSpriteSheet = new TextureRegion(popUpButtonTexturePath).split(117, 47); //Breaks down the texture into tiles

        //Sets up the position of the buttons in a square 2x2
        float x;
        float y;
        for(int i = 1; i < 5; i ++){
            menuButtons[i] =  new ImageButton(new TextureRegionDrawable(popUpButtonSpriteSheet[i-1][0]), new TextureRegionDrawable(popUpButtonSpriteSheet[i-1][1]));
            if(i == 1 || i == 3){ x = 380/2f - 5 - popUpButtonSpriteSheet[0][0].getRegionWidth();}
            else { x = 380/2f + 5;}
            if(i < 3){ y = WORLD_HEIGHT/2f - 10 + popUpButtonSpriteSheet[0][0].getRegionHeight()/2f;}
            else{y = WORLD_HEIGHT/2f - 10 - popUpButtonSpriteSheet[0][0].getRegionHeight();}
            menuButtons[i].setPosition(x, y);
            menuStage.addActor(menuButtons[i]);
            menuButtons[i].setVisible(false);       //Initially all the buttons are off

            //Sets up each buttons function
            final int finalI = i;
            menuButtons[i].addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    playButtonSFX();
                    //Returns to the main menu
                    if(finalI == 1){
                        music.stop();
                        greedyGobo.setScreen(new MenuScreen(greedyGobo));
                    }
                    //Restarts the game
                    else if(finalI == 2){ restart(); }
                    //Turns on the help menu
                    else if(finalI == 3){
                        helpFlag = true;
                        //Turns off all the buttons
                        for(ImageButton imageButton : menuButtons){ imageButton.setVisible(false); }
                        //Turns exit button on
                        menuButtons[5].setVisible(true);
                    }
                    //Turns on/off the sound
                    else {soundButtonAction(finalI, popUpButtonSpriteSheet);}
                }
            });
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Changes the button image and turn the sound on and off
    */
    private void soundButtonAction(final int finalI, final TextureRegion[][] popUpButtonSpriteSheet){
        //Gets rid of current button
        menuButtons[finalI].setVisible(false);
        //Turns the volume down
        if(sfxVolume == 1f) {
            music.stop();
            sfxVolume = 0;
            menuButtons[finalI] =  new ImageButton(new TextureRegionDrawable(popUpButtonSpriteSheet[4][0]), new TextureRegionDrawable(popUpButtonSpriteSheet[4][1]));
        }
        //Turns the sound on
        else{
            music.play();
            sfxVolume = 1;
            menuButtons[finalI] =  new ImageButton(new TextureRegionDrawable(popUpButtonSpriteSheet[3][0]), new TextureRegionDrawable(popUpButtonSpriteSheet[3][1]));
        }
        //Creates new button in the place of the old one with a different image
        menuButtons[finalI].setPosition(380/2f + 5, WORLD_HEIGHT/2f - 10 - popUpButtonSpriteSheet[0][0].getRegionHeight());
        menuStage.addActor(menuButtons[finalI]);
        //Adds in this function if the button is clicked again
        menuButtons[finalI].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                soundButtonAction(finalI, popUpButtonSpriteSheet);
            }
        });
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the button used to exit the help menu
    */
    private void setUpExitButton(){
        //Sets up the texture
        Texture exitButtonTexturePath = new Texture(Gdx.files.internal("UI/ExitButton.png"));
        TextureRegion[][] exitButtonSpriteSheet = new TextureRegion(exitButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        //Sets up the position
        menuButtons[5] =  new ImageButton(new TextureRegionDrawable(exitButtonSpriteSheet[0][0]), new TextureRegionDrawable(exitButtonSpriteSheet[0][1]));
        menuButtons[5].setPosition(WORLD_WIDTH - 50, WORLD_HEIGHT - 50);
        menuButtons[5].setWidth(20);
        menuButtons[5].setHeight(20);
        menuStage.addActor(menuButtons[5]);
        menuButtons[5].setVisible(false);
        //Sets up to turn of the help menu if clicked
        menuButtons[5].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                playButtonSFX();
                helpFlag = false;
                //Turn on all buttons but turn off this one
                for (ImageButton imageButton : menuButtons) { imageButton.setVisible(true); }
                menuButtons[5].setVisible(false);
            }
        });
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up player and the font
    */
    private void showObjects(){
        blueUnlocked = greedyGobo.getBlue();
        holyUnlocked = greedyGobo.getHoly();
        toxicUnlocked = greedyGobo.getToxic();
        player = new PlayerCharacter(425, 180, goboSpriteSheetTexture, new Texture(Gdx.files.internal("Sprites/Portal.png")));
        if(greedyGobo.getAssetManager().isLoaded("Fonts/GreedyGobo.fnt")){bitmapFont = greedyGobo.getAssetManager().get("Fonts/GreedyGobo.fnt");}
        bitmapFont.getData().setScale(0.6f);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the music for the level
    */
    private void showMusic(){
        music = greedyGobo.getAssetManager().get("Music/GoboLevelTheme.wav", Music.class);
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Plays the abbot sfx as long as it didn't play too recently
    */
    private void playAbbotSFX() {
        if (!abbotSFXFlag) {
            greedyGobo.getAssetManager().get("SFX/Abbot.wav", Sound.class).play(sfxVolume);
            abbotSFXFlag = true;
        }
    }
    /*
    Input: Void
    Output: Void
    Purpose: Plays the sound effect when called
    */
    private void playKnightSFX() { greedyGobo.getAssetManager().get("SFX/Knight.wav", Sound.class).play(sfxVolume); }
    private void playCoinSFX() { greedyGobo.getAssetManager().get("SFX/Coin.wav", Sound.class).play(sfxVolume); }
    private void playCrossSFX() { greedyGobo.getAssetManager().get("SFX/Cross.wav", Sound.class).play(sfxVolume); }
    private void playSlapSFX() { greedyGobo.getAssetManager().get("SFX/Slap.wav", Sound.class).play(sfxVolume); }
    private void playBagSFX() { greedyGobo.getAssetManager().get("SFX/Bag.wav", Sound.class).play(sfxVolume); }
    private void playDropSFX() { greedyGobo.getAssetManager().get("SFX/Drop.wav", Sound.class).play(sfxVolume); }
    private void playButtonSFX() { greedyGobo.getAssetManager().get("SFX/Button.wav", Sound.class).play(1/2f); }
    private void playHighScoreSFX() { greedyGobo.getAssetManager().get("SFX/HighScore.mp3", Sound.class).play(1/2f); }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the different renders to draw objects in wireframe
    */
    private void showRender(){
        //Enemy
        shapeRendererEnemy = new ShapeRenderer();
        shapeRendererEnemy.setColor(Color.RED);

        //User
        shapeRendererUser = new ShapeRenderer();
        shapeRendererUser.setColor(Color.GREEN);

        //Background
        shapeRendererBackground = new ShapeRenderer();
        shapeRendererBackground.setColor(Color.WHITE);

        //Intractable
        shapeRendererCollectible = new ShapeRenderer();
        shapeRendererCollectible.setColor(Color.BLUE);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws all of the variables on the screen
    */
    @Override
    public void render(float delta) {
        if(!pausedFlag) { update(delta); }
        clearScreen();
        draw();
        if (developerMode) {
            renderEnemy();
            renderUser();
            renderCollectible();
            renderBackground();
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the enemy/obstacle wireframe
    */
    private void renderEnemy(){
        shapeRendererEnemy.setProjectionMatrix(camera.projection);      		                 //Screen set up camera
        shapeRendererEnemy.setTransformMatrix(camera.view);            			                 //Screen set up camera
        shapeRendererEnemy.begin(ShapeRenderer.ShapeType.Line);         		                 //Sets up to draw lines
        for(Knight knight : knights){knight.drawDebug(shapeRendererEnemy);}
        for(Bishop bishop : bishops){bishop.drawDebug(shapeRendererEnemy);}
        for(Coin coin : coins){if(!coin.isGoodCoin()){coin.drawDebug(shapeRendererEnemy);}}
        shapeRendererEnemy.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws user wireframe
    */
    private void renderUser(){
        shapeRendererUser.setProjectionMatrix(camera.projection);    //Screen set up camera
        shapeRendererUser.setTransformMatrix(camera.view);           //Screen set up camera
        shapeRendererUser.begin(ShapeRenderer.ShapeType.Line);       //Sets up to draw lines
        player.drawDebug(shapeRendererUser);
        shapeRendererUser.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the background object and UI wireframes
    */
    private void renderBackground(){
        shapeRendererBackground.setProjectionMatrix(camera.projection);                 //Screen set up camera
        shapeRendererBackground.setTransformMatrix(camera.view);                        //Screen set up camera
        shapeRendererBackground.begin(ShapeRenderer.ShapeType.Line);                    //Starts to draw
        shapeRendererBackground.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws wireframe of the collectibles -- needs to be redone along with collectible objects
    */
    private void renderCollectible(){
        shapeRendererCollectible.setProjectionMatrix(camera.projection);
        shapeRendererCollectible.setTransformMatrix(camera.view);
        shapeRendererCollectible.begin(ShapeRenderer.ShapeType.Line);
        for(Coin coin : coins){if(coin.isGoodCoin()){coin.drawDebug(shapeRendererCollectible);}}
        shapeRendererCollectible.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates all the moving components and game variables
    */
    private void update(float delta){
        //As long as the game isn't over update the player
        if(!endFlag){updatePlayer(delta);}
        //If game is over only update the players animation and position by portal fall
        else{
            updatePortal();
            if(player.getY() != 180){updatePlayerFalling();}
            player.updateAnimation(delta);
        }
        updateCoin(delta);                          //Updates the coins
        updateAbbots(delta);                       //Updates the abbots
        updateKnight(delta);                        //Updates the knights
        if(abbotSFXFlag){updateSFXTimer(delta);}    //Updates the abbot sfx timer
        if(lives == 0){endGame();}                  //If player has 0 lives end the game
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Counts when the abbot SFX can be played again
    */
    private void updateSFXTimer(float delta) {
        abbotSfxTimer -= delta;
        if (abbotSfxTimer <= 0) {
            abbotSfxTimer = ABBOT_SFX_TIME;
            abbotSFXFlag = false;
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Puts the game in end game state
    */
    private void endGame(){
        endFlag = true;                     //Sets the game to have ended
        player.setStunned(true);            //Makes the player stunned
        player.setInArena(true);            //Used to keep the stunned animation playing
        //Show only the Exit and Restart buttons
        menuButtons[1].setVisible(true);
        menuButtons[2].setVisible(true);
        //If new high score has been reached play the happy music and save the new score
        if(Float.parseFloat(greedyGobo.getCurrentHighScore()) < score && !newHighScoreFlag){
            playHighScoreSFX();
            newHighScoreFlag = true;
            greedyGobo.setCurrentHighScore("" + (float) ((float)Math.round(score * 100.0) / 100.0));
        }
        if(!blueUnlocked && score >= 75){greedyGobo.setBlue(true);}
        if(!toxicUnlocked && score >= 150){greedyGobo.setToxic(true);}
    }

    /*
    Input: Void
    Output: Void
    Purpose: Restarts the game to base state
    */
    private void restart(){
        //Gets rid of all the enemies and collectibles
        bishops.removeAllElements();
        knights.removeAllElements();
        coins.removeAllElements();
        //Resets the number of knights and abbots on screen
        amountOfKnights = 1;
        amountOfAbbots = 3;
        lives = 3;      //Sets lives back to 3
        score = 0;      //Sets score back 0
        size = 1;       //Set the size to 1
        speed = 1;      //Set the speed to 1
        maxScore = 5;   //Set the current goal to 5
        coinBoost = 0f; //Sets the coin value booster back to 0
        //Sets the player back to original size
        currentPlayerWidth = 10;
        player.setDimensions(currentPlayerWidth);
        //Sets all buttons off except the turn menu on button
        for(int i = 1; i < 5; i++){menuButtons[i].setVisible(false);}
        endFlag = false;                //Game is no longer over
        pausedFlag = false;             //Game is not paused
        newHighScoreFlag = false;       //New high score has not been reached
        player.setStunned(false);       //Player is not stunned
        player.setInArena(false);       //Player is not in the arena
        player.setPosition(425, 180);   //Player is back in spawn
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Restarts the game to base state
    */
    private void updatePlayer(float delta){
        //Before the player starts collecting coins and moving around the screen they first need to drag the gobo onto the field
        if(!player.isInArena() && !player.isFalling()) {updatePlayerStart();}
        else if(player.isFalling()){updatePlayerFalling();}
        //Once on the field they can slide around
        else{ updatePlayerPosition(delta); }
        //If portal is not closed update portal
        if(player.getPortalOpening() != 0){updatePortal();}
        //Updates the animation
        player.updateAnimation(delta);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Used for interacting with gobo while he's in spawn
        You can pick him up and drop him in the arena if you drop him anywhere else he will
        go back to the spawn point
    */
    private void updatePlayerStart(){
        //First we look at if the player is touching the screen
        if (Gdx.input.isTouched()){
            float hitBoxExtender = 10;
            //If they are we check where
            float touchedY = WORLD_HEIGHT - Gdx.input.getY()*WORLD_HEIGHT/Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX()*WORLD_WIDTH/Gdx.graphics.getWidth();
            //If they touch on top of the gobo they are now holding it
            if(touchedY >= player.getY() - hitBoxExtender && touchedY <= player.getY() + player.getWidth() + hitBoxExtender
                    && touchedX >= player.getX() - hitBoxExtender  && touchedX <= player.getX() + player.getWidth() + hitBoxExtender && !player.isHeld()){
                playBagSFX();
                player.setHeld(true);
                player.setDimensions(player.getWidth() + 10);
            }
            //If gobo is being held he can be dragged around the screen
            else if(player.isHeld()){ player.setPosition(touchedX, touchedY); }
        }
        //If the player lets go of gobo and he's on the field then he can slide around and interact with the objects
        else if(player.isHeld() && player.getX() + player.getWidth() < 380){
            playDropSFX();
            player.setHeld(false);
            player.setInArena(true);
            player.setDimensions(player.getWidth() - 10);
        }
        //If the player lets go but gobo is not on the playing field he ports back to the spawn point
        else if(player.isHeld()){
            playDropSFX();
            player.setHeld(false);
            player.setPosition(425, 180);
            player.setDimensions(player.getWidth() - 10);
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Used to update the state of the portal that gobo falls through when killed
    */
    private void updatePortal(){
        //Portal opening
        if(player.getPortalOpening() == 1 && player.getPortalWidth() <=  player.getOldWidth() * 1.5){
            //Grown portal and adjust x/y to look like it's not moving from where it is
            player.setPortalWidth(player.getPortalWidth() + player.getOldWidth()/10f);
            player.setOldCoordinates(player.getOldX() - player.getOldWidth()/10f, player.getOldY() - player.getOldWidth()/10f);
        }
        //Portal closing
        else if(player.getPortalOpening() == 2) {
            //Shrink portal and adjust x/y to look like it's not moving from where it is
            player.setPortalWidth(player.getPortalWidth() - player.getOldWidth()/10f);
            player.setOldCoordinates(player.getOldX() + player.getOldWidth()/15f, player.getOldY() + player.getOldWidth()/15f);
            //If portal fully closed it's no longer active
            if(player.getPortalWidth() <= 0){ player.setPortalOpening(0); }
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Update gobo as he's falling through the portal
    */
    private void updatePlayerFalling(){
        //Gobo is falling down in the UI beam
        if(player.getPortalOpening() != 1){
            //Make him fall
            player.setPosition(player.getX(), player.getY() - 4);
            //If he reached point reset him to act as if just spawned
            if(player.getY() <= 180){
                player.setPosition(player.getX(), 180);
                player.setFalling(false);
                player.setStunned(false);
                playDropSFX();
            }
        }
        //Gobo if falling down the portal
        else {
            //Shrink gobo to look like he's falling
            player.setDimensions(player.getWidth() - player.getOldWidth()/100f);
            player.setPosition(player.getX() + player.getOldWidth()/250f, player.getY() - player.getOldWidth()/400f);
            //If Gobo is small enough move him above UI beam
            if (player.getWidth() < 1) {
                //Set above spawn position
                player.setPosition(425, WORLD_HEIGHT + player.getOldWidth());
                //Give him his old size back
                player.setDimensions(player.getOldWidth());
                //Make the portal close
                player.setPortalOpening(2);
            }
        }
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Update movement on screen and his state of being stunned
    */
    private void updatePlayerPosition(float delta){
        int x = 0;
        int y = 0;
        int z = 0;

        //If we're on phone read the tilt using Accelerometer
        if( greedyGobo.getPhone()) {
            x = (int) Gdx.input.getAccelerometerX();
            y = (int) Gdx.input.getAccelerometerY();
            z = (int) Gdx.input.getAccelerometerZ();
        }

        //If we're on phone and Gobo is not stunned use the phone read out to move him
        if(z > Math.abs(x) && z > Math.abs(y) && !player.isStunned() && greedyGobo.getPhone()) {
            player.boostX(y);
            player.boostY(-x);
            playerDirection(y, x);      //Change his texture
        }
        //else if we're on pc use the WASD to move gobo around
        else if(!player.isStunned() && !greedyGobo.getPhone())
        {
            //Move Up
            if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
                player.boostY(2f);
                player.boostX(0);
                player.updateDirection(1);
                player.setStopped(false);
            }
            //Move Down
            else if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
                player.boostY(-2f);
                player.boostX(0);
                player.updateDirection(0);
                player.setStopped(false);
            }
            //Move Left
            else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
                player.boostX(-2f);
                player.boostY(0);
                player.updateDirection(3);
                player.setStopped(false);
            }
            //Move Right
            else if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
                player.boostX(2f);
                player.boostY(0);
                player.updateDirection(2);
                player.setStopped(false);
            }
            //Lower gobo's speed to 0
            else{
                player.speedUpdate();
                if(Math.abs(player.getSpeedY())  < 0.2f && Math.abs(player.getSpeedX()) < 0.2f){
                    player.updateDirection(0);
                    player.setStopped(true);
                }
            }
        }
        //If the player gets hit by a priest they get stunned, they can recover by either clicking on the character or waiting it out
        else if(player.isStunned()) {
            float hitBoxIncrease = 20;
            float touchedY = WORLD_HEIGHT - Gdx.input.getY() * WORLD_HEIGHT / Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX() * WORLD_WIDTH / Gdx.graphics.getWidth();
            //Clicking destuns the player
            if (Gdx.input.isTouched() && touchedY >= player.getY() - hitBoxIncrease && touchedY <= player.getY() + player.getWidth() + hitBoxIncrease && touchedX >= player.getX() - hitBoxIncrease && touchedX <= player.getX() + player.getWidth() + hitBoxIncrease && !player.isHeld()) {
                playSlapSFX();
                player.deStun();
            }
            //Counts down till they naturally destun
            else { player.stunnedTimer(delta); }
        }
        //Update his position based on the inputs
        player.update();
    }

    /*
    Input: X and Y
    Output: Void
    Purpose: Use the X and Y given to determine which way gobo is facing based on which is stronger
    */
    private void playerDirection(float axisX, float axisY){
        //Pick direction based on which is bigger
        if(axisY > 0 && axisY > axisX){player.updateDirection(0);}
        else if(axisY < 0 && Math.abs(axisY) > axisX){player.updateDirection(1);}
        else if(axisX > 0 && axisX > axisY){player.updateDirection(2);}
        else if(axisX < 0 && Math.abs(axisX) > axisY){player.updateDirection(3);}

        //If they are the same he should face the player and not update animation
        if(axisX == axisY){
            player.setStopped(true);
            player.updateDirection(0);
        }
        //Make sure he update animations
        else{player.setStopped(false);}
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Central function for updating the coins/crosses
    */
    private void updateCoin(float delta){
        updateCoinPosition(delta);                  //Update the position
        if(coins.size() < 6){ createNewCoin(); }    //If need more make more
        removeCoin();                               //If any of them are collected or off screen remove
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Move coin/cross
    */
    private void updateCoinPosition(float delta){ for(Coin coin : coins){ coin.update(coin.getDirection(), delta); } }

    /*
    Input: Void
    Output: Void
    Purpose: If new coins need to be created make them
    */
    private void createNewCoin(){
        //Give them a random chance of being coin or cross, then based on state of game gives them size, speed and value booster
        Coin coin = new Coin(MathUtils.randomBoolean(), size, speed, coinBoost, coinTextures);
        coins.add(coin);
    }

    /*
    Input: Void
    Output: Void
    Purpose: If they interact with player of leave screen remove them
    */
    private void removeCoin() {
        Vector<Coin> removedCoins = new Vector<>();
        for (Coin coin : coins) {
            //Checks if the coin has left the screen
            if (coin.getX() > 380 + coin.getWidth() || coin.getX() < -coin.getWidth() ||
            coin.getY() > WORLD_HEIGHT + coin.getWidth()|| coin.getY() < - coin.getWidth()) {
                removedCoins.add(coin);
            }
            //Check if it has interacted with the player
            else if(coin.isColliding(player) && player.isInArena() && !endFlag){
                //If it has and its a coin
                if(coin.isGoodCoin()){
                    //Add value
                    score += coin.getValue();
                    playCoinSFX();
                    //If the added value passes current threshold update game stats
                    if(score > maxScore) {
                        //Add x knights based on x/100
                        amountOfKnights = (int) score/100f;
                        //Add x abbots based on x/50
                        amountOfAbbots = (int) score/50f + 2;
                        //Increase new threshold
                        maxScore += 5 + maxScore/100f;
                        //Increase size of the objects for difficultly
                        if(size < 2) {size += 0.1f;}
                        //Once they're big enough we start increasing the speed for difficulty
                        else if(size >= 2 && speed < 2.5){speed += 0.1f;}
                        //Once score is high enough start boosting the coin values so we can
                        //get silver and gold coin and crosses
                        if(score < 30){coinBoost = 0;}
                        else if(score < 60){coinBoost = 0.7f;}
                        else{coinBoost = 1.5f;}
                        //Increase the player size
                        player.updateSize(size);
                        //Get that size to be used for all new objects
                        currentPlayerWidth = player.getWidth();
                    }
                }
                //If they interact and it's a cross subtract the value
                else{
                    playCrossSFX();
                    score -= coin.getValue();
                    if(score == -20){
                        playHighScoreSFX();
                        greedyGobo.setHoly(true);
                        endGame();
                    }
                }
                removedCoins.add(coin);
            }
        }
        //Get rid off all the coins that have been interacted with or left screen
        for(Coin coin: removedCoins){ coins.remove(coin); }
        removedCoins.removeAllElements();
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Central function for updating abbots
    */
    private void updateAbbots(float delta){
        updateAbbotsPosition(delta);                               //Update position
        if(bishops.size() < amountOfAbbots){ createNewAbbots(); }  //If there is room make new abbot
        removeAndCollideAbbots();                                  //If they leave screen delete them
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Updates the position of all abbots
    */
    private void updateAbbotsPosition(float delta){ for(Bishop bishop : bishops) {bishop.update(bishop.getDirection(), delta); } }

    /*
    Input: Void
    Output: Void
    Purpose: If there is room make a new abbot
    */
    private void createNewAbbots(){ bishops.add(new Bishop(currentPlayerWidth, speed, abbotSpriteSheetTexture)); }

    /*
    Input: Void
    Output: Void
    Purpose: If abbot leaves screen remove him check if they touched player
    */
    private void removeAndCollideAbbots() {
        Vector<Bishop> removedBishops = new Vector<>();
        for (Bishop bishop : bishops) {
            //If abbot leaves screen remove him
            if (bishop.getX() > 380 + bishop.getWidth() || bishop.getX() < -bishop.getWidth() ||
                    bishop.getY() > WORLD_HEIGHT + bishop.getWidth() || bishop.getY() < - bishop.getWidth()) {
               removedBishops.add(bishop);
            }
            //If the abbot touches the player stun him and send him flying
            if(bishop.isColliding(player) && player.isInArena() && !endFlag){
                player.setStunned(true);
                playAbbotSFX();
                //Depending on which direction he came from send him flying back
                if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getWidth()/2f){
                    player.boostY(-1);
                    player.boostX(-1);
                }
                else if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() > bishop.getY() + bishop.getWidth()/2f){
                    player.boostY(1);
                    player.boostX(-1);
                }
                else if(player.getX() > bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getWidth()/2f){
                    player.boostY(-1);
                    player.boostX(1);
                }
                else{
                    player.boostY(1);
                    player.boostX(1);
                }
            }
        }
        //Remove the bishop
        for(Bishop bishop: removedBishops){ bishops.remove(bishop);}
        removedBishops.removeAllElements();
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Central function for updating the knights
    */
    private void updateKnight(float delta){
        removeAndCollisionKnight();                                 //Remove knights
        updateKnightPosition(delta);                                //Update postion
        if(knights.size() < amountOfKnights){createNewKnights();}   //Make more knights
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Central function for updating the knights
    */
    private void createNewKnights(){ knights.add(new Knight(currentPlayerWidth + 5, speed, knightSpriteSheetTexture)); }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Moves the knights
    */
    private void updateKnightPosition(float delta){ for(Knight knight : knights) { knight.update(knight.getDirection(), delta); } }

    /*
    Input: Void
    Output: Void
    Purpose: Checks if the knight left screen and removes him or has interacted with player
    */
    private void removeAndCollisionKnight(){
        Vector<Knight> removedKnights = new Vector<>();
        for(Knight knight : knights) {
            //Checks if the knight has left the screen
            if (knight.getX() > 380 + knight.getWidth() || knight.getX() < -knight.getWidth() ||
                    knight.getY() > WORLD_HEIGHT + knight.getWidth() || knight.getY() < -knight.getWidth()) {
                removedKnights.add(knight);
            }
            //Checks if the knight has touched the player
            if (knight.isColliding(player) && player.isInArena() && !endFlag) {
                playKnightSFX();
                removedKnights.add(knight);             //Get rid of the knight
                player.setOldWidth(player.getWidth());  //Grab the players current width
                //Grab where the player was
                player.setOldCoordinates(player.getX() + player.getWidth(), player.getY() + player.getWidth());
                //Set portal to open
                player.setPortalOpening(1);
                //Set player to be falling
                player.setFalling(true);
                //Set player to have left the arena
                player.setInArena(false);
                //Remove a life
                lives--;
            }
        }

        //Gets rid of the knights
        for(Knight knight: removedKnights){knights.remove(knight);}
        removedKnights.removeAllElements();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Central drawing function
    */
    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        //Draw the background
        batch.draw(backgroundTexture, 0, 0, 400, WORLD_HEIGHT);
        for (Coin coin : coins){coin.draw(batch);}          //Draw coins/crosses
        for(Bishop bishop : bishops){bishop.draw(batch);}   //Draw Abbots
        for(Knight knight : knights){knight.draw(batch);}   //Draw knights
        //Draw player
        player.draw(batch);
        //Draw the scoreBoard
        batch.draw(scoreBoardTexture, 380/2f -  scoreBoardTexture.getWidth()/2f + 20, 300 - scoreBoardTexture.getHeight()/2f);
        //Draws the UI beam
        batch.draw(UITexture, 380, 0);
        //Draw the gobo spawn area
        batch.draw(goboSpawnTexture, 388, 165, goboSpawnTexture.getWidth() - 5, goboSpawnTexture.getHeight());
        //Draw the lives gobo has
        drawLives();
        //Draw current score
        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "" + (float) ((float)Math.round(score * 100.0) / 100.0), 380/2f + 20, 300);
        //If dev mode is on draw hit boxes and phone stats
        if(developerMode){drawDeveloperInfo();}
        batch.end();

        //Draw open menu button
        if(!pausedFlag){menuStage.draw();}

        batch.begin();
        //Draw the menu pop up
        bitmapFont.getData().setScale(0.3f);
        if(pausedFlag || endFlag){batch.draw(popUpTexture, 380/2f - popUpTexture.getWidth()/2f, WORLD_HEIGHT/2 - popUpTexture.getHeight()/2f);}
        //Draw the help menu
        if(helpFlag){
            batch.draw(popUpTexture, 10, 10, WORLD_WIDTH - 20, WORLD_HEIGHT-20);
            drawHelpScreen();
        }
        batch.end();

        //Draw the buttons over the pop up
        if(pausedFlag || endFlag || helpFlag){menuStage.draw();}

        batch.begin();
        //Draw the player over the UI
        if((!player.isInArena() || endFlag && player.getX() == 425) && !helpFlag){player.draw(batch);}
        //Draw the menu button text
        if(pausedFlag && !helpFlag){ drawButtonText();}
        //Draw end pop up text
        if(endFlag){ drawEndPopUpText();}
        batch.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the hit boxes and the phone stats
    */
    private void drawDeveloperInfo(){
        //Batch setting up texture
        int x = (int) Gdx.input.getAccelerometerX();
        int y = (int) Gdx.input.getAccelerometerY();
        int z = (int) Gdx.input.getAccelerometerZ();
        centerText(bitmapFontDeveloper, "X: " + x, 40, 300);
        centerText(bitmapFontDeveloper, "Y: " + y, 40, 280);
        centerText(bitmapFontDeveloper, "Z: " + z, 40, 260);
        if(Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)){ centerText(bitmapFontDeveloper, "Surface X", 40, 240); }
        else if(Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)){ centerText(bitmapFontDeveloper, "Surface Y", 40, 240); }
        else if(Math.abs(z) > Math.abs(x) && Math.abs(z) > Math.abs(y)){ centerText(bitmapFontDeveloper, "Surface Z", 40, 240); }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the heads that represent lives
    */
    private void drawLives(){
        if(lives >= 0) {
            for (int i = 0; i < 3; i++) {
                if(i < lives) { batch.draw(goboLifeHeadTexture, 400, 110 - 50 * i, 60, 40); }
                else{ batch.draw(goboDeadHeadTexture, 400, 110 - 50 * i, 60, 40); }
            }
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws text over the menu buttons
    */
    private void drawButtonText(){
        float x;
        float y;
        for(int i = 1; i < 5; i ++){
            if(i == 1 || i == 3){ x = 380/2f - 1.4f*menuButtons[0].getWidth();}
            else { x = 380/2f + 1.4f*menuButtons[0].getWidth();}
            if(i < 3){ y = WORLD_HEIGHT/2f - 15 + menuButtons[0].getHeight();}
            else{y = WORLD_HEIGHT/2f - menuButtons[0].getHeight();}
            //If the volume is off draw Sound On else Sound off
            if(i == 4 && sfxVolume == 0){i = 5;}
            centerText(bitmapFont, menuButtonText[i -1], x , y);
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws text over the menu and high score
    */
    private void drawEndPopUpText(){
        float x;
        float y;
        for(int i = 1; i < 3; i ++){
            if(i == 1){ x = 380/2f - 1.4f*menuButtons[0].getWidth();}
            else { x = 380/2f + 1.4f*menuButtons[0].getWidth();}
            y = WORLD_HEIGHT/2f - 15 + menuButtons[0].getHeight();
            centerText(bitmapFont, menuButtonText[i -1], x , y);
        }
        bitmapFont.getData().setScale(.5f);
        centerText(bitmapFont, "Total Horde" , 380/2f, WORLD_HEIGHT/2f - 10);
        centerText(bitmapFont, "" + (float) ((float)Math.round(score * 100.0) / 100.0), 380/2f, WORLD_HEIGHT/2f - 40);
        bitmapFont.getData().setScale(.4f);

        if(score >= 150 && !blueUnlocked && !toxicUnlocked){
            batch.draw(popUpTexture, 380/2f - 120, WORLD_HEIGHT/2f + 80, 240, 60);
            centerText(bitmapFont, "Water & Toxic Gobo Unlocked!", 380/2f, WORLD_HEIGHT/2f + 120);
            bitmapFont.getData().setScale(.2f);
            centerText(bitmapFont, "Click on Gobo in main menu to change color!", 380/2f, WORLD_HEIGHT/2f + 100);
        }
        if(score >= 150 && !toxicUnlocked && blueUnlocked){
            batch.draw(popUpTexture, 380/2f - 120, WORLD_HEIGHT/2f + 80, 240, 60);
            centerText(bitmapFont, "Toxic Gobo Unlocked!", 380/2f, WORLD_HEIGHT/2f + 120);
            bitmapFont.getData().setScale(.2f);
            centerText(bitmapFont, "Click on Gobo in main menu to change color!", 380/2f, WORLD_HEIGHT/2f + 100);
        }
        if(score >= 75 && !blueUnlocked){
            batch.draw(popUpTexture, 380/2f - 120, WORLD_HEIGHT/2f + 80, 240, 60);
            centerText(bitmapFont, "Blue Gobo Unlocked!", 380/2f, WORLD_HEIGHT/2f + 120);
            bitmapFont.getData().setScale(.2f);
            centerText(bitmapFont, "Click on Gobo in main menu to change color!", 380/2f, WORLD_HEIGHT/2f + 100);
        }
        if(score == -20 && !holyUnlocked) {
            batch.draw(popUpTexture, 380 / 2f - 120, WORLD_HEIGHT / 2f + 80, 240, 60);
            centerText(bitmapFont, "Holy Gobo Unlocked!", 380 / 2f, WORLD_HEIGHT / 2f + 120);
            bitmapFont.getData().setScale(.2f);
            centerText(bitmapFont, "Click on Gobo in main menu to change color!", 380 / 2f, WORLD_HEIGHT / 2f + 100);
        }
        if(newHighScoreFlag){ centerText(bitmapFont, "New High Score!", 380/2f, WORLD_HEIGHT/2f - 70); }
        if(score == -20){centerText(bitmapFont, "Your are redeemed!", 380/2f, WORLD_HEIGHT/2f - 70); }
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
    Purpose: Updates all the variables on the screen
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }

    /*
    Input: Void
    Output: Void
    Purpose: Destroys everything once we move onto the new screen
    */
    @Override
    public void dispose() {
        menuStage.dispose();
        music.dispose();

        bishops.removeAllElements();
        knights.removeAllElements();
        coins.removeAllElements();

        bitmapFont.dispose();
        bitmapFontDeveloper.dispose();

        backgroundTexture.dispose();
        UITexture.dispose();
        scoreBoardTexture.dispose();
        goboLifeHeadTexture.dispose();
        goboDeadHeadTexture.dispose();
        goboSpawnTexture.dispose();
        popUpTexture.dispose();
        touchControlTexture.dispose();
        movementControlTexture.dispose();
    }
}
