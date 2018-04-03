package com.turnbasedgdx.game.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.turnbasedgdx.game.TurnBasedGdxGame;

import java.io.Serializable;

/**
 * Created by Deadvard on 2017-02-24.
 */

public class MovableSprite extends Sprite implements Serializable{

    public static final int size = TurnBasedGdxGame.WIDTH/10;

    public MovableSprite(Texture texture){
        super(texture);
        this.setSize(size,size);
    }

    public MovableSprite(){
        super();
    }

    public void changeFrame(int x, int y){

        this.setRegion(this.size * x, this.size * y, this.size, this.size);
    }

    public boolean isPosition(int x, int y){
        boolean result = false;
        if((int)(this.getX()/size) == x && (int)((TurnBasedGdxGame.HEIGHT - this.getY() - size)/size) == y){
            result = true;
        }

        return result;
    }

    public void setPos(int x, int y){
        this.setPosition(x * size, (TurnBasedGdxGame.HEIGHT - (y * size) - size));
    }

    public int xCord(){
        return (int) (this.getX()/size);
    }

    public int yCord(){
        return (int) ((TurnBasedGdxGame.HEIGHT - this.getY() - size)/size);
    }
}
