package fr.overrride.game.server

import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.gnom.cache.CacheSearchMethod
import fr.linkit.api.internal.system.AppLogger
import fr.linkit.engine.gnom.cache.sync.DefaultSynchronizedObjectCache
import fr.linkit.engine.gnom.cache.sync.instantiation.Constructor
import fr.linkit.engine.internal.language.bhv.{Contract, ObjectsProperty}
import fr.linkit.server.ServerApplication
import fr.linkit.server.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.levels.DefaultLevel
import fr.overrride.game.shooter.session.{GameSessionImpl, PlayState}

import scala.io.StdIn

object GameServer {

    final val Port      : Int    = 48485
    final val ServerName: String = "GameServer"

    def main(args: Array[String]): Unit = {
        val serverConfiguration = new ServerApplicationConfigBuilder {
            override val resourcesFolder: String = System.getenv("LinkitHome")

            pluginFolder = None
            loadSchematic = new ScalaServerAppSchematic {
                servers += new ServerConnectionConfigBuilder {
                    override val identifier: String = ServerName
                    override val port      : Int    = Port
                    defaultPersistenceConfigScript = Some(getClass.getResource("/libgdx_persistence_config_server.sc"))
                }
            }
        }
        val serverApp           = ServerApplication.launch(serverConfiguration,
            classOf[GameSession],
            classOf[GameSessionImpl],
            classOf[Texture],
            classOf[LwjglFileHandle],
            classOf[Color])
        val connection          = serverApp.findConnection(Port).get
        val traffic             = connection.traffic
        val network             = connection.network
        val global              = network.globalCache
        val contracts           = Contract(classOf[GameSession].getResource("/network/NetworkContract.bhv"))(connection.getApp, ObjectsProperty.default(network))
        val cache               = global.attachToCache(51, DefaultSynchronizedObjectCache[GameSession](contracts), CacheSearchMethod.GET_OR_OPEN)

        val gameSession = cache
                .syncObject(0, Constructor[GameSessionImpl](3, new DefaultLevel, new ServerSideParticleManager))
        val col         = traffic.defaultPersistenceConfig.contextualObjectLinker
        col += (700, gameSession.getParticleManager)
        global.getCacheTrafficNode(51).preferPerformances()
        AppLogger.info(s"Server Application launched on port $Port.")

        /*val config = new LwjglApplicationConfiguration
        config.width = 100
        config.height = 15
        config.x = 0
        config.y = 0
        config.title = "GameServer"
        config.foregroundFPS = -1
        config.backgroundFPS = -1
        new LwjglApplication(new DumbApplicationAdapter, config)*/

        while (true) {
            val line = StdIn.readLine()
            //val t = GLContext.getCapabilities
            if (line == "stop")
                return
        }
    }

}
