---
typora-root-url: images
---

# Java设计模式

## 第一章 内容和授课方式

### 1.1 Java设计模式内容介绍

先看几个经典的面试题

原型设计模式问题：

1、有请使用UML类图画出原型模式核心角色

2、原型设计模式的深拷贝和浅拷贝是什么，并写出深拷贝和浅拷贝的两种方式的源码(重写clone方法实现深拷贝、使用序列化来实现深拷贝)

3、在Spring框架中那里使用到了原型模式，并对源码进行分析

beans.xml

<bean id="id01" class="com.atguigu.spring.bean.Monster" scope="prototype" />



- ### 先看几个经典设计模式面试题

设计模式七大原则 要求1) 七大设计原则核心思想 2) 能够以类图说明设计原则 3)在项目实际开发中，你那里用了 ocp 原则

![](/Snipaste_2020-09-28_09-05-13.png)

- ### 解释器设计模式

1、介绍解释器设计模式是什么？

2、画出解释器设计模式的 UML 类图，分析设计模式中各个角色是什么？

3、请说明 Spring 框架中，那里使用到了解释器设计模式，并作出源码级别的分析

4、Spring 框架中 SpelExpressionParser 就使用到了解释器模式

5、代码分析+Debug源码+模式角色分析说明

![](/Snipaste_2020-09-28_09-09-42.png)



- ### 单例设计模式一共有几种实现方式？ 请分别用代码实现，并说明各个实现方式的优点和缺点?

单例设计模式一共有8中写法，后面会依次讲到

饿汉式 两种

懒汉式 两种

双重检查

静态内部类

枚举



- ### 设计模式的重要性

1、软件工程中，**设计模式 (design pattern)** 是对软件设计中**普通存在(反复出现)** 的各种问题，所提出的解决方案 这个术语是埃里希·伽玛 等人 在1990年代从建筑设计领域引入到计算机科学的

2、大厦 VS 简易房

3、拿实际工作经历来说，当一个项目开发完后，如果**客户提出增新功能**，怎么办？(可扩展性,使用了设计模式，软件具有很好的扩展性)

4、如果项目开发完后，原来程序员离职，你接手维护的项目怎么办?（**维护性**(可读性、规范性)）

5、目前程序员门槛越来越高，一线公司(大厂)，都会问你在实际项目中**使用过什么设计模式，怎么使用的，解决了什么问题**

6、**设计模式在软件那里**？面向对象(oo)=>功能设计模块(设计模式+算法(数据结构))=>框架[使用到多种设计模式]=>架构[服务i集群]

7、如果想要成为合格的软件**工程师**，那就花时间来研究下设计模式是非常必要的



## 第二章 设计模式七大原则

### 2.1设计模式的目的

编写软件过程中，程序员面临着**耦合性，内聚性以及可维护性，可扩展性，重用性，灵活性**，等多方面的条件，设计模式是为了让程序(软件),具有更好

1、代码重用性(即：相同功能的代码，不用多次编写)
2、可读性(即：编程规范性，便于其他程序员的阅读和理解)

3、可扩展性(即：当需要增加新功能的时候，非常的方便，称为可维护)

4、可靠性(即：当我们增加新功能的时候，对原来的功能没有影响)

5、使程序程先高内聚，低耦合的特性

​	分享金句

6、设计模式包含了面向对象的精髓，“懂了设计模式，你就懂了面向对象分析和设计，(OOP/D)的精要”

7、Scott Myers 在其巨著《Effective C++》就曾经说过 C++的老手和 C++的新手区别是前者手背上有很多伤疤



### 2.2 设计模式七大原则

设计模式原则，其实就是程序员在编程时，应当遵守的原则，也是各种设计模式的基础(即：**设计模式为什么这样的依据**)

设计模式常用的七大原则是

1、单一职责原则

2、接口隔离原则

3、依赖倒转(倒置)原则

4、里式替换原则

5、开闭原则

6、迪米特原则

7、**合成复用原则**



### 2.3 单一职责原则

#### 	2.3.1 基本介绍

对类来说的，**即一个类应该只负责一项职责**，如类A负责两个不同的职责，职责1，职责2，当职责1需求变更而改变A时，可能造成职责2 执行错误，所以需要将类A的粒度分解为A1 A2

#### 	2.3.2 应用实例

​	以交通工具案例讲解

​	看老师代码演示

1. 方案1

   ```java
   
   /**
    * @author gcq
    * @Create 2020-09-28
    */
   public class SingleResponsibility1 {
       public static void main(String[] args) {
           Vehicle vehicle = new Vehicle();
           vehicle.run("摩托车");
           vehicle.run("汽车");
           vehicle.run("飞机");
       }
   }
   
   // 交通工具类
   // 方式1
   // 1、在方式1的run方法中，违反了单一职责原则
   // 2、解决的方案非常简单，根据交通工具的运行方法不同，分解成不同类即可
   class Vehicle {
       public void run(String vehicle) {
           System.out.println(vehicle + " 在公路上运行...");
       }
   }
   
   ```

   

