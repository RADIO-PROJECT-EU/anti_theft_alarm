package robot.utils;

import robot.handlers.Axis3D;
import robot.handlers.Movement;

public class DataUtils {
	
	public static Movement parseMovementData(String value){
		Movement movement = new Movement();
		byte[] valueByte = hexStringToByteArray(value.replace(" ", ""));
		int gyroY = (valueByte[1]<<8) + valueByte[0];
        int gyroX = (valueByte[3]<<8) + valueByte[2];
        int gyroZ = (valueByte[5]<<8) + valueByte[4];
        final float SCALEGYRO = (float) 128.0;
        Axis3D gyroscope = new Axis3D((gyroX/SCALEGYRO),(gyroY/SCALEGYRO),(gyroZ/SCALEGYRO));
		movement.setGyroscope(gyroscope);
		
		final float SCALEACCEL = (float) 16384.0;
        int accelX = (valueByte[7]<<8) + valueByte[6];
        int accelY = (valueByte[9]<<8) + valueByte[8];
        int accelZ = (valueByte[11]<<8) + valueByte[10];
        Axis3D accelerometer = new Axis3D((accelX / SCALEACCEL * -1), (accelY / SCALEACCEL), (accelZ / SCALEACCEL * -1));
        movement.setAccelerometer(accelerometer);
        
        final float SCALE = 32768 / 4912;
        int magX = (valueByte[13]<<8) + valueByte[12];
        int magY = (valueByte[15]<<8) + valueByte[14];
        int magZ = (valueByte[17]<<8) + valueByte[16];
        Axis3D magnetometer = new Axis3D((magX / SCALE), (magY / SCALE), (magZ / SCALE));
        movement.setMagnetometer(magnetometer);
        return movement;
	}

	/**
	 * Calculate Ti Acceleration
	 */
	public static double[] calculateAcceleration(String value) {
		double[] acceleration = new double[3];
		byte[] valueByte = hexStringToByteArray(value.replace(" ", ""));
        final float SCALE = (float) 4096.0;

        int x = shortSignedAtOffset(valueByte, 6);
        int y = shortSignedAtOffset(valueByte, 8);
        int z = shortSignedAtOffset(valueByte, 10);

        acceleration[0] = x / SCALE * -1;
        acceleration[1] = y / SCALE;
        acceleration[2] = z / SCALE * -1;
        return acceleration;
	}
	
	/*
     * Calculate TI Magnetic Field
     */
    public static float[] calculateMagneticField(String value) {

        float[] magneticField = new float[3];
        byte[] valueByte = hexStringToByteArray(value.replace(" ", ""));

        final float SCALE = 32768 / 4912;

        int x = shortSignedAtOffset(valueByte, 12);
        int y = shortSignedAtOffset(valueByte, 14);
        int z = shortSignedAtOffset(valueByte, 16);

        magneticField[0] = x / SCALE;
        magneticField[1] = y / SCALE;
        magneticField[2] = z / SCALE;

        return magneticField;
    }
    
    /*
     * Calculate gyroscope
     */
    public static float[] calculateGyroscope(String value) {
        float[] gyroscope = new float[3];
        byte[] valueByte = hexStringToByteArray(value.replace(" ", ""));
        int y = shortSignedAtOffset(valueByte, 0);
        int x = shortSignedAtOffset(valueByte, 2);
        int z = shortSignedAtOffset(valueByte, 4);
        final float SCALE = 65535 / 500;
        gyroscope[0] = x / SCALE;
        gyroscope[1] = y / SCALE;
        gyroscope[2] = z / SCALE;
        return gyroscope;
    }
	
	public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static Integer shortSignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer upperByte = (int) c[offset + 1]; // Interpret MSB as signedan
        return (upperByte << 8) + lowerByte;
    }

    /*private static Integer shortUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer upperByte = c[offset + 1] & 0xFF;
        return (upperByte << 8) + lowerByte;
    }

    private static Integer twentyFourBitUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = c[offset] & 0xFF;
        Integer mediumByte = c[offset + 1] & 0xFF;
        Integer upperByte = c[offset + 2] & 0xFF;
        return (upperByte << 16) + (mediumByte << 8) + lowerByte;
    }*/
	
}
