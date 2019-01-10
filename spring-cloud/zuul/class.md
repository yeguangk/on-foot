# 类解析
Spring Controller 解析：http://www.bubuko.com/infodetail-1176622.html </br>
ZuulServlet 继承 ServletWrappingController，ServletWrappingController 实现了 Controller 及继承 WebContentGenerator，

- Controller
控制器的基础接口，接收 HttpServletRequest 和 HttpServletResponse 实例，完成 MVC 流程。需要注意的是实现该接口的类必须是线程安全的。
Controller 只有一个接口：<code> ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)；</code>处理请求信息，
处理后返回一个 ModelAndView 对象进行界面渲染。

- WebContentGenerator
Web 的内容生成器，支持 Http 参数的控制，控制 HTTP 缓存信息(CacheControl)。其中requireSession、useExpiresHeader、useCacheControlHeader、
useCacheControlNoStore、cacheSeconds、alwaysMustRevalidate均是可配置的。

- AbstractController
使用模板方法模式，方便子类处理请求和内部请求处理。AbstractController 的抽象类中包含两个方法:
 <code>handleRequest(HttpServletRequest, HttpServletResponse)</code>,
 <code>handleRequestInternal(HttpServletRequest, HttpServletResponse)</code>, 如果请求要求 session，可选的同步调用 HttpSession
 需要实现该方法。
 
- ServletWrappingController
这是一个与 Servlet 相关的控制器，还有一个与 Servlet 相关的控制器是 ServletForwardingController。ServletWrappingController 则是将当前应用中的某个 
Servlet 直接包装为一个 Controller，所有到 ServletWrappingController 的请求实际上是由它内部所包装的这个 Servlet 来处理的。也就是说内部封装的 Servlet 
实例对外并不开放，对于程序的其他范围是不可见的，适配所有的 HTTP 请求到内部封装的 Servlet 实例进行处理。它通常用于对已存 Servlet 的逻辑重用上。
ServletWrappingController 是为了 Struts 专门设计的，作用相当于代理 Struts 的 ActionServlet 请注意，Struts 有一个特殊的要求，因为它解析 web.xml 
找到自己的servlet映射。因此，你需要指定的 DispatcherServlet 作为 “servletName” 在这个控制器 servlet 的名字，认为这样的Struts的DispatcherServlet的映射 
（它指的是ActionServlet的）。
核心方法，实现了父类的抽象方法：
<code>
protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
throws Exception {

this.servletInstance.service(request, response);
return null;
}   
</code> 

 - ZuulServlet
 ZuulServlet 是 Zuul 的核心类，初始化 ZuulRunner 和 zuulFilter 的执行。请求通过 filterChain 传输请求到 ZuulServlet 的 service 中，
 service 方法，方法设置请求的 pre、route、post、error，同时设置 RequestContext
 - 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 