2. 方案2

   ```java
   /**
    * @author gcq
    * @Create 2020-09-28
    */
   public class SingleResponsibility2 {
       public static void main(String[] args) {
           RoadVehicle roadVehicle = new RoadVehicle();
           roadVehicle.run("摩托车");
           roadVehicle.run("汽车");
           AirVehicle airVehicle = new AirVehicle();
           airVehicle.run("飞机");
       }
   }
   //方案2 分析
   // 1、遵守单一职责原则
   // 2、但是这样做的改动很大，即将类分解，同时修改客户端
   // 3、改进：直接修改Vehicle类，改动代码会比较少=>方案3
   class RoadVehicle {
       public void run(String vehicle) {
           System.out.println(vehicle + " 公路运行");
       }
   }
   class AirVehicle {
       public void run(String vehicle) {
           System.out.println(vehicle + " 天空运行");
       }
   }
   ```

3. 方案3

   ```java
   
   /**
    * @author gcq
    * @Create 2020-09-28
    */
   public class SingleResponsibility3 {
       public static void main(String[] args) {
           Vehicle2 vehicle2 = new Vehicle2();
           vehicle2.run("骑车");
           vehicle2.runAir("飞机");
           vehicle2.runWater("船");
       }
   }
   
   // 方式3的分析
   // 1、这种修改方法没有对原来的类做打的修改，只是增加方法
   // 2、这里虽然没有在类这个级别遵守单一职责原则，但是在方法级别上，任然是遵守单一职责
   class Vehicle2 {
       public void run(String vehicle) {
           System.out.println(vehicle +" 在公路上运行....");
       }
   
       public void runAir(String vehicle) {
           System.out.println(vehicle + " 在天空上运行....");
       }
   
       public void runWater(String vehicle) {
           System.out.println(vehicle + " 在水上运行");
       }
   }
   ```

   #### 2.3.3 单一职责原则注意事项和细节

   1、降低类的复杂度，一个类只负责一项职责

   2、提高类的可读性，可维护性

   3、降低变更引起的风险

   4、通常情况下，我们应当遵守单一职责原则，只有逻辑足够简单，才可以在代码违反单一职责原则，只有类中方法数量足够少，可以在方法级别保持单一职责原则



### 2.4 接口隔离原则(Interface Segregation Principle)

#### 	2.4.1 基本介绍

​	1、客户端不应该依赖它不需要的接口，即一个**类对另一个类的依赖应该建立在最小接口**上

​	2、先看一张图

![](/Snipaste_2020-09-28_13-17-23.png)

​	3、类 A 通过接口 Interface1 依赖B ，类C 通过接口 Interface 依赖D，如果接口 Interface1 对于类 A 和类 C 说不是最小接口，那么类 B 和类 D必须去实现他们不需要的方法

​	4、按隔离原则应当这样处理

​	**将接口 Interface1** 拆分为独立的几个接口(**这里我们拆分成三个接口)**，类 A 和 类 C 分别与他们需要的接口建立依赖关系，也就是采用接口隔离原则

#### 	2.4.2 应用实例

​	1、类A通过接口 interface1 依赖B 类C通过接口 Interface1依赖类D 请编写代码完成此实例，

​	2、老师代码，没有用接口隔离原则代码

```java
/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Segregation1 {

}

interface Interface1 {
    void operation1();
    void operation2();
    void operation3();
    void operation4();
    void operation5();
}
class B implements Interface1 {
    @Override
    public void operation1() {
        System.out.println("B 实现了Operation1");
    }

    @Override
    public void operation2() {
        System.out.println("B 实现了Operation2");
    }

    @Override
    public void operation3() {
        System.out.println("B 实现了Operation3");
    }

    @Override
    public void operation4() {
        System.out.println("B 实现了Operation4");
    }

    @Override
    public void operation5() {
        System.out.println("B 实现了Operation5");
    }
}
class D implements Interface1 {
    @Override
    public void operation1() {
        System.out.println("D 实现了Operation1");
    }

    @Override
    public void operation2() {
        System.out.println("D 实现了Operation2");
    }

    @Override
    public void operation3() {
        System.out.println("D 实现了Operation3");
    }

    @Override
    public void operation4() {
        System.out.println("D 实现了Operation4");
    }

    @Override
    public void operation5() {
        System.out.println("D 实现了Operation5");
    }
}

class A { // A 类通过接口Interface1 依赖使用B类 但是只会用到1，2，3方饭
    public void depend1(Interface1 i){
        i.operation1();
    }
    public void depend(Interface1 i) {
        i.operation2();
    }
    public void depend3(Interface1 i) {
        i.operation3();
    }
}
class C { //C 类通过接口Interface1 依赖使用 D类 但是只会用到 1，4，5 方法
    public void depend1(Interface1 i) {
        i.operation1();
    }
    public void depend4(Interface1 i) {
        i.operation4();
    }
    public void depend5(Interface1 i) {
        i.operation5();
    }
}
```

