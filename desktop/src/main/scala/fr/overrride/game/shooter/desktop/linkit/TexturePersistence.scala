package fr.overrride.game.shooter.desktop.linkit

import com.badlogic.gdx.graphics.Texture
import fr.linkit.api.connection.packet.persistence.v3.HandledClass
import fr.linkit.engine.connection.packet.persistence.v3.deserialisation.UnexpectedObjectException
import fr.linkit.engine.connection.packet.persistence.v3.persistor.DefaultObjectPersistor

class TexturePersistence extends DefaultObjectPersistor[Texture] {

    override val handledClasses: Seq[HandledClass] = Seq(HandledClass(classOf[Texture], true))

    override def useSortedDeserializedObjects: Boolean = true

    override def sortedDeserializedObjects(objects: Array[Any]): Unit = {
        objects.foreach {
            case t: Texture => t.load(t.getTextureData)
            case other => throw new UnexpectedObjectException("Expected texture but found " + other)
        }
    }

}
