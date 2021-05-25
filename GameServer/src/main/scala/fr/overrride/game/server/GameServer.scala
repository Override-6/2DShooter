package fr.overrride.game.server

import fr.linkit.api.local.system.AppLogger
import fr.linkit.server.ServerApplication
import fr.linkit.server.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}

object GameServer {

    val PORT: Int = 48485

    def main(args: Array[String]): Unit = {
        val serverConfiguration = new ServerApplicationConfigBuilder {
            override val resourceFolder: String = System.getProperty("LinkitHome")
            loadSchematic = new ScalaServerAppSchematic {
                servers += new ServerConnectionConfigBuilder {
                    override val identifier: String = "GameServer"
                    override val port      : Int    = PORT
                }
            }
        }
        val serverApp           = ServerApplication.launch(serverConfiguration)
        AppLogger.info(s"Server Application launched on port $PORT.")
    }

}
