package fr.overrride.game.shooter.session

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.math.Vector2
import fr.linkit.api.application.connection.ExternalConnection
import fr.linkit.api.gnom.cache.CacheSearchBehavior
import fr.linkit.api.gnom.cache.sync.SynchronizedObjectCache
import fr.linkit.api.internal.concurrency.Procrastinator
import fr.linkit.engine.gnom.cache.sync.DefaultSynchronizedObjectCache
import fr.linkit.engine.gnom.cache.sync.behavior.ObjectBehaviorBuilder.MethodControl
import fr.linkit.engine.gnom.cache.sync.behavior.{AnnotationBasedMemberBehaviorFactory, ObjectBehaviorBuilder, ObjectBehaviorStoreBuilder}
import fr.linkit.engine.gnom.cache.sync.instantiation.Constructor
import fr.overrride.game.shooter.GameConstants
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel
import fr.overrride.game.shooter.session.weapons.SimpleWeapon
import fr.overrride.game.shooter.api.other.states.ScreenState
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.linkit.api.gnom.cache.sync.behavior.annotation.BasicInvocationRule.BROADCAST_IF_OWNER
class PlayState(val connection: ExternalConnection) extends ScreenState {

    private val lwjglProcrastinator  = Procrastinator.wrapSubmitterRunnable(Gdx.app.postRunnable)
    private val tree                 = new ObjectBehaviorStoreBuilder(AnnotationBasedMemberBehaviorFactory) {
        behaviors += new ObjectBehaviorBuilder[ShooterCharacter]() {
            annotateAllMethods("damage") and "setWeapon" and "dash" by new MethodControl(BROADCAST_IF_OWNER, procrastinator = lwjglProcrastinator)
        }
        behaviors += new ObjectBehaviorBuilder[SimpleWeapon]() {
            annotateAllMethods("shoot") by new MethodControl(BROADCAST_IF_OWNER, procrastinator = lwjglProcrastinator)
        }
        behaviors += new ObjectBehaviorBuilder[Vector2]() {
            annotateAllMethods("set") by new MethodControl(BROADCAST_IF_OWNER)
        }
    }.build
    private val session: GameSession = if (connection == null) new GameSessionImpl(3, new DefaultLevel) else {
        //val test = SimplePuppetClassDescription(classOf[GameSessionImpl])
        val center: SynchronizedObjectCache[GameSession] = connection.runLaterControl {
            connection
                    .network
                    .globalCache
                    .attachToCache(51, DefaultSynchronizedObjectCache[GameSession](tree), CacheSearchBehavior.GET_OR_OPEN)
        }.join().get
        center.getOrSynchronize(0)(Constructor[GameSessionImpl](3, new DefaultLevel))
    }
    println("Game Session found !")
    private val background = new Texture("background.png")
    createPlayers()
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
