package fr.overrride.game.shooter.session

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.Vector2
import fr.linkit.api.application.connection.ExternalConnection
import fr.linkit.api.gnom.cache.CacheSearchBehavior
import fr.linkit.api.gnom.cache.sync.SynchronizedObjectCache
import fr.linkit.api.gnom.cache.sync.contract.behavior.annotation.BasicInvocationRule.BROADCAST_IF_OWNER
import fr.linkit.api.internal.concurrency.Procrastinator
import fr.linkit.engine.gnom.cache.sync.DefaultSynchronizedObjectCache
import fr.linkit.engine.gnom.cache.sync.contract.behavior.builder.SynchronizedObjectBehaviorFactoryBuilder
import fr.linkit.engine.gnom.cache.sync.contract.behavior.builder.SynchronizedObjectBehaviorFactoryBuilder.MethodBehaviorBuilder
import fr.linkit.engine.gnom.cache.sync.contract.description.SyncObjectDescription.fromTag
import fr.overrride.game.shooter.GameConstants
import fr.overrride.game.shooter.api.other.states.ScreenState
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.overrride.game.shooter.session.PlayState.gameSessionBehavior
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel
import fr.overrride.game.shooter.session.weapons.SimpleWeapon

class PlayState(val connection: ExternalConnection) extends ScreenState {

    private val session: GameSession = if (connection == null) new GameSessionImpl(3, new DefaultLevel) else {
        val center: SynchronizedObjectCache[GameSession] = connection
                .network
                .globalCache
                .attachToCache(51, DefaultSynchronizedObjectCache[GameSession](gameSessionBehavior), CacheSearchBehavior.GET_OR_OPEN)
        center.findObject(0).get
    }
    private val background           = new Texture("background.png")
    createPlayers()

    switchToPerformant()
    camera.setToOrtho(false, GameConstants.VIEWPORT_WIDTH, GameConstants.VIEWPORT_HEIGHT)

    private def createPlayers(): Unit = {
        val player1    = new ShooterCharacter(500 + (session.countPlayers() * 100), 75, Color.GREEN)
        val controller = new CharacterController(player1)
        controller.addKeyControl(KeyControl.of(KeyType.DASH, A, _.dash()))
        controller.addKeyControl(KeyControl.of(KeyType.JUMP, SPACE, _.jump()))
        controller.addKeyControl(KeyControl.of(KeyType.LEFT, Q, _.left()))
        controller.addKeyControl(KeyControl.of(KeyType.RIGHT, D, _.right()))
        controller.addKeyControl(KeyControl.of(KeyType.SHOOT, E, _.shoot()))
        player1.setController(controller)
        player1.setGameSession(session)
        session.addCharacter(player1)

    }

    private def switchToPerformant(): Unit = {

    }

    override protected def handleInputs(): Unit = {
        session.updateInputs()
    }

    override def update(deltaTime: Float): Unit = {
        session.updateScene(deltaTime)
        handleInputs()
    }

    override def render(batch: SpriteBatch): Unit = {
        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        batch.draw(background, 0, 0)
        session.renderScene(batch)
        batch.end()
        //println("FPS : " + Gdx.graphics.getFramesPerSecond)
    }

    override def dispose(): Unit = {
        session.disposeScene()
        background.dispose()
    }
}

object PlayState {

    final val lwjglProcrastinator = Procrastinator.wrapSubmitterRunnable({ runnable =>
        if (Gdx.app == null) runnable.run() //run in the current thread
        else Gdx.app.postRunnable(runnable)
    })
    final val gameSessionBehavior = new SynchronizedObjectBehaviorFactoryBuilder {
        describe(new ClassDescriptor[ShooterCharacter]() {
            enable method "damage" and "setWeapon" and "dash" as new MethodBehaviorBuilder(BROADCAST_IF_OWNER) {
                withProcrastinator(lwjglProcrastinator)
            }
            enable method "makeWalkingEffect" as new MethodBehaviorBuilder(agreement => agreement.acceptAll().discard("GameServer")) {
                withProcrastinator(lwjglProcrastinator)
            }
        })
        describe(new ClassDescriptor[SimpleWeapon]() {
            enable method "shoot" as new MethodBehaviorBuilder(BROADCAST_IF_OWNER) {
                withProcrastinator(lwjglProcrastinator)
                mustForceLocalInvocation()
            }
        })
        describe(new ClassDescriptor[Vector2]() {
            enable method "set" as new MethodBehaviorBuilder(BROADCAST_IF_OWNER) {}
        })
    }.build()

}
