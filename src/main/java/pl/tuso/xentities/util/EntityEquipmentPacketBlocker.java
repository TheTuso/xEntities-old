package pl.tuso.xentities.util;

import io.netty.channel.*;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityEquipmentPacketBlocker {
    private final int ID;

    public EntityEquipmentPacketBlocker(@NotNull Entity entity) {
        this.ID = entity.getId();
    }

    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    public void injectPlayer(@NotNull Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
            }
            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if(packet instanceof ClientboundSetEquipmentPacket equipmentPacket){
                    if (equipmentPacket.getEntity() == ID) {
                        return;
                    }
                }
                super.write(channelHandlerContext, packet, channelPromise);
            }
        };
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        if (pipeline.context(player.getName()) == null) {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        }
    }
}
