package pl.tuso.xentities.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.type.Part;

import java.util.ArrayList;
import java.util.List;

public interface Parent {
    List<Part> parts = new ArrayList<>();
    List<Hitbox> hitboxes = new ArrayList<>();
    //Parts
    default void spawnParts(Level level) {
        for (Part part : parts) {
            this.spawnPart(level, part);
        }
    }

    default void spawnPart(@NotNull Level level, Part part) {
        level.addFreshEntity(part);
    }

    default void removeParts() {
        for (Part part : parts) {
            this.removePart(part);
        }
    }

    default void removePart(@NotNull Part part) {
        part.remove(Entity.RemovalReason.KILLED);
    }
    //Hitboxes
    default void initHitboxes() {
        for (Hitbox hitbox : hitboxes) {
            hitbox.getEntity().setNoGravity(true);
            hitbox.getEntity().setSilent(true);
//            hitbox.getEntity().setInvisible(true);
//            hitbox.getEntity().persistentInvisibility = true;
            hitbox.getEntity().collides = false;
            hitbox.getEntity().persist = false;
        }
    }

    default void addHitboxes(Level level) {
        for (Hitbox hitbox : hitboxes) {
            this.addHitbox(level, hitbox);
        }
    }

    default void addHitbox(@NotNull Level level, @NotNull Hitbox hitbox) {
        level.addFreshEntity(hitbox.getEntity());
    }

    default void removeHitboxes() {
        for (Hitbox hitbox : hitboxes) {
            this.removeHitbox(hitbox);
        }
    }

    default void removeHitbox(@NotNull Hitbox hitbox) {
        hitbox.getEntity().remove(Entity.RemovalReason.KILLED);
    }

    void positionTick();
}
