package fr.override.game.shooter.session;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public interface GameSessionObject extends Disposable, Comparable<GameSessionObject> {

    void update(float deltaTime);

    void render(SpriteBatch batch);

    Optional<GameSession> getCurrentGameSession();

    void setGameSession(@Nullable GameSession gameSession);

    Vector2 getLocation();

    DrawPriority getDrawPriority();

    default int compareTo(@NotNull GameSessionObject o) {
        if (getDrawPriority() == o.getDrawPriority() && !equals(o))
            return 1;
        return getDrawPriority().ordinal() - o.getDrawPriority().ordinal();
    }

    enum DrawPriority {
        BACKGROUND, MIDDLE, FOREGROUND
    }

}
