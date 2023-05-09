package com.sysmap.parrot.service;

import org.springframework.web.multipart.MultipartFile;

public interface IAwsService {
	String upload(MultipartFile multipartFile, String fileName);
}
