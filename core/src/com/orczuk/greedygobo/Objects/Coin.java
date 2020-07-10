package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.math.MathUtils;

public class Coin extends ParentObject{
    private boolean goodCoin;

    public Coin(int direction, boolean goodCoin, float size){
        this.goodCoin = goodCoin;
        this.direction = direction;
        speed = 1;
        size = MathUtils.random(1, size);
        value = (int) size;
        float x = 0;
        float y = 0;
        float coinWidth = 10 * size;
        switch (direction){
            case 0:{
                x = -coinWidth;
                break;
            }
            case 1:{
                x = 480+ coinWidth;
                break;
            }
            case 2:{
                y = -coinWidth;
                break;
            }
            case 3:{
                y = 320+ coinWidth;
                break;
            }
        }
        coinWidth *= size;
        if(direction < 2){y = MathUtils.random(coinWidth,320- coinWidth);}
        else{x = MathUtils.random(coinWidth,480- coinWidth);}
        spawn(x, y, coinWidth, coinWidth);
    }

    public boolean isGoodCoin(){return goodCoin;}
}
