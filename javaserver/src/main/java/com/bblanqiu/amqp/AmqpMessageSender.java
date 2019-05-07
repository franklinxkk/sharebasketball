package com.bblanqiu.amqp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AmqpMessageSender {
	@Autowired
	AmqpTemplate template;
	private ExecutorService executor;
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(AmqpMessageSender.class);

	public AmqpMessageSender() {
		this.executor = Executors.newSingleThreadExecutor();
	}

	public void asyncPushDataMessage(final Object o, final String type) {
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				pushDataMessage(o, type);
			}
		});
	}

	public void pushDataMessage(Object o,String type) {
		Assert.notNull(o);
		try {
			template.convertAndSend("bblq.exchange.data", "bblq.key.data."+type, o);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
