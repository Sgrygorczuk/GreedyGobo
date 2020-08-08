package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Knight extends ParentObject{

    public Knight(float size, float speed, TextureRegion[][] spriteSheet){
        super(size, speed);
        setUpSpriteSheet(spriteSheet);
        setUpAnimation();
    }
}
