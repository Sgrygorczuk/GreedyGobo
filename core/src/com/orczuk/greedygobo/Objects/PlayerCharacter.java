package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerCharacter extends ParentObject{

    protected float width = 15;  //Width of the hit box
    protected float height = 15;
    private  float speedX;
    private float speedY;
    private boolean isInArena = false;
    private boolean isHeld = false;
    private boolean isStunned = false;
    private boolean isFalling = false;
    private boolean isStopped = true;

    private TextureRegion[][] goboSpriteSheet;
    private TextureRegion holdingBagTexture;
    private Animation frontAnimation;
    private Animation backAnimation;
    private Animation rightAnimation;
    private Animation leftAnimation;
    private Animation stunAnimation;
    private Animation fallAnimation;
    private Animation waveAnimation;
    private float animationTime = 0;

    //Timing variables
    private static final float STUNNED_TIME = 5F;                 //Time that the conversation box stays on screen
    private float stunnedTimer = STUNNED_TIME;                        //Counter that checks if it reached the end of time



    public PlayerCharacter(float x, float y, Texture goboSpriteSheet){
        super();
        speed = 3;
        direction = 1; //Down

        this.goboSpriteSheet = new TextureRegion(goboSpriteSheet).split(420, 420); //Breaks down the texture into tiles

        setUpAnimations();

        spawn(x, y, width, height);
    }

    void setUpAnimations(){
        frontAnimation= new Animation<>(0.2f, this.goboSpriteSheet[0][0], this.goboSpriteSheet[0][1],
                this.goboSpriteSheet[0][0], this.goboSpriteSheet[0][2]);
        frontAnimation.setPlayMode(Animation.PlayMode.LOOP);

        backAnimation= new Animation<>(0.2f, this.goboSpriteSheet[1][0], this.goboSpriteSheet[1][1],
                this.goboSpriteSheet[1][0], this.goboSpriteSheet[1][2]);
        backAnimation.setPlayMode(Animation.PlayMode.LOOP);

        rightAnimation = new Animation<>(0.2f, this.goboSpriteSheet[2][0], this.goboSpriteSheet[2][1],
                this.goboSpriteSheet[2][0], this.goboSpriteSheet[2][2]);
        rightAnimation.setPlayMode(Animation.PlayMode.LOOP);

        leftAnimation = new Animation<>(0.2f, this.goboSpriteSheet[3][0], this.goboSpriteSheet[3][1],
                this.goboSpriteSheet[3][0], this.goboSpriteSheet[3][2]);
        leftAnimation.setPlayMode(Animation.PlayMode.LOOP);

        waveAnimation = new Animation<>(0.15f, this.goboSpriteSheet[0][3], this.goboSpriteSheet[0][4],
                this.goboSpriteSheet[0][3], this.goboSpriteSheet[0][5]);
        waveAnimation.setPlayMode(Animation.PlayMode.LOOP);

        stunAnimation = new Animation<>(0.15f, this.goboSpriteSheet[1][3], this.goboSpriteSheet[1][4]);
        stunAnimation.setPlayMode(Animation.PlayMode.LOOP);

        fallAnimation = new Animation<>(0.15f, this.goboSpriteSheet[2][3], this.goboSpriteSheet[2][4]);
        fallAnimation.setPlayMode(Animation.PlayMode.LOOP);

        holdingBagTexture = this.goboSpriteSheet[3][3];
    }

    void movedOffScreen(){
        if(hitBox.x + hitBox.width < 0){hitBox.x = 380;}
        else if(hitBox.x  > 380){hitBox.x = 0;}

        if(hitBox.y + hitBox.height < 0){hitBox.y = 320;}
        else if(hitBox.y > 320){hitBox.y = 0;}
    }

    public void setDimensions(float width, float height){
        hitBox.width = width;
        hitBox.height = height;
    }

    public void boostY(float speed){ speedY = speed; }

    public void boostX(float speed){ speedX = speed; }

    public void update(float delta){
        movedOffScreen();
        if(speedX > 0){speedX -= 0.01;}
        else{speedX += 0.01;}
        if(speedY > 0){speedY -= 0.01;}
        else{speedY += 0.01;}

        hitBox.x += speedX;
        hitBox.y += speedY;
        animationTime += delta;
    }

    public void updateDirection(int direction){ this.direction = direction;}

    public boolean isInArena(){return isInArena;}
    public void setInArena(boolean inArena){this.isInArena = inArena;}

    public void setHeld(boolean isHeld){this.isHeld = isHeld;}
    public boolean isHeld(){return isHeld;}

    public boolean isStunned(){return isStunned;}
    public void setStunned(boolean isStunned){this.isStunned = isStunned;}

    public boolean isFalling(){return isFalling;}
    public void setFalling(boolean isFalling){this.isFalling = isFalling;}

    public void setStopped(boolean isStopped){this.isStopped = isStopped;}

    public void setPosition(float x, float y){
        hitBox.x = x;
        hitBox.y = y;
    }

    public void stunnedTimer(float delta){
        stunnedTimer -= delta;
        if (stunnedTimer <= 0) { deStun(); }
    }

    public void deStun(){
        stunnedTimer = STUNNED_TIME;
        isStunned = false;
    }

    public void updateSize(float size){
        hitBox.width = width * size;
        hitBox.height = height * size;
    }

    /*
    Input: ShapeRenderer
    Output:Void
    Purpose: Draws the wireframe
    */
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = (TextureRegion) frontAnimation.getKeyFrame(0);
        if (isInArena()) {
            if (!isStunned) {
                if (!isStopped) {
                    currentFrame = (TextureRegion) frontAnimation.getKeyFrame(animationTime);
                    if (direction == 1) {
                        currentFrame = (TextureRegion) backAnimation.getKeyFrame(animationTime);
                    } else if (direction == 2) {
                        currentFrame = (TextureRegion) rightAnimation.getKeyFrame(animationTime);
                    } else if (direction == 3) {
                        currentFrame = (TextureRegion) leftAnimation.getKeyFrame(animationTime);
                    }
                }
            }
            else {
                currentFrame = (TextureRegion) stunAnimation.getKeyFrame(animationTime);
            }
        }
        else{
            if(isFalling()){
                currentFrame = (TextureRegion) fallAnimation.getKeyFrame(animationTime);
            }
            else if(isHeld){
                currentFrame = holdingBagTexture;
            }
            else{
                currentFrame = (TextureRegion) waveAnimation.getKeyFrame(animationTime);
            }
        }
        batch.draw(currentFrame, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
