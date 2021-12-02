package fr.overrride.game.shooter.session.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.gnom.cache.sync.SynchronizedObject;
import fr.linkit.api.gnom.cache.sync.contract.behavior.annotation.BasicInvocationRule;
import fr.linkit.api.gnom.cache.sync.contract.behavior.annotation.MethodControl;
import fr.linkit.api.gnom.cache.sync.contract.behavior.annotation.Synchronized;
import fr.overrride.game.shooter.api.other.util.MathUtils;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.Shooter;
import fr.overrride.game.shooter.api.session.weapon.Muzzle;
import fr.overrride.game.shooter.api.session.weapon.Weapon;
import fr.overrride.game.shooter.session.character.ShooterCharacter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static fr.linkit.api.gnom.cache.sync.contract.behavior.annotation.BasicInvocationRule.BROADCAST_IF_OWNER;
import static fr.overrride.game.shooter.GameConstants.SIZE_DIVIDE;


public class SimpleWeapon implements Weapon {

    private final Texture texture;
    @Synchronized
    private Shooter owner;
    private final Muzzle muzzle;
    private float rotation = 0;

    private final float fireRate;
    private GameSession session;

    private double lastShoot = 0;

    public SimpleWeapon(@Synchronized Shooter owner, Texture texture, float fireRate, Muzzle muzzle) {
        this.owner = owner;
        this.texture = texture;
        this.fireRate = fireRate;
        this.muzzle = muzzle;
    }

    public SimpleWeapon(@Synchronized Shooter owner, String texturePath, float fireRate, Muzzle muzzle) {
        this(owner, new Texture(Gdx.files.internal(texturePath)), fireRate, muzzle);
    }

    public SimpleWeapon(SimpleWeapon other) {
        this(other.owner, other.texture, other.fireRate, other.muzzle);
    }

    @Override
    public void update(float dt) {
        SynchronizedObject<ShooterCharacter> syncOwner = (SynchronizedObject<ShooterCharacter>) owner;
        if (syncOwner.isOwnedByCurrent()) {
            int mouseX = Gdx.input.getX() * SIZE_DIVIDE;
            int mouseY = Gdx.input.getY() * SIZE_DIVIDE;
            int rotation = (int) MathUtils.angle(getCenter(), new Vector2(mouseX, mouseY), 1080 - 13);
            if ((int) this.rotation != rotation && !muzzle.isPlayingRecoilAnimation())
                setRotation(rotation);
        }
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

    @MethodControl(value = BROADCAST_IF_OWNER)
    public void setRotation(float angle) {
        rotation = angle % 360;
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
        return System.currentTimeMillis() - lastShoot >= fireRate;
    }

    @MethodControl(value = BasicInvocationRule.BROADCAST_IF_OWNER)
    public void shoot() {
        if (!canShoot())
            return;
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(rotation - 90);
        muzzle.fire(direction, this);
        lastShoot = System.currentTimeMillis();
    }

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

    public static SimpleWeapon empty(Shooter shooter) {
        return new SimpleWeapon(shooter, (Texture) new Texture("empty.png"), 0, new Muzzle() {
            @Override
            public void fire(Vector2 direction, Weapon weapon) {

            }

            @Override
            public void update(float deltaTime) {

            }

            @Override
            public boolean isPlayingRecoilAnimation() {
                return false;
            }
        });
    }

}

