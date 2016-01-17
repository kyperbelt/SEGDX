package com.segdx.game.work;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.RestStop;
import com.segdx.game.states.GameState;

public abstract class Work {
	
	private String name;
	
	private String Desc;
	
	private RestStop parentRestStop;
	
	private boolean remove;
	
	public abstract boolean canComplete(Player p);
	public abstract Table getWorkTable(GameState state);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public boolean shouldRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public RestStop getParentRestStop() {
		return parentRestStop;
	}
	public void setParentRestStop(RestStop parentRestStop) {
		this.parentRestStop = parentRestStop;
	}
	
	
	

}
