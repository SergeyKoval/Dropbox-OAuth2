package com.exadel;

import com.dropbox.core.*;
import java.io.*;
import java.util.List;
import java.util.Locale;

public class Main {

    public static final String APP_KEY = "derbtkmhivsl3wb";
    public static final String APP_SECRET = "7skyaw7ax0rpuck";
    public static final String ACCESS_TOKEN = "JQb2jaNFOd0AAAAAAAAABTsD96xANeJXUr-jIijKzBpq6BRLuT05GnpUDIJuuw0O";

    public static void main(String[] args) throws IOException, DbxException {
        /* Dropbox API initialization */
        //DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("dbox-adapter",
                Locale.getDefault().toString());
        // DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        /* end of section */

        /* App authorization */
/*        String authorizeUrl = webAuth.start();
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        // This will fail if the user enters an invalid authorization code.
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.accessToken;*/
        /* end of section */

        DbxClient client = new DbxClient(config, ACCESS_TOKEN);

        /* Retrieving account information */
        System.out.println("Account information: " );
        DbxAccountInfo accountInfo = getAccountInfo(client);
        System.out.println("Country: " + accountInfo.country);
        System.out.println("Name: " + accountInfo.displayName);
        /* end of section */

        /* Creating a folder */
        addFolder(client, "/ForTest");
        /* end of section */

        /* Downloading a file */
        System.out.println("\nDownloading a file...");
        downloadFile(client, "/Documents/test.txt", "test.txt");
        /* end of section */

        /* Uploading a file */
        System.out.println("\nUploading a file...");
        uploadFile(client, "test.txt", "/ForTest/test.txt");
        /* end of section */

        /* Retrieving folder metadata */
        System.out.println("\nRetrieving folder metadata...");
        List<DbxEntry> folderContents = getFolderMetadata(client, "/ForTest");

        for (DbxEntry entry : folderContents) {
            System.out.println("Element type: " + (entry.isFile() ? "file" : "folder"));
            System.out.println("Element name: " + entry.name);
        }
        /* end of section */

        /* Remove a file */
        System.out.println("\nRemoving a file...");
        deleteElement(client, "/ForTest/test.txt");
        /* end of section */

        /* Remove a folder */
        System.out.println("\nRemoving a folder");
        deleteElement(client, "/ForTest");
        /* end of section */
    }

    public static DbxEntry.File uploadFile(DbxClient client, String sourcePath, String targetPath) {
        File inputFile = new File(sourcePath);
        FileInputStream inputStream = null;
        DbxEntry.File uploadedFile = null;

        try {
            inputStream = new FileInputStream(inputFile);
            uploadedFile = client.uploadFile(targetPath, DbxWriteMode.add(), inputFile.length(), inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return uploadedFile;
    }

    public static List<DbxEntry> getFolderMetadata(DbxClient client, String folderPath) {
        List<DbxEntry> result = null;

        try {
            result = client.getMetadataWithChildren(folderPath).children;
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static DbxAccountInfo getAccountInfo(DbxClient client) {
        DbxAccountInfo accountInfo = null;

        try {
            accountInfo = client.getAccountInfo();
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return accountInfo;
    }

    public static DbxEntry.File downloadFile(DbxClient client, String sourcePath, String targetPath) {
        FileOutputStream outputStream = null;
        DbxEntry.File downloadedFile = null;

        try {
            outputStream = new FileOutputStream(targetPath);
            downloadedFile = client.getFile(sourcePath, null, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return downloadedFile;
    }

    public static void deleteElement(DbxClient client, String elementPath) {
        try {
            client.delete(elementPath);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    public static void addFolder(DbxClient client, String folderPath) {
        try {
            client.createFolder(folderPath);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
}