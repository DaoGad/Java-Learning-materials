---
typora-root-url: images
---



## day28 反射

Java Reflection

Reflection（反射）视为动态语言的关键，反射机制允许程序在允许期间借助于Reflection API取到任何类的内部的信息，并能直接操作任意对象的内部属性以及方法

加载完类后，在堆内存的方法区中就产生了一个Class类型的对象 一个类只有一个Class对象，这个对象就包含了完整的类的结构信息，我们可以通过这个对象看到类的结构，这个对象就像一面镜子，透过这个镜子看到类的结构，所以我们称之为反射

正常方式：==》 引入需要的 “包类”名称 ==》通过new实例化==》取到实例化对象

反射方式 ==》getClass()方法 ==》得到完整的 ”包类“名称

Java反射机制提供的功能

> 在运行时判断任意一个对象所属的类
>
> 在运行时构造任意一个类的对象
>
> 在运行是判断任意一个类所具有的成员变量和方法
>
> 在运行时获取泛型信息
>
> 在运行时调用任意一个对象的的成变量和方法
>
> 在运行时处理注解
>
> 生成动态代理



## 反射相关主要API

> Java.lang.Class:代表一个类
>
> Java.lang.reflect.Method: 代表类的方法
>
> Java.lang.reflect.Field:代表类的成员变量
>
> Java.lang.reflect.Constructor:代表类的构造器
>
> ....

ReflectionTest.java

```java
package com.atguigu.java;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gcq
 * @Create 2020-09-26
 */
public class ReflectionTest {

    //反射之前
    @Test
    public void test1() {
        Person p1 = new Person("Tom", 23);

        p1.age=10;
        System.out.println(p1.toString());

        p1.show();
        // 在Person类外部，不可以通过Person类的对象调用其内部私有结构
        // 比如name.私有方法
    }

    @Test
    public void test2() throws Exception {
        Class classz  = Person.class;
        Constructor cons = classz.getConstructor(String.class, int.class);
        Object obj = cons.newInstance("Tom", 22);
        Person person = (Person) obj;
        System.out.println(person.toString());
        // 通过对象调用指定的属性、方法
        // 调用属性
        Field age = classz.getDeclaredField("age");
        age.set(person,10);
        System.out.println(person);

        // 调用方法
        Method show = classz.getDeclaredMethod("show");
        show.invoke(person);

        // 通过反射，可以调用Person类的私有结构， 比如私有的构造器
        // 调用私有构造方法
        Constructor cons1 = classz.getDeclaredConstructor(String.class);
        cons1.setAccessible(true);
        Person person1 = (Person) cons1.newInstance("Jerry");
        System.out.println(person1);

        // 调用私有属性
        Field name = classz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person1,"Hanmeimei");
        System.out.println(person1);

        // 调用私有方法
        Method showNation = classz.getDeclaredMethod("showNation", String.class);
        showNation.setAccessible(true);
        String nation = (String) showNation.invoke(person1, "中国");
        System.out.println(nation);

    }
    //  疑问1：通过直接new的方法或反射的方式都可以调用公共的结构，开发中用那个
    //建议 直接用new
    // 什么时候会使用反射的方式，反射的特征，动态性
    // 疑问2: 反射机制与面向对象的分装修是不是矛盾，如何看待连个技术
    //不矛盾
    /**
     * 关于Java.lang.Class类的理解
     * 1、类的加载过程：
     * 程序经过javac.exe命令后，会生成一个或多个字节码文件，.class结尾
     * 接着我们使用java.exe命令对某个字节码文件进行解释执行，相当于某个字节码文件
     * 加载到内存中，此过程就称为类的加载，加载到内存中的类，我们称为运行时类，此运行时类
     * 就作为Class的一个实例
     *
     * 2、换句话说 Class的实例对应着一个运行时类
     * 3、加载到内存中的类，会缓存一定时间，在此时间内，我们可以通过不同的方式来获取此运行时类
     */

    /**
     * 获取Class实例的方式 前三种需要掌握
     */
    @Test
    public void test3() throws ClassNotFoundException {
        // 方式一 调用运行时类的属性.class
        Class class1 = Person.class;
        System.out.println(class1);
        // 方式二、通过运行时类的对象
        Person person = new Person();
        Class  class2 = person.getClass();
        System.out.println(class2);

        // 方式三:调用Class静态方法,forName(String classPath)
        Class class3 = Class.forName("com.atguigu.java.Person");
//        Class class3 = Class.forName("java.lang.String");
        System.out.println(class3);
        System.out.println(class1 == class2);

        // 方式四：classLoader 了解
        ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        Class aClass = classLoader.loadClass("com.atguigu.java.Person");
        System.out.println(aClass);
    }

    @Test
    public void test4() {
        int[] a = new int[10];
        int[] b = new int[100];
        Class c10 = a.getClass();
        Class c11 = b.getClass();
        // 只要数组的元素和类型与维度一样，就是同一个class
        System.out.println(c10 == c11);
    }


}
```



## 类的加载与ClassLoader的理解

### 类的加载过程

当程序主动使用某个类时，如果该类还未被加载到内存中，则系统会通过如下三个步骤对该类进行初始化

 ![](Snipaste_2020-09-26_11-09-41.png)

![](/Snipaste_2020-09-26_11-15-54.png)

![](/Snipaste_2020-09-26_11-16-21.png)

类加载器的作用

类加载器的作用：将class文件字节码内容加载到内存中，并将这些静态数据转换为方法区的运行时数据结构，然后再堆中生成一个代表该类的 Java.lang.Class对象，作为方法区中类数据的访问入口

类缓存：标准的JavaSE 类加载器可以按要求查找类，但一旦某个类被加载到类加载器中，他将位置加载（缓存）一段时间，不过 JVM 垃圾回收机制可以收回这些class对象

了解 ClassLoader

类加载器作用是用来把类(class)装载进内存的，JVM规范如下类型类的加载器

![](/Snipaste_2020-09-26_11-22-02.png)





##### ClassLoaderTest.java

```java

/**
 * @author gcq
 * @Create 2020-09-26
 */
public class ClassLoaderTest {
    @Test
    public void test1() {
        // 自定义类，使用系统类加载器进行加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader);
        // 调用系统类加载器getParent() 无法获取引导类加载器
        // 引导类加载器主要负责加载Java的核心类库，无法加载自定义类加载器
        ClassLoader parent = classLoader.getParent();
        System.out.println(parent);

        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);
    }

    @Test
    public void test2() throws Exception {
        Properties pros = new Properties();
        //读取方式一
        FileInputStream fis = new FileInputStream("src\\jdbc.properties");
        pros.load(fis);
//        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
//        InputStream is = classLoader.getResourceAsStream("jdbc.properties");
//        pros.load(is);

        //读取方式二
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        System.out.println("user = " + user + ",password = " + password);
    }
}
```

##### NewInstance.java

