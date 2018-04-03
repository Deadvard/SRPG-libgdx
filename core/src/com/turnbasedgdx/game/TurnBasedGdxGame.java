package com.turnbasedgdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.turnbasedgdx.game.gameplay.MovableSprite;
import com.turnbasedgdx.game.states.GameStateManager;
import com.turnbasedgdx.game.states.MenuState;

public class TurnBasedGdxGame extends ApplicationAdapter {

	public static final int HEIGHT = 800;
	public static final int WIDTH = 480;

    private GameStateManager manager;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		manager = new GameStateManager();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        manager.push(new MenuState(manager));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.justTouched()){
			Vector3 temp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			int x = Gdx.input.getX()/(WIDTH/10);
			int y = Gdx.input.getY()/(WIDTH/10);
		}
        manager.update(Gdx.graphics.getDeltaTime());
        manager.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
