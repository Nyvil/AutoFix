package dev.nyvil.autofix.client;

import dev.nyvil.autofix.AutoFix;
import dev.nyvil.autofix.client.notifications.Notification;
import dev.nyvil.autofix.client.notifications.NotificationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DurabilityCheckListener {

    private long lastCommandTime = 0;
    private static final long COOLDOWN = 2000; // 2 seconds
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
            if (currentTime - lastCommandTime > COOLDOWN && (!(Minecraft.getInstance().screen instanceof ContainerScreen))) {
                Notification notification = new Notification("AutoFix", "Your tool has been repaired.", 200, 500, 200, 0x000000, 0xFFFFFF, 0xFFFFFF);
                player.chat("/repairall");

                notificationManager.addNotification(notification);
                lastCommandTime = currentTime;
            }
        }

    }


    @SubscribeEvent
    public void onContainerOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof ContainerScreen && event.getGui().getTitle().getString().equals("Confirm Repair All")) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(this::handleInventory, 100, TimeUnit.MILLISECONDS); // small delay to allow the inventory to load
        }
    }

    private void handleInventory() {
        ContainerScreen<?> gui = (ContainerScreen<?>) Minecraft.getInstance().screen;

        assert gui != null;
        for (Slot slot : gui.getMenu().slots) {
            if (slot.hasItem() && slot.getItem().getItem() == Items.LIME_STAINED_GLASS_PANE) {
                CClickWindowPacket packet = new CClickWindowPacket(gui.getMenu().containerId, slot.index, 0, ClickType.PICKUP, slot.getItem(), (short) 0);

                try {
                    Minecraft.getInstance().getConnection().send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Minecraft.getInstance().gameMode.handleInventoryMouseClick(gui.getMenu().containerId, slot.index, 0, ClickType.PICKUP, Minecraft.getInstance().player);
            }
        }

    }
}


