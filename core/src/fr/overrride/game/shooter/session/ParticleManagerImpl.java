package fr.overrride.game.shooter.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.other.actions.RestAction;
import fr.overrride.game.shooter.api.other.actions.RestActionCompleter;
import fr.overrride.game.shooter.api.other.actions.SimpleRestAction;
import fr.overrride.game.shooter.api.session.ParticleManager;

import java.util.HashMap;
import java.util.Map;

public class ParticleManagerImpl implements ParticleManager {

    private final Map<ParticleEffect, RestActionCompleter<ParticleEffect>> currentParticles = new HashMap<>();

    @Override
    public void update(float deltaTime) {
        new HashMap<>(currentParticles).forEach((effect, action) -> {
            effect.update(deltaTime);
            if (effect.isComplete()) {
                currentParticles.remove(effect);
                action.onActionCompleted(effect);
                effect.dispose();
            }
        });
    }

    @Override
    public void render(SpriteBatch batch) {
        currentParticles.forEach((effect, action) -> effect.draw(batch));
    }

    @Override
    public void dispose() {

    }

    @Override
    public RestAction<ParticleEffect> playEffect(String file, Vector2 position) {
        ParticleEffect effect = createEffect(file, position.x, position.y, Color.WHITE);

        SimpleRestAction<ParticleEffect> action = RestAction.build(effect);
        currentParticles.put(effect, action);

        return action;
    }

    @Override
    public RestAction<ParticleEffect> playEffect(String file, float x, float y, Color tint) {
        ParticleEffect effect = createEffect(file, x, y, tint);

        SimpleRestAction<ParticleEffect> action = RestAction.build(effect);
        currentParticles.put(effect, action);

        return action;
    }

    @Override
    public RestAction<ParticleEffect> playEffect(String file, float x, float y, Color tint, float highMax, float highMin, float lowMax, float lowMin) {
        ParticleEffect effect = createEffect(file, x, y, tint);

        SimpleRestAction<ParticleEffect> action = RestAction.build(effect);
        currentParticles.put(effect, action);

        ParticleEmitter.ScaledNumericValue wind = effect.getEmitters()
                .first()
                .getWind();

        wind.setHigh(highMin, highMax);
        wind.setLow(lowMin, lowMax);

        return action;
    }

    @Override
    public RestAction<ParticleEffect> playEffect(String file, Vector2 position, Color tint) {
        return playEffect(file, position.x, position.y, tint);
    }

    private ParticleEffect createEffect(String file, float x, float y, Color tint) {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(file), Gdx.files.internal("particles"));
        effect.setPosition(x, y);
        effect.start();

        float[] color = effect.getEmitters()
                .first()
                .getTint()
                .getColors();

        color[0] = tint.r;
        color[1] = tint.g;
        color[2] = tint.b;

        return effect;
    }

}
