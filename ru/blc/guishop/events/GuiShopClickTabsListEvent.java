package ru.blc.guishop.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import ru.blc.guishop.gui.GUI;
import ru.blc.guishop.gui.ShopInventory.OperationType;

public class GuiShopClickTabsListEvent extends GuiShopClickEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private OperationType operationType;

    public GuiShopClickTabsListEvent(GuiShopClickEvent event, OperationType operationType) {
        super(event);
        this.operationType = operationType;
    }

    public Inventory getInventory() {
        return this.getView().getTopInventory();
    }

    public int getPage() {
        List<Inventory> pages = new ArrayList();
        if(this.getOperationType() == OperationType.BUY) {
            pages = GUI.getBuyTabsPages();
        }

        if(this.getOperationType() == OperationType.SELL) {
            pages = GUI.getSellTabsPages();
        }

        int page = -1;

        for(int i = 0; i < ((List)pages).size(); ++i) {
            if(((Inventory)((List)pages).get(i)).equals(this.getInventory())) {
                page = i + 1;
            }
        }

        return page;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public OperationType getOperationType() {
        return this.operationType;
    }
}
