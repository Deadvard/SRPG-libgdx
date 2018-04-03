package com.turnbasedgdx.game.states;

/**
 * Created by Deadvard on 2017-02-27.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turnbasedgdx.game.TurnBasedGdxGame;
import com.turnbasedgdx.game.gameplay.Character;

public class BattleState extends GameState {

    private BitmapFont font = new BitmapFont();
    private Sound hit;
    private Texture hitEffect;
    private Texture background;
    private Texture ui;
    private Character[] characters;
    private int posX1;
    private int posY1;
    private int posX2;
    private int posY2;
    private int index;
    private boolean showHit;

    int frame = 0;
    float effectTime = 0f;
    float animTime = 0f;

    public BattleState(GameStateManager manager, Character c1, Character c2){
        super(manager);
        background = new Texture("background0.png");
        ui = new Texture("ui2.png");
        hitEffect = new Texture("flare.png");
        this.characters = new Character[2];
        this.characters[0] = c1;
        this.characters[1] = c2;
        posX1 = this.characters[0].xCord();
        posY1 = this.characters[0].yCord();
        posX2 = this.characters[1].xCord();
        posY2 = this.characters[1].yCord();
        index = 1;
        showHit = false;
        this.characters[0].setPos(2,8);
        this.characters[1].setPos(7,8);
        this.hit = Gdx.audio.newSound(Gdx.files.internal("10.ogg"));
    }


    @Override
    public void handleInput() {

        if(Gdx.input.justTouched()){
            effectTime =1.9f;
        }
    }

    @Override
    public void update(float dt) {
        if(index < 0){
            this.leaveState();
        }
        animTime += dt;
        effectTime += dt;
        handleInput();
        updateAttack();
        updateFrame();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, TurnBasedGdxGame.HEIGHT-TurnBasedGdxGame.WIDTH,
                TurnBasedGdxGame.WIDTH, TurnBasedGdxGame.HEIGHT);
        batch.draw(ui, 0, 0, TurnBasedGdxGame.WIDTH,
                TurnBasedGdxGame.HEIGHT-TurnBasedGdxGame.WIDTH);
        batch.end();
        batch.begin();
        characters[0].draw(batch);
        characters[1].draw(batch);
       if(showHit){
           batch.draw(hitEffect, characters[index].getX()-48, characters[index].getY()-48);
       }
        batch.end();
    }

    private void updateAttack(){
        if(effectTime > 1.8f && effectTime < 2.0f){
            showHit = true;
        }
        else{
            showHit = false;
        }

        if(effectTime > 2.0f){
            effectTime = 0f;
            index--;
            hit.play();
            if(index >= -1) {
                characters[Math.abs(index)].attack(characters[(index * 1) + 1]);
            }
            System.out.println(characters[0].statistics()[0]);
            System.out.println(characters[0].statistics()[1]);
            System.out.println(characters[0].statistics()[2]);
            System.out.println(characters[0].statistics()[3]);
            System.out.println(characters[0].statistics()[4]);
            System.out.println();
            System.out.println(characters[1].statistics()[1]);
        }
    }

    private void updateFrame(){
        if(animTime > 0.2f){
            animTime = 0f;
            characters[0].changeFrame(frame,2);
            characters[1].changeFrame(frame,2);

            frame = (frame+1)%9;
            if(index >= 0) {
                characters[Math.abs(index - 1)].changeFrame(frame, 1 + index + index);
            }
        }
    }

    private void leaveState(){
        this.characters[0].setPos(posX1,posY1);
        this.characters[1].setPos(posX2,posY2);
        this.getGameStateManager().pop();
    }

    public void dispose(){
        background.dispose();
        this.disposeSFX();
    }
}
