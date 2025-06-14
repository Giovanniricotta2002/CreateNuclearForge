package net.nuclearteam.createnuclear.content.contraptions.irradiated.chicken;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedChickenRenderer extends MobRenderer<IrradiatedChicken, IrradiatedChickenModel<IrradiatedChicken>> {
    private static final ResourceLocation IRRADIATED_CHICKEN_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_chicken.png");

    public IrradiatedChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedChickenModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CHICKEN)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedChicken entity) {
        return IRRADIATED_CHICKEN_LOCATION;
    }

    @Override
    protected float getBob(IrradiatedChicken livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float g = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * g;
    }
}