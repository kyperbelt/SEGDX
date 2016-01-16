package com.segdx.game.managers;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.segdx.game.entity.CycleTimer.CycleTask;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.events.ResourceEvent;
import com.segdx.game.states.GameState;

public class NodeEventManager {
	
	private ObjectMap<Integer,NodeEvent> events;
	private Array<SpaceNode> eventlessnodes;
	private Array<SpaceNode> nodeswithevents;
	
	
	private int updateEventEvery;
	
	private int removeEventsEvery;
	
	public NodeEventManager(int updateEventsEvery,int removeEventsEvery,int createEventsEvery) {
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		events = new ObjectMap<Integer, NodeEvent>();
		eventlessnodes = new Array<SpaceNode>();
		nodeswithevents = new Array<SpaceNode>();
		this.setUpdateEventEvery(updateEventsEvery);
		this.setRemoveEventsEvery(removeEventsEvery);
		
		
		
		//createRemoveTask
		state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				
				removeEvents();
			}
		}).setSleep(2f).repeat();
		
		//createCreateTask
		state.getSpaceMap().getTimer().addCycleTask(new CycleTask() {
			public void onCycle() {
				createNewEvents();
			}
		}).repeat().setRepeatEvery(1);
		
		//createUpdateTask
		state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				update();
			}
		}).repeat().setSleep(.3f);
		
		for (int i = 0; i < state.getSpaceMap().getPassivenodes().size; i++) {
			eventlessnodes.add(state.getSpaceMap().getPassivenodes().get(i));
		}
	}
	
	public void update(){
		
		Values<NodeEvent> values=events.values();
		while (values.hasNext()) {
			NodeEvent e = values.next();
			if(e.isShouldRemove())
				continue;
			else 
				e.update();
		}
		
	}
	
	//change the event at a node
	public void changeNodeEvent(NodeEvent e,SpaceNode node){
		e.setId(node.getEvent().getId());
		node.getEvent().removeNodeEvent();
		node.setEvent(e);
		events.put(e.getId(), e);
		((GameState)StateManager.get().getState(StateManager.GAME)).updateActionbar();
		((GameState)StateManager.get().getState(StateManager.GAME)).updateAbilities();
	}
	
	public void createNewEvents(){
		Array<Integer> remove = new Array<Integer>();
		
		for (int i = 0; i < eventlessnodes.size; i++) {
			if(generateRandomeEvent(eventlessnodes.get(i))){
				remove.add(eventlessnodes.get(i).getEvent().getId());
				if(eventlessnodes.get(i).getMap().getPlayer().getCurrentNode().getIndex()==
						eventlessnodes.get(i).getIndex()){
					((GameState)StateManager.get().getState(StateManager.GAME)).updateActionbar();
				((GameState)StateManager.get().getState(StateManager.GAME)).updateAbilities();
				}
				nodeswithevents.add(eventlessnodes.removeIndex(i));
			}
		}
		
	}
	
	public boolean generateRandomeEvent(SpaceNode node){
		int eventtype = MathUtils.round(MathUtils.random(0, 2));
		
		Circle circle = null;
		int id = events.size;
		Array<SpaceNode> nearbytradenodes = null;
		switch (eventtype) {
		case NodeEvent.RESOURCE:
			ResourceEvent e = new ResourceEvent(node);
			e.setType(eventtype);
			node.setEvent(e);
			while(events.containsKey(id)){
				id++;
			}
			e.setId(id);
			events.put(e.getId(), e);
			circle = node.getEffectradius();
			nearbytradenodes = new Array<SpaceNode>();
			for (int i = 0; i < node.getMap().getTradenodes().size; i++) {
				SpaceNode n = node.getMap().getTradenodes().get(i);
				if(circle.contains(n.getX(), n.getY())){
					nearbytradenodes.add(n);
				}
			}
			e.applyEconomics(nearbytradenodes);
			return true;
		case NodeEvent.HELP:
			return false;
		case NodeEvent.COMBAT:
			CombatEvent c = new CombatEvent(node);
			c.setType(eventtype);
			node.setEvent(c);
			id = events.size;
			while(events.containsKey(id)){
				id++;
			}
			c.setId(id);
			events.put(c.getId(), c);
			circle = node.getEffectradius();
			nearbytradenodes = new Array<SpaceNode>();
			for (int i = 0; i < node.getMap().getTradenodes().size; i++) {
				SpaceNode n = node.getMap().getTradenodes().get(i);
				if(circle.contains(n.getX(), n.getY())){
					nearbytradenodes.add(n);
				}
			}
			c.applyEconomics(nearbytradenodes);
			return true;
		default:
			break;
		}
		
		return false;
	}
	
	public void removeEvents(){
		for (int i = 0; i < nodeswithevents.size; i++) {
			if(nodeswithevents.get(i).getEvent().isShouldRemove()){
				
				NodeEvent e = nodeswithevents.get(i).getEvent();
				events.remove(nodeswithevents.get(i).getEvent().getId());
				nodeswithevents.get(i).setEvent(null);
				e.removeNodeEvent();
				eventlessnodes.add(nodeswithevents.removeIndex(i));
			}
		}
		
		
	}

	public int getUpdateEventEvery() {
		return updateEventEvery;
	}

	public void setUpdateEventEvery(int updateEventEvery) {
		this.updateEventEvery = updateEventEvery;
	}

	public int getRemoveEventsEvery() {
		return removeEventsEvery;
	}

	public void setRemoveEventsEvery(int removeEventsEvery) {
		this.removeEventsEvery = removeEventsEvery;
	}

	public ObjectMap<Integer, NodeEvent> getEvents() {
		return events;
	}

	public void setEvents(ObjectMap<Integer, NodeEvent> events) {
		this.events = events;
	}

	public Array<SpaceNode> getEventlessnodes() {
		return eventlessnodes;
	}

	public void setEventlessnodes(Array<SpaceNode> eventlessnodes) {
		this.eventlessnodes = eventlessnodes;
	}

	public Array<SpaceNode> getNodeswithevents() {
		return nodeswithevents;
	}

	public void setNodeswithevents(Array<SpaceNode> nodeswithevents) {
		this.nodeswithevents = nodeswithevents;
	}


}
