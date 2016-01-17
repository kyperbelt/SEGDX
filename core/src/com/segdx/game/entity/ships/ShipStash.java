package com.segdx.game.entity.ships;

import com.segdx.game.entity.Ship;

public class ShipStash {
	
	public static final int SHUTTLE = 0;
	public static final int INTERCEPTOR = 1;
	public static final int RAIDER = 2;
	public static final int SENTINEL = 3;
	public static final int MARAUDER = 4;
	public static final int CARRIER = 5;
	public static final int CRUISER = 6;
	public static final int GUILLOTINE = 7;
	public static final int ENTERPRISE = 8;
	
	public static Ship getShip(int type,int version){
		Ship s = null;
		switch (type) {
		case SHUTTLE:
			s = new RaiderShip(version);
			break;
		case RAIDER:
			s = new RaiderShip(version);
			break;
		case SENTINEL:
			s = new SentinelShip(version);
			break;
		case MARAUDER:
			s = new MauraderShip(version);
			break;
		case CRUISER:
			s = new CruiserShip(version);
			break;
		case CARRIER:
			s = new CarrierShip(version);
			break;
		case GUILLOTINE:
			s = new GuillotineShip(version);
			break;
		case INTERCEPTOR:
			s = new InterceptorShip(version);
			break;
		case ENTERPRISE:
			s = new EnterpriseShip(version);
			break;
		default:
			break;
		}
		
		return s;
	}

}
