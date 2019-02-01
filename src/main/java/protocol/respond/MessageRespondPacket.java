package protocol.respond;

import lombok.Data;
import protocol.Packet;
import protocol.command.CommandConstants;

/**
 * 消息接受对象
 */
@Data
public class MessageRespondPacket extends Packet{

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return CommandConstants.MESSAGE_RESPONSE;
    }
}
