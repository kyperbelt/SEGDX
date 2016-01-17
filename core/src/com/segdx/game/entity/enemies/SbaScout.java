package com.segdx.game.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.FireEnergyCannon;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.abilities.ShootRailGun;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.ships.GuillotineShip;
import com.segdx.game.entity.ships.InterceptorShip;
import com.segdx.game.managers.StateManager;

public class SbaScout extends Enemy{
	int level;
	boolean canflee;
	public SbaScout(int level) {
		canflee = true;
		this.level = level;
		this.setAbilities(new Array<ShipAbility>());
		this.setShip(new InterceptorShip(level));
		this.setCurrentShield(0);
		this.setCurrentHull(getShip().getHull());
		this.setName("SBA Scout");
		this.getAbilities().add(new ShootRailGun(1));
		this.getAbilities().add(new ShootRailGun(1));
		this.getAbilities().add(new ShootRailGun(1));
	}
	
	@Override
	public void update(SpaceEntity target) {
		
		if(this.isDisabled())
			return;
		if(canflee)
			flee();
		
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
	
	public void flee(){
		canflee = false;
		System.out.println("tried to flee");
		if(MathUtils.randomBoolean(.7f)){
			this.getParentCombatEvent().getParentnode().getMap().getTimer().addTimedTask(
					new TimedTask() {
						
						@Override
						public void onExecute() {
							canflee = true;
						}
					}).repeat(1).setSleep(2);
		}else{
			this.remove();
			this.getParentCombatEvent().setLoot(new Array<Object>());
			this.getParentCombatEvent().removeDeadEntities();
			ShipAbility.showMessage("Fled             ", "The scout fled. Maybe you could figure out a way to disable him.", StateManager.get().getGameState().skin).show(
					StateManager.get().getGameState().uistage);
		}
	}

}
