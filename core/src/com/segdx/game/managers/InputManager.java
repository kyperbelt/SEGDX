package com.segdx.game.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.segdx.game.states.GameState;

public class InputManager implements InputProcessor,GestureListener{

	@Override
	public boolean keyDown(int keycode) {
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		boolean checked = false;
		switch (keycode) {
		case Input.Keys.ESCAPE:
			checked = !state.menutab.isChecked();
			;
			state.menutab.setChecked(!state.closeOpenTabs());
			state.openCloseMenu();
			
			System.out.println("pressed");
			break;
		case Input.Keys.NUM_1:
			checked = !state.abilitytab.isChecked();
			state.closeOpenTabs();
			state.abilitytab.setChecked(checked);
			state.openCloseSkill();
			
			System.out.println("pressed");
			break;
		case Input.Keys.NUM_2:
			checked = !state.modtab.isChecked();
			state.closeOpenTabs();
			state.modtab.setChecked(checked);
			state.openCloseMods();
			break;
		case Input.Keys.NUM_3:
			checked = !state.cargotab.isChecked();
			state.closeOpenTabs();
			state.cargotab.setChecked(checked);
			state.openCloseHaul();
			break;
		case Input.Keys.NUM_4:
			checked = !state.shipinfotab.isChecked();
			state.closeOpenTabs();
			state.shipinfotab.setChecked(checked);
			state.openCloseShipInfo();
			break;
		case Input.Keys.NUM_5:
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		zoom(amount);
		return true;
	}

	//GESTURES
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		OrthographicCamera cam = state.getSpaceMap().getCam();
		cam.position.set(cam.position.x - deltaX, cam.position.y+deltaY, 0);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		zoom((initialDistance-distance)*.1f);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		
		return false;
	}
	
	public void zoom(float zoom){
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		OrthographicCamera cam = state.getSpaceMap().getCam();
		cam.zoom += zoom*(cam.zoom*.05f);
		if(cam.zoom > 3f)
			cam.zoom = 3f;
		if(cam.zoom < .9f)
			cam.zoom = .9f;
	}

}
