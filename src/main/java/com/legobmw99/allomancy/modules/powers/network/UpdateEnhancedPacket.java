package com.legobmw99.allomancy.modules.powers.network;

import com.legobmw99.allomancy.modules.powers.util.AllomancyCapability;
import com.legobmw99.allomancy.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class UpdateEnhancedPacket {

    private int enhance_time;
    private int entityID;


    public UpdateEnhancedPacket(boolean enhanced,  int entityID){
        this.enhance_time = enhanced ? 1000: 0;
        this.entityID = entityID;
    }

    public UpdateEnhancedPacket(int enhance_time, int entityID){
        this.enhance_time = enhance_time;
        this.entityID = entityID;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(this.enhance_time);
        buf.writeInt(this.entityID);
    }

    public static UpdateEnhancedPacket decode(PacketBuffer buf){
        return new UpdateEnhancedPacket(buf.readInt(), buf.readInt());
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = (PlayerEntity) Minecraft.getInstance().world.getEntityByID(entityID);
            if (player != null) {
                AllomancyCapability playerCap = AllomancyCapability.forPlayer(player);
                playerCap.setEnhanced(enhance_time);

                if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER){ // Update player of own status
                    Network.sync(new UpdateEnhancedPacket(enhance_time, entityID), player);
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
