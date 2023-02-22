package com.hubino.uploaddownload.fileuploaddownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandlerResponse;
import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;

import jakarta.validation.Valid;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private FileAttachmentRepository attachmentRepository;
	
	private static final String folderPath="E:\\My PDF_Files\\";
	
	//uploading the File to local Directory
	
	public ResponseEntity<?> fileUpload(@Valid MultipartFile files) 
	{
		String filePath=folderPath+files.getOriginalFilename();
		
		FileAttachment fileAttachment=attachmentRepository.save(
				FileAttachment.builder()
				.filename(files.getOriginalFilename())
				.fileType(files.getContentType())
				.filePath(filePath).build());
		
		//copying the files from db to the specified local path
				
		try {
				files.transferTo(new File(filePath));
		} catch (IllegalStateException | IOException e) {
			return ErrorHandler.response(ErrorHandler.FILE_COPY_FAILED, HttpStatus.NOT_FOUND);
		}
		if(fileAttachment!=null)
		{
			return ErrorHandler.response(ErrorHandler.FILE_UPLOAD, HttpStatus.ACCEPTED);
		}
		else
		return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
	}

	
	public String downloadFile(@Valid String filename)
	{
		
		//getting the filename from the repository as an optional
	Optional<FileAttachment> attachment=attachmentRepository.findByFilename(filename);
	
			//getting the filepath
			String filePath=attachment.get().getFilePath();
			
			//converting it to byte Array using the filepath
			byte[] downloadedFile=null;
			String base64File=null;
			try {
				downloadedFile = Files.readAllBytes(new File(filePath).toPath());
				//converting the ByteArray to Base64 String format
				base64File=Base64.getEncoder().encodeToString(downloadedFile);
				return base64File;
			} catch (IOException e) {
				ErrorHandler.response(ErrorHandler.BYTE_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
			}
			return base64File;

	}


	public ResponseEntity<?> getFilesById(Integer id) {
		Optional<FileAttachment> attachment=attachmentRepository.findById(id);
		
		if(attachment.isPresent())
		{
			attachment.get();
			return ErrorHandler.response(ErrorHandler.FILE_RETRIEVED,HttpStatus.OK);
		}
		else {
			return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
}
