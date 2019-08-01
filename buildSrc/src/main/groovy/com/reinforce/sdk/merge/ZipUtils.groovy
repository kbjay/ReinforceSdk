package com.reinforce.sdk.merge

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 *
 * @anthor kb_jay* create at 2019/7/9 下午5:29
 */
class ZipUtils {
    /**
     * 解压
     * @param file
     * @param dir
     */
    static void unzip(File file, File dir) {
        def sourceApk = new ZipFile(file)
        sourceApk.entries().each {
            ZipEntry entry ->
                def itemFile = new File(dir, entry.getName())
                if (!entry.isDirectory()) {
                    def parent = itemFile.getParentFile()
                    parent.mkdirs()
                    def is = sourceApk.getInputStream(entry)
                    def fos = new FileOutputStream(itemFile)
                    byte[] b = new byte[2048]
                    int len = -1
                    while ((len = is.read(b)) > 0) {
                        fos.write(b, 0, len)
                    }

                    is.close()
                    fos.close()
                }
        }
    }
    /**
     * <p>
     * 压缩文件
     * </p>
     *
     * @param sourceFolder 压缩文件夹
     * @param zipFilePath 压缩文件输出路径
     * @throws Exception
     */
    static void zip(String sourceFolder, String zipFilePath) {
        OutputStream out = new FileOutputStream(zipFilePath)
        BufferedOutputStream bos = new BufferedOutputStream(out)
        ZipOutputStream zos = new ZipOutputStream(bos)
        File file = new File(sourceFolder)
        String basePath = null
        if (file.isDirectory()) {
            basePath = file.getPath()
        } else {
            basePath = file.getParent()
        }
        zipFile(file, basePath, zos)
        zos.closeEntry()
        zos.close()
        bos.close()
        out.close()
    }

    /**
     * <p>
     * 递归压缩文件
     * </p>
     *
     * @param parentFile
     * @param basePath
     * @param zos
     * @throws Exception
     */
    private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) {
        File[] files = new File[0]
        if (parentFile.isDirectory()) {
            files = parentFile.listFiles()
        } else {
            files = new File[1]
            files[0] = parentFile
        }
        String pathName
        InputStream is
        BufferedInputStream bis
        byte[] cache = new byte[2048]
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + "/"
                zos.putNextEntry(new ZipEntry(pathName))
                zipFile(file, basePath, zos)
            } else {
                pathName = file.getPath().substring(basePath.length() + 1)
                is = new FileInputStream(file)
                bis = new BufferedInputStream(is)
                zos.putNextEntry(new ZipEntry(pathName))
                int nRead = 0
                while ((nRead = bis.read(cache, 0, 2048)) != -1) {
                    zos.write(cache, 0, nRead)
                }
                bis.close()
                is.close()
            }
        }
    }
}