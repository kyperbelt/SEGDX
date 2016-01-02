package com.segdx.game.entity;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class NodeEvent {
	public static final int COMBAT = 0;
	public static final int RESOURCE = 1;
	public static final int INTERACTION = 2;
	
	public static final int TRADE = 3;
	public static final int REST = 4;
	
	public static final int LOCAL = 32;
	public static final int NEARBY = 64;
	public static final int GLOBAL = 128;
	
	private int type;
	private int effect_container;
	private String description;
	private boolean RequiresExtraction;
	private int numberofInteractions;
	private Array<String> interactions;
	private Enemy enemy;
	private NodeEventAction action;
	private float occuranceRate;
	private boolean effectAplied;
	
	private Array<Resource> resources;
	
	private ObjectMap<Integer, Float> resourcerates;
	
	
	
	public int getType() {
		return type;
	}
	
	public boolean requiresEffectApplication(){
		if(effect_container > 0)
			return true;
		else return false;
	}
	
	public boolean containsEnemy(){
		if(getType()==COMBAT)
			return true;
		else return false;
	}
	
	public boolean containsResource(){
		if(getType()==RESOURCE)
			return true;
		else return false;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEffect_container() {
		return effect_container;
	}

	public void setEffect_container(int effect_container) {
		this.effect_container = effect_container;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequiresExtraction() {
		return RequiresExtraction;
	}

	public void setRequiresExtraction(boolean requiresExtraction) {
		RequiresExtraction = requiresExtraction;
	}

	public int getNumberofInteractions() {
		return numberofInteractions;
	}
	
	public void addNodeEventAction(NodeEventAction action){
		this.action = action;
	}

	public void setNumberofInteractions(int numberofInteractions) {
		this.numberofInteractions = numberofInteractions;
	}
	
	public void ExecuteEventAction(){
		if(action==null)
			return;
		action.act(this, interactions);
	}
	
	public boolean isEffectAplied() {
		return effectAplied;
	}

	public void setEffectAplied(boolean effectAplied) {
		this.effectAplied = effectAplied;
	}

	public float getOccuranceRate() {
		return occuranceRate;
	}

	public void setOccuranceRate(float occuranceRate) {
		this.occuranceRate = occuranceRate;
	}

	public Array<Resource> getResources() {
		return resources;
	}

	public void setResources(Array<Resource> resources) {
		this.resources = resources;
	}

	public ObjectMap<Integer, Float> getResourcerates() {
		return resourcerates;
	}

	public void setResourcerates(ObjectMap<Integer, Float> resourcerates) {
		this.resourcerates = resourcerates;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public interface NodeEventAction{
		
		public void act(NodeEvent event,Array<String> options);
	}
	

}
