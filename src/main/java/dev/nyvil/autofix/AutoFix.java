package dev.nyvil.autofix;

import dev.nyvil.autofix.client.DurabilityCheckListener;
import dev.nyvil.autofix.client.notifications.NotificationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AutoFix.MOD_ID)
public class AutoFix {


    public static final String MOD_ID = "autofix";

    private static AutoFix instance;
    private final NotificationManager notificationManager;

    public AutoFix() {
        instance = this;
        notificationManager = new NotificationManager();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.register(new DurabilityCheckListener());
            MinecraftForge.EVENT_BUS.register(notificationManager);
        });
    }


    public static AutoFix getInstance() {
        return instance;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
