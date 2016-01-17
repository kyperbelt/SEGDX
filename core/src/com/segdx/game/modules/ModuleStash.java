package com.segdx.game.modules;

public class ModuleStash {
	
	public static final int EXTRAHAUL = 0;
	public static final int SCANNER = 1;
	public static final int FUELRESERVE = 2;
	public static final int ENGINEBOOSTER = 3;
	public static final int MINING = 4;
	public static final int ENERGYCANNON =5;
	public static final int RAILGUN = 6;
	public static final int REFINERY = 7;
	public static final int MPG = 8;
	public static final int REPAIRDRONES = 9;
	public static final int REPAIRKIT = 10;
	public static final int SHIELDGEN = 11;
	
	
	public static ShipModule getModule(int module,int level){
		ShipModule m = null;
		switch (module) {
		case EXTRAHAUL:
			m = new ExtraHaul(level);
			break;
		case SCANNER:
			m = new ScannerModule(1);
			break;
			
		case FUELRESERVE:
			m = new FuelReserves(level);
			break;
			
		case ENGINEBOOSTER:
			m = new EngineBoosters();
			break;
			
		case MINING:
			m = new MiningModule(level);
			break;
			
		case ENERGYCANNON:
			m = new EnergyCannon(level);
			break;
			
		case RAILGUN:
			m = new RailGun(level);
			break;
			
		case REFINERY:
			m = new RefineryModule();
			break;
			
		case MPG:
			m = new MPGModule(level);
			break;
			
		case REPAIRDRONES:
			m = new RepairDrones(level);
			break;
			
		case REPAIRKIT:
			m = new RepairKitModule(level);
			break;
			
		case SHIELDGEN:
			m = new ShieldGenerator(level);
			break;
			

		default:
			break;
		}
		
		return m;
	}

}
