package com.sysmap.parrot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.backupstorage.model.PutObjectRequest;
import com.amazonaws.services.iot.model.CannedAccessControlList;
import com.amazonaws.services.s3.AmazonS3;

@Service
public class AwsService {

	@Autowired
	private AmazonS3 amazonS3;

	public String upload(MultipartFile multipartFile, String fileName) throws Exception {
		var fileUri = "";

		try {
			var fileConverted = convertMultipartfileToFile(multipartFile);

			amazonS3.putObject(
					new PutObjectRequest("teste-parrot", fileName, fileConverted).withCannedAcl(CannedAccessControlList.PublicRead));

			fileUri = "http://s3.localhost.localstack.cloud:4566" + "/" + "teste-parrot" + "/" + fileName;

			fileConverted.delete();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return fileUri;
	}

	private File convertMultipartfileToFile(MultipartFile multipartFile) throws IOException {
		var convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		var fos = new FileOutputStream(convFile);
		fos.write(multipartFile.getBytes());
		fos.close();

		return convFile;
	}
}
