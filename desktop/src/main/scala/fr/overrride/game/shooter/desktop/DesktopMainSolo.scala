package fr.overrride.game.shooter.desktop

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import fr.overrride.game.shooter.GameAdapter
import fr.overrride.game.shooter.desktop.DesktopMain.{GameTitle, WindowHeight, WindowWidth}

object DesktopMainSolo {

    def main(args: Array[String]): Unit = {
        val config = new LwjglApplicationConfiguration
        config.width = WindowWidth
        config.height = WindowHeight
        config.title = GameTitle
        new LwjglApplication(new GameAdapter(null), config)

    }
}
