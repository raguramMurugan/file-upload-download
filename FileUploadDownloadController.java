package com.hubino.uploaddownload.fileuploaddownload.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.service.FileServiceImpl;

import jakarta.validation.Valid;

@RestController
public class FileUploadDownloadController {
	
	@Autowired
	private FileServiceImpl fileService;
	
	@PostMapping("/fileUpload")
	public ResponseEntity<?> fileUpload(@Valid @RequestParam("file") MultipartFile files)
	{
		if(files!=null)
		{
			fileService.fileUpload(files);
			return ErrorHandler.response(ErrorHandler.FILE_UPLOAD, HttpStatus.ACCEPTED);
		}
		else
		return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED,HttpStatus.NOT_FOUND); 
	}
	
	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<?> downloadFile(@Valid @PathVariable("filename") String filename)
	{
		if(filename!=null)
		{
			String downloadedFile=fileService.downloadFile(filename);
			return ResponseEntity.status(HttpStatus.OK)
					.body(downloadedFile);
		}
		else
		return ErrorHandler.response(ErrorHandler.INVALID_FILE_UPLOAD, HttpStatus.UNAUTHORIZED);
	}
	@GetMapping("/getFileById/{id}")
	public ResponseEntity<?> getFilesById(@Valid @PathVariable("id") Integer id)
	{
		if(id!=null)
		{
			ResponseEntity<?> files=fileService.getFilesById(id);
			return ResponseEntity.status(HttpStatus.OK).body(files);
		}
		else 
		{
			ErrorHandler.response(ErrorHandler.FILE_ID_EMPTY, HttpStatus.NOT_FOUND);
		}
		return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
}

