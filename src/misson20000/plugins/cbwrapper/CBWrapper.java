package misson20000.plugins.cbwrapper;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

public final class CBWrapper extends JavaPlugin {
    @Override
    public void onEnable() {
    }
    @Override
    public void onDisable() {
    }

    public int parseOffset(int orig, String offset) {
	if(offset.startsWith("~")) {
	    return orig + Integer.parseInt(offset.substring(1));
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
		if ((sender instanceof Player)) {
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
		    Player dummy = new DummyPlayer(name, getServer(),
						   world,
						   x, y, z);
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

