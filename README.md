## 关于项目
使用java [Netty]("https://netty.io/4.1/api/index.html")通信框架制作的获取斗鱼弹幕/挂机直播间的项目。

## 使用说明
1. 获取弹幕功能示例在com.yijianguanzhu.douyu.barrage#Main类中。

2. 发送弹幕/挂机直播间功能示例在com.yijianguanzhu.douyu.barrage#Main类中。

3. 发送弹幕：使用 `DefaultWebSocketClientConfiguration.pushMessage( cookie, content )` 构建消息后 `writeAndFlush` 即可，内容中的 `\`、`@` 与 `/` 会自动转义（与网页版行为一致）；发送结果可通过监听 `MessageType.CHAT_RES` 获取服务器回执（含 res 状态码、cd 发言冷却、len 弹幕长度上限）。

4. 协议细节与逆向证据详见 [docs/协议逆向-2026-10-REA.md](docs/协议逆向-2026-10-REA.md)（vk 签名源码级验证、网关加密签名算法还原、完整请求链路）。

## 交流
如果你有更好的想法，欢迎加入QQ群：250097154

## 免责声明
1. 本项目仅用于个人学习、研究或欣赏。我们不保证内容的正确性。通过使用本项目内容随之而来的风险与作者无关。

2. 访问者可将本项目提供的内容或服务用于个人学习、研究或欣赏，以及其他非商业性或非盈利性用途，但同时应遵守著作权法及其他相关法律的规定，不得侵犯相关权利人的合法权利。

3. 本项目如侵犯到您的权益，请及时通知作者，予以删除。
