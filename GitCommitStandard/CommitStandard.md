## Git 提交规范

### 一、前言
Commit message 需要有统一的规范这个共识无需赘言。
目前，社区有多种 Commit message 的写法规范。本规范基于AngularJS Git Commit Message Conventions ，取其大部分规范约定而成。
### 二、Commit message 的格式
Commit message 基本格式如下：
包括三个部分：Header，Body 和 Footer。
`<type>(<scope>) : <subject>
<空行>
<body>
<空行>
<footer>`
其中，Header 是必需的，Body 和 Footer 可以省略。
不管是哪一个部分，任何一行都不得超过72个字符（或100个字符）。这是为了避免自动换行影响美观。<br>
<font color=#FF4500>空行用于区分commit信息的header和body，有的时候可能只需要显示header，有的时候则希望看到详细的信息，没有空行就没法区分了
比如 git log --oneline就只显示commit message的header，又比如在github上，在commit list页面，默认也是只显示header </font><br>
 以下是一次git commit message示例：<br>
 
 	fix(locationAvailable): 修复判断定位是否开启可用的bug
 
	原有的【定位是否开启】判断逻辑只判断了手机是否开启了定位服务开关
	【定位是否开启】不仅要判断硬件是否开启了定位服务，还要判断该应用是否获取了定位授权
 
	fix MTCS-821
#### 2.1 Header
Header部分只有一行，包括三个字段：type（必需）、scope（可选）和subject（必需）。<br>
（1）type<br>
type用于说明 commit 的类别，只允许使用下面7个标识。<br>

- feat：新功能（feature
- fix：修补bug
- docs：文档（documentation）
- style： 格式（不影响代码运行的变动）
- refactor：重构（即不是新增功能，也不是修改bug的代码变动）
- test：增加测试
- chore：构建过程或辅助工具的变动
如果type为feat和fix，则该 commit 将肯定出现在 Change log 之中。其他情况（docs、chore、style、refactor、test）由你决定，要不要放入 Change log，建议是不要。<br>
（2）scope<br>
scope用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同。比如待会儿要推荐的工具commitizen给出了几个 如：location 、browser、compile，<br>
不过我推荐使用<br>
       all ：表示影响面大 ，如修改了网络框架，会对整个程序产生影响<br>
       location： 表示影响小，某个小小的功能<br>
       module(是具体某个模块名称)：表示会影响某个模块 如登录模块、首页模块 、用户管理模块等等<br>
（3）subject<br>
subject是 commit 目的的简短描述，不超过50个字符。<br>
以动词开头，使用第一人称现在时，比如change，而不是changed或changes<br>
第一个字母小写<br>
结尾不加句号（.）<br>
我们团队都是中国人，直接使用中文即可，也以动词开头，使用祈使语句。O(∩_∩)O哈哈~<br>
#### 2.2 Body
Body 部分是对本次 commit 的详细描述，可以分成多行。下面是一个范例。<br>
More detailed explanatory text, if necessary.  Wrap it to <br>
about 72 characters or so. Further paragraphs come after blank lines.- Bullet points are okay, too- Use a hanging indent<br>
有两个注意点。<br>
（1）使用第一人称现在时，比如使用change而不是changed或changes。<br>
（2）应该说明代码变动的动机，以及与以前行为的对比。<br>
#### 2.3 Footer
Footer 部分我们可只用于一种情况。<br>
关闭 Issue<br>
如果当前 commit 针对某个issue，那么可以在 Footer 部分关闭这个 issue 。<br>
Closes #234。 <br>
也可以一次关闭多个 issue 。<br>
Closes #123, #245, #992<br>
详细可见[GitHub关于关闭issue的描述](https://help.github.com/articles/closing-issues-via-commit-messages/)  <br>
#### 2.4 Revert
还有一种特殊情况，如果当前 commit 用于撤销以前的 commit，则必须以revert:开头，后面跟着被撤销 Commit 的 Header。<br>
revert: feat(pencil): add 'graphiteWidth' option<br>

This reverts commit 667ecc1654a317a13331b17617d973392f415f02.<br>
Body部分的格式是固定的，必须写成This reverts commit &lt;hash>.，其中的hash是被撤销 commit 的 SHA 标识符。<br>
如果当前 commit 与被撤销的 commit，在同一个发布（release）里面，那么它们都不会出现在 Change log 里面。如果两者在不同的发布，那么当前 commit，会出现在 Change log 的Reverts小标题下面。<br>
### 三、Commit message 提交实操
永远不在 git commit 上增加 -m <msg> 或 --message=<msg> 参数，而单独写提交信息
一个不好的例子 git commit -m "Fix login bug"。<br>
提交的时候别加 -m，这样就会出现一个编辑页面，即可按照提交规范进行编辑提交信息。<br>
运行命令 git config –global core.editor vim 可以修改编辑器为vim。当然你也可以换成自己熟悉的编辑器。<br>
#### 四、Commit message 提交小工具Commitizen
 每次调出编辑器，自己编辑信息比较麻烦？有Commitizen小工具前来帮忙。<br>
Commitizen是一个撰写合格 Commit message 的工具。该工具是基于Node的，因此我们首先必须先安装node环境。<br>
使用Homebrew可以轻松安装node。安装Homebrew命令如下：<br>
`$ ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/{/install)"`<br>
安装好Homebrew之后，执行下面命令安装node<br>
`$ brew install node`<br>
node安装后，会附带npm工具。 npm即node package manager，用来搜索、下载、管理Node.js相关的套件。<br>
安装Commitizen命令如下：<br>
`$ npm install -g commitizen`<br>
全局下载 规范模板文件<br>
`$ npm install -g cz-conventional-changelog`<br>
其中 cz-conventional-changelog 这个东西就是AngularJS的规范 <br>
`$ echo '{ "path": "cz-conventional-changelog" }' > ~/.czrc`
此时你当前用户的根目录下应该可以看到一个  .czrc的文件了.<br>
好了，安装完成。<br>
以后，凡是用到git commit命令，一律改为使用git cz。这时，就会出现选项，用来生成符合格式的 Commit message。<br>
<font color=#FF4500>在Commitizen的编辑页面编辑信息时，需要进行换行的地方请使用\n，不要直接按Enter键（Enter键会进入下一个步骤）。</font>

### 五、从commit生成Change log
commit message规范之后可以直接从commit生成Change log。<br>
关于如何生成Change log日后单独开个wiki讲述。<br>

### 六、总结
此commit message规范，团队成员务必严格执行。<br>


