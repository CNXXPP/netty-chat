package protocol.request;

import lombok.Data;
import protocol.Packet;

import static protocol.command.CommandConstants.MESSAGE_REQUEST;

/**
 * 消息发送对象
 */
@Data
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}