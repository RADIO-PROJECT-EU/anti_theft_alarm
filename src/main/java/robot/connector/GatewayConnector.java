package robot.connector;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import robot.utils.NetworkUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GatewayConnector implements ConnectorModule {
	
	private MqttClient mqclient;
	private final String ROBOT_OBSERVATOR_CLIENT_PREFIX = "Robot-Antithet-Mapper-";
	private final String CHARSET = "UTF-8";
	private final String DIGEST_ALGORITHM = "MD5";
	private String connectorId;
	private String network;
			
	public GatewayConnector(){
		this.network = null;
		this.connectorId = this.ROBOT_OBSERVATOR_CLIENT_PREFIX + this.generateSuffix();
	}
	
	public GatewayConnector(String network){
		this.network = network;
		this.connectorId = this.ROBOT_OBSERVATOR_CLIENT_PREFIX + this.generateSuffix();
	}

	@Override
	public void initialize() {
		try {
			System.out.println("[GatewayConnector] - Trying to find gateway....");
			String brokerUrl = this.getBrokerUrl();
			if( brokerUrl == null ) {
				System.out.println("Warn - [GatewayConnector] - Gateway not found...");
				return;
			}
			System.out.println("[GatewayConnector] - Gateway found at: " + brokerUrl + ", connecting....");
			this.mqclient = new MqttClient(brokerUrl, this.connectorId, new MemoryPersistence());
		} catch (Exception e) {
			System.out.println("Error - [GatewayConnector] - Unable to initialize Gateway connector");
			e.printStackTrace();
		}
	}

	private String getBrokerUrl() throws Exception {
		String brokerUrl = (this.network==null)?NetworkUtils.findGateway():NetworkUtils.findGatewayOnNetwork(this.network,24);
		if( brokerUrl == null ) return null;
		return "tcp://"+brokerUrl+":"+NetworkUtils.GATEWAY_PORT;
	}

	@Override
	public void connect() {
		try {
			this.mqclient.connect(new GatewayConnectorOptions().getMqttConnectionOptions());
			if( this.mqclient.isConnected() ) System.out.println("[GatewayConnector] - Robot Observator connected successfully to the gateway( "+this.mqclient.getServerURI()+" )");
		} catch (MqttSecurityException e) {
			System.out.println("Error - [GatewayConnector] - Invalid authentication information...");
			e.printStackTrace();
		} catch (MqttException e) {
			System.out.println("Error - [GatewayConnector] - Unable to connect to the Broker");
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			this.mqclient.disconnect();
		} catch (MqttException e) {
			System.out.println("Error - [GatewayConnector] - Unable to disconnect from Broker...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate a suffix for the Client
	 */
	public String generateSuffix(){
		String uuid = UUID.randomUUID().toString();
		String temp[] = uuid.split("-");
		//byte[] bytesOfMessage;
		//try {
		return temp[temp.length-1];
			//MessageDigest md = MessageDigest.getInstance(this.DIGEST_ALGORITHM);
		//	md.digest(bytesOfMessage).toString();
		//} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
		//	System.out.println("Error - [GatewayConnector] - Unable to create suffix for the client: ");
		//	e.printStackTrace();
		//}
		//return null;
	}

	@Override
	public MqttClient getConnector() {
		if( this.mqclient.isConnected() ) return this.mqclient;
		return null;
	}

	@Override
	public boolean isConnected() {
		return this.mqclient.isConnected();
	}

	@Override
	public void publish(String topic, byte[] message) {
		try {
			if( this.mqclient.isConnected() ) this.mqclient.publish(topic, new MqttMessage(message));
		} catch (MqttException e) {
			System.out.println("Error - [GatewayConnector] - Unable to publish message to topic " + topic);
			e.printStackTrace();
		}
	}

}
