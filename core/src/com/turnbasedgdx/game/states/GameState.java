package com.turnbasedgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.turnbasedgdx.game.TurnBasedGdxGame;

public abstract class GameState {

    private GameStateManager manager;
    private OrthographicCamera cam;

    private Sound touch;

    public GameState(GameStateManager manager){

        this.manager = manager;
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(true, TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.HEIGHT);
        this.touch = Gdx.audio.newSound(Gdx.files.internal("chop.ogg"));
    }

    public GameStateManager getGameStateManager() {

        return this.manager;
    }

    public OrthographicCamera getCam() {

        return this.cam;
    }

    public void playSound(){
        this.touch.play();
    }

    public void disposeSFX(){
        touch.dispose();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();
}
