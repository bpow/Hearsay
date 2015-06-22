package org.renci.hearsay.dao.jpa;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

public class DownloadVertebrateMammalianFilesRunnable implements Runnable {

    public DownloadVertebrateMammalianFilesRunnable() {
        super();
    }

    @Override
    public void run() {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("ftp.ncbi.nlm.nih.gov");

            ftpClient.login("anonymous", "anonymous");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return;
            }

            List<FTPFile> ftpFileList = Arrays.asList(ftpClient.listFiles("/refseq/release/vertebrate_mammalian/",
                    new FTPFileFilter() {

                        @Override
                        public boolean accept(FTPFile ftpFile) {
                            if (ftpFile != null && ftpFile.getName().endsWith("rna.gbff.gz")) {
                                return true;
                            }
                            return false;
                        }
                    }));

            for (FTPFile ftpFile : ftpFileList) {
                File tmpFile = new File(System.getProperty("java.io.tmpdir", "/tmp"), ftpFile.getName());
                try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(tmpFile))) {
                    ftpClient.retrieveFile(String.format("/refseq/release/vertebrate_mammalian/%s", ftpFile.getName()),
                            fos);
                    fos.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        DownloadVertebrateMammalianFilesRunnable runnable = new DownloadVertebrateMammalianFilesRunnable();
        runnable.run();
    }
}