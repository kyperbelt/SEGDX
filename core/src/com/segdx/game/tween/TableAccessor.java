package com.segdx.game.tween;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.TweenAccessor;

public class TableAccessor implements TweenAccessor<Table>{
	public static final int POSITION_X = 0;
	public static final int POSITION_Y = 1;

	@Override
	public int getValues(Table target, int type, float[] values) {
		
		switch (type) {
		case POSITION_X:
				values[0] = target.getX();
			return 1;
		case POSITION_Y:
				values[0] = target.getY();
			return 1;
		default:
			return -1;
		}
		
	}

	@Override
	public void setValues(Table target, int type, float[] values) {
		switch (type) {
		case POSITION_X:
				target.setX(values[0]);
			break;
		case POSITION_Y:
				target.setY(values[0]);
			break;
		default:
			break;
		}
	}

}
