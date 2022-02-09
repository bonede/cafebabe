package org.javaexplorer.utils;



import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class ResourcesUtils {
    private static int BUFFER_SIZE = 1024 * 4;


    public static String readResource(Class<?> clazz, String fileName) throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        InputStream in = clazz.getClassLoader().getResourceAsStream(fileName);
        if(in == null){
            throw new IOException("Resource file not found: " + fileName);
        }
        IOUtils.copy(in, out);
        String result = new String(((ByteArrayOutputStream) out).toByteArray());
        out.close();
        in.close();
        return result;
    }
}
