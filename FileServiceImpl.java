package com.hubino.uploaddownload.fileuploaddownload.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;
import jakarta.validation.Valid;

@Service
@Transactional
public class FileServiceImpl implements FileService{
	
	private static final Logger log=LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired
	private FileAttachmentRepository attachmentRepository;
	
	private static final String folderPath="F:\\My PDF_Files\\";
	
	//uploading the File to local Directory
	
	public ResponseEntity<?> fileUpload(@Valid MultipartFile files) 
	{
		String filePath=folderPath+files.getOriginalFilename();
		try {
			files.transferTo(new File(filePath));
			log.info("file Saving..");
			FileAttachment fileAttachment=attachmentRepository.save(
					FileAttachment.builder()
					.filename(files.getOriginalFilename())
					.fileType(files.getContentType())
					.filePath(filePath).build());
			
			if(fileAttachment!=null)
			{
				log.info("File not null");
				return ErrorHandler.successResponse(ErrorHandler.FILE_UPLOAD,HttpStatus.ACCEPTED);
			}
			else
			log.info("file is null");
			return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
		
			
		} catch (IllegalStateException | IOException e) {
			log.info("Path not found..."+" "+ e);
			return ErrorHandler.response(ErrorHandler.FILE_COPY_FAILED, HttpStatus.NOT_FOUND);
		}
}
		
		

	
	public ResponseEntity<?> downloadFile(@Valid String filename)
	{
		
		//getting the filename from the repository as an optional
	Optional<FileAttachment> attachment=attachmentRepository.findByFilename(filename);
	
			log.info("File status:"+" "+attachment);
			
			String filePath=null;
			byte[] downloadedFile=null;
			String base64File=null;
			
			if(!attachment.isPresent())
			{
			return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
			filePath= attachment.get().getFilePath();
			
			try {
				downloadedFile = Files.readAllBytes(new File(filePath).toPath());
				//converting the ByteArray to Base64 String format
				base64File=Base64.getEncoder().encodeToString(downloadedFile);
			} 
			catch (IOException e) {
				return ErrorHandler.response(ErrorHandler.BYTE_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
			}
			return ErrorHandler.successResponse(base64File, HttpStatus.ACCEPTED);
	}
	public ResponseEntity<?> getFilesById(Integer id) {
			FileAttachment attachment=attachmentRepository.findById(id).get();
			byte[] attachmentFile;
			String base64;
			
			if(!attachment.getId().equals(id))
			{
				log.info("id is:"+attachment.getId().equals(id));
				return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
				try {
					attachmentFile=Files.readAllBytes(new File(attachment.getFilePath()).toPath());
					log.info("File reading..");
				} catch (IOException e) {
					return ErrorHandler.response(ErrorHandler.BYTE_CONVERSION_FAILED,HttpStatus.BAD_REQUEST);
				}	
				base64=Base64.getEncoder().encodeToString(attachmentFile);
			return ErrorHandler.successResponse(base64, HttpStatus.ACCEPTED);
	}
}
