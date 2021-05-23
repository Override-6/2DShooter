package fr.overrride.game.shooter.api.other.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public class GameStateManager {

    private final Stack<GameState> gameStates = new Stack<>();

    public void push(GameState state) {
        gameStates.push(state);
    }

    public <T extends GameState> void push(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T state = constructor.newInstance();
            push(state);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void set(GameState gameState) {
        gameStates.pop().dispose();
        push(gameState);
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render(SpriteBatch batch) {
        gameStates.peek().render(batch);
    }

    public void resize(int width, int height) {
        gameStates.peek().resize(width, height);
    }
}
