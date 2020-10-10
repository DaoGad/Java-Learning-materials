---

typora-root-url: image
typora-copy-images-to: image
---

# Redis

## SpringBoot整合

创建SpringBoot项目 选择Redis配置

![](/Snipaste_2020-10-10_11-41-48.png)

在 SpringBoot2.x 之后 原来使用 jedis 被替换为了 lettuce？

jedis：采用的直连，多个线程操作的话，是不安全的，如果想要避免不安全，使用 jedis pool 连接池 BIO 模式

lettuce：采用的netty，实例可以在多个线程中共享，不存在线程不安全的情况，可以减少线程数据 更像 NIO 模式

源码分析：

```java
@Bean
@ConditionalOnMissingBean(name = "redisTemplate")
public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
    // 默认的 RedisTemplate 没有过多的设置 reids对象都是需要序列化！
    // 两个泛型都是 object Object的类型 我们后使用的需要强制类型转换<String,Object>
   RedisTemplate<Object, Object> template = new RedisTemplate<>();
   template.setConnectionFactory(redisConnectionFactory);
   return template;
}

// 由于 String 类型 是redis中最常使用的类型 所以说单独提出来了一个
@Bean
@ConditionalOnMissingBean
public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
      throws UnknownHostException {
   StringRedisTemplate template = new StringRedisTemplate();
   template.setConnectionFactory(redisConnectionFactory);
   return template;
}
```

> 整合测试

1、导入依赖

```xml
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```

2、配置连接

```properties
# SpringBoot 所有配置类 都有一个自动配置类 RedisAutoConfiguration
# 自动配置类都会绑定一个 properties配置文件 RedisProperties
spring.redis.host=192.168.186.133
spring.redis.port=6379
```

3、测试！ 在SpringBoot中 大多数使用 RedisTemplate 操作

```java
 @Test
    void contextLoads() {
        // redisTemplate 操作不同的类型
        // opsForValue 操作字符串 类似 String
        // opsForSet 操作集合
//        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
//        connection.flushDb();
//        connection.flushAll();

        redisTemplate.opsForValue().set("mykey","lubenwei");
        System.out.println(redisTemplate.opsForValue().get("mykey"));
    }
```

 自定义RedisTemplate

所有的 redis 的操作，其实对于 Java 开发人员来说 十分简单，更重要的是去理解 redis 的思想 和每一种数据结构的用处和作用场景

```java
![Snipaste_2020-10-10_11-41-48](/Snipaste_2020-10-10_11-41-48.png) // 编写我们自己的reidsTemplate
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
```



# Redis.conf 详解

启动的时候，就通过配置文件来启动

行家一出手 

> 单位

![](/Snipaste_2020-10-10_12-55-41.png)

1、配置文件 unit单位 对大小写不敏感

> 包含

![](/Snipaste_2020-10-10_12-58-24.png)

 就好比是我们学习的 Spring、import、include

> 网络

```bash
bind 127.0.0.1 # 绑定的ip
protected-mode no #	保护模式
port 6379 #	端口设置
```

> 通用 GENERAL

```bash
daemonize yes #	以守护线程的方式运行,默认是no 我们需要自己开启为yes！

pidfile /var/run/redis_6379.pid # 如果以后台的方式运行，我们需要指定一个 pid 文件

# 日志
# Specify the server verbosity level.
# This can be one of:
# debug (a lot of information, useful for development/testing)
# verbose (many rarely useful info, but not a mess like the debug level)
# notice (moderately verbose, what you want in production probably) 生产环境
# warning (only very important / critical messages are logged) 警告
loglevel notice

logfile "" # 日志文件位置名

databases 16 # 数据库数量 默认是16个数据库

always-show-logo yes # 是否显示Logo



```



> 快照

持久化 ，在规定的时间内，执行了多少次操作，则会持久化到文件，rdb aof

redis 是内存数据库，如果没有持久化，那么数据断电就会流失

