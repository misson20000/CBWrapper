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
		if(cmd.getName().equals("cb")) {
			if(args[0] != null) {
				int i = 0;
				String name = sender.getName();
				if(args[0].equals("-u")) {
					if(args[1] != null) {
						name = args[1];
						i = 2;
					} else {
						sender.sendMessage("Error: The -u option requires one argument!");
						return false;
					}
				}
				if(args[i] == null) {
					sender.sendMessage("Error: Where is the command?");
				}
				String concatargs = "";
				for(; i < args.length; i++) {
					concatargs = concatargs + args[i] + " ";
				}
				sender.sendMessage("ConcatArgs: " + concatargs);
				if(sender instanceof Player) {
					sender.sendMessage("Please note that the /cb command is not intended for players.");
					getServer().dispatchCommand(sender, concatargs);
					return true;
				}
				if(sender instanceof ConsoleCommandSender) {
					sender.sendMessage("Sorry, but this command is not intended for the console, because the console has no x, y, or z. Try running as a player or command block");
					return false;
				}
				if(sender instanceof BlockCommandSender) {
					Block block = ((BlockCommandSender) sender).getBlock();
					Player dummy = new DummyPlayer(name, getServer(), block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
					getServer().dispatchCommand(dummy, concatargs);
				}
				return true;
			} else {
				sender.sendMessage("Error: The /cb command requires at least one parameter");
				return false;
			}
		}
		return false;
	}
}
