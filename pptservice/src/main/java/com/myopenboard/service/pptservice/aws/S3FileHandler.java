package com.myopenboard.service.pptservice.aws;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.myopenboard.service.logger.AppLogger;
import com.myopenboard.service.pptservice.config.EnvironmentConfig;

public class S3FileHandler {
	private static final AppLogger logger = AppLogger.getLogger(S3FileHandler.class);
	private static AmazonS3 s3Client;

	private static AmazonS3 getClient() {
		if (s3Client == null) {
			s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		}
		return s3Client;
	}

	private static String getSrcBucketName() throws Exception {
		String srcBucket = EnvironmentConfig.getSrcBucket();
		if (getClient().doesBucketExistV2(srcBucket)) {
			return srcBucket;
		} else {
			throw new Exception("Bucket " + srcBucket + "does not exist");
		}
	}

	public static S3ObjectInputStream downloadFromS3(String objId) throws Exception {
		String srcBucket;
		try {
			srcBucket = getSrcBucketName();
		} catch (Exception e) {
			throw (e);
		}
		S3Object s3Obj = getClient().getObject(srcBucket, EnvironmentConfig.getSrcFolder() + "/" + objId);
		S3ObjectInputStream inputStream = s3Obj.getObjectContent();
		// FileUtils.copyInputStreamToFile(inputStream, new
		// File(workDir+File.separator+objId));
		return inputStream;
	}

	public static String uploadToS3(String objId, InputStream input) {

		ObjectMetadata meta = new ObjectMetadata();
		getClient().putObject(EnvironmentConfig.getDestBuscket(), EnvironmentConfig.getDestFolder() + "/" + objId,
				input, meta);
		return EnvironmentConfig.getDestBuscket() + "/" + EnvironmentConfig.getDestFolder() + "/" + objId;
	}

	public static String uploadToS3(String objId, File file) {

		ObjectMetadata meta = new ObjectMetadata();
		getClient().putObject(EnvironmentConfig.getDestBuscket(), EnvironmentConfig.getDestFolder() + "/" + objId,
				file);
		return EnvironmentConfig.getDestBuscket() + "/" + EnvironmentConfig.getDestFolder() + "/" + objId;
	}

}
