package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.managers.StateManager;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

/**
 * abilities that the ship can have
 * 
 * @author Jonathan Camarena -kyperbelt
 *
 */
public abstract class ShipAbility {

	// TODO: make base ship ability class and then extend it into individual
	// ability types
	// abstract class performAbility(Target)

	private String name;
	private String desc;
	private ShipModule parentModule;
	private int id;
	protected int cooldown;
	private int cctimer = 0;
	private boolean enabled;
	private boolean onCooldown;
	private boolean combatCappable;
	private boolean outOfCombat;
	private boolean attack;
	private boolean playerUsed;
	private int level;

	public abstract void performAbility(SpaceEntity target);

	public abstract boolean requirementsMet(SpaceEntity target);

	public abstract void afterCooldown(GameState state);

	public String getName() {
		return name + " " + level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public boolean hasCooldown() {
		return (cooldown > 0);
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public int getCooldownLeft() {
		return getCooldown() - cctimer;
	}

	public void startCooldown() {
		setOnCooldown(true);
		final GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {

			@Override
			public void onExecute() {
				cctimer++;
				if (getParentModule() != null) {

					state.updateAbilities();
					state.updateActionbar();
				}

				if (cctimer >= getCooldown()) {
					cctimer = 0;

					afterCooldown(state);
					setOnCooldown(false);
					if (getParentModule() != null) {
						state.updateAbilities();
						state.updateActionbar();
					}

				}

			}
		}).repeat(this.getCooldown());
	}

	public boolean hasParentModule() {
		return (parentModule != null);
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCombatCappable() {
		return combatCappable;
	}

	public void setCombatCappable(boolean combatCappable) {
		this.combatCappable = combatCappable;
	}

	public boolean isOutOfCombat() {
		return outOfCombat;
	}

	public void setOutOfCombat(boolean outOfCombat) {
		this.outOfCombat = outOfCombat;
	}

	public boolean isPlayerUsed() {
		return playerUsed;
	}

	public void setPlayerUsed(boolean playerUsed) {
		this.playerUsed = playerUsed;
	}

	public ShipModule getParentModule() {
		return parentModule;
	}

	public void setParentModule(ShipModule parentModule) {
		this.parentModule = parentModule;
	}

	public int getCooldown() {
		return MathUtils.round(cooldown * (getLevel()));
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOnCooldown() {
		return onCooldown;
	}

	public void setOnCooldown(boolean onCooldown) {
		this.onCooldown = onCooldown;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static Dialog showMessage(String title, String content, Skin skin) {
		Dialog d = new Dialog(title, skin);
		d.setSize(200, 200);
		d.getTitleLabel().setFontScale(.7f);
		Label l = new Label(content, skin);
		l.setFontScale(.5f);
		l.setWrap(true);
		d.getContentTable().add(l).center().expand().fill();
		d.button("OK", true);
		((TextButton) d.getButtonTable().getChildren().get(0)).getLabel().setFontScale(.7f);

		return d;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

}
