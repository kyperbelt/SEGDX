package com.segdx.game.events;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.SpaceNode;

public class CombatEvent extends NodeEvent{
	
	public static final String[] GENERIC_DESCRIPTIONS = new String[]{
			"There seems to be hostile readings from this part of space. Better take precautions!",
			"Something about this region of space doesn't feel right. Maybe we should avoid it."
	};
	
	
	public CombatEvent() {
		this.setGenericDescs(GENERIC_DESCRIPTIONS);
		
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
