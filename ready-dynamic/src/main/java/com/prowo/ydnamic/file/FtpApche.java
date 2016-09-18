package com.prowo.ydnamic.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpApche {
    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");

    /**
     * Description: 向FTP服务器上传文件
     * 
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param path
     *            FTP服务器保存目录,如果是根目录则为“/”
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            本地文件输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, int port, String username, String password, String path,
            String filename, InputStream input) {
        boolean result = false;

        try {
            int reply;
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.connect(url);
            // ftp.connect(url, port);// 连接FTP服务器
            // 登录
            ftpClient.login(username, password);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                LoggerUtil.log(Level.ERROR, "连接失败");
                ftpClient.disconnect();
                return result;
            }

            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding), "iso-8859-1"), input);
                if (result) {
                    LoggerUtil.log(Level.ERROR, "上传成功!");
                }
            }
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
            LoggerUtil.log(Level.ERROR, "uploadFile error");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 将本地文件上传到FTP服务器上
     * 
     */
    public void testUpLoadFromDisk() {
        try {
            FileInputStream in = new FileInputStream(new File("E:/号码.txt"));
            boolean flag = uploadFile("127.0.0.1", 21, "zlb", "123", "/", "哈哈.txt", in);
            System.out.println(flag);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 从FTP服务器下载文件
     * 
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param remotePath
     *            FTP服务器上的相对路径
     * @param fileName
     *            要下载的文件名
     * @param localPath
     *            下载后保存到本地的路径
     * @return false 未修改，不用下载 true 为下载，或者下载过了，但是需要更新
     */
    public static boolean downFile(String url, int port, String username, String password, String remotePath,
            String fileName, String localPath, FtpDate ftpDate) {
        boolean result = false;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);

            /*
             * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
             * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
             */
            // FTPClientConfig conf = new
            // FTPClientConfig(FTPClientConfig.SYST_NT);
            // conf.setServerLanguageCode("zh");

            ftpClient.connect(url, port);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(username, password);// 登录
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                LoggerUtil.log(Level.ERROR, "FTP server refused connection.");
                return result;
            }
            // 转移到FTP服务器目录至指定的目录下
//            ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding), "iso-8859-1"));
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles(new String((remotePath + fileName).getBytes(encoding), "iso-8859-1"));
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)
                        && (ftpDate.getLastModifiedDate() == null || ff.getTimestamp().getTime()
                                .after(ftpDate.getLastModifiedDate()))) {
                    File localFile = new File(localPath + File.separator + ff.getName());
                    if (!localFile.exists()) {
                        localFile.getParentFile().mkdirs();
                    }
                    OutputStream is = new FileOutputStream(localPath + File.separator + ff.getName());
                    ftpClient.retrieveFile(ff.getName(), is);
                    is.close();
                    result = true;
                    ftpDate.setLastModifiedDate(ff.getTimestamp().getTime());
                }
            }
            if (!result) {
                LoggerUtil.log(LoggerUtil.Level.DEBUG, "no file or not modified");
            }

            ftpClient.logout();
        } catch (IOException e) {
            LoggerUtil.log(Level.WRAN, e, "downFile error");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    LoggerUtil.log(Level.WRAN, ioe, "disconnect error");
                }
            }
        }
        return result;
    }

    /**
     * 将FTP服务器上文件下载到本地
     * 
     */
    public void testDownFile() {
        try {
            boolean flag = downFile("113.10.188.208", 21, "vh575172", "6854z386", "/www/config/", "config_global.php",
                    "D:/", null);
            System.out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FtpApche fa = new FtpApche();
        fa.testDownFile();
    }

    /**
     * 将FTP服务器上文件下载到本地
     * 
     */
    public static boolean downFile(String fileName, FtpDate ftpDate, FtpDownInfo ftpDownInfo) {
        try {
            boolean flag = FtpApche.downFile(ftpDownInfo.getIp(), ftpDownInfo.getPort(), ftpDownInfo.getUsername(),
                    ftpDownInfo.getPassword(), ftpDownInfo.getRemotePath(), fileName, ftpDownInfo.getLocalPath(),
                    ftpDate);
            return flag;
        } catch (Exception e) {
            LoggerUtil.log(Level.WRAN, e, "download file error");
        }
        return false;
    }

    /**
     * 封装最后更新时间，作为引用传值
     * 
     */
    public static class FtpDate {
        private Date lastModifiedDate;

        public void setLastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public Date getLastModifiedDate() {
            return lastModifiedDate;
        }

    }

    /**
     * 下载ftp服务器制定文件到本地路径
     * 
     */
    public static class FtpDownInfo {
        private String ip;
        private int port;
        private String username;
        private String password;
        private String remotePath;
        private String localPath;

        public FtpDownInfo(String ip, int port, String username, String password, String remotePath, String localPath) {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.password = password;
            this.remotePath = remotePath;
            this.localPath = localPath;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRemotePath() {
            return remotePath;
        }

        public void setRemotePath(String remotePath) {
            this.remotePath = remotePath;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }
    }
}
