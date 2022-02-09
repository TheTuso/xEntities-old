package pl.tuso.xentities.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.Level;
import pl.tuso.xentities.api.Hitbox;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class MainHitbox extends Turtle implements Hitbox {
    private IntelligentArmorStand owner;

    public MainHitbox(IntelligentArmorStand owner, Level world) {
        super(EntityType.TURTLE, world);
        this.owner = owner;
        this.setNoAi(true);
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public void setOwner(IntelligentArmorStand owner) {
        this.owner = owner;
    }

    @Override
    public IntelligentArmorStand getOwner() {
        return this.owner;
    }

    @Override
    public LivingEntity getEntity() {
        return this;
    }
}
