package fr.overrride.test

import fr.linkit.engine.connection.cache.obj.description.{ObjectTreeDefaultBehavior, SimpleClassDescription, WrapperInstanceBehavior}
import fr.linkit.engine.connection.cache.obj.description.annotation.AnnotationBasedMemberBehaviorFactory
import fr.overrride.game.shooter.session.character.ShooterCharacter
import org.junit.jupiter.api.Test

class LinkitBehaviorTests {

    @Test
    def testBehavior(): Unit = {
        val tree = new ObjectTreeDefaultBehavior(AnnotationBasedMemberBehaviorFactory())
        val bhv = WrapperInstanceBehavior[ShooterCharacter](SimpleClassDescription(classOf[ShooterCharacter]), tree)
        println(s"bhv = ${bhv}")
    }

}
