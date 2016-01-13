package com.segdx.game.abilities;

import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.states.GameState;

public class PerformWarp extends ShipAbility{
	//TODO: Long cooldown with a naquadria requirement
	// will allow you to warp vast distances depending on the level
	//higher level has longer range but also longer cooldown and naquadria cost
	//warping costs no fuel

	@Override
	public void performAbility(SpaceEntity target) {
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		
		return false;
	}
	public void afterCooldown(GameState state) {
	}

}
