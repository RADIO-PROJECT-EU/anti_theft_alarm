package robot.handlers;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import robot.connector.ConnectorModule;
import robot.connector.GatewayConnector;
import robot.utils.DataUtils;

public class AntitheftHandler implements MqttCallback{

	private ObjectMapper jsonMapper = new ObjectMapper();
	private final String SENSOR_MOVEMENT_TOPIC = "wsn/data/movement";
	private final String NOTIFICATIONS_TOPIC = "apps/notifications";
	private final String ALARM_PAYLOAD = "R_alarm";
	private final String DATATYPE = "movement";
	private ConnectorModule connector;
	private HandlerState state = HandlerState.CALIBRATION;
	private GyroscopeThresholds gyroThres;
	
	public AntitheftHandler(GatewayConnector connector) {
		this.connector  = connector;
		this.gyroThres = new GyroscopeThresholds();
		try {
			this.connector.getConnector().subscribe(this.SENSOR_MOVEMENT_TOPIC);
			this.connector.getConnector().setCallback(this);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String dataStr = new String(message.getPayload());
		Movement movement = DataUtils.parseMovementData(dataStr);
		this.handleGyroscope(movement.getGyroscope());
	}

	private void handleGyroscope(Axis3D data) {
		if( this.state == HandlerState.CALIBRATION ){
			this.gyroThres.calibrate(data);
		}else{
			this.gyroThres.check(data);
			if( this.gyroThres.isFireAlarm() ){
				this.sendTheftAlarm();
				this.gyroThres.setFireAlarm(false);
				this.gyroThres.setBordersExceedCounter(0);
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {	}
	
	public void sendTheftAlarm(){
		ServiceData data = new ServiceData();
		try {
			data.setDatatype(this.DATATYPE);
			data.setPayload(this.ALARM_PAYLOAD);
			this.connector.publish(this.NOTIFICATIONS_TOPIC, this.jsonMapper.writeValueAsBytes(data));
			System.out.println("Theft alarm sent successfully...");
		} catch (JsonProcessingException e) {
			System.out.println("Unable to send Theft Alarm....");
			e.printStackTrace();
		}
	}
	
	public void setState(HandlerState state) {
		System.out.println("Switching to state: " + state);
		this.gyroThres.normalizeCalibration();
		this.state = state;
	}

}
