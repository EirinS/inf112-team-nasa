package gui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import scenes.styling.Colors;

public class Checkerboard {

    public static TiledMap getCheckerboard() {
        Pixmap pixmap = new Pixmap(64, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); // add your 1 color here
        pixmap.fillRectangle(0, 0, 32, 32);

        pixmap.setColor(Colors.darkColor); // add your 2 color here
        pixmap.fillRectangle(32, 0, 32, 32);
        // the outcome is an texture with an blue left square and an red right
        // square
        Texture t = new Texture(pixmap);
        TextureRegion reg1 = new TextureRegion(t, 0, 0, 32, 32);
        TextureRegion reg2 = new TextureRegion(t, 32, 0, 32, 32);

        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        for (int l = 0; l < 20; l++) {
            TiledMapTileLayer layer = new TiledMapTileLayer(150, 100, 32, 32);
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 100; y++) {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    if (y % 2 != 0) {
                        if (x % 2 != 0) {
                            cell.setTile(new StaticTiledMapTile(reg1));
                        } else {
                            cell.setTile(new StaticTiledMapTile(reg2));
                        }
                    } else {
                        if (x % 2 != 0) {
                            cell.setTile(new StaticTiledMapTile(reg2));
                        } else {
                            cell.setTile(new StaticTiledMapTile(reg1));
                        }
                    }
                    layer.setCell(x, y, cell);
                }
            }
            layers.add(layer);
        }
        return map;
    }

}