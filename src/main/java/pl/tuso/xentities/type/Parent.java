package pl.tuso.xentities.type;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.api.Animation;
import pl.tuso.xentities.api.Hitbox;
import pl.tuso.xentities.api.Parential;

import java.util.ArrayList;
import java.util.List;

public abstract class Parent extends IntelligentArmorStand implements Parential {
    List<Part> parts = new ArrayList<>();
    List<Hitbox> hitboxes = new ArrayList<>();
    List<Animation> animations = new ArrayList<>();
    public Parent(EntityType<? extends IntelligentArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public void tick() {
        super.tick();
        positionSubEntityTick();
        if (isRemoved()) {
            removeParts();
            removeHitboxes();
            stopAnimations();
        }
    }
    //Parts
    public void spawnParts(Level level) {
        for (Part part : parts) {
            this.spawnPart(level, part);
        }
    }

    public void spawnPart(@NotNull Level level, Part part) {
        level.addFreshEntity(part);
    }

    public void removeParts() {
        for (Part part : parts) {
            this.removePart(part);
        }
    }

    public void removePart(@NotNull Part part) {
        part.remove(Entity.RemovalReason.KILLED);
    }

    public List getParts() {
        return this.parts;
    }
    //Hitboxes
    @Override
    public void initHitboxes() {
        for (Hitbox hitbox : hitboxes) {
            hitbox.getEntity().setNoGravity(true);
            hitbox.getEntity().setSilent(true);
            hitbox.getEntity().setInvisible(true);
            hitbox.getEntity().persistentInvisibility = true;
            hitbox.getEntity().collides = false;
            hitbox.getEntity().persist = false;
        }
    }

    @Override
    public void addHitboxes(Level level) {
        for (Hitbox hitbox : hitboxes) {
            this.addHitbox(level, hitbox);
        }
    }

    @Override
    public void addHitbox(@NotNull Level level, @NotNull Hitbox hitbox) {
        level.addFreshEntity(hitbox.getEntity());
    }

    @Override
    public void removeHitboxes() {
        for (Hitbox hitbox : hitboxes) {
            this.removeHitbox(hitbox);
        }
    }

    @Override
    public void removeHitbox(@NotNull Hitbox hitbox) {
        hitbox.getEntity().remove(Entity.RemovalReason.KILLED);
    }

    @Override
    public List getHitboxes() {
        return this.hitboxes;
    }

    @Override
    public void positionSubEntityTick() {

    }
    //Animation
    @Override
    public void startAnimations() {
        for (Animation animation : animations) {
            this.startAnimation(animation);
        }
    }

    @Override
    public void stopAnimations() {
        for (Animation animation : animations) {
            this.stopAnimation(animation);
        }
    }

    @Override
    public void startAnimation(@NotNull Animation animation) {
        animation.start();
    }

    @Override
    public void stopAnimation(@NotNull Animation animation) {
        animation.stop();
    }

    @Override
    public List getAnimations() {
        return this.animations;
    }

    @Override
    public abstract IntelligentArmorStand getEntity();
}