```java

/**
 *
 * 通过反射创建对应的运行时类对象
 *
 * @author gcq
 * @Create 2020-09-26
 */
public class NewInstanceTest {

    @Test
    public void test1() throws IllegalAccessException, InstantiationException {
        Class<Person> clazz = Person.class;
        /**
         * newInstance() 调用此方法，创建对应运行时类的对象，内部调用了运行时类的空参的构造函数
         *  要想运行此方法正常的创建运行时类的对象，要求：
         *  1、运行时类必须提供空参的构造器
         *  2、空参的构造器的访问权限，通常，设置为public
         *
         *  在JavaBean中要求提供一个public空参构造器
         */
        Person person = clazz.newInstance();
        System.out.println(person);
    }

    /**
     * 体会反射动态性
     */
    @Test
    public void test2() {
        int num = new Random().nextInt(3); //0,1,2
        String classPath = "";
        switch (num) {
            case 0:
                classPath ="java.util.Date";
                break;
            case 1:
                classPath = "java.sql.Date";
                break;
            case 2:
                classPath = "com.atguigu.java.Person";
                break;
        }
        try {
            Object instance = getInstance(classPath);
            System.out.println(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param classPath
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object getInstance(String classPath) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(classPath);
        return clazz.newInstance();
    }
}
```

##### ReflectionTest.java

```java

/**
 * @author gcq
 * @Create 2020-09-26
 */
public class ReflectionTest {

    //反射之前
    @Test
    public void test1() {
        Person p1 = new Person("Tom", 23);

        p1.age=10;
        System.out.println(p1.toString());

        p1.show();
        // 在Person类外部，不可以通过Person类的对象调用其内部私有结构
        // 比如name.私有方法
    }

    @Test
    public void test2() throws Exception {
        Class classz  = Person.class;
        Constructor cons = classz.getConstructor(String.class, int.class);
        Object obj = cons.newInstance("Tom", 22);
        Person person = (Person) obj;
        System.out.println(person.toString());
        // 通过对象调用指定的属性、方法
        // 调用属性
        Field age = classz.getDeclaredField("age");
        age.set(person,10);
        System.out.println(person);

        // 调用方法
        Method show = classz.getDeclaredMethod("show");
        show.invoke(person);

        // 通过反射，可以调用Person类的私有结构， 比如私有的构造器
        // 调用私有构造方法
        Constructor cons1 = classz.getDeclaredConstructor(String.class);
        cons1.setAccessible(true);
        Person person1 = (Person) cons1.newInstance("Jerry");
        System.out.println(person1);

        // 调用私有属性
        Field name = classz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person1,"Hanmeimei");
        System.out.println(person1);

        // 调用私有方法
        Method showNation = classz.getDeclaredMethod("showNation", String.class);
        showNation.setAccessible(true);
        String nation = (String) showNation.invoke(person1, "中国");
        System.out.println(nation);

    }
    //  疑问1：通过直接new的方法或反射的方式都可以调用公共的结构，开发中用那个
    //建议 直接用new
    // 什么时候会使用反射的方式，反射的特征，动态性
    // 疑问2: 反射机制与面向对象的分装修是不是矛盾，如何看待连个技术
    //不矛盾
    /**
     * 关于Java.lang.Class类的理解
     * 1、类的加载过程：
     * 程序经过javac.exe命令后，会生成一个或多个字节码文件，.class结尾
     * 接着我们使用java.exe命令对某个字节码文件进行解释执行，相当于某个字节码文件
     * 加载到内存中，此过程就称为类的加载，加载到内存中的类，我们称为运行时类，此运行时类
     * 就作为Class的一个实例
     *
     * 2、换句话说 Class的实例对应着一个运行时类
     * 3、加载到内存中的类，会缓存一定时间，在此时间内，我们可以通过不同的方式来获取此运行时类
     */

    /**
     * 获取Class实例的方式 前三种需要掌握
     */
    @Test
    public void test3() throws ClassNotFoundException {
        // 方式一 调用运行时类的属性.class
        Class class1 = Person.class;
        System.out.println(class1);
        // 方式二、通过运行时类的对象
        Person person = new Person();
        Class  class2 = person.getClass();
        System.out.println(class2);

        // 方式三:调用Class静态方法,forName(String classPath)
        Class class3 = Class.forName("com.atguigu.java.Person");
//        Class class3 = Class.forName("java.lang.String");
        System.out.println(class3);
        System.out.println(class1 == class2);

        // 方式四：classLoader 了解
        ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        Class aClass = classLoader.loadClass("com.atguigu.java.Person");
        System.out.println(aClass);
    }

    @Test
    public void test4() {
        int[] a = new int[10];
        int[] b = new int[100];
        Class c10 = a.getClass();
        Class c11 = b.getClass();
        // 只要数组的元素和类型与维度一样，就是同一个class
        System.out.println(c10 == c11);
    }
}
```

获取运行时类中的对应 方法、属性、构造、父类

准备条件

##### Person.java

```java
/**
 * @author gcq
 * @Create 2020-09-26
 */
@MyAnnotation(value="hi")
@ToString
public class Person extends Creature<String> implements Comparable<String>,MyInterface {

    private String name;
    int age;
    public int id;

    public Person() {

    }
    @MyAnnotation(value="abc")
    public Person(String name) {
        this.name = name;
    }
    Person(String name,int age) throws Exception {
        this.name = name;
        this.age = age;
    }

    @Override
    public void info() {
        System.out.println("我是一个人");
    }
    @MyAnnotation()
    private String show(String nation) {
        System.out.println("我的国籍是:" + nation);
        return nation;
    }
    public String display(String interests) {
        return interests;
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }


    public static void showDesc() {
        System.out.println("我是一个可爱的人");
    }
}
```

##### MyInstace.java 接口

```java
/**
 * @author gcq
 * @Create 2020-09-26
 */
public interface MyInterface {
    void info();
}

```

##### MyAnnotation.java 注解

```java
/**
 * Created by GuoChengQian on 2020/9/26 13:15
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value() default "hello";
}
```

##### Creature.java 父类

```java
/**
 * @author gcq
 * @Create 2020-09-26
 */
public class Creature<T> implements Serializable {

    private char gender;
    public double weight;

    private void breath() {
        System.out.println("生物呼吸");
    }
    public void eat() {
        System.out.println("吃饭了");
    }
}
```



##### FieldTest.Java 获取当前运行时类的属性结构

```java

/**
 * 获取当前运行时类的属性结构
 *
 * @author gcq
 * @Create 2020-09-26
 */
public class FieldTest {

    @Test
    public void test1() {
        Class clazz = Person.class;

        // 获取属性结构
        // getFields() 获取当时运行时类及其父类中声明为public访问权限的属性
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            System.out.println(f);
        }

        // getDeclaredFields 获取当前运行时类中声明的所有属性，（不包含父类中声明的属性）
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field fs : declaredFields) {
            System.out.println(fs);
        }
    }

    /**
     * 权限修饰符
     */
    @Test
    public void test2() {
        Class clazz = Person.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            // 1、权限修饰符
            int modifiers = f.getModifiers();
//            System.out.println(modifiers.toString(modifiers) +"\t");

            // 2、数据类型
            Class type = f.getType();
            System.out.println(type.getName() + "\t");

            // 3、变量名
            String name = f.getName();
            System.out.println(name);
        }
    }
}
```

MethodTest.java 运行时方法

