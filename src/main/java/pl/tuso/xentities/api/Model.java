package pl.tuso.xentities.api;

import net.minecraft.world.item.ItemStack;

public interface Model {
    int getModelData();

    ItemStack getNMSCopy();
}
