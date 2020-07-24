package fr.override.game.shooter.api.session.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.other.animations.Animable;
import fr.override.game.shooter.api.session.GameSession;
import fr.override.game.shooter.api.session.character.GameSessionObject;
import fr.override.game.shooter.api.other.util.MathUtils;
import fr.override.game.shooter.api.session.character.Shooter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Weapon implements GameSessionObject, Animable {

    private final Texture texture;
    private Shooter shooter;
    private final Muzzle muzzle;
    private float rotation = 0;

    private final double speed;
    private GameSession session;

    private double lastShoot = 0;

    public Weapon(Shooter owner, Texture texture, float speed, Muzzle muzzle) {
        this.shooter = owner;
        this.texture = texture;
        this.speed = speed;
        this.muzzle = muzzle;
    }

    @Override
    public void update(float dt) {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();

        rotation = MathUtils.angle(getCenter(), new Vector2(mouseX, mouseY), 1080 - 13); //TODO GameConstants
        muzzle.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 pos = getCenter();

        float x = pos.x - 7.5F;
        float y = pos.y;

        boolean yFlip = rotation > 90 && rotation < 270;
        batch.draw(texture, x, y, 0, yFlip ? 0 : 0.75F, 1, 1, texture.getWidth(), texture.getHeight(), rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, yFlip);
    }

    @Override
    public void dispose() {
        if (session != null)
            session.removeObject(this);
        texture.dispose();
    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(session);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        this.session = gameSession;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public Vector2 getLocation() {
        return getCenter();
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.FOREGROUND;
    }

    @Override
    public void setLocation(float x, float y) {
        //no-op
    }

    @Override
    public void setRotation(float angle) {
        rotation = angle;
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void setSize(float width, float height) {
        /* no-op */
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShoot >= speed;
    }

    public void shoot() {
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(rotation - 90);

        muzzle.fire(direction, this);
        lastShoot = System.currentTimeMillis();
    }

    public Shooter getOwner() {
        return shooter;
    }

    public void setOwner(Shooter owner) {
        this.shooter = owner;
    }

    private Vector2 getCenter() {
        Vector2 pos = new Vector2(shooter.getLocation());
        pos.add(75 / 2F, 30);
        return pos;
    }

    public static Weapon empty(Shooter shooter) {
        return new Weapon(shooter, new Texture("empty.png"), 0, new Muzzle() {
            @Override
            public void fire(Vector2 direction, Weapon weapon) {

            }

            @Override
            public void update(float deltaTime) {

            }
        });
    }

}