```bash
# 如果 900s 内，如果至少有一个 1 key 进行了修改，我们及进行了持久化操作
save 900 1
# 如果 300s 内，如果至少有一个 10 key 进行了修改，我们及进行了持久化操作
save 300 10
# 如果 600s 内，如果至少有一个 10000 key 进行了修改，我们及进行了持久化操作
save 60 10000

stop-writes-on-bgsave-error yes # 持久化出错，是否嗨需要继续工作

rdbcompression yes # 是否压缩 rdb 文件，需要消耗 CPU 资源 

rdbchecksum yes # 保存 rdb 文件的时候,进行错误的检查效验

dir ./ # rdb 文件保存的目录


```



> REPLICATION 复制，后面讲主从复制的时候在讲解



> SECURITY 安全

可以是遏制redis的密码 ，默认是没有密码的

```bash
127.0.0.1:6379> ping
PONG
127.0.0.1:6379> config get requirepass # 获取 redis 密码
1) "requirepass"
2) ""
127.0.0.1:6379> config set requirepass "123456" # 设置 redis 密码
OK
127.0.0.1:6379> ping 
(error) NOAUTH Authentication required. # 发现所有命令都没有权限
127.0.0.1:6379> auth 123456 # 使用密码进行登录
OK
127.0.0.1:6379> config get requirepass 
1) "requirepass"
2) "123456"
127.0.0.1:6379> 

```



> 限制 CLIENTS

```bash
maxclients 10000 # 设置能连接上 redis 的最大客户端的数量

maxmemory <bytes> # redis 配置最大的内存容量


# maxmemory-policy noeviction

1、volatile-lru：只对设置了过期时间的key进行LRU（默认值） 
2、allkeys-lru ： 删除lru算法的key   
3、volatile-random：随机删除即将过期key   
4、allkeys-random：随机删除   
5、volatile-ttl ： 删除即将过期的   
6、noeviction ： 永不过期，返回错误
```



> APPEND ONLY MODE 模式 aof配置

```bash
appendonly no # 默认是不开启 aof 模式的，默认是使用 rdb 方式持久化，在大部分的所有情况下，rdb 完全够用！

appendfilename "appendonly.aof" # 持久化文件的名字

# appendfsync always 	# 每次修改都会 sync，消耗性能
appendfsync everysec	# 每秒一次 sync，可能会丢失 1s 的数据
# appendfsync no		# 不执行 sync 这个时候操作系统自己同步数据，速度最快

```

具体配置



# Redis持久化

面试工作 持久化是重点！

Redis是内存数据库,如果不将内存中的数据库状态保存到磁盘,那么-旦服务器进程退出 ,服务器中的数据库状态也会消失。所
以Redis提供了持久化功能!DB (Redis DataSource)

> 什么是RDB

主从复制中，rdb 就是备用了！ 从机上面

![image-20201010054102228](/image-20201010054102228.png)

在指定的时间间隔内将内存中的数据集快照写入磁盘,也就是行话讲的Snapshot快照,它恢复时是将快照文件直接读到内存里。Redis会单独创建( fork)一个子进程来进行持久化,会先将数据写入到一一个临时文件中,待持久化过程都结束了,再用这个临时文件替换上次持久化好的文件。整个过程中,主进程是不进行任何I0操作的。这就确保了极高的性能。如果需要进行大规模数据恢复，且对于数据恢复的完整性不是非常敏感,那RDB方式要比AOF方式更加的高效。RDB的缺点是最后- -次持久化后的数据可丢失。

![image-20201010054433608](/image-20201010054433608.png)

![image-20201010054516446](/image-20201010054516446.png)

> 触发机制

1、save 的规则满足的情况下，会自动触发 rdb 的规则

2、执行 flushall 命令 也会触发我们的rdb 规则

3、退出 redis 也会产生 rdb 文件

备份就会自动生成一个	dump.rdb 文件

![image-20201010054959525](/image-20201010054959525.png)

> 如果恢复 rdb 文件

1.只需要将rdb文件放在我们redis启动目录就可以, redis启动的时候会自动检查dump.rdb恢复其中的数据!

2.查看需要存在的位置

```bash
127.0.0.1:6379> config get dir
1) "dir"
2) "/usr/local/redis/bin" # 如果在这个目录下存在 rdb文件 就会自动恢复其中的数据
127.0.0.1:6379> 
```

> 几乎就他自己默认的配置就够用了，但是我们还是需要去学习

优点：

1、适合大规模的数据恢复

2、对数据的完整性要求不高

