package mods.gregtechmod.common.objects.blocks;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.util.IFluidModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluid extends BlockFluidClassic implements IFluidModel {
    private final String name;

    public BlockFluid(String name, Fluid fluid, Material material) {
        super(fluid, material);
        setTranslationKey(name);
        setRegistryName(name);
        this.name = name;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerCustomMeshAndState() {
        String location = GregtechMod.MODID+":"+name;

        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> new ModelResourceLocation(location, "fluid"));

        ModelLoader.setCustomStateMapper(this, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation(location, "fluid");
            }
        });
    }
}
