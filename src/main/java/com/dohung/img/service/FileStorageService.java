package com.dohung.img.service;

import com.dohung.img.domain.Image;
import com.dohung.img.property.FileStorageProperties;
import com.dohung.img.repository.ImageRepository;
import com.dohung.img.web.rest.errors.FileStorageException;
import com.dohung.img.web.rest.errors.MyFileNotFoundException;
import com.dohung.img.web.rest.response.UploadFileResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileStorageService {

    @Autowired
    private ImageRepository imageRepository;

    //    public static final String UPLOAD_DIR = "D:\\DuLieuTmp\\SourceCode\\Cyber\\CyberTaxV2\\FE\\public\\img";
    public static final String UPLOAD_DIR = "D:\\SourceCode\\CodeJHipster\\FE\\public\\img";

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Không thể tạo thư mục để upload file.", ex);
        }
    }

    public UploadFileResponse storeFile(MultipartFile file) {
        String fileExtentions = ".txt,.xls,.xlsx,.xlsm,.doc,.docx,.pdf,.jpg,.png";
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        int lastIndex = fileName.lastIndexOf('.');
        String substring = fileName.substring(lastIndex, fileName.length());
        String nameOfFileWithoutExtension = fileName.substring(0, lastIndex);
        if (!fileExtentions.contains(substring)) {
            throw new FileStorageException("Upload file thất bại! file để upload phải có định dạng ( " + fileExtentions + " )");
        } else {
            try {
                // Check if the file's name contains invalid characters
                if (fileName.contains("..")) {
                    throw new FileStorageException("Thất bại! Tên file chưa đường dẫn không hợp lệ " + fileName);
                }
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

                //save to database
                Image image = new Image();
                image.setName(nameOfFileWithoutExtension);
                image.setPath(fileDownloadUri);
                image.setCreatedBy("api");
                image.setCreatedDate(LocalDateTime.now());
                image.setLastModifiedDate(LocalDateTime.now());

                Image imageRest = imageRepository.save(image);
                System.out.println("imageRest: " + imageRest);
                if (imageRest.getId() < 0) {
                    throw new FileStorageException("Lỗi lưu Image ");
                }
                UploadFileResponse uploadFileResponseTarget = new UploadFileResponse();
                BeanUtils.copyProperties(imageRest, uploadFileResponseTarget);
                uploadFileResponseTarget.setFileName(imageRest.getName());
                uploadFileResponseTarget.setFileDownloadUri(imageRest.getPath());

                return uploadFileResponseTarget;
            } catch (IOException ex) {
                throw new FileStorageException("Không thể lưu file " + fileName + ". Xin vui lòng thử lại!", ex);
            }
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("Không tìm thấy file " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Không tìm thấy file " + fileName, ex);
        }
    }
}
