package pl.tuso.xentities.type;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.api.Partial;

public class Part extends IntelligentArmorStand implements Partial {
    private IntelligentArmorStand owner = this;

    public Part(EntityType<? extends Part> entitytypes, Level world) {
        super(entitytypes, world);
        this.persist = false;
        this.collides = false;
        this.setNoGravity(true);
        this.setKnockbackResistance(1.0D);
    }

    public Part(@NotNull Parent owner, EntityType<? extends Part> entitytypes, Level world) {
        this(entitytypes, world);
        this.owner = owner;
        owner.getParts().add(this);
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
    public IntelligentArmorStand getEntity() {
        return this;
    }
}
