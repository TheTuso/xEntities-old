package pl.tuso.xentities.entity.snail;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.model.ModelFactory;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;

public class Snail extends Parent {
    private ModelFactory snail = new ModelFactory(Material.PAPER, 1);
    private ModelFactory snail_shell = new ModelFactory(Material.PAPER, 2);
    private ModelFactory snail_tinted = new ModelFactory(Material.PAPER, 3);
    private ModelFactory snail_shell_tinted = new ModelFactory(Material.PAPER, 4);

    public Snail(EntityType<? extends Snail> entitytypes, Level world) {
        super(entitytypes, world);
        this.setCustomName(Component.nullToEmpty("Snail"));
        this.setDisplacement(-1.25F);
        this.setMovementSpeed(0.1D);
        this.setMaxBaseHealth(4.0D);
        this.setKnockbackResistance(1.0D);
        this.setItemSlot(EquipmentSlot.HEAD, snail.getNMSCopy());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public void hurtModel() {
        int currentModel = this.getItemBySlot(EquipmentSlot.HEAD).asBukkitCopy().getItemMeta().getCustomModelData();
        if (currentModel == 1) {
            this.setItemSlot(EquipmentSlot.HEAD, snail_tinted.getNMSCopy());
        } else if (currentModel == 2) {
            this.setItemSlot(EquipmentSlot.HEAD, snail_shell_tinted.getNMSCopy());
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(XEntities.getInstance(), () -> {
            if (isAlive()) {
                this.setItemSlot(EquipmentSlot.HEAD, new ModelFactory(Material.PAPER, currentModel).getNMSCopy());
            }
        }, 10);
    }

    @Override
    public IntelligentArmorStand getEntity() {
        return this;
    }
}
