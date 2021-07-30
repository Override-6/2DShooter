package fr.overrride.game.shooter.api.other.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public class GameStateManager {

    private final Stack<ScreenState> screenStates = new Stack<>();

    public void push(ScreenState state) {
        screenStates.push(state);
    }

    public void update(float dt) {
        screenStates.peek().update(dt);
    }

    public void render(SpriteBatch batch) {
        screenStates.peek().render(batch);
    }

    public void resize(int width, int height) {
        screenStates.peek().resize(width, height);
    }
}
