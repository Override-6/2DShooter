package fr.override.game.shooter.session.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.components.RectangleComponent;
import fr.override.game.shooter.session.GameSession;
import fr.override.game.shooter.session.GameSessionObject;
import org.jetbrains.annotations.Nullable;


import java.util.Optional;
import java.util.Set;

import static fr.override.game.shooter.util.GameConstants.*;

public abstract class Level implements GameSessionObject {

    private final Set<GameSessionObject> components;
    private GameSession session;

    public Level() {
        components = buildLevel();
        /*
         * default frame
         * */
        components.add(new RectangleComponent(0, 0, VIEWPORT_WIDTH, 76, Color.WHITE));
        components.add(new RectangleComponent(0, 0, 12, VIEWPORT_HEIGHT, Color.WHITE));
        components.add(new RectangleComponent(0, VIEWPORT_HEIGHT - 12, VIEWPORT_WIDTH, 12, Color.WHITE));
        components.add(new RectangleComponent(VIEWPORT_WIDTH - 13, 0, 12, VIEWPORT_HEIGHT, Color.WHITE));
    }

    @Override
    public void update(float deltaTime) {
        onUpdate(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
    }

    @Override
    public void dispose() {
        components.forEach(GameSessionObject::dispose);
        if (session != null)
            components.forEach(session::removeObject);
    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(session);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        if (gameSession == null) {
            return;
        }
        this.session = gameSession;

        components.forEach(component -> {
            component.setGameSession(gameSession);
            gameSession.addObject(component);
        });
    }

    @Override
    public Vector2 getLocation() {
        return new Vector2();
    }

    protected void addComponent(GameSessionObject component) {
        components.add(component);
    }

    protected void removeComponent(GameSessionObject component) {
        components.remove(component);
    }

    protected abstract Set<GameSessionObject> buildLevel();

    protected abstract void onUpdate(float deltaTime);

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.BACKGROUND;
    }
}
