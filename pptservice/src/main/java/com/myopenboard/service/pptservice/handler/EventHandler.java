package com.myopenboard.service.pptservice.handler;

import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.myopenboard.service.logger.AppLogger;
import com.myopenboard.service.pptservice.merger.PPTXMerger;

public class EventHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final AppLogger logger = AppLogger.getLogger(EventHandler.class);

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
		System.out.println(event.toString());
		JSONObject response = new JSONObject();
		String bodyString = event.getBody();
		JSONParser parser = new JSONParser();
		try {
			JSONObject body = (JSONObject) parser.parse(bodyString);
			logger.info(body.toString());
			PPTXMerger merger = new PPTXMerger();
			response = merger.doMerge(body);
			return ok(response);
		} catch (ParseException e) {
			e.printStackTrace();
			return error(response, e);
		} catch (Throwable e) {
			e.printStackTrace();
			return error(response, e);
		}
	}

	private APIGatewayProxyResponseEvent ok(JSONObject response) {
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(response.toJSONString())
				.withIsBase64Encoded(false);
	}

	private APIGatewayProxyResponseEvent error(JSONObject response, Throwable exc) {
		String exceptionString = String.format("error: %s: %s", exc.getMessage(), Arrays.toString(exc.getStackTrace()));
		response.put("Exception", exceptionString);

		return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(response.toJSONString())
				.withIsBase64Encoded(false);
	}
}
