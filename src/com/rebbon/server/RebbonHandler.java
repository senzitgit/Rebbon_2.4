package com.rebbon.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/RebbonHandler")
public class RebbonHandler extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	String liveCam1="";
	String liveCam2="";
	String liveCam3="";
	String liveCam4="";
	String liveCam5="";
	String liveCam6="";
	String liveAud1="";
	String liveAud2="";
	String liveAud3="";
	String liveAud4="";
	
	String recordFeed1="";
	String recordFeed2="";
	String recordFeed3="";
	String recordFeed4="";
	String recordFeed5="";
	String recordFeed6="";
	String recordFeed7="";
	String recordFeed8="";
	String recordFeed9="";
	String recordFeed10="";
	
	String shHome="";
	String workingDirectory="";
	String eviRecordHome="";
	String clazRecordHome="";
	String tempFolder="";
	String rebId="";
	String IpAddressForAppServer="";
	String IpAddressForClient="";
	String portNumber="";
	
	public RebbonHandler() {
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		
		liveCam1 = config.getInitParameter("liveCam1");
		liveCam2 = config.getInitParameter("liveCam2");
		liveCam3 = config.getInitParameter("liveCam3");
		liveCam4 = config.getInitParameter("liveCam4");
		liveCam5 = config.getInitParameter("liveCam5");
		liveCam6 = config.getInitParameter("liveCam6");
		
		
		liveAud1 = config.getInitParameter("liveAud1");
		liveAud2 = config.getInitParameter("liveAud2");
		liveAud3 = config.getInitParameter("liveAud3");
		liveAud4 = config.getInitParameter("liveAud4");
		
		recordFeed1 = config.getInitParameter("liveCam1");
		recordFeed2 = config.getInitParameter("liveCam2");
		recordFeed3 = config.getInitParameter("liveCam3");
		recordFeed4 = config.getInitParameter("liveCam4");
		recordFeed5 = config.getInitParameter("liveAud1");
		recordFeed6 = config.getInitParameter("liveAud2");
		recordFeed7 = config.getInitParameter("liveAud3");
		recordFeed8 = config.getInitParameter("liveAud4");
		recordFeed9 = config.getInitParameter("liveCam5");
		recordFeed10 = config.getInitParameter("liveCam6");
		
		shHome = config.getInitParameter("SHFolder");
		workingDirectory = config.getInitParameter("WorkingDirecory");
		eviRecordHome = config.getInitParameter("EviRecordHome");
		clazRecordHome = config.getInitParameter("ClazRecordHome");
		tempFolder = config.getInitParameter("TempDirectory");
		rebId=config.getInitParameter("RebId");
		IpAddressForAppServer=config.getInitParameter("IpAddressForAppServer");
		IpAddressForClient=config.getInitParameter("IpAddressForClient");
		portNumber=config.getInitParameter("RebbonPort");
		
		System.out.println("REBBON INITIALIZATION COMPLETED");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestOrigin="";
//		int resultCode=0;
//		String message="";
		String jsonResponseText="";
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding("UTF-8");
		}
		response.setContentType("application/json; charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		RecordManager manageObj = new RecordManager();
		
		
		/* Following multipart request is for video editing from ClazPortal.
		 * It's not in use now. Can be commented.
		 */
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			String fileName1=null;
			String fileName2=null;
			String firstFileName=null;
            String start1=null;
            String end1=null;
            String start2=null;
            String end2=null;
            String root=null;
            
            //delete current files

            root = clazRecordHome+"uploads";
            
            File delFile1 = new File(root+File.separatorChar+"final1.mp4");
            delFile1.delete();
            File delFile2 = new File(root+File.separatorChar+"final2.mp4");
            delFile2.delete();
            File delFile3 = new File(root+File.separatorChar+"output.mp4");
            delFile3.delete();
            File delFile4 = new File(root+File.separatorChar+"FINAL.mp4");
            delFile4.delete();
            
            try {
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                System.out.println("List size = "+items.size());
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
 
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        
                        //fileName.replaceAll("\\s+","");
                        
                        
                        System.out.println("Filename "+ fileName);
                        
                        if(fileName1==null)
                        	fileName1=fileName;
                        else if(fileName2==null && !fileName.equals(fileName1))
                        	fileName2=fileName;
                        
 
                        File path = new File(root);
                        if (!path.exists()) {
                            throw new Exception("Upload Folder not found");
                        }
 
                        File uploadedFile = new File(root + File.separatorChar + fileName);
                        item.write(uploadedFile);
                    }
                    else{
                    	
                    	String fieldName = item.getFieldName();
                    	if(fieldName.equals("firstFileName")){
                    		
                    		firstFileName=item.getString();
                    		//firstFileName.replaceAll("\\s+","");
                    	}
                    	else if(fieldName.equals("start1"))
                    		start1=item.getString();
                    	else if(fieldName.equals("end1"))
                    		end1=item.getString();
                    	else if(fieldName.equals("start2"))
                    		start2=item.getString();
                    	else if(fieldName.equals("end2"))
                    		end2=item.getString();
                    	
                    }
                }

            	System.out.println(start1);
            	System.out.println(end1);
            	System.out.println(start2);
            	System.out.println(end2);
                
                
                if(!fileName1.equals(firstFileName) && fileName2.equals(firstFileName)){
                	
                	String temp=fileName1;
                	fileName1=fileName2;
                	fileName2=temp;
                }
                
                System.out.println(fileName1);
                System.out.println(fileName2);
                                
        		//String command11[] = {"gnome-terminal","-x","bash","-c",root+"/ffmpeg -i "+root+"/file1.mp4 -force_key_frames "+time11+","+time12+" "+folder+"/out1.mp4"};
        		String command12[] = {"gnome-terminal","-x","bash","-c",root+"/ffmpeg -ss "+start1+" -i "+root+File.separatorChar+fileName1+" -t "+manageObj.diffTime(start1,end1)+" -vcodec copy -acodec copy -y "+root+File.separatorChar+"final1.mp4"};
        		//String command21[] = {"gnome-terminal","-x","bash","-c",root+"/ffmpeg -i "+root+"/file2.mp4 -force_key_frames "+time21+","+time22+" "+folder+"/out2.mp4"};
        		String command22[] = {"gnome-terminal","-x","bash","-c",root+"/ffmpeg -ss "+start2+" -i "+root+File.separatorChar+fileName2+" -t "+manageObj.diffTime(start2, end2)+" -vcodec copy -acodec copy -y "+root+File.separatorChar+"final2.mp4"};
        		
        		String commandConcat[] = {"gnome-terminal","-x","bash","-c",root+"/ffmpeg -f concat -i "+root+"/mylist.txt -c copy "+root+"/output.mp4"};
        		
        		String commandWaterMark[] = {"gnome-terminal","-x","bash","-c",
        				root+"/ffmpeg -i "+root+"/output.mp4 -i "+root+"/logo.png -filter_complex \"overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2\" -codec:a copy "+root+"/FINAL.mp4"};
                
                
        		Runtime rt=Runtime.getRuntime();
    			/*Process p1 = rt.exec(command11);
    			Process p2 = rt.exec(command21);
    			p1.waitFor();
    			p2.waitFor();*/
    			Process p3 = rt.exec(command12);
    			p3.waitFor();
    			Process p4 = rt.exec(command22);
    			p4.waitFor();
    			
    			
    			
    			//Concat using FFmpeg
    			
    			Process p5=rt.exec(commandConcat);
    			
    			p5.waitFor();
    			
    			
    			//delete input files
    			File delFile5 = new File(root+File.separatorChar+fileName1);
                delFile5.delete();
                File delFile6 = new File(root+File.separatorChar+fileName2);
                delFile6.delete();
    			
    			
    			Process p6 = rt.exec(commandWaterMark);
    			p6.waitFor();
    			
                                
    			JsonParser<String,String > jsonResponse = new JsonParser<String,String>("concateVideo", 1, "Success", "resultLink", "http://"+IpAddressForClient+":"+portNumber+"/Rebbon/ClazRecords/uploads/FINAL.mp4");
    			String  jsonResponseStr =  jsonResponse.JsonResponseText();
    			System.out.println("jsonResponse : " + jsonResponseStr);
    			response.getWriter().println(jsonResponseStr);
				response.getWriter().flush();
                
                
                
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		else {
			
			requestOrigin = request.getParameter("origin");
			System.out.println("RebbonOrigin : "+requestOrigin);
						
			if(requestOrigin.equals("StartMediaServer")){
				
				//manageObj.executeShFile(shHome+"ffserver.sh");
				
				JsonParser<String,String > jsonResponse = new JsonParser<String,String>("StartMediaServer", 1, "Media Server Started!!", "MediaServerStatus", "Online");
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("StartRebbon")){
				
				//manageObj.executeShFile(shHome+"start.sh");
				
				Properties propIn1 = new Properties();
		    	Properties propIn2 = new Properties();
		    	Properties propIn3 = new Properties();
		    	Properties propIn4 = new Properties();
		    	Properties propIn5 = new Properties();
		    	Properties propIn6 = new Properties();
		    	Properties result = new Properties();
		    	
		    	propIn1.put("Url",liveCam1);
		    	propIn1.put("Aud",liveAud1);
		    	result.put("CAM1", propIn1);
		    	
		    	propIn2.put("Url",liveCam2);
		    	propIn2.put("Aud",liveAud2);
		    	result.put("CAM2", propIn2);
		    	
		    	propIn3.put("Url",liveCam3);
		    	propIn3.put("Aud",liveAud3);
		    	result.put("CAM3", propIn3);
		    	
		    	propIn4.put("Url",liveCam4);
		    	propIn4.put("Aud",liveAud4);
		    	result.put("CAM4", propIn4);
		    	
		    	propIn5.put("Url",liveCam5);
		    	propIn5.put("Aud",liveAud4);
		    	result.put("CAM5", propIn5);
		    	
		    	propIn6.put("Url",liveCam6);
		    	propIn6.put("Aud",liveAud4);
		    	result.put("CAM6", propIn6);
		    	
		    	JsonParser<String,Properties > jsonResponse = new JsonParser<String,Properties>("StartRebbon", 1, "Stream Started!!", "liveCamDetails", result);
		    	jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("StopClazRebbon")){
				
				String jsonImage = request.getParameter("jsonImage");
				int eventId= Integer.parseInt(request.getParameter("classEventId"));
				manageObj.executeShFile(shHome+"Qkill.sh");
				
				manageObj.saveRecordings(clazRecordHome, workingDirectory, jsonImage, eventId,
						recordFeed1,recordFeed2,recordFeed3,recordFeed4,
						recordFeed5,recordFeed6,recordFeed7,recordFeed8,
						"cyberclaz",request.getParameter("jsonWatermark"));
				
				JsonParser<String,String> jsonResponse = new JsonParser<String,String>("StopRebbon", 1, "Stream Stopped!!", "RecordStatus", "Stopped");
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("StopEviRebbon")){
				
				String jsonImage = request.getParameter("jsonImage");
				int eventId= Integer.parseInt(request.getParameter("caseEventId"));
				//manageObj.executeShFile(shHome+"Qkill.sh");
				
				manageObj.saveRecordings(eviRecordHome, workingDirectory, jsonImage, eventId,
						recordFeed1,recordFeed2,recordFeed3,recordFeed4,
						recordFeed5,recordFeed6,recordFeed7,recordFeed8,
						"evidencer",request.getParameter("jsonWatermark"));
				
				JsonParser<String,String> jsonResponse = new JsonParser<String,String>("StopRebbon", 1, "Stream Stopped!!", "RecordStatus", "Stopped");
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("GetLiveFeeds")){
				
				Properties propIn1 = new Properties();
		    	Properties propIn2 = new Properties();
		    	Properties propIn3 = new Properties();
		    	Properties propIn4 = new Properties();
		    	Properties result = new Properties();
		    	
		    	propIn1.put("Url",liveCam1);
		    	propIn1.put("Aud",liveAud1);   
		    	result.put("CAM1", propIn1);
		    	
		    	propIn2.put("Url",liveCam2);
		    	propIn2.put("Aud",liveAud2);   
		    	result.put("CAM2", propIn2);
		    	
		    	propIn3.put("Url",liveCam3);
		    	propIn3.put("Aud",liveAud3);   
		    	result.put("CAM3", propIn3);
		    	
		    	propIn4.put("Url",liveCam4);
		    	propIn4.put("Aud",liveAud4);   
		    	result.put("CAM4", propIn4);
		    	
		    	JsonParser<String,Properties> jsonResponse = new JsonParser<String,Properties>("getLiveFeeds", 1, "Stream Fetched!!", "liveCamDetails", result);
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("GetClazAvFiles")){
				
				Integer classEventId = Integer.parseInt(request.getParameter("classEventId"));
				String userName = request.getParameter("userName");
				Properties prop = manageObj.getAvFiles(tempFolder+File.separatorChar+userName,"Temp",userName,
						clazRecordHome, classEventId, IpAddressForClient, portNumber,
						recordFeed1, recordFeed2, recordFeed3, recordFeed4,
						recordFeed5, recordFeed6, recordFeed7, recordFeed8,recordFeed9,recordFeed10,"ClazRecords");
				JsonParser<String,Properties> jsonResponse = new JsonParser<String,Properties>("GetAvFiles", 1, "Av Files Fetched", "RecordedFeeds", prop);
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("GetEviAvFiles")){
				
				Integer caseEventId = Integer.parseInt(request.getParameter("caseEventId"));
				String userName = request.getParameter("userName");
				Properties prop = manageObj.getAvFiles(tempFolder+File.separatorChar+userName,"Temp",userName,
						eviRecordHome,
						caseEventId, IpAddressForClient, portNumber,
						recordFeed1, recordFeed2, recordFeed3, recordFeed4,
						recordFeed5, recordFeed6, recordFeed7, recordFeed8,recordFeed9,recordFeed10, "CaseRecords");
				JsonParser<String,Properties> jsonResponse = new JsonParser<String,Properties>("GetAvFiles", 1, "Av Files Fetched", "RecordedFeeds", prop);
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
			else if(requestOrigin.equals("GetEviAttachment")){
				
				String caseEventId = request.getParameter("caseEventId");
				String userName = request.getParameter("userName");
				
				
				//If encryption used
				//String attachmentPath = "http://"+IpAddressForClient+":"+portNumber+"/Rebbon/Temp/"+userName+"/";
				
				//If encryption not used
				String attachmentPath = "http://"+IpAddressForClient+":"+portNumber+"/Rebbon/"+caseEventId+"/";
				
				JsonParser<String,String > jsonResponse = new JsonParser<String,String>("GetAttachment", 1, "Attachment Files Fetched", "Attachments", attachmentPath);
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
//			else if(requestOrigin.equals("GetClazAttachment")){
//				
//				
//			}
			
//			else if(requestOrigin.equals("getMediaLinkFromJson")){				
//				
//			}
			
			else if(requestOrigin.equals("ping")){
				
				Properties obj = new Properties();
				obj.put("RebbonId",rebId);
				JsonParser<String,Properties > jsonResponse = new JsonParser<String,Properties>("RebbonId", 1, "Success", "RebbonResponse", obj);
				jsonResponseText = jsonResponse.JsonResponseText();
			}
			
//			else if(requestOrigin.equals("getTime")){
//				
//				Properties obj=new Properties();
//				obj.put("time", System.currentTimeMillis());
//				JsonParser<String,Properties > jsonResponse = new JsonParser<String,Properties>("RebbonId", 1, "Success", "RebbonResponse", obj);
//				jsonResponseText = jsonResponse.JsonResponseText();
//			}
			
//			else if(requestOrigin.equals("uploadFile")){
//				
//				
//			}
			else {
				System.out.println("INVALID ORIGIN IN REBBON : "+requestOrigin);
				jsonResponseText = "Invalid origin "+requestOrigin;
			}
			
			
			System.out.println("Response From Rebbon : " + jsonResponseText);
			response.getWriter().println(jsonResponseText);
			response.getWriter().flush();
		}
	}

}
