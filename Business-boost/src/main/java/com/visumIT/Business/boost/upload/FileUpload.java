package com.visumIT.Business.boost.upload;

public class FileUpload {
	
	private String fileName;
	private String mimetype;
	private String base64;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
	
	@Override
	public String toString() {
		return "FileName: " + fileName + "Mimetype: "+ mimetype;
	}
	
		
}
