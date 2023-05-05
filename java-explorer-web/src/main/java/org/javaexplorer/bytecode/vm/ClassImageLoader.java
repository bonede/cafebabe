package org.javaexplorer.bytecode.vm;



import org.apache.commons.io.IOUtils;
import org.javaexplorer.bytecode.classimage.ClassImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassImageLoader {
    private String userClassPath;
    private String javaHome;

    public ClassImageLoader(String userClassPath, String javaHome) {
        this.userClassPath = userClassPath;
        this.javaHome = javaHome;
    }

    public ClassImage loadClassImageByName(String className) throws IOException {
        String classFileName = className + ".class";
        File rtJarFile = getRtJarFile();
        InputStream inputStream = loadFromJarFile(rtJarFile, classFileName);
        if(inputStream == null){
            String[] paths = userClassPath.split(";");
            for(String p : paths){
                if(inputStream != null){
                    break;
                }
                Path path = Paths.get(p);
                File file = path.toFile();
                if(file.isDirectory()){
                    inputStream = loadFromJarFile(path.resolve(classFileName).toFile(), classFileName);
                }else {
                    inputStream = loadFromJarFile(file, classFileName);
                }
            }
        }
        if(inputStream != null){
            return new ClassImage(IOUtils.toByteArray(inputStream));
        }
        return null;
    }

    private InputStream loadFromJarFile(File jarFile, String classFileName) throws IOException {
        JarFile jar = new JarFile(jarFile);
        JarEntry entry = jar.getJarEntry(classFileName);
        if(entry == null){
            return null;
        }
        return jar.getInputStream(entry);
    }

    private File getRtJarFile(){
        return Paths.get(javaHome, "lib", "rt.jar").toFile();
    }
}
