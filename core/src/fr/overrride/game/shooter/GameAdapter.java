package fr.overrride.game.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.linkit.api.connection.ExternalConnection;
import fr.overrride.game.shooter.api.other.states.GameStateManager;
import fr.overrride.game.shooter.session.PlayState;

public class GameAdapter extends ApplicationAdapter {
    private final GameStateManager manager = new GameStateManager();
    private SpriteBatch batch;
    private final ExternalConnection serverConnection;

    public GameAdapter(ExternalConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void create() {
        manager.push(PlayState.class);
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        manager.update(Gdx.graphics.getDeltaTime());
        manager.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        manager.resize(width, height);
    }
}
