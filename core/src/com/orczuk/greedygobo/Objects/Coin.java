package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Coin extends ParentObject{

    private float value;    //Value of the collected item
    private boolean goodCoin;
    private TextureRegion coinTexture;

    public Coin(boolean goodCoin, float size, float speed, float coinBoost){
        super(10 *  size, speed);
        value = MathUtils.random(1,size + coinBoost);
        this.goodCoin = goodCoin;
    }

    public boolean isGoodCoin(){return goodCoin;}

    public float getValue(){return value;}

    public void setTexture(TextureRegion[][] coinTexture){
        int row = 0;
        int column = 0;
        if(!isGoodCoin()){ row = 1; }
        if(value > 2.5){column = 2;}
        else if (value > 1.5){column = 1;}

        this.coinTexture = coinTexture[row][column];
    }

    /*
    Input: ShapeRenderer
    Output:Void
    Purpose: Draws the wireframe
    */
    public void draw(SpriteBatch batch) {batch.draw(coinTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height); }

}
