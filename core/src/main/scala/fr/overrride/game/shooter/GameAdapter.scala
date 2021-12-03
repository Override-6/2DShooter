package fr.overrride.game.shooter

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import fr.linkit.api.application.connection.ExternalConnection
import fr.linkit.engine.internal.concurrency.pool.HiringBusyWorkerPool
import fr.overrride.game.shooter.api.other.states.GameStateManager
import fr.overrride.game.shooter.session.PlayState

class GameAdapter(val serverConnection: ExternalConnection) extends ApplicationAdapter {

    final private val manager      = new GameStateManager
    private var batch: SpriteBatch = _

    override def create(): Unit = {
        val pool = new HiringBusyWorkerPool("LWJGL Pool")
        PlayState.lwjglProcrastinator = pool
        pool.runLater {
            manager.push(new PlayState(serverConnection))
            batch = new SpriteBatch
        }
        pool.hireCurrentThread()
        Gdx.app
    }

    override def render(): Unit = {
        manager.update(Gdx.graphics.getDeltaTime)
        manager.render(batch)
    }

    override def dispose(): Unit = {
        batch.dispose()
    }

    override def resize(width: Int, height: Int): Unit = {
        manager.resize(width, height)
    }
}
