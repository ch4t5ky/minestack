package org.nomadenjoyers.minestack.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import org.nomadenjoyers.minestack.MineStack;

public class CreateVMCommand {

    public CreateVMCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("minestack")
                        .then(Commands.literal("create")
                                .then(Commands.literal("vm"))
                                .executes((command) -> {
                                    return createVM(command.getSource());
                                })));
    }

    private int createVM(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        String key = player.getPersistentData().getString(MineStack.MOD_ID + "_keystone_key");

        CompoundNBT compoundTag = new CompoundNBT();
        compoundTag.putString("id", "minecraft:pig");

        source.sendSuccess(new StringTextComponent("Spawn pig"), true);

        Entity entity = EntityType.loadEntityRecursive(compoundTag, source.getLevel(), (e) -> {
            e.moveTo(player.getX(), player.getY(), player.getZ() + 1);
            return e;
        });

        entity.setCustomName(new StringTextComponent(key));

        source.getLevel().addFreshEntity(entity);
        return 1;
    }
}
