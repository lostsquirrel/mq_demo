package test.mq.rabbit.task_queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {

	private final static String QUEUE_NAME = "hello";
	
	private static final Log log = LogFactory.getLog(Worker.class);

	public static void main(String[] argv)
			throws java.io.IOException, java.lang.InterruptedException, TimeoutException {

		
		ConnectionFactory factory = new ConnectionFactory();
		String host = "192.168.19.101";
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		log.info(" [*] Waiting for messages. To exit press CTRL+C");
		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				log.info(" [x] Received '" + message + "'");
				try {
					try {
						doWork(message);
					} catch (InterruptedException e) {
					}
				} finally {
					log.info(" [x] Done ");
				}
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}

	private static void doWork(String task) throws InterruptedException {
		Thread.sleep(1000 * Integer.parseInt(task.split(":")[1]));
	}
}
