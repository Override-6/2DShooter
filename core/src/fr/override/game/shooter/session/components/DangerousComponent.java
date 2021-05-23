package fr.override.game.shooter.session.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import fr.overrride.game.shooter.api.session.character.LivingEntity;
import fr.overrride.game.shooter.api.session.comps.RectangleComponent;
import fr.overrride.game.shooter.api.session.character.Collidable;

public class DangerousComponent extends RectangleComponent {

    private final float damagePerSeconds;

    public DangerousComponent(float x, float y, float width, float height, float damagePerSeconds) {
        super(x, y, width, height, Color.RED);
        this.damagePerSeconds = damagePerSeconds;
    }

    @Override
    public void onCollision(Collidable collidable) {
        if (!(collidable instanceof LivingEntity))
            return;
        float damageToMake = damagePerSeconds * Gdx.graphics.getDeltaTime();
        ((LivingEntity) collidable).damage(damageToMake);
    }
}
