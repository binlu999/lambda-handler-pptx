package com.myopenboard.service.logger;

public class AppLogger {

	private String name;

	private AppLogger(String clsName) {
		this.name = clsName;
	}

	public static AppLogger getLogger(Class cls) {
		return new AppLogger(cls.getName());
	}

	public void error(String message) {
		this.doLog("ERROR", message);
	}

	public void debug(String message) {
		this.doLog("DEBUG", message);
	}
	
	private void doLog(String level,String message) {
		System.out.println("["+level+"]:"+this.name+":"+message);
	}

	public void info(String message) {
		this.doLog("INFO", message);
		
	}

}
