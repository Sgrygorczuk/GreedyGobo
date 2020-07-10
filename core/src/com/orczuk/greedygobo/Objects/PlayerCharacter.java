package com.orczuk.greedygobo.Objects;

public class PlayerCharacter extends ParentObject{

    protected float width = 10;  //Width of the hit box
    protected float height = 15;
    private  float speedX;
    private float speedY;
    private boolean isInArena = false;
    private boolean isHeld = false;
    private boolean isStunned = false;


    //Timing variables
    private static final float STUNNED_TIME = 5F;                 //Time that the conversation box stays on screen
    private float stunnedTimer = STUNNED_TIME;                        //Counter that checks if it reached the end of time


    public PlayerCharacter(float x, float y){
        super();
        speed = 3;
        direction = 1; //Down
        spawn(x, y, width, height);
    }

    void movedOffScreen(){
        if(hitBox.x + hitBox.width < 0){hitBox.x = 380;}
        else if(hitBox.x  > 380){hitBox.x = 0;}

        if(hitBox.y + hitBox.height < 0){hitBox.y = 320;}
        else if(hitBox.y > 320){hitBox.y = 0;}
    }

    public void boostY(float speed){ speedY = speed; }

    public void boostX(float speed){ speedX = speed; }

    public void update(){
        movedOffScreen();

        if(speedX > 0){speedX -= 0.01;}
        else{speedX += 0.01;}
        if(speedY > 0){speedY -= 0.01;}
        else{speedY += 0.01;}

        hitBox.x += speedX;
        hitBox.y += speedY;
    }

    public boolean isInArena(){return isInArena;}
    public void setInArena(boolean inArena){this.isInArena = inArena;}

    public void setHeld(boolean isHeld){this.isHeld = isHeld;}
    public boolean isHeld(){return isHeld;}

    public boolean isStunned(){return isStunned;}
    public void setStunned(boolean isStunned){this.isStunned = isStunned;}

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
}
