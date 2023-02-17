package com.hubino.uploaddownload.fileuploaddownload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.hubino.uploaddownload.fileuploaddownload.service.FileServiceImpl;

@RestController
public class FileUploadDownloadController {
	
	@Autowired
	private FileServiceImpl fileService;
	
	@PostMapping("/fileUpload")
	public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile files) throws IllegalStateException, IOException
	{
		 String fileUpload=fileService.fileUpload(files);
		 return ResponseEntity.status(HttpStatus.OK)
				 .body(fileUpload);
	}
	
	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename) throws IOException
	{
		String downloadedFile=fileService.downloadFile(filename);
		return ResponseEntity.status(HttpStatus.OK)
				.body(downloadedFile);
	}

}
