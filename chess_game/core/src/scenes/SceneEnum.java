package scenes;

import game.Chess;

public enum SceneEnum {

    MAIN_MENU {

        @Override
        public AbstractScene getScreen(Object... params) {
            return new MainMenuScene((Chess)params[0]);
        }
    },
    GAME {

        @Override
        public AbstractScene getScreen(Object... params) {
            return new GameScene((Chess)params[0]);
        }
    };

    public abstract AbstractScene getScreen(Object... params);
}