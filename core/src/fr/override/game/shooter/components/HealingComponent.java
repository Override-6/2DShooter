package fr.override.game.shooter.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import fr.override.game.shooter.character.LivingEntity;
import fr.override.game.shooter.session.Collidable;

public class HealingComponent extends RectangleComponent {

    private final float pvPerSeconds;

    public HealingComponent(float x, float y, float width, float height, float pvPerSeconds) {
        super(x, y, width, height, Color.GREEN);
        this.pvPerSeconds = pvPerSeconds;
    }

    @Override
    public void onCollision(Collidable collidable) {
        if (!(collidable instanceof LivingEntity))
            return;
        LivingEntity entity = (LivingEntity) collidable;
        float pvToAdd = pvPerSeconds * Gdx.graphics.getDeltaTime();
        if (entity.getHealth() >= entity.getMaxHealth())
            return;
        entity.heal(pvToAdd);
    }
}
