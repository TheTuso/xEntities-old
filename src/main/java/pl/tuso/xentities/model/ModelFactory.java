package pl.tuso.xentities.model;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tuso.xentities.api.Model;

public class ModelFactory extends ItemStack implements Model {
    private final int MODEL_DATA;
    private final ItemMeta META = this.getItemMeta();

    public ModelFactory(Material type, int modelData, Component name) {
        this(type, modelData);

        META.displayName(name);
        this.setItemMeta(META);
    }

    public ModelFactory(Material type, int modelData) {
        super(type);
        this.MODEL_DATA = modelData;

        META.setCustomModelData(modelData);
        this.setItemMeta(META);
    }

    @Override
    public int getModelData() {
        return this.MODEL_DATA;
    }

    @Override
    public net.minecraft.world.item.ItemStack getNMSCopy() {
        return CraftItemStack.asNMSCopy(this);
    }
}
