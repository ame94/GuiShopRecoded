package ru.blc.guishop;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.blc.guishop.gui.GUI;
import ru.blc.guishop.lang.Lang;
import ru.blc.guishop.lang.Phrases;
import ru.blc.guishop.shopLogger.ShopLogger;
import ru.blc.guishop.utils.ConfigUtil;
import ru.blc.guishop.utils.SU;

public class GuiShopRecoded extends JavaPlugin {
    private static GuiShopRecoded instance;
    protected static Economy econ = null;
    protected static FileConfiguration main;
    protected static FileConfiguration tabs;
    private boolean currentlystarted = false;
    private ShopLogger logger = null;

    public GuiShopRecoded() {
    }

    public static FileConfiguration getTabs() {
        return tabs;
    }

    public static FileConfiguration getMain() {
        return main;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static GuiShopRecoded instance() {
        return instance;
    }

    public void log(String message) {
        if(this.logger != null) {
            this.logger.log(message);
        }

    }

    public void onEnable() {
        instance = this;
        if(!this.setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[]{this.getDescription().getName()}));
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            new AllHandler();
            new Cmds();
            if(!this.loadConfig()) {
                this.getServer().getPluginManager().disablePlugin(this);
            } else {
                if(this.getConfig().getBoolean("LogInfo")) {
                    this.logger = new ShopLogger(this.getConfig().getString("LogFileName"), this.getConfig().getBoolean("LogToConsole"));
                }

                GUI.createShop();
                this.currentlystarted = true;
            }
        }
    }

    public void onDisable() {
        if(this.currentlystarted) {
            this.currentlystarted = false;
            if(this.logger != null) {
                this.logger.endLogger();
                this.logger = null;
            }

            GUI.endShop();
        }
    }

    public void reloadPlugin() {
        this.getLogger().log(Level.INFO, "Reloading plugin");
        if(this.currentlystarted) {
            GUI.endShop();
            if(this.logger != null) {
                this.logger.endLogger();
                this.logger = null;
            }
        }

        this.currentlystarted = false;
        this.reloadConfig();
        if(!this.loadConfig()) {
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            if(this.getConfig().getBoolean("LogInfo")) {
                this.logger = new ShopLogger(this.getConfig().getString("LogFileName"), this.getConfig().getBoolean("LogToConsole"));
            }

            GUI.createShop();
            this.currentlystarted = true;
            Bukkit.getConsoleSender().sendMessage(String.format("[%s] " + SU.genStrng(Phrases.Reloaded.getMessage()), new Object[]{this.getDescription().getName()}));
        }
    }

    public boolean loadConfig() {
        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if(!config.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Can't find file config.yml");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creating new config file for you...");
            this.saveDefaultConfig();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Completed!");
        }

        if(!Lang.installLanguage(this.getConfig().getString("lang")) && !Lang.installLanguage("en_US")) {
            Bukkit.getConsoleSender().sendMessage(String.format("[%s] - Can't find any corerct lang file! Will used default messages", new Object[]{this.getDescription().getName()}));
        }

        main = ConfigUtil.loadYaml(instance, "main.yml");
        if(main == null) {
            Bukkit.getConsoleSender().sendMessage(String.format("[%s] - Can't load file \"main.yml\"! Please contact with developer.", new Object[]{this.getDescription().getName()}));
            return false;
        } else {
            tabs = ConfigUtil.loadYaml(instance, "tabs.yml");
            if(tabs == null) {
                Bukkit.getConsoleSender().sendMessage(String.format("[%s] - Can't load file \"tabs.yml\"! Please contact with developer.", new Object[]{this.getDescription().getName()}));
                return false;
            } else {
                return true;
            }
        }
    }

    public void save(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException var4) {
            ;
        }

    }

    private boolean setupEconomy() {
        if(this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
            if(rsp == null) {
                return false;
            } else {
                econ = (Economy)rsp.getProvider();
                return econ != null;
            }
        }
    }
}
