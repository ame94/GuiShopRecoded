package ru.blc.guishop.shopLogger;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import ru.blc.guishop.GuiShopRecoded;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import ru.blc.guishop.GuiShopRecoded;

public class ShopLogger {
    private boolean logToConsole;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private PrintWriter out = null;
    private BukkitRunnable saver = null;

    public ShopLogger(String pathToLogFile, boolean logToConsole) {
        this.logToConsole = logToConsole;
        if(!pathToLogFile.endsWith(".log")) {
            pathToLogFile = pathToLogFile + ".log";
        }

        final File logFile = new File(GuiShopRecoded.instance().getDataFolder(), pathToLogFile);
        if(!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException var6) {
                GuiShopRecoded.instance().getLogger().log(Level.WARNING, "There is an error while creating new log file!");
                var6.printStackTrace();
            }
        }

        try {
            this.out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.saver = new BukkitRunnable() {
            public void run() {
                ShopLogger.this.out.close();

                try {
                    ShopLogger.this.out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                } catch (IOException var2) {
                    var2.printStackTrace();
                }

            }
        };
        this.saver.runTaskTimer(GuiShopRecoded.instance(), 0L, 600L);
    }

    public void endLogger() {
        this.out.close();
        this.saver.cancel();
    }

    public void log(String message) {
        message = ChatColor.stripColor(message);
        if(this.logToConsole) {
            GuiShopRecoded.instance().getLogger().log(Level.INFO, message);
        }

        Date now = new Date();
        message = this.format.format(now) + "| " + message;
        Validate.notNull(this.out, "Logger global error! Contact with developer. Error:LEBOIN");
        this.out.println(message);
    }
}
