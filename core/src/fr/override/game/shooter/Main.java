package fr.override.game.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.override.game.shooter.states.GameStateManager;
import fr.override.game.shooter.states.PlayState;

public class Main extends ApplicationAdapter {
    private final GameStateManager manager = new GameStateManager();
    private SpriteBatch batch;

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
