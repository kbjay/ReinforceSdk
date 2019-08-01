package com.reinforce.sdk.changemethod
/**
 * 方法实体
 *
 * @author kb_jay* @time 2019/7/29
 * */
class JavaMethod {
    String methodName;
    String methodBody;
    String returnType;
    String methodParams;

    @Override
    public String toString() {
        return "JavaMethod{" +
                "methodName='" + methodName + '\'' +
                ", methodBody='" + methodBody + '\'' +
                ", returnType='" + returnType + '\'' +
                ", methodParams='" + methodParams + '\'' +
                '}';
    }
}