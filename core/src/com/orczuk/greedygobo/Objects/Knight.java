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

    public void updatePosition(){
        float size = hitBox.width;
        direction = MathUtils.random(0,3);
        float x = 0;
        float y = 0;
        switch (direction){
            case 0:{
                x = -size;
                break;
            }
            case 1:{
                x = 380+ size;
                break;
            }
            case 2:{
                y = -size;
                break;
            }
            case 3:{
                y = 320+ size;
                break;
            }
        }
        if(direction < 2){y = MathUtils.random(size,320- size);}
        else{x = MathUtils.random(size,480- size);}
        spawn(x, y, size, size);
    }
}
