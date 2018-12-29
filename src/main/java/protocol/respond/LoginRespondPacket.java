package protocol.respond;

import lombok.Data;
import protocol.Packet;
import protocol.command.CommandConstants;
@Data
public class LoginRespondPacket extends Packet{

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return CommandConstants.LOGIN_RESPONSE;
    }
}
