package fr.overrride.game.shooter.api.other;


import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GraphicComponent {

    void update(float deltaTime);

    void render(SpriteBatch batch);

    void dispose();

}
