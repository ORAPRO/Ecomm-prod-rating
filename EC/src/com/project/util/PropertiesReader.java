package com.project.util;

import java.io.FileInputStream;
import java.util.Properties;



public class PropertiesReader {
	
	
	private static PropertiesReader HealthcarePropertyReader;
	private static Properties properties;
	
	
	private PropertiesReader()
	{
		
	}
	public static final PropertiesReader getInstance(){
		if(HealthcarePropertyReader == null){
			HealthcarePropertyReader = new PropertiesReader();
		}
		return HealthcarePropertyReader;
		
	}
	public void loadProperties(String propertyFilePath){
		if(properties == null){
			properties = new Properties();
		}try{
			properties.load(new FileInputStream(propertyFilePath));
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	

		public String getProperty(String key){
			if(properties!=null)
			{
				return properties.getProperty(key);
			}
			return null;
			
	}
		public String getInt(String key){
			if(properties!=null) 
			{
				return properties.getProperty(key);
			}
			return null;
		}
		public String getBoolean(String key){
			if(properties!=null)
			{
				return properties.getProperty(key);
			}
			return null;
		}
		public String getFloat(String key){
			if(properties!=null)
			{
				return properties.getProperty(key);
			}
			return null;
		}
//		public static boolean loadProperty(final String schemaLoc)
//				throws IOException {
//			File file = new File(schemaLoc);
//			FileInputStream fileInput = new FileInputStream(file);
//			properties = new Properties();
//			properties.load(fileInput);
//			fileInput.close();
//			return Boolean.TRUE;
//		}		
		
		public Properties getAllProperties(){
			return properties;
			
		}
	
	
	
	



}
