//package com.example.finalProject_synrgy.controller;
//
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import com.example.finalProject_synrgy.dto.UploadFileResponse;
//import com.example.finalProject_synrgy.dto.base.BaseResponse;
//import com.example.finalProject_synrgy.service.FileStorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.Objects;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Tag(name = "Files")
//@RestController
//@EnableCaching
//@Slf4j
//public class FileStorageController {
//
//    @Value("${app.uploadto.cdn}")
//    private String UPLOADED_FOLDER;
//
//    @Autowired
//    private FileStorageService fileStorageService;
//
//    @RequestMapping(value = "v1/files", method = RequestMethod.POST, consumes = {"multipart/form-data", "application/json"})
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        String strDate = String.valueOf(UUID.randomUUID());
//        String nameFormat = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
//        if (nameFormat.isEmpty()) {
//            nameFormat = ".png";
//        }
//        String fileName = UPLOADED_FOLDER + strDate + nameFormat;
//
//
//        String fileNameforDownload = strDate + nameFormat;
//        Path TO = Paths.get(fileName);
//
//        try {
//            Files.copy(file.getInputStream(), TO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(BaseResponse.success(new UploadFileResponse(fileNameforDownload, null,
//                    file.getContentType(), file.getSize(), e.getMessage()), "Success Post File"));
//
//        }
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/v1/files/")
//                .path(fileNameforDownload)
//                .toUriString();
//
//        return ResponseEntity.ok(BaseResponse.success(new UploadFileResponse(fileNameforDownload, fileDownloadUri,
//                file.getContentType(), file.getSize(), "false"), "Success Post File"));
//    }
//
//    @PostMapping("v1/files/multiples")
//    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
//        return ResponseEntity.ok(BaseResponse.success(Arrays.stream(files)
//                .map(file -> {
//                    try {
//                        return uploadFile(file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }).collect(Collectors.toList()), "Success Post File"));
//    }
//
//    @GetMapping("v1/files/{fileName:.+}")
//    public ResponseEntity<?> showFile(@PathVariable String fileName, HttpServletRequest request) { // Load file as Resource : step 1 load path lokasi name file
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            log.info("Could not determine file type.");
//        }
//        if (contentType == null) {
//            contentType = "application/octet-stream";// type .json
//        }
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
//}