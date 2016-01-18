package com.segdx.game.managers;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.CycleTimer.CycleTask;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.work.ANewTomorrow;
import com.segdx.game.work.FasterThanLight;
import com.segdx.game.work.ScannerResearch;
import com.segdx.game.work.WayToPeace;
import com.segdx.game.work.Work;

public class WorkManager {
	
	private Array<Work> work;
	
	private Array<SpaceNode> worklessnodes;
	
	public WorkManager() {
		work = new Array<Work>();
		work.add(new ANewTomorrow());
		work.add(new ScannerResearch());
		work.add(new WayToPeace());
		work.add(new FasterThanLight());
		
		SpaceMap map = StateManager.get().getGameState().getSpaceMap();
		worklessnodes = new Array<SpaceNode>();
		setWorklessnodes(map.getRestnodes());
		
		map.getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				update();
			}
		}).repeat().setSleep(1);
		
		map.getTimer().addCycleTask(new CycleTask() {
			
			@Override
			public void onCycle() {
				assignWork();
			}
		}).repeat().setRepeatEvery(3);
	}
	
	public void assignWork(){
		for (int i = 0; i < worklessnodes.size; i++) {
			if(work.size>0){
				work.peek().setParentRestStop(worklessnodes.get(i).getReststop());
				worklessnodes.get(i).getReststop().setWork(work.pop());
				worklessnodes.removeIndex(i);
			}
		}
		
	}
	
	public void update(){
		for (int i = 0; i < work.size; i++) {
			Work w = work.get(i);
			if(w.shouldRemove()){
				worklessnodes.add(w.getParentRestStop().getParent());
				w.getParentRestStop().setWork(null);
			}
		}
		
	}

	public Array<SpaceNode> getWorklessnodes() {
		return worklessnodes;
	}

	public void setWorklessnodes(Array<SpaceNode> worklessnodes) {
		Array<SpaceNode> readynodes = new Array<SpaceNode>();
		for (int i = 0; i < worklessnodes.size; i++) {
			if(worklessnodes.get(i).getReststop().getWork()!=null)
				continue;
			readynodes.add(worklessnodes.get(i));
		}
		this.worklessnodes = readynodes;
	}

	public Array<Work> getWork() {
		return work;
	}

	public void setWork(Array<Work> work) {
		this.work = work;
	}
	
	

}
