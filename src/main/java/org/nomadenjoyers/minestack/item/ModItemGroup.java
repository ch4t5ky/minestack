package org.nomadenjoyers.minestack.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup MINESTACK_GROUP = new ItemGroup("minestackModTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.VIRTUAL_MACHINE.get());
        }
    };
}
