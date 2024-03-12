package org.nomadenjoyers.minestack.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.nomadenjoyers.minestack.MineStack;

public class GetOpenstackImagesCommand {
    public GetOpenstackImagesCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("minestack")
                        .then(Commands.literal("get")
                                .then(Commands.literal("images"))
                                .executes((command) -> {
            return getAvailableImages(command.getSource());
        })));
    }

    private int getAvailableImages(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        String key = player.getPersistentData().getString(MineStack.MOD_ID + "_keystone_key");

        source.sendSuccess(new StringTextComponent("Registered key is " + key), true);
        return 1;
    }
}
