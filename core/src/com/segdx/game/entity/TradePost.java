package com.segdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class TradePost {
	
	public static final float MIN = 1.1f;
	public static final float MAX = 5f;
	
	//buy modifiers for the different types of resoruces
	private ObjectMap<Integer, Float> buymodifiers;

	//the resources at this tradepost
	private IntIntMap resources;
	
	public TradePost(){
		
		resources = new IntIntMap();
		resources.put(ResourceStash.DRIDIUM.getId(), 0);
		resources.put(ResourceStash.KNIPTORYTE.getId(), 0);
		resources.put(ResourceStash.LATTERIUM.getId(), 0);
		resources.put(ResourceStash.NAQUIDRA.getId(), 0);
		resources.put(ResourceStash.SALVAGE.getId(), 0);
		
		
		buymodifiers = new ObjectMap<Integer, Float>();
		buymodifiers.put(ResourceStash.DRIDIUM.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.KNIPTORYTE.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.LATTERIUM.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.NAQUIDRA.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.SALVAGE.getId(),MathUtils.random(MIN, MAX));
		
		
		
	}
	
	public boolean isEventModified(Resource r){
		if(getModifierFor(r)==1f)
			return true;
		else return false;
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
	
	public boolean isResourceAvailbale(Resource resource,int amount){
		if(resources.get(resource.getId(), 0)>amount)
			return true;
		
		return false;
	}
	
	public Resource getResource(Resource resource){
		removeResources(resource, 1);
		return ResourceStash.RESOURCES.get(resource.getId()).clone();
	}
	
	public void removeResources(Resource resource,int amount){
		resources.getAndIncrement(resource.getId(), 0, -amount);
		if(resources.get(resource.getId(), 0)<0)
			resources.put(resource.getId(), 0);
	}
	
	public void addResource(Resource resource){
		this.addResource(resource, 1);
	}
	
	public void addResource(Resource resource,int amount){
		resources.getAndIncrement(resource.getId(), 0, amount);
		if(resources.get(resource.getId(), 0)>99)
			resources.put(resource.getId(), 99);
	}

	public IntIntMap getResources() {
		return resources;
	}

	public void setResources(IntIntMap resources) {
		this.resources = resources;
	}

}
