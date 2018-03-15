### 说明
--------

```
1、分支develop对应svn开发的uat分支
2、分支release/xxx对应svn每周的发布tag
3、feature分支对应redmine的每个需求，上线之后才可删除feature分支
4、feature分支合并develop(uat),release分支，需要管理员做代码审核
5、分支冲突合并需要与相关人员协同解决冲突
```


### 开发说明
--------

```
1、对应旧的svn操作， 每个redmine需求从develop新建一个feature/[0505]-[yuqi]-[redmineID]
2、用户开发完成feature，需要合并到develop(uat)分支，同时保留feature，除非该分支发布上线才可以删除（合并代码需要管理员审核）
3、管理员每周从master新建release/0505分支，对应现在svn的每周发布tag
4、发布之前，各个开发成员将自己的feature申请合并到release分支（管理员进行merge审核）
5、发布上线之后， 管理员需要将release代码合并到master
6、hotfix分支从master拉取， 部署到pre环境进行测试验证
```

### 其它说明
---------

```
1、遵循gitflow原则
2、对于培优差异化，从代码层面处理，尽量维护一条开发主线
3、对于命令chery pick, rebase 等尽量少用或者不用
```
