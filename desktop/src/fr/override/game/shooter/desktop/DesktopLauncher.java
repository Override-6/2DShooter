package fr.override.game.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.override.game.shooter.Main;

import static fr.override.game.shooter.util.GameConstants.*;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) WINDOW_WIDTH;
        config.height = (int) WINDOW_HEIGHT;
        config.title = GAME_TITLE;

      //  Box2D.init();

        new LwjglApplication(new Main(), config);
    }

}