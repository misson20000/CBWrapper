package misson20000.plugins.cbwrapper;

import net.minecraft.server.v1_8_R1.EnumProtocolDirection;
import net.minecraft.server.v1_8_R1.NetworkManager;

public class DummyNetworkManager extends NetworkManager {
	public DummyNetworkManager(EnumProtocolDirection enumprotocoldirection) {
		super(enumprotocoldirection);
	}
}
