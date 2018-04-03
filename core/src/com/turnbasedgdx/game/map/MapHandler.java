package com.turnbasedgdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

 /**
 * Created by Deadvard on 2017-02-25.
 */

public class MapHandler {

    private OrthographicCamera camera;
    private TiledMap map;
    private TiledMapTileLayer layer;
    private OrthogonalTiledMapRenderer renderer;
    private Tile[][] tiles;
    private Sound music;
     private final int mapSize = 10;

    public MapHandler(OrthographicCamera camera, String mapName){
        this.camera = camera;
        map = new TmxMapLoader().load(mapName + ".tmx");
        layer = (TiledMapTileLayer)map.getLayers().get(0);
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(this.camera);

        Object musicTrack = map.getProperties().get("music");
        if(musicTrack instanceof String) {
            this.music = Gdx.audio.newSound(Gdx.files.internal(((String) musicTrack)));
        }
        this.music.loop();

        this.createTiles();
    }
    public void draw(SpriteBatch batch){
        this.renderer.render();
        batch.begin();
        for(int i =  0; i < mapSize; i++){
            for(int j = 0; j < mapSize; j++){
                tiles[i][j].draw(batch);
            }
        }
        batch.end();
    }

    public void stopMusic(){
        music.stop();
        music.dispose();
    }

    public void setTileState(int x, int y, int color){

        tiles[x][y].setAlpha(50);

        if(!tiles[x][y].getBlocked() && color == 0) {
            tiles[x][y].changeFrame(color, 0);
            tiles[x][y].setCanMoveTo(false);
        }else if(!tiles[x][y].getBlocked() && color == 1) {
            tiles[x][y].changeFrame(color, 0);
            tiles[x][y].setCanMoveTo(true);
        }else{
            tiles[x][y].changeFrame(2, 0);
            tiles[x][y].setCanMoveTo(false);
        }
    }
    public void clearTileState(){
        for(int i =  0; i < mapSize; i++){
            for(int j = 0; j < mapSize; j++){
                tiles[i][j].changeFrame(0, 0);
                tiles[i][j].setAlpha(0);
                tiles[i][j].setCanMoveTo(false);
            }
        }

    }

    public void setSelectedTiles(int x, int y, int moveDist){
        this.setTileState(x,y,1);

        if(moveDist > 0){
            moveDist--;

            if(this.isOnMap(x-1,y) && !tiles[x-1][y].getBlocked()){
                this.setSelectedTiles(x-1, y, moveDist);
            }
            if(this.isOnMap(x,y-1) && !tiles[x][y-1].getBlocked()){
                this.setSelectedTiles(x, y-1, moveDist);
            }
            if(this.isOnMap(x+1,y) && !tiles[x+1][y].getBlocked()){
                this.setSelectedTiles(x+1, y, moveDist);
            }
            if(this.isOnMap(x,y+1) && !tiles[x][y+1].getBlocked()){
                this.setSelectedTiles(x, y+1, moveDist);
            }
        }
    }

    public Vector2 setAttackableTiles(int x, int y, Vector2 result, int attackDist){

        if(attackDist > 0){
            attackDist--;

        }
        if(this.isOnMap(x-1,y) && attackDist > 0){
            result = this.setAttackableTiles(x-1, y, result, attackDist);
        }
        if(this.isOnMap(x,y-1) && attackDist > 0){
            result = this.setAttackableTiles(x, y-1,  result,attackDist);
        }
        if(this.isOnMap(x+1,y) && attackDist > 0){
            result = this.setAttackableTiles(x+1, y,  result,attackDist);
        }
        if(this.isOnMap(x,y+1) && attackDist > 0){
            result = this.setAttackableTiles(x, y+1,  result, attackDist);
        }
        if(this.isOnMap(x,y) && this.canMoveTo(x,y)){
            result = new Vector2(x,y);
        }

        return result;
    }

    public void setBlockedTile(int x, int y){
        tiles[x][y].setBlocked(true);
    }

    public void setUnblockedTile(int x, int y){
        tiles[x][y].setBlocked(false);
    }

    public boolean canMoveTo(int x, int y){
        return tiles[x][y].getCanMoveTo();
    }

    public boolean isOnMap(int x, int y){
        return x >= 0 && x < mapSize && y >= 0 && y < mapSize;
    }

    public int[][] getPlayerSpawn(int playerNr){
        int arr[][] = new int[5][2];
        int x = 0;

        for(int i =  0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(playerNr == 0 && tiles[i][j].getSpawnpoint1()){
                    arr[x][0] = tiles[i][j].xCord();
                    arr[x][1] = tiles[i][j].yCord();
                    x++;
                }else if(playerNr == 1 && tiles[i][j].getSpawnpoint2()){
                    arr[x][0] = tiles[i][j].xCord();
                    arr[x][1] = tiles[i][j].yCord();
                    x++;
                }

            }
        }

        return arr;
    }

    public void dispose(){
        map.dispose();
        renderer.dispose();
        music.dispose();
    }

    private void createTiles(){
        Texture tileTexture = new Texture("tiles.jpg");
        tiles = new Tile[mapSize][mapSize];
        for(int i =  0; i < mapSize; i++){
            for(int j = 0; j < mapSize; j++){
                tiles[i][j] = new Tile(tileTexture);
                tiles[i][j].setBlocked(layer.getCell(i,j).getTile().getProperties().containsKey("blocked"));
                tiles[i][j].setSpawnpoint1(layer.getCell(i,j).getTile().getProperties().containsKey("spawnpoint1"));
                tiles[i][j].setSpawnpoint2(layer.getCell(i,j).getTile().getProperties().containsKey("spawnpoint2"));
                tiles[i][j].setPos(i,j);
                tiles[i][j].changeFrame(0,0);
                tiles[i][j].setAlpha(0);
            }
        }
    }
}