```java
/**
 * 获取运行时类方法结构
 *
 * @author gcq
 * @Create 2020-09-26
 */
public class MethodTest {

    @Test
    public void test1() {
        Class clazz = Person.class;
        Method[] declaredMethods = clazz.getMethods();
        for (Method m : declaredMethods) {
            System.out.println(m);
        }
        System.out.println();

        // getDeclaredMethods 获取当前运行时类声明的所有方法，不包含父类中声明
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m);
        }
    }

    /**
     * @xxx
     * 权限修饰符，返回值类型，方法名(参数类型1，形参名1) throws XXXexception
     */
    @Test
    public void test2() {

        // 获取方法注解
        Class clazz = Person.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            Annotation[] annotations = m.getAnnotations();
            for (Annotation a : annotations) {
                System.out.print(a);
            }

            // 权限修饰符
            System.out.print(Modifier.toString(m.getModifiers()) + "\t");

            // 返回值类型
            System.out.print(m.getReturnType().getName() + "\t");

            // 方法名
            System.out.print(m.getName());
            System.out.print("(");

            //形参列表
            Class[] parameterTypes = m.getParameterTypes();
            if (!(parameterTypes == null && parameterTypes.length == 0)) {
               for(int i = 0; i < parameterTypes.length; i++) {
                   if (i == parameterTypes.length - 1) {
                       System.out.print(parameterTypes[i].getName() + "args_" + i);
                       break;
                   }
                   System.out.print(parameterTypes[i].getName() + "args_" + i + ",");
               }
            }
            System.out.print(")");

            // 抛出的异常
            Class[] exceptionTypes = m.getExceptionTypes();
            if (exceptionTypes.length > 0) {
                System.out.print("throws");
                for(int i = 0; i < exceptionTypes.length; i++) {
                    if (i == exceptionTypes.length-1) {
                        System.out.print(exceptionTypes[i].getName());
                    }
                    System.out.print(exceptionTypes[i].getName() + ",");
                }
            }
            System.out.println();
        }
    }
}
```

##### OtherTest.java

```java
/**
 * @author gcq
 * @Create 2020-09-26
 */
public class OtherTest {
    /**
     * 获取构造器结构
     */
    @Test
    public void test1() {
        Class clazz = Person.class;
        // getConstructors 获取当前运行时类中声明为public的构造器
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor c : constructors) {
            System.out.println(c);
        }
        // getDeclaredConstructors 获取当前运行时类中声明的所有构造器
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for(Constructor c : declaredConstructors) {
            System.out.println(c) ;
        }
    }


    /**
     * 获取运行时类的父类
     */
    @Test
    public void test2() {
        Class clazz = Person.class;
        Type superClass =  clazz.getGenericSuperclass();
        System.out.println(superClass);
    }

    /**
     * 获取运行时类的带泛型的父类的泛型
     */
    @Test
    public void test3() {
        Class clazz = Person.class;
        Type superClass =  clazz.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) superClass;
        // 获取泛型参数
        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        System.out.println(actualTypeArguments[0].getTypeName());
    }

    /**
     * 获取运行时类实现的接口
     */
    @Test
    public void test4() {
        Class clazz = Person.class;
        Class[] interfaces = clazz.getInterfaces();
        for (Class c : interfaces) {
            System.out.println(c);
        }
        System.out.println();
        // 获取运行时类的父类实现的接口
        Class[] interfaces1 = clazz.getSuperclass().getInterfaces();
        for (Class c : interfaces1) {
            System.out.println(c);
        }
    }

    /**
     * 获取运行时类所在的包
     */
    @Test
    public void test6() {
        Class clazz = Person.class;

        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation a : annotations) {
            System.out.println(a);
        }
    }
}
```

ReflectionTest.java

```java
/**
 * 调用运行时类指定的结构，属性，方法，构造器
 *
 *
 * @author gcq
 * @Create 2020-09-26
 */
public class ReflectionTest {

    /**
     * 
     */
    @Test
    public void testField() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        // 创建运行时类的对象
        Class clazz = Person.class;

        Person p  = (Person) clazz.newInstance();

        // 获取指定的属性 要求 运行类中为public
        Field id = clazz.getField("id");

        /**
         * 参数1 设置当前属性的值 参数2 将此属性值设置为多少
         */
        //设置当前属性的值
        id.set(p,1001);

        /**
         * 获取当前属性的值
         * get() 参数1 获取那个对象的当前属性值
         */
        id.get(p);

        System.out.println(p.toString());
    }

    /**
     * 如何操作运行类中指定的属性
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    @Test
    public void testield1() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Class clazz = Person.class;

        // 创建运行时对象
        Person p = (Person) clazz.newInstance();

        // 1、getDeclaredField(String fileName) 获取运行时类中指定变量名的属性
        Field name = clazz.getDeclaredField("name");

        // 2、保证当前属性是可以访问的
        name.setAccessible(true);

        // 3、获取，设置指定对象此属性值
        name.set(p,"tom");

        System.out.println(name.get(p));
    }

    /**
     * 如何操作运行时类中指定的方法
     */
    @Test
    public void testMethod() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Class clazz = Person.class;

        // 创建运行时对象
        Person p = (Person) clazz.newInstance();

        Method show = clazz.getDeclaredMethod("show", String.class);
        show.setAccessible(true);
        // 参数1方法的调用者， 参数2 给方法形参赋值的实参
        // invoke() 的返回值即位对应类中的调用方法的返回值
        Object returnValue = show.invoke(p, "CHN");
        System.out.println(returnValue);

        System.out.println("******************如何调用静态方法");


        Method showDesc = clazz.getDeclaredMethod("showDesc");
        showDesc.setAccessible(true);
        Object invoke = showDesc.invoke(Person.class);
        System.out.println(returnValue);

    }


    /**
     * 如何调用运行时类中的指定的构造器
     */
    @Test
    public void testConstructor() throws Exception {
        Class clazz = Person.class;

        Constructor constructor = clazz.getDeclaredConstructor(String.class);


        // 保证构造器是可访问的
        constructor.setAccessible(true);
        // 调用此构造器创建运行时类的对象
        Person person = (Person) constructor.newInstance("Tom");
        System.out.println(person);
    }
}
```





### 反射的应用:动态代理

代理设计模式的原理

使用一个代理将对象包装起来，然后用该代理对象取代原始对象，任何对原始对象的调用都要通过代理，代理对象决定是否以及何时将方法调用转到原始对象之上

静态代理，特征时代理类和目标对象的类是在编译期间确定下来，不利于程序的扩展，同时每一个代理类只能为一个接口服务，这样一来程序开发中必然产生过多代理，最好可以通过一个代理类完成全部的代理功

举例子；

实现Runnable接口的方法创建多线程

class MyThread implements Runnable{}

class Thread implements Runnable{}

##### 静态代理实例

```java

/**
 *
 * 静态代理举例
 *
 * 特点：代理类和被代理类在编译期间，就确定下来
 * @author gcq
 * @Create 2020-09-26
 */


interface  ClothFactory {

    void produceCloth();

}
// 代理类
class ProxyClothFactory implements ClothFactory {
    // 用被代理的对象进行实例化
    private ClothFactory factory;

    public ProxyClothFactory(ClothFactory clothFactory) {
        this.factory = clothFactory;
    }

    @Override
    public void produceCloth() {
        System.out.println("代理工厂做了一些准备工作 ");
        factory.produceCloth();
        System.out.println("代理工厂做了一些后续的收尾工作");
    }
}

class NickClothFactory implements ClothFactory {

    @Override
    public void produceCloth() {
        System.out.println("nick工厂生产一批运动服");
    }
}
public class staticProxyTest {

    public static void main(String[] args) {
        // 创建被代理类的对象
        NickClothFactory nike = new NickClothFactory();
        // 创建代理类的对象
        ProxyClothFactory proxyClothFactory = new ProxyClothFactory(nike);
        proxyClothFactory.produceCloth();
    }
}
```



