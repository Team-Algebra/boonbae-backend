<<<<<<< HEAD
package com.agebra.boonbaebackend.controller;


import com.agebra.boonbaebackend.dto.ImageNameDTO;
import com.agebra.boonbaebackend.service.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "S3Controller", description = "S3 관련 컨트롤러입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned")
    public ResponseEntity getS3PresignedKey(@RequestBody ImageNameDTO imageNameDTO) {
        log.info("@@@asdf");
        String preSignedUrl = s3Service.getPreSignedUrl(imageNameDTO.getImage_name());

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }

}
=======
package com.agebra.boonbaebackend.controller;


import com.agebra.boonbaebackend.dto.ImageNameDTO;
import com.agebra.boonbaebackend.service.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "S3Controller", description = "S3 관련 컨트롤러입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned")
    public ResponseEntity getS3PresignedKey(@RequestBody ImageNameDTO imageNameDTO) {
        log.info("@@@asdf");
        String preSignedUrl = s3Service.getPreSignedUrl(imageNameDTO.getImage_name());

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }

}
>>>>>>> 101b6baff5a63ac14df29ed0701f64b145b762bd
