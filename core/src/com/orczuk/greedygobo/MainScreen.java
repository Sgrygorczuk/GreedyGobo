package com.orczuk.greedygobo;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

    PlayerCharacter player;

    private BitmapFont bitmapFont = new BitmapFont();

    private Vector<Coin> coins = new Vector<>();
    private Vector<Bishop> bishops = new Vector<>();
    private Vector<Knight> knights = new Vector<>();

    private TextureRegion[][] coinTextures;
    private Texture goboSpriteSheetTexture;
    private Texture abbotSpriteSheetTexture;
    private Texture knightSpriteSheetTexture;
    private Texture backgroundTexture;
    private Texture UITexture;
    private Texture goboLifeHeadTexture;
    private Texture goboDeadHeadTexture;
    private Texture goboSpawnTexture;
    private Texture popUpTexture;


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
        goboSpriteSheetTexture = new Texture(Gdx.files.internal("Sprites/GoboSpriteSheet.png"));
        abbotSpriteSheetTexture = new Texture(Gdx.files.internal("Sprites/AbbotSpriteSheet.png"));
        knightSpriteSheetTexture = new Texture(Gdx.files.internal("Sprites/KnightSpriteSheet.png"));
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/Background.png"));
        UITexture = new Texture(Gdx.files.internal("Sprites/UI.png"));
        goboLifeHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHead.png"));
        goboDeadHeadTexture = new Texture(Gdx.files.internal("Sprites/GoboHeadOff.png"));
        goboSpawnTexture = new Texture(Gdx.files.internal("Sprites/GoboSpawn.png"));
        popUpTexture = new Texture(Gdx.files.internal("UI/PopUpBoarder.png"));
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
        TextureRegion[][] popUpButtonSpriteSheet = new TextureRegion(popUpButtonTexturePath).split(117, 47); //Breaks down the texture into tiles


        menuButtons[0] =  new ImageButton(new TextureRegionDrawable(buttonSpriteSheet[0][0]), new TextureRegionDrawable(buttonSpriteSheet[0][1]));
        //Places the buttons down
        menuButtons[0].setPosition(430 - buttonSpriteSheet[0][0].getRegionWidth()/2f, WORLD_HEIGHT - 10 - buttonSpriteSheet[0][0].getRegionHeight());
        menuStage.addActor(menuButtons[0]);

        menuButtons[0].addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                pausedFlag = !pausedFlag;
                for(int i = 1; i < 5; i ++){
                    if(pausedFlag){menuButtons[i].setVisible(true);}
                    else{menuButtons[i].setVisible(false);}
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
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws all of the variables on the screen
    */
    private void showObjects(){
        player = new PlayerCharacter(425, 180, goboSpriteSheetTexture, new Texture(Gdx.files.internal("Sprites/Portal.png")));
    }

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
        updatePlayer(delta);
        updateCoin(delta);
        updateBishops(delta);
        updateKnight(delta);
        restart();
    }

    private void restart(){
        if(lives == 0 && !player.isFalling()){
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
        }
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
            //If they are we check where
            float touchedY = WORLD_HEIGHT - Gdx.input.getY()*WORLD_HEIGHT/Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX()*WORLD_WIDTH/Gdx.graphics.getWidth();
            //If they touch on top of the gobo they are now holding it
            if(touchedY >= player.getY() && touchedY <= player.getY() + player.getHeight() && touchedX >= player.getX()  && touchedX <= player.getX() + player.getWidth() && !player.isHeld()){
                player.setHeld(true);
                player.setDimensions(player.getWidth() + 10, player.getHeight() + 10);
            }
            //If gobo is being held he can be dragged around the screen
            else if(player.isHeld()){ player.setPosition(touchedX, touchedY); }
        }
        //If the player lets go of gobo and he's on the field then he can slide around and interact with the objects
        else if(player.isHeld() && player.getX() + player.getHeight() < 380){
            player.setHeld(false);
            player.setInArena(true);
            player.setDimensions(player.getWidth() - 10, player.getHeight() - 10);
        }
        //If the player lets go but gobo is not on the playing field he ports back to the spawn point
        else if(player.isHeld()){
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

        //While phone is held right and player is not stunned they control where they go
        if(z > Math.abs(x) && z > Math.abs(y) && !player.isStunned()){
            player.boostX(y);
            player.boostY(-x);

            if(x > 0 && x > y){player.updateDirection(0);}
            else if(x < 0 && Math.abs(x) > y){player.updateDirection(1);}
            else if(y > 0 && y > x){player.updateDirection(2);}
            else if(y < 0 && Math.abs(y) > x){player.updateDirection(3);}

            if(y == x){
                player.setStopped(true);
                player.updateDirection(0);
            }
            else{player.setStopped(false);}
        }
        //If the player gets hit by a priest they get stunned, they can recover by either clicking on the character or waiting it out
        else if(player.isStunned()) {
            float hitBoxIncrease = 20;
            float touchedY = WORLD_HEIGHT - Gdx.input.getY() * WORLD_HEIGHT / Gdx.graphics.getHeight();
            float touchedX = Gdx.input.getX() * WORLD_WIDTH / Gdx.graphics.getWidth();
            //Clicking destuns the player
            if (touchedY >= player.getY() - hitBoxIncrease && touchedY <= player.getY() + player.getHeight() + hitBoxIncrease && touchedX >= player.getX() - hitBoxIncrease && touchedX <= player.getX() + player.getWidth() + hitBoxIncrease && !player.isHeld()) {
                player.deStun();
            }
            //Counts down till they naturally destun
            else { player.stunnedTimer(delta); }
        }

        player.update();
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
                    if(score > maxScore) {
                        amountOfKnights = (int) score/100f + 1;
                        amountOfAbbots = (int) score/50f + 3;
                        maxScore += 5;
                        //Once they're big enough we start increasing the speed for difficulty
                        if(size < 2) {size += 0.1f;}
                        else if(size >= 2 && speed < 3){speed += 0.1f;}
                        if(score < 30){coinBoost = 0;}
                        else if(score < 60){coinBoost = 0.7f;}
                        else{coinBoost = 1.5f;}
                        player.updateSize(size);
                        currentPlayerWidth = player.getWidth();
                    }
                }
                else{score -= coin.getValue();}
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

            if(bishop.isColliding(player) && player.isInArena()){
                player.setStunned(true);
                if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(-2);
                    player.boostX(-2);
                }
                else if(player.getX() < bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() > bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(2);
                    player.boostX(-2);
                }
                else if(player.getX() > bishop.getX() + bishop.getWidth()/2f &&
                        player.getY() < bishop.getY() + bishop.getHeight()/2f){
                    player.boostY(-2);
                    player.boostX(2);
                }
                else{
                    player.boostY(2);
                    player.boostX(2);
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
            if (knight.isColliding(player) && player.isInArena()) {
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
        batch.draw(UITexture, 380, 0);
        batch.draw(goboSpawnTexture, 388, 165, goboSpawnTexture.getWidth() - 5, goboSpawnTexture.getHeight());
        drawLives();
        centerText(bitmapFont, "Score: " + (float) ((float)Math.round(score * 100.0) / 100.0), WORLD_WIDTH/2, 300);
        drawDeveloperInfo();
        batch.end();

        //Draw all buttons
        if(!pausedFlag){menuStage.draw();}

        batch.begin();
        player.draw(batch);
        if(pausedFlag){batch.draw(popUpTexture, 380/2f - popUpTexture.getWidth()/2f, WORLD_HEIGHT/2 - popUpTexture.getHeight()/2f);}
        batch.end();

        if(pausedFlag){menuStage.draw();}
    }

    private void drawDeveloperInfo(){
        //Batch setting up texture
        int x = (int) Gdx.input.getAccelerometerX();
        int y = (int) Gdx.input.getAccelerometerY();
        int z = (int) Gdx.input.getAccelerometerZ();
        centerText(bitmapFont, "X: " + x, 40, 300);
        centerText(bitmapFont, "Y: " + y, 40, 280);
        centerText(bitmapFont, "Z: " + z, 40, 260);
        if(Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)){ centerText(bitmapFont, "Surface X", 40, 240); }
        else if(Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)){ centerText(bitmapFont, "Surface Y", 40, 240); }
        else if(Math.abs(z) > Math.abs(x) && Math.abs(z) > Math.abs(y)){ centerText(bitmapFont, "Surface Z", 40, 240); }

    }

    private void drawLives(){
        if(lives >= 0) {
            for (int i = 0; i < 3; i++) {
                if(i < lives) { batch.draw(goboLifeHeadTexture, 400, 110 - 50 * i, 60, 40); }
                else{ batch.draw(goboDeadHeadTexture, 400, 110 - 50 * i, 60, 40); }
            }
        }
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
    }
}
