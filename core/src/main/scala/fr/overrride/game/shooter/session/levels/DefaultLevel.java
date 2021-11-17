package fr.overrride.game.shooter.session.levels;

import com.badlogic.gdx.graphics.Color;
import fr.overrride.game.shooter.api.session.GameSessionObject;
import fr.overrride.game.shooter.api.session.comps.RectangleComponent;
import fr.overrride.game.shooter.api.session.levels.Level;
import fr.overrride.game.shooter.session.components.DangerousComponent;
import fr.overrride.game.shooter.session.components.HealingComponent;

import java.util.HashSet;
import java.util.Set;

public class DefaultLevel extends Level {

    @Override
    protected Set<GameSessionObject> buildLevel() {
        Set<GameSessionObject> components = new HashSet<>();
        components.add(new RectangleComponent(75, 800, 400, 50, Color.WHITE));
        components.add(new RectangleComponent(1440, 800, 400, 50, Color.WHITE));
        components.add(new HealingComponent(900, 700, 100, 100, 12));
        components.add(new RectangleComponent(750, 450, 400, 50, Color.WHITE));
        components.add(new DangerousComponent(1600, 350, 50, 30, 25));
        components.add(new DangerousComponent(250, 350, 50, 30, 25));
        components.add(new RectangleComponent(250, 76, 50, 275, Color.WHITE));
        //NOW HANDLED BY SERVER components.add(new ItemSpawner(800, 525, 5000, 15000, 5000, 6000, 7500));
        components.add(new RectangleComponent(1600, 76, 50, 275, Color.WHITE));
        return components;
    }

    @Override
    protected void onUpdate(float deltaTime) {
    }
}
