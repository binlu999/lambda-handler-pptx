package com.myopenboard.service.pptservice.merger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.myopenboard.service.logger.AppLogger;
import com.myopenboard.service.pptservice.aws.S3FileHandler;
import com.myopenboard.service.pptservice.config.EnvironmentConfig;

public class PPTXMerger {
	private static final AppLogger logger = AppLogger.getLogger(PPTXMerger.class);

	@SuppressWarnings("unchecked")
	public JSONObject doMerge(JSONObject event) {
		JSONObject result = new JSONObject();
		logger.info("START to merge:" + event.toString());
		Date startTime = new Date();
		try {
			JSONArray inputs = (JSONArray) event.get("input_files");
			String output = (String) event.get("output_file");

			XMLSlideShow combined = new XMLSlideShow();

			try {
				Iterator<String> iterator = inputs.iterator();
				while (iterator.hasNext()) {
					String infile = iterator.next();
					logger.debug(infile);
					InputStream inputstream = S3FileHandler.downloadFromS3(infile);
					// inputstream = new FileInputStream(workdir + File.separator + infile);
					XMLSlideShow src = new XMLSlideShow(inputstream);
					for (XSLFSlide srcSlide : src.getSlides()) {
						// merging the contents
						combined.createSlide().importContent(srcSlide);
					}
					src.close();
				}

				
				String outputFile = EnvironmentConfig.getWorkingDir() + File.separator + output;
				File outFile=new File(outputFile);
				if(outFile.exists()) {
					outFile.delete();
					logger.info("Output file exist and deleted " + outputFile);
				}
				
				FileOutputStream out = new FileOutputStream(outputFile);
				combined.write(out);
				out.close();
				combined.close();
				
				logger.info("Merged into " + outputFile);
				
				File file=new File(outputFile);
				String s3FilePath = S3FileHandler.uploadToS3(output, file);
				result.put("Status", "Success");
				result.put("output_file", s3FilePath);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				result.put("Status", "Error");
				result.put("Error", e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				result.put("Status", "Error");
				result.put("Error", e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				result.put("Status", "Error");
				result.put("Error", e.getMessage());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("Status", "Error");
			result.put("Error", e.getMessage());
		}

		Date endTime = new Date();
		result.put("processed_time_seconds", (endTime.getTime()-startTime.getTime()) / 1000);

		return result;
	}
}
