import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

class MainClass {

    public static FTPClient ftpClient = new FTPClient();
    public static String server = "ftp.mozilla.org";
    public static String downloadPath = "D:\\";
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static BufferedReader rr = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {
            if (connection()) {
                System.out.println("Connected");
                printHelp();
                printListObject(getListObject(ftpClient, ""));
                System.out.println("Enter the name of file which you want to download or the name of folder which you want to open:");
                selectEvent((getListObject(ftpClient, "")), "", br.readLine());
            }
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean connection() {

        try {
            ftpClient.connect(server);
            ftpClient.setControlKeepAliveTimeout(300);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login("anonymous", "anonymous");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient.isConnected();
    }

    public static String selectEvent(FTPFile[] files, String path, String expected_filename) throws IOException {
        while (!expected_filename.equals("q")) {
            if (!expected_filename.equals("r")) {
                for (FTPFile file : files) {
                    if (file.getName().toLowerCase().trim().equals(expected_filename.toLowerCase().trim())) {
                        if (file.isFile()) {
                            downloadFile(file);
                        } else {
                            path += "//" + file.getName();
                            printListObject(getListObject(ftpClient, path));
                            System.out.println("Enter the name of file which you want to download or the name of folder which you want to open:");
                            expected_filename = br.readLine();
                            selectEvent(getListObject(ftpClient, path), path, expected_filename);

                        }
                    }}

            } else {
                printListObject(getListObject(ftpClient, ""));
                System.out.println("Enter the name of file which you want to download or the name of folder which you want to open:");
                selectEvent((getListObject(ftpClient, "")), "", br.readLine());
            }
        }
        System.exit(0);
        return expected_filename;
    }

    public static void downloadFile(FTPFile file) throws IOException {
        OutputStream output = new FileOutputStream(downloadPath + "//" + file.getName());
        try {
            ftpClient.retrieveFile(file.getName(), output);
            System.out.println("You successfully downloaded " + file.getName() + "!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        output.close();
        rentResult();
    }

    public static void printListObject(FTPFile[] files) throws IOException {
        for (FTPFile file : files) {
            if (file.isDirectory()) {
                System.out.println(file.getName() + "(directory)");
                server += "//" + file.getName();
            } else {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        }
    }


    public static FTPFile[] getListObject(FTPClient ftpClient, String server) throws IOException {
        FTPFile[] listedFiles = ftpClient.listFiles(server);
        return listedFiles;
    }


    public static void rentResult() throws IOException {
        printHelp();
        System.out.println("What do you do?(download, exit, return)");
        String result_event = rr.readLine();
        String result_number = result_event;
        if (result_number.equals("q")) {
            System.out.println("End!");
            System.exit(0);
        } else if (result_event.equals("r")) {
            printListObject(getListObject(ftpClient, ""));
            System.out.println("Enter the name of file which you want to download or the name of folder which you want to open:");
            selectEvent((getListObject(ftpClient, "")), "", br.readLine());
        } else {
            System.out.println("You've entered is not correct letter.");
            rentResult();
        }
    }

    public static void printHelp() {
        System.out.println("If you want go to return to the root folder-> write 'r '");
        System.out.println("If you want exit-> write 'q'");

    }    }

