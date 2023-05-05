package app.cafebabe.utils;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public abstract class ResourcesUtils {

    public static InputStream readResource(Class<?> clazz, String fileName) throws IOException{
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(fileName);
        if(inputStream == null){
            throw new IOException("Resource file not found: " + fileName);
        }
        return inputStream;
    }

    public static byte[] readResourceToBytes(Class<?> clazz, String fileName) throws IOException{
        return IOUtils.toByteArray(readResource(clazz, fileName));
    }

    public static String readResourceToString(Class<?> clazz, String fileName) throws IOException {
        InputStream inputStream = readResource(clazz, fileName);
        String result = IOUtils.toString(inputStream, "utf-8");
        inputStream.close();
        return result;
    }
}
