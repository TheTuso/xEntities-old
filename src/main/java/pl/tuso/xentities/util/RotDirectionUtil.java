package pl.tuso.xentities.util;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class RotDirectionUtil {
    public static @NotNull Vector toDirection(double yRot, double xRot) {
        Vector vector = new Vector();

        double rotX = yRot;
        double rotY = xRot;

        vector.setY(-Math.sin(Math.toRadians(rotY)));

        double xz = Math.cos(Math.toRadians(rotY));

        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }
}
