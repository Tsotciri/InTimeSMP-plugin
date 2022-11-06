package net.tsotciri.itsmp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public final class InTimeSMP extends JavaPlugin implements Listener {

    Timer timer;
    NamespacedKey Time = new NamespacedKey(this,"seconds");
    int t;
    public void simpleTimer() {
        int countdown_speed = 1000;
        timer = new Timer(countdown_speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Tick logic
                //IDK what I am typing really

                //Executing the timer on every player
                for (Player player : Bukkit.getOnlinePlayers()) {
                    //Getting NBT data container
                    PersistentDataContainer data = player.getPersistentDataContainer();

                    //Getting the Time value
                    t = data.get(Time, PersistentDataType.INTEGER);

                    //Subtracting 1 from current Time value
                    if (t > 0) {
                        t--;
                    }

                    //Sending Action Bar
                    displayTime(player, t);

                    //Saving Time value to player NBT
                    data.set(Time, PersistentDataType.INTEGER, t);

                    if (t < 0) {
                        Bukkit.getLogger().info(player.getName() + "Run out of time!");
                    }
                }
            }
        });

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("InTimeSMP plugin has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        simpleTimer();
        timer.start();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().warning("InTimeSMP plugin has been disabled");
    }

    static void displayTime(Player player, int t) {
        int weeks = 00;
        int days = 00;
        int hours = 00;
        int minutes = 00;
        int seconds = 00;

        weeks = t/604800;
        days = (t % 604800) / 86400;
        hours = (t % 86400) / 3600;
        minutes = (t % 3600) / 60;
        seconds = t % 60;

        String timeString = String.format("%02d:%02d:%02d:%02d:%02d", weeks, days, hours, minutes, seconds);

        player.sendActionBar(ChatColor.GREEN + timeString);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Getting player data
        Player player = event.getPlayer();

        //Getting player Name
        String playerName = player.getName();



        //Getting NBT data container
        PersistentDataContainer data = player.getPersistentDataContainer();

        //Checking if player that joined has Time value
        if (data.has(Time, PersistentDataType.INTEGER)) {
            Bukkit.getLogger().info("Player " + playerName + " Has Time NBT ");
        } else {
            Bukkit.getLogger().warning("Player " + playerName + " Joined without having Time value, setting now");
            data.set(Time, PersistentDataType.INTEGER, 55);
        }

        //Setting the join message to green
        event.setJoinMessage(ChatColor.DARK_GREEN + playerName + ChatColor.GREEN + " joined the game");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        //Getting player data
        Player player = event.getPlayer();

        //Getting player Name
        String playerName = player.getName();

        event.setQuitMessage(ChatColor.DARK_GREEN + playerName + ChatColor.GREEN + " left the game");

    }

    int pings;

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        pings ++;
        event.setMotd(ChatColor.GREEN + "Server Pinged " + ChatColor.DARK_GREEN + pings + ChatColor.GREEN + " times in total");
    }

}