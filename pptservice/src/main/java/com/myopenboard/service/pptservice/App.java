package com.myopenboard.service.pptservice;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myopenboard.service.pptservice.merger.PPTXMerger;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		PPTXMerger merger = new PPTXMerger();
		if (args.length < 1) {
			logger.error("Please provide configuration file on command line!");
			System.exit(9);
		}

		logger.debug(args[0]);
		JSONParser parser = new JSONParser();
		String configFile = args[0];
		try {
			FileReader fileReader = new FileReader(configFile);
			Object obj = parser.parse(fileReader);
			logger.info(obj.toString());
			JSONObject event = (JSONObject)obj;
			JSONObject result = merger.doMerge(event);
			logger.info(result.toJSONString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Done with merge");
	}
}
