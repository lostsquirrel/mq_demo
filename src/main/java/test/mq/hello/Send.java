package test.mq.hello;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		String host = "192.168.19.101";
		factory.setHost(host);
		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Hello World! at:" + System.currentTimeMillis();
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		} catch (TimeoutException e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			connection.close();
		}
		
	}
}
