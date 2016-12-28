package com.galive.logic.helper;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {

    public static List<Class<?>> getClasssFromPackage(String pack) {
        List<Class<?>> clazzs = new ArrayList<Class<?>>();
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                    findClassInPackageByFile(packageName, filePath, true, clazzs);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile  
                    System.err.println("jar类型的扫描");
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    // 从此jar包 得到一个枚举类  
                    Enumeration<JarEntry> entries = jar.entries();
                    // 同样的进行循环迭代  
                    while (entries.hasMoreElements()) {
                        // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件  
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        // 如果是以/开头的  
                        if (name.charAt(0) == '/') {
                            // 获取后面的字符串  
                            name = name.substring(1);
                        }
                        // 如果前半部分和定义的包名相同  
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            // 如果以"/"结尾 是一个包  
                            if (idx != -1) {
                                // 获取包名 把"/"替换成"."  
                                packageName = name.substring(0, idx).replace('/', '.');
                            }
                            // 如果可以迭代下去 并且是一个包  
                            // 如果是一个.class文件 而且不是目录
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                // 去掉后面的".class" 获取真正的类名
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                try {
                                    // 添加到classes
                                    clazzs.add(Class.forName(packageName + '.' + className));
                                } catch (ClassNotFoundException e) {
                                    // log
                                    // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazzs;
    }

    private static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
                                                 List<Class<?>> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> {
            boolean acceptDir = recursive && file.isDirectory();
            boolean acceptClass = file.getName().endsWith("class");
            return acceptDir || acceptClass;
        });

        if (dirFiles != null) {
            for (File file : dirFiles) {
                if (file.isDirectory()) {
                    findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
