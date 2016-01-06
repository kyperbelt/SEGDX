package com.segdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class TradePost {
	
	public static final float MIN = 1.1f;
	public static final float MAX = 5f;
	
	//buy modifiers for the different types of resoruces
	private ObjectMap<Integer, Float> buymodifiers;

	//the resources at this tradepost
	private ObjectMap<Integer, Array<Resource>> resources;
	
	public TradePost(){
		setResources(new ObjectMap<Integer, Array<Resource>>());
		resources.put(ResourceStash.DRIDIUM.getId(), new Array<Resource>());
		resources.put(ResourceStash.KNIPTORYTE.getId(), new Array<Resource>());
		resources.put(ResourceStash.LATTERIUM.getId(), new Array<Resource>());
		resources.put(ResourceStash.NAQUIDRA.getId(), new Array<Resource>());
		resources.put(ResourceStash.SALVAGE.getId(), new Array<Resource>());
		
		
		buymodifiers = new ObjectMap<Integer, Float>();
		buymodifiers.put(ResourceStash.DRIDIUM.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.KNIPTORYTE.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.LATTERIUM.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.NAQUIDRA.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.SALVAGE.getId(),MathUtils.random(MIN, MAX));
		
	}
	
	public float getModifierFor(Resource resource){
		return buymodifiers.get(resource.getId());
	}
	
	public void setModifierFor(Resource resource,float modifier){
		buymodifiers.remove(resource.getId());
		buymodifiers.put(resource.getId(), modifier);
	}
	
	public int getResourceBuyPrice(Resource resource){
		return (int) (resource.getBasevalue()*getModifierFor(resource));
	}
	
	public int getResourceSellPrice(Resource resource){
		return (int) ((resource.getBasevalue()*getModifierFor(resource))*.9f);
	}

	public ObjectMap<Integer, Array<Resource>> getResources() {
		return resources;
	}

	public void setResources(ObjectMap<Integer, Array<Resource>> resources) {
		this.resources = resources;
	}

}
