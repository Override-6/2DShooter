package fr.overrride.game.shooter.desktop

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration, LwjglFileHandle}
import com.badlogic.gdx.graphics.{Color, Texture}

import fr.linkit.api.internal.system.AppLogger
import fr.linkit.client.ClientApplication
import fr.linkit.client.config.schematic.ScalaClientAppSchematic
import fr.linkit.client.config.{ClientApplicationConfigBuilder, ClientConnectionConfigBuilder}
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl
import fr.overrride.game.shooter.{GameAdapter, GameConstants}

import java.net.InetSocketAddress
import scala.io.StdIn

object DesktopMain {

    final val WindowWidth  : Int               = GameConstants.WINDOW_WIDTH.toInt
    final val WindowHeight : Int               = GameConstants.WINDOW_HEIGHT.toInt
    final val GameTitle                        = "2DShooter"
    final val Port                             = 48485
    final val ServerAddress: InetSocketAddress = new InetSocketAddress("localhost", Port)

    def main(arg: Array[String]): Unit = {

        println("Choose client identifier.")
        print(" > ")
        val clientIdentifier = StdIn.readLine()


        val config = new LwjglApplicationConfiguration
        config.width = WindowWidth
        config.height = WindowHeight
        config.foregroundFPS = 60
        config.title = GameTitle + s" - $clientIdentifier"

        val clientConfig = new ClientApplicationConfigBuilder {
            override val resourcesFolder: String = System.getenv("LinkitHome")
            nWorkerThreadFunction = _ + 1
            pluginFolder = None
            loadSchematic = new ScalaClientAppSchematic {
                clients += new ClientConnectionConfigBuilder {
                    override val identifier   : String            = clientIdentifier
                    override val remoteAddress: InetSocketAddress = ServerAddress
                    defaultPersistenceConfigScript = Some(getClass.getResource("/libgdx_persistence_config_client.sc"))
                }
            }
        }
        val client       = ClientApplication.launch(clientConfig, getClass,
            classOf[GameSession],
            classOf[GameSessionImpl],
            classOf[Texture],
            classOf[LwjglFileHandle],
            classOf[Color])
        val connection   = client.findConnection(Port).get
        AppLogger.info("Linkit client Application started, Starting LwjglApplication...")
        new LwjglApplication(new GameAdapter(connection), config)
        AppLogger.info("LwjglApplication started !")

        while (true) {
            val line = StdIn.readLine()
            if (line == "stop")
                return
        }
    }
}