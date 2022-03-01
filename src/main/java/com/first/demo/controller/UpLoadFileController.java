package com.first.demo.controller;

import com.first.demo.models.ResponseObject;
import com.first.demo.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path = "/api/vd/upload")
public class UpLoadFileController {
    @Autowired
    private IStorageService iStorageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> upLoadFile(@RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = iStorageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "upload file successfilly", generatedFileName)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Ok", e.getMessage(), "")
            );
        }
    }

}