#### 	2.4.3 应传统方法的问题和使用接口隔离原则改进

类 A 通过接口 Interface1 依赖类B 类C 通过接口 Interface1 依赖类D ，如果接口 Interface1对类A 和类C来说不是最小接口，那么类B和类D 必须去实现他们不需要的方法

将接口 Interface1 拆分成独立的几个接口，类A 和类C 分别与他们需要的接口建立依赖关系，也就是采用接口隔离原则

接口 interface1出现的方法，根据实际情况拆分为三个接口

![](/Snipaste_2020-09-28_13-17-34.png)

代码实现

```java
package com.atguigu.principle.segregation.improve;

/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Segregation1 {
    public static void main(String[] args) {
        A a = new A();
        // A类通过接口去依赖B类
        a.depend1(new B());
        a.depend2(new B());
        a.depend3(new B());
        C c = new C();
        // C类通过接口去依赖(使用)D类
        c.depend1(new D());
        c.depend4(new D());
        c.depend5(new D());

    }

}

//接口1
interface Interface1 {
    void operation1();
}
// 接口2
interface Interface2 {
    void operation2();
    void operation3();
}
// 接口3
interface Interface3{
    void operation4();
    void operation5();
}

class B implements Interface1,Interface2 {
    @Override
    public void operation1() {
        System.out.println("B 实现了Operation1");
    }

    @Override
    public void operation2() {
        System.out.println("B 实现了Operation2");
    }

    @Override
    public void operation3() {
        System.out.println("B 实现了Operation3");
    }
}

class D implements Interface1,Interface3 {
    @Override
    public void operation1() {
        System.out.println("D 实现了Operation1");
    }
    @Override
    public void operation4() {
        System.out.println("D 实现了Operation4");
    }

    @Override
    public void operation5() {
        System.out.println("D 实现了Operation5");
    }
}

class A { // A 类通过接口Interface1,Interface2 依赖使用B类 但是只会用到1，2，3方饭
    public void depend1(Interface1 i){ i.operation1(); }
    public void depend2(Interface2 i){
        i.operation2();
    }
    public void depend3(Interface2 i){
        i.operation3();
    }
}

class C { //C 类通过接口Interface1 Interface3 依赖使用 D类 但是只会用到 1，4，5 方法
    // 参数是接口
    public void depend1(Interface1 i) {
        i.operation1();
    }
    public void depend4(Interface3 i) {
        i.operation4();
    }
    public void depend5(Interface3 i) {
        i.operation5();
    }
}
```



### 2.5 依赖倒转原则



#### 2.5.1基本介绍

1. 依赖倒转原则(Dependence Inversion Principle)是指

2. 高层模块不应该依赖底层模块，二者都应该实现其抽象

3. **抽象不应该依赖细节，细节应该依赖对象**

4. 依赖倒转(倒置)的中心思想是**面向接口编程**

5. 依赖倒转原则是基于这样的设计理念，相对于细节多变性，抽象的东西要稳定得多，以抽象为基础搭建的架构比细节为基础的架构要稳定的多，在Java中，重选ing指的是接口或抽象类，细节就是具体的实现类

6. 使用**接口或抽象类**目的是制**定好规范**，而不涉及任何具体的操作，把**展现细节的任务交给他们实现类**去完成

   

#### 2.5.2 应用案例

请编程完成Person接收消息功能

1、实现方案1+分析说明

```java
package com.atguigu.principle.inversion;

/**
 * @author gcq
 * @Create 2020-09-28
 */
public class DependecyInversion {
    public static void main(String[] args) {
        Person person = new Person();
        person.receive(new Email());
    }
}
class Email {
    public String getInfo() {
        return "电子邮件消息：hello world";
    }
}
// 完成Person接收消息功能
//方式1完成分析
// 1、简单，比较容易想到
// 2、如果我们获取的对象是微信，短信等等，则新增类，同时Persons也要增加相应的接收方法
// 3、解决思路 引入一个抽象的接口 IReceiver 表示接收者，这样Person类与接口IReceiver发送依赖
// 因为Email、WeXin,等等属于接收的范围，他们各自实现IReceiver 接口就ok，这样我们就符合依赖倒转原则
class Person {
    public void receive(Email email) {
        System.out.println(email.getInfo());
    }
}
```

2、实现方案2+分析说明

