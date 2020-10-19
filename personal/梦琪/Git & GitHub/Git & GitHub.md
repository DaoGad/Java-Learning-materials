<<<<<<< HEAD
---
typora-root-url: img
---

# Git 学习

## Git基础

### 1.1你认为git 的优势是在哪里

 	![image-20201009085359028](image-20201009085359028.png)

###  1.2Git提交的流程

![img](https://www.runoob.com/wp-content/uploads/2015/02/git-command.jpg)

区域：

​		workspace:工作区域 

​		stagingarea:暂存区/缓存区

​		local repository:本地仓库

​		repository:远程仓库

```
$ git init    初始化仓库。
$ git add .   添加文件到暂存区。
$ git commit  将暂存区内容添加到仓库中。
```

![](/Snipaste_2020-10-10_10-15-21.png)

### 1.3什么是Git的签名？有什么作用

作用：区分不同人员的级别

​			项目级别/仓库级别：仅在当前本地库范围内有效(**电脑本机)**

​				命令：git config user.name Mint

​							git config user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系统内的范围(**电脑用户)**

​				命令：git config  **--global** user.name Mint

​							git config  **--global** user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系

![image-20201009095606237](image-20201009095606237.png)

### 1.4Git的工作是什么？

Git 的工作就是创建和保存你的项目的快照及与之后的快照进行对比。

### 1.5你理解的HEAD指针有什么作用？

基于索引值HEAD进行前进后退的操作

与 git diff 配合使用可以和历史记录去比较文件的差异，可以比较多个

### 1.6如何切换版本

1.查看分支的所有操作记录  git reflog

2.根据索引进行操作 git resett --hard [索引值]

![](/image-202010100921.png)

### 1.7Git中log和reflog的区别

git log可以显示所有提交过的版本信息，不包括已经被删除的 commit 记录和 reset 的操作

git reflog是显示所有的操作记录，包括提交，回退的操作。一般用来找出操作记录中的版本号，进行回退。

git reflog常用于恢复本地的错误操作。

https://blog.csdn.net/Della0930/article/details/89487914

### 8.如何理解Git的分支

#### 好处

在版本控制中，使用多条分支线同时推进多个任务提高开发效率，各个分支在开发的过程中，如果某一个分支开发失败，不会对其他分支有任何有影响。

## Git原理

### 2.1哈希

​	**git上传的文件通过哈希（一个系列的加密算法）从明文转变成密文，git底层采用的是SHA-1算法**

​	共同点：

- 哈希算法不可逆
- 不管输入数据量有大，得到的加密长度固定
- 哈希算法确定，输入数据确定，输出数据能够保证不变
- 哈希算法确定，输入数据有变化，输出数据一定有变化，而且通常变化很大

#### 2.2.1集中式与分布式管理的区别

​	SVN:增量式的版本控制，不会去保存重复的数据，能够很好的节约服务器的存储空间，非分布式.无网络不可以提交.

​	Git:把数据看作是小型文件系统的一组快照（数据的副本或者备份）每一次重新提交Git都会对当前的所有文件制作一个快照并保存快照的索引。Git的的工作方式可以成为快照流,本地有镜像,无网络时也可以提交到本地镜像,待到有网络时再push到服务器. 

#### 2.2.2详细说明下Git文件的管理机制

Git的提交对象以及其父对象形成了一个链条，跟链条下就是分支。分支的切换实际就是指针的切换

### 2.2GitFow工作流了解过吗

## Git实战

### 3.1无法pull远程仓库？

![image-20201009162103951](image-20201009162103951.png)

![image-20201009162157886](image-20201009162157886.png)

我遇到的是冲突问题，远程仓库修改了很多内容，pull的效果是从远程获取代码并合并本地的版本，相当于git fetch + git merge HEAD集合。但是吧，这东西就出问题了。**最后是，先把本地的内容都放到缓存区，紧接着重新pull，再把缓存区的文件删除**.

```txt
1.先将本地修改存储起来，放到暂存区
$ git stash
2.直接拉去 pull内容
$ git pull 或者 git pull <远程主机名> <远程分支名>:<本地分支名>
3.还原暂存的内容
$ git stash pop stash@{0}
4.清除整个暂存的内容
$ git stash clear
5.删除隐藏的更改
$ git stash drop STASH-NAME
================================
查看自己的stash@{0}
$ git stash list

第二种方法：放弃本地的修改，直接覆盖
$ git reset
$ git pull
```

#### 	结论

​	Git有一个称为存储区的区域，您可以在其中临时存储所做更改的快照，而无需将更改提交到存储库。 它与工作目录，暂存区或存储库分开

#### 	资料

https://blog.csdn.net/cumi6497/article/details/108153099

https://blog.csdn.net/liuchunming033/article/details/45368237

https://blog.csdn.net/Chad97/article/details/88529647

### 3.2由于您没有合并的文件，因此无法提交。

之前一直写成git add. 错了。

```
流程：
	1. 指定当前目录为git仓库，初始化
	$  git init newrepo
	
	2. 开始追踪文件，添加到缓存区暂存区
	$  git add * (or/梦琪)
	
	3. 提交本地仓库/保存仓库的历史记录
	$  git commit -m "first commit提交信息" 指定文件
	
	3.1修改提交信息/只会找上一个提交的日志
	$ git commit --amend
	
	4. 下载远程代码并合并
	$  git pull
	
	5. 上传远程代码并合并
	$  git push origin[远程仓库] master[本地的分支名]
```

#### 注意事项

​	1.正确使用语句 git add . 或者 git add * 或者把*换成具体的路径

​	2.先拉取再提交自己的,先pull再push

​	3.git diff 或者 git status查看冲突(https://blog.csdn.net/weixin_40906761/article/details/102858947)

​	4.第二种方法：

​			https://www.cnblogs.com/zhujiabin/p/10148803.html

​			https://blog.csdn.net/u014022865/article/details/54892326

### 3.3分支的使用？

``` 
1.创建分支 
$ git branch bugFix

2.查看所有分支
$ git branch -v

3.切换分支
$ git checkout [bugFix分支名]

4.合并分支
$ git merge [被合并的分支名字]

5.删除本地分支
$ git branch -D [bugFix分支名]

6.删除服务端分支
$ git push origin –delete [bugFix分支名]

7.克隆远程库
$ git clone

8.拉取远程库
$ git pull
//相当于 fetch 和 merge的结合

9.推送自己的代码 （前提你得是团队的成员）
$ git push

10.回溯历史版本
$ git rest --hard

11.压缩历史
$ git rebase -i

12.更改历史
$ git rebase -i HEAD~2

13.添加远程仓库
$ git remote add
```

#### 分支冲突如何解决？

![](/Snipaste_2020-10-10_09-42-16.png)

### 3.4指定远程仓库

```
1.查看自己定义的远程仓库，显示clone地址
$ git remote -v

2.指定远程仓库
$ git remote add origin[别名] url[http网络地址]
```



# GitHub入门与实践

## 第一章 GitHub世界

### 1.1 什么是GitHub

GitHub是为开发者提供的Git仓库的托管服务，在Git中，开发者将源代码存入名叫Git仓库的资料站并使用，而GitHub则是在网络上提供Git仓库的一项服务。

### 1.2 GitHub提供的主要功能

- 免费建立任意个Git仓库
- 提供公司的Organization账号，可以统一管理账户和权限
- lssue将一个任务或问题分配给一个lssue进行追踪和管理功能。
- Wiki任何人都能随时对一篇文章进行更改并保存，因此可以多人共同完成。改版的历史记录会被保存下。
- Pull Request 向别人仓库提出申请，请求合并

## 第二章 什么是版本管理

### 2.1 诞生背景

Git 属于分散型版本管理系统，是为版本管理而设计的软件。

## 第五章 lssue

### 5.1 查看master分支在最近7天的区别

https://github.com/gcq9527/Java-Learning-materials/tree/master/%E6%A2%A6%E7%90%AA/compare/master@{7.day.ago}...master

### 5.2 查看与指定日期之间的差别

- [ ] 完成
=======
---
typora-root-url: img
---

# Git 学习

## Git基础

### 1.1你认为git 的优势是在哪里

 	![image-20201009085359028](image-20201009085359028.png)

###  1.2Git提交的流程

![img](https://www.runoob.com/wp-content/uploads/2015/02/git-command.jpg)

区域：

​		workspace:工作区域 

​		stagingarea:暂存区/缓存区

​		local repository:本地仓库

​		repository:远程仓库

```
$ git init    初始化仓库。
$ git add .   添加文件到暂存区。
$ git commit  将暂存区内容添加到仓库中。
```

![](/Snipaste_2020-10-10_10-15-21.png)

### 1.3什么是Git的签名？有什么作用

作用：区分不同人员的级别

​			项目级别/仓库级别：仅在当前本地库范围内有效(**电脑本机)**

​				命令：git config user.name Mint

​							git config user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系统内的范围(**电脑用户)**

​				命令：git config  **--global** user.name Mint

​							git config  **--global** user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系

![image-20201009095606237](image-20201009095606237.png)

### 1.4Git的工作是什么？

Git 的工作就是创建和保存你的项目的快照及与之后的快照进行对比。

### 1.5你理解的HEAD指针有什么作用？

基于索引值HEAD进行前进后退的操作

与 git diff 配合使用可以和历史记录去比较文件的差异，可以比较多个

### 1.6如何切换版本

1.查看分支的所有操作记录  git reflog

2.根据索引进行操作 git resett --hard [索引值]

![](/image-202010100921.png)

### 1.7Git中log和reflog的区别

git log可以显示所有提交过的版本信息，不包括已经被删除的 commit 记录和 reset 的操作

git reflog是显示所有的操作记录，包括提交，回退的操作。一般用来找出操作记录中的版本号，进行回退。

git reflog常用于恢复本地的错误操作。

https://blog.csdn.net/Della0930/article/details/89487914

### 8.如何理解Git的分支

#### 好处

在版本控制中，使用多条分支线同时推进多个任务提高开发效率，各个分支在开发的过程中，如果某一个分支开发失败，不会对其他分支有任何有影响。

## Git原理

### 2.1哈希

​	**git上传的文件通过哈希（一个系列的加密算法）从明文转变成密文，git底层采用的是SHA-1算法**

​	共同点：

- 哈希算法不可逆
- 不管输入数据量有大，得到的加密长度固定
- 哈希算法确定，输入数据确定，输出数据能够保证不变
- 哈希算法确定，输入数据有变化，输出数据一定有变化，而且通常变化很大

#### 2.2.1集中式与分布式管理的区别

​	SVN:增量式的版本控制，不会去保存重复的数据，能够很好的节约服务器的存储空间，非分布式.无网络不可以提交.

​	Git:把数据看作是小型文件系统的一组快照（数据的副本或者备份）每一次重新提交Git都会对当前的所有文件制作一个快照并保存快照的索引。Git的的工作方式可以成为快照流,本地有镜像,无网络时也可以提交到本地镜像,待到有网络时再push到服务器. 

#### 2.2.2详细说明下Git文件的管理机制

Git的提交对象以及其父对象形成了一个链条，跟链条下就是分支。分支的切换实际就是指针的切换

### 2.2GitFow工作流了解过吗

## Git实战

### 3.1无法pull远程仓库？

![image-20201009162103951](image-20201009162103951.png)

![image-20201009162157886](image-20201009162157886.png)

我遇到的是冲突问题，远程仓库修改了很多内容，pull的效果是从远程获取代码并合并本地的版本，相当于git fetch + git merge HEAD集合。但是吧，这东西就出问题了。**最后是，先把本地的内容都放到缓存区，紧接着重新pull，再把缓存区的文件删除**.

```txt
1.先将本地修改存储起来，放到暂存区
$ git stash
2.直接拉去 pull内容
$ git pull 或者 git pull <远程主机名> <远程分支名>:<本地分支名>
3.还原暂存的内容
$ git stash pop stash@{0}
4.清除整个暂存的内容
$ git stash clear
5.删除隐藏的更改
$ git stash drop STASH-NAME
================================
查看自己的stash@{0}
$ git stash list

第二种方法：放弃本地的修改，直接覆盖
$ git reset
$ git pull
```

#### 	结论

​	Git有一个称为存储区的区域，您可以在其中临时存储所做更改的快照，而无需将更改提交到存储库。 它与工作目录，暂存区或存储库分开

#### 	资料

https://blog.csdn.net/cumi6497/article/details/108153099

https://blog.csdn.net/liuchunming033/article/details/45368237

https://blog.csdn.net/Chad97/article/details/88529647

### 3.2由于您没有合并的文件，因此无法提交。

之前一直写成git add. 错了。

```
流程：
	1. 指定当前目录为git仓库，初始化
	$  git init newrepo
	
	2. 开始追踪文件，添加到缓存区暂存区
	$  git add * (or/梦琪)
	
	3. 提交本地仓库/保存仓库的历史记录
	$  git commit -m "first commit提交信息" 指定文件
	
	3.1修改提交信息/只会找上一个提交的日志
	$ git commit --amend
	
	4. 下载远程代码并合并
	$  git pull
	
	5. 上传远程代码并合并
	$  git push origin[远程仓库] master[本地的分支名]
```

#### 注意事项

​	1.正确使用语句 git add . 或者 git add * 或者把*换成具体的路径

​	2.先拉取再提交自己的,先pull再push

​	3.git diff 或者 git status查看冲突(https://blog.csdn.net/weixin_40906761/article/details/102858947)

​	4.第二种方法：

​			https://www.cnblogs.com/zhujiabin/p/10148803.html

​			https://blog.csdn.net/u014022865/article/details/54892326

### 3.3分支的使用？

``` 
1.创建分支 
$ git branch bugFix

2.查看所有分支
$ git branch -v

3.切换分支
$ git checkout [bugFix分支名]

4.合并分支
$ git merge [被合并的分支名字]

5.删除本地分支
$ git branch -D [bugFix分支名]

6.删除服务端分支
$ git push origin –delete [bugFix分支名]

7.克隆远程库
$ git clone

8.拉取远程库
$ git pull
//相当于 fetch 和 merge的结合

9.推送自己的代码 （前提你得是团队的成员）
$ git push

10.回溯历史版本
$ git rest --hard

11.压缩历史
$ git rebase -i

12.更改历史
$ git rebase -i HEAD~2

13.添加远程仓库
$ git remote add
```

#### 分支冲突如何解决？

![](/Snipaste_2020-10-10_09-42-16.png)

### 3.4指定远程仓库

```
1.查看自己定义的远程仓库，显示clone地址
$ git remote -v

2.指定远程仓库
$ git remote add origin[别名] url[http网络地址]
```



# GitHub入门与实践

## 第一章 GitHub世界

### 1.1 什么是GitHub

GitHub是为开发者提供的Git仓库的托管服务，在Git中，开发者将源代码存入名叫Git仓库的资料站并使用，而GitHub则是在网络上提供Git仓库的一项服务。

### 1.2 GitHub提供的主要功能

- 免费建立任意个Git仓库
- 提供公司的Organization账号，可以统一管理账户和权限
- lssue将一个任务或问题分配给一个lssue进行追踪和管理功能。
- Wiki任何人都能随时对一篇文章进行更改并保存，因此可以多人共同完成。改版的历史记录会被保存下。
- Pull Request 向别人仓库提出申请，请求合并

## 第二章 什么是版本管理

### 2.1 诞生背景

Git 属于分散型版本管理系统，是为版本管理而设计的软件。

## 第五章 lssue

### 5.1 查看 master 分支在最近7天的区别

https://github.com/gcq9527/Java-Learning-materials/tree/master/%E6%A2%A6%E7%90%AA/compare/master@{7.day.ago}...master

### 5.2 lssue 作用

设立里程碑，用 lssue 管理任务，那么管理 lssue 的系统成为 BTS（ Bug 跟踪管理系统）都可以使用 GFM 描述拥有 Tasklist 语法

出现的场景

- 发现软件的 Bug 并报告
- 有事想问作者探讨
- 事先列出今后准备实施的任务



## 第6章 发送pull Request的准备



>>>>>>> 11b9498acbb674de9f78bfef065d73a1323fcaf0
