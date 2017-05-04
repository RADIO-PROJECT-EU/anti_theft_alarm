package robot.connector;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Connector Module
 * @author kasnot
 */
public interface ConnectorModule {

	/**
	 * Initialize the connector.
	 */
	public void initialize();
	
	/**
	 * Start the connection
	 */
	public void connect();

	/**
	 * End up the connection
	 */
	public void disconnect();
	
	/**
	 * Get the connection
	 */
	public MqttClient getConnector();
	
	/**
	 * Publish messages
	 */
	public void publish(String topic, byte[] message);
	
	/**
	 * Check if the connection is Ok
	 */
	public boolean isConnected();
	
}
