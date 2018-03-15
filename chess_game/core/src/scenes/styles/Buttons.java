package scenes.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Buttons {

    public static void blueButton(Skin skin) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.newDrawable("background", Color.GRAY);
        style.down = skin.newDrawable("background", Color.DARK_GRAY);
        style.checked = skin.newDrawable("background", Color.DARK_GRAY);
        style.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        style.font = skin.getFont("default");
        skin.add("default", style);
    }

    public static Texture createPixmapRoundCornerRect(Color color, int width, int height, int radius) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius, radius, radius);
        pixmap.fillCircle(width - radius, height - radius, radius);
        pixmap.fillCircle(radius, height - radius, radius);
        pixmap.fillRectangle(0, radius, width, height - (radius * 2));
        pixmap.fillRectangle(radius, 0, width - (radius * 2), height);
        Texture pixmaptex = new Texture(pixmap);
        pixmap.dispose();
        return pixmaptex;
    }
}
