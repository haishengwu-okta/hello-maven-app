package com.mycompany.app;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET
                .build();

        String packageUrl = "https://github.com/okta/samples-js-react/archive/master.zip";
        try {
            URL url = new URL(packageUrl);
            HttpGet get = new HttpGet(url.toURI());
            HttpResponse response = null;
            response = httpClient.execute(get);
            InputStream source = response.getEntity().getContent();

            ZipInputStream zipInputStream = new ZipInputStream(source);

            OutputStream tmp = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(tmp);

            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                System.out.println(entry);
                zipOutputStream.putNextEntry(entry);
                if (!entry.isDirectory()) {
                    //TODO:
                }
                zipOutputStream.closeEntry();

                entry = zipInputStream.getNextEntry();
            }

            ZipEntry e = new ZipEntry("answer.txt");
            zipOutputStream.putNextEntry(e);
            zipOutputStream.write("client_id=abc\nclient_sercet=abc".getBytes());
            zipOutputStream.closeEntry();

            // close
            zipInputStream.close();
            zipOutputStream.close();

            System.out.println("==============");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
