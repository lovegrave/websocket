package com.dhls.util.websocket.link;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Global {
    private Global() {
    }

    public final static ConcurrentHashMap<String,Channel> channel = new ConcurrentHashMap<String,Channel>(32);

    public static void put(String id, Channel socketChannel) {
        channel.put(id, socketChannel);
    }

    public static Channel get(String id) {
        return channel.get(id);
    }

    public static List<Channel> getLikeList(String key){
       return channel.entrySet().stream().filter(Objects::nonNull)
                .map(a->{
                    String[] keys = a.getKey().split(":");
                    if(keys.length >=2){
                        if(keys[1].equalsIgnoreCase(key)){
                            return a.getValue();
                        }
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static void remove(Channel nioSocketChannel) {
        channel.entrySet().stream()
                .filter(entry -> entry.getValue() == nioSocketChannel).forEach(entry -> channel.remove(entry.getKey()));
    }
}
