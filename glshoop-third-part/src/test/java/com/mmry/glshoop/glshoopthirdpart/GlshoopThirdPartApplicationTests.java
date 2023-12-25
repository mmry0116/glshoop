package com.mmry.glshoop.glshoopthirdpart;

import com.aliyun.oss.OSSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GlshoopThirdPartApplicationTests {
    @Resource
    OSSClient ossClient;

    @Test
    public void contextLoads() throws FileNotFoundException {
        FileInputStream is = new FileInputStream("M:\\pic\\dark_锤子.png");
        //第一个参数：存储空间的名字 第二个参数：文件的名字
        ossClient.putObject("glshoop", "222.jpg", is);
        System.out.println("上场完成....");
    }

}

