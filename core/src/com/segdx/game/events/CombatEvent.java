package com.segdx.game.events;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;
import com.segdx.game.entity.EnemyStash;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceNode;

public class CombatEvent extends NodeEvent{
	
	public static final int PIRATE_AMBUSH = 0;
	public static final int BESERKER_ATTACK= 1;
	public static final int EMPIRE_BULLY_FORCE  = 2;
	public static final int REBEL_INSURGENCY = 3;
	public static final int SBA_SCOUT = 4;
	public static final int SBA_ATTACK_FORCE = 5;
	public static final int LONE_PIRATE = 6;
	public static final int BESERKER_FLOCK = 7;
	
	public static final int ENEMY_CAP = 3;
	
	public static final String[] GENERIC_DESCRIPTIONS = new String[]{
			"There seems to be hostile readings from this part of space. Better take precautions!",
			"Something about this region of space doesn't feel right. Maybe we should avoid it."
	};
	
	private int combatType;
	
	private int coercion;
	
	private String failcoerce;
	
	private String successcoerce;
	
	private Array<Enemy> enemies;
	
	private Array<SpaceNode> nodes;
	
	public CombatEvent(SpaceNode node) {
		enemies = new Array<Enemy>();
		nodes = new Array<SpaceNode>();
		this.setGenericDescs(GENERIC_DESCRIPTIONS);
		this.setCycleDuration(NodeEvent.getRandomInt(3, 6));
		this.setStartCycle(node.getMap().getTimer().getCurrentCycle());
		this.setParentnode(node);
		combatType = PIRATE_AMBUSH;
		setCoercion(0);
		this.setDescription(this.generateDesc());
		this.getParentnode().addEntry(NodeEvent.HELP_LOG_ENTRY, "Combat Event initiated.");
		
		
		
	}
	
	public void removeDeadEntities(){
		for (int i = 0; i < enemies.size; i++) {
			if(enemies.get(i).shouldRemove())
				enemies.removeIndex(i);
		}
		getParentnode().getMap().createEnemyFrames(enemies);
	}

	@Override
	public void update() {
		if(!hasCycles() || enemies.size==0)
			this.setShouldRemove(true);
			
		if(getParentnode().getMap().getPlayer().getCurrentNode().getIndex()==getParentnode().getIndex()){
			if(!getParentnode().getMap().getPlayer().isTravelDisabled()&&!getParentnode().getMap()
					.getPlayer().isTraveling())
				getParentnode().getMap().getPlayer().setTravelDisabled(true);
			if(SoundManager.get().isInPlayList())
				SoundManager.get().playMusic(SoundManager.BOARDINGPARTY);
			for (int i = 0; i < enemies.size; i++) {
				if(getParentnode().getMap().getPlayer().isIncombat())
					enemies.get(i).update(getParentnode().getMap().getPlayer());
			}
		}	
	}

	@Override
	public void createGossip(Array<SpaceNode> nodes) {
	}

