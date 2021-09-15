package com.dohung.img.web.rest;

import com.dohung.img.service.FileStorageService;
import com.dohung.img.web.rest.response.UploadFileResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileResponse uploadFileResponseReturn = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/downloadFile/")
            .path(uploadFileResponseReturn.getFileName())
            .toUriString();
        uploadFileResponseReturn.setFileType(file.getContentType());
        uploadFileResponseReturn.setSize(file.getSize());

        return uploadFileResponseReturn;
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    //    @PostMapping("/uploadFile")
    //    public ResponseEntity uploadFile(
    //        @RequestParam("file") MultipartFile file
    //    ) {
    //
    //        String fileName = fileStorageService.storeFile(file);
    //
    //        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
    //        System.out.println("fileDownloadUri" + fileDownloadUri);
    //
    //        return new ResponseEntity(fileName, HttpStatus.OK);
    //    }
    //
    //    @PostMapping("/uploadMultipleFiles")
    //    public ResponseEntity uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
    //        return new ResponseEntity(Arrays.asList(files)
    //            .stream()
    //            .map(file -> uploadFile(file))
    //            .collect(Collectors.toList()), HttpStatus.OK);
    //    }

    //    @PostMapping("/uploadMultipleFiles")
    //    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
    //        return Arrays.asList(files)
    //            .stream()
    //            .map(file -> uploadFile(file))
    //            .collect(Collectors.toList());
    //    }

}
