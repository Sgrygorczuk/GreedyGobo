package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ParentObject {

    //Garbage constructor for player
    ParentObject(){}

    ParentObject(float baseSize, float size, float speed){
        this.speed = speed;
        direction = MathUtils.random(0,3);
        value = MathUtils.random(1,size/baseSize);
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
                y = 320 + size;
                break;
            }
        }
        if(direction < 2){y = MathUtils.random(size,320- size);}
        else{x = MathUtils.random(size,480- size);}
        spawn(x, y, size, size);
    }


    protected float value;    //Value of the collected item
    protected float speed;  //Speed of the object
    protected int direction = 0; //0 - Up, 1 = Down, 2 = Right, 3 = Left

    protected Rectangle hitBox;

    void spawn(float x, float y, float width, float height){ hitBox = new Rectangle(x, y, width, height); }

    public int getDirection(){return direction;}
    public float getX(){return hitBox.x;}
    public float getY(){return hitBox.y;}
    public float getWidth(){return hitBox.width;}
    public float getHeight(){return hitBox.height;}
    public float getValue(){return value;}

    public void update(int direction){
        switch (direction){
            case 0:{
                hitBox.x += speed;
                break;
            }
            case 1:{
                hitBox.x -= speed;
                break;
            }
            case 2:{
                hitBox.y += speed;
                break;
            }
            case 3:{
                hitBox.y -= speed;
                break;
            }
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose:
    */
    public boolean isColliding(PlayerCharacter playerCharacter) {
        return Intersector.overlaps(playerCharacter.hitBox, hitBox);
    }

    /*
    Input: ShapeRenderer
    Output:Void
    Purpose: Draws the wireframe
    */
    public void drawDebug(ShapeRenderer shapeRenderer) {shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height); }
}
