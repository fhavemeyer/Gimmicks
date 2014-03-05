package com.gimmicknetwork.gimmicks.killstreaks;

import com.gimmicknetwork.gimmicks.GimmickModule;

public class KillStreaks extends GimmickModule {
	private KillStreaksManager killStreaksManager = new KillStreaksManager();
	
	public KillStreaks() { }
	
	@Override
	public void enable() {
		killStreaksManager = new KillStreaksManager();
		gimmickPlugin.registerModuleForEvents(this, new KillStreaksEventListener(killStreaksManager, 
																				gimmickPlugin, 
																				gimmickPlugin.getConfig().getBoolean("facedeathmessages.enabled", true)));
	}
}
