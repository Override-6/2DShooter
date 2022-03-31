package fr.linkit.tests.performance

import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.application.connection.ExternalConnection
import fr.linkit.api.gnom.cache.CacheSearchMethod
import fr.linkit.api.gnom.packet.Packet
import fr.linkit.api.internal.system.AppLogger
import fr.linkit.client.ClientApplication
import fr.linkit.client.config.schematic.ScalaClientAppSchematic
import fr.linkit.client.config.{ClientApplicationConfigBuilder, ClientConnectionConfigBuilder}
import fr.linkit.engine.gnom.cache.sync.DefaultSynchronizedObjectCache
import fr.linkit.engine.gnom.cache.sync.instantiation.Constructor
import fr.linkit.engine.gnom.packet.fundamental.EmptyPacket
import fr.linkit.engine.gnom.packet.fundamental.RefPacket.ObjectPacket
import fr.linkit.engine.gnom.packet.traffic.channel.SyncAsyncPacketChannel
import fr.linkit.engine.gnom.packet.traffic.{ChannelScopes, DefaultChannelPacketBundle}
import fr.linkit.engine.internal.language.bhv.{Contract, ObjectsProperty}
import fr.linkit.server.ServerApplication
import fr.linkit.server.config.schematic.ScalaServerAppSchematic
import fr.linkit.server.config.{ServerApplicationConfigBuilder, ServerConnectionConfigBuilder}
import fr.linkit.server.connection.ServerConnection
import fr.overrride.game.server.ServerSideParticleManager
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.overrride.game.shooter.session.GameSessionImpl
import fr.overrride.game.shooter.session.PlayState.lwjglProcrastinator
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel
import org.junit.jupiter.api.Test

import java.net.InetSocketAddress
import java.util.concurrent.ThreadLocalRandom

class PacketPerformanceTests {

    AppLogger.printTraffic = true

    @Test
    def server(): Unit = {
        lwjglProcrastinator.hireCurrentThread()
        val serverConnection = PacketPerformanceTests.serverConnection()
        serverConnection.runLater {
            val channel = serverConnection.traffic.getInjectable[SyncAsyncPacketChannel](101, SyncAsyncPacketChannel, ChannelScopes.discardCurrent)
            println("waiting for client...")
            channel.nextSync[Packet]

            val network    = serverConnection.network
            val global     = network.globalCache
            val traffic    = serverConnection.traffic
            val properties = ObjectsProperty.default(network) + ObjectsProperty(Map("lwjgl" -> lwjglProcrastinator))
            val contracts  = Contract(classOf[GameSession].getResource("/network/NetworkContract.bhv"))(serverConnection.getApp, properties)
            val cache      = global.attachToCache(51, DefaultSynchronizedObjectCache[GameSession](contracts), CacheSearchMethod.GET_OR_OPEN)

            val particleManager = new ServerSideParticleManager
            val col             = traffic.defaultPersistenceConfig.contextualObjectLinker
            col += (700, particleManager)
            col += (701, lwjglProcrastinator)
            val session = cache
                .syncObject(0, Constructor[GameSessionImpl](3, new DefaultLevel, particleManager))
            createPlayer(session)

            var count = 0
            println("Client connected!")
            channel.addAsyncListener { case DefaultChannelPacketBundle(_, packet, attributes, _) =>
                count += 1
                println(s"count: $count")
                if (count != 500)
                    channel.sendAsync(packet, attributes)
            }
            channel.sendAsync(ObjectPacket(session))
        }
        lwjglProcrastinator.pauseCurrentTask()
    }

    private def createPlayer(session: GameSession): Unit = {
        val playerCount = session.countPlayers()
        val player1     = new ShooterCharacter(500 + (playerCount * 100), 75, Color.GREEN)
        val controller  = new CharacterController(player1)
        controller.addKeyControl(KeyControl.of(KeyType.DASH, A, _.dash()))
        controller.addKeyControl(KeyControl.of(KeyType.JUMP, SPACE, _.jump()))
        controller.addKeyControl(KeyControl.of(KeyType.LEFT, Q, _.left()))
        controller.addKeyControl(KeyControl.of(KeyType.RIGHT, D, _.right()))
        controller.addKeyControl(KeyControl.of(KeyType.SHOOT, E, _.shoot()))
        player1.setController(controller)
        player1.setGameSession(session)
        session.addCharacter(player1)
    }

    @Test
    def client(): Unit = {
        val clientConnection = PacketPerformanceTests.clientConnection()
        lwjglProcrastinator.hireCurrentThread()
        val channel = clientConnection.traffic.getInjectable[SyncAsyncPacketChannel](101, SyncAsyncPacketChannel, ChannelScopes.discardCurrent)
        val network = clientConnection.network
        val global  = network.globalCache

        val traffic = clientConnection.traffic
        val col     = traffic.defaultPersistenceConfig.contextualObjectLinker
        col += (700, new ServerSideParticleManager)
        col += (701, lwjglProcrastinator)

        val properties = ObjectsProperty.default(network) + ObjectsProperty(Map("lwjgl" -> lwjglProcrastinator))
        val contracts  = Contract(classOf[GameSession].getResource("/network/NetworkContract.bhv"))(clientConnection.getApp, properties)
        val cache      = global.attachToCache(51, DefaultSynchronizedObjectCache[GameSession](contracts), CacheSearchMethod.GET_OR_OPEN)


        clientConnection.runLater {
            cache
            channel.sendSync(EmptyPacket)
            var count = 0
            channel.addAsyncListener { case DefaultChannelPacketBundle(_, packet, attributes, _) =>
                count += 1
                println(s"count: $count")
                if (count != 500)
                    channel.sendAsync(packet, attributes)
            }
        }
        lwjglProcrastinator.pauseCurrentTask()
    }

}

object PacketPerformanceTests {

    private final val ServerName = "ServerTest"
    private final val Port       = 48480

    def serverConnection(): ServerConnection = {
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
        serverApp.findConnection(Port).get
    }

    def clientConnection(): ExternalConnection = {
        val clientIdentifier = ThreadLocalRandom.current().nextInt.toString
        println(s"This client identifier is $clientIdentifier")

        val clientConfig = new ClientApplicationConfigBuilder {
            override val resourcesFolder: String = System.getenv("LinkitHome")
            nWorkerThreadFunction = _ + 1
            pluginFolder = None
            loadSchematic = new ScalaClientAppSchematic {
                clients += new ClientConnectionConfigBuilder {
                    override val identifier   : String            = clientIdentifier
                    override val remoteAddress: InetSocketAddress = new InetSocketAddress("localhost", Port)
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
        client.findConnection(Port).get
    }
}
