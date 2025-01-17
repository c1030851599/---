package weibo.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author baibing
 * @project: springboot-socket
 * @package: com.sailing.websocket.common
 * @Description: WebSocket服务端代码，包含接收消息，推送消息等接口
 * @date 2018/12/200948
 */
@Component
@ServerEndpoint(value = "/socket/weChat/{username}")      //接收不同路径的websocket
public class WebSocketWeChatServer {
  private static final Logger LOG = LoggerFactory.getLogger(WebSocketWeChatServer.class);
  //此处是解决无法注入的关键
  private static ApplicationContext applicationContext ;

  //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
  private static AtomicInteger online = new AtomicInteger();

  //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
  private static Map<String,Session> sessionPools = new HashMap<>();


  public static void setApplicationContext(ApplicationContext context) {
    applicationContext = context;
  }


  /**
   * 发送消息方法
   * @param session 客户端与socket建立的会话
   * @param message 消息
   * @throws IOException
   */
  public void sendMessage(Session session, String message) throws IOException {
    if(session != null){
      session.getAsyncRemote().sendText(message);
    }
  }


  /**
   * 连接建立成功调用
   * @param session 客户端与socket建立的会话
   * @param userName 客户端的userName
   */
  @OnOpen
  public void onOpen(Session session, @PathParam(value = "username") String userName){
//    不能通过@Autowrite 自动注入，只能通过 这种方式来或者service服务。
    sessionPools.put(userName, session);
    addOnlineCount();
    LOG.info(userName + "加入webSocket！当前人数为" + online);

//      sendInfo(userName, "连接成功");
  }



  /**
   * 关闭连接时调用
   * @param userName 关闭连接的客户端的姓名
   */
  @OnClose
  public void onClose(@PathParam(value = "name") String userName){
    sessionPools.remove(userName);
    subOnlineCount();
    LOG.info(userName + "断开webSocket连接！当前人数为" + online);
  }

  /**
   * 收到客户端消息时触发（群发）
   * @param message
   * @throws IOException
   */
  @OnMessage
  public void onMessage(String message) throws IOException{
    for (Session session: sessionPools.values()) {
      try {
        sendMessage(session, message);
      } catch(Exception e){
        e.printStackTrace();
        continue;
      }
    }
  }

  /**
   * 发生错误时候
   * @param session
   * @param throwable
   */
  @OnError
  public void onError(Session session, Throwable throwable){
    LOG.info("发生错误");
    throwable.printStackTrace();
  }

  /**
   * 给指定用户发送消息
   * @param userName 用户名
   * @param message 消息
   * @throws IOException
   */
  public void sendInfo(String userName, String message){
    Session session = sessionPools.get(userName);
    try {
      sendMessage(session, message);
    }catch (Exception e){
      e.printStackTrace();
    }
  }


  public static void addOnlineCount(){
    online.incrementAndGet();
  }

  public static void subOnlineCount() {
    online.decrementAndGet();
  }
}