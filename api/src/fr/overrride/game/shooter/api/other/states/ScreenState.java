package fr.overrride.game.shooter.api.other.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ScreenState {

    protected OrthographicCamera camera;

    protected ScreenState() {
        camera = new OrthographicCamera();
    }

    protected abstract void handleInputs();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

    public void resize(int width, int height) {

    }
}
