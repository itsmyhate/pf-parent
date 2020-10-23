package com.pf.plat.exchange.socket.handler;

import com.pf.util.ApplicationContextUtil;
import com.pf.plat.exchange.TcpDispacher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @ClassName : ReconnectHandler
 * @Description : 
 * @Author : wangjie
 * @Date: 2020/10/16-9:48
 */
@Slf4j
public class JsonClientHandler extends SimpleChannelInboundHandler<String> {

    private final List<String> extendNo;

    private final String matchingRule;

    private TcpDispacher tcpDispacher;

    private String initResData;

    public JsonClientHandler(List<String> extendNo, String matchingRule) {
        this.extendNo = extendNo;
        this.matchingRule = matchingRule;
    }

    public JsonClientHandler(List<String> extendNo, String matchingRule, String initResData) {
        this.extendNo = extendNo;
        this.matchingRule = matchingRule;
        this.initResData = initResData;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        super.channelRegistered(ctx);
        tcpDispacher = ApplicationContextUtil.getBean(TcpDispacher.class);
        /*log.info("Channel注册到EventLoop上，可以处理I/O");*/
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if( StringUtils.isNotBlank(initResData) ) {
            ctx.write(initResData);
        }
        log.info("Channel是连接/绑定就绪;初始请求:{}", initResData);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception{
        log.info("\r\n从Channel中读数据,数据分发标识号{},消息内容:{}",extendNo, msg);
        tcpDispacher.messageRecived(extendNo, matchingRule, msg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        super.channelReadComplete(ctx);
        /*log.info("读操作完成");*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        // Close the connection when an exception is raised.
        super.exceptionCaught(ctx, cause);
        log.info("Channel异常");
        cause.printStackTrace();
        ctx.close();
    }

}