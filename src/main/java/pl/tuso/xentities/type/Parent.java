package pl.tuso.xentities.type;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import pl.tuso.xentities.api.Parential;

public abstract class Parent extends IntelligentArmorStand implements Parential {
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

    @Override
    public void positionSubEntityTick() {

    }

    @Override
    public abstract IntelligentArmorStand getEntity();
}
