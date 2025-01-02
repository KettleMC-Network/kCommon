package net.kettlemc.kcommon.bukkit.itemstack;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// This is inspired by the ItemstackBuilder by lucko, licensed under the MIT License (https://github.com/lucko/helper/blob/master/LICENSE.txt)
public final class ItemBuilder {
    private static final ItemFlag[] HIDDEN_FLAGS = new ItemFlag[]{
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS,
            ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON
    };

    private final ItemStack itemStack;

    public static ItemBuilder of(@NotNull Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    public static ItemBuilder of(@NotNull XMaterial material) {
        return new ItemBuilder(Objects.requireNonNull(material.parseItem()));
    }

    public static ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder copyOf(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack.clone());
    }

    private ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder meta(@NotNull Consumer<ItemMeta> meta) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        meta.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder transform(@NotNull Consumer<ItemStack> transformer) {
        transformer.accept(itemStack);
        return this;
    }

    public ItemBuilder name(@NotNull String name, boolean legacyColor) {
        return meta(meta -> meta.setDisplayName(legacyColor ? ChatColor.translateAlternateColorCodes('&', name) : name));
    }

    public ItemBuilder name(@NotNull String name) {
        return name(name, false);
    }

    public void colorName() {
        meta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(meta.getDisplayName()))));
    }

    public void colorLore() {
        meta(meta -> {
            List<String> lore = meta.getLore();
            if (lore != null) {
                meta.setLore(lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            }
        });
    }

    public ItemBuilder material(@NotNull Material material) {
        return transform(itemStack -> itemStack.setType(material));
    }

    public ItemBuilder addLore(@NotNull String line) {
        return meta(meta -> {
            List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            lore.add(line);
            meta.setLore(lore);
        });
    }

    public ItemBuilder addLore(@NotNull Collection<String> lines) {
        return meta(meta -> {
            List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            lore.addAll(lines);
            meta.setLore(lore);
        });
    }

    public ItemBuilder addLore(@NotNull String @NotNull ... lines) {
        return addLore(Arrays.asList(lines));
    }

    public ItemBuilder addLore(@NotNull Iterable<@NotNull String> lines) {
        lines.forEach(this::addLore);
        return this;
    }

    public ItemBuilder clearLore() {
        return meta(meta -> meta.setLore(null));
    }

    public ItemBuilder lore(@NotNull String line) {
        return clearLore().addLore(line);
    }

    public ItemBuilder lore(@NotNull Collection<@NotNull String> lines) {
        return clearLore().addLore(lines);
    }

    public ItemBuilder lore(@NotNull String @NotNull ... lines) {
        return clearLore().addLore(lines);
    }

    public ItemBuilder lore(@NotNull Iterable<@NotNull String> lines) {
        return clearLore().addLore(lines);
    }

    public ItemBuilder durability(int durability) {
        return transform(itemStack -> itemStack.setDurability((short) durability));
    }

    public ItemBuilder data(int data) {
        return durability(data);
    }

    public ItemBuilder amount(int amount) {
        return transform(itemStack -> itemStack.setAmount(amount));
    }

    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        return transform(itemStack -> itemStack.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder enchant(@NotNull Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(@NotNull Map<Enchantment, Integer> enchantments) {
        return transform(itemStack -> itemStack.addUnsafeEnchantments(enchantments));
    }

    public ItemBuilder unenchant(@NotNull Enchantment enchantment) {
        return transform(itemStack -> itemStack.removeEnchantment(enchantment));
    }

    public ItemBuilder unenchant(@NotNull List<@NotNull Enchantment> enchantments) {
        return transform(itemStack -> enchantments.forEach(itemStack::removeEnchantment));
    }

    public ItemBuilder unenchant() {
        return transform(itemStack -> itemStack.getEnchantments().keySet().forEach(itemStack::removeEnchantment));
    }

    public ItemBuilder flag(ItemFlag... flags) {
        return meta(meta -> meta.addItemFlags(flags));
    }

    public ItemBuilder unflag(ItemFlag... flags) {
        return meta(meta -> meta.removeItemFlags(flags));
    }

    public ItemBuilder hideAttributes() {
        return flag(HIDDEN_FLAGS);
    }

    public ItemBuilder showAttributes() {
        return unflag(HIDDEN_FLAGS);
    }

    public ItemBuilder leatherColor(Color color) {
        return meta(meta -> {
            if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(color);
            }
        });
    }

    public ItemBuilder skullOwner(String owner) {
        return meta(meta -> {
            if (meta instanceof SkullMeta) {
                ((SkullMeta) meta).setOwner(owner);
            }
        });
    }

    public ItemBuilder unbreakable() {
        return meta(meta -> meta.spigot().setUnbreakable(true));
    }

    public ItemBuilder breakable() {
        return meta(meta -> meta.spigot().setUnbreakable(false));
    }

    public ItemStack build() {
        return this.itemStack;
    }


}