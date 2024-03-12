package org.nomadenjoyers.minestack.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.nomadenjoyers.minestack.MineStack;
import org.nomadenjoyers.minestack.commands.AddKeystoneKeyCommand;
import org.nomadenjoyers.minestack.commands.GetOpenstackImagesCommand;

@Mod.EventBusSubscriber(modid = MineStack.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new AddKeystoneKeyCommand(event.getDispatcher());
        new GetOpenstackImagesCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
