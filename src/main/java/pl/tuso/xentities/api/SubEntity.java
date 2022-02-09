package pl.tuso.xentities.api;

import net.minecraft.world.entity.LivingEntity;
import pl.tuso.xentities.type.IntelligentArmorStand;

public interface SubEntity {
    void setOwner(IntelligentArmorStand owner);

    IntelligentArmorStand getOwner();

    LivingEntity getEntity();
}
