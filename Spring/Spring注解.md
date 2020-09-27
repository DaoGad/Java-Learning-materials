# Spring注解驱动开发

## 容器IOC

pom.xml

```xml
 <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.12.RELEASE</version>
 </dependency>
```

### 1. 组件注册-@Configuration&@Bean给容器中注册组件

##### mainConfig.java

```java
//配置类 == 配置文件
@Configuration // 告诉Spring这是配置类
public class MainConfig {
 	@Bean
    public Person person() {
        System.out.println("从容器中拿到对象");
        return new Person("lisi",20);
    }
}
```

##### Person对象

```java
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private int age;
}
```

##### Test.java

```java
 @Test
    public void test1() {
        // 读取xml文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Person bean = applicationContext.getBean(Person.class);
        System.out.println(bean);

        // 基于注解
        AnnotationConfigApplicationContext applicationContext1 = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
```

##### 输出结果

```java
Person(name=zhangsan, age=18)
Person(name=zhangsan, age=18)	
```



### 2 .组件注册-@ComponentScan-自动扫描组件&指定扫描规则

##### MainConfig.java

```java
//配置类 == 配置文件
@Configuration // 告诉Spring这是配置类

@ComponentScans(value = {
        @ComponentScan(value = "com.atguigu",includeFilters = {
//                @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class}),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {BookService.class},
                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})
        },useDefaultFilters = false)
})
// @ComponentScan value 指定要扫描的包
// excludeFilters = Filter[] 指定扫描时候按照什么规则排除
// includeFilters = Filter[] 指定扫描的时候只需要包含那些组件
// FilterType.ANNOTATION 只按照注解
// ASSIGNABLE_TYPE 按照给定的类型
public class MainConfig {
    @Bean
    public Person person() {
        System.out.println("从容器中拿到对象");
        return new Person("lisi",20);
    }
}
```

### 2.2 自定义TypeFilter指定过滤规则

##### MyTypeFilter

```java
/**
 * @author gcq
 * @Create 2020-09-22
 */
public class MyTypeFilter implements TypeFilter {
    /**
     *
     * @param metadataReader 读取到当前正在扫描的类的信息
     * @param metadataReaderFactory 可以获取其他任何类信息的
     * @return
     * @throws IOException
     */
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 获取当前类资源路径
        Resource resource = metadataReader.getResource();
        String className = classMetadata.getClassName();
        System.out.println("---->" + className);
        if (className.contains("er")) {
            return true;
        }
        return false;
    }
}
```

##### Test

```java
  @Test
    public void testMethod() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }
    }
```

##### 输出结果

- 输出注册在容器中的bean 以及被扫描到的组件

```java
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig
person
```

### 2.3 @Scope-设置组件作用域

MainConfig2.java

```java
//配置类 == 配置文件
@Conditional({WindowsCondition.class})
@Configuration
@Import({Color.class, Yellow.class, MyImplortSelector.class,MyImportBeanDefinitonRegistrar.class})
public class MainConfig2 {

  /*   给容器注册一个bean  类型为返回值，id默认是方法名作为id
     ConfigurableBeanFactory#SCOPE_PROTOTYPE
    	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
    	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
    	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
    	 * @see #value
     prototype 多实例
         	ioc容器启动并不会去调用方法创建对象放在容器中
    		每次获取的时候才会调用方法创建对象
     singleton单实例
            单实例默认值 ioc容器启动会调用方法创建对象放到ioc容器中
            以后每次获取就是直接从容器map.get()中拿
     request 同一次请求创建一个实例
     session 同一个session创建一个实例

     懒加载、@Lazy
        单实例bean 默认在容器启动的时候创建对象
        懒加载：启动容器不创建对象，第一次使用(获取)Bean创建对象，并初始化
     
    */
//    @Scope()
    @Bean
//    @Lazy  
    public Person person() {
        System.out.println("从容器中拿到对象");
        return new Person("lisi",20);
    }
```



