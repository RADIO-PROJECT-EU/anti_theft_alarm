package robot.handlers;

public class Movement {
	
	private Axis3D gyroscope;
	private Axis3D accelerometer;
	private Axis3D magnetometer;
	
	public Axis3D getGyroscope() {
		return gyroscope;
	}
	public void setGyroscope(Axis3D gyroscope) {
		this.gyroscope = gyroscope;
	}
	public Axis3D getAccelerometer() {
		return accelerometer;
	}
	public void setAccelerometer(Axis3D accelerometer) {
		this.accelerometer = accelerometer;
	}
	public Axis3D getMagnetometer() {
		return magnetometer;
	}
	public void setMagnetometer(Axis3D magnetometer) {
		this.magnetometer = magnetometer;
	}
	@Override
	public String toString() {
		return "Movement [gyroscope=" + gyroscope + ", accelerometer="
				+ accelerometer + ", magnetometer=" + magnetometer + "]";
	}

}
