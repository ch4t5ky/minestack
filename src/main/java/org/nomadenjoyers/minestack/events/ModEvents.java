package org.nomadenjoyers.minestack.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.nomadenjoyers.minestack.MineStack;
import org.nomadenjoyers.minestack.commands.OpenstackCommands;

@Mod.EventBusSubscriber(modid = MineStack.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new OpenstackCommands(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onKillRegister(LivingDeathEvent event) {
        Entity killedEntity = event.getEntity();
        Entity killer = event.getSource().getDirectEntity();
        boolean isKilledByPlayer = (killer.getType() == EntityType.PLAYER);
        if ((killedEntity.getType() == EntityType.PIG) && (isKilledByPlayer)) {
           String key = killer.getPersistentData().get(MineStack.MOD_ID + "_keystone_key").toString() ;
           Minecraft.getInstance().player.sendMessage(new StringTextComponent(killedEntity.getName().getString() + " killed  by player with" + key), Util.NIL_UUID);
       }
    }
}