##### ProxyTest.java

```java

/**
 * 动态代理举例
 *
 * @author gcq
 * @Create 2020-09-26
 */
interface Human {
    String getBelief();

    void eat(String food);
}
// 被代理类
class SuperMan implements Human {

    @Override
    public String getBelief() {
        return "I believe I can fly!";
    }

    @Override
    public void eat(String food) {
        System.out.println("我喜欢吃" + food);
    }
}
class ProxyFactory {
    /**
     * 调用此方法，返回一个代理类的对象，解决问题一
     * @param obj 被代理的对象
     * @return
     */
    public static Object getproxyInstance(Object obj) {
        MyInvocationHandler handler = new MyInvocationHandler();
        handler.bind(obj);
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),handler);
    }
}

/**
 * 要想实现动态代理，需要解决的问题
 * 问题一、如何根据加载到内存中的被代理类，动态的创建一个代理类及其对象
 * 问题二、当通过代理类的对象调用方法时，如何动态的去调用被代理类中的同名方法
 */
public class ProxyTest {
    public static void main(String[] args) {
        SuperMan superMan = new SuperMan();
        // proxyInstance 代理类的对象
        Human proxyInstance = (Human) ProxyFactory.getproxyInstance(superMan);
        // 当通过代理类对象调用方法时，会自动调用被代理类中同名的方法
        String belief = proxyInstance.getBelief();
        System.out.println(belief);
        proxyInstance.eat("四川麻辣烫");
        System.out.println("-----------------------------------");

        NickClothFactory nickClothFactory = new NickClothFactory();

        ClothFactory proxyClothFactory = (ClothFactory) ProxyFactory.getproxyInstance(nickClothFactory);

        proxyClothFactory.produceCloth();


    }

}
class MyInvocationHandler implements InvocationHandler {

    // 赋值时，需要使用被代理类的对象进行赋值

    private Object obj;
    public void bind(Object obj) {
        this.obj = obj;
    }
    /**
     * 当我们通过代理类的对象，调用方法 a 就会自动的调用如下方法 invoke()
     *  将被代理类的功能声明在invoke()方法中
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // method:代理类对象调用的方法，此方法就作为了代理类对象要调用的方法
        // obj 被代理类的对象
        Object returnValue = method.invoke(obj, args);
        // 上述方法返回值就作为当前类中invoke()的返回值
        return returnValue;
    }
}
```

## Java内置四大函数接口

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_10-49-45.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_10-51-06.png)



## 方法引用(Method References)

当传递给Lambda体的操作，已经有实现方法了，可以使用方法引用

方法引用可以看做 是Lambda表达式深层次的表达。换句话说，方法引用就
是Lambda表达式，也就是函数式接口的一“个实例，通过方法的名字来指向一个方法，可以认为是Lambda表达式的一-个语法糖。

要求:实现接口的抽象方法的参数列表和返回值类型，必须与方法引用的方法的参数列表和返回值类型保持-致!

格式:使用操作符 “::" 将类(或对象)与方法名分隔开来。

如下三种 主要使用情况:

- 对象::实例方法名
- 类::静态方法名
- 类::实例方法名



![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_10-56-45.png)

### 构造器引用

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_10-59-16.png)

### 数组引用

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_10-59-40.png)







## 强大的Stream Api

> Java8中由两大最重要的改变，第一个是Lambda表达式,另外一个是 Stream API
>
> Stream API (java.util.stream) 把真正的函数式编程风格引入到JJava中，这是目前为止对Java类库最好的补充，应为Stream API 可以极大提供Java程序员的生产力，让程序员写入高效、干净、简洁的代码
>
> Stream 是Java8中处理集合的关键抽象概念，它可以指定你希望对集合进行的操作，可以执行非常复杂的查找，过滤和映射等操作，使用 Stream API 对集合数据进行操作，就类似于使用SQL 执行数据库查询，也可以使用 Stream API 来并行执行操作，简言之，Stream API 提供了一种高效且易于使用的处理数据方式

### 为什么要用 Stream API

实际开发中，项目多数数据源来自Mysql,Oracle等，但现在数据源可以更多了，由MongDB,Redis等，而这些NoSQL数据就需要Java层面去处理

Stream 和 Collection 集合的区别， Collecetion是一种静态的内存数据结构，而Stream是有关计算的，前者主要面向内存，存储在内存中，后者主要是面向CPU,通过CPU进行计算

### 什么是 Stream

Stream到底是什么呢？

是数据渠道，用于操作数据源(集合、数组等) 所生成的元素序列

集合讲的是数据，Stream讲的是计算

注意：

1. Stream 不会自己存储元素
2. Stream 不会改变源对象，相反，他们会返回一个持有结果的新Stream
3. Stream 操作时延迟执行的，这意味着它们会等到需要结果才去执行



### Stream 的操作三个步骤

#### 1、创建Stream

一个数据源(如：集合，数组) 获取一个流

#### 2、中间操作

一个中间操作，对数据源的数据进行处理

#### 3、终止操作

一旦执行终止操作，就执行了中间操作连链，并产生结果，之后，不会再被使用

![](/Snipaste_2020-09-26_20-30-55.png)



### 创建 Stream 方式一：通过集合

Java8 中的 Collection 接口被扩展，提供了两个获取流的方法

> default Stream<E> stream()：返回一个顺序流
>
> default Stream<E> parallelStream()：返回一个并行流

```java
// 创建方式一
@Test
public void test1() {
    List<Employee> employees = StreamAPITest.getEmployees();

    // 顺序流
    Stream<Employee> stream = employees.stream();
    //并行流
    Stream<Employee> employeeStream = employees.parallelStream();
}
```



### 创建 Stream 方式二：通过数组

Java8 中的Arrays 的静态方法 stream() 可以获取数组流

static<T> Stream<T> stream(T[] array);  返回一个流

重载形式，能够处理对应基本类型的数组

> public static IntStream stream(int[] array)
>
> public static LongStream stream(long[] array)
>
> public static DoubleStream stream(double[] array)

```java
// 创建方式二
@Test
public void test2() {
      int[] arr = new int[]{1,2,3,4,5,6,7};
    IntStream stream = Arrays.stream(arr);
    Employee e1 = new Employee(1001,"Tom");
    Employee e2 = new Employee(1002,"Jerry");
    Employee[] arr1 = new Employee[]{e1,e2};
    Stream<Employee> stream1 = Arrays.stream(arr1);

}
```

### 创建 Stream 方式三：通过Stream的of()

可以调用Stream类静态方法 of()，通过显示值创建一个流，它可以接收任意数量的参数

public static<T> Stream<T> of(T...values) 返回一个流	 

```java
// 创建方式三
@Test
public void test3() {
    Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6);
}
```

