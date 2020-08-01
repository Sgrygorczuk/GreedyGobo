package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Knight extends ParentObject{

    public Knight(float size, float speed, Texture spriteSheet){
        super(size, speed);
        setUpSpriteSheet(spriteSheet);
        setUpAnimation();
    }

    public void updateSize(float width){
        hitBox.width = width;
        hitBox.height = width;
    }
}
