package com.reinforce.sdk.changemethod

/**
 * 将方法抽空
 *
 * @author kb_jay* @time 2019/7/29
 * */
class JavaChangeMethodUtils {
    static void changeMethod(String sourcePath, String destPath) {
        ArrayList<JavaMethod> javaMethodArray = JavaMethodParser.getJavaMethodArray(sourcePath);
        println javaMethodArray.toString()
        //getFileString
        String fileString = getFileString(sourcePath);
        //replace
        if (fileString != null) {
            for (int i = 0; i < javaMethodArray.size(); i++) {
                String returnType = javaMethodArray.get(i).returnType;
                String replaceStr = "{return null;}";
                if ("byte".equals(returnType) || "char".equals(returnType) || "int".equals(returnType)
                        || "float".equals(returnType) || "double".equals(returnType) || "long".equals(returnType)
                        || "boolean".equals(returnType) || "short".equals(returnType)) {
                    replaceStr = "{return 0;}";
                } else if ("void".equals(returnType)) {
                    replaceStr = "{}";
                }

                String methodParams = javaMethodArray.get(i).methodParams;
                methodParams = methodParams.replaceAll(",", ", ");
                String methodName = javaMethodArray.get(i).methodName;

                String methodId = methodName + "(" + methodParams + ")";
                int index = fileString.indexOf(methodId);

                if (index == -1) {
                    //not found ,need dev fixed source!!
                    throw new RuntimeException("please check method->" + methodName + " in " + sourcePath + ":right format is " + methodId);
                }

                String body = getMethodBody(fileString.substring(index + methodId.length()));
                fileString = fileString.replace(body, replaceStr);
            }
        }

        //writeString 2 file
        writeString2File(fileString, destPath);
    }

    private static String getMethodBody(String substring) {
        int len = substring.length();
        Stack<Integer> stack = new Stack<>();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (substring.charAt(i) == '{') {
                stack.push(i);
                start = i;
                break
            }
        }
        int end = 0;
        for (int i = start + 1; i < len; i++) {
            if (substring.charAt(i) == '{') {
                stack.push(i)
            } else if (substring.charAt(i) == '}') {
                if (stack.size() == 1) {
                    end = i
                    break
                } else {
                    stack.pop()
                }
            }
        }

        return substring.substring(start, end + 1)
    }

    private static void writeString2File(String fileString, String destPath) {
        try {
            FileOutputStream fos = new FileOutputStream(destPath)
            byte[] bytes = fileString.getBytes()
            fos.write(bytes, 0, bytes.length)
            fos.close()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private static String getFileString(String path) {
        try {
            FileInputStream fis = new FileInputStream(path)
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
            byte[] buf = new byte[2048]
            int len = -1
            while ((len = fis.read(buf)) > 0) {
                bos.write(buf, 0, len)
            }
            fis.close()
            bos.close()
            return bos.toString()
        } catch (Exception e) {
            e.printStackTrace()
            return null
        }
    }
}
