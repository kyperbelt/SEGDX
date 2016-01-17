package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;
import com.segdx.game.tween.TableAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class SpaceEntity {

	protected Ship ship;
	protected float currentHull;
	protected SpaceNode currentNode;
	protected float currentShield;
	protected Image shieldimage;

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public float getCurrentHull() {
		return currentHull;
	}

	public void setCurrentHull(float currentHull) {
		this.currentHull = currentHull;
	}

	public SpaceNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(SpaceNode currentNode) {
		this.currentNode = currentNode;
	}

	public boolean hasShield() {
		return currentShield > 0;
	}

	public float getCurrentShield() {
		return currentShield;
	}

	public void setCurrentShield(float shield) {
		this.currentShield = shield;
		if(this.getCurrentShield()<0)
			this.setCurrentShield(0);
	}

	public float getX() {
		return ship.getX();
	}

	public float getY() {
		return ship.getY();
	}

	public void setX(float x) {
		ship.setX(x - (ship.getSprite().getWidth() / 2));
	}

	public void setY(float y) {
		ship.setY(y - (ship.getSprite().getHeight() / 2));
		;
	}

	public boolean inflictDamage(float damage, boolean isEnergy) {
		SoundManager.get().playSound(SoundManager.DAMAGE);
		if (hasShield()) {
			if (isEnergy) {
				setCurrentShield((getCurrentShield() - (damage * 4)));
			} else {
				setCurrentShield(getCurrentShield() - (damage * .5f));
			}
		} else {
			setCurrentHull(getCurrentHull() - damage);
		}

		if (this instanceof Player) {
			StateManager.get().getGameState().hullinfo.setText(this.getCurrentHull() + "/" + this.getShip().getHull());
			StateManager.get().getGameState().shieldinfo.setText("" + this.getCurrentShield());
		}

		if (getCurrentHull() <= 0) {
			return true;
		} else
			return false;
	}
	
	public void repairDamage(float repair){
		this.setCurrentHull(getCurrentHull()+repair);
		visualizeRepair(StateManager.get().getGameState(), StateManager.get().getGameState().getSpaceMap().getStage(), this);
		if(this.getCurrentHull()>this.getShip().getHull()){
			this.setCurrentHull(this.getShip().getHull());
			if(this instanceof Enemy){
				((Enemy)this).getParentCombatEvent().removeDeadEntities();
			}
		}else{
			
		}
	}

	public void poweronShields(GameState state, Stage stage, final SpaceEntity e) {
		Enemy ee = null;
		if (e instanceof Enemy)
			ee = (Enemy) e;
		final Enemy eee = ee;
		shieldimage = new Image(Assets.manager.get("map/sprshield.png", Texture.class));
		shieldimage.setSize(e.getShip().getSprite().getHeight(), e.getShip().getSprite().getHeight());
		shieldimage.setPosition(e.getX(), e.getY());
		stage.addActor(shieldimage);
		state.getSpaceMap().createEnemyFrames(
				((CombatEvent) state.getSpaceMap().getPlayer().getCurrentNode().getEvent()).getEnemies());
		state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {
			public void onExecute() {
				if (e.getCurrentShield() <= 0 || (eee != null && eee.shouldRemove())) {
					shieldimage.remove();
					this.remove();
				}
			}
		}).repeat();

	}

	public void visualizeRepair(GameState state, Stage stage, final SpaceEntity e) {
		TweenManager tm = state.getSpaceMap().getTm();
		final Table table = new Table();
		table.setSize(16, 16);
		System.out.println("healed");
		Image icon = new Image(Assets.manager.get("map/healicon.png", Texture.class));
		table.add(icon).fill();
		int rand = NodeEvent.getRandomInt(-10, 10);
		int rand2 = NodeEvent.getRandomInt(-10, -10);
		table.setPosition(e.getX() + rand, e.getY() + rand2);
		stage.addActor(table);

		Tween.to(table, TableAccessor.POSITION_Y, 1).target(table.getY() + 10).setCallback(new TweenCallback() {

			@Override
			public void onEvent(int type, BaseTween<?> arg1) {
				if (type == TweenCallback.COMPLETE)
					table.remove();
			}
		}).start(tm);

	}

	public void visualizeDamage(GameState state, Stage stage, final SpaceEntity e) {
		TweenManager tm = state.getSpaceMap().getTm();
		final Table table = new Table();
		table.setSize(16, 16);
		Image icon = new Image(Assets.manager.get("map/damageicon.png", Texture.class));
		table.add(icon).expand().fill();
		int rand = NodeEvent.getRandomInt(-10, 20);
		int rand2 = NodeEvent.getRandomInt(-10, 20);
		table.setPosition((e.getX()+e.getShip().getSprite().getWidth()/2) + rand, (e.getY()+e.getShip().getSprite().getHeight()/2) + rand2);
		stage.addActor(table);

		Tween.to(table, TableAccessor.POSITION_Y, 1).target(table.getY() + 10).setCallback(new TweenCallback() {

			@Override
			public void onEvent(int type, BaseTween<?> arg1) {
				if (type == TweenCallback.COMPLETE)
					table.remove();
			}
		}).start(tm);

	}

}
