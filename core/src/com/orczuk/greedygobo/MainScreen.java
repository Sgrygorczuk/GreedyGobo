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

    PlayerCharacter player;

    private BitmapFont bitmapFont = new BitmapFont();

    private Vector<Coin> coins = new Vector<>();
    private Vector<Bishop> bishops = new Vector<>();
    private Knight knight;

    private TextureRegion[][] coinTextures;
    private Texture goboSpriteSheetTexture;
    private Texture abbotSpriteSheetTexture;
    private Texture knightSpriteSheetTexture;
    private Texture backgroundTexture;
    private Texture UITexture;

    private float score = 0;
    private float speed = 1;
    private float maxScore = 5;
    private float size = 1;
    private float coinBoost = 0f;
    private int lives = 3;
    private float currentPlayerWidth = 15;

    /*
    Input: The width and height of the screen
    Output: Void
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
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws all of the variables on the screen
    */
    private void showObjects(){
        player = new PlayerCharacter(430, 240, goboSpriteSheetTexture, new Texture(Gdx.files.internal("Sprites/Portal.png")));
        knight = new Knight(player.getWidth() + 5, speed, knightSpriteSheetTexture);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws all of the variables on the screen
    */
    @Override
    public void render(float delta) {
        //Wipes screen black
        clearScreen();

        update(delta);
        draw();
    }

    private void update(float delta){
        updatePlayer(delta);
        updateCoin(delta);
        updateBishops(delta);
        updateKnight(delta);
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
            player.setPosition(430, 240);
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
            if(player.getY() <= 240){
                player.setPosition(player.getX(), 240);
                player.setFalling(false);
            }
        }
        else {
            player.setDimensions(player.getWidth() - player.getOldWidth()/100f, player.getHeight() - player.getOldWidth()/100f);
            player.setPosition(player.getX() + player.getOldWidth()/250f, player.getY() - player.getOldWidth()/400f);
            if (player.getWidth() < 1) {
                player.setPosition(430, WORLD_HEIGHT + player.getOldWidth());
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
        Vector<Coin> removeCoin = new Vector<>();
        for (Coin coin : coins) {
            if (coin.getX() > 380 + coin.getWidth() || coin.getX() < -coin.getWidth() ||
            coin.getY() > WORLD_HEIGHT + coin.getHeight() || coin.getY() < - coin.getHeight()) {
                //Remove Texture
                removeCoin.add(coin);
            }
            else if(coin.isColliding(player) && player.isInArena()){
                if(coin.isGoodCoin()){
                    score += coin.getValue();
                    if(score > maxScore) {
                        maxScore += 5;
                        //Once they're big enough we start increasing the speed for difficulty
                        if(size < 2) {size += 0.1f;}
                        else{speed += 0.1f;}
                        if(score < 30){coinBoost = 0;}
                        else if(score < 60){coinBoost = 0.7f;}
                        else{coinBoost = 1.5f;}
                        player.updateSize(size);
                        currentPlayerWidth = player.getWidth();
                        knight.updateSize(currentPlayerWidth + 5);
                    }
                }
                else{score -= coin.getValue();}
                removeCoin.add(coin); }
        }
        for(Coin coin : removeCoin){
            coins.remove(coin);
        }
    }

    private void updateBishops(float delta){
        updateBishopsPosition(delta);
        if(bishops.size() < 3){ createNewBishops(); }
        removeAndCollideBishops();
    }

    private void updateBishopsPosition(float delta){ for(Bishop bishop : bishops) {bishop.update(bishop.getDirection(), delta); } }

    private void createNewBishops(){ bishops.add(new Bishop(currentPlayerWidth, speed, abbotSpriteSheetTexture)); }

    private void removeAndCollideBishops() {
        Vector<Bishop> removeBishop = new Vector<>();
        for (Bishop bishop : bishops) {
            if (bishop.getX() > 380 + bishop.getWidth() || bishop.getX() < -bishop.getWidth() ||
                    bishop.getY() > WORLD_HEIGHT + bishop.getHeight() || bishop.getY() < - bishop.getHeight()) {
                removeBishop.add(bishop);
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

        for(Bishop bishop : removeBishop){
            //Remove texture
            bishops.remove(bishop);
        }
    }

    private void updateKnight(float delta){
        updateKnightExistence();
        updateKnightPosition(delta);
    }

    private void updateKnightPosition(float delta){ knight.update(knight.getDirection(), delta); }

    private void updateKnightExistence(){
        if (knight.getX() > 380 + knight.getWidth() || knight.getX() < -knight.getWidth() ||
                knight.getY() > WORLD_HEIGHT + knight.getHeight() || knight.getY() < - knight.getHeight()) {
            knight.updatePosition();
        }
        if(knight.isColliding(player) && player.isInArena()){
            knight.updatePosition();
            player.setOldWidth(player.getWidth());
            player.setOldCoordinates(player.getX() + player.getWidth(), player.getY() + player.getWidth());
            player.setPortalOpening(1);
            player.setFalling(true);
            player.setInArena(false);
            lives--;
        }
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture
        int x = (int) Gdx.input.getAccelerometerX();
        int y = (int) Gdx.input.getAccelerometerY();
        int z = (int) Gdx.input.getAccelerometerZ();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, 400, WORLD_HEIGHT);
        for (Coin coin : coins){coin.draw(batch);}
        for(Bishop bishop : bishops){bishop.draw(batch);}
        knight.draw(batch);
        batch.draw(UITexture, 400, 0);
        player.draw(batch);
        centerText(bitmapFont, "Score: " + (float) ((float)Math.round(score * 100.0) / 100.0), WORLD_WIDTH/2, 300);
        centerText(bitmapFont, "X: " + x, 40, 300);
        centerText(bitmapFont, "Y: " + y, 40, 280);
        centerText(bitmapFont, "Z: " + z, 40, 260);
        if(Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)){
            centerText(bitmapFont, "Surface X", 40, 240);
        }
        else if(Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)){
            centerText(bitmapFont, "Surface Y", 40, 240);
        }
        else if(Math.abs(z) > Math.abs(x) && Math.abs(z) > Math.abs(y)){
            centerText(bitmapFont, "Surface Z", 40, 240);
        }
        batch.end();
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