### 2.4 @Conditional-按条件注册bean

MainConfig2.java

```java
// 满足这个条件,这个类中配置的所有bean注册才能生效
@Conditional({WindowsCondition.class})
@Configuration
public class MainConfig2 {

    /**
     * @Conditional({}) 按照一定的条件进行判断，满足条件给容器中注册bean
     * 系统widows 给容器中注册 bill
     * 系统linux 给容器注册 linus
     */
    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01() {
        return new Person("bill Gates",62);
    }

    @Conditional({LinuxCondition.class})
    @Bean("linus")
    public Person person02() {
        return new Person("linus",48);
    }
```

##### WindowsCondition.class

​	要求：实现Condition接口 重写matches方法

```java
public class WindowsCondition implements Condition {
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 返回当前环境昵称
        Environment environment = context.getEnvironment();
        // 拿到os.name 即当前环境昵称
        String property = environment.getProperty("os.name");
        // 当前环境包含windows
        if (property.contains("Windows")) {
            return true;
        }
        return false;
    }
}
```

##### Test

```java
   @Test
    public void test02() {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // Windows 10
        String property = environment.getProperty("os.name");
        System.out.println(property);
        Map<String, Person> oftype = applicationContext.getBeansOfType(Person.class);
        System.out.println(oftype);
    }
```

##### 结果

```java
person
bill
linus
Windows 10
{person=Person(name=lisi, age=20), bill=Person(name=bill Gates, age=62), linus=Person(name=linus, age=48)}


```



### 2.5 @Import-给容器中快速导入一个组件

MainConfig.java

```java
@Conditional({WindowsCondition.class})
@Configuration
@Import({Color.class})
// @Import导入组件，id默认是组件全类名
public class MainConfig2 {
  /**
     * 给容器注册组件
     * 1、包扫描+组件标准注解(@Controller/@Service/@Repository)
     * 2、@Bean [导入的第三方包里面的组件]
     * 3、@Import[快速给容器导入一个组件]
     *      1、@Import(要导入到容器中的组件) 容器中就会自动注册这个组件，id默认是全类名
     *      2、@ImportSelector：返回需要导入的组件的全类名 数组
     *      3、ImportBeanDefinitionRegistrar 手动注册bean到容器中
     *
    **/ 
```

##### 第二种 MyImportSelector.java

```java
public class MyImplortSelector implements ImportSelector {
    /**
     *
     * @param importingClassMetadata 当前标注@Import注解的所有注解信息
     * @return 导入到容器中的组件全类名
     */
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 返回需要导入组件的全类名
        return new String[]{"com.atguigu.bean.Red"};
    }
}
```

##### 第三种 ImportBeanDefinitionRegistrar

```java
public class MyImportBeanDefinitonRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry BeanDefnition注册类
     *                 把所有需要添加到容器中的bean 调用
     *                 BeanDefinitionRegistry.registerBeanDefinition手工注册
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean definiton = registry.containsBeanDefinition("color");
        boolean definiton2 = registry.containsBeanDefinition("yellow");
        System.out.println(definiton + "==" + definiton2);
        if (true) {
            System.out.println("bean");
            // 指定Bean定义信息，Bean的类型 Bean的作用域等等...
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RainBow.class);
            // 注册一个bean 执行bean名字
            registry.registerBeanDefinition("rainBow",rootBeanDefinition);
        }
    }
}
```

FactoryBean注册组件

```java
   /**
	 * 4、使用Spring提供的FactoryBean(工厂bean)
     *  1、默认获取到的是工厂bean调用getObject创建的对象
     *  2、要获取工厂Bean本身，需要给id前面加一个&
     *          &ColorFactory
     */

    @Bean("colorFactory")
    public ColorFactory colorFactory(){
        return new ColorFactory();
    }

```

ColorFactory.java

