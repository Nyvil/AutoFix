package dev.nyvil.autofix.client.notifications;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationManager extends AbstractGui {

    private static final int NOTIFICATION_MARGIN_BOTTOM = 5;
    private static final int NOTIFICATION_MARGIN = 5;
    private final Queue<Notification> notifications = new ConcurrentLinkedQueue<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
        System.out.println("Notification added: " + notification.getTitle() + " - " + notification.getSubtitle());
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        if (notifications.isEmpty()) {
            return;
        }

        RenderSystem.disableDepthTest();

        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int y = screenHeight - NOTIFICATION_MARGIN - 25; // 25 to offset the notification on the y-axis

        for (Notification notification : notifications) {
            int titleWidth = Minecraft.getInstance().font.width(notification.getTitle());
            int subtitleWidth = Minecraft.getInstance().font.width(notification.getSubtitle());
            int width = Math.max(titleWidth, subtitleWidth);
            int height = Minecraft.getInstance().font.lineHeight;

            int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - width - NOTIFICATION_MARGIN;
            MatrixStack matrixStack = event.getMatrixStack();

            int color = getColor();

            // fix this, if I can ever be bothered
            //drawRoundedRect(x - NOTIFICATION_MARGIN, y - height - NOTIFICATION_MARGIN, width + 2 * NOTIFICATION_MARGIN, height + 2 * NOTIFICATION_MARGIN, 10, color);

            int textY = (y - height / 2) - 5; // 5 to slightly set the text higher
            Minecraft.getInstance().font.drawShadow(matrixStack, notification.getTitle(), x, textY, notification.getTitleColor());
            Minecraft.getInstance().font.drawShadow(matrixStack, notification.getSubtitle(), x, textY + height, notification.getSubtitleColor());

            y -= height + NOTIFICATION_MARGIN_BOTTOM + 25;

            if (y < 0) {
                break;
            }

            if (notification.getDisplayDuration() <= 0) {
                notifications.remove(notification);
            }

            notification.setDisplayDuration(notification.getDisplayDuration() - 1);
        }

        RenderSystem.enableDepthTest();
    }


    public int getColor() {
        return (int) (0.8f * 255) << 24 | 0xFFFFFF;
    }

    public void drawRoundedRect(int x, int y, int width, int height, int cornerRadius, int color) {
        float alpha = ((color >> 24) & 0xFF) / 255.0f;
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        if(alpha == 0) {
            throw new IllegalArgumentException("Alpha cannot be 0");
        }

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuilder();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(x + cornerRadius, y, 0).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(x + width - cornerRadius, y, 0).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(x + width - cornerRadius, y + height, 0).color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(x + cornerRadius, y + height, 0).color(red, green, blue, alpha).endVertex();

        drawArc(bufferBuilder, x + cornerRadius, y + cornerRadius, cornerRadius, 0, 90, red, green, blue, alpha);
        drawArc(bufferBuilder, x + width - cornerRadius, y + cornerRadius, cornerRadius, 90, 180, red, green, blue, alpha);
        drawArc(bufferBuilder, x + width - cornerRadius, y + height - cornerRadius, cornerRadius, 180, 270, red, green, blue, alpha);
        drawArc(bufferBuilder, x + cornerRadius, y + height - cornerRadius, cornerRadius, 270, 360, red, green, blue, alpha);

        bufferBuilder.end();
        WorldVertexBufferUploader.end(bufferBuilder);

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    private void drawArc(IVertexBuilder builder, int x, int y, int radius, int startAngle, int endAngle, float red, float green, float blue, float alpha) {
        for (int i = startAngle; i <= endAngle; i++) {
            double rad = i * Math.PI / 180;
            builder.vertex(x + (float) Math.cos(rad) * radius, y + (float) Math.sin(rad) * radius, 0).color(red, green, blue, alpha).endVertex();
        }
    }

}