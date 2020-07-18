package fr.override.game.shooter.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {

    protected OrthographicCamera camera;
    protected GameStateManager gameStateManager;

    protected GameState(GameStateManager manager) {
        camera = new OrthographicCamera();
        this.gameStateManager = manager;
    }

    protected abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

    public void resize(int width, int height) {

    }
}
