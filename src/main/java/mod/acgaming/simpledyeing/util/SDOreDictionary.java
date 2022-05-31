package mod.acgaming.simpledyeing.util;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class SDOreDictionary
{
    public static void init()
    {
        OreDictionary.registerOre("carpet", new ItemStack(Blocks.CARPET, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockGlassColored", new ItemStack(Blocks.STAINED_GLASS, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("paneGlassColored", new ItemStack(Blocks.STAINED_GLASS_PANE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("terracotta", new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("concrete", new ItemStack(Blocks.CONCRETE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("concretePowder", new ItemStack(Blocks.CONCRETE_POWDER, 1, OreDictionary.WILDCARD_VALUE));

        for (int i = 0; i < SDArrays.arrayGlazedTerracotta.length; ++i)
        {
            OreDictionary.registerOre("terracottaGlazed", new ItemStack(SDArrays.arrayGlazedTerracotta[i]));
        }

        for (int i = 0; i < SDArrays.arrayShulkerBoxes.length; ++i)
        {
            OreDictionary.registerOre("shulkerBox", new ItemStack(SDArrays.arrayShulkerBoxes[i]));
        }

        MinecraftForge.EVENT_BUS.register(SDOreDictionary.class);
    }
}