package com.segdx.game.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.FireEnergyCannon;
import com.segdx.game.abilities.PatchUp;
import com.segdx.game.abilities.PowerShields;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.abilities.ShootRailGun;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.ships.GuillotineShip;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;

public class Berserker extends Enemy {

	int level;
	
	public Berserker(int level) {
		this.level = level;
		this.setAbilities(new Array<ShipAbility>());
		this.setShip(new GuillotineShip(level));
		this.getShip().setSpeed(8);
		this.setCurrentShield(0);
		this.setCurrentHull(getShip().getHull());
		this.setName("Beserker");
		this.getAbilities().add(new ShootRailGun(level));
		this.getAbilities().add(new ShootRailGun(level));
		this.getAbilities().add(new FireEnergyCannon(level));
		this.getAbilities().add(new FireEnergyCannon(level));
		this.getAbilities().add(new FireEnergyCannon(level));
		this.getAbilities().add(new FireEnergyCannon(level));
		
	}
	@Override
	public void update(SpaceEntity target) {
		if(this.isDisabled())
			return;
		for (int i = 0; i < getAbilities().size; i++) {
			ShipAbility a = getAbilities().get(i);
			if(a instanceof ShootRailGun){
				if(!a.isOnCooldown()){
					if(target.getCurrentShield()<=10)
					if(MathUtils.randomBoolean((((getParentevent().getParentnode().
							getMap().getDifficulty()+this.level)*.09f))))
							a.performAbility(target);
				}
			}else if(a instanceof FireEnergyCannon){
				if(!a.isOnCooldown()){
					if(target.getCurrentShield()>0)
						if(MathUtils.randomBoolean((((getParentevent().getParentnode().
								getMap().getDifficulty()+this.level)*.05f))))
								a.performAbility(target);
				}
			}
		}
	}

}
