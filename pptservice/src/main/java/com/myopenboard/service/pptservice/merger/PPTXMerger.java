package com.myopenboard.service.pptservice.merger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PPTXMerger {
	private static final Logger logger = LoggerFactory.getLogger(PPTXMerger.class);

	@SuppressWarnings("unchecked")
	public JSONObject doMerge(JSONObject event) {
		JSONObject result = new JSONObject();
		logger.info("Strt to merge");
		String workdir = (String) event.get("work_dir");
		String output = (String) event.get("output_file");
		JSONArray inputs = (JSONArray) event.get("input_files");
		Iterator<String> iterator = inputs.iterator();

		XMLSlideShow combined = new XMLSlideShow();

		try {
			while (iterator.hasNext()) {
				String infile = iterator.next();
				logger.debug(infile);
				FileInputStream inputstream;

				inputstream = new FileInputStream(workdir + File.separator + infile);
				XMLSlideShow src = new XMLSlideShow(inputstream);
				for (XSLFSlide srcSlide : src.getSlides()) {
					// merging the contents
					combined.createSlide().importContent(srcSlide);
				}
			}
			
			String outputFile=workdir + File.separator + output;
			FileOutputStream out = new FileOutputStream(outputFile);
			combined.write(out);
			out.close();
			logger.info("Merged into "+outputFile);
			result.put("Status", "Success");
			result.put("output_file", output);

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
		}

		return result;
	}
}
