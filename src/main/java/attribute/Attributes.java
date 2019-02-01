package attribute;

import io.netty.util.AttributeKey;
import session.Session;

public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login"); //是否登录标志位

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session"); //绑定channel上的用户session属性
}
