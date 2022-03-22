package pl.tuso.xentities.type;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.entity.ExampleEntity;
import pl.tuso.xentities.entity.TestEntity;
import pl.tuso.xentities.entity.human.Human;
import pl.tuso.xentities.entity.snail.Snail;
import pl.tuso.xentities.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class FantasticBeast {
    public static EntityType<IntelligentArmorStand> INTELLIGENT_ARMOR_STAND;
    public static EntityType<Part> PART;

    public static EntityType<ExampleEntity> EXAMPLE;
    public static EntityType<TestEntity> TEST;

    public static EntityType<Snail> SNAIL;
    public static EntityType<Snail> HUMAN;

    public static void registerTypes() {
        INTELLIGENT_ARMOR_STAND = registerEntity(IntelligentArmorStand::new, "intelligent_armor_stand", EntityType.ARMOR_STAND, EntityType.ARMOR_STAND.getWidth(), EntityType.ARMOR_STAND.getHeight());
        PART = registerEntity(Part::new, "part", EntityType.ARMOR_STAND, 0.0F, 0.0F);

        EXAMPLE = registerEntity(ExampleEntity::new, "example", EntityType.ARMOR_STAND, EntityType.ARMOR_STAND.getWidth(), EntityType.ARMOR_STAND.getHeight());
        TEST = registerEntity(TestEntity::new, "test", EntityType.ARMOR_STAND, EntityType.ARMOR_STAND.getWidth(), EntityType.ARMOR_STAND.getHeight());

        SNAIL = registerEntity(Snail::new, "snail", EntityType.ARMOR_STAND, 0.5F, 0.5F);
        HUMAN = registerEntity(Human::new, "human", EntityType.ARMOR_STAND, EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight());
    }

    private static @NotNull EntityType registerEntity(EntityType.EntityFactory entityFactory, String name, EntityType<?> model, float width, float height) {
        unfreezeRegistry();
        EntityType intelligentType = Registry.ENTITY_TYPE.registerMapping(Registry.ENTITY_TYPE.getId(model),
                ResourceKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("minecraft", name)),
                new EntityType<>(entityFactory, model.getCategory(), true, model.canSerialize(), model.canSummon(),
                        model.fireImmune(), ImmutableSet.of(), EntityDimensions.scalable(width, height),
                        model.clientTrackingRange(), model.updateInterval()), Lifecycle.stable()).value();
        setDefaultAttributes(intelligentType);
        return intelligentType;
    }

    private static void setDefaultAttributes(EntityType type) {
        try {
            final Field suppliers = DefaultAttributes.class.getDeclaredField("b");
            suppliers.setAccessible(true);
            Map<EntityType<?>, AttributeSupplier> attributeSupplierMap = new HashMap<>((Map<EntityType<?>, AttributeSupplier>)suppliers.get(null));
            attributeSupplierMap.put(type, Mob.createMobAttributes().build());
            ReflectionUtil.setField(suppliers, null, attributeSupplierMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void unfreezeRegistry() {
        Class<MappedRegistry> registryClass = MappedRegistry.class;
        try {
            Field intrusiveHolderCache = registryClass.getDeclaredField("bN");
            intrusiveHolderCache.setAccessible(true);
            intrusiveHolderCache.set(Registry.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
            Field frozen = registryClass.getDeclaredField("bL");
            frozen.setAccessible(true);
            frozen.set(Registry.ENTITY_TYPE, false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
    }
}