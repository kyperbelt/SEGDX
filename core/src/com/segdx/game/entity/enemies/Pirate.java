package com.segdx.game.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.PowerShields;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.abilities.ShootRailGun;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.ships.RaiderShip;
import com.segdx.game.events.CombatEvent;

public class Pirate extends Enemy{

	int level;
	public Pirate(int level) {
		this.level = level;
		this.setAbilities(new Array<ShipAbility>());
		this.setShip(new RaiderShip(level));
		this.setCurrentShield(0);
		this.setCurrentHull(getShip().getHull());
		this.setName("Pirate");
		this.getAbilities().add(new ShootRailGun(level));
		this.getAbilities().add(new PowerShields(level));
		
	}
	@Override
	public void update(SpaceEntity target) {
		
		for (int i = 0; i < getAbilities().size; i++) {
			ShipAbility a = getAbilities().get(i);
			if(a instanceof ShootRailGun){
				if(!a.isOnCooldown()){
					if(MathUtils.randomBoolean((((getParentevent().getParentnode().
							getMap().getDifficulty()+this.level)*.05f))))
							a.performAbility(target);
				}
			}else if(a instanceof PowerShields){
				if(!a.isOnCooldown()){
					if(MathUtils.randomBoolean((((getParentevent().getParentnode().
							getMap().getDifficulty()+this.level)*.01f))))
							a.performAbility(this);
				}
			}
		}
		
	}
	
	@Override
	public boolean inflictDamage(float damage, boolean isEnergy) {
		boolean b = super.inflictDamage(damage, isEnergy);
		this.getParentevent().getParentnode().getMap().createEnemyFrames(((CombatEvent)getParentevent()).getEnemies());
		return b;
	}
	

}
