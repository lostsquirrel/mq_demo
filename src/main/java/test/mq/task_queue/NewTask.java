package test.mq.task_queue;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NewTask {

	private final static String QUEUE_NAME = "hello";

	private final static Log log = LogFactory.getLog(NewTask.class);
	
	public static void main(String[] argv) throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		String host = "192.168.19.101";
		factory.setHost(host);
		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
//			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			for (int i= 0; i < 10; i++) {
				
				String message = "Hello World!" + i + " at:" + new Random().nextInt(10);
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				log.info(" [x] Sent '" + message + "'");
			}
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
