package com.segdx.game.entity;

public class CycleTimer {
	
	public static final int SLOW_CYCLE = 240;
	public static final int NORMAL_CYCLE = 180;
	public static final int FAST_CYCLE = 120;
	
	private float cycle_length;
	private float elapsed_time;
	private int cycle_number;
	
	public CycleTimer(){
		cycle_length =0;
		elapsed_time = 0;
		cycle_number = 0;
		
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
		elapsed_time+=delta;
		
		if(cycle_length<elapsed_time){
			cycle_number++;
			elapsed_time = elapsed_time-cycle_length;
		}
	}

}
