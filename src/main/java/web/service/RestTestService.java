package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import web.model.entity.TestEntity;
import web.model.repository.TestEntityRepository;
import web.util.FileUtil;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RestTestService {

    private final TestEntityRepository testEntityRepository;
    private final FileUtil fileUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 2. DB ( RDS ) REST
    public List<TestEntity> test2( String content){
        testEntityRepository.save( TestEntity.builder().content( content ).build() );
        return testEntityRepository.findAll();
    }

    // 3. REDIS( 캐싱 ) REST
    public List<String> test3(  String content ){
        ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
        listOps.rightPush("contents", content);
        return listOps.range("contents", 0, -1);
    }

    // 4. S3 (파일 업로드 ) REST
    public String test4(  MultipartFile file ){
        return fileUtil.fileUpload( file );
    }

}
