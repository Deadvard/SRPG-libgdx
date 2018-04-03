package com.turnbasedgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turnbasedgdx.game.TurnBasedGdxGame;
import com.turnbasedgdx.game.gameplay.Button;

public class MenuState extends GameState {

    private Texture background;
    private Button buttons[];
    private BitmapFont font = new BitmapFont();
    private int inputX = 0;
    private int inputY = 0;
    private boolean stop = false;

    public MenuState(GameStateManager manager){
       super(manager);
        background = new Texture("bg.png");
        buttons = new Button[5];

        buttons[0] = new Button(new Texture("tiles.jpg"),"level1",
                TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.WIDTH/8);

        buttons[1] = new Button(new Texture("tiles.jpg"),"level2",
                TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.WIDTH/8);

        buttons[2] = new Button(new Texture("tiles.jpg"),"level3",
                TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.WIDTH/8);

        buttons[3] = new Button(new Texture("tiles.jpg"),"level4",
                TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.WIDTH/8);

        buttons[4] = new Button(new Texture("tiles.jpg"),"level5",
                TurnBasedGdxGame.WIDTH,TurnBasedGdxGame.WIDTH/8);

        for(int i = 1; i < 6; i++){
            buttons[i-1].setPosition(0,(6-i)*TurnBasedGdxGame.WIDTH/4);

        }
   }


    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            playSound();
            inputX = Gdx.input.getX();
            inputY = Gdx.input.getY();
            for(int i = 0; i < 5; i++){
                if(buttons[i].contains(inputX, inputY) && !stop){
                    stop = true;
                    getGameStateManager().push(new PlayState(getGameStateManager(),buttons[i].title()));
                }
            }
            stop = false;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, 0, TurnBasedGdxGame.WIDTH, TurnBasedGdxGame.HEIGHT);
        for(int i = 0; i < 5; i++){
            buttons[i].draw(batch);
            font.draw(batch,buttons[i].title(),buttons[i].getCenterX(),buttons[i].getCenterY());
        }
        batch.end();
    }

    public void dispose(){
        background.dispose();
        this.disposeSFX();
    }
}
