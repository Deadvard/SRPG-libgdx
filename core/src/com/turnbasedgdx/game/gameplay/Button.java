package com.turnbasedgdx.game.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.turnbasedgdx.game.TurnBasedGdxGame;

/**
 * Created by Deadvard on 2017-03-03.
 */

public class Button extends MovableSprite {

    private String title;

    public Button(Texture texture, String title, int width, int height) {
        super(texture);
        this.setAlpha(100);
        this.title = title;
        this.changeFrame(1,0);
        this.setSize(width,height);
    }

    public boolean contains(float x, float y){
        boolean result = false;

        if(((this.getX() + this.getWidth()) > x) && (this.getX() < x) &&
                (TurnBasedGdxGame.HEIGHT - this.getY() - this.getHeight()) < y &&
                (TurnBasedGdxGame.HEIGHT - this.getY()) > y){
            result = true;
        }

        return result;
    }

    public String title(){
        return title;
    }

    public int getCenterX(){
        return (int)((this.getX()+this.getWidth())-(this.getWidth()/2));
    }

    public int getCenterY(){
        return (int)((this.getY()+this.getHeight())-(this.getHeight()/2));
    }
}
