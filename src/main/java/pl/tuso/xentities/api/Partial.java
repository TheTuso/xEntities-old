package pl.tuso.xentities.api;

import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.util.PacketUtil;

public interface Partial extends SubEntity {
    default void positionPartRelatively(@NotNull Entity reference, double x, double y, double z) {
        Location referenceLocation = reference.getBukkitEntity().getLocation();
        Vector direction = referenceLocation.getDirection();
        Vector i = new Vector(0, 1, 0);

        Vector xPos = i.clone().crossProduct(direction).multiply(x);
        Vector zPos = direction.clone().multiply(z);

        Location childLocation = referenceLocation.clone().subtract(xPos).subtract(zPos);

        getEntity().setPos(childLocation.getX(), childLocation.getY() + y + this.getOwner().getDisplacementY(), childLocation.getZ());
        PacketUtil.sendPackets(PacketUtil.teleportWithPackets(getEntity(), childLocation.getX(), childLocation.getY() + y + this.getOwner().getDisplacementY(), childLocation.getZ()));
    }

    @Override
    IntelligentArmorStand getEntity();
}
