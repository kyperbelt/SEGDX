package com.segdx.game.abilities;

import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.states.GameState;

public class PowerShields extends ShipAbility{

	@Override
	public void performAbility(SpaceEntity target) {
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
	}



}
