package pl.tuso.xentities.api;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Part;

import java.util.List;

public interface Parential {
    //Parts
    void spawnParts(Level level);

    void spawnPart(@NotNull Level level, Part part);

    void removeParts();

    void removePart(@NotNull Part part);

    List getParts();
    //Hitboxes
    void initHitboxes();

    void addHitboxes(Level level);

    void addHitbox(@NotNull Level level, @NotNull Hitbox hitbox);

    void removeHitboxes();

    void removeHitbox(@NotNull Hitbox hitbox);

    List getHitboxes();

    void positionSubEntityTick();

    IntelligentArmorStand getEntity();
}
