package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ParentObject {

    //The movement animations
    protected Animation frontAnimation;
    protected Animation backAnimation;
    protected Animation rightAnimation;
    protected Animation leftAnimation;

    //Sprite sheet used
    protected TextureRegion[][] spriteSheet;

    //Current animation frame time
    protected float animationTime = 0;

    protected float speed;  //Speed of the object
    protected int direction = 0; //1 - Up, 0 = Down, 2 = Right, 3 = Left

    //Hit box
    protected Rectangle hitBox;

    /*
    Input: Void
    Output: Void;
    Purpose: Garbage constructor fo the player object
    */
    ParentObject(){}

    /*
    Input: Size and speed of he object
    Output: Void;
    Purpose: Constructor used of coins/abbots and knights
    */
    ParentObject(float size, float speed){
        this.speed = speed;                 //Sets speed
        direction = MathUtils.random(0,3);  //Picks random direction
        float x = 0;
        float y = 0;
        //Based on direction spawns on the opposite side of the screen
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
        //Based on direction randomly chooses the x or y
        if(direction < 2){y = MathUtils.random(size,320- size);}
        else{x = MathUtils.random(size,380 - size);}
        //Creates the object
        hitBox = new Rectangle(x, y, size, size);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the animation loops in all of the directions
    */
    protected void setUpAnimation(){
        frontAnimation= new Animation<>(0.2f, this.spriteSheet[0][0], this.spriteSheet[0][1],
                this.spriteSheet[0][0], this.spriteSheet[0][2]);
        frontAnimation.setPlayMode(Animation.PlayMode.LOOP);

        backAnimation= new Animation<>(0.2f, this.spriteSheet[1][0], this.spriteSheet[1][1],
                this.spriteSheet[1][0], this.spriteSheet[1][2]);
        backAnimation.setPlayMode(Animation.PlayMode.LOOP);

        rightAnimation = new Animation<>(0.2f, this.spriteSheet[2][0], this.spriteSheet[2][1],
                this.spriteSheet[2][0], this.spriteSheet[2][2]);
        rightAnimation.setPlayMode(Animation.PlayMode.LOOP);

        leftAnimation = new Animation<>(0.2f, this.spriteSheet[3][0], this.spriteSheet[3][1],
                this.spriteSheet[3][0], this.spriteSheet[3][2]);
        leftAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Tells us which direction they are facing
    */
    public int getDirection(){return direction;}

    /*
    Input: Void
    Output: Void
    Purpose: Tells us their x coordinate
    */
    public float getX(){return hitBox.x;}

    /*
    Input: Void
    Output: Void
    Purpose: Tells us their y coordinate
    */
    public float getY(){return hitBox.y;}

    /*
    Input: Void
    Output: Void
    Purpose: Tells us their dimensions
    */
    public float getWidth(){return hitBox.width;}

    /*
    Input: Void
    Output: Void
    Purpose: Updates the position of the object
    */
    public void update(int direction, float delta){
        animationTime += delta;
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
    Purpose: Tells us if they collided with the player
    */
    public boolean isColliding(PlayerCharacter playerCharacter) { return Intersector.overlaps(playerCharacter.hitBox, hitBox); }

    /*
    Input: Sprite Batch
    Output: Void
    Purpose: Draws the current frame of animation
    */
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = (TextureRegion) frontAnimation.getKeyFrame(animationTime);
        if (direction == 2) {
            currentFrame = (TextureRegion) backAnimation.getKeyFrame(animationTime);
        }
        else if (direction == 0) {
            currentFrame = (TextureRegion) rightAnimation.getKeyFrame(animationTime);
        }
        else if (direction == 1) {
            currentFrame = (TextureRegion) leftAnimation.getKeyFrame(animationTime);
        }
        batch.draw(currentFrame, hitBox.x - 2.5f, hitBox.y - 2.5f, hitBox.width + 5, hitBox.height + 5);
    }

    /*
    Input: ShapeRenderer
    Output:Void
    Purpose: Draws the wireframe
    */
    public void drawDebug(ShapeRenderer shapeRenderer) {shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height); }
}