```java
package com.atguigu.principle.inversion.improve;

/**
 * @author gcq
 * @Create 2020-09-28
 */
public class DependecyInversion {
    public static void main(String[] args) {
        // 客户端无需改变
        Person person = new Person();
        person.receive(new Email());
        person.receive(new WeiXin());
    }
}

interface IReceiver {
    public String getInfo();
}
class Email implements IReceiver {
    @Override
    public String getInfo() {
        return "电子邮件消息：hello world";
    }
}
// 增加微信
class WeiXin implements IReceiver {

    @Override
    public String getInfo() {
        return "微信消息: hello ok";
    }
}

class Person {
    public void receive(IReceiver receiver) {
        System.out.println(receiver.getInfo());
    }
}
```

 2.5.3 依赖关系传递的三种方式和应用案例

1、接口传递

2、构造方法传递

3、setter方式传递

代码

```java
package com.atguigu.principle.inversion.improve;

public class DependencyPass {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ChangHong changHong = new ChangHong();
        // 接口
//        OpenAndClose openAndClose = new OpenAndClose();
//        openAndClose.open(changHong);
        // 通过构造器
//        OpenAndClose openAndClose = new OpenAndClose(changHong);
//        openAndClose.open();
        // 通过set
        OpenAndClose openAndClose = new OpenAndClose();
        openAndClose.setTv(changHong);
        openAndClose.open();

    }

}

// 方式1： 通过接口传递实现依赖
// 开关的接口
//interface IOpenAndClose {
//    public void open(ITV tv); //抽象方法,接收接口
//}
//
//interface ITV { //ITV接口
//    public void play();
//}
//
class ChangHong implements ITV {

    @Override
    public void play() {
        System.out.println("长虹电视机,打开");
    }
}
//// 实现接口
//class OpenAndClose implements IOpenAndClose {
//    public void open(ITV tv) {
//        tv.play();
//    }
//}

// 方式2: 通过构造方法依赖传递
/*interface IOpenAndClose {
    public void open(); //抽象方法
}

interface ITV { //ITV接口
    public void play();
}

class OpenAndClose implements IOpenAndClose {
    public ITV tv;

    public OpenAndClose(ITV tv) {
        this.tv = tv;
    }

    public void open() {
        this.tv.play();
    }
 }*/

    // 方式3 , 通过setter方法传递
    interface IOpenAndClose {
        public void open(); // 抽象方法

        public void setTv(ITV tv);
    }

    interface ITV { // ITV接口
        public void play();
    }

    class OpenAndClose implements IOpenAndClose {
        private ITV tv;

        public void setTv(ITV tv) {
            this.tv = tv;
        }

        public void open() {
            this.tv.play();
        }
    }
```

注意事项 

低层模块尽量都要有抽象类或接口，或者两者都有，程先稳定性更好

变量的**声明类型尽量是抽象类或接口**，这样我们变量引用和实际对象间，就存在一个**缓冲层**，利于程先扩展和优化

继承时遵守里氏替换原则



### 2.6 里氏替换原则

#### 	2.6.1  oo中的继承性思考和说明

1、继承包含这样一层含义，父类中凡是已经实现好的方法，实际上时在设定规划和契约，虽然他不强制要求所有的子类必须遵守这些契约，但是如果子类对这些已经实现的方法任意修改，就会对整个继承体系造成破坏



2、**继承在程序设计带来便利的同时，也带来了弊端**，比如使用继承会给程序带来**侵入性**，程序的可移植性降低，增加对象间的耦合性，如果一个类被其他类所继承，则当这个类需要修改时，必须考虑到所有的子类，并且父类修改后，所有涉及到子类的功能都有可能产生故障

3、问题提出，在编程中，如何正确使用继承=》里氏替换原则



基本介绍

1. 里氏替换原则 在 19988年 由麻省理工学院的一位姓里的女士提出的，
2. 如果对每个类型为T1的对象o1，都有类型为T2的对象 o2,时的以 T1定义的程序 P在所有对象o1都代换成o2时，程序P的行为没有发生变化，那么类型T2是类型T1的子类型，**换句话说，所有引用基类的地方必须能透明的使用其子类的对象**
3. 在使用继承时，遵循里氏替换原则，在**子类中尽量不要重写父类方法**
4. 里氏替换原则告诉我们，继承实际上让两个类耦合性增强了，在适当情况下，可以通过聚合，组合，依赖来解决问题



#### 2.6.3 一个程序引出的问题和思考

```java
package com.atguigu.principle.liskow;

/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Liskov {

    public static void main(String[] args) {
        A a = new A();
        System.out.println("11-3=" + a.func1(11,3));
        System.out.println("1-8=" +a.func1(1,8));

        System.out.println("---------------");
        B b = new B();
        System.out.println("11-3=" + b.func1(11,3));
        System.out.println("1-8=" + b.func1(1,8));
        System.out.println("11+3+9=" + b.func2(11,3));
    }
}

class A {
    // 返回连个数的差
    public int func1(int num1,int num2) {
        return num1 - num2;
    }
}
// B 继承了 A
// 增加一个新功能，完成两个数相加，然后和9求和
class B extends A {
    // 这里重写了A类的方法，可能时无意识
    @Override
    public int func1(int a, int b) {
        return a + b;
    }
    public int func2(int a, int b) {
        return func1(a,b) + 9;
    }

}
```

