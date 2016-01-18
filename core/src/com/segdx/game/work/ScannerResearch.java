package com.segdx.game.work;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.entity.Player;
import com.segdx.game.modules.ScannerModule;
import com.segdx.game.states.GameState;

public class ScannerResearch extends Work {
	
	public ScannerResearch() {
		this.setName("Scanner Research!");
		this.setDesc("Small talking with a man at this location he tells you of the work he used to do "
				+ "for some of the corperations of the Shakt Belt Alliance. Scanner Research he says."
				+ "He likes you and says if you bring manage to bring him one he will upgrade it for you "
				+ "free of charge.");
	}

	@Override
	public boolean canComplete(Player p) {
		if(p.containsModuleClass(new ScannerModule(0)))
			return true;
		return false;
	}

	@Override
	public Table getWorkTable(final GameState state) {
		Table t = new Table();
		Table infotable = new Table();
		Label name  = new Label(""+this.getName(), state.skin);
		name.setFontScale(.8f);
		Label desc = new Label(""+this.getDesc()+"\nRequirements:\n-Scanner", state.skin);
		desc.setFontScale(.5f);
		desc.setWrap(true);
		
		ScrollPane descscroll = new ScrollPane(desc, state.skin);
		descscroll.setColor(Color.DARK_GRAY);
		
		infotable.add(name).left().expand().fillX().row();
		infotable.add(descscroll).left().expand().fill().row();
		
		Table optionstable = new Table();
		TextButton complete = new TextButton("Complete", state.skin);
		if(!canComplete(state.getSpaceMap().getPlayer())){
			complete.setDisabled(true);
			complete.setColor(Color.FIREBRICK);
		}else{
			complete.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					grantAchievement(state);
					ScannerModule scanner = new ScannerModule(2);
					scanner.setLevel(2);
					scanner.setCost(0);
					state.getSpaceMap().getPlayer().removeModule(new ScannerModule(0));
					state.getSpaceMap().getPlayer().installNewModule(scanner);
					
					ShipAbility.showMessage("ScanTastic!         ", "The man has kept his word.. he has given you an upgraded version "
							+ "of the scanner.", state.skin);
				}
			});
		}
		optionstable.add(complete).right().expand();
		
		t.add(infotable).left().expand().fill().row();
		t.add(optionstable).left().expand().fillX();
		
		return t;
	}

}
