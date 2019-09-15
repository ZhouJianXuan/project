# graduation-project
为了维护大家的共同的利益，请把模块划分好，放在所属模块，禁止修改其他人的模块,禁止修改、删除公共模块
为了包名不冲突，请把包名命名为com.sise.graduation.[模块名].[自己项目名]后面命名任意
不要随意的改动包名，共同利益靠大家来维护

# 快速启动
在graduation-web模块下面创建自己的私有化模块,maven项目
引入graduation-common-web模块然后在添加spring-boot启动类就可以

### 内置登录模块
登录api: `/login/loginin`

登出api: `/login/logout`

预登陆api: `/login/preLoginin`

验证码api: `/login/getVerifyCode`



### 登录权限注解

`@AccessAuthority`

### 管理员和普通管理员使用权限注解

`@AccessRolePermission`

# 模块说明
请把你们的功能模块独立出来做,如果有别人开发好的就自己引入依赖。
如果就自己开发,自己开发请把模块抽出来开发，这个项目只是为了大家方便，请不要自私写到自己私有化的模块，谢谢合作