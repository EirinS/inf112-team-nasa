package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import java.util.HashMap;

public class PieceSpriteLoader {

    // String to represent the pieces
    private final static String[] pieces = {
            "wp", "wr", "wb", "wn", "wq", "wk",
            "bp", "br", "bb", "bn", "bq", "bk"
    };

    public static HashMap<String, Texture> loadDefaultPieces() {
        return loadPieces("default");
    }

    private static HashMap<String, Texture> loadPieces(String dir) {
        HashMap<String, Texture> sprites = new HashMap<>();
        String path = "pieces/" + dir + "/";
        for (String p : pieces) {
            String fullPath = path + p + ".png";
            Texture texture = new Texture(Gdx.files.internal(fullPath), true);
            texture.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
            sprites.put(p, texture);
        }
        return sprites;
    }
}
