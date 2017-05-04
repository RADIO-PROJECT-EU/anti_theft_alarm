package robot.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import robot.antitheft.Main;

public class ConfigurationUtils {
	
	private static String CONFIGURATION_FILEPATH = "config/antitheft.properties";
	private static Properties appProperties;
	
	static {
		appProperties = new Properties();
		InputStream input = null;
		try {
			System.out.println(getJarParentFilepath() + CONFIGURATION_FILEPATH);
			input = new FileInputStream(getJarParentFilepath() + CONFIGURATION_FILEPATH);
			appProperties.load(input);
		} catch (IOException ex) {
			System.out.println("[ConfigurationUtils] - Unable to load application properties: ");
			ex.printStackTrace();
			System.exit(0);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getConfig(String property){
		return appProperties.getProperty(property);
	}
	
	public static String getJarParentFilepath(){
		String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String temp[] = path.split("/");
		String jarpath = "";
		for( String tmp : temp ){
			if( !tmp.endsWith(".jar") && !tmp.endsWith("bin") ){
				jarpath += tmp+"/";
			}
		}
		return jarpath;
	}

}
