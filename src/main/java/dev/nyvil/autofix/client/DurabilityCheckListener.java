package dev.nyvil.autofix.client;

import dev.nyvil.autofix.AutoFix;
import dev.nyvil.autofix.client.notifications.Notification;
import dev.nyvil.autofix.client.notifications.NotificationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DurabilityCheckListener {

    private long lastCommandTime = 0;
    private static final long COOLDOWN = 5000; // 5 seconds
    private final NotificationManager notificationManager = AutoFix.getInstance().getNotificationManager();

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack tool = player.getMainHandItem();
        if (!tool.isDamageableItem()) {
            return;
        }

        int durability = tool.getMaxDamage() - tool.getDamageValue() - 1;
        int maxDurability = tool.getMaxDamage();
        if (durability <= maxDurability * 0.25) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastCommandTime > COOLDOWN) {
                Notification notification = new Notification("AutoFix", "Your tool has been repaired.", 200, 500, 200, 0x000000, 0xFFFFFF, 0xFFFFFF);
                player.chat("/fix");
                notificationManager.addNotification(notification);
                lastCommandTime = currentTime;
            }
        }
    }
}

