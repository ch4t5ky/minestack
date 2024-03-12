package org.nomadenjoyers.minestack.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.nomadenjoyers.minestack.MineStack;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MineStack.MOD_ID);

    public static final RegistryObject<ForgeSpawnEggItem> VIRTUAL_MACHINE = ITEMS.register("virtual_machine",
            () -> new ForgeSpawnEggItem(EntityType.PIG.delegate, 0xFF55AA, 0x27da9f, new Item.Properties().tab(ModItemGroup.MINESTACK_GROUP)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
