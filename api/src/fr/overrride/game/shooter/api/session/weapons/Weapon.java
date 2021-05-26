package fr.overrride.game.shooter.api.session.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.connection.cache.repo.annotations.InvocationKind;
import fr.linkit.api.connection.cache.repo.annotations.MethodControl;
import fr.overrride.game.shooter.api.other.animations.Animable;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.GameSessionObject;
import fr.overrride.game.shooter.api.other.util.MathUtils;
import fr.overrride.game.shooter.api.session.character.Shooter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static fr.linkit.api.connection.cache.repo.annotations.InvocationKind.ONLY_LOCAL;
import static fr.linkit.api.connection.cache.repo.annotations.InvocationKind.ONLY_OWNER;

public class Weapon implements GameSessionObject, Animable {

    private final Texture texture;
    private Shooter owner;
    private final Muzzle muzzle;
    private float rotation = 0;

    private final float fireRate;
    private GameSession session;

    private double lastShoot = 0;

    public Weapon(Shooter owner, Texture texture, float fireRate, Muzzle muzzle) {
        this.owner = owner;
        this.texture = texture;
        this.fireRate = fireRate;
        this.muzzle = muzzle;
    }

    public Weapon(Shooter owner, String texturePath, float fireRate, Muzzle muzzle) {
        this(owner, new Texture(Gdx.files.internal(texturePath)), fireRate, muzzle);
    }

    public Weapon(Weapon other) {
        this(other.owner, other.texture, other.fireRate, other.muzzle);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void update(float dt) {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();

        rotation = MathUtils.angle(getCenter(), new Vector2(mouseX, mouseY), 1080 - 13); //TODO GameConstants
        muzzle.update(dt);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void render(SpriteBatch batch) {
        Vector2 pos = getCenter();

        float x = pos.x - 7.5F;
        float y = pos.y;

        boolean yFlip = rotation > 90 && rotation < 270;
        batch.draw(texture, x, y, 0, yFlip ? 0 : 0.75F, 1, 1, texture.getWidth(), texture.getHeight(), rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, yFlip);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void dispose() {
        if (session != null)
            session.removeObject(this);
        texture.dispose();
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(session);
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void setGameSession(@Nullable GameSession gameSession) {
        this.session = gameSession;
    }

    @Override
    @MethodControl(ONLY_OWNER)
    public float getRotation() {
        return rotation;
    }

    @Override
    @MethodControl(ONLY_OWNER)
    public Vector2 getLocation() {
        return getCenter();
    }

    @Override
    @MethodControl(ONLY_OWNER)
    public DrawPriority getDrawPriority() {
        return DrawPriority.FOREGROUND;
    }

    @Override
    public void setLocation(float x, float y) {
        //no-op
    }

    @Override
    @MethodControl(ONLY_OWNER)
    public void setRotation(float angle) {
        rotation = angle;
    }

    @Override
    @MethodControl(ONLY_OWNER)
    public Vector2 getSize() {
        return new Vector2(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void setSize(float width, float height) {
        /* no-op */
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShoot >= fireRate;
    }

    @MethodControl(ONLY_LOCAL)
    public void shoot() {
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(rotation - 90);

        muzzle.fire(direction, this);
        lastShoot = System.currentTimeMillis();
    }

    @MethodControl(ONLY_OWNER)
    public Shooter getOwner() {
        return owner;
    }

    public void setOwner(Shooter owner) {
        this.owner = owner;
    }

    private Vector2 getCenter() {
        Vector2 pos = new Vector2(owner.getLocation());
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

