package fr.overrride.game.server

import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle
import com.badlogic.gdx.graphics.Texture
import fr.linkit.api.connection.cache.CacheSearchBehavior
import fr.linkit.api.local.system.AppLogger
import fr.linkit.engine.connection.cache.repo.DefaultEngineObjectCenter
import fr.linkit.engine.local.utils.NumberSerializer
import fr.linkit.server.ServerApplication
import fr.linkit.server.local.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.local.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl

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
        val serverApp = ServerApplication.launch(serverConfiguration,
            classOf[GameSession],
            classOf[GameSessionImpl],
            classOf[Texture],
            classOf[LwjglFileHandle])
        val connection = serverApp.getConnection(Port).get
        val cache = connection
                .network
                .serverEngine
                .cache
                .retrieveCache(0, DefaultEngineObjectCenter[GameSession](), CacheSearchBehavior.GET_OR_OPEN)
        AppLogger.info(s"Server Application launched on port $Port.")
        cache
    }

}
