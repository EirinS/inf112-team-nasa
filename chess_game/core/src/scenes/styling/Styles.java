package scenes.styling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Styles {

    public static void myriadProFont(Skin skin) {
        /*
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Myriad Pro Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;

        parameter.size = 30;
        BitmapFont font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("white-30", font);

        parameter.size = 24;
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("white-24", font);

        parameter.color = Colors.darkColor;
        parameter.size = 24;
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("dark-24", font);

        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        */
    }

    public static void whiteFont(Skin skin) {
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        skin.add("default", font);
    }

    public static void blueButton(Skin skin) {
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.newDrawable("background", Colors.blueColor);
        style.down = skin.newDrawable("background", Colors.bluePressedColor);
        style.over = skin.newDrawable("background", Colors.blueColor);
        style.font = skin.getFont("white-30");
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
