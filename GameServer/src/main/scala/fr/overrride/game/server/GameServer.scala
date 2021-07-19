package fr.overrride.game.server

import fr.linkit.api.local.system.AppLogger
import fr.linkit.engine.local.utils.NumberSerializer
import fr.linkit.server.ServerApplication
import fr.linkit.server.local.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.local.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}

object GameServer {

    val PORT: Int = 48485

    def main(args: Array[String]): Unit = {
        NumberSerializer.serializeInt(152)
        val serverConfiguration = new ServerApplicationConfigBuilder {
            override val resourceFolder: String = System.getenv("LinkitHome")

            pluginFolder = None
            loadSchematic = new ScalaServerAppSchematic {
                servers += new ServerConnectionConfigBuilder {
                    override val identifier: String = "GameServer"
                    override val port      : Int    = PORT
                }
            }
        }
        ServerApplication.launch(serverConfiguration)
        AppLogger.info(s"Server Application launched on port $PORT.")
    }

}
