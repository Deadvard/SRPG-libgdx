package com.turnbasedgdx.game.gameplay;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;

/**
 * Created by Deadvard on 2017-02-24.
 */

public class Character extends MovableSprite implements Serializable{

    private static final long serialVersionUID = 1L;
    private int health;
    private int damage;
    private int luck;
    private int critical;

    private int moveDistance;
    private int attackDistance;

    private int level;
    private int experience;

    private boolean hasMoved;
    private boolean isSelected;

    public Character()
    {
        super();
    }

    public Character(Texture texture)
    {
        super(texture);

        this.health = 10;
        this.damage = 1;
        this.luck = 1;
        this.critical = 1;

        this.moveDistance = 2;
        this.attackDistance = 2;

        this.level = 1;
        this.experience = 0;

        this.hasMoved = false;
        this.isSelected = false;
    }

    public Character(Texture texture, int health, int damage, int luck, int critical, int weakness,
                     int moveDistance, int level)
    {
        super(texture);
        this.health = health;
        this.damage = damage;
        this.luck = luck;
        this.critical = critical;
        this.moveDistance = moveDistance;
        this.level = level;
        this.experience = 0;
        this.hasMoved = false;
    }

    public int getMoveDistance()
    {
        return this.moveDistance;
    }

    public int getAttackDistance()
    {
        return this.attackDistance;
    }

    public boolean getHasMoved()
    {
        return this.hasMoved;
    }

    public boolean getIsSelected()
    {
        return this.isSelected;
    }

    public void setHasMoved(boolean bool)
    {
        this.hasMoved = bool;
    }

    public void setIsSelected(boolean bool)
    {
        this.isSelected = bool;
    }

    public String getHealth()
    {
        return Integer.toString(this.health);
    }

    public void attack(Character other){

        int damageResult = this.damage;

        if((Math.random() * 100 + this.luck) < other.luck){
            damageResult = 0;
        }

        if((Math.random() * 100) < this.critical) {
            damageResult *= 2;
        }

        other.health -= damageResult;
        this.experience += damageResult / (this.level * this.level);

        if(this.experience >= 10){
            this.levelUp();
        }
    }

    public String[] statistics(){
        String arr[] = new String[5];
        arr[0] = "Level: "+ Integer.toString(this.level);
        arr[1] = "Health: "+ Integer.toString(this.health);
        arr[2] = "Damage: "+ Integer.toString(this.damage);
        arr[3] = "Luck: "+ Integer.toString(this.luck);
        arr[4] = "Critical: "+ Integer.toString(this.critical);

        return arr;
    }

    private void levelUp(){
        this.experience = this.experience % 100;
        this.level += 1;

        this.health += (Math.random() * 2);
        this.damage += (Math.random() * 2);
        this.luck += (Math.random() * 2);
        this.critical += (Math.random() * 2);
    }
}