### 创建 Stream 方式四：创建无限流

可以是使用静态方法 Stream.iterate() 和Stream.generate() 创建无限流

> 迭代
>
> public static<T> Stream<T> iterate(final T seed,final UnaryOperator<T> f)
>
> 生成
>
> public static<T> Stream<T> generate(Supplier<T> s)

```java
// 创建方式四
@Test
public void test4() {
    // 迭代
    Stream.iterate(0,t -> t+2).limit(10).forEach(System.out::println);

    // 生成
    Stream.generate(Math::random).limit(10).forEach(System.out::println);

}
```



### Stream 的中间操作

多个中间操作可以连接起来形成一个流水线，除非流水线上除法终止操作，否则中间操作不会执行任何的处理，而在终止操作时一次性全部处理，称为“隋性处理”

#### 1、筛选与切片

|        方法         |                             描述                             |
| :-----------------: | :----------------------------------------------------------: |
| filter(Predicate p) |                接收Lambda 从流中排除某些元素                 |
|     distinct()      |    筛选，通过流所生成的元素hashCode()和equals去除重复元素    |
| limit(long maxSize) |                截断流，使其元素不超过给定数量                |
|    skip(long n)     | 跳过元素，返回一个人扔掉了前n个元素的流，若流中元素不足n个，则返回一个空流，与limit(n)互补 |

```java
// 筛选与切片
@Test
public void test1() {
    List<Employee> employees = StreamAPITest.getEmployees();

    // filter
    Stream<Employee> stream = employees.stream();
    // 查询员工表中薪资大于2000的员工
    stream.filter(e -> e.getSalary() > 2000).forEach(System.out::println);
    System.out.println();

    // limit 截断流
    employees.stream().limit(3).forEach(System.out::println);
    System.out.println();

    // skip 跳过元素
    employees.stream().skip(3).forEach(System.out::println);
    System.out.println();
    //distinct 去重复
    employees.stream().distinct().forEach(System.out::println);


}
```

#### 2、映射

|              方法               |                             描述                             |
| :-----------------------------: | :----------------------------------------------------------: |
|        `map(Function n)`        | 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射到一个新的元素 |
| mapToDouble(ToDoubleFunction f) | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的DoubleStream |
|    mapToInt(ToIntFunction f)    | 接收一个函数作为参数该函数会应用到每个元素上，产生一个新的IntStream |
|   mapToLong(ToLongFunction f)   | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的LongStream |
|      `flatMap(Function f)`      | 接收一个函数作为参数，将流中每个值都换成另外一个流，然后把所有流连接成一个流 |

```java
// 映射
@Test
public void test2() {
    // map
    List<String> list = Arrays.asList("aa", "bb", "cc", "dd");
    list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
    System.out.println();

    // 获取员工姓名长度大于3员工的姓名
    List<Employee> employeeList = StreamAPITest.getEmployees();
    Stream<String> stringStream = employeeList.stream().map(Employee::getName);
    stringStream.filter(name -> name.length() > 3).forEach(System.out::println);

    // flatMap
   Stream<Stream<Character>> streamStream = employeeList.stream().map(StreamAPITest1::fromStringToStream);
   streamStream.forEach(s -> {
       s.forEach(System.out::println);
   });

}
public static Stream<Character> fromStringToStream(String str) {
    ArrayList<Character> list = new ArrayList<>();
    for (Character c : str.toCharArray()) {
        list.add(c);
    }
    return list.stream();
}
@Test
public void test3() {
    ArrayList list1 = new ArrayList();
    list1.add(1);
    list1.add(2);
    list1.add(3);

    ArrayList list2 = new ArrayList();
    list2.add(4);
    list2.add(5);
    list2.add(6);

    list1.addAll(list2);

    System.out.println(list1);
}
```

#### 3、排序

|         方法          |                 描述                 |
| :-------------------: | :----------------------------------: |
|       sorted()        |  产生一个新流，其中按照自然排序排序  |
| sorted(Compartor com) | 产生一个行流，其中按照比较器顺序排序 |

```java
    /**
     * 排序
     */
    @Test
    public void test4() {
        // sorted 自然排序
//        List<Integer> list = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
//        list.stream().sorted().forEach(System.out::println);


        //sorted(Comparator com) 定制排序

        List<Employee> employeeList = StreamAPITest.getEmployees();
        employeeList.stream().sorted((e1,e2) -> {
            int ageValue = Integer.compare(e1.getAge(),e2.getAge());
            if (ageValue != 0) {
                return ageValue;
            } else { 
                return -Double.compare(e1.getSalary(),e2.getSalary());
            }
        }).forEach(System.out::println);

    }
```

### Stream 的终止操作

终端操作会从流的流水线生成结果，其结果可以是任何不是流的值，列入List、Integer甚至是void

流进行了终止操作后，不能再次使用

#### 1、匹配与查找

|          方法          |           描述           |
| :--------------------: | :----------------------: |
| allMatch(Predicate p)  |   检查是否匹配所有元素   |
| anyMatch(Predicate p)  | 检查是否至少匹配一个元素 |
| noneMatch(Predicate p) | 检查是否没有匹配所有元素 |
|      findFirst()       |      返回第一个元素      |
|       findAny()        |   返回当前流中任意元素   |

```java
@Test
public void test1() {
    List<Employee> employees = StreamAPITest.getEmployees();
    // allMatch(Predicate p)
    // 是否所有员工的年龄都大于18
    boolean b = employees.stream().allMatch(e -> e.getAge() > 18);
    System.out.println(b);
    // anyMath 一检查是否至少匹配一个元素
    boolean b1 = employees.stream().anyMatch(e -> e.getSalary() > 10000);
    System.out.println(b1);

    // noneMath(Predicate p)-检查是否没有匹配的元素
    boolean b2 = employees.stream().noneMatch(e -> e.getName().startsWith("马"));
    System.out.println(b2);

    // findFirst 返回第一条元素
    Optional<Employee> employee = employees.stream().findFirst();
    System.out.println(employee);

    //findAny 返回当前流中的任意一个元素
    Optional<Employee> any = employees.parallelStream().findAny();
    System.out.println(any);
}
```

|        方法         | 描述                                                         |
| :-----------------: | ------------------------------------------------------------ |
|       count()       | 返回流中元素总数                                             |
|  max(Comparator c)  | 返回流中最大值                                               |
|  min(Comparator c)  | 返回流中最小值                                               |
| forEach(Consumer c) | 内部迭代(使用 Collection 接口需要用户去做迭代，称为外部迭代，相反 Stream API 使用内部迭代-他帮你把迭代做了) |

```java
@Test
public void test2() {
    List<Employee> employees = StreamAPITest.getEmployees();
    //count 返回流中元素个数
    long count = employees.stream().filter(e -> e.getSalary() > 5000).count();
    System.out.println(count);

    //max(Comparator c) 返回流中最大值
    // 返回最大工资
    Stream<Double> salaryStream = employees.stream().map(e -> e.getSalary());
    Optional<Double> max = salaryStream.max(Double::compare);
    System.out.println(max);

    //min(Comparator c)
    Optional<Employee> min = employees.stream().min((e1, e2) -> {
        return Double.compare(e1.getSalary(), e2.getSalary());
    });
    System.out.println(min);

    //forEach 内部迭代
    employees.stream().forEach(System.out::println);

}
```

