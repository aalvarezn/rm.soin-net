package com.soin.sgrm.model;

import java.io.File;

public class ImageTree {
	
	   private byte[] ProductImg;
	    
	    private File UploadFile;
	    
	    private String Base64Img;

		public byte[] getProductImg() {
			return ProductImg;
		}

		public void setProductImg(byte[] productImg) {
			ProductImg = productImg;
		}

		public File getUploadFile() {
			return UploadFile;
		}

		public void setUploadFile(File uploadFile) {
			UploadFile = uploadFile;
		}

		public String getBase64Img() {
			return Base64Img;
		}

		public void setBase64Img(String base64Img) {
			Base64Img = base64Img;
		}
	    
	    
}
