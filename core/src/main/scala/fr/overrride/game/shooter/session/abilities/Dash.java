package fr.overrride.game.shooter.session.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.session.abilities.Ability;
import fr.overrride.game.shooter.api.session.character.Character;
import org.jetbrains.annotations.Nullable;

public class Dash implements Ability {

    private static final float POWER = 5000;
    private static final double USE_RATE = 1250;

    private double lastUse = 0;
    private final Character character;
    @Nullable
    private ParticleEffect currentEffect = null;

    private boolean isUsing = false;

    public Dash(Character character) {
        this.character = character;
    }

    @Override
    public void use() {
        boolean mouseRight = Gdx.input.getX() >= character.getWeapon().getLocation().x;

        character.getVelocity().x += mouseRight ? POWER : -POWER;
        lastUse = System.currentTimeMillis();
        isUsing = true;

        character.getCurrentGameSession()
                .ifPresent(gameSession -> currentEffect = gameSession
                        .getParticleManager()
                        .playEffect("particles/dash.party", character.getLocation(), character.getColor())
                        .get());

        character.getAxisController().blockYAxisFor(750)
                .then(() -> {
                    isUsing = false;
                    currentEffect = null;
                });


    }

    @Override
    public boolean canUse() {
        return System.currentTimeMillis() - lastUse >= USE_RATE;
    }

    @Override
    public boolean isUsing() {
        return isUsing;
    }

    @Override
    public void update(float deltaTime) {
        if (isUsing && currentEffect != null) {
            Vector2 pos = character.getLocation();
            currentEffect.getEmitters().first().setPosition(pos.x, pos.y + 35);
        }
    }
}
