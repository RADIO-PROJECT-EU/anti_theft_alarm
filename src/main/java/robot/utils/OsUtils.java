package robot.utils;

import java.util.Locale;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class OsUtils {

	//private static final Logger logger = LoggerFactory.getLogger(OsUtils.class);
	
	/**
	 * Operating Systems types
	 */
	public enum OSType {
		Windows, MacOS, Linux, Other
	};
	private static OSType detectedOS = null;
	
	/**
	 * Return the operating system that applications is running
	 * @return
	 */
	public static OSType getOperatingSystemType() {
	    if (detectedOS == null) {
	    	String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
	    	if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
	    		detectedOS = OSType.MacOS;
	    	} else if (OS.indexOf("win") >= 0) {
	    		detectedOS = OSType.Windows;
	    	} else if (OS.indexOf("nux") >= 0) {
	    		detectedOS = OSType.Linux;
	    	} else {
	    		detectedOS = OSType.Other;
	    	}
	    }
	    return detectedOS;
	}
	
	/**
	 * Convert byte to megabytes
	 * @param bytes
	 * @return
	 */
	public static String convertBytesToReadableFormat(long bytes, boolean si){
		int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
