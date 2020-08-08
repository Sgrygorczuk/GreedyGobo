package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ParentObject {

    protected Animation frontAnimation;
    protected Animation backAnimation;
    protected Animation rightAnimation;
    protected Animation leftAnimation;
    protected TextureRegion[][] spriteSheet;

    protected float animationTime = 0;

    protected float speed;  //Speed of the object
    protected int direction = 0; //0 - Up, 1 = Down, 2 = Right, 3 = Left

    protected Rectangle hitBox;

    //Garbage constructor for player
    ParentObject(){}

    ParentObject(float size, float speed){
        this.speed = speed;
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
                y = 320 + size;
                break;
            }
        }
        if(direction < 2){y = MathUtils.random(size,320- size);}
        else{x = MathUtils.random(size,380 - size);}
        spawn(x, y, size, size);
    }

    public ParentObject(float speed){
        this.speed = speed;
        float size =  MathUtils.random(10,25);
        direction = 1;
        float x = 480 - size;
        float y = MathUtils.random(50, 380);
        spawn(x, y, size, size);
    }

    public void setUpLeafTexture(TextureRegion[][] texture){
        spriteSheet = texture;
        int leafColor = MathUtils.random(0, 2);
        frontAnimation= new Animation<>(0.2f, this.spriteSheet[0][leafColor]);
        frontAnimation.setPlayMode(Animation.PlayMode.LOOP);
        leftAnimation = new Animation<>(0.2f, this.spriteSheet[0][leafColor]);
        leftAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    protected void setUpSpriteSheet(TextureRegion[][] texture){
        this.spriteSheet = texture;
    }

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

    void spawn(float x, float y, float width, float height){ hitBox = new Rectangle(x, y, width, height); }

    public int getDirection(){return direction;}
    public float getX(){return hitBox.x;}
    public float getY(){return hitBox.y;}
    public float getWidth(){return hitBox.width;}
    public float getHeight(){return hitBox.height;}

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

    public void updateLeaf(){
        hitBox.x -= speed;
        hitBox.y = hitBox.y + 3 *MathUtils.sin(MathUtils.PI * hitBox.x/100);
    }

    /*
    Input: Void
    Output: Void
    Purpose:
    */
    public boolean isColliding(PlayerCharacter playerCharacter) {
        return Intersector.overlaps(playerCharacter.hitBox, hitBox);
    }

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
