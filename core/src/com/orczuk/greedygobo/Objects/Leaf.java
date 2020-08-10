package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Leaf extends ParentObject{

    /*
    Input:Speed and texture
    Output: Void
    Purpose: Create the leaf
    */
    public Leaf(float speed, TextureRegion[][] texture){
        super();
        this.speed = speed;
        spriteSheet = texture;
        //Get random size
        float size =  MathUtils.random(10,25);
        //Make it go from right to left
        direction = 1;
        float x = 480 - size;
        //Make it spawn at random y
        float y = MathUtils.random(50, 380);
        hitBox = new Rectangle(x, y, size, size);
        //Sets up the texture that will be used
        setUpLeafTexture();
    }

    /*
    Input: Void
    Output: Void;
    Purpose: Sets up the texture that the leaf will use
    */
    public void setUpLeafTexture(){
        int leafColor = MathUtils.random(0, 2);
        frontAnimation= new Animation<>(0.2f, this.spriteSheet[0][leafColor]);
        frontAnimation.setPlayMode(Animation.PlayMode.LOOP);
        leftAnimation = new Animation<>(0.2f, this.spriteSheet[0][leafColor]);
        leftAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /*
    Input: Void
    Output: Void;
    Purpose: Moves the leaf to the left and up and down along a sin wave
    */
    public void updateLeaf(){
        hitBox.x -= speed;
        hitBox.y = hitBox.y + 3 * MathUtils.sin(MathUtils.PI * hitBox.x/100);
    }
}
