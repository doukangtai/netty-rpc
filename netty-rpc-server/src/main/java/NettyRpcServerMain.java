import com.netty.rpc.framework.annotation.NettyRpcScan;
import com.netty.rpc.framework.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 窦康泰
 * @date 2021/07/01
 */
@NettyRpcScan(basePackages = {"com.netty.rpc.server.serviceImpl"})
public class NettyRpcServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(NettyRpcServerMain.class);
        NettyRpcServer nettyRpcServer = context.getBean(NettyRpcServer.class);
        nettyRpcServer.start();
    }
}
