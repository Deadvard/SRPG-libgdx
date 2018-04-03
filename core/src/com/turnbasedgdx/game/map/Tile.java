package com.turnbasedgdx.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.turnbasedgdx.game.gameplay.MovableSprite;

/**
 * Created by Deadvard on 2017-02-24.
 */

public class Tile extends MovableSprite {

    private boolean blocked;
    private boolean spawnpoint1;
    private boolean spawnpoint2;
    private boolean canMoveTo;

    public Tile(Texture texture){
        super(texture);
        blocked = false;
        spawnpoint1 = false;
        spawnpoint2 = false;
        canMoveTo = false;
    }

    public boolean getBlocked(){
        return this.blocked;
    }

    public boolean getSpawnpoint1(){
        return this.spawnpoint1;
    }

    public boolean getSpawnpoint2(){
        return this.spawnpoint2;
    }

    public boolean getCanMoveTo(){
        return this.canMoveTo;
    }

    public void setBlocked(boolean bool){
        this.blocked = bool;
    }

    public void setSpawnpoint1(boolean bool){
        this.spawnpoint1 = bool;
    }

    public void setSpawnpoint2(boolean bool){
        this.spawnpoint2 = bool;
    }

    public void setCanMoveTo(boolean bool){
        this.canMoveTo = bool;
    }
}
