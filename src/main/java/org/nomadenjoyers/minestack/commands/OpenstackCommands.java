package org.nomadenjoyers.minestack.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nomadenjoyers.minestack.MineStack;
import org.nomadenjoyers.minestack.openstack.Keystone;
import org.nomadenjoyers.minestack.openstack.Nova;

import java.util.UUID;

public class OpenstackCommands {

    public OpenstackCommands(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("minestack")
                .then(Commands.literal("auth")
                  .then(Commands.argument("username", StringArgumentType.string())
                    .then(Commands.argument("password", StringArgumentType.string())
                      .executes(command -> getKeystoneKey(command.getSource(),StringArgumentType.getString(command, "username"), StringArgumentType.getString(command,"password")))
                    )
                  )
                )
                .then(Commands.literal("vm")
                        .executes(command -> createVM(command.getSource()))
                )
        );
    }
    private int getKeystoneKey(CommandSource source, String username, String password) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();

        String token = Keystone.getToken(username, password);

        if (token.equals("")) {
            source.sendFailure(new StringTextComponent("Failed to get token from keystone"));
            return 1;
        }

        player.getPersistentData().putString(MineStack.MOD_ID + "_keystone_key", token);
        source.sendSuccess(new StringTextComponent("Keystone key successfully added"), true);
        return 1;
    }

    private int createVM(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();


        String key =  UUID.randomUUID().toString();
        String token = player.getPersistentData().get(MineStack.MOD_ID + "_keystone_key").toString() ;

        String vmID = Nova.createVm(token, "minestack-pig-"+key);

        CompoundNBT compoundTag = new CompoundNBT();
        compoundTag.putString("id", "minecraft:pig");

        source.sendSuccess(new StringTextComponent("Spawn pig"), true);

        Entity entity = EntityType.loadEntityRecursive(compoundTag, source.getLevel(), (e) -> {
            e.moveTo(player.getX(), player.getY(), player.getZ() + 1);
            return e;
        });

        entity.setCustomName(new StringTextComponent("minestack-pig-"+key));
        entity.getPersistentData().putString(MineStack.MOD_ID+"_vm", vmID);

        source.getLevel().addFreshEntity(entity);
        return 1;
    }



}
