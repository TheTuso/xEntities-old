package pl.tuso.xentities.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.api.Hitbox;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;

public class MainHitbox extends Turtle implements Hitbox {
    private IntelligentArmorStand owner;

    public MainHitbox(@NotNull Parent owner, Level world) {
        super(EntityType.TURTLE, world);
        this.owner = owner;
        owner.getHitboxes().add(this);
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
