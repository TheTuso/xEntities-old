package pl.tuso.xentities.api;

import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;

public interface Model {
    int getModelData();

    Material getModelType();

    ItemStack getNMSCopy();
}
