package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Knight extends ParentObject{

    /*
    Input: Size, speed and texture
    Output: Void
    Purpose: Creates a knight
    */
    public Knight(float size, float speed, TextureRegion[][] spriteSheet){
        super(size, speed);
        this.spriteSheet = spriteSheet;
        setUpAnimation();
    }
}
