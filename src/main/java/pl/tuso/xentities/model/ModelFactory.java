package pl.tuso.xentities.model;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.tuso.xentities.api.Model;

public class ModelFactory extends ItemStack implements Model {
    private final Material type;
    private final int modelData;

    public ModelFactory(Material type, int modelData) {
        super(type);
        this.type = type;
        this.modelData = modelData;

        ItemMeta meta = this.getItemMeta();
        meta.setCustomModelData(modelData);
        this.setItemMeta(meta);
    }

    @Override
    public int getModelData() {
        return this.modelData;
    }

    @Override
    public Material getModelType() {
        return this.type;
    }

    @Override
    public net.minecraft.world.item.ItemStack getNMSCopy() {
        return CraftItemStack.asNMSCopy(this);
    }
}
