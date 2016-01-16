package com.segdx.game.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.CycleTimer.CycleTask;
import com.segdx.game.entity.Resource;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.entity.TradePost;
import com.segdx.game.states.GameState;

public class TradePostManager {
	
	public static final int SHIP_MAX = 2;
	public static final int MODULE_MAX = 4;
	public static final int  RESOURCE_MAX = 15;

	private Array<TradePost> tradeposts;
	
	public TradePostManager(SpaceMap map) {
		tradeposts = new Array<TradePost>();
		for (int i = 0; i < map.getTradenodes().size; i++) {
			tradeposts.add(map.getTradenodes().get(i).getTradepost());
		}
		
		//Resuply the tradeposts with more Resources
		map.getTimer().addCycleTask(new CycleTask() {
			
			@Override
			public void onCycle() {
				resupplyTradePosts();
			}
			
		}).repeat().setRepeatEvery(1);
		
		//Re Adjust the prices
		map.getTimer().addCycleTask(new CycleTask() {
					
			@Override
			public void onCycle() {
					adjustModifiers();
			}
					
		}).repeat().setRepeatEvery(2);		
			
		//Put up a new shipment of Modules
		map.getTimer().addCycleTask(new CycleTask() {
			
			@Override
			public void onCycle() {
				moduleShipment();
			}
			
		}).repeat().setRepeatEvery(3);
		
		//put up a new Shipment of SpaceShips
		map.getTimer().addCycleTask(new CycleTask() {
			
			@Override
			public void onCycle() {
				shipShipment();
			}
			
		}).repeat().setRepeatEvery(4);
	}
	
	public void resupplyTradePosts(){
		for (int i = 0; i < tradeposts.size; i++) {
			for (int j = 0; j < ResourceStash.RESOURCE_IDS.length; j++) {
				Resource r = ResourceStash.RESOURCES.get(ResourceStash.RESOURCE_IDS[j]);
				float mod =tradeposts.get(i).getModifierFor(r);
				if(mod==1f)
					continue;
				else if(mod>1&&mod<2)
					tradeposts.get(i).addResource(r, MathUtils.round(MathUtils.random(1, RESOURCE_MAX*.5f)));
				else if(mod>2&&mod<4)
					tradeposts.get(i).addResource(r, MathUtils.round(MathUtils.random(3, RESOURCE_MAX*.8f)));
				else if(mod>4&&mod<=5)
					tradeposts.get(i).addResource(r, MathUtils.round(MathUtils.random(6, RESOURCE_MAX)));
			}
		}
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		if(state.getSpaceMap().getPlayer().getCurrentNode().getNodeType()==SpaceNode.TRADE)
			state.updateTradeBar();
		
	}
	
	public void adjustModifiers(){
		
	}
	
	public void moduleShipment(){
		
	}
	
	public void shipShipment(){
		
	}
}