#### 2.6.4 解决方法

1. 我们发现原来运行正常的相减功能发生了错误，原因就是类B无意中重写了父类的方法，造成原有功能出现错误，在实际编程中，我们常常会通过重写父类的方法完成新的功能，这样写虽然简单，但整个继承体系的复用性比较差，特别是运行多态比较频繁的时候

2. 通用的做法是：原来的父类和子类都继承了一个更通俗的基类，原有的继承关系去掉，采用依赖、聚合、组合等关系替代

3. 解决方案

   ![](/Snipaste_2020-09-28_15-29-14.png)

代码实现

```java
package com.atguigu.principle.liskow.improve;

/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Liskov {

    public static void main(String[] args) {
        A a = new A();
        System.out.println("11-3=" + a.func1(11,3));
        System.out.println("1-8=" +a.func1(1,8));

        System.out.println("---------------");
        B b = new B();
        System.out.println("11-3=" + b.func1(11,3));
        System.out.println("1-8=" + b.func1(1,8));
        System.out.println("11+3+9=" + b.func2(11,3));


        System.out.println("11+3+9=" + b.func3(11,3));
    }
}

// 创建一个更加基础的基类
class Base {
    // 把更加基础的方法和成员写到Base类

}
class A extends Base{
    // 返回连个数的差
    public int func1(int num1,int num2) {
        return num1 - num2;
    }
}

// B 继承了 A
// 增加一个新功能，完成两个数相加，然后和9求和
class B extends Base {
    // 如果B需要使用A类的方法，使用组合关系
    private A a = new A();
    // 这里重写了A类的方法，可能时无意识
    public int func1(int a, int b) {
        return a + b;
    }
    public int func2(int a, int b) {
        return func1(a,b) + 9;
    }
    public int func3(int a, int b) {
        return this.a.func1(a,b);
    }
}
```

 

### 2.7 开闭原则

#### 	2.7.1基本介绍

1、开闭原则(Open Closed Principle)是编程中**最基础、最重要**的设计原则

