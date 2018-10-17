# Markdown

> ## 标题
> 1.     =（最高阶标题）
> 2.     -（第二阶标题）
> 3.     \# 最高阶标题
> 4.     \## 二阶标题   
> 5.     \### 三阶标题
>

> ## 引用区块
> This is a blockquote with two paragraphs. Lorem ipsum dolor sit amet,
> consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus.
> Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.
> 
> 或者 
> 
>
> Donec sit amet nisl. Aliquam semper ipsum sit amet velit. Suspendisse
> id sem consectetuer libero luctus adipiscing.
> 

> ## 列表
> Markdown 支持有序列表和无序列表
>
> *   Red
> *   Green
> *   Blue*_``_*
> 
> 或者
> 
> +   Red
> +   Green
> +   Blue
> 
> 或者
>
> - Red
> - Green
> - Blue
>
> 或者
>
> 1.  Bird
> 2.  McHale
> 3.  Parish
>
> 或者
>
> <ol>
> <li>Bird</li>
> <li>McHale</li>
> <li>Parish</li>
> </ol>
>

> ## 缩进
> *   Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
>     Aliquam hendrerit mi posuere lectus. Vestibulum enim wisi,
>     viverra nec, fringilla in, laoreet vitae, risus.
> *   Donec sit amet nisl. Aliquam semper ipsum sit amet velit.
>     Suspendisse id sem consectetuer libero luctus adipiscing.
>

> ## 代码区块
>  和程序相关的写作或是标签语言原始码通常会有已经排版好的代码区块，通常这些区块
>  我们并不希望它以一般段落文件的方式去排版，而是照原来的样子显示，Markdown 会用 
>  \<pre> 和 \<code> 标签来把代码区块包起来。
>
>  要在 Markdown 中建立代码区块很简单，只要简单地缩进 4 个空格或是 1 个制表符就可以.
>   
>     eg: 这是一个代码区块
>
> ### 角标  
>
> &copy; 2004 Foo Corporation 
> >
> 或者
> >
>   <div class="footer">
>    &copy; 2004 Foo Corporation
>   </div>
> >
> 
> ## 分割线
> 
> * * *
> 
> 或者
> 
> ***
> 
> 或者
> 
> *****
> 
> 或者
> 
> ---
> 
> 
> ## 区段元素
>
> ### 链接
> Markdown 支持两种形式的链接语法： 行内式和参考式两种形式。
>
> 链接文字都是用 [方括号] 来标记。    
> [百度](www.baidu.com)
>     
> 链接到同样主机的资源，你可以使用相对路径    
> [相对路径](./file/word.txt)   
>     
> 参考式的链接是在链接文字的括号后面再接上另一个方括号，而在
> 第二个方括号里面要填入用以辨识链接的标记
> 
> [Reference] [5]  
> 
> [5]: http://www.baidu.com "百度"
> 
> 链接内容定义的形式为
> 
> - 方括号（前面可以选择性地加上至多三个空格来缩进），里面输入链接文字
> - 接着一个冒号
> - 接着一个以上的空格或制表符
> - 接着链接的网址
> - 选择性地接着 title 内容，可以用单引号、双引号或是括弧包着 
>  
> eg: I get 10 times more traffic from [Google] [1] than from
> 
> [1]: http://google.com/        "Google"
> [2]: http://search.yahoo.com/  "Yahoo Search"
> [3]: http://search.msn.com/    "MSN Search"
> >
>

> ## 强调
>
> *Italics*
> 
> _Italics_
>
> **Strong**
> 
> __Strong__
> 
> 

> ## 代码
>
> ``There is a literal backtick (`) here.``
> 

> ## 图片
>
> ![文件地址图片](./file/test.jpg)
> 
> ![Url 地址路径](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539694468179&di=67175854c2d87e8e6dd18f6abbc4d454&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0125fd5770dfa50000018c1b486f15.jpg%401280w_1l_2o_100sh.jpg) 
>
> 详细叙述如下
> 
> - 一个惊叹号 !
> - 接着一个方括号，里面放上图片的替代文字
> - 接着一个普通括号，里面放上图片的网址，
>   最后还可以用引号包住并加上 选择性的 'title' 文字。
>

> ## 反斜杠
>
> Markdown 可以利用反斜杠来插入一些在语法中有其它意义的符号
> 
> - \   反斜线
> - `   反引号
> - \*   星号
> - _   底线
> - {}  花括号
> - []  方括号
> - ()  括弧
> - \#   井字号
> - []  方括号
> - \+   加号
> - \-   减号
> - .   英文句点
> - !   惊叹号
> 





