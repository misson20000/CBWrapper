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
		if (cmd.getName().equals("cb")) {
			if (args.length > 0) {
				String name = sender.getName();
				String concatargs = "";
				for (int i = 0; i < args.length; i++) {
					concatargs = concatargs + args[i] + " ";
				}
				if ((sender instanceof Player)) {
					sender.sendMessage("Please note that the /cb command is not intended to be run by players");
					getServer().dispatchCommand(sender, concatargs);
					return true;
				}
				if ((sender instanceof ConsoleCommandSender)) {
					sender.sendMessage("Sorry, but this command is not intended for the console, because the console has no x, y, or z. Try running as a player or command block");
					return false;
				}
				if ((sender instanceof BlockCommandSender)) {
					Block block = ((BlockCommandSender)sender).getBlock();
					Player dummy = new DummyPlayer(name, getServer(), block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
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
