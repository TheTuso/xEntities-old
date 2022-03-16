package pl.tuso.xentities.api;

import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.util.PacketUtil;

public interface Hitbox extends SubEntity {
    default void positionHitboxRelatively(@NotNull Entity reference, double x, double y, double z) {
        Location referenceLocation = reference.getBukkitEntity().getLocation();
        Vector direction = referenceLocation.getDirection();
        Vector i = new Vector(0, 1, 0);

        Vector xPos = i.clone().crossProduct(direction).multiply(x);
        Vector zPos = direction.clone().multiply(z);

        Location childLocation = referenceLocation.clone().subtract(xPos).subtract(zPos);

        getEntity().setPos(childLocation.getX(), childLocation.getY() + y + this.getOwner().getDisplacementY(), childLocation.getZ());
        getEntity().setYRot(reference.getYRot());
        getEntity().setXRot(reference.getXRot());

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getLocation().distanceSquared(this.getOwner().getBukkitEntity().getLocation()) <= Math.pow(this.getOwner().getFollowRange() / 2, 2)) {
                if (!player.canSee(this.getEntity().getBukkitEntity())) {
                    player.showEntity(XEntities.getInstance(), this.getEntity().getBukkitEntity());
                }
                if (getOwner().getDisplacementY() != 0.0) {
                    PacketUtil.sendPackets(player, PacketUtil.teleportWithPackets(
                            getEntity(),
                            childLocation.getX(),
                            childLocation.getY() + y + this.getOwner().getDisplacementY(),
                            childLocation.getZ(),
                            reference.getYRot(),
                            reference.getXRot()));
                }
            } else if (player.canSee(this.getEntity().getBukkitEntity())) {
                player.hideEntity(XEntities.getInstance(), this.getEntity().getBukkitEntity());
            }
        });
    }
}
