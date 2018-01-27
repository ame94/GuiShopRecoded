package ru.blc.guishop.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import ru.blc.guishop.GuiShopRecoded;
import ru.blc.guishop.Trader;
import ru.blc.guishop.events.GuiShopClickMainInventoryEvent;
import ru.blc.guishop.events.GuiShopClickTabEvent;
import ru.blc.guishop.events.GuiShopClickTabsListEvent;
import ru.blc.guishop.gui.ShopInventory.OperationType;

public class Handler implements Listener {
    public Handler() {
        GuiShopRecoded.instance().getServer().getPluginManager().registerEvents(this, GuiShopRecoded.instance());
    }

    @EventHandler
    public void onGuiMain(GuiShopClickMainInventoryEvent e) {
        if(e.getSlot() == 0) {
            GUI.openTabsList((Player)e.getWhoClicked(), 1, OperationType.BUY);
        }

        if(e.getSlot() == 1) {
            GUI.openTabsList((Player)e.getWhoClicked(), 1, OperationType.SELL);
        }

        GuiShopRecoded.instance();
        if(e.getSlot() == 3 && GuiShopRecoded.getMain().getBoolean("vipmenu1.Show")) {
            GUI.openVip((Player)e.getWhoClicked(), 1, 1);
        }

        GuiShopRecoded.instance();
        if(e.getSlot() == 4 && GuiShopRecoded.getMain().getBoolean("vipmenu2.Show")) {
            GUI.openVip((Player)e.getWhoClicked(), 2, 1);
        }

        GuiShopRecoded.instance();
        if(e.getSlot() == 5 && GuiShopRecoded.getMain().getBoolean("vipmenu3.Show")) {
            GUI.openVip((Player)e.getWhoClicked(), 3, 1);
        }

    }

    @EventHandler
    public void onGuiTabsList(GuiShopClickTabsListEvent e) {
        if(e.getCurrentItem() != null) {
            if(e.getCurrentItem().getType() != Material.AIR) {
                if(e.getSlot() == 0) {
                    GUI.openMain((Player)e.getWhoClicked());
                } else if(e.getSlot() == 18) {
                    GUI.openTabsList((Player)e.getWhoClicked(), e.getPage() - 1, e.getOperationType());
                } else if(e.getSlot() == 27) {
                    GUI.openTabsList((Player)e.getWhoClicked(), e.getPage() + 1, e.getOperationType());
                } else if(e.getSlot() % 9 != 0 && e.getSlot() % 9 != 1) {
                    GUI.openTabFromList((Player)e.getWhoClicked(), e.getOperationType(), e.getPage(), e.getSlot());
                }
            }
        }
    }

    @EventHandler
    public void onGuiTab(GuiShopClickTabEvent e) {
        if(e.getCurrentItem() != null) {
            if(e.getCurrentItem().getType() != Material.AIR) {
                if(e.getSlot() == 0) {
                    if(e.getTab().isVip()) {
                        GUI.openMain((Player)e.getWhoClicked());
                    } else {
                        GUI.openTabsList((Player)e.getWhoClicked(), 1, e.getOperationType());
                    }

                } else if(e.getSlot() == 18) {
                    GUI.openTab((Player)e.getWhoClicked(), e.getTab(), e.getPage() - 1, e.getOperationType());
                } else if(e.getSlot() == 27) {
                    GUI.openTab((Player)e.getWhoClicked(), e.getTab(), e.getPage() + 1, e.getOperationType());
                } else if(e.getSlot() % 9 != 0 && e.getSlot() % 9 != 1) {
                    if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                        int amount = e.getTab().isVip()?1:(e.getClick() == ClickType.LEFT?1:(e.getClick() == ClickType.RIGHT?16:64));
                        if(e.getOperationType() == OperationType.SELL) {
                            Trader.sellItem((Player)e.getWhoClicked(), e.getCurrentItem(), amount, e.getTab());
                        }

                        if(e.getOperationType() == OperationType.BUY) {
                            Trader.buyItem((Player)e.getWhoClicked(), e.getCurrentItem(), amount, e.getTab());
                        }

                    }
                }
            }
        }
    }
}
