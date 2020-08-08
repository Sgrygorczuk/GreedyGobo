package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.swing.SpringLayout;

public class PlayerCharacter extends ParentObject {

    protected float width = 10;  //Width of the hit box
    private float oldWidth;
    private float portalWidth = 0;
    private float oldX;
    private float oldY;
    private float speedX;
    private float speedY;
    private boolean isInArena = false;
    private boolean isHeld = false;
    private boolean isStunned = false;
    private boolean isFalling = false;
    private boolean isStopped = true;
    private int portalOpening = 0; //0 is closed, 1 is opening, 2 is closing

    private TextureRegion holdingBagTexture;
    protected Texture portalTexture;
    private Animation stunAnimation;
    private Animation fallAnimation;
    private Animation waveAnimation;


    //Timing variables
    private static final float STUNNED_TIME = 5F;                 //Time that the conversation box stays on screen
    private float stunnedTimer = STUNNED_TIME;                        //Counter that checks if it reached the end of time


    public PlayerCharacter(float x, float y, TextureRegion[][] goboSpriteSheet, Texture portalTexture) {
        super();
        speed = 3;
        direction = 1; //Down

        setUpSpriteSheet(goboSpriteSheet);
        setUpAnimation();
        this.portalTexture = portalTexture;

        spawn(x, y, width, width);
    }

    @Override
    protected void setUpAnimation() {
        //Sets up the up, down, left, right animations
        super.setUpAnimation();

        //Sets up extra animation only for player
        waveAnimation = new Animation<>(0.15f, this.spriteSheet[0][3], this.spriteSheet[0][4],
                this.spriteSheet[0][3], this.spriteSheet[0][5]);
        waveAnimation.setPlayMode(Animation.PlayMode.LOOP);

        stunAnimation = new Animation<>(0.15f, this.spriteSheet[1][3], this.spriteSheet[1][4]);
        stunAnimation.setPlayMode(Animation.PlayMode.LOOP);

        fallAnimation = new Animation<>(0.15f, this.spriteSheet[2][3], this.spriteSheet[2][4]);
        fallAnimation.setPlayMode(Animation.PlayMode.LOOP);

        holdingBagTexture = this.spriteSheet[3][3];
    }

    void movedOffScreen() {
        if (hitBox.x + hitBox.width < 0) {
            hitBox.x = 380;
        } else if (hitBox.x > 380) {
            hitBox.x = 0;
        }

        if (hitBox.y + hitBox.height < 0) {
            hitBox.y = 320;
        } else if (hitBox.y > 320) {
            hitBox.y = 0;
        }
    }

    public void setDimensions(float width, float height) {
        hitBox.width = width;
        hitBox.height = height;
    }

    public void boostY(float speed) { speedY = speed; }

    public void boostX(float speed) { speedX = speed; }

    public void update() {
        movedOffScreen();
        if (speedX > 0) { speedX -= 0.01; } else { speedX += 0.01; }
        if (speedY > 0) { speedY -= 0.01; } else { speedY += 0.01; }

        hitBox.x += speedX;
        hitBox.y += speedY;
    }

    public void updateAnimation(float delta) { animationTime +=delta; }

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

    public float getOldWidth(){return oldWidth;}
    public void setOldWidth(float oldWidth){this.oldWidth = oldWidth;}

    public int getPortalOpening(){return portalOpening;}
    public void setPortalOpening(int portalOpening){this.portalOpening = portalOpening;}

    public float getPortalWidth(){return  portalWidth;}
    public void setPortalWidth(float width){portalWidth = width;}

    public float getSpeedY(){return speedY;}

    public float getOldX(){return  oldX;}
    public float getOldY(){return  oldY;}
    public void setOldCoordinates(float x, float y){
        oldX = x;
        oldY = y;
    }

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
        hitBox.height = width * size;
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
        if(portalOpening != 0){ batch.draw(portalTexture, oldX, oldY, portalWidth, portalWidth);}
        batch.draw(currentFrame, hitBox.x - 2.5f, hitBox.y - 2.5f, hitBox.width + 5, hitBox.height + 5);
    }

}
