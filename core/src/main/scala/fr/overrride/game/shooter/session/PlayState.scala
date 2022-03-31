package fr.overrride.game.shooter.session

import com.badlogic.gdx.Input.Keys._
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, Texture}
import fr.linkit.api.application.connection.ExternalConnection
import fr.linkit.api.gnom.cache.CacheSearchMethod
import fr.linkit.api.gnom.cache.sync.SynchronizedObjectCache
import fr.linkit.engine.gnom.cache.sync.DefaultSynchronizedObjectCache
import fr.linkit.engine.internal.concurrency.pool.HiringBusyWorkerPool
import fr.linkit.engine.internal.language.bhv.{Contract, ObjectsProperty}
import fr.overrride.game.shooter.GameConstants
import fr.overrride.game.shooter.api.other.states.ScreenState
import fr.overrride.game.shooter.api.session.GameSession
import fr.overrride.game.shooter.api.session.character.{KeyControl, KeyType}
import fr.overrride.game.shooter.session.PlayState.lwjglProcrastinator
import fr.overrride.game.shooter.session.character.{CharacterController, ShooterCharacter}
import fr.overrride.game.shooter.session.levels.DefaultLevel
class PlayState(val connection: ExternalConnection) extends ScreenState {

    prepareGNOL()
    private val session: GameSession = if (connection == null) new GameSessionImpl(3, new DefaultLevel, new ParticleManagerImpl()) else {
        val network = connection.network
        connection.traffic.defaultPersistenceConfig.contextualObjectLinker += (701, lwjglProcrastinator)
        val properties = ObjectsProperty.default(network) + ObjectsProperty(Map("lwjgl" -> lwjglProcrastinator))
        val contracts = Contract(classOf[GameSession].getResource("/network/NetworkContract.bhv"))(connection.getApp, properties)
        val center: SynchronizedObjectCache[GameSession] = connection
                .network
                .globalCache
                .attachToCache(51, DefaultSynchronizedObjectCache[GameSession](contracts), CacheSearchMethod.GET_OR_OPEN)
        center.findObject(0).get
    }
    private val background           = new Texture("background.png")
    createPlayers()

    switchToPerformant()
    camera.setToOrtho(false, GameConstants.VIEWPORT_WIDTH, GameConstants.VIEWPORT_HEIGHT)

    private def createPlayers(): Unit = {
        val playerCount = session.countPlayers()
        val player1    = new ShooterCharacter(500 + (playerCount * 100), 75, Color.GREEN)
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

    private def prepareGNOL(): Unit = {
        val col = connection.traffic.defaultPersistenceConfig.contextualObjectLinker
        col += (700, new ParticleManagerImpl())
    }

    private def switchToPerformant(): Unit = {
        connection
                .network
                .globalCache
                .getCacheTrafficNode(51)
                .preferPerformances()
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

    final val lwjglProcrastinator: HiringBusyWorkerPool = new HiringBusyWorkerPool("LWJGL Pool")
}