2、一个软件实体如类，模块和函数应该对扩展开放**(对提供方)，对修改关闭(对使用**
**方。**用抽象构建框架，用实现扩展细节。

3、当软件需要变化时，尽量**通过扩展软件实体**的行为来实现变化，而不是**通过修改**已
有的代码来实现变化。

4、编程中遵循其它原则，以及使用设计模式的目的就是遵循开闭原则。

#### 	2.7.2 看下面一段代码

看一个画图形的功能

![](/Snipaste_2020-09-29_11-56-10.png)



代码演示

```java
package com.atguigu.principle.ocp;


/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Ocp {

    public static void main(String[] args) {
        // 使用看看存在的问题
        GraphicEditor graphicEditor = new GraphicEditor();
        graphicEditor.drawShape(new Rectangle());
        graphicEditor.drawShape(new Circle());
        graphicEditor.drawShape(new Triangle());
        graphicEditor.drawShape(new A());
    }
}
// 这是一个用于绘图的类
class GraphicEditor {
    public void drawShape(Shape s) {
        if (s.m_type == 1) {
            drawRectangle(s);
        } else  if (s.m_type == 2) {
            drawCircle(s);
        } else if (s.m_type == 3) {
            drawTriangle(s);
        } else if (s.m_type == 4) {

        }
    }
    public void drawRectangle(Shape r) {
        System.out.println("绘制矩形");
    }
    public void drawCircle(Shape r) {
        System.out.println("绘制圆形");
    }
    public void drawTriangle(Shape r) {
        System.out.println("绘制三角形");
    }
    public void drawA(Shape r) {
        System.out.println("绘制A");
    }
}
// Shape类 基类
class Shape {
    int m_type;
}
class Rectangle extends Shape {
    Rectangle() {
        super.m_type = 1;
    }
}
class Circle extends Shape {
    Circle() {
        super.m_type = 2;
    }
}
//新增画三角形
class Triangle extends Shape {
    Triangle() {
        super.m_type = 3;
    }
}
class A extends Shape {
    A() {
        super.m_type = 4;
    }
}
```



####  2.7.3 方式1 的优缺点

1. 优点是比较好理解，简单易操作
2. 缺点是违反了设计模式的ocp原则，即对扩展开放(提供方)，对修改关闭(使用方)。即当我们给类增加新功能的时候，尽量不修改代码，或者尽可能少修改代码.
3. 比如我们这时要新增加一个图形种类三角形，我们需要做如下修改，修改的地方较多
4. 代码演示



方式 1改进思路分析

#### 	2.7.4 改进思路分析

思路:把创建Shape类做成抽象类，并提供- -个抽象的draw方法，让子类去实现即可，这样我们有新的图形种类时，只需要让新的图形类继承Shape,并实现draw方法即可，使用方的代码就不需要修>满足了 开闭原则

改进后代码

```java
package com.atguigu.principle.ocp.improve;


/**
 * @author gcq
 * @Create 2020-09-28
 */
public class Ocp {

    public static void main(String[] args) {
        // 使用看看存在的问题
        GraphicEditor graphicEditor = new GraphicEditor();
        graphicEditor.drawShape(new Rectangle());
        graphicEditor.drawShape(new Circle());
        graphicEditor.drawShape(new Triangle());
        graphicEditor.drawShape(new OtherGraphic());
    }
}

// 这是一个用于绘图的类
class GraphicEditor {
    // 接收Shape对象，然后根据type，来绘制不同的图形
    public void drawShape(Shape s) {
        s.draw();
    }

}

// Shape类 基类
abstract class Shape {
    int m_type;
    // 抽象方法
    public abstract void draw();
}

class Rectangle extends Shape {
    Rectangle() {
        super.m_type = 1;
    }

    @Override
    public void draw() {
        System.out.println("绘制矩形");
    }
}

class Circle extends Shape {
    Circle() {
        super.m_type = 2;
    }

    @Override
    public void draw() {
        System.out.println("绘制圆形");
    }
}

//新增画三角形
class Triangle extends Shape {
    Triangle() {
        super.m_type = 3;
    }

    @Override
    public void draw() {
        System.out.println("绘制三角形");
    }
}

//新增一个图形
class OtherGraphic extends Shape {
    OtherGraphic() {
        super.m_type = 4;
    }

    @Override
    public void draw() {
        System.out.println("七边形");
    }
}
```



### 2.8 迪米特法则

#### 	2.8.1 基本介绍

1、一个对象应该对其他对象保持最少的了解

2、类与类关系越密切，耦合度越大

3、迪米特法则(Demeter Principle)又叫**最少知道原则**，即一个**类对自己依赖的类知道的越少越好**。也就是说，对于被依赖的类不管多么复杂，都尽量将逻辑封装在类的内部。对外除了提供的public方法，不对外泄露任何信息

4、迪米特法则还有个更简单的定义:只与直接的朋友通信

5、**直接的朋友**: 每个对象都会与其他对象有**耦合关系**，只要两个对象之间有耦合关系，我们就说这两个对象之间是朋友关系。耦合的方式很多，依赖，关联，组合，聚合等。其中，我们称出现**成员变量，方法参数，方法返回值**中的类为直接的朋友，而出现在**局部变量中的类不是直接的朋友**。也就是说，陌生的类最好不要以局部变量的形式出现在类的内部。

#### 	2.8.2 应用实例

1、有一个学校，下属有各个学院和总部，现要求打印出学校总部员工ID和学院员工的id 
2、编程实现上面的功能，看代码演示

3、代码演示

```java
package com.atguigu.principle.demeter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gcq
 * @Create 2020-09-29
 */
// 客户端
public class Demter {

    public static void main(String[] args) {
        SchoolManager schoolManager = new SchoolManager();
        schoolManager.printAllEmployee(new CollegeManager());
    }
}
// 学校总部员工类
class Employee {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// 学院员工类
class CollegeEmployee {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
// 学院管理类
class CollegeManager {
    // 返回学院所有员工
    public List<CollegeEmployee> getAllEmployee() {
        List<CollegeEmployee> list = new ArrayList<CollegeEmployee>();
        // 增加了10个员工到list集合
        for(int i = 0; i < 10; i++) {
            CollegeEmployee emp = new CollegeEmployee();
            emp.setId("学院员工id=" + i);
            list.add(emp);
        }
        return list;
    }

}

// 分析 SchoolManager 类的直接朋友类有那些 Employee CollegeManager
// CollegeEmployee 不是直接朋友而是一个陌生类，违反了迪米特法则
// 学校管理类
class SchoolManager {
    // 返回学校总部的员工
    public List<Employee> getAllEmployee() {
        List<Employee> list = new ArrayList<Employee>();
        for(int i = 0; i < 5; i++) {
            Employee emp = new Employee();
            emp.setId("学校员工id=" + i);
            list.add(emp);
        }
        return list;
    }
    // 该方法完成输出学校总部和学院员工的信息id
    void printAllEmployee(CollegeManager sub) {
        // 分析问题
        // 1、这里的CollegeEmployee 不是 SchoolManager 直接朋友
        //  2、CollegeEmployee 是以局部变量的方式出现在 SchoolManager
        // 3、违反了迪米特法则

        // 将输出学院的员工方法，封装到 CollegeManager
        // 获取到学院员工
        List<CollegeEmployee> list1 = sub.getAllEmployee();
        System.out.println("---------------分公司员工----------------");
        for (CollegeEmployee e: list1) {
            System.out.println(e.getId());
        }

        // 获取到学校总部员工
        List<Employee> list2 = this.getAllEmployee();
        System.out.println("---------------学校总部员工---------------");
        for (Employee e : list2) {
            System.out.println(e.getId());
        }
    }
}
```



#### 	2.8.3 应案例改进

1、前面设计的问题在于SchoolManager中，CollegeEmployee 类并不是SchoolManager类的直接朋(分析)

2、按照迪米特法则，应该避免类中出现这样非直接朋友关系的耦合

3、对代码按照迪米特法则进行改进. (看老师演示)

```java
package com.atguigu.principle.demeter.improve;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gcq
 * @Create 2020-09-29
 */
// 客户端
public class Demter {

    public static void main(String[] args) {
        System.out.println("使用迪米特法则改进");
        SchoolManager schoolManager = new SchoolManager();
        schoolManager.printAllEmployee(new CollegeManager());
    }
}

// 学校总部员工类
class Employee {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// 学院员工类
class CollegeEmployee {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// 学院管理类
class CollegeManager {
    // 返回学院所有员工
    public List<CollegeEmployee> getAllEmployee() {
        List<CollegeEmployee> list = new ArrayList<CollegeEmployee>();
        // 增加了10个员工到list集合
        for(int i = 0; i < 10; i++) {
            CollegeEmployee emp = new CollegeEmployee();
            emp.setId("学院员工id=" + i);
            list.add(emp);
        }
        return list;
    }
    // 输出学院员工信息
    public void printEmployee() {
        List<CollegeEmployee> list1 = getAllEmployee();
        System.out.println("---------------分公司员工----------------");
        for (CollegeEmployee e: list1) {
            System.out.println(e.getId());
        }
    }
}

// 分析 SchoolManager 类的直接朋友类有那些 Employee CollegeManager
// CollegeEmployee 不是直接朋友而是一个陌生类，违反了迪米特法则
// 学校管理类
class SchoolManager {
    // 返回学校总部的员工
    public List<Employee> getAllEmployee() {
        List<Employee> list = new ArrayList<Employee>();
        for(int i = 0; i < 5; i++) {
            Employee emp = new Employee();
            emp.setId("学校员工id=" + i);
            list.add(emp);
        }
        return list;
    }
    // 该方法完成输出学校总部和学院员工的信息id
    void printAllEmployee(CollegeManager sub) {
        // 分析问题
        // 1、这里的CollegeEmployee 不是 SchoolManager 直接朋友
        //  2、CollegeEmployee 是以局部变量的方式出现在 SchoolManager
        // 3、违反了迪米特法则

        // 将输出学院的员工方法，封装到 CollegeManager
        // 获取到学院员工
        sub.printEmployee();

        // 获取到学校总部员工
        List<Employee> list2 = this.getAllEmployee();
        System.out.println("---------------学校总部员工---------------");
        for (Employee e : list2) {
            System.out.println(e.getId());
        }
    }
}
```



#### 	2.8.4 迪米特法则注意事项和细节

1、迪米特法则核心是降低类之间的耦合

2、但是注意：由于每个类都减少了不必要的依赖因此迪米特法则只是要求降低类间(对象间)耦合关系，并不是要求完全没有依赖关系



### 2.9 合成复用原则(Composite Reuse Principle)



#### 	2.9.1 基本介绍

原则尽量使用合成/聚合的方式，而不是使用继承

![](/Snipaste_2020-09-29_12-15-38.png)

#### 	

#### 	2.9.10 设计原则核心思想

1、找出应用中可能需要变化之处，把他们独立出来，不要和那些不需要变化的代码混在一起

2、针对接口编程，而不是针对实现编程

3、为了交互对象之间的松耦合设计而努力



# 第三章 UML 类图



### 	3.1 UML 基本介绍

1、UML--Unified modeling language UML(统一建模语言)，是一种用于软件系统分析和设计的语言工具，它用于帮助软件开发人员进行思考和记录思路的结果

2、 UML本身是一套符号的规定，就像数学符号和化学符号一样，这些符号用于描述软件模型中的各个元素和他们之间的关系，比如类、接口、实现、泛化、依赖、组合、聚合等，如右图:

![](/Snipaste_2020-09-29_12-56-43.png)

3、使用UML来建模，常用的工具有RationalRose ,也可以使用一些插件来建模

![](/Snipaste_2020-09-29_12-53-30.png)



#### 	3.2 UML 图

画UML图与写文章差不多，都是把自己的思想描述给别人看，关键在于思路和条理，UML图分类:

1、用例图(use case)
2、静态结构图:类图、对象图、包图、组件图、部署图
3、动态行为图:交互图(时序图与协作图)、状态图、活动图

说明：

1、类图是描述类与类之间的关系的，是UML图中最核心的
2、在讲解设计模式时，我们必然会使用类图，为了让学员们能够把设计模式学到位，需要先给大家讲解类图
3、温馨提示:如果已经掌握UML类图的学员，可以直接听设计模式的章节



#### 	3.3 UML 类图

1、用于描述系统中的**类(对象)本身的组成和类(对象)之间的各种静态关系。**
2、类之间的关系:依赖、泛化(继承)、实现、关联、聚合与组合。
3、类图简单举例

```java
public class Person { // 代码形式 ---> 类图

    private Integer id;
    private String name;


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
```

![](/Snipaste_2020-09-29_13-21-21.png)



#### 	3.4 类图--依赖关系(Dependence)

只要是在类中用到了对方，那么他们之间就存在依赖关系，如果没有对方，连编译都过不了

```java
public class PersonServiceBean{
    private PersonDao personDao;
    public void save(Person person){}
    public IDCard getIDCard(Integer personid) {}
    public void modify(){
        Department department = new Department();
    }
}
public class PersonDao{}
public class IDCard{}
public Person{}
public class Department{}
```

![](/Snipaste_2020-09-29_16-16-53.png)

**小结**

1、类中用到了对方

2、如果是**类的成员属性**

3、如果是**方法的返回类型**

4、是方法的**接收参数类型**

5、**方法中使用到**



#### 	3.5 类图--泛化关系(generalization)

泛化关系实**际上就是继承关系**，他是依赖关系的特例

```java
public abstract class DaoSupport {
    public void save(Object entity) {
        
    }
    public void deelte(Object id) {
        
    }
}
public class PersonServiceBean extends DaoSupport {
    
}
```

对应的类图

![](/Snipaste_2020-09-29_16-27-22.png)

**小结：**

1、泛型关系实际**上就是继承关系**

2、如果 A 类继承了 B 类，我们就说 A 和 B 存在泛化关系



#### 	3.6 类图--实现关系(Implementation)

实现关系实际上就是**A类实现B接口**，他是依赖关系的特例

```java
public interface PersonService{
    public void deelte(Integer id);
}
public class PersonServiceBean implements PersonService{
    public void deletge(Integer id){}
}
```

类图

![](/Snipaste_2020-09-29_16-29-36.png)



#### 	3.7 类图--关联关系(Association)

![](/Snipaste_2020-09-29_16-32-19.png)



#### 3.8 类图--聚合关系(Aggregation)

 	**3.8.1基本介绍**	 

​		聚合关系(Aggregation) 表示的是**整体和部分的关系**，**整体与部分可以分开**，聚合关系是**关联关系的特例**，所以他具有关联的**导航性与多重性**

​		如：一台电脑由键盘(keyboard)，显示器(monitor)，鼠标等组成电脑的各个配件可以从电脑上分离出来的，使用带空心菱形的实现来表示

![](/Snipaste_2020-09-29_16-38-16.png)

​	**3.8.1 应用实例**

![](/Snipaste_2020-09-29_16-38-05.png)



#### 3.9 类图--组合关系(composition)

​		**3.9.1 基本介绍**

组合关系：也是整体与部分的关系，但是**整体与部分不可以分开**，

在看一个案例：在程序中我们定义实体，Person与IDCard 、Head,那么 Head 和 Person 就是 组合 IDCard和 Person就是聚合



但是如果在程序中 Person实体中定义了对IDCard进行**级联删除**，即删除 Person时连同IDCard 一起删除，那么 IDcard 和 Person就是组合



 	**3.9.2 应用案例**

```java
public class Person{
    private IDCard card;
    private Head head = new Head();
}
public class IDCard{}
public class Head{}
```

对应类图

![](/Snipaste_2020-09-29_16-46-06.png)

案例二

```java
package com.atguigu.aggregation;

/**
 * @author gcq
 * @Create 2020-09-29
 */
public class Computer {

    // 鼠标可以和computer不能分离
    private Moniter moniter = new Mouse();
    // 显示器可以和computer不能分离
    private Mouse mouse = new Mouse();

    public void setMoniter(Moniter moniter) {
        this.moniter = moniter;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }
}
```

```java
/**
 * @author gcq
 * @Create 2020-09-29
 */
public class Moniter {

}
```

```java
/**
 * @author gcq
 * @Create 2020-09-29
 */
public class Mouse {

}
```

![](/Snipaste_2020-09-29_16-48-34.png)





## 第 4 章 设计模式概述 

### 	4.1 掌握设计模式的层次