```java
// 创建一个Spring定义的FactoryBean
public class ColorFactory implements FactoryBean<Color> {
    /**
     * 返回一个color对象,这个对象会添加到容器中
     * @return
     * @throws Exception
     */
    public Color getObject() throws Exception {
        System.out.println("factoryBean.....");
        return new Color();
    }

    // 返回对象的类型
    public Class<?> getObjectType() {
        return Color.class;
    }

    /**
     * true 是单实例，在容器中保存一份
     * false 多实例，每次获取都会创建一个新的bean
     * @return
     */
    public boolean isSingleton() {
        return true;
    }
}
```

Test

```java
 @Test
    public void testImport() {
        printBeans(applicationContext);
//        Color red = applicationContext.getBean(Color.class);
//        System.out.println(red);
        //工厂Bean获取的是调用getObjec创建的对象
        Object colorFactory = applicationContext.getBean("colorFactory");
        Object colorFactory2 = applicationContext.getBean("colorFactory");

        System.out.println(colorFactory == colorFactory2);

        Object colorFactory3 = applicationContext.getBean("&colorFactory");
        System.out.println(colorFactory3);

    }
```

结果

```java
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig2
com.atguigu.bean.Color
com.atguigu.bean.Yellow
com.atguigu.bean.Red
person
bill
linus
colorFactory
rainBow
factoryBean.....
true
com.atguigu.bean.ColorFactory@78b1cc93
```



### 3 生命周期-@B ean指定初始化和销毁方法

##### MainConfigOfLifeCyle.java

```java
/**
 * bean 生命周期
 *      bean创建---初始化---销毁的课程
 *
 * 容器管理bean的生命周期：
 * 我们可以自定义初始化和销毁方法，容器在bean进行到当前生命周期的时候调用我们自定义的初始化和销毁
 *
 * 构造(对象创建)
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 *
 * BeanPostProcessor.postProcessBeforeInitialization
 * 初始化：
 *      对象创建完成时，并赋值好，调用初始化方法
 * BeanPostProcessor.postProcessAfterInitialization
 *
  * 遍历得到容器中所有的BeanPostProcessor 挨个执行beforeInitialization
 *  一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization
 *
 * BeanPostProcessor原理
 * populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw) //给bean属性赋值
 * initializeBean
 * {
 * wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);前面
 * invokeInitMethods(beanName, wrappedBean, mbd); 执行自定义初始化
 *  applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);后边
 *  }
 *      销毁:
 *      容器销毁
 * 1、指定初始化和销毁方法
 *      	通过在@Bean中init-method和destory-method
 * 2、通过Bean实现InitializingBean(定义初始化逻辑)
 *          DisposableBean(定义销毁逻辑)
 *
 * 3、可以使用JSR250
 *          @PostConstruct: 在bean创建完后并且属性赋值完成，来执行初始化方法
 *          @PostDestory:在容器销毁bean之前通知我们进行清理工作
 *
 * 4、BeanPostProcessor[interface]: bean的后置处理器
 *          在bean初始化前进行一些处理工作
 *          postProcessBeforeInitialization:初始化前工作
 *          postProcessAfterInitialization: 初始化之后
  * Spring底层对BeanPostProcessor的使用
 *      bean赋值，注入其他组件 @Autowired 生命周期注解功能，@Async xxxBeanPostProcessor

 * @author gcq
 * @Create 2020-09-22
 */
@ComponentScan("com.atguigu.bean")
@Configuration
public class MainConfigOfLifeCycle {

   // @Scope("prototype") 作用域
    /**
    initMethod 初始化方法
    destorymethod 销毁
    */
    @Bean(initMethod = "init",destroyMethod = "detory")
    public Car car() {
        return new Car();
    }
}
```

##### 第二种 通过让Bean实现 InitializingBean

