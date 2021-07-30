package fr.overrride.game.shooter.api.other.util;

import com.badlogic.gdx.math.Vector2;

public final class MathUtils {

    private MathUtils() {
        //no instance
    }

    public static float angle(Vector2 origin, Vector2 cause, float yShift) {

        double dx = cause.x - origin.x;
        double dy = cause.y + origin.y - yShift;

        double inRad = Math.atan2(dy, dx);

        if (inRad < 0)
            inRad = -inRad;
        else inRad = 2 * Math.PI - inRad;

        return (float) Math.toDegrees(inRad) % 360;
    }

}
