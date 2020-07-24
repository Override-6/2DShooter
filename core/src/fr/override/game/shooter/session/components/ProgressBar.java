package fr.override.game.shooter.session.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.session.GameSession;
import fr.override.game.shooter.api.session.comps.RectangleComponent;
import fr.override.game.shooter.session.GameSessionImpl;
import fr.override.game.shooter.api.session.character.GameSessionObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ProgressBar implements GameSessionObject {

    private final RectangleComponent backGround, progression;

    private GameSession gameSession;
    private final float max;

    public ProgressBar(Color background, Color progression, float x, float y, int width, int height, float max) {
        this.backGround = new RectangleComponent(x, y, width, height, background);
        this.progression = new RectangleComponent(x, y, 0, height, progression);
        this.max = max;
    }

    public ProgressBar(Color background, Color progression, float x, float y, int width, int height, float max, float progress) {
        this(background, progression, x, y, width, height, max);
        this.progression.setWidth(width * (progress / max));
    }


    public float getMax() {
        return max;
    }

    public void setProgress(float progress) {
        float width = backGround.getWidth() * (progress / max);
        progression.setWidth(Math.max(width, 0));
    }

    public float getProgress() {
        return (progression.getWidth() / backGround.getWidth()) * max;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(SpriteBatch batch) {
        backGround.render(batch);
        progression.render(batch);
    }

    @Override
    public void dispose() {
        backGround.dispose();
        progression.dispose();
    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(gameSession);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        this.gameSession = gameSession;
        backGround.setGameSession(gameSession);
        progression.setGameSession(gameSession);
    }


    @Override
    public Vector2 getLocation() {
        return backGround.getLocation();
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.FOREGROUND;
    }

    @Override
    public String toString() {
        return progression + " / " + max;
    }

    public void setPosition(Vector2 position) {
        backGround.setPosition(position);
        backGround.setPosition(position);
    }

    public void setPosition(float x, float y) {
        backGround.setPosition(x, y);
        progression.setPosition(x, y);
    }
}