#### 2、归约

|              方法               |                          描述                           |
| :-----------------------------: | :-----------------------------------------------------: |
| reduce(T iden,BinaryOperator b) |      可以将流中元素反复结合起来，得到一个值，返回T      |
|    reduce(BinaryOperator b)     | 可以将流中元素反复结合起来，得到一个值，返回Optional<T> |

map和reduce 的连接通常称为map-reduce模式，因Google用它来进行网络搜索来出名

```java
@Test
public void test3() {
    // reduce 可以将流中元素反复结合起来
    List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    Integer reduce = list.stream().reduce(0, Integer::sum);
    System.out.println(reduce);

    // reduce(BinaryOperator)
    List<Employee> employeeList = StreamAPITest.getEmployees();
    Stream<Double> doubleStream = employeeList.stream().map(Employee::getSalary);
    Optional<Double> reduce1 = doubleStream.reduce(Double::sum);
    System.out.println(reduce1);
}
```

#### 3、收集

|         方法         |                             描述                             |
| :------------------: | :----------------------------------------------------------: |
| collect(Collector c) | 将流转换为其他形式，接收一个Collector接口的实现，用于给Stream中元素做汇总的方法 |

Collector 接口方法的实现决定了如何对流执行收集的操作(如收集到List、Set、Map)

另外，Colleectors 实用类提供了静态方法，可以方便创建常见收集器实例，

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_09-35-06.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_09-35-21.png)

```java
@Test
public void test4() {
    //Collect 将流转换为其他形式
    List<Employee> list = StreamAPITest.getEmployees();
    List<Employee> collect = list.stream().filter(e -> e.getSalary() > 6000).collect(Collectors.toList());
    collect.forEach(System.out::println);
    System.out.println();

    Set<Employee> collect1 = list.stream().filter(e -> e.getSalary() > 6000).collect(Collectors.toSet());
    collect1.forEach(System.out::println);
}
```



## Optional类

> 到目前为止，臭名昭著的空指针异常是导致Java应用程序失败的最常见原因，以前，为了解决空指针异常，Google公司著名的Guava项目引入了Ooptiona类，Guava通过使用检查空值的方式来防止代码污染，他鼓励程序员写更干净的代码，受到Google Guava的启动，Optiona类已经成为Java 8类库的一部分
>
> Optional<T>类 (java.util.Optional)是一个容器类，它可以保存类型T值，代表这个值存在，或者仅仅保存null，表示这个值不存在，原来用null表示一个值不存在，现在Optional可以更好的表达这个概念，并且可以避免空指针异常
>
> Optional类的Javadoc描述如下m这是一个可以为null的容器对象，如果值存在则isPresent()方法返回true，调用get()方法返回该对象

Optional提供很多有用的方法，这样我们就不用显示进行空值检测

##### 创建Optional类对象的方法

> Optional.of(T t) 创建一个Optional实例，t必须为空
>
> Optional.empty() 创建一个空的Optional实例
>
> Optional.ofNullable(T t) t可以为null

##### 判断Optional容器中是否包含对象

> boolean isPresent() 判断是否包含对象
>
> void isPresent(Consumer<? super T> consumer) 如果有值，就执行Consumer接口的实例代码，并且该值会作为参数传给他

##### 获取Optional对象

> T get() 如果调用对象包含值，返回该值，否则抛出异常
>
> T orElse(T other) 如果有值将其返回，否则返回指定的other对象
>
> T orElseGet(Supplier<? extends T> other) 如果有值则将其返回，否则返回由Supplier接口实现提供的对象
>
> T orElseThrow(Supplier<? extensd X> exceptionSupplier) 如果有值将其返回，否则抛出由Supplier接口实现提供的异常

```java
/**
 * @author gcq
 * @Create 2020-09-27
 */
public class OptionalTest {
    @Test
    public void test1() {
        Girl girl = new Girl();
        girl = null;
        // of 保证t为空
        Optional<Girl> optionalGirl= Optional.of(girl);
        System.out.println(optionalGirl);
    }

    @Test
    public void test2() {
        Girl girl = new Girl();
        girl = null;
        // ofNullable(T t) 可以为空
        Optional<Girl> optionalGirl = Optional.ofNullable(girl);
        System.out.println(optionalGirl);
        // orElse(T t1) 如果单前的Optional内部封装的t是非空的，则返回内部的t
        // 如果内部的t是空的，则返回orElse()方法中的参数
        Girl girll = optionalGirl.orElse(new Girl("张三"));
        System.out.println(girll);
    }


    public String getGirlName(Boy boy) {
        if (boy != null) {
            Girl girl = boy.getGirl();
            if (girl != null) {
                return girl.getName();
            }
        }
     return null;
    }
    @Test
    public void test3() {
        Boy boy = new Boy();
        boy = null;
        String girlName = getGirlName(boy);
        System.out.println(girlName);
    }

    public String getGirlName2(Boy boy) {
        Optional<Boy> optionalBoy = Optional.ofNullable(boy);
        //此时boy1一定非空
        Boy boy1 = optionalBoy.orElse(new Boy(new Girl("李四")));

        Girl girl = boy1.getGirl();
        Optional<Girl> girlOptional = Optional.of(girl);
        //girl1一定非空
        Girl girl1 = girlOptional.orElse(new Girl("王五"));
        return girl1.getName();
    }

    @Test
    public void test5() {
        Boy boy = null;
        String grilName = getGirlName2(boy);
        System.out.println(grilName);
    }
}
```





## JDK 9 发布

- 经过4次跳票，历经曲折的Java 9终于终于在2017年9月21日发布。
- 从Java9这个版木开始，Java的计划发布周期是6个月，下一个Java的主版
  木将于2018年3月发布，命名为Java18.3， 紧接着再过六个月将发布Java
  18.9。
- 这意味着Java的更新从传统的以特性驱动的发布周期，转变为以时间驱动的
  (6个月为周期)发布模式，并逐步的将OracleJDK原商业特性进行开源。
- 针对企业客户的需求，Oracle 将以三年为周期发布长期支持版木( long term
  support)
- Java 9提供了超过150项新功能特性，包括备受期待的模块化系统、可交互
  的REPL工具: jshell, JDK 编译工具，Java 公共API和私有代码，以及安
  全增强、扩展提升、性能管理改善等。可以说Java 9是一个庞大的系统工程，完全做了一个整体改变。



#### 语法改进：接口私有方法

Java 8 中规定接口的方法除了抽象方法之外，还可以定义静态方法和默认方法，一定程度上，扩展了接口的功能，此时的接口更像是一个抽象类

在Java 9中，接口更加灵活强大,连方法的访问修饰符都可以声明为private的，此时方法将不会成为你对外暴露的API的一部分

```java
public interface MyInterface {

    // java9中允许接口中定义私有方法
    private static void methodPrivate() {
        System.out.println("我是接口的私有方法");
    }

    default void methodDefual(){
        System.out.println("接口默认方法");
        methodPrivate();
    }
}
```

#### 钻石操作符使用升级

我们能够将匿名实现类共同使用钻石操作符(dimaond operator) 在 java 8中 如下操作会报错

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-01-02.png)

