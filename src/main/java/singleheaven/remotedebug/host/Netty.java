package singleheaven.remotedebug.host;

import com.alibaba.fastjson.JSONObject;
import com.freddy.chat.im.MessageType;
import com.freddy.im.protobuf.MessageProtobuf;
import ioc.Injector;
import jdi.impl.NettyImpl;
import jdi.log.Log;

import java.io.IOException;
import java.util.UUID;

public class Netty {
    final NettyImpl debugCommunication = new NettyImpl("pc-43ed5a0b-6ccd-41f3-8cf5-1f72d804651a", "192.168.3.14", 8090) {
        @Override
        protected boolean isNetworkAvailable() {
            return true;
        }

        @Override
        protected MessageProtobuf.Msg getHandshakeMsg() {
            MessageProtobuf.Msg.Builder builder = MessageProtobuf.Msg.newBuilder();
            MessageProtobuf.Head.Builder headBuilder = builder.getHeadBuilder();
            headBuilder.setMsgId(UUID.randomUUID().toString());
            headBuilder.setMsgType(MessageType.HANDSHAKE.getMsgType());
            headBuilder.setFromId(myId);
            headBuilder.setTimestamp(System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("side", "host");
            jsonObject.put("device_id", "android-27cc6c58-10db-4eee-a77f-6ff24800e56d");
            headBuilder.setExtend(jsonObject.toJSONString());
            return builder.build();
        }

        @Override
        protected void onError(String s) {
        }

        @Override
        protected void onPeerHandshake() {
            // AndroidStudio Eclipse 常用debug端口
            try {
                Log.d("", "可以开始 IDE debug");
                startListening("localhost:5006");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPeerGoodbye() {
            try {
                stopListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        Injector.registerQualifiedClass(Log.ILog.class, LoggerImpl.class);
        Netty netty = new Netty();
        try {
            netty.debugCommunication.start();
            while(true) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            netty.debugCommunication.stop();
        }
    }
}
