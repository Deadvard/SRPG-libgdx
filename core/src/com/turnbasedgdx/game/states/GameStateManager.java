package com.turnbasedgdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<GameState> manager;

    public GameStateManager()
    {
        this.manager = new Stack<GameState>();
    }

    public void push(GameState state){

        this.manager.push(state);
    }

    public void pop(){

        this.manager.pop().dispose();
    }

    public void update(float dt){

        this.manager.peek().update(dt);
    }

    public void render(SpriteBatch sb){

        this.manager.peek().render(sb);
    }

}
