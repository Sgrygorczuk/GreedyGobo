package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends ParentObject{

    private boolean goodCoin;
    private TextureRegion coinTexture;

    public Coin(boolean goodCoin, float size, float speed){
        super(10, 10 *  size, speed);
        this.goodCoin = goodCoin;
    }

    public boolean isGoodCoin(){return goodCoin;}

    public void setTexture(TextureRegion[][] coinTexture){
        int row = 0;
        int column = 0;
        if(!isGoodCoin()){ row = 1; }
        if(value > 3){column = 2;}
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
