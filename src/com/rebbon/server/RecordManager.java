package com.rebbon.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecordManager {
	
	private final String key = "Mary has one cat";
	
	public String diffTime(String time1,String time2){
		
		java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        java.util.Date date1;
		java.util.Date date2;
		try {
			date1 = df.parse(time1);
			date2 = df.parse(time2);
			long millis = date2.getTime() - date1.getTime();
			
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

			return hms;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void executeShFile(String path){
		
		String command[] = {"gnome-terminal", "-x", "bash", "-c", path+";"};
		Runtime rt=Runtime.getRuntime();
		try {
			rt.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveRecordings(String recordFolderPath,String workingDirectory,String jsonImage,int eventId,
			String v1,String v2,String v3,String v4,String a1,String a2,String a3,String a4,
			String app,String jsonWatermark){
		
		JSONArray serverJsonResponse = null;
		File recordFolder = new File(recordFolderPath+File.separatorChar+eventId);
//		if(recordFolder.exists()) recordFolder.delete();
		recordFolder.mkdir();
		try {
			serverJsonResponse = new JSONArray(jsonImage);
			for (int i = 0; i < serverJsonResponse.length(); i++) {
				
				JSONObject obj;
				obj = serverJsonResponse.getJSONObject(i);
				String image = obj.getString("image"+i);
				String filename = obj.getString("file"+i);
				
				URL newUrl = new URL(image);
				File newFile = new File(recordFolderPath+eventId+File.separatorChar+filename);
				org.apache.commons.io.FileUtils.copyURLToFile(newUrl,newFile);
				
				
				// ENCRYPTION OF ATTACHMENTS
				/*
		        try {
					CryptoUtils.encrypt(key, newFile, new File(recordFolderPath+eventId+File.separatorChar+filename+".encrypted"));
					newFile.delete();
				} catch (CryptoException e) {
					e.printStackTrace();
				}
				*/
		        
			}
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
		
		// ADDING WATERMARK
		/*
		if("evidencer".equals(app)){
			
			addEviWaterMark(workingDirectory,v1,jsonWatermark);
			addEviWaterMark(workingDirectory,v2,jsonWatermark);
			addEviWaterMark(workingDirectory,v3,jsonWatermark);
			addEviWaterMark(workingDirectory,v4,jsonWatermark);
			
		}
		else if("cyber".equals(app)){
			
			addCyberWaterMark(workingDirectory,v1,jsonWatermark);
			addCyberWaterMark(workingDirectory,v2,jsonWatermark);
			addCyberWaterMark(workingDirectory,v3,jsonWatermark);
			addCyberWaterMark(workingDirectory,v4,jsonWatermark);
			
		}
		*/
		
		copyFile(workingDirectory+v1,recordFolderPath+eventId+File.separatorChar+v1);
		copyFile(workingDirectory+v2,recordFolderPath+eventId+File.separatorChar+v2);
		copyFile(workingDirectory+v3,recordFolderPath+eventId+File.separatorChar+v3);
		copyFile(workingDirectory+v4,recordFolderPath+eventId+File.separatorChar+v4);
		copyFile(workingDirectory+a1,recordFolderPath+eventId+File.separatorChar+a1);
		copyFile(workingDirectory+a2,recordFolderPath+eventId+File.separatorChar+a2);
		copyFile(workingDirectory+a3,recordFolderPath+eventId+File.separatorChar+a3);
		copyFile(workingDirectory+a4,recordFolderPath+eventId+File.separatorChar+a4);
	}
	
	private void copyFile(String source, String dest){
		
		File afile =new File(source);
		System.out.println("File moving from "+source+" to "+dest);
		if(afile.renameTo(new File(dest)))
			System.out.println("File moved");
		else System.out.println("Error in moving file");
		
		// ENCRYPTION OF ATTACHMENTS
		/*
		
        try {
			CryptoUtils.encrypt(key, new File(source), new File(dest+".encrypted"));
		} catch (CryptoException e) {
			e.printStackTrace();
		}
		*/
//        CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
	}
	
	private void addEviWaterMark(String folder,String file, String jsonWatermark){		
		
		try {
			
			JSONObject obj = new JSONObject(jsonWatermark);
			String caseNo = obj.getString("caseNo");
			
			String commandWaterMark[] = {"gnome-terminal","-x","bash","-c",
					folder+"/ffmpeg -i "+folder+"/"+file+" -vf \"[in]drawtext=fontsize=20:fontcolor=black@0.5:text='null':x=(2):y=(h-20), drawtext=fontsize=20:fontcolor=black@0.5:text=' Case No : "+caseNo+"':x=(2):y=(h-20-25),drawtext=fontsize=20:fontcolor=black@0.5:text='Date : 11-12-2015':x=(2):y=(h-20-50)[out]\" -y output.mp4"};
            
			Runtime rt=Runtime.getRuntime();
			Process p = rt.exec(commandWaterMark);
			p.waitFor();
			
			File s = new File(folder+File.separatorChar+"output.mp4");
			s.renameTo(new File(folder+File.separatorChar+file));
			
		} catch (JSONException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void addCyberWaterMark(String folder,String file, String jsonWatermark){
		
		
		
	}
	
	public Properties getAvFiles(String tempFolderPath,String tempFolderName,String userName,String recordPath,int eventId,String ipAddress,String port,
			String cam1,String cam2,String cam3,String cam4,String mic1,String mic2,String mic3,String mic4,String cam5,String cam6,
			String recordFolderName){
		
		
		
		// DECRYPTION COMMENTED
		
		/*
		
		//First Decrypt All Files
		File folder = new File(tempFolderPath);
		folder.mkdir();
		
		File fileFolder = new File(recordPath+eventId);
		File[] fileArray = fileFolder.listFiles();
		
        try {
        	
        	for(File eachFile : fileArray){
        		String name = eachFile.getName();
        		name = name.substring(0, name.indexOf(".encrypted"));
        		CryptoUtils.decrypt(key, eachFile, new File(tempFolderPath+File.separatorChar+name));
        	}
			
			
		} catch (CryptoException e) {
			e.printStackTrace();
		}
		*/
		
		
		
		
		// Use this if encrypted
		/*
		
		Properties propIn = new Properties();
		
		propIn.put("VideoFeed1","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+cam1);
		propIn.put("AudioFeed1","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+mic1);   
		
		propIn.put("VideoFeed2","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+cam2);
		propIn.put("AudioFeed2","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+mic2);   
				 
		propIn.put("VideoFeed3","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+cam3);
		propIn.put("AudioFeed3","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+mic3);   
				
		propIn.put("VideoFeed4","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+cam4);
		propIn.put("AudioFeed4","http://"+ipAddress+":"+port+"/Rebbon/"+tempFolderName+"/"+userName+"/"+mic4);
		
		*/
		
		//Use this if not encrypted	

		
		Properties propIn = new Properties();
		
		propIn.put("VideoFeed1",cam1);
		propIn.put("AudioFeed1",mic1);   
		
		propIn.put("VideoFeed2",cam2);
		propIn.put("AudioFeed2",mic2);   
				 
		propIn.put("VideoFeed3",cam3);
		propIn.put("AudioFeed3",mic3);   
				
		propIn.put("VideoFeed4",cam4);
		propIn.put("AudioFeed4",mic4);
		
		propIn.put("VideoFeed5",cam5);
		propIn.put("AudioFeed4",mic4);
		
		propIn.put("VideoFeed6",cam6);
		propIn.put("AudioFeed4",mic4);
		
				
		return propIn;
	}

}
