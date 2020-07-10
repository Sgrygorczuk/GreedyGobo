package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class ParentObject {

    protected float size = 1;   //Size based on score (mods the width/height) between 0.1f-2f
    protected int value;    //Value of the collected item
    protected float speed;  //Speed of the object
    protected int direction = 0; //0 - Up, 1 = Down, 2 = Right, 3 = Left

    protected Rectangle hitBox;

    void spawn(float x, float y, float width, float height){ hitBox = new Rectangle(x, y, width, height); }

    public int getDirection(){return direction;}
    public float getX(){return hitBox.x;}
    public float getY(){return hitBox.y;}
    public float getWidth(){return hitBox.width;}
    public float getHeight(){return hitBox.height;}
    public int getValue(){return value;}

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
