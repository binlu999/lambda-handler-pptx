package com.myopenboard.service.pptservice.config;

import com.myopenboard.service.logger.AppLogger;


public class EnvironmentConfig {
	private static final AppLogger logger = AppLogger.getLogger(EnvironmentConfig.class);
	
	private static final String ENV_SRC_BUCKET="SRC_BUCKET";
	private static final String ENV_SRC_FOLDER="SRC_FOLDER";
	private static final String ENV_DEST_BUCKET="DEST_BUCKET";
	private static final String ENV_DEST_FOLDER="DEST_FOLDER";
	private static final String ENV_WORKING_DIR="WORKING_DIR";
	
	public static String getSrcBucket() {
		String srcBucket=System.getenv(ENV_SRC_BUCKET);
		logger.info("Environment Configuration SRC_Bucket:"+srcBucket);
		return srcBucket;
	}
	
	public static String getSrcFolder() {
		return System.getenv(ENV_SRC_FOLDER);
	}
	
	public static String getWorkingDir() {
		return System.getenv(ENV_WORKING_DIR);
	}

	public static String getDestBuscket() {
		return System.getenv(ENV_DEST_BUCKET);
	}
	
	public static String getDestFolder() {
		return System.getenv(ENV_DEST_FOLDER);
	}
}
