package web.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.model.entity.TestEntity;
import web.service.RestTestService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestTestController {

    private final RestTestService restTestService;

    // 1. 일반 REST
    @GetMapping("/rest") // [GET] http://localhost:8080/api/rest
    public String test1(){
        return "Hello Rest";
    }

    // 2. DB ( RDS ) REST
    @PostMapping("/rds") // [POST] http://localhost:8080/api/rds?content=클라우드테스트
    public List<TestEntity> test2(@RequestParam String content){
        return restTestService.test2( content );
    }

    // 3. REDIS( 캐싱 ) REST
    @PostMapping("/cache") // [POST] http://localhost:8080/api/cache?content=캐싱
    public List<String> test3( @RequestParam String content ){
        return restTestService.test3( content );
    }

    // 4. S3 (파일 업로드/삭제 ) REST
    @PostMapping("/s3")     // [POST] http://localhost:8080/api/s3 , multipart/form-data , file
    public String test4(  MultipartFile file ){
        return restTestService.test4( file );
    }
}
