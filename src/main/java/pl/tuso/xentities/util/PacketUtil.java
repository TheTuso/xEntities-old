package pl.tuso.xentities.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class PacketUtil {
    public static void sendPackets(Packet<?> @NotNull ... packets) {
        for (Packet packet : packets) {
            Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().connection.send(packet));
        }
    }

    public static void sendPackets(Player player, Packet<?> @NotNull ... packets) {
        for (Packet packet : packets) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

    public static @NotNull ClientboundTeleportEntityPacket teleportWithPackets(Entity entity, double x, double y, double z) {
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        try {
            final Field xField = teleportEntityPacket.getClass().getDeclaredField("b");
            final Field yField = teleportEntityPacket.getClass().getDeclaredField("c");
            final Field zField = teleportEntityPacket.getClass().getDeclaredField("d");
            xField.setAccessible(true);
            yField.setAccessible(true);
            zField.setAccessible(true);
            xField.setDouble(teleportEntityPacket, x);
            yField.setDouble(teleportEntityPacket, y);
            zField.setDouble(teleportEntityPacket, z);
            xField.setAccessible(false);
            yField.setAccessible(false);
            zField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return teleportEntityPacket;
    }

    public static @NotNull ClientboundTeleportEntityPacket teleportWithPackets(Entity entity, double x, double y, double z, float yRot, float xRot) {
        ClientboundTeleportEntityPacket teleportEntityPacket = teleportWithPackets(entity, x, y, z);
        byte byteYRot = (byte)((int)(yRot * 256.0F / 360.0F));
        byte byteXRot = (byte)((int)(xRot * 256.0F / 360.0F));
        try {
            final Field yRotField = teleportEntityPacket.getClass().getDeclaredField("e");
            final Field xRotField = teleportEntityPacket.getClass().getDeclaredField("f");
            yRotField.setAccessible(true);
            xRotField.setAccessible(true);
            yRotField.setByte(teleportEntityPacket, byteYRot);
            xRotField.setByte(teleportEntityPacket, byteXRot);
            yRotField.setAccessible(false);
            xRotField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return teleportEntityPacket;
    }
}