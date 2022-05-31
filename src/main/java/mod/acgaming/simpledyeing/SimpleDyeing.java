package mod.acgaming.simpledyeing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import mod.acgaming.simpledyeing.util.SDOreDictionary;

@Mod(modid = SimpleDyeing.MOD_ID, name = SimpleDyeing.NAME, version = SimpleDyeing.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class SimpleDyeing
{
    public static final String MOD_ID = "simpledyeing";
    public static final String NAME = "Simple Dyeing";
    public static final String VERSION = "1.12.2-1.0.0";
    public static final Logger LOGGER = LogManager.getLogger("SD");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        SDOreDictionary.init();
        LOGGER.info("Simple Dyeing initialized");
    }
}