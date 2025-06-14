package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IrradiatedCatRenderer extends MobRenderer<IrradiatedCat, IrradiatedCatModel<IrradiatedCat>> {
    private static final ResourceLocation IRRADIATED_CAT_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_cat.png");

    public IrradiatedCatRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCatModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CAT)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedCat entity) {
        return IRRADIATED_CAT_LOCATION;
    }
    protected void scale(IrradiatedCat livingEntity, PoseStack matrixStack, float partialTickTime) {
        super.scale(livingEntity, matrixStack, partialTickTime);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    protected void setupRotations(IrradiatedCat entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);

        float f = entity.getLieDownAmount(partialTick);
        if (f > 0.0F) {
            poseStack.translate(0.4F * f, 0.15F * f, 0.1F * f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(f, 0.0F, 90.0F)));
            BlockPos blockPos = entity.blockPosition();
            List<Player> list = entity.level().getEntitiesOfClass(Player.class, (new AABB(blockPos)).inflate(2.0, 2.0, 2.0));

            for (Player player : list) {
                if (player.isSleeping()) {
                    poseStack.translate(0.15F * f, 0.0F, 0.0F);
                    break;
                }
            }
        }

    }
}
