package misson20000.plugins.cbwrapper;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public final class CBWrapper extends JavaPlugin {
        
    private WorldEditPlugin  wePlugin;
    private WorldEditAdapter weAdapter;
        
    private Map<String, DummyPlayer> dummies;
        
    @Override
    public void onEnable() {
        dummies = new HashMap<String, DummyPlayer>();
        
        wePlugin = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
        if(wePlugin != null) {
            try {
                weAdapter = new WorldEditAdapter(wePlugin.getWorldEdit().getSessionManager());
            } catch(ReflectiveOperationException e) {
                weAdapter = null;
                wePlugin  = null;
                e.printStackTrace();
                this.getLogger().warning("Error while initializing WorldEditAdapter. Report this to CBWrapper devs. Until this is fixed, CBWrapper won't work properly with WorldEdit");
            }
        }
    }
    @Override
    public void onDisable() {
    }

    public int parseOffset(int orig, String offset) {
        if(offset.startsWith("~")) {
        	if(offset.length() > 1) {
        		return orig + Integer.parseInt(offset.substring(1));
        	} else {
        		return orig;
        	}
        } else {
            return Integer.parseInt(offset);
        }
    }    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("cb") || cmd.getName().equals("cw")) {
            String name = sender.getName();
            int x = 0;
            int y = 0;
            int z = 0;
            boolean haspos = false;
            World world = null;
            if((sender instanceof BlockCommandSender)) {
                Block block = ((BlockCommandSender)sender).getBlock();
                x = block.getLocation().getBlockX();
                y = block.getLocation().getBlockY();
                z = block.getLocation().getBlockZ();
                world = block.getLocation().getWorld();
                haspos = true;
            }
            if(sender instanceof Player) {
                return false;               
            }
        
            int i;
            for(i = 0; i < args.length; i++) {
                if(!args[i].startsWith("-")) {
                    break;
                }
                if(args[i].equals("-u")) {
                    if(args.length - ++i >= 1) {
                        name = args[i];                                 
                    } else {
                        sender.sendMessage("Not enough arguments for -u. Expected -u <username>");
                        return false;
                    }
                }
                if(args[i].equals("-w")) {
                    world = getServer().getWorld(args[++i]);
                    if(world == null) {
                        sender.sendMessage("World '" + args[i] + "' does not exist");
                        return false;
                    }
                }
                if(args[i].equals("-o")) {
                    if(args.length - ++i >= 3) {
                        if(!haspos && args[i].startsWith("~")) {
                            sender.sendMessage("The console has no position for a relative offset");
                            return false;
                        }
                        x = parseOffset(x, args[i++]);
                        if(!haspos && args[i].startsWith("~")) {
                            sender.sendMessage("The console has no position for a relative offset");
                            return false;
                        }
                        y = parseOffset(y, args[i++]);
                        if(!haspos && args[i].startsWith("~")) {
                            sender.sendMessage("The console has no position for a relative offset");
                            return false;
                        }
                        z = parseOffset(z, args[i]);
                    } else {
                        sender.sendMessage("Not enough arguments for -o. Expected -o <x> <y> <z>");
                        return false;
                    }
                }
            }
            if (args.length > 0) {
                String concatargs = "";
                for (; i < args.length; i++) {
                    concatargs = concatargs + args[i] + " ";
                }
                if(world == null) {
                    sender.sendMessage("The -w flag is necessary for the console");
                    return false;
                }
                            
                DummyPlayer dummy;
                if(dummies.containsKey(name)) {
                    dummy = dummies.get(name);
                    dummy.teleport(new Location(world, x, y, z));
                } else {
                    dummy = new DummyPlayer(name, getServer(),
                                            world, x, y, z);
                                
                    if(weAdapter != null) {
                        weAdapter.createSession(dummy.getUniqueId(), wePlugin.wrapPlayer(dummy).getSessionKey());
                    }
                                
                    dummies.put(name, dummy);
                }
                
                getServer().dispatchCommand(dummy, concatargs);
            } else {
                sender.sendMessage("Not enough arguments");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}