```java
@Component
public class Cat implements InitializingBean, DisposableBean {

    public Cat() {
        System.out.println("car....");
    }

    // 销毁
    public void destroy() throws Exception {
        System.out.println("destory....");
    }
	// 创建时
    public void afterPropertiesSet() throws Exception {
        System.out.println("after.....");
    }
}
```

##### 第三种 JSR250

```java
@Component
public class Dog {
    
    public Dog() {
        System.out.println("dog constructor....");
    }
    // 对象创建并赋值后
    @PostConstruct
    public void init() {
        System.out.println("Dog.....Constructor....");
    }

    // 对象销毁后
    @PreDestroy
    public void detory() {
        System.out.println("Dog.......Destory.....");
    }
}
```

##### 第四种 BeanPostProcessor[interface]: bean的后置处理器

```java

/**
	实现接口
 * 后置处理器:初始化前后进行处理工作
 *  将后置处理器加入到容器中
 * @author gcq
 * @Create 2020-09-22
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
   /**
     *
     * @param bean 容器创建的实例
     * @param beanName bean在容器中的名字
     * @return
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization..." + beanName);
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization..." + beanName);
        return bean;
    }
}
```

### 4. @Value赋值

```java
  	@Value("张三")
    public String name;
    @Value("30")
    public int age;
    @Value("${person.nickName}")
    public String nickName;
```

MainConfigOfPropertyValue.java

```java
// 使用@PropertySource 读取外部配置文件中的k/v保存到运行环境变量中
@PropertySource(value = {"classpath:/person.properties"})
@Configuration
public class MainConfigOfPropertyValue {

    @Bean
    public Person person() {
        return new Person();
    }
}
```

### 5.@Autowired@Qualifier@Primary

mainConfigAutowired.java

```java
package com.atguigu.config;

import com.atguigu.bean.Car;
import com.atguigu.bean.Color;
import com.atguigu.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 自动装配：
 *      Spring利用依赖注入(DI) 完成对IOC容器中各个组件的依赖关系赋值
 * 1)、@Autowired:自动注入
 *      1、默认优先级按照类型区容器中找对应的组件：applicationContext.getBean(BeanDao.class)
 *      2、如果找到多个相同类型的组件，再将属性的名称作为组件的id去容器中查找
 *                  applicationContext.getBean("bookDao")
 *      3、@Qualifier("bookDao") 使用@Qualifier指定需要装配组件的id,而不是使用属性名
 *      4、自动转配一定要将属性赋值好，没有就会报错
 *          可以使用@Autowired(required=false)
 *      5、@Primary 让Spring进行自动装配的时候，默认使用bean
 *              也可以使用@Qualifier指定需要装配的bean的名字
 *        BookService{
 *          @Autowired
 *          BookDao bookdao;
 *        }
 * 2、Spring还支持@Resource(JSR250) @Inject(JSR303)
 *       @Resource
 *          可以和@Autowired一样实现自动装配功能，默认是按照组件名称进行装配
 *          没有能支持@Primary功能没有支持@Autowired(required=false)
 *       @Inject
 *       需要带入javax.inject的包，和Autowired功能一样，没有required=false的功能
 * @Autowired:Spring定义的 @Resource、@Inject 都是Java规范
 *
 * AutowireAnnotationBeanPostProcessor:解析完成自动装配功能
 *
 * 3、@Autowired 构造器，参数，方法，属性
 *          1、[标注在方法位置] @Bean+方法参数：参数都是从容器中获取，默认不写@Autowired效果是一样的，都能自动装配
 *          2、[标在构造器上]，如果组件只有一个有参构造器，这个有参构造器@Autowired可以省略，参数位置的组件还是可以自动从容器中获取
 *          3、放在参数位置
 *
 * 4、自定一组件想要使用Spring容器底层的一些组件(ApplicationContext,BeanFactory,xxx)
 *
 * @author gcq
 * @Create 2020-09-25
 */

@Configuration
@ComponentScan(value = {"com.atguigu.service","com.atguigu.controller","com.atguigu.dao","com.atguigu.bean"})
public class MainConigOfAutowired {

    @Primary
    @Bean("bookDao2")
    public BookDao bookDao() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

    /**
     * @Bean标注的方法创建对象的时候，方法参数的值从容器中获取
     * @param car
     * @return
     */
    @Bean
    public Color color(Car car) {
        Color color = new Color();
        color.setCar(car);
        return  color;
    }
}
```

