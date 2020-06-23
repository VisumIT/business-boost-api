package com.visumIT.Business.boost.functions;

import java.util.ArrayList;

import com.visumIT.Business.boost.upload.FileUpload;


public class ImageValidations {

	private ArrayList<String> allowedExtensions = new ArrayList<>();
	
	public boolean validImage(FileUpload file) {
		String type = file.getMimetype();
		allowedExtensions.add("image/jpg");
		allowedExtensions.add("image/png");
		allowedExtensions.add("image/jpeg");

		if (!allowedExtensions.contains(type)) {
			return false;
		} else {
			return true;
		}
	}

	public ImageValidations() {
	}
	
	
}
