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

    private ShapeRenderer shapeRendererEnemy; //Creates the wire frames
    private ShapeRenderer shapeRendererUser;
    private ShapeRenderer shapeRendererBackground;
    private ShapeRenderer shapeRendererCollectible;

    //The buttons used to randomly generate a deck or open the deck building menu
    private Stage menuStage;
    private ImageButton[] menuButtons;

    private GreedyGobo greedyGobo;
    PlayerCharacter player;
    private Music music;

    private BitmapFont bitmapFont = new BitmapFont();
    private BitmapFont bitmapFontDeveloper = new BitmapFont();

    private Vector<Coin> coins = new Vector<>();
    private Vector<Bishop> bishops = new Vector<>();
    private Vector<Knight> knights = new Vector<>();

    private TextureRegion[][] coinTextures;
    private TextureRegion[][] goboSpriteSheetTexture;
    private TextureRegion[][] abbotSpriteSheetTexture;
    private TextureRegion[][] knightSpriteSheetTexture;
    private Texture backgroundTexture;
    private Texture UITexture;
    private Texture scoreBoardTexture;
    private Texture goboLifeHeadTexture;
    private Texture goboDeadHeadTexture;
    private Texture goboSpawnTexture;
    private Texture popUpTexture;
    private Texture touchControlTexture;
    private Texture movementControlTexture;


    private String[] menuButtonText = new String[]{"Main Menu", "Restart", "Help", "Sound Off", "Sound On"};
    private float amountOfKnights = 1;
    private float amountOfAbbots = 3;
    private float score = 0;
    private float speed = 1;
    private float maxScore = 5;
    private float size = 1;
    private float coinBoost = 0f;
    private int lives = 3;
    private float currentPlayerWidth = 10;
    private boolean developerMode = true;
    private boolean pausedFlag = false;
    private boolean endFlag = false;
    private float musicVolume = 0.6f;
    private float sfxVolume = 1f;
    private boolean helpFlag = false;
    private boolean newHighScoreFlag = false;


    //Timing variables
    private boolean abbotSFXFlag = false;
    private static final float ABBOT_SFX_TIME = 0.5F;                 //Time that the conversation box stays on screen
    private float abbotSfxTimer = ABBOT_SFX_TIME;                        //Counter that checks if it reached the end of time

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
        showTextures();
        showObjects();      //Sets up player and enemies
        showButtons();
        showMusic();
        if(developerMode){showRender();}
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

    private void showTextures(){
        Texture coinTexturePath = new Texture(Gdx.files.internal("Sprites/ObjectSpriteSheet.png"));
        coinTextures = new TextureRegion(coinTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture goboTexturePath = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheet.png"));
        goboSpriteSheetTexture  = new TextureRegion(goboTexturePath).split(420, 420); //Breaks down the texture into tiles
        Texture abbotTexturePath = new Texture(Gdx.files.internal("Sprites/AbbotSpriteSheet.png"));
        abbotSpriteSheetTexture = new TextureRegion(abbotTexturePath).split(420, 420);
        Texture knightTexturePath = new Texture(Gdx.files.internal("Sprites/KnightSpriteSheet.png"));
        knightSpriteSheetTexture = new TextureRegion(knightTexturePath).split(420, 420);backgroundTexture = new Texture(Gdx.files.internal("Sprites/Background.png"));
        UITexture = new Texture(Gdx.files.internal("Sprites/UI.png"));
        scoreBoardTexture = new Texture(Gdx.files.internal("Sprites/ScoreBoard.png"));
        goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHead.png"));
        goboDeadHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadOff.png"));
        goboSpawnTexture = new Texture(Gdx.files.internal("Sprites/GoboSpawn.png"));
        popUpTexture = new Texture(Gdx.files.internal("UI/PopUpBoarder.png"));
        touchControlTexture = new Texture(Gdx.files.internal("Sprites/ClickerHand.png"));
        movementControlTexture  = new Texture(Gdx.files.internal("Sprites/PhoneMovement.png"));
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the button
    */
    private void showButtons(){
        menuStage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(menuStage);

        menuButtons = new ImageButton[6];
        //Button Textures

        Texture menuButtonTexturePath = new Texture(Gdx.files.internal("UI/MenuButton.png"));
        TextureRegion[][] buttonSpriteSheet = new TextureRegion(menuButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        Texture popUpButtonTexturePath = new Texture(Gdx.files.internal("UI/ButtonSpriteSheet.png"));
        final TextureRegion[][] popUpButtonSpriteSheet = new TextureRegion(popUpButtonTexturePath).split(117, 47); //Breaks down the texture into tiles


        menuButtons[0] =  new ImageButton(new TextureRegionDrawable(buttonSpriteSheet[0][0]), new TextureRegionDrawable(buttonSpriteSheet[0][1]));
        //Places the buttons down
        menuButtons[0].setPosition(430 - buttonSpriteSheet[0][0].getRegionWidth()/2f, WORLD_HEIGHT - 10 - buttonSpriteSheet[0][0].getRegionHeight());
        menuStage.addActor(menuButtons[0]);

        menuButtons[0].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if (!endFlag) {
                    playButtonFX();
                    pausedFlag = !pausedFlag;
                    for (int i = 1; i < 5; i++) {
                        if (pausedFlag) { menuButtons[i].setVisible(true); }
                        else{menuButtons[i].setVisible(false);}
                    }
                }
            }
        });

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
            menuButtons[i].setVisible(false);

            final int finalI = i;
            menuButtons[i].addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    playButtonFX();
                    if(finalI == 1){
                        music.stop();
                        greedyGobo.setScreen(new MenuScreen(greedyGobo));
                    }
                    else if(finalI == 2){
                        restart();
                    }
                    else if(finalI == 3){
                        helpFlag = true;
                        for(ImageButton imageButton : menuButtons){ imageButton.setVisible(false); }
                        menuButtons[5].setVisible(true);
                    }
                    else {soundButtonAction(finalI, popUpButtonSpriteSheet);}
                }
            });
        }

        Texture exitButtonTexturePath = new Texture(Gdx.files.internal("UI/ExitButton.png"));
        TextureRegion[][] exitButtonSpriteSheet = new TextureRegion(exitButtonTexturePath).split(45, 44); //Breaks down the texture into tiles

        menuButtons[5] =  new ImageButton(new TextureRegionDrawable(exitButtonSpriteSheet[0][0]), new TextureRegionDrawable(exitButtonSpriteSheet[0][1]));
        menuButtons[5].setPosition(WORLD_WIDTH - 50, WORLD_HEIGHT - 50);
        menuButtons[5].setWidth(20);
        menuButtons[5].setHeight(20);
        menuStage.addActor(menuButtons[5]);
        menuButtons[5].setVisible(false);
        menuButtons[5].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                playButtonFX();
                helpFlag = false;
                for (ImageButton imageButton : menuButtons) { imageButton.setVisible(true); }
                menuButtons[5].setVisible(false);
            }
        });
    }

    private void soundButtonAction(final int finalI, final TextureRegion[][] popUpButtonSpriteSheet){
        menuButtons[finalI].setVisible(false);
        if(musicVolume == 0.6f) {
            musicVolume = 0;
            sfxVolume = 0;
            menuButtons[finalI] =  new ImageButton(new TextureRegionDrawable(popUpButtonSpriteSheet[4][0]), new TextureRegionDrawable(popUpButtonSpriteSheet[4][1]));
        }
        else{
            musicVolume = 0.6f;
            sfxVolume = 1;
            menuButtons[finalI] =  new ImageButton(new TextureRegionDrawable(popUpButtonSpriteSheet[3][0]), new TextureRegionDrawable(popUpButtonSpriteSheet[3][1]));
        }
        music.setVolume(musicVolume/2f);
        menuButtons[finalI].setPosition(380/2f + 5, WORLD_HEIGHT/2f - 10 - popUpButtonSpriteSheet[0][0].getRegionHeight());
        menuStage.addActor(menuButtons[finalI]);
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
    Purpose: Draws all of the variables on the screen
    */
    private void showObjects(){
        player = new PlayerCharacter(425, 180, goboSpriteSheetTexture, new Texture(Gdx.files.internal("Sprites/Portal.png")));
        if(greedyGobo.getAssetManager().isLoaded("Fonts/GreedyGobo.fnt")){bitmapFont = greedyGobo.getAssetManager().get("Fonts/GreedyGobo.fnt");}
        bitmapFont.getData().setScale(0.6f);
    }

    private void showMusic(){
        music = greedyGobo.getAssetManager().get("Music/GoboLevelTheme.wav", Music.class);
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
    }

    private void playAbbotSFX() {
        if (!abbotSFXFlag) {
            greedyGobo.getAssetManager().get("SFX/Abbot.wav", Sound.class).play(sfxVolume);
            abbotSFXFlag = true;
        }
    }
    private void playKnightSFX() { greedyGobo.getAssetManager().get("SFX/Knight.wav", Sound.class).play(sfxVolume); }
    private void playCoinSFX() { greedyGobo.getAssetManager().get("SFX/Coin.wav", Sound.class).play(sfxVolume); }
    private void playCrossSFX() { greedyGobo.getAssetManager().get("SFX/Cross.wav", Sound.class).play(sfxVolume); }
    private void playSlapSFX() { greedyGobo.getAssetManager().get("SFX/Slap.wav", Sound.class).play(sfxVolume); }
    private void playBagFX() { greedyGobo.getAssetManager().get("SFX/Bag.wav", Sound.class).play(sfxVolume); }
    private void playDropFX() { greedyGobo.getAssetManager().get("SFX/Drop.wav", Sound.class).play(sfxVolume); }
    private void playButtonFX() { greedyGobo.getAssetManager().get("SFX/Button.wav", Sound.class).play(1/2f); }

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


    private void update(float delta){
        if(!endFlag){updatePlayer(delta);}
        else{
            updatePortal();
            if(player.getY() != 180){updatePlayerFalling();}
            player.updateAnimation(delta);
        }
        updateCoin(delta);
        updateBishops(delta);
        updateKnight(delta);
        if(abbotSFXFlag){updateSFXTimer(delta);}
        if(lives == 0){endGame();}
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Counts down until the communication screen turns off
    */
    private void updateSFXTimer(float delta) {
        abbotSfxTimer -= delta;
        if (abbotSfxTimer <= 0) {
            abbotSfxTimer = ABBOT_SFX_TIME;
            abbotSFXFlag = false;
        }
    }

    private void endGame(){
        endFlag = true;
        player.setStunned(true);
        player.setInArena(true);
        menuButtons[1].setVisible(true);
        menuButtons[2].setVisible(true);
        if(Float.parseFloat(greedyGobo.getCurrentHighScore()) < score){
            newHighScoreFlag = true;
            greedyGobo.setCurrentHighScore("" + (float) ((float)Math.round(score * 100.0) / 100.0));
        }
    }

    private void restart(){
            bishops.removeAllElements();
            knights.removeAllElements();
            coins.removeAllElements();
            //Pop up the menu
            amountOfKnights = 1;
            amountOfAbbots = 3;
            lives = 3;
            score = 0;
            size = 1;
            speed = 1;
            maxScore = 5;
            coinBoost = 0f;
            currentPlayerWidth = 10;
            player.setDimensions(10, 10);
            for(int i = 1; i < 5; i++){menuButtons[i].setVisible(false);}
            endFlag = false;
            pausedFlag = false;
            newHighScoreFlag = false;
            player.setStunned(false);
            player.setInArena(false);
            player.setPosition(425, 180);
    }

    private void updatePlayer(float delta){
        //Before the player starts collecting coins and moving around the screen they first need to drag the gobo onto the field
        if(!player.isInArena() && !player.isFalling()) {updatePlayerStart();}
        else if(player.isFalling()){updatePlayerFalling();}
        //Once on the field they can slide around
        else{ updatePlayerPosition(delta); }
        if(player.getPortalOpening() != 0){updatePortal();}
        player.updateAnimation(delta);
    }

    private void updatePlayerStart(){
        //First we look at if the player is touching the screen
        if (Gdx.input.isTouched()){
            float hitBoxExtender = 10;
            //If they are we check where
            float touchedY = WORLD_HEIGHT - Gdx.input.getY()*WORLD_HEIGHT/Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX()*WORLD_WIDTH/Gdx.graphics.getWidth();
            //If they touch on top of the gobo they are now holding it
            if(touchedY >= player.getY() - hitBoxExtender && touchedY <= player.getY() + player.getHeight() + hitBoxExtender
                    && touchedX >= player.getX() - hitBoxExtender  && touchedX <= player.getX() + player.getWidth() + hitBoxExtender && !player.isHeld()){
                playBagFX();
                player.setHeld(true);
                player.setDimensions(player.getWidth() + 10, player.getHeight() + 10);
            }
            //If gobo is being held he can be dragged around the screen
            else if(player.isHeld()){ player.setPosition(touchedX, touchedY); }
        }
        //If the player lets go of gobo and he's on the field then he can slide around and interact with the objects
        else if(player.isHeld() && player.getX() + player.getHeight() < 380){
            playDropFX();
            player.setHeld(false);
            player.setInArena(true);
            player.setDimensions(player.getWidth() - 10, player.getHeight() - 10);
        }
        //If the player lets go but gobo is not on the playing field he ports back to the spawn point
        else if(player.isHeld()){
            playDropFX();
            player.setHeld(false);
            player.setPosition(425, 180);
            player.setDimensions(player.getWidth() - 10, player.getHeight() - 10);
        }
    }

    private void updatePortal(){
        if(player.getPortalOpening() == 1 && player.getPortalWidth() <=  player.getOldWidth() * 1.5){
            player.setPortalWidth(player.getPortalWidth() + player.getOldWidth()/10f);
            player.setOldCoordinates(player.getOldX() - player.getOldWidth()/10f, player.getOldY() - player.getOldWidth()/10f);
        }
        else if(player.getPortalOpening() == 2) {
            player.setPortalWidth(player.getPortalWidth() - player.getOldWidth()/10f);
            player.setOldCoordinates(player.getOldX() + player.getOldWidth()/15f, player.getOldY() + player.getOldWidth()/15f);
            if(player.getPortalWidth() <= 0){
                player.setPortalOpening(0);
            }
        }
    }

    private void updatePlayerFalling(){
        if(player.getPortalOpening() != 1){
            player.setPosition(player.getX(), player.getY() - 4);
            if(player.getY() <= 180){
                player.setPosition(player.getX(), 180);
                player.setFalling(false);
                player.setStunned(false);
                playDropFX();
            }
        }
        else {
            player.setDimensions(player.getWidth() - player.getOldWidth()/100f, player.getHeight() - player.getOldWidth()/100f);
            player.setPosition(player.getX() + player.getOldWidth()/250f, player.getY() - player.getOldWidth()/400f);
            if (player.getWidth() < 1) {
                player.setPosition(425, WORLD_HEIGHT + player.getOldWidth());
                player.setDimensions(player.getOldWidth(), player.getOldWidth());
                player.setPortalOpening(2);
            }
        }
    }

    private void updatePlayerPosition(float delta){
        int x = (int) Gdx.input.getAccelerometerX();
        int y = (int) Gdx.input.getAccelerometerY();
        int z = (int) Gdx.input.getAccelerometerZ();

        if(z > Math.abs(x) && z > Math.abs(y) && !player.isStunned()) {
            player.boostX(y);
            player.boostY(-x);
            playerDirection(y, x);
        }
        //If the player gets hit by a priest they get stunned, they can recover by either clicking on the character or waiting it out
        else if(player.isStunned()) {
            float hitBoxIncrease = 20;
            float touchedY = WORLD_HEIGHT - Gdx.input.getY() * WORLD_HEIGHT / Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX() * WORLD_WIDTH / Gdx.graphics.getWidth();
            //Clicking destuns the player
            if (Gdx.input.isTouched() && touchedY >= player.getY() - hitBoxIncrease && touchedY <= player.getY() + player.getHeight() + hitBoxIncrease && touchedX >= player.getX() - hitBoxIncrease && touchedX <= player.getX() + player.getWidth() + hitBoxIncrease && !player.isHeld()) {
                playSlapSFX();
                player.deStun();
            }
            //Counts down till they naturally destun
            else { player.stunnedTimer(delta); }
        }
        player.update();
    }

    private void playerDirection(float axisX, float axisY){
        if(axisY > 0 && axisY > axisX){player.updateDirection(0);}
        else if(axisY < 0 && Math.abs(axisY) > axisX){player.updateDirection(1);}
        else if(axisX > 0 && axisX > axisY){player.updateDirection(2);}
        else if(axisX < 0 && Math.abs(axisX) > axisY){player.updateDirection(3);}

        if(axisX == axisY){
            player.setStopped(true);
            player.updateDirection(0);
        }
        else{player.setStopped(false);}
    }

    private void updateCoin(float delta){
        updateCoinPosition(delta);
        if(coins.size() < 6){ createNewCoin(); }
        removeCoin();
    }

    private void updateCoinPosition(float delta){
        for(Coin coin : coins){ coin.update(coin.getDirection(), delta); }
    }

    private void createNewCoin(){
        Coin coin = new Coin(MathUtils.randomBoolean(), size, speed, coinBoost);
        coin.setTexture(coinTextures);
        coins.add(coin);
    }

    private void removeCoin() {
        Vector<Coin> removedCoins = new Vector<>();
        for (Coin coin : coins) {
            if (coin.getX() > 380 + coin.getWidth() || coin.getX() < -coin.getWidth() ||
            coin.getY() > WORLD_HEIGHT + coin.getHeight() || coin.getY() < - coin.getHeight()) {
                removedCoins.add(coin);
            }
            else if(coin.isColliding(player) && player.isInArena()){
                if(coin.isGoodCoin()){
                    score += coin.getValue();
                    playCoinSFX();
                    if(score > maxScore) {
                        amountOfKnights = (int) score/100f;
                        amountOfAbbots = (int) score/50f + 2;
                        maxScore += 5;
                        //Once they're big enough we start increasing the speed for difficulty
                        if(size < 2) {size += 0.1f;}
                        else if(size >= 2 && speed < 2.5){speed += 0.1f;}
                        if(score < 30){coinBoost = 0;}
                        else if(score < 60){coinBoost = 0.7f;}
                        else{coinBoost = 1.5f;}
                        player.updateSize(size);
                        currentPlayerWidth = player.getWidth();
                    }
                }
                else{
                    playCrossSFX();
                    score -= coin.getValue();
                }
                removedCoins.add(coin);
            }
        }
        for(Coin coin: removedCoins){ coins.remove(coin); }
        removedCoins.removeAllElements();
    }

    private void updateBishops(float delta){
        updateBishopsPosition(delta);
        if(bishops.size() < amountOfAbbots){ createNewBishops(); }
        removeAndCollideBishops();
    }

    private void updateBishopsPosition(float delta){ for(Bishop bishop : bishops) {bishop.update(bishop.getDirection(), delta); } }

    private void createNewBishops(){ bishops.add(new Bishop(currentPlayerWidth, speed, abbotSpriteSheetTexture)); }

    private void removeAndCollideBishops() {
        Vector<Bishop> removedBishops = new Vector<>();
        for (Bishop bishop : bishops) {
            if (bishop.getX() > 380 + bishop.getWidth() || bishop.getX() < -bishop.getWidth() ||
                    bishop.getY() > WORLD_HEIGHT + bishop.getHeight() || bishop.getY() < - bishop.getHeight()) {
               removedBishops.add(bishop);
            }

            if(bishop.isColliding(player) && player.isInArena() && !endFlag){
                player.setStunned(true);
                playAbbotSFX();
                if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(-1);
                    player.boostX(-1);
                }
                else if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() > bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(1);
                    player.boostX(-1);
                }
                else if(player.getX() > bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(-1);
                    player.boostX(1);
                }
                else{
                    player.boostY(1);
                    player.boostX(1);
                }
            }
        }
        for(Bishop bishop: removedBishops){ bishops.remove(bishop);}
        removedBishops.removeAllElements();
    }

    private void updateKnight(float delta){
        removeAndCollisionKnight();
        updateKnightPosition(delta);
        if(knights.size() < amountOfKnights){createNewKnights();}
    }

    private void createNewKnights(){ knights.add(new Knight(currentPlayerWidth + 5, speed, knightSpriteSheetTexture)); }

    private void updateKnightPosition(float delta){
        for(Knight knight : knights) {
            knight.update(knight.getDirection(), delta);
        }
    }

    private void removeAndCollisionKnight(){
        Vector<Knight> removedKnights = new Vector<>();
        for(Knight knight : knights) {
            if (knight.getX() > 380 + knight.getWidth() || knight.getX() < -knight.getWidth() ||
                    knight.getY() > WORLD_HEIGHT + knight.getHeight() || knight.getY() < -knight.getHeight()) {
                removedKnights.add(knight);
            }
            if (knight.isColliding(player) && player.isInArena() && !endFlag) {
                playKnightSFX();
                removedKnights.add(knight);
                player.setOldWidth(player.getWidth());
                player.setOldCoordinates(player.getX() + player.getWidth(), player.getY() + player.getWidth());
                player.setPortalOpening(1);
                player.setFalling(true);
                player.setInArena(false);
                lives--;
            }
        }
        for(Knight knight: removedKnights){knights.remove(knight);}
        removedKnights.removeAllElements();
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, 400, WORLD_HEIGHT);
        for (Coin coin : coins){coin.draw(batch);}
        for(Bishop bishop : bishops){bishop.draw(batch);}
        for(Knight knight : knights){knight.draw(batch);}
        player.draw(batch);
        batch.draw(scoreBoardTexture, 380/2f -  scoreBoardTexture.getWidth()/2f + 20, 300 - scoreBoardTexture.getHeight()/2f);
        batch.draw(UITexture, 380, 0);
        batch.draw(goboSpawnTexture, 388, 165, goboSpawnTexture.getWidth() - 5, goboSpawnTexture.getHeight());
        drawLives();
        bitmapFont.getData().setScale(0.5f);
        centerText(bitmapFont, "" + (float) ((float)Math.round(score * 100.0) / 100.0), 380/2f + 20, 300);
        drawDeveloperInfo();
        batch.end();

        //Draw all buttons
        if(!pausedFlag){menuStage.draw();}

        batch.begin();
        bitmapFont.getData().setScale(0.3f);
        if(pausedFlag || endFlag){batch.draw(popUpTexture, 380/2f - popUpTexture.getWidth()/2f, WORLD_HEIGHT/2 - popUpTexture.getHeight()/2f);}
        if(helpFlag){
            batch.draw(popUpTexture, 10, 10, WORLD_WIDTH - 20, WORLD_HEIGHT-20);
            drawHelpScreen();
        }
        batch.end();

        if(pausedFlag || endFlag || helpFlag){menuStage.draw();}

        batch.begin();
        if(!player.isInArena() || endFlag && player.getX() == 425){player.draw(batch);}
        if(pausedFlag && !helpFlag){ drawButtonText();}
        if(endFlag){ drawEndPopUpText();}
        batch.end();
    }

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

    private void drawLives(){
        if(lives >= 0) {
            for (int i = 0; i < 3; i++) {
                if(i < lives) { batch.draw(goboLifeHeadTexture, 400, 110 - 50 * i, 60, 40); }
                else{ batch.draw(goboDeadHeadTexture, 400, 110 - 50 * i, 60, 40); }
            }
        }
    }

    private void drawButtonText(){
        float x;
        float y;
        for(int i = 1; i < 5; i ++){
            if(i == 1 || i == 3){ x = 380/2f - 1.4f*menuButtons[0].getWidth();}
            else { x = 380/2f + 1.4f*menuButtons[0].getWidth();}
            if(i < 3){ y = WORLD_HEIGHT/2f - 15 + menuButtons[0].getHeight();}
            else{y = WORLD_HEIGHT/2f - menuButtons[0].getHeight();}
            if(i == 4 && musicVolume == 0){i = 5;}
            centerText(bitmapFont, menuButtonText[i -1], x , y);
        }
    }

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
        if(newHighScoreFlag){ centerText(bitmapFont, "New High Score!", 380/2f, WORLD_HEIGHT/2f - 70); }
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
