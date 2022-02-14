package pl.tuso.xentities.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Part;

import java.util.ArrayList;
import java.util.List;

public interface Parential {
    List<Part> parts = new ArrayList<>();
    List<Hitbox> hitboxes = new ArrayList<>();
    List<Animation> animations = new ArrayList<>();
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

    default List getParts() {
        return this.parts;
    }
    //Hitboxes
    default void initHitboxes() {
        for (Hitbox hitbox : hitboxes) {
            hitbox.getEntity().setNoGravity(true);
            hitbox.getEntity().setSilent(true);
            hitbox.getEntity().setInvisible(true);
            hitbox.getEntity().persistentInvisibility = true;
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

    void positionSubEntityTick();

    default List getHitboxes() {
        return this.hitboxes;
    }
    //Animation
    default void startAnimations() {
        for (Animation animation : animations) {
            this.startAnimation(animation);
        }
    }

    default void stopAnimations() {
        for (Animation animation : animations) {
            this.stopAnimation(animation);
        }
    }

    default void startAnimation(@NotNull Animation animation) {
        animation.start();
    }

    default void stopAnimation(@NotNull Animation animation) {
        animation.stop();
    }

    default List getAnimations() {
        return this.animations;
    }

    IntelligentArmorStand getEntity();
}
