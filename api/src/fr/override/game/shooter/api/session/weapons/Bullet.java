package fr.override.game.shooter.api.session.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.api.session.GameSession;
import fr.override.game.shooter.api.session.character.*;
import fr.override.game.shooter.api.session.comps.RectangleComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Bullet extends RectangleComponent implements GameSessionObject, Collidable {

    private final Vector2 velocity;
    private final Vector2 lastLocation;
    private final Shooter shooter;
    private final float weight;
    private final float damage;
    private GameSession session;


    private Bullet(Shooter shooter, Vector2 location, Vector2 velocity, float weight, float damage) {
        super(location.x, location.y, 25, 10, Color.ORANGE);
        this.velocity = velocity;
        this.lastLocation = new Vector2();
        this.weight = weight;
        this.shooter = shooter;
        this.damage = damage;

    }

    public static Bullet create(Shooter shooter, Vector2 location, Vector2 velocity, float weight, float damage) {
        Bullet bullet = new Bullet(shooter, location, velocity, weight, damage);
        shooter.getCurrentGameSession().ifPresent(session -> {
            bullet.setGameSession(session);
            session.addObject(bullet);
        });
        return bullet;
    }

    public static Bullet create(Weapon weapon, Vector2 velocity, float weight, float damage) {
        return create(weapon.getOwner(), weapon.getLocation(), velocity, weight, damage);
    }

    @Override
    public void update(float deltaTime) {
        lastLocation.set(getLocation());

        velocity.sub(0, weight);
        velocity.scl(deltaTime);
        position.add(velocity);
        velocity.scl(1 / deltaTime);

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 vec = new Vector2(position.x - lastLocation.x, position.y - lastLocation.y);
        float angle = vec.angle();
        super.render(batch, 0.5F, 0.5F, angle);
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
    public void onCollision(Collidable collidable) {
        if (collidable == shooter || session == null || !collidable.isSolid())
            return;

        if (collidable instanceof LivingEntity) {
            handleEntityCollision((LivingEntity) collidable);
            makeImpact(collidable);
            return;
        }

        if (collidable instanceof Bullet) {
            if (((Bullet) collidable).getShooter() == shooter)
                return; //it's a friendly bullet !
            session.removeObject(collidable);
            collidable.dispose();
        }

        makeImpact(collidable);

    }

    private void handleEntityCollision(LivingEntity entity) {
        entity.damage(damage);
        Vector2 bulletVelocity = velocity;
        float weight = this.weight;
        float multiplier = weight / (3.6F * 5);
        entity.getVelocity().set(bulletVelocity.x * multiplier, bulletVelocity.y * multiplier);
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public Rectangle getHitBox() {
        return super.getHitBox();
    }

    public Shooter getShooter() {
        return shooter;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getDamage() {
        return damage;
    }

    public float getWeight() {
        return weight;
    }

    private void makeImpact(Collidable collidable) {
        Color color = extractColor(collidable);

        session.getParticleManager()
                .playEffect("particles/impact.party", position, color)
                .then(this::dispose);
        session.removeObject(this);
        dispose();
    }

    private Color extractColor(Collidable collidable) {
        if (collidable instanceof Colorable) {
            return ((Colorable) collidable).getColor();
        }
        return Color.WHITE;
    }
}