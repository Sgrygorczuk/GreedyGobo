package com.orczuk.greedygobo;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.orczuk.greedygobo.Objects.Coin;
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
    private ShapeRenderer shapeRendererEnemy;           //Creates wire frame for enemy objects
    private ShapeRenderer shapeRendererUser;            //Creates wire frame for player object
    private ShapeRenderer shapeRendererBackground;      //Creates wire frame for background objects
    private ShapeRenderer shapeRendererCollectible;     //Creates wire frame for collectible objects

    private Viewport viewport;			 //The screen where we display things
    private Camera camera;				 //The camera viewing the viewport
    private SpriteBatch batch = new SpriteBatch();			 //Batch that holds all of the textures

    PlayerCharacter player;

    private BitmapFont bitmapFont = new BitmapFont();

    private Vector<Coin> coins = new Vector<>();

    private boolean renderFlag = true;
    private int score = 0;
    private int minScore = 0;
    private int maxScore = 5;
    private float size = 1;

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
        showRender();       //Set up all of renders
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
    private void showObjects(){
        player = new PlayerCharacter(WORLD_WIDTH/2, WORLD_HEIGHT/2);
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

        update();

        //setDebugMode();                 //Checks if user changed the status of the debugModeFlag
        if(renderFlag) {                     //If debugMode is on ShapeRender will drawing lines
            renderEnemy();              //Draws Enemy Wire Frame
            renderUser();               //Draws User Wire Frame
            renderBackground();         //Draws Background Wire Frame
            renderCollectible();        //Draws Collectible Wire Frame
        }
        draw();
    }

    private void update(){
        updatePlayerPosition();
        updateCoin();
    }

    private void updatePlayerPosition(){
        System.out.println(Gdx.input.getAccelerometerX() + " " + Gdx.input.getAccelerometerY() + " " + Gdx.input.getAccelerometerZ() );

        if (Gdx.input.getGyroscopeY() > 1 && Gdx.input.getGyroscopeZ() < -1) { player.boostY(-1); }
        else if (Gdx.input.getGyroscopeY() < -1 && Gdx.input.getGyroscopeZ() > 1) { player.boostY(1);}

        if (Gdx.input.getGyroscopeX() > 1  && Gdx.input.getGyroscopeZ() < -1) { player.boostX(-1); }
        else if (Gdx.input.getGyroscopeX() < -1 && Gdx.input.getGyroscopeZ() > 1) { player.boostX(1); }

        player.update();
    }

    private void updateCoin(){
        updateCoinPosition();
        if(coins.size() < 6){ createNewCoin(); }
        removeCoin();
    }

    private void updateCoinPosition(){
        for(Coin coin : coins){ coin.update(coin.getDirection()); }
    }

    private void createNewCoin(){
        //Figure out which wall it should come from
        int direction = MathUtils.random(0,3);
        Coin coin = new Coin(direction, MathUtils.randomBoolean(), size);
        coins.add(coin);
    }

    private void removeCoin() {
        Vector<Coin> removeCoin = new Vector<>();
        for (Coin coin : coins) {
            if (coin.getX() > WORLD_WIDTH || coin.getX() < -coin.getWidth() ||
            coin.getY() > WORLD_HEIGHT || coin.getY() < - coin.getHeight()) {
                //Remove Texture
                removeCoin.add(coin);
            }
            else if(coin.isColliding(player)){
                if(coin.isGoodCoin()){
                    score += coin.getValue();
                    if(score > maxScore) {
                        maxScore += 5;
                        size += 0.1f;
                        player.updateSize(size);
                    }
                }
                else{score -= coin.getValue();}
                removeCoin.add(coin); }
        }
        for(Coin coin : removeCoin){
            coins.remove(coin);
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

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture
        batch.begin();
        centerText(bitmapFont, "Score: " + score, WORLD_WIDTH/2, 300);
        centerText(bitmapFont, "X: " + (int) Gdx.input.getAccelerometerX(), 40, 300);
        centerText(bitmapFont, "Y: " + (int) Gdx.input.getAccelerometerY(), 40, 280);
        centerText(bitmapFont, "Z: " + (int) Gdx.input.getAccelerometerZ(), 40, 260);
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
