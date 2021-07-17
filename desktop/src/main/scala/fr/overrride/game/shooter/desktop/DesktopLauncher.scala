package fr.overrride.game.shooter.desktop

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import fr.linkit.api.local.system.AppLogger
import fr.linkit.client.ClientApplication
import fr.linkit.client.local.config.{ClientApplicationConfigBuilder, ClientConnectionConfigBuilder}
import fr.linkit.client.local.config.schematic.ScalaClientAppSchematic
import fr.overrride.game.shooter.GameAdapter
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl

import java.net.InetSocketAddress

object DesktopLauncher {

    val WindowWidth  = 1920
    val WindowHeight = 1080
    val GameTitle    = "2DShooter"
    val ServerAddress   : InetSocketAddress = new InetSocketAddress("localhost", 48485)
    val ClientIdentifier: String            = "Client"

    def main(arg: Array[String]): Unit = {
        val config = new LwjglApplicationConfiguration
        config.width = WindowWidth
        config.height = WindowHeight
        config.title = GameTitle

        val clientConfig = new ClientApplicationConfigBuilder {
            override val resourcesFolder: String = System.getenv("LinkitHome")
            pluginFolder = None
            loadSchematic = new ScalaClientAppSchematic {
                clients += new ClientConnectionConfigBuilder {
                    override val identifier   : String            = ClientIdentifier
                    override val remoteAddress: InetSocketAddress = ServerAddress
                }
            }
        }
        val client       = ClientApplication.launch(clientConfig, getClass, classOf[GameSessionImpl], classOf[GameSession])
        val connection = client.getConnection(ClientIdentifier).get
        AppLogger.info("Linkit client Application started, Starting LwjglApplication...")
        new LwjglApplication(new GameAdapter(connection), config)
        AppLogger.info("LwjglApplication started !")
    }
}