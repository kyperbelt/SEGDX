package com.segdx.game.events;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.SpaceNode;

public class HelpEvent extends NodeEvent{
	
	public static String[] GENERIC_DESCRIPTIONS = new String[]{
			"You can detect a faint distress signal coming from this region of space.",
			"Looks like someone is in some need of assistance, Maybe if you help you will be rewarded."
	};

	public HelpEvent() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void update() {
	}

	@Override
	public void createGossip(Array<SpaceNode> nodes) {
	}

	@Override
	public void applyEconomics(Array<SpaceNode> nearbytradenodes) {
	}

	@Override
	public void removeNodeEvent() {
	}

}
