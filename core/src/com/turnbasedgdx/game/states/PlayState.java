package com.turnbasedgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turnbasedgdx.game.TurnBasedGdxGame;
import com.turnbasedgdx.game.gameplay.Button;
import com.turnbasedgdx.game.gameplay.Character;
import com.turnbasedgdx.game.gameplay.CharacterHandler;
import com.turnbasedgdx.game.map.MapHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayState extends GameState {

    private BitmapFont font;

    private Texture ui;
    private MapHandler currentMap;
    private CharacterHandler[] currentPlayer;
    private Button quitButton;
    private int inputX;
    private int inputY;
    private int playerTurn = 0;
    private int nrOfCharacters = 5;

    int frame = 0;
    float time = 0f;

    public PlayState(GameStateManager manager, String mapName) {
        super(manager);
        ui = new Texture("ui2.png");
        font = new BitmapFont();
        font.setColor(255, 0, 0, 255);

        inputX = 0;
        inputY = 0;

        this.currentMap = new MapHandler(this.getCam(), mapName);
        this.currentPlayer = new CharacterHandler[2];

        this.quitButton = new Button(new Texture("tiles.jpg"), "QUIT!",
                TurnBasedGdxGame.WIDTH / 10,
                TurnBasedGdxGame.WIDTH / 10);
        this.quitButton.setPosition(0, 0);
        this.quitButton.changeFrame(0, 0);
        this.quitButton.setAlpha(0);

        currentPlayer[0] = new CharacterHandler(0);
        currentPlayer[0].setCurrentMap(this.currentMap);
        currentPlayer[1] = new CharacterHandler(1);
        currentPlayer[1].setCurrentMap(this.currentMap);

       try {
            read(0);
            read(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; i++) {
            currentPlayer[i].spawnCharacters();
        }

    }


    @Override
    public void handleInput() {

        if(Gdx.input.justTouched()){
            playSound();
            inputX = Gdx.input.getX()/(TurnBasedGdxGame.WIDTH/10);
            inputY = Gdx.input.getY()/(TurnBasedGdxGame.WIDTH/10);

            if(quitButton.isPosition(inputX,inputY-1)){
                for(int i = 0; i < nrOfCharacters; i++){
                    currentPlayer[0].getCharacter(i).setIsSelected(false);
                    currentPlayer[0].getCharacter(i).setHasMoved(false);
                    currentPlayer[1].getCharacter(i).setIsSelected(false);
                    currentPlayer[1].getCharacter(i).setHasMoved(false);
                }

                try {
                    write(0);
                    write(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentMap.stopMusic();
                getGameStateManager().pop();
            }

            playerTurn = currentPlayer[playerTurn].getTurnState();
            int attacking = currentPlayer[(playerTurn+1)%2].findCharacter(inputX,inputY);
            System.out.println(attacking);
            int selected = currentPlayer[playerTurn].getSelected();
            System.out.println(selected);
            currentPlayer[playerTurn].handleMovement(inputX,inputY,attacking);

            if(currentPlayer[playerTurn].battleStarted()){
                getGameStateManager().push(new BattleState(getGameStateManager(),
                        currentPlayer[playerTurn].getCharacter(selected),
                        currentPlayer[(playerTurn+1)%2].getCharacter(attacking)));
            }
        }
    }

    @Override
    public void update(float dt) {
       gameOver();
        handleInput();

        time += dt;
        if(time > 0.2f){
            time = 0f;

            for(int i = 0; i < nrOfCharacters; i++){
                frame = (frame+1)%9;
                currentPlayer[0].getCharacter(i).changeFrame(frame,2);
                currentPlayer[1].getCharacter(i).changeFrame(frame,2);
            }
        }

    }

    @Override
    public void render(SpriteBatch batch) {

        batch.begin();
        batch.draw(ui, 0, 0, TurnBasedGdxGame.WIDTH, TurnBasedGdxGame.HEIGHT-TurnBasedGdxGame.WIDTH);
        quitButton.draw(batch);
        font.draw(batch,quitButton.title(),quitButton.getCenterX(),quitButton.getCenterY());

        String[] str = currentPlayer[playerTurn].getUI();
        for(int i = 0; i < nrOfCharacters; i++){
            font.draw(batch,str[i],TurnBasedGdxGame.WIDTH/2,
                    (TurnBasedGdxGame.HEIGHT-TurnBasedGdxGame.WIDTH)-(i*50)-50);
        }

        font.draw(batch,quitButton.title(),quitButton.getCenterX(),quitButton.getCenterY());
        batch.end();
        this.currentMap.draw(batch);

        batch.begin();
        for(int i = 0; i < nrOfCharacters; i++){
            currentPlayer[0].getCharacter(i).draw(batch);
            font.draw(batch,currentPlayer[0].getCharacter(i).getHealth(),
                    currentPlayer[0].getCharacter(i).getX(),
                    currentPlayer[0].getCharacter(i).getY());
            currentPlayer[1].getCharacter(i).draw(batch);
            font.draw(batch,currentPlayer[1].getCharacter(i).getHealth(),
                    currentPlayer[1].getCharacter(i).getX(),
                    currentPlayer[1].getCharacter(i).getY());
        }
        batch.end();
    }

    public void gameOver(){
        for(int i = 0; i < nrOfCharacters; i++){
            if(Integer.parseInt(currentPlayer[0].getCharacter(i).getHealth()) <= 0){
                for (int j = 0; i < nrOfCharacters; i++) {
                    currentPlayer[0].setCharacter(j, new Character(new Texture("warrior.png")));
                    currentPlayer[0].getCharacter(j).changeFrame(0, 0);

                    currentPlayer[1].setCharacter(j, new Character(new Texture("warrior.png")));
                    currentPlayer[1].getCharacter(j).changeFrame(0, 0);

                    try {
                        write(0);
                        write(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentMap.stopMusic();
                    getGameStateManager().pop();
                }
            }
            if(Integer.parseInt(currentPlayer[1].getCharacter(i).getHealth()) <= 0){
                for (int j = 0; i < nrOfCharacters; i++) {
                    currentPlayer[0].setCharacter(j, new Character(new Texture("warrior.png")));
                    currentPlayer[0].getCharacter(j).changeFrame(0, 0);

                    currentPlayer[1].setCharacter(j, new Character(new Texture("warrior.png")));
                    currentPlayer[1].getCharacter(j).changeFrame(0, 0);

                    try {
                        write(0);
                        write(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentMap.stopMusic();
                    getGameStateManager().pop();
                }
            }
        }
    }

    public void write(int index) throws IOException {
        ObjectOutputStream out = new
                ObjectOutputStream(new FileOutputStream("Character"+Integer.toString(index)+".dat"));

        for(int i = 0; i < nrOfCharacters; i++) {
            out.writeObject(currentPlayer[index].getCharacter(i));
        }

        out.close();
    }

    public void read(int index) throws IOException, ClassNotFoundException  {
        ObjectInputStream in = new
                ObjectInputStream(new FileInputStream("Character"+Integer.toString(index)+".dat"));

        for(int i = 0; i < nrOfCharacters; i++){
            currentPlayer[index].setCharacter(i,(Character) in.readObject());
            currentPlayer[index].getCharacter(i).setSize(Character.size,Character.size);
            currentPlayer[index].getCharacter(i).setTexture(new Texture("warrior.png"));
            System.out.println(currentPlayer[index].getCharacter(i).size);
        }


        in.close();
    }

    public void dispose(){
        ui.dispose();
        currentMap.dispose();
        this.disposeSFX();
    }
}
