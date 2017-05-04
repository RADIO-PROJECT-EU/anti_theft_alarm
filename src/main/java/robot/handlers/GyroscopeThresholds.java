package robot.handlers;

import robot.utils.ConfigurationUtils;

public class GyroscopeThresholds {
	
	private double minx = 1000000;
	private double miny = 1000000;
	private double minz = 1000000;
	private double maxx = -1000000;
	private double maxy = -1000000;
	private double maxz = -1000000;
	
	/**
	 * Determine a minimum gap for meausurements that will exceed for little from the min max
	 */
	private double gap;
	private int bordersExceedCounter;
	private int cleanMeasurementsCounter = 50;
	private int currentMeasurement = 0;
	private boolean limitExceeded = false;
	
	//Determine the limit of how many exceed borders before fire alarm.
	private int bordersExceedLimit;
	
	private boolean fireAlarm;
	
	public GyroscopeThresholds(){
		this.gap = Double.parseDouble(ConfigurationUtils.getConfig("gyroscope.gap"));
		this.bordersExceedLimit = 5;
	}
	
	public GyroscopeThresholds(double gap, int bordersExceedLimit){
		this.gap = gap;
		this.bordersExceedLimit = bordersExceedLimit;
	}

	public double getMinx() {
		return minx;
	}

	public void setMinx(double minx) {
		this.minx = minx;
	}

	public double getMiny() {
		return miny;
	}

	public void setMiny(double miny) {
		this.miny = miny;
	}

	public double getMinz() {
		return minz;
	}

	public void setMinz(double minz) {
		this.minz = minz;
	}

	public double getMaxx() {
		return maxx;
	}

	public void setMaxx(double maxx) {
		this.maxx = maxx;
	}

	public double getMaxy() {
		return maxy;
	}

	public void setMaxy(double maxy) {
		this.maxy = maxy;
	}

	public double getMaxz() {
		return maxz;
	}

	public void setMaxz(double maxz) {
		this.maxz = maxz;
	}

	public double getGap() {
		return gap;
	}

	public void setGap(double gap) {
		this.gap = gap;
	}

	public int getBordersExceedCounter() {
		return bordersExceedCounter;
	}

	public void setBordersExceedCounter(int bordersExceedCounter) {
		this.bordersExceedCounter = bordersExceedCounter;
	}

	public boolean isFireAlarm() {
		return fireAlarm;
	}

	public void setFireAlarm(boolean fireAlarm) {
		this.fireAlarm = fireAlarm;
	}
	
	public void calibrate(Axis3D data){
		if( data.getX() < this.minx ) this.minx = data.getX();
		if( data.getX() > this.maxx ) this.maxx = data.getX();
		
		if( data.getY() < this.miny ) this.miny = data.getY();
		if( data.getY() > this.maxy ) this.maxy = data.getY();
		
		if( data.getZ() < this.minz ) this.minz = data.getZ();
		if( data.getZ() > this.maxz ) this.maxz = data.getZ();
	}

	@Override
	public String toString() {
		return "GyroscopeThresholds [minx=" + minx + ", miny=" + miny
				+ ", minz=" + minz + ", maxx=" + maxx + ", maxy=" + maxy
				+ ", maxz=" + maxz + "]";
	}

	public void check(Axis3D data) {
		if( this.isFireAlarm() ){
			this.setFireAlarm(false);
			this.setBordersExceedCounter(0);
			this.currentMeasurement = 0;
		}
		this.currentMeasurement++;
		this.limitExceeded = false;
		
		if( (data.getX() < this.minx) ||
			(data.getX() > this.maxx) ||
			(data.getY() < this.miny) ||
			(data.getY() > this.maxy) ||
			(data.getZ() < this.minz) ||
			(data.getZ() > this.maxz)
				){
			System.out.println(data);
			System.out.println(this);
			this.limitExceeded = true;
			this.bordersExceedCounter++;
		}
		
		if( (this.currentMeasurement >= this.cleanMeasurementsCounter) && !this.limitExceeded ){
			this.setBordersExceedCounter(0);
		}
		
		if( this.bordersExceedCounter > this.bordersExceedLimit ){
			this.setFireAlarm(true);
			this.setBordersExceedCounter(0);
			this.currentMeasurement = 0;
		}
		
	}

	public int getBordersExceedLimit() {
		return bordersExceedLimit;
	}

	public void setBordersExceedLimit(int bordersExceedLimit) {
		this.bordersExceedLimit = bordersExceedLimit;
	}

	public void normalizeCalibration() {
		this.minx = this.minx-this.gap;
		this.maxx = this.maxx+this.gap;
		
		this.miny = this.miny-this.gap;
		this.maxy = this.maxy+this.gap;
		
		this.minz = this.minz-this.gap*2;
		this.maxz = this.maxz+this.gap*2;
	}

}
