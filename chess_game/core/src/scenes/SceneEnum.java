package scenes;

import animation.CustomAnimation;
import game.Chess;
import game.chessGame.GameInfo;

public enum SceneEnum {

    MAIN_MENU {

        @Override
        public AbstractScene getScreen(Object... params) {
            return MainMenuScene.getInstance((Chess)params[0]);
        }
    },
    GAME {

        @Override
        public AbstractScene getScreen(Object... params) {
            return new GameScene((Chess)params[0], (GameInfo)params[1]);
        }
    },
    VICTORY {

        @Override
        public AbstractScene getScreen(Object... params) {
            return new VictoryScene((Chess)params[0], (GameInfo)params[1], (Boolean)params[2]);
        }
    };

    public abstract AbstractScene getScreen(Object... params);
}