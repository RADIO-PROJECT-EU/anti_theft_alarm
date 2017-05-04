package robot.antitheft;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import robot.connector.GatewayConnector;
import robot.handlers.AntitheftHandler;
import robot.handlers.HandlerState;
import robot.utils.OsUtils.OSType;
import robot.utils.ConfigurationUtils;
import robot.utils.OsUtils;

public class Main {

	public static void main(String[] args) {
		
		String argsNet = null;
		if( OsUtils.getOperatingSystemType() == OSType.Windows ){
			if( args.length != 1 ){
				System.out.println("[Usage] - java -jar <network>");
				System.exit(0);
			}
			argsNet = args[0];
		}else{
			if( args.length >= 1 ){
				argsNet = args[0];
			}
		}
		
		
		
		System.out.println("[Main] - Starting up Robot Observator - Initializing Connection with Gateway...");
		GatewayConnector connector = null;
		if( argsNet != null ) connector = new GatewayConnector(argsNet);
		else connector = new GatewayConnector();
		connector.initialize();
		connector.connect();
		
		if( connector.isConnected() ){
			System.out.println("[Main] - Connection with Gateway established...");
			System.out.println("[RobotAntitheft] - Starting up Robot antitheft...");
			AntitheftHandler handler = new AntitheftHandler(connector);
			
			Callable<HandlerState> task = () -> {
			    try {
			        TimeUnit.SECONDS.sleep(Integer.parseInt(ConfigurationUtils.getConfig("data.calibration.time")));
			        return HandlerState.MEASUREMENT;
			    }
			    catch (InterruptedException e) {
			        throw new IllegalStateException("task interrupted", e);
			    }
			};
			
			ExecutorService executor = Executors.newFixedThreadPool(1);
			Future<HandlerState> future = executor.submit(task);

			try {
				HandlerState state = future.get();
				handler.setState(state);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			System.out.println("[Main] - Unable to establish connection with Gateway, Exiting...");
			System.exit(0);
		}
		
		
	}

}
