package xyz.mrsky.mr_world;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.plugin.PluginBase;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Mr_World extends PluginBase implements Listener {
    private List list;

    public Mr_World() {
    }

    public void onEnable() {
        this.saveResource("config.yml");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("插件正在加载，作者Mr_sky贴吧ID賤哥啊哈哈");
        File f = new File(this.getServer().getDataPath() + "worlds/");
        String[] diao = f.list();
        this.list = Arrays.asList(diao);
        this.LoadWorld();
    }

    public void LoadWorld() {
        String worldlist = "";
        try {
            worldlist = (new File(""))+this.getServer().getDataPath()+"worlds/";
        } catch (Exception var3) {
        }
        for(int i = 0; i < (new File(worldlist)).listFiles().length; ++i) {
            if((new File(worldlist)).listFiles()[i].isDirectory() && !this.getServer().isLevelLoaded((new File(worldlist)).listFiles()[i].getName())) {
                this.getServer().loadLevel((new File(worldlist)).listFiles()[i].getName());
                this.getLogger().info("插件已加载地图[" + (new File(worldlist)).listFiles()[i].getName() + "]");
            }
        }
    }

    public void onDisable() {
        this.getLogger().info("插件正在关闭");
    }

    public boolean onCommand(CommandSender sender, Command command, String zhiling, String[] liebiao) {
        if(zhiling.equals("w")) {
            if(liebiao.length < 1) {
                sender.sendMessage("请使用/w <世界名> 以传送至世界");
                return false;
            } else if(this.getServer().isLevelLoaded(liebiao[0])) {
                this.getServer().getPlayer(sender.getName()).teleport(this.getServer().getLevelByName(liebiao[0]).getSafeSpawn());
                String tpTip = this.getConfig().get("传送世界提示信息").toString();
                String tpTipReplace = tpTip.replace("{world}", liebiao[0]);
                sender.sendMessage(tpTipReplace);
                return true;
            } else {
                String
                sender.sendMessage("世界[" + liebiao[0] + "]不存在");
                return false;
            }
        } else if(zhiling.equals("load")) {
            if(!sender.isOp()) {
                sender.sendMessage("不是OP请一边凉快去");
                return false;
            } else if(liebiao.length < 1) {
                sender.sendMessage("请使用/load <世界名> 以加载世界");
                return false;
            } else if(this.getServer().isLevelLoaded(liebiao[0])) {
                sender.sendMessage("地图[" + liebiao[0] + "]已被加载 , 无法再次加载");
                return false;
            } else {
                sender.sendMessage("地图[" + liebiao[0] + "]正在加载");
                this.getServer().loadLevel(liebiao[0]);
                sender.sendMessage("地图[" + liebiao[0] + "]加载完毕");
                return true;
            }
        } else if(zhiling.equals("unload")) {
            if(!sender.isOp()) {
                sender.sendMessage("不是OP请一边凉快去");
                return false;
            } else if(liebiao.length < 1) {
                sender.sendMessage("请使用/unload <世界名> 以卸载世界");
                return false;
            } else if(this.getServer().isLevelLoaded(liebiao[0])) {
                sender.sendMessage("地图[" + liebiao[0] + "]正在卸载");
                this.getServer().unloadLevel(this.getServer().getLevelByName(liebiao[0]));
                sender.sendMessage("地图[" + liebiao[0] + "]卸载完毕");
                return true;
            } else {
                sender.sendMessage("地图[" + liebiao[0] + "]已被卸载 , 无法再次卸载");
                return false;
            }
        } else if(zhiling.equals("lw")) {
            String worldList = this.getConfig().get("世界列表提示信息").toString();
            String worldListReplace = worldList.replace("{list}", String.valueOf(this.list));
            sender.sendMessage(worldListReplace);
            return true;
        } else {
            if(zhiling.equals("new")) {
                if(!sender.isOp()) {
                    sender.sendMessage("不是OP请一边凉快去");
                    return false;
                }

                if(liebiao.length < 1) {
                    sender.sendMessage("请使用/new <世界名> 以新建世界");
                    return false;
                }

                if(liebiao.length < 2) {
                    if(!this.getServer().isLevelLoaded(liebiao[0])) {
                        sender.sendMessage("正在创建地图[" + liebiao[0] + "]...");
                        this.getServer().generateLevel(liebiao[0]);
                        sender.sendMessage("地图[" + liebiao[0] + "]创建完成");
                        return true;
                    }

                    sender.sendMessage("地图[" + liebiao[0] + "]已存在");
                    return false;
                }
            }

            if(zhiling.equals("wp")) {
                if(!sender.isOp()) {
                    sender.sendMessage("不是OP请一边凉快去");
                    return false;
                }

                if(liebiao.length < 1) {
                    sender.sendMessage("请使用/wp admin <add/del> <玩家名> 以添加世界保护白名单");
                    return false;
                }

                List worldPVPList;
                if(liebiao[0].equals("admin")) {
                    if(liebiao.length < 1) {
                        sender.sendMessage("请使用/wp admin <add/del> <玩家名> 以添加世界保护白名单");
                        return false;
                    }

                    if(liebiao[1].equals("add")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp admin add <玩家名> 以添加世界保护白名单");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("世界保护白名单");
                        worldPVPList.add(liebiao[2]);
                        this.getConfig().set("世界保护白名单", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("添加管理员[" + liebiao[2] + "]完成");
                    }

                    if(liebiao[1].equals("del")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp admin del <玩家名> 以删除世界保护白名单");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("世界保护白名单");
                        worldPVPList.remove(liebiao[2]);
                        this.getConfig().set("世界保护白名单", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("删除管理员[" + liebiao[2] + "]完成");
                    }
                }

                if(liebiao[0].equals("world")) {
                    if(liebiao.length < 1) {
                        sender.sendMessage("请使用/wp world <add/del> <world> 以添加世界保护列表");
                        return false;
                    }

                    if(liebiao[1].equals("add")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp world add <world> 以调整世界保护列表");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("世界保护列表");
                        worldPVPList.add(liebiao[2]);
                        this.getConfig().set("世界保护白名单", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("添加保护世界[" + liebiao[2] + "]完成");
                    }

                    if(liebiao[1].equals("del")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp world del <world> 以调整世界保护列表");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("世界保护列表");
                        worldPVPList.remove(liebiao[2]);
                        this.getConfig().set("世界保护白名单", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("删除保护世界[" + liebiao[2] + "]完成");
                    }
                }

                if(zhiling.equals("pvp")) {
                    if(liebiao.length < 1) {
                        sender.sendMessage("请使用/wp pvp <add/del> <world> 以调整PVP世界保护列表");
                        return false;
                    }

                    if(liebiao[1].equals("add")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp pvp add <world> 以添加PVP世界保护");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("PVP世界保护列表");
                        worldPVPList.add(liebiao[2]);
                        this.getConfig().set("PVP世界保护列表", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("添加PVP世界[" + liebiao[2] + "]保护完成");
                    }

                    if(liebiao[1].equals("del")) {
                        if(liebiao.length < 2) {
                            sender.sendMessage("请使用/wp pvp del <world> 以删除PVP世界保护");
                            return false;
                        }

                        worldPVPList = this.getConfig().getStringList("PVP世界保护列表");
                        worldPVPList.remove(liebiao[2]);
                        this.getConfig().set("PVP世界保护列表", worldPVPList);
                        this.getConfig().save();
                        sender.sendMessage("删除PVP世界[" + liebiao[2] + "]保护完成");
                    }
                }
            }

            return true;
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        List worldProtectList = this.getConfig().getStringList("世界保护列表");
        List worldAdminList = this.getConfig().getStringList("世界保护白名单");
        if(worldProtectList.contains(event.getPlayer().getLevel().getName()) && !worldAdminList.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage(this.getConfig().get("世界保护提示信息").toString());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerUseItem(BlockPlaceEvent event) {
        List worldProtectList = this.getConfig().getStringList("世界保护列表");
        List worldAdminList = this.getConfig().getStringList("世界保护白名单");
        if(worldProtectList.contains(event.getPlayer().getLevel().getName()) && !worldAdminList.contains(event.getPlayer().getName().tolow)) {
            event.getPlayer().sendMessage(this.getConfig().get("世界保护提示信息").toString());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerPVP(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof Player && ((EntityDamageByEntityEvent)event).getDamager() instanceof Player) {
            String map = event.getEntity().getLevel().getFolderName();
            List worldPVPList = this.getConfig().getStringList("PVP世界保护列表");
            List worldAdminList = this.getConfig().getStringList("世界保护白名单");
            if(worldPVPList.contains(map) && !worldAdminList.contains(((EntityDamageByEntityEvent)event).getDamager().getName())) {
                event.setCancelled(true);
                this.getServer().getPlayer(((EntityDamageByEntityEvent)event).getDamager().getName()).sendMessage(this.getConfig().get("PVP世界保护提示信息").toString());
            }
        }

    }
}
