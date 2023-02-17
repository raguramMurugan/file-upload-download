package com.hubino.uploaddownload.fileuploaddownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private FileAttachmentRepository attachmentRepository;
	
	private static final String folderPath="E:\\My PDF_Files/";
	
	//uploading the File to local Directory
	
	public String fileUpload(MultipartFile files) throws IllegalStateException, IOException
	{
		String filePath=folderPath+files.getOriginalFilename();
		
		FileAttachment fileAttachment=attachmentRepository.save(
				FileAttachment.builder()
				.filename(files.getOriginalFilename())
				.fileType(files.getContentType())
				.filePath(filePath).build());
		
		//copying the files from db to the specified local path
			files.transferTo(new File(filePath));
		
		if(fileAttachment!=null)
		{
			return "File Uploaded Succesfully:"+filePath;
		}
		
		return "File Uploaded Failed";
	}

	
	public String downloadFile(String filename) throws IOException
	{
		
		//getting the filename from the repository as an optional
	Optional<FileAttachment> attachment=attachmentRepository.findByFilename(filename);
	
			//getting the filepath
			String filePath=attachment.get().getFilePath();
			
			//converting it to byte Array using the filepath
			byte[] downloadedFile=Files.readAllBytes(new File(filePath).toPath());
			
			//converting the ByteArray to Base64 String format
			String base64File=Base64.getEncoder().encodeToString(downloadedFile);
		
		return base64File;
	}
}
