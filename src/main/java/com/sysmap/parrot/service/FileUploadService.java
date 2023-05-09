package com.sysmap.parrot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FileUploadService implements IFileUploadService {
	@Autowired
	private IAwsService awsService;

	public String upload(MultipartFile file, String fileName) {
		var fileUri = "";

		try {
			fileUri = awsService.upload(file, fileName);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return fileUri;
	}
}
