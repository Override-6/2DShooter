package fr.overrride.game.shooter.desktop

import java.net.InetSocketAddress
import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import fr.linkit.client.ClientApplication
import fr.linkit.client.config.{ClientApplicationConfigBuilder, ClientConnectionConfigBuilder}
import fr.overrride.game.shooter.GameAdapter
import fr.overrride.game.shooter.session.GameSessionImpl

object DesktopLauncher {
    val WINDOW_WIDTH                     = 1920
    val WINDOW_HEIGHT                    = 1080
    val GAME_TITLE                       = "2DShooter"
    val ServerAddress: InetSocketAddress = new InetSocketAddress("localhost", 48485)

    def main(arg: Array[String]): Unit = {
        val config = new LwjglApplicationConfiguration
        config.width = WINDOW_WIDTH
        config.height = WINDOW_HEIGHT
        config.title = GAME_TITLE

        val clientConfig         = new ClientApplicationConfigBuilder {
            override val resourcesFolder: String = System.getProperty("user.home") + "/Linkit/Home/"
        }
        val client               = ClientApplication.launch(clientConfig, getClass, classOf[GameSessionImpl])
        val gameServerConnection = new ClientConnectionConfigBuilder {
            override val identifier   : String            = "GameServer"
            override val remoteAddress: InetSocketAddress = ServerAddress
        }
        val serverConnection     = client.openConnection(gameServerConnection)
        new LwjglApplication(new GameAdapter(serverConnection), config)
    }
}