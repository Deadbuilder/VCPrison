package net.vaultcraft.vcprison;

import com.google.common.collect.Lists;
import net.vaultcraft.vcessentials.auction.AucManager;
import net.vaultcraft.vcprison.candy.*;
import net.vaultcraft.vcprison.cells.CellManager;
import net.vaultcraft.vcprison.cells.CellsListener;
import net.vaultcraft.vcprison.cells.VCCell;
import net.vaultcraft.vcprison.commands.*;
import net.vaultcraft.vcprison.crate.CrateFile;
import net.vaultcraft.vcprison.crate.CrateListener;
import net.vaultcraft.vcprison.crate.MineCrateInjector;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.furance.FurnaceListener;
import net.vaultcraft.vcprison.gangs.GangManager;
import net.vaultcraft.vcprison.gangs.VCGangs;
import net.vaultcraft.vcprison.listener.AsyncChatListener;
import net.vaultcraft.vcprison.listener.PortalListener;
import net.vaultcraft.vcprison.listener.PrisonUserListener;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.mine.MineUtil;
import net.vaultcraft.vcprison.mine.warp.WarpGUI;
import net.vaultcraft.vcprison.mine.warp.WarpLoader;
import net.vaultcraft.vcprison.pickaxe.*;
import net.vaultcraft.vcprison.scoreboard.PrisonScoreboard;
import net.vaultcraft.vcprison.shop.CandyManListener;
import net.vaultcraft.vcprison.shop.FFAShopListener;
import net.vaultcraft.vcprison.shop.PrisonShopListener;
import net.vaultcraft.vcprison.sword.*;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.worth.ItemWorthLoader;
import net.vaultcraft.vcprison.worth.Warden;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.innerplugin.VCPluginManager;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.sign.SignManager;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCPrison extends JavaPlugin {

    public static Location spawn;
    private static VCPrison instance;


    private static boolean shuttingDown = false;

    public static Collection<Player> getFFA() {
        List<Player> l = Lists.newArrayList();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying())
                l.add(player);
        }
        return l;
    }

    private CellManager cellManager = null;
    private BukkitTask cellSaveTask = null;

    public void onEnable() {

        instance = this;

        CommandManager.addCommand(new VCRankup("rankup", Group.COMMON, "nextrank"));
        CommandManager.addCommand(new VCPrestige("prestige", Group.COMMON, "startover"));
        CommandManager.addCommand(new VCReset("reset", Group.ADMIN));
        CommandManager.addCommand(new VCWarp("warp", Group.COMMON, "mine", "mines", "warps"));
        CommandManager.addCommand(new VCAddCrateItem("addcrateitem", Group.DEVELOPER, "aci"));
        CommandManager.addCommand(new VCCell("plot", Group.COMMON, "p", "cell", "plots", "plotme", "cells"));
        CommandManager.addCommand(new VCHelp("help", Group.COMMON, "?"));
        CommandManager.addCommand(new VCRules("rules", Group.COMMON));
        CommandManager.addCommand(new VCGangs("gang", Group.COMMON, "gangs", "f", "team"));
        CommandManager.addCommand(new VCDropParty("dp", Group.COMMON, "dropparty"));
        CommandManager.addCommand(new VCFix("fix", Group.WOLF, "repair"));
        CommandManager.addCommand(new VCPoint("point", Group.ADMIN));
        CommandManager.addCommand(new VCKit("kit", Group.COMMON, "kits"));
        CommandManager.addCommand(new VCSpawn("spawn", Group.COMMON));
        CommandManager.addCommand(new VCShop("shop", Group.COMMON));
        CommandManager.addCommand(new VCFFA("ffa", Group.COMMON, "freeforall", "pvp", "fight"));
        CommandManager.addCommand(new VCSell("sell", Group.WITHER));
        CommandManager.addCommand(new VCBlock("block", Group.WOLF));

        CommandManager.removeCommand("money");
        CommandManager.addCommand(new VCPrisonMoney("money", Group.COMMON, "bal", "balance"));

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
        new PortalListener();
        new CellsListener();
        new CandyManListener();

        MineUtil.createBlockInjector(new MineCrateInjector());

        new WarpGUI();

        //Pickaxe perks
        PickaxePerk.addPerk(new EfficiencyPerk(Material.STONE, "Efficiency", 1, 5, 100, "Adds a level of efficiency to your pick."), 0);
        PickaxePerk.addPerk(new FortunePerk(Material.DIAMOND, "Fortune", 1, 5, 100, "Adds a level of fortune to your pick."), 1);
        PickaxePerk.addPerk(new HastePerk(Material.DIAMOND_PICKAXE, "Haste", 5, 0, 4, "Adds a level of haste when you have your pick selected."), 2);
        PickaxePerk.addPerk(new ExplosionPerk(Material.TNT, "Explosion", 3, 0, 25, "Blow up the blocks you wish to mine."), 3);
        PickaxePerk.addPerk(new AutoSmeltPerk(Material.FIRE, Material.FIRE, Material.FURNACE, "Auto Smelt", 8, false, "Smelt things as you go!", "Toggleable"), 4);
        PickaxePerk.addPerk(new SilkTouchPerk(Material.WEB, Material.WEB, Material.STRING, "Silk Touch", 8, false, "Adds the Silk Touch enchantment to your pick.", "Toggleable"), 5);
        PickaxePerk.addPerk(new NightVisionPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 8, false, "Adds night vision when you have your pick selected.", "Toggleable"), 6);
        PickaxePerk.addPerk(new SpeedPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 8, false, "Adds Speed when you have your pick selected.", "Toggleable"), 7);

        //Sword perks
        SwordPerk.addPerk(new SharpnessSwordPerk(Material.QUARTZ, "Sharpness", 2, 0, 3, "Adds a level of sharpness to your sword."), 0);
        SwordPerk.addPerk(new FireAspectSwordPerk(Material.FIRE, "Fire Aspect", 6, 0, 1, "Adds a level of fire aspect to your sword."), 1);
        SwordPerk.addPerk(new KnockbackSwordPerk(Material.ANVIL, "Knockback", 5, 0, 2, "Adds a level of knockback to your sword."), 2);
        SwordPerk.addPerk(new HasteSwordPerk(Material.DIAMOND_PICKAXE, "Haste", 1, 0, 2, "Adds a level of haste when you have your sword selected."), 3);
        SwordPerk.addPerk(new NightVisionSwordPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 5, false, "Adds night vision when you have your sword selected.", "Toggleable"), 4);
        SwordPerk.addPerk(new SpeedSwordPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 5, false, "Adds Speed when you have your sword selected.", "Toggleable"), 5);
        SwordPerk.addPerk(new ExplosionSwordPerk(Material.TNT, "Explosion", 5, 0, 2, "Adds a level of explosion to your sword."), 6);

        //Remove Recipes
        Iterator<Recipe> rIterator = getServer().recipeIterator();
        while (rIterator.hasNext()) {
            Recipe current = rIterator.next();
            if (current.getResult().getType().equals(Material.QUARTZ))
                rIterator.remove();
            if(current.getResult().getType().equals(Material.GOLDEN_APPLE) && current.getResult().getData().getData() == 1)
                rIterator.remove();
            if(current.getResult().getType().equals(Material.COOKIE))
                rIterator.remove();
        }

        //Candy
        new CandyListener();
        CandyManager.registerCandy("gum", new Gum());
        CandyManager.registerCandy("butterscotch", new Butterscotch());
        CandyManager.registerCandy("sugar", new SugarCube());
        CandyManager.registerCandy("jawbreaker", new Jawbreaker());
        CandyManager.registerCandy("sourpatch", new SourPatch());
        CandyManager.registerCandy("warhead", new Warhead());
        CandyManager.registerCandy("candyapple", new CandyApple());
        CandyManager.registerCandy("chocolatebar", new ChocolateBar());
        CandyManager.registerCandy("cookie", new Cookie());
        CandyManager.registerCandy("sweedishfish", new SwedishFish());

        //Add users who are online
        Bukkit.getOnlinePlayers().forEach(net.vaultcraft.vcprison.user.PrisonUser::new);

        //Mine loading
        MineLoader.loadMines();
        WarpLoader.loadWarps();
        ItemWorthLoader.loadItemWorth();

        //Gangs
        new GangManager();

        //Main class as listener
        VCPluginManager.register(this);

        //
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

        Runnable minePercentUpdate = () -> {
            for (Mine mine : MineLoader.getMines()) {
                if (SignManager.fromMeta("mine%" + mine.getRank().toString()) == null)
                    continue;

                SignManager.updateSigns("mine%"+mine.getRank().toString(), "&m---&c=&0&m---", "&5Percent Mined", "&8&l&n"+(df.format(mine.getPercent() * 100))+"%", "&m---&c=&0&m---");
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, minePercentUpdate, 20, 20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
        }, 20, 20);

        cellManager = new CellManager();
        cellSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, cellManager::saveCells, (20*60)*5, (20*60)*5);

        AucManager.isPrison = true;
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public void onDisable() {
        PrisonUser.disable();
        GangManager.disable();
        if(cellSaveTask != null) {
            cellSaveTask.cancel();
        }
        cellManager.saveCells();

        for (Player player : Bukkit.getOnlinePlayers()) {
            FFAPlayer ffa = FFAPlayer.getFFAPlayerFromPlayer(player);

            if (ffa.isPlaying())
                ffa.endFFA();
        }
    }

    public static List<Player> getStaff() {
        List<Player> staff = Lists.newArrayList();
        staff.addAll(Bukkit.getOnlinePlayers().stream().filter(player -> User.fromPlayer(player) != null && User.fromPlayer(player).getGroup() != null).map(player -> player).collect(Collectors.toList()));
        return staff;
    }

    public static VCPrison getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("ppoint")) {
            if(args.length < 2)
                return false;
            int amount = Integer.parseInt(args[0]);
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null)
                return false;
            ItemStack itemStack = Pickaxe.getAddPointItem();
            itemStack.setAmount(amount);
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
            Form.at(player, Prefix.VAULT_CRAFT, "Your pickaxe perk points have arrived! Check your inventory!");
            for(ItemStack itemStack1 : map.values())
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack1);
            if(!map.isEmpty())
                Form.at(player, Prefix.WARNING, "Some of your pickaxe points were dropped next to you since you didn't have enough room in your inventory.");
        }

        if(label.equals("prisonsave")) {
            if(args.length > 0)
                shuttingDown = true;
            Logger.log(VCPrison.getInstance(), "Saving Prison Users...");
            for(Player player : Bukkit.getOnlinePlayers()) {
                PrisonUser.fromPlayer(player).save();
                User.update(User.fromPlayer(player));
            }
            Logger.log(VCPrison.getInstance(), "Prison Users saved!");
        }
        return true;
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        if(shuttingDown)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server is going into maintenance! Please join back later.");
    }

    public CellManager getCellManager() {
        return cellManager;
    }
}
