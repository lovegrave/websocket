package com.dhls.util.websocket.websocket;

import com.alibaba.fastjson.JSONObject;
import com.dhls.util.websocket.link.Global;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PushMessage {

    /**
     * netty 推送
     * @param channelId
     * @param obj
     * @return
     */
    public boolean sendMessage(String channelId,Object obj) {
        Channel channel = Global.get(channelId);
        if(null == channel){
            return false;
        }
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(obj));
        ChannelFuture channelFuture = channel.writeAndFlush(textWebSocketFrame);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                 channelFuture.isSuccess();
            }
        });
        return channelFuture.isSuccess();
    }


    /**
     * netty 推送
     * @param key
     * @param obj
     * @return
     */
    public void pushAll(String key,Object obj) {
        if(key == null){
            key = "ALL";
        }
        List<Channel> channel = Global.getLikeList(key);
        if(null == channel || channel.size() ==0){
            return;
        }
        channel.forEach(a->{
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(obj));
            ChannelFuture channelFuture = a.writeAndFlush(textWebSocketFrame);
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    boolean success = channelFuture.isSuccess();
                }
            });
        });

    }


    public List<String> getChannelIds(){
        return Global.channel.keySet().stream().collect(Collectors.toList());
    }
}
