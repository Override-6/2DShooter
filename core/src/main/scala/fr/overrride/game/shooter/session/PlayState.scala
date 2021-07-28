package fr.overrride.game.shooter.session

import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.connection.ExternalConnection
import fr.linkit.api.connection.cache.CacheSearchBehavior
import fr.linkit.engine.connection.cache.obj.DefaultEngineObjectCenter
import fr.linkit.engine.connection.cache.obj.description.TreeViewDefaultBehavior
import fr.linkit.engine.connection.cache.obj.description.annotation.AnnotationBasedMemberBehaviorFactory
import fr.overrride.game.shooter.GameConstants
import fr.overrride.game.shooter.api.other.states.ScreenState
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel

class PlayState(val connection: ExternalConnection) extends ScreenState {

    val tree = new TreeViewDefaultBehavior(new AnnotationBasedMemberBehaviorFactory())
    private val session: GameSession = {
        //val test = SimplePuppetClassDescription(classOf[GameSessionImpl])
        val center = connection.runLaterControl {
            connection
                    .network
                    .serverEngine
                    .cache
                    .retrieveCache(0, DefaultEngineObjectCenter[GameSession](), CacheSearchBehavior.GET_OR_OPEN)
        }.join().get
        val obj    = new GameSessionImpl(3, new DefaultLevel)
        center.findObject(0)
                .getOrElse(center.postObject(0, obj))
    }
    private val background           = new Texture("background.png")
    createPlayers()
    camera.setToOrtho(false, GameConstants.VIEWPORT_WIDTH, GameConstants.VIEWPORT_HEIGHT)

    private def createPlayers(): Unit = {
        val player1    = new ShooterCharacter(500, 550, Color.GREEN)
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
        handleInputs()
        session.updateScene(deltaTime)
    }

    override def render(batch: SpriteBatch): Unit = {
        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        batch.draw(background, 0, 0)
        session.renderScene(batch)
        batch.end()
    }

    override def dispose(): Unit = {
        session.disposeScene()
        background.dispose()
    }
}
