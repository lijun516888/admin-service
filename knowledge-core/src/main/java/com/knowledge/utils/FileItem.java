package com.knowledge.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileItem implements Serializable {

	private static final long serialVersionUID = -8639964771831617764L;

	public FileItem() {}
	
    private String fileName;
    private String mimeType = "application/octet-stream";
    private byte[] content;
}