##### BookService.java

```java
@Service
public class BookService {

//    @Qualifier("bookDao")
//	  @AutoWired
//    @Resource
    @Inject
    private BookDao bookDao2;

    public void print() {
        System.out.println(bookDao2);
    }

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao2=" + bookDao2 +
                '}';
    }
}
```

##### Boss.java

```java
/**
 * 默认加载ioc容器中的组件，容器启动会调用无参构造器对象，再进行初始化赋值等操作
 * @author gcq
 * @Create 2020-09-25
 */
@Component
public class Boss {

    private Car car;

    public Boss(@Autowired Car car) {
        this.car = car;
        System.out.println("boss有参构造器");
    }
    public Car getCar() {
        return car;
    }

    @Autowired
    // 标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值
    //方法使用的参数，自定义类型的值ioc容器中获取
    public void setCar(Car car) {
        this.car = car;
    }
}
```

### 6 Profile环境搭建

```java
/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境，动态的激活和切换一系列组件的功能
 *
 * 开发环境，测试环境，生产环境
 * 数据源 A B C
 *
 * @Profile 指定组件在那个环境情况下才能注册到容器中,不指定，任何环境下都能注册到这个组件
 *
 * 1、加了环境标识的bean，只有这个环境激活的时候才能注册到容器中，默认是default环境
 * 2、卸载配置类上，只有指定环境的时候，整个配置类里面的所有配置才能开始
 * 3、没有标注环境标识的bean，任何环境下都是不会加载的
 * @author gcq
 * @Create 2020-09-25
 */
@Profile("test")
@PropertySource("classpath:/dbconfig.properties")
@Configuration
public class MainConfigProfile implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver valueResolver;

    private String  driverClass;

    @Profile("test")
    @Bean
    public Yellow yellow() {
        return new Yellow();
    }


    @Profile("test")
    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}")String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Profile("dev")
    @Bean("devDataSource")
    public DataSource dataSourceDev(@Value("${db.password}")String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ssm");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Profile("prod")
    @Bean("prodDataSource")
    public DataSource dataSourceProd(@Value("${db.password}")String pwd) throws Exception{
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ssm");

        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        // TODO Auto-generated method stub
        this.valueResolver = resolver;
        driverClass = valueResolver.resolveStringValue("${db.driverClass}");
    }

}
```



```java

/**
 * @author gcq
 * @Create 2020-09-25
 */
public class IOCTest_Profile {

    //1、使用命令行动态参数，在虚拟机参数位置加载 -Dspring.profiles.active=test
    //2、代码的方式激活某种环境
    //3
    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigProfile.class);
        // 1、创建一个applicationContext
        // 2、设置需要激活的环境
       /* applicationContext.getEnvironment().setActiveProfiles("dev");
        // 3、注册配置类
        applicationContext.register(MainConfigProfile.class);
        // 4、启动刷新容器.
        applicationContext.refresh();*/

        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String string : beanNamesForType) {
            System.out.println(string);
        }
    }
}
```

## IOC 小结



组件添加

> @ComponentScan
>
> @Bean
>
> @Configuration
>
> @Component
>
> @Service
>
> @Controller
>
> @Repository
>
> @Conditional
>
> @Primary
>
> @Lazy
>
> @Scope
>
> @Import

组件赋值

> @Value
>
> @Autowired
>
> ​	@Qualifier
>
> @Resources(JSR250)
>
> @Inject(JSR303)
>
> @PropertySource
>
> @ProperySources
>
> @Profile





## AOP