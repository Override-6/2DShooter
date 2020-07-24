package fr.overrride.game.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.override.game.shooter.Main;

public class DesktopLauncher {

    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;
    public static final String GAME_TITLE = "2DShooter";


    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WINDOW_WIDTH;
        config.height = WINDOW_HEIGHT;
        config.title = GAME_TITLE;

        new LwjglApplication(new Main(), config);
    }

}