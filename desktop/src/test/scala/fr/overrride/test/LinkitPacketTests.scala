package fr.overrride.test

import com.badlogic.gdx.graphics.Texture
import fr.linkit.engine.connection.packet.fundamental.RefPacket
import fr.linkit.engine.connection.packet.fundamental.RefPacket.AnyRefPacket
import fr.linkit.engine.connection.packet.persistence.DefaultSerializer
import fr.linkit.engine.local.LinkitApplication
import fr.linkit.engine.local.system.fsa.LocalFileSystemAdapters
import fr.linkit.engine.local.utils.ScalaUtils
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.session.GameSessionImpl
import fr.overrride.game.shooter.session.levels.DefaultLevel
import fr.overrride.test.LinkitPacketTests.testPacket
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}

@TestInstance(Lifecycle.PER_CLASS)
class LinkitPacketTests {
    @BeforeAll
    def makeMapping(): Unit = {
        LinkitApplication.mapEnvironment(LocalFileSystemAdapters.Nio, Seq(getClass, classOf[GameSession], classOf[GameSessionImpl], classOf[Texture]))
    }

    @Test
    def testDefaultLevelObject(): Unit = {
        val packet = new GameSessionImpl(3, new DefaultLevel)
        testPacket(packet)
    }

}

object LinkitPacketTests {

    val serializer = new DefaultSerializer

    def testPacket(obj: AnyRef): Unit = {
        val packet = RefPacket(obj)
        println(s"Serializing packet $packet...")
        val bytes = serializer.serialize(packet, true)
        println(s"bytes = ${ScalaUtils.toPresentableString(bytes)} (size: ${bytes.length})")
        val packet2 = serializer.deserialize(bytes)
        println(s"deserialized packet = $packet2")
    }
}
