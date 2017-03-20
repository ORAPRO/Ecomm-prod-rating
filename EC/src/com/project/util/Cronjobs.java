package com.project.util;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class Cronjobs extends Configured implements Tool {

	private static PropertiesReader propertyReader;
	
	public static void main(String[] args) throws Exception {
		System.out.println("In Main Method");
		
		//Step-1 Validate input arguments
		if(args.length < 1){
			System.out.println("Java Usage" + Cronjobs.class.getName() + "In valid arguments lenth and Properties path");
	       return;
		}
    		propertyReader = PropertiesReader.getInstance();
	        propertyReader.loadProperties(args[0]);
		
		//step-2 Initialise configuration
		Configuration con = new Configuration(Boolean.TRUE);
		con.set("fs.defaultFS", "hdfs://localhost:8020");
		
		//step-3 Run ToolRunner.run method to set the arguments to config
		try{
			int i = ToolRunner.run(con, new Cronjobs(), args);
			if(i == 0){
				System.out.println(HDFSUTIL.SUCCESS);
			}else{
				System.out.println(HDFSUTIL.FAILED + "STATUS code:" + i);
			}
		}catch(Exception e){
			System.out.println(HDFSUTIL.FAILED);
			e.printStackTrace();
		}
		
	}

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("In Run Method");
		
		final String fBaseLocation = propertyReader.getProperty(Properties.BASE_LOCATION);
		final String fFileSourceLocation = fBaseLocation + System.getProperty(HDFSUTIL.FILE_SEPERATOR_PROPERTY) + propertyReader.getProperty(Properties.LANDING_ZONE);
		
		//create directory if does not exist
		FileUtil.createDirectory(fFileSourceLocation);
		final String fArchiveLocation = fBaseLocation + System.getProperty(HDFSUTIL.FILE_SEPERATOR_PROPERTY) + propertyReader.getProperty(Properties.ARCHIVE);
		
		FileUtil.createDirectory(fArchiveLocation);
		final String fFailedLocation = fBaseLocation +System.getProperty(HDFSUTIL.FILE_SEPERATOR_PROPERTY)  +propertyReader.getProperty(Properties.FAILED);
		
		final String fHDFSBaseLocation = propertyReader.getProperty(Properties.HDFS_BASE_LOCATION);
		final String fDestinationPath = fHDFSBaseLocation +System.getProperty(HDFSUTIL.FILE_SEPERATOR_PROPERTY)+ propertyReader.getProperty(Properties.HDFS_LANDING_ZONE);
		
		// Load the configuration
		Configuration con = getConf();
		

		// Create a instance for File System object.
		FileSystem hdfs = FileSystem.get(con);
		
		// Create directory on HDFS File System if does not exist.
		HDFSUTIL.createdHDFSDirectories(hdfs, fDestinationPath);
		
		
		while(true){
			File  fInboxDir = new File(fFileSourceLocation);
			if(fInboxDir.isDirectory()){
				File[] fListFiles = fInboxDir.listFiles();
				for(File fInputFile : fListFiles){
					String[] args = {fInputFile.getAbsolutePath().toString(), fDestinationPath};
					boolean isCopied = HDFSUTIL.copyFromLocal(con, hdfs, args);
					if(isCopied){
						FileUtil.moveFile(fInputFile, new File(fArchiveLocation));
					}
					else{
						FileUtil.moveFile(fInputFile, new File(fFailedLocation));
					}
				}
			}
			System.out.println("mounika");
			Thread.sleep(1000 * 60 * 2);
		}
	}

}
