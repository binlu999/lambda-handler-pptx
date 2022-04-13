package com.myopenboard.service.pptservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class EventHandler implements RequestHandler<Object, Object>  {
	
	private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

	public Object handleRequest(Object event, Context context) {
		logger.info(event.toString());
		return null;
	}

}