缺点：

1、需要一定的时间间隔进程操作，如果redis意外宕机了，那么最后一次保存到数据就没有了！

2、fork进程的时候，会占用一定的内存空间



### AOF （Append Only File）

将我们所有的命令都记录下来，history，恢复的时候就把这个文件全部在执行一遍

> 是什么

![image-20201010080820784](/image-20201010080820784.png)

以日志的形式来记录每个写操作,将Redis执行过的所有指令记录下来(读操作不记录) ,只许追加文件但不可以改写文件, redis
启动之初会读取该文件重新构建数据,换言之, redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复
工作

**Aof保存的是appendonly,aof文件**

> append

![image-20201010073255503](/image-20201010073255503.png)

默认是不开启的，我们需要手动设置，我们只需要将 appendonly 改为 yes 就开启了of

重启 redis就可以生效了

如果这个 aof 文件有错误 这时候 redis 是启动不起来的 我们需要修复这个 aof文件

redis 给我们提供了一个工具 `redis-check-aof --fix`

![image-20201010075511233](/image-20201010075511233.png)

如果文件正常，重启就可以恢复了

![image-20201010075549953](/image-20201010075549953.png)



> 优点和缺点

优点：

1、每一次修改都同步，文件的完整性更加的好

2、每秒同步一次，可能会丢失一秒的数据

缺点：

1、对于数据文件来说，aof 远远大于 rdb 修复速度也比 rdb 慢

2、Aof 运行效率也要比 rdb慢 所以我们 redis 默认的配置就是 rdb持久化



**扩展**:

1、RDB 持久化方式能够在指定的时间间隔内对你的数据进行快照存储

2、AOF 持久化方式记录每次对服务器写的操作,当服务器重启的时候会重新执行这些命令来恢复原始的数据, AOF命令以Redis协
议追加保存每次写的操作到文件末尾, Redis还能对AOF文件进行后台重写,使得AOF文件的体积不至于过大。

3、只做缓存,如果你只希望你的数据在服务器运行的时候存在,你也可以不使用任何持久化
4、同时开启两种持久化方式

- 在这种情况下,当redis重启的时候会优先载入AOF文件来恢复原始的数据,因为在通常情况下AOF文件保存的数据集要比RDB
  文件保存的数据集要完整。
- RDB 的数据不实时,同时使用两者时服务器重启也只会找AOF文件,那要不要只使用AOF呢?作者建议不要,因为RDB更适合
  用于备份数据库( AOF在不断变化不好备份) , 快速重启,而且不会有AOF可能潜在的Bug ,留着作为一个万的手段。

5、性能建议

- 因为RDB文件只用作后备用途,建议只在Slave上持久化RDB文件,而且只要15分钟备份- -次就够了,只保留save 900 1这条
  规则。
- 如果Enable AOF ,好处是在最恶劣情况下也只会丢失不超过两秒数据,启动脚本较简单只load自己的AOF文件就可以了,代价
  是带来了持续的I0 ,二是AOF rewrite的最后将rewrite过程中产生的新数据写到新文件造成的阻塞几乎是不可避免的。只要
  硬盘许可,应该尽量减少AOF rewrite的频率, AOF重写的基础大小默认值64M太小了,可以设到5G以上,默认超过原大小
  100%大小重写可以改到适当的数值。
- 如果不Enable AOF , 仅靠Master-Slave Replcation实现高可用性也可以,能省掉-大笔IO ,也减少了rewrite时带来的系统
  波动。代价是如果Master/Slave 同时倒掉,会丢失十几分钟的数据,启动脚本也要比较两个Master/Slave中的RDB文件,载
  入较新的那个,微博就是这种架构。



# Redis发布订阅



Redis 发布订阅 (pub/sub) 是一种消息通信模式：发送者 (pub) 发送消息，订阅者 (sub) 接收消息。

Redis 客户端可以订阅任意数量的频道。

订阅/发布消息图

![image-20201010081429183](/image-20201010081429183.png)

下图展示了频道 channel1 ， 以及订阅这个频道的三个客户端 —— client2 、 client5 和 client1 之间的关系：

