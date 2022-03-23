package pl.tuso.xentities.entity.snail;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.Rotations;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.util.Vector;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.model.ModelFactory;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;

import javax.annotation.Nullable;

public class Snail extends Parent {
    public final ModelFactory SNAIL_MODEL = new ModelFactory(Material.PAPER, 1);
    public final ModelFactory SNAIL_SHELL_MODEL = new ModelFactory(Material.PAPER, 2);
    public final ModelFactory SNAIL_TINTED_MODEL = new ModelFactory(Material.PAPER, 3);
    public final ModelFactory SNAIL_SHELL_TINTED_MODEL = new ModelFactory(Material.PAPER, 4);
    public final ModelFactory SNAIL_SHELL_MISC_ITEM_MODEL = new ModelFactory(Material.PAPER, 5, net.kyori.adventure.text.Component.text("Snail Shell").decoration(TextDecoration.ITALIC, false));

    private boolean knocked = false;
    private boolean hidden = false;

    public Snail(EntityType<? extends Snail> entitytypes, Level world) {
        super(entitytypes, world);
        this.getBukkitEntity().customName(Component.text("Snail"));
        this.setDisplacementY(-1.25F);
        this.setMovementSpeed(0.1D);
        this.setMaxBaseHealth(4.0D);
        this.setKnockbackResistance(1.0D);
        this.setItemSlot(EquipmentSlot.HEAD, SNAIL_MODEL.getNMSCopy());
        this.setInvisible(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public void hurtModel() {
        int currentModel = this.getItemBySlot(EquipmentSlot.HEAD).asBukkitCopy().getItemMeta().getCustomModelData();
        if (currentModel == 1) {
            this.setItemSlot(EquipmentSlot.HEAD, SNAIL_TINTED_MODEL.getNMSCopy());
        } else if (currentModel == 2) {
            this.setItemSlot(EquipmentSlot.HEAD, SNAIL_SHELL_TINTED_MODEL.getNMSCopy());
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(XEntities.getInstance(), () -> {
            if (isAlive()) {
                this.setItemSlot(EquipmentSlot.HEAD, new ModelFactory(Material.PAPER, currentModel).getNMSCopy());
            }
        }, 10);
    }

    public void setKnocked(boolean knocked) {
        this.knocked = knocked;
    }

    public boolean isKnocked() {
        return this.knocked;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        if (hidden) {
            this.setItemSlot(EquipmentSlot.HEAD, SNAIL_SHELL_MODEL.getNMSCopy());
        } else {
            this.setItemSlot(EquipmentSlot.HEAD, SNAIL_MODEL.getNMSCopy());
            this.setMovementSpeed(0.1D);
            this.getBukkitEntity().setVelocity(new Vector(0.0D, 0.75D, 0.0D).multiply(0.5D));
            this.setHeadPose(new Rotations(0.0F, this.getHeadPose().getY(), this.getHeadPose().getZ()));
            this.setKnocked(false);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(this.getBukkitEntity().getLocation(), Sound.BLOCK_COMPOSTER_EMPTY, 0.25F, 1.0F + random.nextFloat()));
        }
    }

    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return null;
    }

    @Override
    @Nullable
    public SoundEvent getDeathSound() {
        return SoundEvents.SLIME_BLOCK_BREAK;
    }

    @Override
    public IntelligentArmorStand getEntity() {
        return this;
    }
}
