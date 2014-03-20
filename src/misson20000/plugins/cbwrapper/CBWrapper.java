package misson20000.plugins.cbwrapper;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CBWrapper extends JavaPlugin {
    @Override
    public void onEnable() {
    }
    @Override
    public void onDisable() {
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (cmd.getName().equals("cb") || cmd.getName().equals("cw")) {
		String name = sender.getName();
		int ox = 0;
		int oy = 0;
		int oz = 0;
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
			if(args[i].equals("-o")) {
				if(args.length - ++i >= 3) {
					ox = Integer.parseInt(args[i++].substring(1));
					oy = Integer.parseInt(args[i++].substring(1));
					oz = Integer.parseInt(args[i].substring(1));
				} else {
					sender.sendMessage("Not enough arguments for -o. Expected -o ~<x> ~<y> ~<z>");
					return false;
				}
			}
		}
	    if (args.length > 0) {
			String concatargs = "";
			for (; i < args.length; i++) {
			    concatargs = concatargs + args[i] + " ";
			}
			if ((sender instanceof Player)) {
			    return false;
			}
			if ((sender instanceof ConsoleCommandSender)) {
			    sender.sendMessage("Sorry, but this command is not intended for the console, because the console has no x, y, or z. Try running in a command block");
			    return false;
			}
			if ((sender instanceof BlockCommandSender)) {
			    Block block = ((BlockCommandSender)sender).getBlock();
			    Player dummy = new DummyPlayer(name, getServer(),
			        block.getLocation().getWorld(),
					block.getLocation().getBlockX()+ox,
					block.getLocation().getBlockY()+oy,
	                block.getLocation().getBlockZ()+oz);
			    getServer().dispatchCommand(dummy, concatargs);
			}
			return true;
	    }
	    sender.sendMessage("Error: The /cb command requires at least one parameter");
	    return false;
	}
	
	return false;
    }
}
