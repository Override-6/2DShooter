package fr.overrride.game.shooter.api.session.character;

import fr.overrride.game.shooter.api.other.actions.Action;

public interface AxisController {

    void update(float deltaTime);

    void blockXAxis();

    Action blockXAxisFor(int millis);

    void unblockXAxis();

    void blockYAxis();

    Action blockYAxisFor(int millis);

    void unlockYAxis();
}
