package fr.override.game.shooter.session.levels;

import fr.overrride.game.shooter.api.session.character.GameSessionObject;
import fr.overrride.game.shooter.api.session.levels.Level;

import java.util.HashSet;
import java.util.Set;

public class EmptyLevel extends Level {
    @Override
    protected Set<GameSessionObject> buildLevel() {
        return new HashSet<>();
    }

    @Override
    protected void onUpdate(float deltaTime) {

    }
}
