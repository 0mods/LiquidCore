package liquid.objects.container.client;

import liquid.LiquidCore;
import liquid.config.LiquidConfig;
import liquid.objects.data.scale.ScaleArray.*;
import liquid.dynamic.container.DynamicContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class DynamicScreen extends AbstractContainerScreen<DynamicContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(LiquidCore.ModId, "textures/dynamic_gui.png");
    private final int textColor = Integer.decode(LiquidConfig.colorInText);

    public DynamicScreen(DynamicContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        Size size = pMenu.getSize();
        this.imageWidth = size.width();
        this.imageHeight = size.height();
        this.titleLabelY = 7;
        this.inventoryLabelX = pMenu.getPlayerSlots(size, 0, 0).x();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        renderBgTexture(pPoseStack, new Rectangle(x, y, imageWidth, imageHeight), 0xFFFFFFFF);
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/container/hopper.png"));

        for (Slot slot : getMenu().slots) {
            this.blit(pPoseStack, x + slot.x - 1, y + slot.y - 1, 43, 19, 18, 18);
        }
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
        if (LiquidConfig.renderText.equals(true)) {
            font.draw(p_97808_, title, p_97809_, p_97810_, textColor);
            font.draw(p_97808_, playerInventoryTitle, inventoryLabelX, inventoryLabelY, textColor);
        }
    }

    public void renderBgTexture(PoseStack poseStack, Rectangle bounds, int color) {
        float alpha = ((color >> 24) & 0xFF) / 255f;
        float red = ((color >> 16) & 0xFF) / 255f;
        float green = ((color >> 8) & 0xFF) / 255f;
        float blue = (color & 0xFF) / 255f;

        RenderSystem.clearColor(red, green, blue, alpha);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = bounds.x(), y = bounds.y(), width = bounds.width(), height = bounds.height();
        int xTextureOffset = 0;
        int yTextureOffset = 66;

        this.blit(poseStack, x, y, 106 + xTextureOffset, 124 + yTextureOffset, 8, 8);
        this.blit(poseStack, x + width - 8, y, 248 + xTextureOffset, 124 + yTextureOffset, 8, 8);
        this.blit(poseStack, x, y + height - 8, 106 + xTextureOffset, 182 + yTextureOffset, 8, 8);
        this.blit(poseStack, x + width - 8, y + height - 8, 248 + xTextureOffset, 182 + yTextureOffset, 8, 8);

        Matrix4f matrix = poseStack.last().pose();

        texturedQuad(matrix, x + 8, x + width - 8, y, y + 8, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (124 + yTextureOffset) / 256f, (132 + yTextureOffset) / 256f);
        texturedQuad(matrix, x + 8, x + width - 8, y + height - 8, y + height, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (182 + yTextureOffset) / 256f, (190 + yTextureOffset) / 256f);
        texturedQuad(matrix, x, x + 8, y + 8, y + height - 8, getBlitOffset(), (106 + xTextureOffset) / 256f, (114 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
        texturedQuad(matrix, x + width - 8, x + width, y + 8, y + height - 8, getBlitOffset(), (248 + xTextureOffset) / 256f, (256 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);

        texturedQuad(matrix, x + 8, x + width - 8, y + 8, y + height - 8, getBlitOffset(), (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
    }

    private static void texturedQuad(Matrix4f matrix4f, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, (float)x0, (float)y1, (float)z).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y0, (float)z).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x0, (float)y0, (float)z).uv(u0, v0).endVertex();
        BufferUploader.draw(bufferBuilder.end());
    }
}

