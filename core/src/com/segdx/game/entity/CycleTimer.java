package com.segdx.game.entity;

import com.badlogic.gdx.utils.Array;

/**
 * Cycle timer class to keep track of time passed in game
 * and handle events when certain cycles or time has passed.
 * @author Jonathan Camarena -kyperbelt
 *
 */
public class CycleTimer {
	
	public static final int SLOW_CYCLE = 20;
	public static final int NORMAL_CYCLE = 75;
	public static final int FAST_CYCLE = 45;
	
	private float cycle_length;
	private float elapsed_time;
	private int cycle_number;
	
	private boolean paused;
	
	private Array<CycleTask> cycletasks;
	private Array<TimedTask> timedtasks;
	
	public CycleTimer(){
		cycletasks = new Array<CycleTimer.CycleTask>();
		timedtasks = new Array<CycleTimer.TimedTask>();
		cycle_length =0;
		elapsed_time = 0;
		cycle_number = 0;
		
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void setPaused(boolean pause){
		this.paused = pause;
	}
	
	public CycleTask addCycleTask(CycleTask task){
		this.cycletasks.add(task);
		return task;
	}
	
	public TimedTask addTimedTask(TimedTask task){
		this.timedtasks.add(task);
		return task;
	}
	
	public void setCycleLength(float cyclelength){
		this.cycle_length = cyclelength;
	}
	
	public int getCurrentCycle(){
		return cycle_number;
	}
	
	public float getTimeLeft(){
		return cycle_length-elapsed_time;
	}
	
	public void update(float delta){
		if(paused)
			return;
		elapsed_time+=delta;
		
		if(cycle_length<elapsed_time){
			cycle_number++;
			elapsed_time = elapsed_time-cycle_length;
			for (int i = 0; i < cycletasks.size; i++) {
				if(cycletasks.get(i).shouldremove)
					cycletasks.removeIndex(i);
				else
					cycletasks.get(i).resetFired();
			}
		}
		
		for (int i = 0; i < cycletasks.size; i++) {
			cycletasks.get(i).executeCycleTask(cycle_number);
		}
		
		for (int i = 0; i < timedtasks.size; i++) {
			if(timedtasks.get(i).shouldRemove())
				timedtasks.removeIndex(i);
			else
				timedtasks.get(i).executeTimedTask(delta);
		}
	}
	
	public static abstract class CycleTask{
		
		public static final int REPEATFOREVER = -1;
		
		private int repeatTimes = 0;
		
		private int repeatEvery = 0;
		
		private int currentRepeats = 0;
		
		private int startCycle = 0;
		
		private boolean fired;
		private boolean shouldremove = false;
		
		public abstract void onCycle();
		
		public void executeCycleTask(int currentCycle){
			if(fired==true)
				return;
			if(startCycle==currentCycle){
				onCycle();
				startCycle+= repeatEvery;
			}
			fired = true;
			currentRepeats++;
			
			if(repeatTimes != -1 && currentRepeats >= repeatTimes){
				shouldremove = true;
			}
		}
		
		public boolean shouldRemove(){
			return shouldremove;
		}
		
		public void remove(){
			shouldremove = true;
		}
		
		public void resetFired(){
			this.fired = false;
		}
		
		public boolean isRepeats(){
			if(repeatTimes!=0)
				return true;
			else return false;
		}
		
		public CycleTask repeat(){
			return this.repeat(-1);
		}
		
		public CycleTask repeat(int repeattimes){
			return this.repeat(repeattimes, 1);
		}
		
		public CycleTask repeat(int repeatTimes,int reapeatEvery){
			this.repeatTimes = repeatTimes;
			this.repeatEvery = reapeatEvery;
			return this;
		}

		public int getCurrentRepeats() {
			return currentRepeats;
		}

		public void setCurrentRepeats(int currentRepeats) {
			this.currentRepeats = currentRepeats;
		}

		public int getRepeatEvery() {
			return repeatEvery;
		}

		public void setRepeatEvery(int repeatEvery) {
			this.repeatEvery = repeatEvery;
		}

		public int getStartCycle() {
			return startCycle;
		}

		public CycleTask startCycle(int startCycle) {
			this.startCycle = startCycle;
			return this;
		}
		
	}
	
	public static abstract class TimedTask{
		
		public static final int REPEATFOREVER = -1;
		
		private float sleeptime=1f;
		
		private int repeatTimes = 0;
		
		private int currentRepeats = 0;
		
		private float elapsedtime = 0;
		
		private boolean shouldRemove = false;
		
		public abstract void onExecute();
		
		public void executeTimedTask(float delta){
			elapsedtime+=delta;
			if(elapsedtime > sleeptime){
				onExecute();
				elapsedtime= elapsedtime-sleeptime;
				currentRepeats++;
				
				if(repeatTimes!=-1 && currentRepeats>=repeatTimes){
					shouldRemove = true;
				}
					
			}
				
		}
		
		public int getCurrentRepeats(){
			return currentRepeats;
		}
		
		public TimedTask setSleep(float sleep){
			this.sleeptime = sleep;
			return this;
		}
		
		public boolean shouldRemove(){
			return this.shouldRemove;
		}
		
		public void remove(){
			shouldRemove = true;
		}
		
		public TimedTask repeat(){
			return this.repeat(-1);
		}
		
		public TimedTask repeat(int repeatTimes){
			this.repeatTimes = repeatTimes;
			return this;
		}
		
	}

}
