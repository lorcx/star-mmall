package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * ftp服务工具类
 * @Author lx
 * @Date 2017/11/19 16:48
 */
public class FtpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);

    private static final String FTP_IP = PropertiesUtil.getProperty("ftp.server.ip");
    private static final String FTP_USERNAME = PropertiesUtil.getProperty("ftp.user");
    private static final String FTP_PASSWORD = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;

    private FTPClient ftpClient;

    public FtpUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 文件上传
     * @param fileList
     * @return
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil = new FtpUtil(FTP_IP, 21, FTP_USERNAME, FTP_USERNAME);
        LOGGER.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        LOGGER.info("结束上传，上传结果{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uplaoded = true;
        FileInputStream fis = null;
        // 连接服务器
        if (connectServer(ip, port, user, pwd)) {
            try {
                //转移到ftpfile/img路径下
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                // 设置为二进制文件，防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 使用被动模式
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                LOGGER.error("上传文件异常", e);
                uplaoded = false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uplaoded;
    }

    /**
     * 连接ftp服务
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            LOGGER.error("连接ftp服务器失败", e);
        }
        return isSuccess;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
