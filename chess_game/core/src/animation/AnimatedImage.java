package animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image
{
    protected Animation<TextureRegion> animation;
    private float stateTime = 0;

    public AnimatedImage(Animation<TextureRegion> ani) {
        super(ani.getKeyFrame(0));
        animation = ani;
    }

    @Override
    public void act(float delta)
    {
    	stateTime += Gdx.graphics.getDeltaTime();
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime, true));
        super.act(delta);
    }
}