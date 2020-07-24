package fr.override.game.shooter.api.other.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {

    protected OrthographicCamera camera;

    protected GameState() {
        camera = new OrthographicCamera();
    }

    protected abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

    public void resize(int width, int height) {

    }
}
