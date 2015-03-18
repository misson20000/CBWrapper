package misson20000.plugins.cbwrapper;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.NetworkManager;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayInFlying;
import net.minecraft.server.v1_8_R1.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R1.PlayerConnection;

public class DummyPlayerConnection extends PlayerConnection {

	public DummyPlayerConnection(MinecraftServer minecraftserver,
			NetworkManager networkmanager, EntityPlayer entityplayer) {
		super(minecraftserver, networkmanager, entityplayer);
	}

	@Override
	public void c() {} //onTick? whatever, all it does is send KeepAlives and kick the player, neither of which are desirable here
	
	@Override
	public void disconnect(String s) {} //nope
	
	@Override public void a(PacketPlayInSteerVehicle p) {}
	@Override public void a(PacketPlayInFlying p) {}
	
	@Override public void sendPacket(Packet p) {}
	
}
