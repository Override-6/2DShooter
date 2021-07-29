package fr.overrride.test

import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.connection.packet.DedicatedPacketCoordinates
import fr.linkit.engine.connection.packet.persistence.DefaultPacketSerializer
import fr.linkit.engine.local.LinkitApplication
import fr.linkit.engine.local.system.fsa.LocalFileSystemAdapters
import fr.linkit.engine.local.utils.ScalaUtils
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.Controllable
import fr.overrride.game.shooter.session.GameSessionImpl
import fr.overrride.game.shooter.session.character.ShooterCharacter
import fr.overrride.test.LinkitPacketTests.testPacket
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}

import java.nio.ByteBuffer
import java.util

@TestInstance(Lifecycle.PER_CLASS)
class LinkitPacketTests {
    @BeforeAll
    def makeMapping(): Unit = {
        LinkitApplication.mapEnvironment(LocalFileSystemAdapters.Nio, Seq(getClass, classOf[GameSession], classOf[GameSessionImpl], classOf[Texture]))
    }

    @Test
    def testDefaultLevelObject(): Unit = {
        val packet = new util.HashSet[Controllable[_]]()
        testPacket(Array(packet))
    }

}

object LinkitPacketTests {

    val serializer = new DefaultPacketSerializer

    def testPacket(obj: Array[AnyRef]): Unit = {
        println(s"Serializing packets ${obj.mkString("Array(", ", ", ")")}...")
        val buff = ByteBuffer.allocate(1000)
        serializer.serializePacket(obj, DedicatedPacketCoordinates(78, "SALAM", "SALAM"), buff, true)
        val bytes = buff.array().take(buff.position())
        buff.position(0)
        println(s"bytes = ${ScalaUtils.toPresentableString(bytes)} (size: ${bytes.length})")
        val deserial = serializer.deserializePacket(buff)
        println(s"deserialized coords = ${deserial.getCoordinates}")
        deserial.forEachObjects(packet2 => {
            println(s"deserialized packet = ${packet2}")
        })
    }
}