	@Override
	public void applyEconomics(Array<SpaceNode> nearbytradenodes) {
		
		nodes = nearbytradenodes;
		
		this.getParentnode().getMap().getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				for (int i = 0; i < nodes.size; i++) {
					nodes.get(i).getTradepost().removeResources(ResourceStash.RESOURCES.get(
							ResourceStash.randomID()),NodeEvent.getRandomInt(1, 4));
				}
				if(shouldRemove())
					this.remove();
			}
		}).repeat().setSleep(10);
		
	}

	@Override
	public void removeNodeEvent() {
		
		
		Player p = ((GameState)StateManager.get().getState(StateManager.GAME)).getSpaceMap().getPlayer();
		if(getParentnode().getIndex()==p.getCurrentNode().getIndex()){
			
			p.setIncombat(false);
			p.setTravelDisabled(false);
			((GameState)StateManager.get().getState(StateManager.GAME)).updateActionbar();
			((GameState)StateManager.get().getState(StateManager.GAME)).updateAbilities();
			SoundManager.get().playMusicList();
		}
	}
	
	public Array<Enemy> getEnemies(){
		return enemies;
	}
	
	public String generateDesc(){
		String desc = "There ";
		switch (combatType) {
		case PIRATE_AMBUSH:
				for (int i = 0; i < ENEMY_CAP; i++) {
					enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.PIRATE));
				}
				setCoercion(4);
				desc+=" was an ambush of pirates laying in wait. ";
				setSuccesscoerce("The pirates have accepted your terms! Phew.. that was a close one.");
				setFailcoerce("They are not satisfied with what you offered and have begun their attack!");
				
			break;
		case BESERKER_ATTACK:
			enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.BERSERKER));
			desc+="is a Lone [FIREBRICK]Beserker[] coming at you! There is no reasoning with this bloodthirsty heathen.";
			setCoercion(0);
			setSuccesscoerce("nope");
			setFailcoerce("You really need to start reading man! You cant reason with these monsters...");
			break;
		case EMPIRE_BULLY_FORCE:
			for (int i = 0; i < ENEMY_CAP; i++) {
				enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.EMPIRE_ENFORCER));
			}
			setCoercion(5);
			setSuccesscoerce("Looks like they were just having a bad day. Good thing that didnt escalate.");
			setFailcoerce("You just managed to anger them! Way to go!");
			desc+=" is a small force of Empire jerks looking for trouble. Maybe they need to be taught a lesson.";
			break;
		case SBA_ATTACK_FORCE:
			for (int i = 0; i < ENEMY_CAP; i++) {
				enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.SBA_MERC));
			}
			setCoercion(3);
			setSuccesscoerce("That was a close one... Good thing you are good with words.");
			setFailcoerce("They appear to ignore your plea. Get ready for a fight!");
			desc+="an SBA force is moving in the area .Looks like the Shak't Belt Alliance against the Empire. Best to just avoid conflict.!";
			break;
		case SBA_SCOUT:
			setCoercion(9);
			setSuccesscoerce("You didn't even get to finish before he started to run away.");
			setFailcoerce("Looks like the scout thinks he stands a chance... ");
			enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.SBA_SCOUT));
			desc="You spot an SBA scout. He doesnt seem to be interested in conflict, But letting him go might bring more unwanted SBA affiliates.";
			break;
		case BESERKER_FLOCK:
			for (int i = 0; i < ENEMY_CAP; i++) {
				enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.BERSERKER));
			}
			setSuccesscoerce("nope");
			setFailcoerce("They begin their assault, with even more ferocity now. How dare you insult their bloodlust");
			setCoercion(0);
			desc="A flock of [FIRERED]Beserkers[] was passing through this area. ";
			break;
		case LONE_PIRATE:
			setCoercion(7);
			setSuccesscoerce("The pirate vessel takes your money. he looked like he was starving.");
			setFailcoerce("");
			enemies.add(EnemyStash.get().getNewEnemy(EnemyStash.PIRATE));
			desc+="is a lone pirate vessel. If you cant avoid it the next best thing is paying it off.";
			break;
		default:
			break;
		}
		
		for (int i = 0; i < enemies.size; i++) {
			enemies.get(i).setParentevent(this);
			enemies.get(i).setName(enemies.get(i).getName()+" "+(i+1));
			enemies.get(i).setX((getParentnode().getX()-(enemies.get(i).getShip().getSprite().getWidth()))
					+((enemies.get(i).getShip().getSprite().getWidth()*2)*i));
			enemies.get(i).setY(getParentnode().getY()+(enemies.get(i).getShip().getSprite().getHeight()*4));
		}
		return desc;
	}
	
	public boolean attemptCoerce(){
		int ran = getRandomInt(1, 10);
		if(ran>0&&ran<getCoercion()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean attemptFlee(float speed){
		float m = (speed-enemies.get(0).getShip().getSpeed())*.1f;
		if(m<-.5f)
			m=-.4f;
		if(m>.5f)
			m=.5f;
		return MathUtils.randomBoolean(.5f+m);
	}

	public int getCombatType() {
		return combatType;
	}

	public void setCombatType(int combatType) {
		this.combatType = combatType;
	}

	public int getCoercion() {
		return coercion;
	}
	
	@Override
	public boolean hasCycles() {
		
		return super.hasCycles()||(getParentnode().getIndex() == getParentnode().getMap().getPlayer().getCurrentNode().getIndex());
	}

	public void setCoercion(int coercion) {
		this.coercion = coercion;
	}

	public String getFailcoerce() {
		return failcoerce;
	}

	public void setFailcoerce(String failcoerce) {
		this.failcoerce = failcoerce;
	}

	public String getSuccesscoerce() {
		return successcoerce;
	}

	public void setSuccesscoerce(String successcoerce) {
		this.successcoerce = successcoerce;
	}

}
