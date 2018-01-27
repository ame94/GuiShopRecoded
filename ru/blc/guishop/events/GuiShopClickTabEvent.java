package ru.blc.guishop.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import ru.blc.guishop.gui.ShopInventory;
import ru.blc.guishop.gui.Tab;
import ru.blc.guishop.gui.ShopInventory.OperationType;

public class GuiShopClickTabEvent extends GuiShopClickEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Tab tab;
    private OperationType operationType;

    public GuiShopClickTabEvent(GuiShopClickEvent event, Tab tab, OperationType operationType) {
        super(event);
        this.tab = tab;
        this.operationType = operationType;
    }

    public int getPage() {
        List<ShopInventory> pages = new ArrayList();
        if(this.getOperationType() == OperationType.BUY) {
            pages = this.getTab().getBuyPages();
        }

        if(this.getOperationType() == OperationType.SELL) {
            pages = this.getTab().getSellPages();
        }

        int page = -1;

        for(int i = 0; i < ((List)pages).size(); ++i) {
            if(((ShopInventory)((List)pages).get(i)).getInventory().equals(this.getInventory())) {
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

    public Tab getTab() {
        return this.tab;
    }

    public OperationType getOperationType() {
        return this.operationType;
    }
}
