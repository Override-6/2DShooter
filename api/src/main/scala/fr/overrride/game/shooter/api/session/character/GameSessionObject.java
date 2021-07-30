package fr.overrride.game.shooter.api.session.character;

import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.other.GraphicComponent;
import fr.overrride.game.shooter.api.session.GameSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface GameSessionObject extends GraphicComponent, Comparable<GameSessionObject> {

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
