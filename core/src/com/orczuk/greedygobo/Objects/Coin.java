package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Coin extends ParentObject{

    private float value;            //Value of the collected item
    private boolean goodCoin;       //Tells us if its a coin or cross
    private TextureRegion coinTexture;  //Grabs it texture

    /*
    Input: Good coin tells us if it's a coin or cross, size speed are standard var
           and coinBoost sets how much extra value the coin can get
    Output: Void
    Purpose: Creates a coin
    */
    public Coin(boolean goodCoin, float size, float speed, float coinBoost, TextureRegion[][] coinTexture){
        super(10 *  size, speed);
        //Gets a random value from 1 up
        value = MathUtils.random(1,size + coinBoost);
        //Sets if it is a coin or cross
        this.goodCoin = goodCoin;
        spriteSheet = coinTexture;
        setTexture();
    }

    /*
    Input: Void
    Output: Boolean
    Purpose: Tells us if it's a coin or cross
    */
    public boolean isGoodCoin(){return goodCoin;}

    /*
    Input: Void
    Output: Float
    Purpose: Tells us value of the coin
    */
    public float getValue(){return value;}

    /*
    Input: Void
    Output: Void
    Purpose: Grabs the coin/cross texture based on the goodCoin and value
    */
    public void setTexture(){
        int row = 0;
        int column = 0;
        if(!isGoodCoin()){ row = 1; }
        if(value > 2.5){column = 2;}
        else if (value > 1.5){column = 1;}
        //Sets the texture from sprit sheet
        this.coinTexture = spriteSheet[row][column];
    }

    /*
    Input: Sprite batch
    Output:Void
    Purpose: Draws the texture
    */
    public void draw(SpriteBatch batch) {batch.draw(coinTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height); }
}
