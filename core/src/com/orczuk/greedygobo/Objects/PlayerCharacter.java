package com.orczuk.greedygobo.Objects;

public class PlayerCharacter extends ParentObject{

    protected float width = 10;  //Width of the hit box
    protected float height = 15;
    private  float speedX;
    private float speedY;

    public PlayerCharacter(float x, float y){
        speed = 3;
        direction = 1; //Down
        spawn(x, y, width, height);
    }

    void movedOffScreen(){
        if(hitBox.x + hitBox.width < 0){hitBox.x = 480;}
        else if(hitBox.x  > 480){hitBox.x = 0;}

        if(hitBox.y + hitBox.height < 0){hitBox.y = 320;}
        else if(hitBox.y > 320){hitBox.y = 0;}
    }

    public void boostY(float speed){ speedY = speed; }

    public void boostX(float speed){ speedX = speed; }

    public void update(){
        movedOffScreen();

        if(speedX > 0){speedX -= 0.001;}
        else{speedX += 0.001;}
        if(speedY > 0){speedY -= 0.001;}
        else{speedY += 0.001;}

        hitBox.x += speedX;
        hitBox.y += speedY;
    }

    public void updateSize(float size){
        hitBox.width = width * size;
        hitBox.height = height * size;
    }
}
