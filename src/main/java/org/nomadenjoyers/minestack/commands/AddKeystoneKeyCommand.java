package org.nomadenjoyers.minestack.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.gen.Heightmap;
import org.nomadenjoyers.minestack.MineStack;

public class AddKeystoneKeyCommand {

    public AddKeystoneKeyCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("minestack").then(Commands.literal("set").executes((command) -> {
            return setKeystoneKey(command.getSource());
        })));
    }

    private int setKeystoneKey(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        String info = "Add keystone key";

        String keystoneKey = java.util.UUID.randomUUID().toString();

        player.getPersistentData().putString(MineStack.MOD_ID + "_keystone_key", keystoneKey);

        source.sendSuccess(new StringTextComponent(info), true);
        return 1;
    }

}
