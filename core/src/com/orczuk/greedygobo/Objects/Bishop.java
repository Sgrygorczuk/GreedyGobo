package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bishop extends ParentObject{

    public Bishop(float size, float speed, TextureRegion[][] spriteSheet){
        super(size, speed);
        setUpSpriteSheet(spriteSheet);
        setUpAnimation();
    }
}
