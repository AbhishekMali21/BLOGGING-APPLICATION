package com.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog.services.FileService;
import com.blog.utils.LoggingUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		LoggingUtils.logMethodStart();
		// File name
		String name = file.getOriginalFilename();
		// random name generate file
		String randomID = UUID.randomUUID().toString();
		String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));
		// full path
		String filePath = path + File.separator + fileName;
		// create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		// file copy
		Files.copy(file.getInputStream(), Paths.get(filePath));
		log.info(fileName);
		LoggingUtils.logMethodEnd();
		return fileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		LoggingUtils.logMethodStart();
		String fullPath = path + File.separator + fileName;
		InputStream is = new FileInputStream(fullPath);
		log.info(is);
		LoggingUtils.logMethodEnd();
		return is;
	}

}
