package fr.overrride.game.shooter.desktop

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration, LwjglFileHandle}
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.local.system.AppLogger
import fr.linkit.client.ClientApplication
import fr.linkit.client.local.config.{ClientApplicationConfigBuilder, ClientConnectionConfigBuilder}
import fr.linkit.client.local.config.schematic.ScalaClientAppSchematic
import fr.linkit.engine.local.utils.NumberSerializer
import fr.overrride.game.shooter.{GameAdapter, GameConstants}
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl

import java.net.InetSocketAddress
import scala.io.StdIn

object DesktopMain {

    val WindowWidth : Int = GameConstants.WINDOW_WIDTH.toInt
    val WindowHeight: Int = GameConstants.WINDOW_HEIGHT.toInt
    val GameTitle         = "2DShooter"
    val Port = 48485
    val ServerAddress   : InetSocketAddress = new InetSocketAddress("localhost", Port)

    def main(arg: Array[String]): Unit = {
        println("Choose client identifier.")
        print(" > ")
        val clientIdentifier = StdIn.readLine()

        val config = new LwjglApplicationConfiguration
        config.width = WindowWidth
        config.height = WindowHeight
        config.title = GameTitle + s" - $clientIdentifier"

        val clientConfig = new ClientApplicationConfigBuilder {
            override val resourcesFolder: String = System.getenv("LinkitHome")
            nWorkerThreadFunction = _ + 1
            pluginFolder = None
            loadSchematic = new ScalaClientAppSchematic {
                clients += new ClientConnectionConfigBuilder {
                    override val identifier   : String            = clientIdentifier
                    override val remoteAddress: InetSocketAddress = ServerAddress
                }
            }
        }
        NumberSerializer.serializeInt(4)
        val client       = ClientApplication.launch(clientConfig, getClass,
            classOf[GameSession],
            classOf[GameSessionImpl],
            classOf[Texture],
            classOf[LwjglFileHandle],
            classOf[Color])
        val connection = client.getConnection(Port).get
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