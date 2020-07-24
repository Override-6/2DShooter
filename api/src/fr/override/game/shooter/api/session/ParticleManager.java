package fr.override.game.shooter.api.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.other.GraphicComponent;
import fr.override.game.shooter.api.other.actions.RestAction;

public interface ParticleManager extends GraphicComponent {


    RestAction<ParticleEffect> playEffect(String file, Vector2 position);

    RestAction<ParticleEffect> playEffect(String file, Vector2 position, Color tint);

    RestAction<ParticleEffect> playEffect(String file, float x, float y, Color tint);

    RestAction<ParticleEffect> playEffect(String file, float x, float y, Color tint, float highMax, float highMin, float lowMax, float lowMin);
}
