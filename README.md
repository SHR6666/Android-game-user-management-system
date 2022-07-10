# Android-game-user-management-system
android端用户登录游戏时需要进行登录和注册以上传至数据库后方可进入游戏界面游玩

1.2 设计思路

设计简单的 APP。APP 中至少包括 5 个以上的 Activity 页面。要求有数据库连接，
并进行简单的交互。至少包括以下控件和元素：文本框、按钮、下拉列表、单选按钮、
复选按钮、文本、ListView 或 GridView 等。

2．APP 功能

该 APP 包含 6 个 Activity，拥有着用户管理信息系统同时以打开俄罗斯方块小游
戏，主活动页是 LogiActivity 活动，进行着 SQL 的查询操作，而 MainActivity 活动
页是属于俄罗斯方块游戏区页；注册活动页 RegisterActivity 是进行着数据库增加操
作；忘记密码活动页 ForgotInfoActivity 进行着 SQL 的修改密码操作；注销用户活动
页 DeleteActivity 进行 SQL 的删除操作；到用户登录临时界面 UserInfoActivity 则有
着停留欢迎登录者。

2.1 登录活动 LoginActivity

运行结果如图 2-1 所示。在文本框里填写用户名和密码后，如果用户名和密码与
数据库内容相匹配，则进入主页面。否则，提示“账户或密码错误请重新输入”。如
果数据库里没有密码，保持文本框为空时，单击登录按钮也可以进入主页，同时设置
了注册活动页、忘记密码活动页和注销号活动页。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/Login.png)

图 2-1 首页登录图


2.2 注册活动 RegisterActivity
运行结果如 2-2 所示。在进入 Tetris app 时如果没有密码则无法进入，若需要
拥有账号则需要进行注册操作，当用户输入注册信息时提交就是上传到本地的数据库
之中为之提供登录服务，反之则空密码则不提供登录服务，故此注册活动页与登录活
动页有着承前启后的关系，不可变更，所以用户要进行注册操作，当注册账号时，就
会把账号上传到 SQL 中。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/register.png)

图 2-2 用户注册图


2.3 忘记密码活动页 ForgotInfoActivity
运行结果如图 2-3 所示。当用户忘记密码时会遇到无法登录的情况，而这时就需
要提供为用户能继续登录的设置，即更改密码，而用户只需要记得自动注册时的用户
名即可提供修改密码服务，若忘记了自己的用户名称则将无法进行密码修改操作。当
进行用户的密码修改时就会把提交的新密码替换掉旧密码，即 SQL 的更新或修改操
作。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/forget.png)

图 2-3 重置密码活动页



2.4 注销账户活动 DeleteActivity
运行结果如图 2-4 所示。当用户不再需要本游戏账号时则进行用户数据删除，用
户需要输入账号与密码后点击提交，即把 SQL 中的用户信息抹去。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/detele.png)

图 2-4 注销账号活动页



2.5 用户信息显示活动 UsersInfoActivity
运行程序如图 2-5 所示。当用户在登录界面输入正确的账号与密码时点击登录后，会
出现一个临时的用户信息窗口，且会显示用户信息后停留三秒钟跳转到俄罗斯方块游
戏界面。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/stay.png)

图 2-5 用户信息停留页



2.6 游戏活动 MainActivity
游戏活动页如图 2-6 所示。当停留时间结束之后会自动跳转到游戏活动页之中，
这里通过定义迪卡尔坐标系后即定义地图大小，方块的样式，下落时间与暂停开始游
戏结束等操作来执行；规定左移、右移、变形、快速下落的按钮来执行游戏的运行逻
辑。且要执行下落后停留低部，定义方块的界限，规定方块的旋转中心，且同时下落
后停留完成时再定义一个随机新生成的新方块且同时定义方块的堆叠。完成堆叠后也
要再定义游戏的停止时不可旋转、下落、左移、右移。设置好之后再进行消行逻辑。
消行要根据坐标系进行底部-1。且是当行满时消行，上行下沉。

[image text]!
(https://github.com/SHR6666/Android-game-user-management-system/blob/main/Screenshots/main.png)

图 2-6 游戏活动图



3．数据库设计
Sqlite 数所库名为 Data.db。共 1 个表，表名为：usertable。usertable 表记录了用
户的详细情况，表结构如下表 3.1 所示。
表 3.1 tb_account 表结构
列名        类型     是否为主键    备注
id        integer      是       自增一
username   text      用户名
password   text      用户密码
