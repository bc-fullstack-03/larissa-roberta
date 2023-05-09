package com.sysmap.parrot.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
	String upload(MultipartFile file, String fileName);
}
