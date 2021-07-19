package fr.overrride.game.shooter.session

import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.connection.ExternalConnection
import fr.linkit.api.connection.cache.CacheSearchBehavior
import fr.linkit.api.local.concurrency.WorkerPools
import fr.linkit.engine.connection.cache.repo.DefaultEngineObjectCenter
import fr.overrride.game.shooter.GameConstants
import fr.overrride.game.shooter.api.other.states.ScreenState
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel

class PlayState(val connection: ExternalConnection) extends ScreenState {

    private val session: GameSession = {
        val center = connection.runLaterControl {
            connection
                    .network
                    .serverEngine
                    .cache
                    .getCache(0, DefaultEngineObjectCenter[GameSession](), CacheSearchBehavior.GET_OR_OPEN)
        }.join().get
        center.findObject(0)
                .getOrElse(center.postObject(0, new GameSessionImpl(3, new DefaultLevel)))
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
        val player2     = new ShooterCharacter(1500, 550, Color.PURPLE)
        val controller2 = new CharacterController(player2)
        controller2.addKeyControl(KeyControl.of(KeyType.DASH, P, _.dash()))
        controller2.addKeyControl(KeyControl.of(KeyType.JUMP, UP, _.jump()))
        controller2.addKeyControl(KeyControl.of(KeyType.LEFT, LEFT, _.left()))
        controller2.addKeyControl(KeyControl.of(KeyType.RIGHT, RIGHT, _.right()))
        controller2.addKeyControl(KeyControl.of(KeyType.SHOOT, R, _.shoot()))
        player2.setController(controller2)
        player2.setGameSession(session)
        session.addCharacter(player1)
        session.addCharacter(player2)
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