```java
@Test
public void test2() {
    // 钻石操作符与匿名内部类在Java 8中不能共存，在Java9中可以
    Comparator<Object> com = new Comparator<>() {
        @Override
        public int compare(Object o1, Object o2) {
            return 0;
        }
    };

    // jdk7 中新特性 类型推断
    ArrayList<String> list = new ArrayList<>();
}
```



#### 语法该进: try语句

Java 8 中，可以实现资源的自动关闭，但是要求执行后必须关闭啊所有的资源必须在try子句中初始化，否则编译不通过

```java
![Snipaste_2020-09-27_14-23-50](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-23-50.png)![Snipaste_2020-09-27_14-22-41](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-22-41.png) // java9 特性六 try 操作升级
    public static void main(String[] args) {
        // java8 之前资源关闭操作
//        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
//        try {
//           char[] cha = new char[20];
//            int len;
//            if ((len = inputStreamReader.read(cha)) != -1) {
//                String str = new String(cha, 0, len);
//                System.out.println(str);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStreamReader != null) {
//                try {
//                    inputStreamReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        // java8 资源关闭操作，可以实现资源自动关闭
       /* try(InputStreamReader reader = new InputStreamReader(System.in)) {
            char[] cha = new char[20];
            int len;
            if ((len = reader.read(cha)) != -1) {
                String str = new String(cha, 0, len);
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // java9资源关闭操作，需要自动关闭的资源的实例化可以放在try的一对小括号中
        InputStreamReader reader = new InputStreamReader(System.in);
        try (reader){
            char[] cha = new char[20];
            int len;
            if ((len = reader.read(cha)) != -1) {
                String str = new String(cha, 0, len);
                System.out.println(str);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
```



#### String存储结构变更

jdk1.8

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-22-15.png)

jdk1.9

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-22-41.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-23-50.png)



#### 集合工厂方法: 快速创建只读集合

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-33-56.png)

```java
   // java9特性 集合工厂类方法，创建只读集合
    @Test
    public void test4() {
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        // 不能添加
//        integers.add(6);
        System.out.println(integers);

        Set<Integer> set1 = Set.of(1, 2, 3,44, 4, 5, 6, 7);
//        set1.add(123);
        System.out.println(set1);

        Map<String, Integer> tom = Map.of("Tom", 123, "Jerry", 123);
//        tom.put("list",123);
        System.out.println(tom);

        Map<String, Integer> stringIntegerMap = Map.ofEntries(Map.entry("Tom", 34), Map.entry("Jerry", 12));
        System.out.println(stringIntegerMap);
    }
```



#### InputStream加强

inputStream终于有了一个非常有用的方法，transferTo,可以用来将数据直接传输到OutputStream 这是在处理原始数据流时非常非常见的一种用法

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_14-39-58.png)



#### 增强的StreamApi

> Java 的Steam API是java标准库最好的改进之一一，让开发者能够快速运算，
> 从而能够有效的利用数据并行计算。Java 8提供的Steam能够利用多核架构
> 实现声明式的数据处理。
>
> 在Java9中，Stream API变得更好，Stream接口中添加了4个新的方法:
> takeWhile, dropWhile, ofNullable,还有个iterate方法的新重载方法，可以。
> 让你提供--个Predicate(判断条件)来指定什么时候结束迭代。
>
> 除了对Stream本身的扩展，Optional和Stream之间的结合也得到了改进。
> 现在可以通过Optional的新方法stream()将- -个Optional对象转换为一个
> (可能是空的)Stream对象。



##### takeWhile()使用

用于从Stream中获取一部分数据，接收一个Predicate来进行选择，在有序的Stream中，taskWhile返回从头开始的尽量多的元素



##### dropWhile()的使用

dropWhile的行为与takeWhile相反，I返回剩余的元素。



![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_15-06-45.png)



##### iterate()重载的使用

这个iterate 方法的新重载方法，可以让你提供一-个Predicate (判断条件)来指定什
么时候结束迭代。



```java
    @Test
    public void test1() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // takeWhile返回从开头开始指定规则尽量多的元素
//        list.stream().takeWhile(x -> x < 6).forEach(System.out::println);
        // dropWhile 与takewhile相反，返回剩余元素
        list.stream().dropWhile(x -> x < 6).forEach(System.out::println);
    }

    @Test
    public void test2() {
        //of()参数中多个元素 可以包含null值
        Stream<Integer> stream1 = Stream.of(1, 2, 3, null);
        stream1.forEach(System.out::println);

        Integer i = 10;
        i = null;
        Stream<Integer> stream3 = Stream.ofNullable(i);
        long count = stream3.count();
        System.out.println(count);
    }

	@Test
    public void test3() {
        Stream.iterate(0,x -> x+1).limit(10).forEach(System.out::println);

        //java9 重载方法
        Stream.iterate(0,x -> x < 100, x -> x+1).forEach(System.out::println);
    }
```



#### Optional获取Stream的方法

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_15-14-55.png)

#### JavaScript引擎升级

> Nashorn项目在JDK 9中得到改进，它为Java提供轻量级的Javascript 运行时。Nashorn项目跟随Netscape的Rhino项目，目的是为了在Java中实现-一个高
> 性能但轻量级的Javascript 运行时。Nashorn 项目使得Java 应用能够嵌入Javascript。它在JDK 8中为Java提供-个Javascript引擎。
>
>  JDK9包含一个用来解析Nashorn的ECMAScript语法树的API。 这个API使得IDE和服务端框架不需要依赖Nashorn 项目的内部实现类，就能够分析
> ECMAScript代码。







## Java 10新特性概述

> 2018年3月21日，Oracle官 方宣布Java10正式发布。
>
> 需要注意的是Java 9和Java 10都不是LTls (Long-Term-Support) 版本。和
> 过去的Java大版本升级不同，这两个只有半年左右的开发和维护期。而未
> 来的Java 11，也就是18.9LTS，才是Java8之后第一个LTS版本。
>
> JDK10-共定 义了109个新特性，其中包含12个JEP (对于程序员来讲，真
> 正的新特性其实就-一个)，还有一些新API和JVM规范以及JAVA语言规范上
> 的改动。
>
> JDK10的12个JEP (JDK Enhancement Proposal特性加强提议)参阅官方
> 文档: http://openjdk.java.net/projects/jdk/10/



![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_15-39-06.png)



### 一、局部变量类型推断

●产生背景
开发者经常抱怨Java中引用代码的程度。局部变量的显示类型声明，常常被认为
是是不必须的，给- -个好听的名字经常可以很清楚的表达出下面应该怎样继续。
●好处:
减少了哕嗦和形式的代码，避免了信息冗余，而且对齐了变量名，更容易阅读!
●举例如下:
➢场景一:类实例化时
作为Java开发者，在声明-一个变量时，我们总是习惯了敲打两次变量类型，第
一次用于声明变量类型，第二次用于构造器。
LinkedHashSet<Integer> set = new LinkedHashSet<>();
➢场景二:返回值类型含复杂泛型结构
变量的声明类型书写复杂且较长，尤其是加上泛型的使用
Iterator<Map. Entry< Integer, Student>> iterator = set. iterator();

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-10-15.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-11-01.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-11-19.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-12-05.png)

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-12-35.png)







## Java11新特性概述



