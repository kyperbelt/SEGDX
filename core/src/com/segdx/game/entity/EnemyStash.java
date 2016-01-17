package com.segdx.game.entity;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.enemies.Berserker;
import com.segdx.game.entity.enemies.EmpireGrunt;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.entity.enemies.Pirate;
import com.segdx.game.entity.enemies.SbaScout;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class EnemyStash {
	public static final int PIRATE = 0;
	public static final int REBEL = 1;
	public static final int BERSERKER = 2;
	public static final int EMPIRE_ENFORCER = 3;
	public static final int SBA_SCOUT = 4;
	public static final int SBA_MERC = 5;
	
	private static EnemyStash stash;
	
	private Array<Integer> enemies;
	
	public static EnemyStash get(){
		if(stash == null){
			stash = new EnemyStash();
		}
		return stash;
	}
	
	public EnemyStash() {
		enemies = new Array<Integer>();
		enemies.add(PIRATE);
		enemies.add(REBEL);
		enemies.add(BERSERKER);
		enemies.add(EMPIRE_ENFORCER);
		enemies.add(SBA_MERC);
		enemies.add(SBA_SCOUT);
	}
	
	public Enemy getRandomEnemy(){
		int type =enemies.get(NodeEvent.getRandomInt(0, enemies.size-1));
		return getNewEnemy(type);
	}
	
	public Enemy getNewEnemy(int enemy){
		Enemy e = null;
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		int level =  getEnemyLevel(state.getSpaceMap().getTimer().getCurrentCycle(), state.difficulty);
		switch (enemy) {
		case PIRATE:
				e = new Pirate(getEnemyLevel(state.getSpaceMap().getTimer().getCurrentCycle(), state.difficulty));
			break;
		case REBEL:
			break;
		case BERSERKER:
			e = new Berserker(getEnemyLevel(state.getSpaceMap().getTimer().getCurrentCycle(), state.difficulty));
			break;
		case EMPIRE_ENFORCER:
			e = new EmpireGrunt(level);
			break;
		case SBA_SCOUT:
			e = new SbaScout(level);
			break;
		case SBA_MERC:
			break;
		default:
			break;
		}
		
		return e;
		
	}
	
	public static int getEnemyLevel(int cycle,int difficulty){
		int s = cycle/10;
		if(s>3)
			s=3;
		return NodeEvent.getRandomInt(1+s, 2+difficulty+s);
	}

}
