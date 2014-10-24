package net.vaultcraft.vcprison;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.commands.*;
import net.vaultcraft.vcprison.crate.CrateFile;
import net.vaultcraft.vcprison.crate.CrateListener;
import net.vaultcraft.vcprison.crate.MineCrateInjector;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.furance.FurnaceListener;
import net.vaultcraft.vcprison.gangs.GangManager;
import net.vaultcraft.vcprison.gangs.VCGangs;
import net.vaultcraft.vcprison.listener.AsyncChatListener;
import net.vaultcraft.vcprison.listener.PrisonUserListener;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.mine.MineUtil;
import net.vaultcraft.vcprison.mine.warp.WarpGUI;
import net.vaultcraft.vcprison.mine.warp.WarpLoader;
import net.vaultcraft.vcprison.pickaxe.*;
import net.vaultcraft.vcprison.plots.PlotWorld;
import net.vaultcraft.vcprison.scoreboard.PrisonScoreboard;
import net.vaultcraft.vcprison.shop.FFAShopListener;
import net.vaultcraft.vcprison.shop.PrisonShopListener;
import net.vaultcraft.vcprison.sword.*;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.worth.ItemWorthLoader;
import net.vaultcraft.vcprison.worth.Warden;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.events.ServerEventHandler;
import net.vaultcraft.vcutils.innerplugin.VCPluginManager;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.sign.SignManager;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCPrison extends JavaPlugin {

    private static ServerEventHandler eventHandler;
    public static Location spawn;
    private static VCPrison instance;

    public void onEnable() {

        instance = this;

        CommandManager.addCommand(new VCRankup("rankup", Group.COMMON, "nextrank"));
        CommandManager.addCommand(new VCPrestige("prestige", Group.COMMON, "startover"));
        CommandManager.addCommand(new VCReset("reset", Group.ADMIN));
        CommandManager.addCommand(new VCWarp("warp", Group.COMMON, "mine", "mines", "warps"));
        CommandManager.addCommand(new VCAddCrateItem("addcrateitem", Group.DEVELOPER, "aci"));
        //CommandManager.addCommand(new VCPlots("plot", Group.COMMON, "p", "cell", "plots", "plotme"));
        CommandManager.addCommand(new VCHelp("help", Group.COMMON, "?"));
        CommandManager.addCommand(new VCRules("rules", Group.COMMON));
        CommandManager.addCommand(new VCGangs("gang", Group.COMMON, "gangs", "f", "team"));
        CommandManager.addCommand(new VCDropParty("dp", Group.ADMIN, "dropparty"));
        CommandManager.addCommand(new VCFix("fix", Group.WOLF, "repair"));
        CommandManager.addCommand(new VCPoint("point", Group.ADMIN));
        CommandManager.addCommand(new VCKit("kit", Group.COMMON, "kits"));
        CommandManager.addCommand(new VCSpawn("spawn", Group.COMMON));
        CommandManager.addCommand(new VCShop("shop", Group.COMMON));
        CommandManager.addCommand(new VCFFA("ffa", Group.COMMON, "freeforall", "pvp", "fight"));
        CommandManager.addCommand(new VCSell("sell", Group.ENDERDRAGON));

        new PlotWorld();

        eventHandler = new ServerEventHandler(this);
        spawn = new Location(Bukkit.getServer().getWorld("world"), -3839.5, 86, 0.5);

        CrateFile.getInstance().load();

        new AsyncChatListener();
        new PrisonUserListener();
        new PickaxeListener();
        new SwordListener();
        new FurnaceListener();
        new CrateListener();
        new PrisonShopListener();
        new FFAShopListener();

        MineUtil.createBlockInjector(new MineCrateInjector());

        new WarpGUI();

        PickaxePerk.addPerk(new EfficiencyPerk(Material.STONE, "Efficiency", 1, 5, 100, "Adds a level of efficiency to your pick."), 0);
        PickaxePerk.addPerk(new FortunePerk(Material.DIAMOND, "Fortune", 1, 5, 100, "Adds a level of fortune to your pick."), 1);
        PickaxePerk.addPerk(new HastePerk(Material.DIAMOND_PICKAXE, "Haste", 5, 0, 4, "Adds a level of haste when you have your pick selected."), 2);
        PickaxePerk.addPerk(new ExplosionPerk(Material.TNT, "Explosion", 3, 0, 25, "Blow up the blocks you wish to mine."), 3);
        PickaxePerk.addPerk(new AutoSmeltPerk(Material.FIRE, Material.FIRE, Material.FURNACE, "Auto Smelt", 8, false, "Smelt things as you go!", "Toggleable"), 4);
        PickaxePerk.addPerk(new SilkTouchPerk(Material.WEB, Material.WEB, Material.STRING, "Silk Touch", 8, false, "Adds the Silk Touch enchantment to your pick.", "Toggleable"), 5);
        PickaxePerk.addPerk(new NightVisionPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 8, false, "Adds night vision when you have your pick selected.", "Toggleable"), 6);
        PickaxePerk.addPerk(new SpeedPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 8, false, "Adds Speed when you have your pick selected.", "Toggleable"), 7);

        SwordPerk.addPerk(new SharpnessSwordPerk(Material.QUARTZ, "Sharpness", 2, 0, 5, "Adds a level of sharpness to your sword."), 0);
        SwordPerk.addPerk(new FireAspectSwordPerk(Material.FIRE, "Fire Aspect", 4, 0, 2, "Adds a level of fire aspect to your sword."), 1);
        SwordPerk.addPerk(new KnockbackSwordPerk(Material.ANVIL, "Knockback", 5, 0, 8, "Adds a level of knockback to your sword."), 2);
        SwordPerk.addPerk(new HasteSwordPerk(Material.DIAMOND_PICKAXE, "Haste", 5, 0, 2, "Adds a level of haste when you have your sword selected."), 3);
        SwordPerk.addPerk(new NightVisionSwordPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 5, false, "Adds night vision when you have your sword selected.", "Toggleable"), 4);
        SwordPerk.addPerk(new SpeedSwordPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 5, false, "Adds Speed when you have your sword selected.", "Toggleable"), 5);
        SwordPerk.addPerk(new ExplosionSwordPerk(Material.TNT, "Explosion", 2, 0, 10, "Adds a level of explosion to your sword."), 6);
        for(Player player : Bukkit.getOnlinePlayers()) {
            new PrisonUser(player);
        }

        MineLoader.loadMines();
        WarpLoader.loadWarps();
        ItemWorthLoader.loadItemWorth();

        new GangManager();

        VCPluginManager.register(this);

        System.out.println("Loading FFA Region...");
        World ffaWorld = Bukkit.getServer().createWorld(new WorldCreator("ffa"));
        ProtectedArea area = new ProtectedArea(new Area(new Location(ffaWorld, -5000, 0, -5000), new Location(ffaWorld, 5000, 256, 5000)));
        area.setPriority(100);
        area.addToProtection(FlagType.PVP, false);
        area.addToProtection(FlagType.CREATURE_SPAWN, true);
        area.addToProtection(FlagType.ENTITY_DAMAGE, false);
        area.addToProtection(FlagType.PLAYER_DAMAGE, false);

        ProtectionManager.getInstance().addToProtection("ffa", area);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            int pos = 60;
            @Override
            public void run() {
                for (Mine mine : MineLoader.getMines()) {
                    Runnable delay = new Runnable() {
                        @Override
                        public void run() {
                            MineLoader.resetMine(mine);
                            System.out.println("Mine: "+mine.getRank().toString()+" reset stage: COMPLETE!");
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.this, delay, pos+=60);
                }
            }
        }, 5l);

        new Warden();
        PrisonScoreboard.init();

        Runnable minePercentUpdate = new Runnable() {
            public void run() {
                for (Mine mine : MineLoader.getMines()) {
                    if (SignManager.fromMeta("mine%" + mine.getRank().toString()) == null)
                        continue;

                    SignManager.updateSigns("mine%"+mine.getRank().toString(), "&m---&c=&0&m---", "&5Percent Mined", "&8&l&n"+(df.format(mine.getPercent() * 100))+"%", "&m---&c=&0&m---");
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, minePercentUpdate, 20, 20);
    }

    public static ServerEventHandler getEventHandler() {
        return eventHandler;
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public void onDisable() {
        PrisonUser.disable();
        PlotWorld.getPlotManager().disable();
        GangManager.disable();

        for (Player player : Bukkit.getOnlinePlayers()) {
            FFAPlayer ffa = FFAPlayer.getFFAPlayerFromPlayer(player);

            if (ffa.isPlaying())
                ffa.endFFA();
        }
    }

    public static List<Player> getStaff() {
        List<Player> staff = Lists.newArrayList();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (User.fromPlayer(player) != null && User.fromPlayer(player).getGroup() != null)
                if (User.fromPlayer(player).getGroup().hasPermission(Group.HELPER))
                    staff.add(player);
        }
        return staff;
    }

    public static VCPrison getInstance() {
        return instance;
    }
}
