package com.segdx.game.tween;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import aurelienribon.tweenengine.TweenAccessor;

public class CameraAccessor implements TweenAccessor<OrthographicCamera>{
	public static final int POSITION_X = 2;
	public static final int POSITION_Y = 4;
	public static final int POSITION_X_Y = 6;
	public static final int ROTATE = 8;

	@Override
	public int getValues(OrthographicCamera target, int type, float[] values) {
		switch (type) {
		case POSITION_X:
			values[0] = target.position.x;
			return 1;
		case POSITION_Y:
			values[0] = target.position.y;
			return 1;
		case POSITION_X_Y:
			values[0] = target.position.x;
			values[1] = target.position.y;
			return 2;
		case ROTATE:
			values[0] = getCameraCurrentXYAngle(target);
			return 1;
		default:
			assert false;
			return -1;
		}
		
	}

	@Override
	public void setValues(OrthographicCamera target, int type, float[] values) {
		switch (type) {
		case POSITION_X:
			target.position.set(values[0], target.position.y, target.position.z);
			break;
		case POSITION_Y:
			target.position.set(target.position.x, values[0], target.position.z);
			break;
		case POSITION_X_Y:
			target.position.set(values[0], values[1], target.position.z);
			break;
		case ROTATE:
			target.rotate(values[0]);
			break;
		default:
			break;
		}
	}
	
	public float getCameraCurrentXYAngle(OrthographicCamera cam)
	{
	    return (float)Math.atan2(cam.up.x, cam.up.y)*MathUtils.radiansToDegrees;
	}


}
