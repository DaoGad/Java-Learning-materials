---
typora-root-url: img
---

# Git & GitHub

## Git

### 1.你认为git 的优势是在哪里

 	![image-20201009085359028](image-20201009085359028.png)

###  2.Git提交的流程

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



### 3.什么是Git的签名？有什么作用

作用：区分不同人员的级别

​			项目级别/仓库级别：仅在当前本地库范围内有效(**电脑本机)**

​				命令：git config user.name Mint

​							git config user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系统内的范围(**电脑用户)**

​				命令：git config  **--global** user.name Mint

​							git config  **--global** user.email Mint@qq.com

​			系统级别/用户级别：登录当前操作系

![image-20201009095606237](image-20201009095606237.png)

### 4.Git的工作是什么？

Git 的工作就是创建和保存你的项目的快照及与之后的快照进行对比。

### 5.你理解的HEAD指针有什么作用？

## 实战Git

### 1.无法pull远程仓库？

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

#### 结论

Git有一个称为存储区的区域，您可以在其中临时存储所做更改的快照，而无需将更改提交到存储库。 它与工作目录，暂存区或存储库分开

#### 资料

https://blog.csdn.net/cumi6497/article/details/108153099

https://blog.csdn.net/liuchunming033/article/details/45368237

https://blog.csdn.net/Chad97/article/details/88529647

### 2.由于您没有合并的文件，因此无法提交。edit java

之前一直写成git add. 错了。

```
流程：
	1. 指定当前目录为git仓库，初始化
	$  git init newrepo
	2. 开始追踪文件，添加到缓存区暂存区
	$  git add * (or/梦琪)
	3. 提交本地仓库
	$  git commit -m "简介" 指定文件
	4. 提交远程仓库
	$  git push
	5. 拉取远程
	$  git pull
```



