![img](https://www.runoob.com/wp-content/uploads/2014/11/pubsub1.png)

当有新消息通过 PUBLISH 命令发送给频道 channel1 时， 这个消息就会被发送给订阅它的三个客户端：

![img](https://www.runoob.com/wp-content/uploads/2014/11/pubsub2.png)

> Redis 发布订阅吗命令

这些命令被广泛用于构建和即时通信应用，比如网络聊天室，和实时广播，实时提醒等等

![image-20201010081909989](/image-20201010081909989.png)



> 测试

订阅者：

```bash
127.0.0.1:6379> SUBSCRIBE zhangsan
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "zhangsan"
3) (integer) 1
1) "message"
2) "zhangsan"
3) "hello zhangsan"
1) "message"
2) "zhangsan"
3) "hello zhangsan111"

```

发送端：

```bash
127.0.0.1:6379> PUBLISH zhangsan "hello zhangsan"
(integer) 1
127.0.0.1:6379> PUBLISH zhangsan "hello zhangsan111"
(integer) 1
127.0.0.1:6379> 

```

> 原理

Redis是使用C实现的,通过分析Redis源码里的pubsub.c文件.了解发布和订阅机制的底层实现.籍此加深对Redis的理解。

Redis通过PUBLISH . SUBSCRIBE 和PSUBSCRIBE等命令实现发布和订阅功能。

通过SUBSCRIBE命令订阅某频道后, redis-server 里维护了一一个字典.字典的键就是一个个channel , 而字典的值则是-一个链
表,链表中保存了所有订阅这个channel的客户端。SUBSCRIBE 命令的关键,就是将客户端添加到给定channel的订阅链表中。

![image-20201010084824504](/image-20201010084824504.png)

通过PUBLISH命令向订阅者发送消息, redis-server会使用给定的频道作为键,在它所维护的channel字典中查找记录了订阅这
个频道的所有客户端的链表,遍历这个链表.将消息发布给所有订阅者。

Pub/Sub从字面上理解就是发布( Publish )与订阅( Subscribe) . 在Redis中,你可以设定对某一个key值进行消息发布及消息订
阅，当一个key值上进行了消息发布后,所有订阅它的客户端都会收到相应的消息。这-功能最明显的用法就是用作实时消息系
统，比如普通的即时聊天,君群聊等功能。

使用场景

1、实时消息系统

2、实时聊天()频道作为聊天室 将信息回显给所有人_

复杂的场景我们会使用 消息中间件 MQ



# Redis主从复制

### 概念

主从复制，是指将一台Redis服务器的数据,复制到其他的Redis服务器。前者称为主节点(master/leader) ,后者称为从节点(slave/follower) ;数据的复制是单向的,只能由主节点到从节点。Master以写为主 , Slave 以读为主。

**默认情况下,每台Redis服务器都是主节点;**

且-一个主节点可以有多个从节点(或没有从节点) ,但-一个从节点只能有一一个主点。

主从复制的作用主要包括:

1.数据冗余:主从复制实现了数据的热备份,是持久化之外的一种数据冗余方式。

2.故障恢复:当主节点出现问题时,可以由从节点提供服务,实现快速的故障恢复;实际上是一一种服务的冗余。

3.负载均衡:在主从复制的基础上。配合读写分离,可以由主节点提供写服务,由从节点提供读服务(即写Redis数据时应用连接主节点,读Redis数据时应用连接从节点) , 分担服务器负载;尤其是在写少读多的场景下,通过多个从节点分担读负载,可以大大提高

Redis服务器的并发量。

4、高可用（集群）基石:除了上述作用以外,主从复制还是哨兵和集群能够实施的基础,因此说主从复制是Redis高可用的基础。



一般来说，要将Redis运用于工程项目中,只使用一台Redis是万万不能的（宕机）,原因如下: 

1.从结构上,单个Redis服务器会发生单点故障,并且一台服务器需要处理所有的请求负载,压力较大;

2、从容量上。单个Redis服务器内存容量有限。就算一-台Redis服务 器内存容量为256G ,也不能将所有内存用作Redis存储内存。一般来说，**单台Redis最大使用内存不应该超过20G.**

电商网站.上的商品，-般都是- -次上传,无数次浏览的,说专业点也就是"多读少写"。

对于这种场景,我们可以使如下这种架构:

![image-20201010211501550](/image-20201010211501550.png)

主从复制 读写分离，80% 的情况下都是在进行操作，减轻服务器的压力，架构中经常使用，一主二从

只要在公司中，主从复制就是必须使用的，因为在真实的项目中不可能单机使用 Redis ！



### 环境配置

只配置从库，不用配置主库

```bash
127.0.0.1:6379> info replication # 查看当前库的信息
# Replication
role:master # 角色 master
connected_slaves:0 # 没有从机
master_replid:b38dd03d9534ebea3023ec0eb0c8280c8bdbd426
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
127.0.0.1:6379> 

```

复制 3 个文件 ，然后修改对应的信息

1、端口

2、pid 名字

3、log文件名字

4、dump.rdb 名字

修改完毕后，启动我们的 3 个 redis 服务器，可以通过进程信息 查看

![image-20201010210750983](/image-20201010210750983.png)



## 一主二从

默认情况下，每台 Redis 服务器都是主节点 我们一般情况下只用配置从机就好了！

认老大 一主(79) 二从(80,91)

```bash
127.0.0.1:6380> SLAVEOF 127.0.0.1 6379  # SLAVEOF host 6379
OK
127.0.0.1:6380> info replication
# Replication
role:slave # 当前角色是从机
master_host:127.0.0.1 # 可以看到主机的信息
master_port:6379
master_link_status:up
master_last_io_seconds_ago:2
master_sync_in_progress:0
slave_repl_offset:0
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:a2bd5794fcc83538afc7de058a5baf57f679f41d
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:0
127.0.0.1:6380> 

# 从主机中打开
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=127.0.0.1,port=6380,state=online,offset=0,lag=0
master_replid:a2bd5794fcc83538afc7de058a5baf57f679f41d
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:0
127.0.0.1:6379> 


```

![image-20201010211441316](/image-20201010211441316.png)



> 细节

主机可以写，从机只能读



![image-20201010211733412](/image-20201010211733412.png)

![image-20201010211804009](/image-20201010211804009.png)

测试 主机断开连接，从机依旧只能读不能写，这个时候 主机如果回来了，从机依旧可以直接获取到主机写的信息！

如果是使用命令行，来配置主从，这个时候如果重启了，就会变回主机 只要变为从机，立马就会从主机

> 复制原理

Slave启动成功连接到master后会发送一-个sync命令

Master接到命令,启动后台的存盘进程,同时收集所有接收到的用于修改数据集命令,在后台进程执行完毕之后, **master将传送**

**整个数据文件到slave ,并完成-次完全同步**。

**全量复制**:而slave服务在接收到数据库文件数据后,将其存盘并加载到内存中。

**增量复制**: Master继续将新的所有收集到的修改命令依次传给slave ,完成同步

但是只要是重新连接master ,-次完全同步 (全量复制)将被自动执行



> 层层链路

上一个M 链接下一个 S

![image-20201010212606676](/image-20201010212606676.png)

这时候也可以完成我们的主从复制



> 如果没有老大了，这个时候能不能新出来一个老大？手动！

谋权篡位

如果主机断开了连接，我们可以使用 `SLAVEOF no one` 让自己变成主机！ 其他节点就可以手动连接到最新的这个节点(手动) 如果这个时候老大修复了，那就重新连接



## 哨兵模式

自动选举老大的模式

> 概述

主从切换技术的方法是:当生服务器宕机后,需要手动把一-台从服务 器切换为主服务器,这就需要人工干预,费事费力,还会造成
一段时间内服务不可用。这不是一-种推荐的方式,更多时候,我们优先考虑哨兵模式。Redis从2.8开始正式提供了Sentinel (哨
兵)架构来解决这个问题。

谋朝篡位的自动版，能够后台监控主机是香敌障,如果故障了根据投票数自动将从库转换为主库。

哨兵模式是一种特殊的模式,首先Redis提供了哨兵的命令,哨兵是一一个独立的进程,作为进程,它会独立运行。其原理是**哨兵通**
**过发送命令,等待Redis服务器响应,从而监控运行的多个Redis实例。**

![image-20201010213326390](/image-20201010213326390.png)

这里的哨兵有两个作用

- 通过发送命令,让Redis服务器返回监控其运行状态,包括主服务器和从服务器。
- 当哨兵监测到master宕机,会自动将slave切换成master ,然后通过发布订阅模式通知其他的从服务器,修改配置文件,让它们切换主机。

然而一个哨兵进程对Redis服务器进行监控,可能会出现问题,为此,我们可以使用多个哨兵进行监控。各个哨兵之间还会进行监控,这样就形成了多哨兵模式。

![](/image-20201010213326390.png)



假设主服务器宕机,销兵1先检测到这个结果,系统并不会马上进行ailover过程,仅仅是哨兵1主观的认为主服务器不可用,这个
现象成为**主观下线**。当后面的哨兵也检测到主服务器不可用,并且数量达到一定值时,那么哨兵之间就会进行- -次投票 ,投票的结
果由一个哨兵发起,进行failover[故障转移]操作。切换成功后, 就会通过发布订阅模式,让各个哨兵把自己监控的从服务器实现切
换主机，这个过程称为**客观下线。**





> 测试

目前状态 一主二从

1、配置哨兵配置文件 sentinel.conf

```bash
# sentinel monitor 监控的名称 host port 1
sentinel monitor myredis 127.0.0.1 6379 1
```

后面这个数字 1，代表主机挂了，slave投票看谁接替成为主机，票数最多，就会成为主机

2、启动哨兵

```bash
[root@localhost bin]# ./redis-sentinel myredisconfig/sentinel.conf 
4382:X 10 Oct 2020 21:45:41.544 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
4382:X 10 Oct 2020 21:45:41.544 # Redis version=5.0.9, bits=64, commit=00000000, modified=0, pid=4382, just started
4382:X 10 Oct 2020 21:45:41.544 # Configuration loaded
4382:X 10 Oct 2020 21:45:41.545 * Increased maximum number of open files to 10032 (it was originally set to 1024).
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.9 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26379
 |    `-._   `._    /     _.-'    |     PID: 4382
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

4382:X 10 Oct 2020 21:45:41.546 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
4382:X 10 Oct 2020 21:45:41.546 # Sentinel ID is 062ab5d6371a1152589781498896a35ec4474076
4382:X 10 Oct 2020 21:45:41.546 # +monitor master myredis 127.0.0.1 6379 quorum 1
4382:X 10 Oct 2020 21:46:02.319 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
4382:X 10 Oct 2020 21:46:12.336 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6379

```

如果 Master 节点断开了，这个时候就会从从机中随机选择一个服务器(这里有一个投票算法)

![image-20201010214824262](/image-20201010214824262.png)



哨兵日志

![image-20201010214925803](/image-20201010214925803.png)

如果主机此时回来了，只能归并到新的主机下，当作从机，这就是哨兵模式的规则

> 哨兵模式

优点：

1、哨兵集群,基于主从复制模式,所有的主从配置优点，它全有

2、主从可以切换,故障可以转移,系统的可用性就会更好

3、哨兵模式就是主从模式的升级，手动到自动,更加健壮!

缺点：

1、Redis 不好在线扩容，集群容量一旦达到上限，在线扩容就十分玛法

2、哨兵模式的配置其实还是很麻烦，里面有很多选择

> 哨兵模式全部配置

```bash
# Example sentine1. conf

#哨兵sentine1实例运行的端口 默认26379
port 26379

#哨兵sentine1的工作目录
dir /tmp

#哨兵sentine1监控的redis主节点的ip port
# master-name 可以自己命名的主节点名字只能由字母A-Z、 数字0-9、这三个字符".- ."组成。 
# quorum 配置多少个sentine1哨兵统- -认为master 主节点失联那么这时客观上认为主节点失联了
# sentine1 monitor <master-name> <ip> <redis-port> <quorum>
sentinel monitor mymaster 127.0.0.1 6379 2

#当在Redi s实例中开启了requirepass foobared 授权密码这样所有连接Redi s实例的客户端都要提供
#设置哨兵sentine1 连接主从的密码注意必须为主从设置一 样的验证密码
# sentine1 auth-pass <master-name> <password>
sentine1 auth-pass mymaster MySUPER --secret -0123passwOrd

#指定多少毫秒之后主节点没有应答哨兵sentine1此时哨兵主观上认为主节点下线默认30秒
# sentinel down -after-milliseconds <master-name> <milliseconds>
sentine1 down-after-mi 11i seconds mymaster 30000

#这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步，
这个数字越小，完成failover所需 的时间就越长，
但是如果这个数字越大，就意味着越多的slave因为replicati on而不可用。
可以通过将这个值设为1来保证每次只有一个s1ave处于不能处理命令请求的状态。
# sentine1 paral1e1-syncs <master-name> <numslaves>
sentine1 para1l1el-syncs mymaster 1

#故障转移的超时时间fai lover-timeout 可以用在以下这些方面:
#1.同一个sentine1对同一 个master两次fai lover之间的间隔时间。
#2.当一个slave从一 个错误的master那里同步数据开始计算时间。直到s lave被纠正为向正确的master那里同步数据时。
#3.当想要取消一个正在进行的failover所需要的时间。
#4.当进行failover时，配置所有slaves 指向新的master所需的最大时间。不过，即使过了这个超时，slaves 依然会被正确配
master,但是就不按paral1e1-syncs所配置的规则来了
#默认三分钟
# sentinel failover-timeout <master-name> <mi11i seconds>
sentine1 fai1 over-ti meout mymaster 180000

# SCRIPTS EXECUTION

#配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。
#对于脚本的运行结果有以下规则:
#若脚本执行后返回1，那么该脚本稍后将会被再次执行，重复次数目前默认为10
#若脚本执行后返回2，或者比2更高的一-个返回值，脚本将不会重复执行。
#如果脚本在执行过程中由于收到系统中断信号被终止了，则同返回值为1时的行为相同。
#一个脚本的最大执行时间为60s，如果超过这个时间，脚本将会被- - 个SIGKILL信号终止，之后重新执行。

#通知型脚本:当sentinel有任何警告级别的事件发生时(比如说redis实例的主观失效和客观失效等等)，将会去调用这个脚木，这时这个
#脚本应该通过邮件，SMS 等方式去通知系统管理员关于系统不正常运行的信息。调用该脚本时，将传给脚本两个参数，一个是事件的类型，一
#个是事件的描述。如果sentine1. conf配置文件中配置了这个脚本路径，那么必须保证这个脚本存在于这个路径，并且是可执行的，否则
#sentine1无法正常启动成功。
#通知脚本
# sentinel notificati on-script <master-name> <script-path>
sentine1 notification-script mymaster /var/redis/notify. sh 

#客户端重新配置主节点参数脚本
#当一个master由于fai lover而发生改变时，这个脚本将会被调用，通知相关的客户端关于maste r地址:已经发生改变的信息。
#以下参数将会在调用脚本时传给脚本:
# <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
#目前<state>总是"“failover”,
# <role>是“eader"或者“observer"中的一 一个。
#参数from-ip， from-port, to-ip， to-port是用来和旧的master和新的master (即旧的slave)通信的
#这个脚本应该是通用的，能被多次调用，不是针对性的。
# sentine1 client-reconfig-script <master-name> <script-path>
sentine1 notificati on-script mymaster /var/redis/notify.sh

#客户端重新配置主节点参数脚本!
#当一个master由 于failover而发生改变时，这个脚本将会被调用，通知相关的客户端关于master地址:已经发生改变的信息。
#以下参数将会在调用脚本时传给脚本:
# <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
#目前<state> 总是“failover",
# <role> 是“Teader"或者“observer"中的-一个。
#参数from-ip， from-port， to-ip， to-port是用来和旧的master和新的master(即旧的slave)通信的
#这个脚本应该是通用的，能被多次调用，不是针对性的。
# sentinel client-reconfig-script <master-name> <script-path>
sentinel client-reconfi g-script mymaster /var/redis/reconfig. sh

```



# Redis缓存穿透和雪崩

服务器的高可用

在这里我们不会详细的区分析解决方案的底层(专题)

Redis缓存的使用,极大的提升了应用程序的性能和效率,特别是数据查询方面。但同时,它也带来了一些问题。其中,最要害的
问题，就是数据的- -致性问题,从严格意义上讲,这个问题无解。如果对数据的一致性要求很高,那么就不能使用缓存。

另外的一一些典型问题就是,缓存穿透、缓存雪崩和缓存击穿。目前,业界也都有比较流行的解决方案。

![image-20201010221147975](/image-20201010221147975.png)



### 缓存穿透

> 概念

缓存穿透的概念很简单,用户想要查询-一个数据,发现redis内存数据库没有,也就是缓存没有命中,于是向持久层数据库查询。发
现也没有,于是本次查询失败。当用户很多的时候,缓存都没有命中(秒杀！！),于是都去请求了持久层数据库。这会给持久层数据库造成很
大的压力,这时候就相当于出现了缓存穿透。

> 解决方案

#### 布隆过滤器

布隆过滤器是一种数据结构, 对所有可能查询的参数以hash形式存储,在控制层先进行校验,不符合则丢弃,从而避免了对底层存
储系统的查询压力;

![image-20201010221253359](/image-20201010221253359.png)

但是这种方法会存在两个问题:
1、如果空值能够被缓存起来,这就意味着缓存需要更多的空间存储更多的键,因为这当中可能会有很多的空值的键;
2、即使对空值设置了过期时间,还是会存在缓存层和存储层的数据会有一段时间窗口的不一致,这对于需要保持- -致性的业务会
有影响。

### 缓存击穿

微博服务器宕机()

> 概述

这里需要注意和缓存击穿的区别,缓存击穿,是指-个key非常热点,在不停的扛着大并发,大井发集中对这一一个点进行访问,当
这个key在失效的瞬间,持续的大并发就穿破缓存,直接请求数据库,就像在-一个屏障上凿开了-一个洞。

当某个key在过期的瞬间,有大量的请求并发访问,这类数据一-般是热点数据,由于缓存过期,会同时访问数据库来查询最新数
据，并且回写缓存,会导使数据库瞬间压力过大。





> 解决方案

#### 设置热点数据永不过期

从缓存层面来看,没有设置过期时间,所以不会出现热点key过期后产生的问题。

#### 加互斥锁

分布式锁:使用分布式锁,保证对于每个key同时只有一一个线程去查询后端服务,其他线程没有获得分布式锁的权限,因此只需要
等待即可。这种方式将高并发的压力转移到了分布式锁,因此对分布式锁的考验很大。

![image-20201010221915379](/image-20201010221915379.png)

### 缓存雪崩

> 概念

缓存雪崩,是指在某一个时间段,缓存集中过期失效。Redis 宕机!

产生雪崩的原因之- - ,比如在写本文的时候,马上就要到双十二零点,很快就会迎来-波抢购,这波商品时间比较集中的放入了缓
存，假设缓存-个小时。那么到了凌晨- -点钟的时候 ,这批商品的缓存就都过期了。而对这批商品的访问查询,都落到了数据库
上,对于数据库而言,就会产生周期性的压力波峰。于是所有的请求都会达到存储层,存储层的调用量会暴增,造成存储层也会挂
掉的情况。

![image-20201010222150352](/image-20201010222150352.png)

其实集中过期,倒不是非常致命,比较致命的缓存雪崩,是缓存服务器某个节点宕机或断网。因为自然形成的缓存雪崩，- -定是在
某个时间段集中创建缓存,这个时候,数据库也是可以顶住压力的。无非就是对数据库产生周期性的压力而已.而缓存服务节点的
宕机,对数据库服务器造成的压力是不可预知的,很有可能瞬间就把数据库压垮。

> 解决方案

#### redis 高可用

这个思想的含义是,既然redis有可能挂掉,那我多增设几台redis ,这样- -台挂掉之 后其他的还可以继续工作。其实就是搭建的集
群。

#### 限流降级.

这个解决方案的思想是,在缓存失效后,通过加锁或者队列来控制读数据库写缓存的线程数量，比如对某个key只允许-个线程查
询数据和写缓存,其他线程等待。

#### 数据预热

数据加热的含义就是在正式部署之前,我先把可能的数据先预先访问一遍,这样部分可能大量访问的数据就会加载到缓存中。在即
将发生大并发访问前手动触发加载缓存不同的key ,设置不同的过期时间,让缓存失效的时间点尽量均匀。



> 小结

参考文档 :遇见狂胜说 https://www.bilibili.com/video/BV1S54y1R7SB