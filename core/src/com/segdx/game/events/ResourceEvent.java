package com.segdx.game.events;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.Resource;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.entity.TradePost;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class ResourceEvent extends NodeEvent{
	
	public static final String[] GENERIC_DESCRIPTIONS = new String[]{
			"We are picking up readings of a small comet in this area, might be worth checking out!",
			"Looks like there is a lot of activity in this region of space. Could be something valuable..."
	};
	
	//the type of resource held here
	private int resource;
	
	private int resourcesAvailable;
	
	private int startCycle;
	
	private Array<SpaceNode> tradenodes;
	
	public ResourceEvent(SpaceNode node) {
		SpaceMap map = node.getMap();
		resource = ResourceStash.randomID();
		System.out.println("created a "+ResourceStash.RESOURCES.get(resource).getName()+" event node");
		resourcesAvailable = MathUtils.round(MathUtils.random(3, 20));
		this.setName("");
		tradenodes = new Array<SpaceNode>();
		startCycle = map.getTimer().getCurrentCycle();
		this.setCycleDuration(MathUtils.round(MathUtils.random(1, 4)));
		this.setShouldRemove(false);
		this.setDescription(this.generateDescription());
		this.setParentnode(node);
	}
	
	public Resource fetchResource(){
		resourcesAvailable--;
		return ResourceStash.RESOURCES.get(resource).clone();
	}

	@Override
	public void update() {
		if(resourcesAvailable<=0 ||(getParentnode().getMap().getTimer().getCurrentCycle()-startCycle)>=getCycleDuration())
			setShouldRemove(true);
	}

	@Override
	public void createGossip(Array<SpaceNode> nodes) {
		if(nodes.size == 0)
			return;
		
	}

	@Override
	public void applyEconomics(Array<SpaceNode> nearbytradenodes) {
		if(resourcesAvailable < 10 || nearbytradenodes.size == 0)
			return;
		for (int i = 0; i < nearbytradenodes.size; i++) {
			nearbytradenodes.get(i).getTradepost().setModifierFor(ResourceStash.RESOURCES.get(resource), 1.f);
		}
		tradenodes = nearbytradenodes;
			
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public int getResourcesAvailable() {
		return resourcesAvailable;
	}

	public void setResourcesAvailable(int resourcesAvailable) {
		this.resourcesAvailable = resourcesAvailable;
	}

	@Override
	public void removeNodeEvent() {
		Player p = null;
		if(tradenodes.size > 0)
			p = tradenodes.get(0).getMap().getPlayer();
		for (int i = 0; i < tradenodes.size; i++) {
			tradenodes.get(i).getTradepost().setModifierFor(ResourceStash.RESOURCES.get(resource), MathUtils.random(TradePost.MIN, TradePost.MAX));
			if(tradenodes.get(i).getIndex()==p.getCurrentNode().getIndex())
				((GameState)StateManager.get().getState(StateManager.GAME)).updateTradeBar();
		}
	}
	
	public String generateDescription(){
		String desc = "There is a ";
		
		if(resource!=ResourceStash.SALVAGE.getId()){
			if(this.getResourcesAvailable() > 10){
				desc+="rich deposit ";
			}else if(this.getResourcesAvailable() < 10){
				desc+="deposit ";
			}
			desc+="of "+ResourceStash.RESOURCES.get(resource).getName()+" in the area.";
		}else{
			desc+="wreckage in the vicinity, maybe you could get make off with some salvage.";
		}
		
		
		
		return desc;
	}

}
