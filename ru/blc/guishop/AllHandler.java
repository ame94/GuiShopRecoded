package ru.blc.guishop;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.blc.guishop.events.MainHandler;
import ru.blc.guishop.gui.GUI;
import ru.blc.guishop.gui.Handler;
import ru.blc.guishop.utils.ItemMetaUtil;
import ru.blc.guishop.utils.SU;

public class AllHandler implements Listener {
    public AllHandler() {
        GuiShopRecoded.instance().getServer().getPluginManager().registerEvents(this, GuiShopRecoded.instance());
        new Handler();
        new MainHandler();
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onSpawnerPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.MOB_SPAWNER) {
            CreatureSpawner Sp = (CreatureSpawner)e.getBlock().getState();
            Sp.setCreatureTypeByName(getCreatureType(e.getPlayer().getInventory().getItemInMainHand()));
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onSpawnerMove(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if(e.getInventory() != null) {
            if(MainHandler.getClickedInventory(e) != null) {
                if(!GUI.getInventories().containsKey(MainHandler.getClickedInventory(e))) {
                    if(e.getCurrentItem() != null && e.getCurrentItem().getData().getItemType() != Material.AIR) {
                        if(p.getGameMode() == GameMode.CREATIVE && e.getCurrentItem().getType() == Material.MOB_SPAWNER) {
                            ((Player)e.getWhoClicked()).sendMessage(SU.genMessage("§cSorry, but moving Mobspawner in creative gamemode is unavalible!"));
                            p.updateInventory();
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    public static String getCreatureType(ItemStack item) {
        try {
            Object nmsStack = ItemMetaUtil.getCraftbukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke((Object)null, new Object[]{item});
            Object tag = ((Boolean)nmsStack.getClass().getMethod("hasTag", new Class[0]).invoke(nmsStack, new Object[0])).booleanValue()?nmsStack.getClass().getMethod("getTag", new Class[0]).invoke(nmsStack, new Object[0]):ItemMetaUtil.getNmsClass("NBTTagCompound").newInstance();
            Object tagg = tag.getClass().getMethod("get", new Class[]{String.class}).invoke(tag, new Object[]{"BlcSpawnData"});
            tagg = tagg.getClass().getMethod("get", new Class[]{String.class}).invoke(tagg, new Object[]{"Entity"});
            return tagg.toString().replace("\"", "");
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | ClassNotFoundException | NullPointerException | IllegalAccessException var4) {
            return "";
        }
    }
}
