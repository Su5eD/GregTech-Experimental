package mods.gregtechmod.objects.items;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ICustomItemModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;

public class ItemTranslatableBook extends Item implements ICustomItemModel {

    public ItemTranslatableBook() {
        setRegistryName("translatable_book");
        setMaxStackSize(1);
    }

    public static boolean validUnlocalizedBookTagContents(NBTTagCompound nbt) {
        if (!ItemWritableBook.isNBTValid(nbt) || !nbt.hasKey("title", 8)) {
            return false;
        }
        else {
            String titleKey = nbt.getString("title");
            String title = I18n.format(titleKey);
            return title != null && title.length() <= 32 && nbt.hasKey("author", 8);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            String title = nbt.getString("title");

            if (!StringUtils.isNullOrEmpty(title)) {
                return I18n.format(title);
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            String author = nbt.getString("author");
            if (!StringUtils.isNullOrEmpty(author)) {
                tooltip.add(TextFormatting.GRAY + I18n.format("book.byAuthor", author));
            }
            tooltip.add(TextFormatting.GRAY + I18n.format("book.generation." + nbt.getInteger("generation")));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            ItemStack translated = getTranslatedBook(stack);
            GregTechMod.runProxy(clientProxy -> clientProxy.openWrittenBook(translated));
        }
        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private ItemStack getTranslatedBook(ItemStack stack) {
        ItemStack copy = stack.copy();
        NBTTagCompound nbt = copy.getTagCompound();
        if (nbt != null && validUnlocalizedBookTagContents(nbt)) {
            String title = I18n.format(nbt.getString("title"));
            nbt.setString("title", title);
            
            NBTTagList list = nbt.getTagList("pages", 8);
            String[] args = getTranslationArgs(nbt);

            for (int i = 0; i < list.tagCount(); ++i) {
                String key = list.getStringTagAt(i);
                String page = '\"' + I18n.format(key, (Object[]) args) + '\"';
                list.set(i, new NBTTagString(page));
            }

            nbt.setTag("pages", list);
        }
        return copy;
    }
    
    private String[] getTranslationArgs(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("translationArgs", 8);
        return list != null ? StreamSupport.stream(list.spliterator(), false)
            .map(tag -> ((NBTTagString) tag).getString())
            .toArray(String[]::new)
            : new String[0];
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public ResourceLocation getItemModel() {
        return GtUtil.getModelResourceLocation("translatable_book", null);
    }
}
