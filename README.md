# SeleniumRequests
## 简介
本工具整理Chrome IE phantomjs JbrowerDriver等selenium浏览器集合
## 使用方法
* 构造器为静态方法,无显示构造器,分为无头有头模式,可选参数为超时时间和代理
```Java
SeleniumRequests requests = ChromeRequests.getSelenium(new ProxyPara("127.0.0.1",8080));//有头模式,设置代理
SeleniumRequests requests = PhantomjsRequests.getHeadlessSelenium(30000);
```
* 常用方法
```Java
requests.get(url);//访问链接
requests.postAjax(url, data);//ajax模式采集数据
requests.close();//关闭浏览器
requests.clearAllCookies();//清除所有
requests.addCookie(cookies, domain);//添加cookie
requests.doNextUntilSomeOneElementShowUp(cssSelector);//某原始出现才进行下一步
requests.executeJavaScript(javascript)//执行js
requests.getBufferedImage(cssSelector)//获得选择器对应的图片
requests.getFixAllRelativeHrefsContent()//获得补全链接的全文
requests.clickSection(xpathOrCssSelector);//点击选择器,选择的部分
requests.move(moveElement, tracks);//移动滑块
requests.screenShot();//截图
requests.scrollDown(distance);//向下滑动屏幕
requests.sendKeysToInput(xpathOrCssSelector, sendKeys);//填写数据到对应的选择器
requests.switchToFrame();//切换到iframe
requests.switchToWindow(match);//切换不同的窗口
```
* 辅助类说明<br>
TrackGenerator滑动轨迹生成
* 所需驱动下载<br>
已经上传到百度云,链接: https://pan.baidu.com/s/1yrnWRWLg4ab9lJGiZMFpyg 提取码: xdbg
* 配置驱动<br>
使用时,需要把对应路径的驱动配置到path.properties