北京时间2018年9月26日，Oracle官方宣布Java 11正式发布。这是Java大版本周期变化后的第一个长期支持版本，非常值得关注。从官网即可下载,最新发布的Java11 将带来ZGCHttp Client 等重要特性，-共包含17个JEP (JDK Enhancement Proposals，JDK 增强提案)。其实，总共更新不止17个，只是我们更关注如下的17个JEP更新。

JDK 11将是-一个企业不可忽视的版本。从时间节点来看，JDK 11的发布正好处在JDK8免费更新到期的前夕，同时JDK9、10也陆续成为“历史版本”，下面是Oracle JDK支持路线图:



JDK11是一一个长期支持版本(LTS, Long-Term-Support )
●对于企业来说，选择11将意味着长期的、可靠的、可预测的技术路线图。其中免费的OpenJDK11确定将得到OpenJDK 社区的长期支持，LTS 版本将:是可以放心选择的版本。

●从JVM GC的角度，JDK11 引入了两种新的GC，其中包括也许是划时代意义的ZGC，虽然其目前还是实验特性，但是从能力上来看，这是JDK的一一个巨大突破，为特定生产环境的苛刻需求提供了一个可能的选择。例如，对部分企业核心存储等产品，如果能够保证不超过10ms的GC暂停，可靠性会上一个大的台阶，这是过去我们进行GC调优几乎做不到的，是能与不能的问题。



一、String类方法

```java
![Snipaste_2020-09-27_17-09-42](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_17-09-42.png)  @Test
    public void test1() {
        // isBlank() 判断字符空白
        System.out.println("   ".isBlank());
        // strip() 去除收尾空白
        System.out.println("------" + "\t abc \t \n".strip() + "-------");
        // stripTraling()  去除尾部空格
        System.out.println("------" + "\t abc \t \n".stripTrailing() + "-------");
        // stripLeading() 去除头部空格
        System.out.println("------" + "\t abc \t \n".stripLeading() + "-------");
//        repeat(int count)
        String str1 = "abc";
        String str2 = str1.repeat(5);
        System.out.println(str2);

        // lines.count() 行数统计
        String str3 = "abc\ndef\ng";
        System.out.println(str3.lines().count());
    }
```

##### 二、Optional加强

Optional也增加了几个非常酷的方法，现在可以很方便的将-一个 Optional 转换
成一个Stream,或者当- -个空Optional时给它一一个 替代的。

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_16-41-33.png)



#### 三、局部变量类型推断升级

在var上添加注解的语法格式，在jdk10上不能实现的，在Jdk11中加入了这样的语法

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_17-09-42.png)

#### 四、全新的HTTP客户端API

- HTTP，用于传输网页的协议，早在1997年就被采用在目前的1.1版本中。直到2015年，HTTP2才成为标准。
- HTTP/1.1和HTTP/2的主要区别是如何在客户端和服务器之间构建和传输数据。HTTP/1.1依赖于请求/响应周期。HTTP/2 允许服务器“push”数据:它可以发送比客户端请求更多的数据。这使得它可以优先处理并发送对于首先加载网页至关重要的数据。
- 这是Java 9开始引入的一一个处理HTTP请求的的HTTP Client API,该API支持同步和异步，而在Java 11中已经为正式可用状态，你可以在，java.net包中找到这个API。
- 它将替代仪适用于blocking 模式的HttpURLConnection( HttpURLConnection是在HTTP 1.0的时代创建的，并使用了协议无关的方法)，并提供对WebSocket 和HTTP/2的支持。让天下没方难学的1



#### 七、ZGC

> GC是java主要优势之-一。 然而，当GC停顿太长，就会开始影响应用的响应时间。消除或者减少GC停顿时长，java将对更广泛的应用场景是-一个更有吸引力的平台。此外，现代系统中可用内存不断增长，用户和程序员希望JVM能够以高效的方式充分利用这些内存,并且无需长时间的GC暂停时间。
>
>  ZGC, A Scalable L ow-L atency Garbage Collector(Experimental)ZGC,这应该是JDK11最为瞩目的特性，没有之-一。 但是后面带了Experimental,说明这还不建议用到生产环境。
>
> ZGC是一一个并发，基于region,压缩型的垃圾收集器，只有root扫描阶段会STW(stop the world),因此GC停顿时间不会随着堆的增长和存活对象的增长而变长。

#### 优势:

GC暂停时间不会超过10ms既能处理几百兆的小堆，也能处理几个T的大堆(OMG)和G1相比,应用吞吐能力不会下降超过15% 为未来的GC功能和利用colord指针以及Load barriers优化奠定基础 初始只支持64位系统

ZGC的设计目标是:支持TB级内存容量，暂停时间低(<10ms) ，对整个程序吞吐量的影响小于15%。将 来还可以扩展实现机制，以支持不少令人兴奋的功能，例如多层堆(即热对象置于DRAM和冷对象置于NVMe闪存) ,或压缩堆。



## 在当前JDK中看不到什么

#### 一个标准化和轻量级的JSON API

- 一个标准化和轻量级的JSON API被许多Java开发人员所青睐，但是由于资金问题无法在当前java版本中见到，但并不会被削减到，Java平台首席架构师Mark Reinhold在JDK 9邮件列表中说：这个JEP将是平台上一个有用的补充，但是在计划中，他并不像Oracle资助的其他功能那么重要，可能会重写考虑JDK 10或更高版本中实现
- 新的货币API
  对许多应用而言货币价值都是一-个关键的特性，但JDK对此却几乎没有任何支持。严格来讲，现有的java.util.urrency类只是代表了当前ISO 4217货币的一- 个数据结构，但并没有关联的值或者自定义货币。JDK对 货币的运算及转换也没有内建的支持，
  更别说有一个能够代表货币值的标准类型了。
- 此前，Oracle公布的JSR 354定义了一套新的Java货币API: JavaMoney, 计划会在Java9中正式引入。但是目前没有出现在JDK新特性中。
- 不过，如果你用的是Maven的话，可以做如下的添加，即可使用相关的API处理货币: 

![](D:\java\脑图\JavaSE\images\Snipaste_2020-09-27_17-54-18.png)

#### 展望

- 随着云计算和Al等技术浪潮，当前的计算模式和场景正在发生翻天覆地的变化，不仅对Java 的发展速度提出了更高要求，也深刻影响着Java技术的发展方向。传统的大型企业或互联网应用，正在被云端、容器化应用、模块化的微服务甚至是函数(FaaS，Function-as-a-Service)所 替代。
- Java 虽然标榜面向对象编程，却毫不顾忌的加入面向接口编程思想，又扯出匿名对象之概念，每增加一个新的东西，对Java的根本所在的面向对象思想的一次冲击。反观Python， 抓住面向对象的本质，又 能在函数编程思想方面游刃有余。Java对标C/C++，以抛掉内存管理为卖点，却又陷入了JVM优化的噩梦。选择比努力更重要，选择Java的人更需要对它有更清晰的认识。
- Java 需要在新的计算场景下，改进开发效率。这话说的有点笼统，我谈- -些自己的体会，Java代码虽然进行了一些类型推断等改进，更易用的集合API等，但仍然给开发者留下了过于刻板、形式主义的印象，这是一一个长期的改进方向。