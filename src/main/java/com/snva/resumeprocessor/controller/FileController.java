package com.snva.resumeprocessor.controller;


import com.snva.resumeprocessor.objects.FileResponse;
import com.snva.resumeprocessor.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    IFileStorageService iFileStorageService;


    // POST "/uploadfile"

    @CrossOrigin(origins ="http://127.0.0.1:5501")
    @PostMapping("/uploadfile")
    public ResponseEntity<FileResponse> uploadSingleFile(@RequestParam("file")MultipartFile multipartFile){
       String upFile= iFileStorageService.saveFile(multipartFile);

       String fileDownloadUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(upFile)
                .toUriString();

        return  ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upFile,fileDownloadUri,"File Uploaded Successfully"));
    }
    @CrossOrigin(origins ="http://127.0.0.1:5501")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource upFile = iFileStorageService.loadFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + upFile.getFilename() + "\"")
                .body(upFile);
    }
    @CrossOrigin(origins ="http://127.0.0.1:5501")
    @GetMapping("/viewAllFiles")
    public ResponseEntity<List> viewAllFIles() {
        List<String> upFile = iFileStorageService.allFileNames();
        

        return ResponseEntity.status(HttpStatus.OK)         .body(upFile);
    }

}
