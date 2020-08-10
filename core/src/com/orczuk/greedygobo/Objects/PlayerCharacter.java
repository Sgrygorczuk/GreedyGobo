package com.orczuk.greedygobo.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PlayerCharacter extends ParentObject {

    protected float width = 10;         //Width of the hit box
    private float oldWidth;             //Old width before gobo falls in portal
    private float portalWidth = 0;      //Width of the portal
    //Where gobo was before he started falling
    private float oldX;
    private float oldY;
    //Gobo's speed
    private float speedX;
    private float speedY;
    private boolean isInArena = false;  //Is he in arena or UI
    private boolean isHeld = false;     //Is the player holding him
    private boolean isStunned = false;  //Is he stunned
    private boolean isFalling = false;  //IS he falling
    private boolean isStopped = true;   //Is he not moving (make animations stop)
    private int portalOpening = 0; //0 is closed, 1 is opening, 2 is closing

    private TextureRegion holdingBagTexture;
    protected Texture portalTexture;    //Portal texture
    //Extra animation only for gobo
    private Animation stunAnimation;
    private Animation fallAnimation;
    private Animation waveAnimation;


    //Timer counting down until he naturally de stuns
    private static final float STUNNED_TIME = 5F;
    private float stunnedTimer = STUNNED_TIME;

    /*
    Input: Void
    Output: Void
    Purpose: Create the player character
    */
    public PlayerCharacter(float x, float y, TextureRegion[][] goboSpriteSheet, Texture portalTexture) {
        super();    //Uses the empty constructor
        direction = 1; //Down
        this.spriteSheet = goboSpriteSheet; //Set up the sprites
        setUpAnimation();                   //Set up animation
        this.portalTexture = portalTexture; //Set up portal
        hitBox = new Rectangle(x, y, width, width); //Create hit box
    }

    /*
    Input: Void
    Output: Void
    Purpose: Uses the generic animation in addition to extra ones for gobo
    */
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

    /*
    Input: Float width
    Output: Void
    Purpose: Updates gobo's size
    */
    public void setDimensions(float width) {
        hitBox.width = width;
        hitBox.height = width;
    }

    /*
    Input: Float speed
    Output: Void
    Purpose: Sets gobo's Y speed to be that which is passed in
    */
    public void boostY(float speed) { speedY = speed; }

    /*
    Input: Float speed
    Output: Void
    Purpose: Sets gobo's X speed to be that which is passed in
    */
    public void boostX(float speed) { speedX = speed; }

    /*
    Input: Void
    Output: Void
    Purpose: Updates gobo's position
    */
    public void update() {
        movedOffScreen(); //Checks if he moved off screen
        speedUpdate();    //Slows him momentum over time
        //Moves him according to his speed
        hitBox.x += speedX;
        hitBox.y += speedY;
    }

    /*
    Input: Void
    Output: Void
    Purpose: Moves gobo onto the other side of the screen if he leave on one end
    */
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

    /*
    Input: Void
    Output: Void
    Purpose: Lowers gobo's speed over time
    */
    public void speedUpdate() {
        if (speedX > 0) { speedX -= 0.01; } else { speedX += 0.01; }
        if (speedY > 0) { speedY -= 0.01; } else { speedY += 0.01; }
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Moves the frames of animation
    */
    public void updateAnimation(float delta) { animationTime +=delta; }

    /*
    Input: int direction
    Output: Void
    Purpose: Updates which direction gobo is looking
    */
    public void updateDirection(int direction){ this.direction = direction;}

    /*
    Input:
    Output: Void
    Purpose: Setting Flags
    */
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

    /*
    Input: int direction
    Output: Void
    Purpose: Updates which direction gobo is looking
    */
    public float getSpeedY(){return speedY;}
    public float getSpeedX(){return speedX;}

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

    /*
    Input: Float delta
    Output: Void
    Purpose: Naturally ticks down till he's no longer stunned
    */
    public void stunnedTimer(float delta){
        stunnedTimer -= delta;
        if (stunnedTimer <= 0) { deStun(); }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Turns his stun off
    */
    public void deStun(){
        stunnedTimer = STUNNED_TIME;
        isStunned = false;
    }

    /*
    Input: Float size
    Output: Void
    Purpose: Grows gobo
    */
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
        //Draws regular movement
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
            //Draws him stunned
            else {
                currentFrame = (TextureRegion) stunAnimation.getKeyFrame(animationTime);
            }
        }
        //Draws his out of the arena
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
