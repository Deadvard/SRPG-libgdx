package com.turnbasedgdx.game.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.turnbasedgdx.game.map.MapHandler;

/**
 * Created by Deadvard on 2017-02-26.
 */

public class CharacterHandler{

    private Character[] characters;
    private MapHandler currentMap;
    private int playerNr;
    private int cha = -1;
    private final int nrOfCharacters = 5;
    private int characterMoves = nrOfCharacters;
    private boolean startBattle;

    public CharacterHandler(int playerNr){

        this.characters = new Character[nrOfCharacters];
        this.playerNr = playerNr;
    }

    public Character getCharacter(int index){
        return characters[index];
    }

    public void setCharacter(int index, Character character){
        this.characters[index] = character;
    }

    public void setCurrentMap(MapHandler currentMap){
        this.currentMap = currentMap;
    }

    public boolean battleStarted(){
        boolean result = false;

        if(startBattle){
            startBattle = false;
            result = true;
        }

        return result;
    }

    public void handleMovement(int x, int y, int index){

        if((cha != -1) && !this.characters[cha].getHasMoved() && this.characters[cha].getIsSelected()
                && currentMap.isOnMap(x, y)){

            if(currentMap.canMoveTo(x, y)){
                this.move(x,y);
            }else if(index != -1){
                Vector2 temp = new Vector2(0,0);
                temp = currentMap.setAttackableTiles(x,y,temp,
                        this.characters[cha].getAttackDistance()+1);
                if(currentMap.canMoveTo((int)temp.x,(int)temp.y)){
                    startBattle = true;
                    this.move((int)temp.x,(int)temp.y);
                }
            }else {
                currentMap.clearTileState();
                this.characters[cha].setIsSelected(false);
                cha = -1;
            }

        }else if(cha == -1){
            this.findSelectedCharacter(x,y);
        } else{
            currentMap.clearTileState();
            this.characters[cha].setIsSelected(false);
            cha = -1;
        }

    }

    public void spawnCharacters(){
        int arr[][] = currentMap.getPlayerSpawn(playerNr);
        int x = 0;
        for(int j = 0; j < nrOfCharacters; j++)
        {
            this.characters[j].setPos(arr[x][0],arr[x][1]);
            x++;
        }
    }

    public void resetMoves(){
        for(int i = 0; i < nrOfCharacters; i++){
            this.characters[i].setHasMoved(false);
        }
        this.characterMoves = nrOfCharacters;
    }

    public int getTurnState(){
        int result = playerNr;

        if(characterMoves <= 0) {
            result = (playerNr+1)%2;
            resetMoves();
        }
        return result;

    }

    public int getSelected(){
        return cha;
    }

    public int findCharacter(int x, int y){
        int result = -1;

        for(int i = 0; i < nrOfCharacters; i++){
            if(this.characters[i].isPosition(x,y)){
                result = i;
            }
        }
        return result;
    }

    public String[] getUI(){
        String[] str = new String[]{"","","","",""};

        if(this.cha != -1){
            str = characters[cha].statistics();
        }
        return str;
    }

    private void findSelectedCharacter(int x, int y){
        for(int i = 0; i < nrOfCharacters; i++){
            if(this.characters[i].isPosition(x,y) && !this.characters[i].getHasMoved()){
                cha = i;
                this.characters[cha].setIsSelected(true);
                currentMap.setSelectedTiles(x,y,this.characters[cha].getMoveDistance());
            }
        }
    }

    private void move(int x, int y){
        currentMap.setUnblockedTile(this.characters[cha].xCord(),this.characters[cha].yCord());
        this.characters[cha].setPos(x,y);
        this.characters[cha].setHasMoved(true);
        this.characters[cha].setIsSelected(false);
        currentMap.clearTileState();
        currentMap.setBlockedTile(x,y);
        cha = -1;
        characterMoves--;
    }
}
