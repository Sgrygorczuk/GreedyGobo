package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;

public class Bishop extends ParentObject{

    public Bishop(float size, float speed, Texture spriteSheet){
        super(size, speed);
        setUpSpriteSheet(spriteSheet);
        setUpAnimation();
    }
}
