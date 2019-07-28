package com.bgsoftware.superiorskyblock.menu;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.utils.FileUtil;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public final class MemberRoleMenu extends SuperiorMenu {

    private static Inventory inventory = null;
    private static String title = "";
    private static int memberSlot, modSlot, adminSlot, leaderSlot;
    //private static Sound memberSound, modSound, adminSound, leaderSound;

    private SuperiorPlayer targetPlayer;

    private MemberRoleMenu(SuperiorPlayer targetPlayer){
        super("rolePage");
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getWhoClicked());

        if(memberSlot == e.getRawSlot()){
            Bukkit.dispatchCommand(superiorPlayer.asPlayer(), "island setrole " + targetPlayer.getName() + " member");
        }

        else if(modSlot == e.getRawSlot()){
            Bukkit.dispatchCommand(superiorPlayer.asPlayer(), "island setrole " + targetPlayer.getName() + " moderator");
        }

        else if(adminSlot == e.getRawSlot()){
            Bukkit.dispatchCommand(superiorPlayer.asPlayer(), "island setrole " + targetPlayer.getName() + " admin");
        }

        else if(leaderSlot == e.getRawSlot()){
            Bukkit.dispatchCommand(superiorPlayer.asPlayer(), "island transfer " + targetPlayer.getName());
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, inventory.getSize(), title.replace("{}", targetPlayer.getName()));
        inv.setContents(inventory.getContents());
        return inv;
    }

    public static void init(){
        MemberRoleMenu memberRoleMenu = new MemberRoleMenu(null);
        File file = new File(plugin.getDataFolder(), "guis/panel-gui.yml");

        if(!file.exists())
            FileUtil.saveResource("guis/panel-gui.yml");

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        inventory = FileUtil.loadGUI(memberRoleMenu, cfg.getConfigurationSection("roles-panel"), 5, "");
        title = ChatColor.translateAlternateColorCodes('&', cfg.getString("roles-panel.title"));

        ItemStack memberButton = FileUtil.getItemStack(cfg.getConfigurationSection("roles-panel.member-role"));
        ItemStack modButton = FileUtil.getItemStack(cfg.getConfigurationSection("roles-panel.mod-role"));
        ItemStack adminButton = FileUtil.getItemStack(cfg.getConfigurationSection("roles-panel.admin-role"));
        ItemStack leaderButton = FileUtil.getItemStack(cfg.getConfigurationSection("roles-panel.leader-role"));
        memberSlot = cfg.getInt("roles-panel.member-role.slot");
        modSlot = cfg.getInt("roles-panel.mod-role.slot");
        adminSlot = cfg.getInt("roles-panel.admin-role.slot");
        leaderSlot = cfg.getInt("roles-panel.leader-role.slot");
        memberRoleMenu.addSound(memberSlot, getSound(cfg.getConfigurationSection("roles-panel.member-role.sound")));
        memberRoleMenu.addSound(modSlot, getSound(cfg.getConfigurationSection("roles-panel.mod-role.sound")));
        memberRoleMenu.addSound(adminSlot, getSound(cfg.getConfigurationSection("roles-panel.admin-role.sound")));
        memberRoleMenu.addSound(leaderSlot, getSound(cfg.getConfigurationSection("roles-panel.leader-role.sound")));

        inventory.setItem(memberSlot, memberButton);
        inventory.setItem(modSlot, modButton);
        inventory.setItem(adminSlot, adminButton);
        inventory.setItem(leaderSlot, leaderButton);
    }

    public static MemberRoleMenu createInventory(SuperiorPlayer targetPlayer){
        return new MemberRoleMenu(targetPlayer);
    }

    public static void openInventory(SuperiorPlayer superiorPlayer, SuperiorMenu previousMenu, SuperiorPlayer targetPlayer){
        new MemberRoleMenu(targetPlayer).open(superiorPlayer, previousMenu);
    }

}
