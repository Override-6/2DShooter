package fr.overrride.game.shooter.api.session.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.GameSessionObject;
import fr.overrride.game.shooter.api.session.comps.RectangleComponent;
import org.jetbrains.annotations.Nullable;


import java.util.Optional;
import java.util.Set;

public abstract class Level implements GameSessionObject {

    private final Set<GameSessionObject> components;
    private GameSession session;

    public Level() {
        components = buildLevel();
        /*
         * default frame
         * */
        //TODO GameConstants
        components.add(new RectangleComponent(0, 0, 1920, 76, Color.WHITE));
        components.add(new RectangleComponent(0, 0, 12, 1080, Color.WHITE));
        components.add(new RectangleComponent(0, 1080 - 12, 1920, 12, Color.WHITE));
        components.add(new RectangleComponent(1920 - 13, 0, 12, 1920, Color.WHITE));
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
