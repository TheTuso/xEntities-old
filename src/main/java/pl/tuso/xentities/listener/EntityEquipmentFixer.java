package pl.tuso.xentities.listener;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.util.EntityEquipmentPacketBlocker;

import java.util.HashMap;

public class EntityEquipmentFixer implements Listener {
    private HashMap<EquipmentSlot, ItemStack> equipment = new HashMap<>();
    private final ItemStack EMPTY = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR));

    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof IntelligentArmorStand armorStand) {
            saveEquipment(armorStand);
            EntityEquipmentPacketBlocker listener = new EntityEquipmentPacketBlocker(armorStand);
            Bukkit.getOnlinePlayers().forEach(player -> listener.injectPlayer(player));
            Bukkit.getScheduler().scheduleSyncDelayedTask(XEntities.getInstance(), () -> {
                Bukkit.getOnlinePlayers().forEach(player -> listener.removePlayer(player));
                loadEquipment(armorStand);
            }, 2);
        }
    }

    private void saveEquipment(IntelligentArmorStand armorStand) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            equipment.put(slot, armorStand.getItemBySlot(slot));
            armorStand.setItemSlot(slot, EMPTY);
        }
    }

    private void loadEquipment(IntelligentArmorStand armorStand) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            armorStand.setItemSlot(slot, equipment.get(slot));
        }
    }
}
