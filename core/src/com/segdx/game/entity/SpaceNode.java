package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class SpaceNode {
	public static final int NEUTRAL = 0;
	public static final int REST = 1;
	public static final int TRADE = 2;
	public static final int DANGER = 3;
	
	private int index;
	private int prevtype;
	private int nodeType;
	private ImageButton button;
	private Circle effectradius;
	private float x;
	private float y;
	
	
	public ImageButton getButton() {
		return button;
	}
	public void setButton(ImageButton button) {
		this.button = button;
	}
	public Circle getEffectradius() {
		return effectradius;
	}
	public void setEffectradius(Circle effectradius) {
		this.effectradius = effectradius;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public int getPrevtype() {
		return prevtype;
	}
	public void setPrevtype(int prevtype) {
		this.prevtype = prevtype;
	}
	
	public static ImageButton createButton(final SpaceNode node){
		ImageButton button = new ImageButton(getButtonStyle(node));
		button.setX(node.getX());
		button.setY(node.getY());
		button.setSize(16, 16);
		button.setOrigin(Align.center);
		button.setBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());
		button.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("clicked"+node.getIndex());
				GameState state = (GameState) StateManager.get().getState(StateManager.GAME);

				SpaceNode selectednodee = state.getSpaceMap().getAllnodes().get(state.getSpaceMap().getNodebuttons().getCheckedIndex());
				Vector2 destination = new Vector2(selectednodee.getX(),selectednodee.getY());
				Vector2 start = new Vector2(state.getSpaceMap().getPlayer().getX(),state.getSpaceMap().getPlayer().getY());
				
				state.nodedistance.setText("distance:"+(int)Vector2.dst(start.x, start.y, destination.x, destination.y));
				SpaceMap map = state.getSpaceMap();
				ButtonGroup<ImageButton> nodebuttons = map.getNodebuttons();
				Array<ImageButton> buttons = nodebuttons.getButtons();
				if(!buttons.get(node.getIndex()).isChecked()){
					buttons.get(node.getIndex()).setChecked(true);
				}
				for (int i = 0; i < buttons.size; i++) {
						if(!buttons.get(i).isChecked()){
							buttons.get(i).setSize(16, 16);
							buttons.get(i).invalidate();
						}
				}
				((ImageButton)event.getListenerActor()).setSize(32, 32);
				((ImageButton)event.getListenerActor()).invalidate();
				SoundManager.get().playSound(SoundManager.NODESELECT);
			}
		});
		
		
		return button;
	}
	
	public static ImageButtonStyle getButtonStyle(SpaceNode node){
		Image rednode = new Image(Assets.manager.get("map/rednode.png", Texture.class));
		Image bluenode = new Image(Assets.manager.get("map/bluenode.png", Texture.class));
		Image greennode = new Image(Assets.manager.get("map/greennode.png", Texture.class));
		Image passivenode = new Image(Assets.manager.get("map/passivenode.png", Texture.class));
		
		ImageButtonStyle ibs = new ImageButtonStyle();
		switch (node.getNodeType()) {
		case NEUTRAL:
			ibs.imageDown = passivenode.getDrawable();
			ibs.imageUp = passivenode.getDrawable();
			ibs.imageChecked = passivenode.getDrawable();
			break;
		case REST:
			ibs.imageDown = bluenode.getDrawable();
			ibs.imageUp = bluenode.getDrawable();
			ibs.imageChecked = bluenode.getDrawable();
			break;
		case TRADE:
			ibs.imageDown = greennode.getDrawable();
			ibs.imageUp = greennode.getDrawable();
			ibs.imageChecked = greennode.getDrawable();
			break;
		case DANGER:
			ibs.imageDown = rednode.getDrawable();
			ibs.imageUp = rednode.getDrawable();
			ibs.imageChecked = rednode.getDrawable();
			break;
		default:
			break;
		}
		return ibs;
	}
	

}
