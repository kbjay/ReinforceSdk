#### 思路
    1.生成两个jar（一个不包含方法实体的jar A 跟一个真正的jar B）
    2.A为了提供给使用者引用，B需要进行加密之后放入assets目录下 
    3.运行的时候动态加载B 

##### Q：如何生成A？
    利用sun下的api
    参考：http://ragnraok.github.io/using-jdk-parser.html
##### Q：如何动态加载B？
    参考tinker原理
    https://juejin.im/post/5b640deef265da0f86544bb1

##### 整体的方案？
    1.自定义grale插件(两个task:生成空壳java文件；将真实的jar文件放入assets目录)
    2.sdk定义两个flavor -> A 跟 B（一个对应真实源代码，一个对应空壳源代码）
    3.空壳源代码是通过gradle插件自动生成的
    4.build这两个flavor生成aarA跟aarB
    5.利用gradle插件将aarB中的jarB放入aarA的asset下（暂时先不加密）
    6.在app运行时使用dexClassLoader加载jarB（application->attachBaseContext）
    7.从dexClassLoader中取出jarB的Element C
    8.反射获取PathClassLoader中的ElmentList，将C放入该list的前面

##### 使用步骤：
    1.sdk新建两个flavor，一个是内部使用的，一个是提供给外部使用的
    2.sdk依赖reinfoce插件，填写配置参数changeMethod跟mergeAar
    3.sdk执行changeMethod
    4.sdk执行assemble
    5.sdk执行mergeAar生成提供给外部使用的aar  ->A
    6.shell执行assemble生成aar   ->B
    7.提供A跟B给外部使用。
    8.接入sdk的app需要在applicaiton的attachBaseContext方法中执行SdkShell.init方法解壳;
   
   具体可以参考demo                                                    
    
    



