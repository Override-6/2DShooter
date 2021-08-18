package fr.overrride.game.server

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration, LwjglFileHandle}
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.connection.cache.CacheSearchBehavior
import fr.linkit.api.local.system.AppLogger
import fr.linkit.engine.connection.cache.obj.DefaultSynchronizedObjectCenter
import fr.linkit.engine.connection.packet.persistence.DefaultPacketSerializer
import fr.linkit.engine.local.utils.NumberSerializer
import fr.linkit.server.ServerApplication
import fr.linkit.server.local.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.local.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl

import scala.io.StdIn

object GameServer {

    val Port: Int = 48485

    def main(args: Array[String]): Unit = {
        NumberSerializer.serializeInt(152)
        val serverConfiguration = new ServerApplicationConfigBuilder {
            override val resourceFolder: String = System.getenv("LinkitHome")

            pluginFolder = None
            loadSchematic = new ScalaServerAppSchematic {
                servers += new ServerConnectionConfigBuilder {
                    override val identifier: String = "GameServer"
                    override val port      : Int    = Port
                }
            }
        }
        val serverApp           = ServerApplication.launch(serverConfiguration,
            classOf[GameSession],
            classOf[GameSessionImpl],
            classOf[Texture],
            classOf[LwjglFileHandle],
            classOf[Color])
        val connection          = serverApp.getConnection(Port).get
        val serializer          = connection.translator.getSerializer.asInstanceOf[DefaultPacketSerializer]
        val cache               = connection
            .network
            .globalCache
            .attachToCache(51, DefaultSynchronizedObjectCenter[GameSession](), CacheSearchBehavior.GET_OR_OPEN)
        AppLogger.info(s"Server Application launched on port $Port.")

        val config = new LwjglApplicationConfiguration
        config.width = 100
        config.height = 15
        config.x = 0
        config.y = 0
        config.title = "GameServer"
        new LwjglApplication(new DumbApplicationAdapter, config)


        while (true) {
            val line = StdIn.readLine()
            if (line == "stop")
                return
        }
    }

}
