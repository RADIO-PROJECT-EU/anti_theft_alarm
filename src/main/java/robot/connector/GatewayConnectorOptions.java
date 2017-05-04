package robot.connector;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class GatewayConnectorOptions {
	
	private String DEFAULT_GATEWAY_AUTH_USERNAME = "<>";
	private String DEFAULT_GATEWAY_AUTH_PASSWORD = "<>";
	
	private String username = "";
	private String password = "";
	private boolean autoReconnect = false;
	private boolean cleanSession = true;
	
	public GatewayConnectorOptions(){
		this.setUsername(this.DEFAULT_GATEWAY_AUTH_USERNAME);
		this.setPassword(this.DEFAULT_GATEWAY_AUTH_PASSWORD);
		this.setCleanSession(true);
		this.setAutoReconnect(true);
	}

	public MqttConnectOptions getMqttConnectionOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(this.getUsername());
		options.setPassword(this.getPassword().toCharArray());
		options.setAutomaticReconnect(this.isAutoReconnect());
		options.setCleanSession(this.isCleanSession());
		return options;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoReconnect() {
		return autoReconnect;
	}

	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
	}

	@Override
	public String toString() {
		return "GatewayConnectorOptions [username=" + username + ", password="
				+ password + ", autoReconnect=" + autoReconnect
				+ ", cleanSession=" + cleanSession + "]";
	}
	
}
