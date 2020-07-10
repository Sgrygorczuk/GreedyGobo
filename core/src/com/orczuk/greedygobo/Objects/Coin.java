package com.orczuk.greedygobo.Objects;


public class Coin extends ParentObject{

    private boolean goodCoin;

    public Coin(boolean goodCoin, float size, float speed){
        super(10, 10 *  size, speed);
        this.goodCoin = goodCoin;
    }

    public boolean isGoodCoin(){return goodCoin;}
}
