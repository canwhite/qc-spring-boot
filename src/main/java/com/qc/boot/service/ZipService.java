package com.qc.boot.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ZipService {

    final Logger logger = LoggerFactory.getLogger(getClass());


    //上传操作
    public void upload(MultipartFile file) throws IllegalStateException, IOException{
        File dest = new File("/Users/zack/Desktop/path/to/save/" + file.getOriginalFilename());
        //transferTo用于将上传的文件写入指定的路径
        file.transferTo(dest);
    }


    //执行脚本
    public boolean execShell() throws InterruptedException, ExecutionException{
        Callable<Process> task = () -> {
            // 执行异步任务
            Runtime runtime = Runtime.getRuntime();
            //根据具体位置来更换
            Process process = runtime.exec("/Users/zack/Desktop/qc-runtime-callable/src/main/java/com/qc/runtime/shell.sh");
            return process;
        };
    
         // 将Callable包装成FutureTask
         FutureTask<Process> future = new FutureTask<>(task);

         // 启动新线程来执行异步任务
         new Thread(future).start();
 
         // 获取异步任务的结果
         Process result = future.get();
         if (result != null) {
            System.out.println("Command ran successfully!");
            return true;
        } else {
            System.out.println("Failed to run command.");
            return false;
        }
    }

    //修改nginx
    public void changeNginxConfig(){
        // Read the Nginx configuration file into memory
        File configFile = new File("/path/to/nginx.conf");
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find the line where you want to insert the new configuration
        int lineToInsertAfter = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("# My configuration block starts here")) {
                lineToInsertAfter = i;
                break;
            }
        }

        // Insert the new configuration after the specified line
        if (lineToInsertAfter >= 0) {
            lines.add(lineToInsertAfter + 1, "    new_configuration_option value;");
        }

        // Write the modified contents back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
