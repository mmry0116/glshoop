package com.mmry.glshoop.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GlshoopProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Resource
    OSSClient ossClient;

    @Test
    public void testSave(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("牛牛牌2");
        brandService.save(brandEntity);
        System.out.println("success...");
    }


    @Test
    public void testOSS() throws FileNotFoundException {
        //上传给阿里的端点
        String endpoint ="oss-cn-beijing.aliyuncs.com";
        String accessKey ="LTAI5tFchf3nCZWskeU4gm2M";
        String accessKeySecret ="uW7xIp11Ui2tlOb5fm8hIXQtQ7Pb3P";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        FileInputStream is = new FileInputStream("M:\\pic\\令牌.png");

        //第一个参数：存储空间的名字 第二个参数：文件的名字
        ossClient.putObject("glshoop","1.jpg",is);
        System.out.println("上场完成....");
    }

    @Test
    public void testSpringClousOSS() throws FileNotFoundException {
        FileInputStream is = new FileInputStream("M:\\pic\\dark_锤子.png");

        //第一个参数：存储空间的名字 第二个参数：文件的名字
        ossClient.putObject("glshoop","2.jpg",is);
        System.out.println("上场完成....");
    }
}
