package mod.acgaming.simpledyeing.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.acgaming.simpledyeing.SimpleDyeing;

@Config(modid = SimpleDyeing.MOD_ID, name = "SimpleDyeing")
public class SDConfig
{
    @Config.Name("Sounds")
    @Config.Comment("Play sounds when dyeing and cleaning in-world.")
    public static boolean sdSounds = true;

    @Config.Name("Particles")
    @Config.Comment("Spawn particles when dyeing and cleaning in-world.")
    public static boolean sdParticles = true;

    @Config.Name("Sneaking")
    @Config.Comment("If sneaking is required to perform dyeing or cleaning.")
    public static boolean sdSneaking = false;

    @Mod.EventBusSubscriber(modid = SimpleDyeing.MOD_ID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(SimpleDyeing.MOD_ID))
            {
                ConfigManager.sync(SimpleDyeing.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}