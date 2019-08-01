package com.reinforce.sdk.changemethod

import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.MethodTree
import com.sun.source.util.JavacTask
import com.sun.source.util.TreeScanner
import com.sun.tools.javac.api.JavacTool
import com.sun.tools.javac.file.JavacFileManager
import com.sun.tools.javac.util.Context

import javax.tools.JavaCompiler
import javax.tools.JavaFileObject
import java.nio.charset.Charset


/**
 * 解析.java文件，获取方法array
 *
 * @author kb_jay* @time 2019/7/29
 * */
class JavaMethodParser {

    static ArrayList<JavaMethod> getJavaMethodArray(String javaFilePath) {
        println javaFilePath
        ArrayList<JavaMethod> javaMethods = new ArrayList<>()
        Context context = new Context()
        String className = ""
        JavacFileManager fileManager = new JavacFileManager(context, true, Charset.defaultCharset())
        JavacTool javacTool = new JavacTool()

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(javaFilePath)
        JavaCompiler.CompilationTask compilationTask = javacTool.getTask(null, fileManager, null, null, null, files)
        JavacTask javacTask = (JavacTask) compilationTask
        try {
            Iterable<? extends CompilationUnitTree> result = javacTask.parse()
            for (CompilationUnitTree tree : result) {
                tree.accept(new TreeScanner<Void, Void>() {

                    @Override
                    Void visitClass(ClassTree node, Void aVoid) {
                        className = node.getSimpleName().toString()
                        return super.visitClass(node, aVoid)
                    }

                    @Override
                    Void visitMethod(MethodTree node, Void aVoid) {
                        JavaMethod javaMethod = new JavaMethod()
                        javaMethod.methodBody = node.getBody().toString()
                        if (("<init>").equals(node.getName().toString())) {
                            javaMethod.methodName = className
                        } else {
                            javaMethod.methodName = node.getName().toString()
                        }
                        javaMethod.methodParams = node.getParameters().toString()
                        if (node.getReturnType() == null) {
                            javaMethod.returnType = "void"
                        } else {
                            javaMethod.returnType = node.getReturnType().toString()
                        }
                        javaMethods.add(javaMethod)

                        return null
                    }
                }, null)
            }
        } catch (IOException e) {
            e.printStackTrace()

        }
        return javaMethods
    }
}