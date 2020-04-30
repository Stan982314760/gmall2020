package com.atguigu.gmall.manage.util;

import com.atguigu.gmall.consts.GmallConst;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class UploadUtil {

    public String uploadImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            log.error("文件为空");
        }

        log.debug("开始上传");

        // 文件后缀名
        String multipartFileName = multipartFile.getOriginalFilename();
        String extFileName = multipartFileName.substring(multipartFileName.lastIndexOf(".") + 1);

        // 最终返回url地址
        String imageUrl = "";

        try {
            // 加载tracker配置文件
            String confPath = "C:\\Users\\Zelda\\Desktop\\tracker.conf";
            ClientGlobal.init(confPath);

            // 获取trackerClient trackerServer
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();

            // 获取storagedClient
            StorageClient storageClient = new StorageClient(trackerServer, null);

            // 上传文件
            String[] infos = storageClient.upload_file(multipartFile.getBytes(), extFileName, null);

            imageUrl = GmallConst.FDFS_ADDRESS + "/" + infos[0] + "/" + infos[1];

        } catch (Exception e) {
            log.debug("上传出错 {}", e.getMessage());
            e.printStackTrace();
        }

        return imageUrl;
    }


}
