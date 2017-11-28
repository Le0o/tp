package com.jry.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wsg
 * @time 2017-11-15
 * @description:获取.properties文件中数据
 *
 */
public class PropertiesUtils {
	public static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
	private Properties propertie;
	private String fileFullPath;
	private FileInputStream inputFile;
	private FileOutputStream outputFile;
	public PropertiesUtils(){
		this.propertie = new Properties();
		try {
			fileFullPath = System.getProperty("user.dir") + "/" + "config.properties";
			this.inputFile = new FileInputStream(fileFullPath);
			this.propertie.load(inputFile);
			this.inputFile.close();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}
	
	public String getValue(String key) {
		if (propertie.containsKey(key)) {
			String value = propertie.getProperty(key);
			return value;
		} else
			return "";
	}

}
