import com.aliyuncs.exceptions.ClientException;
import com.itcast.utils.SMSUtils;
import org.junit.Test;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/25 11:57
 * @description:
 */
public class SMSTest {

    @Test
    public void sendMSg() throws ClientException {
//
//        SMSUtils.sendShortMessage("SMS_178456679", "15797721570", "5201314");

        SMSUtils.sendShortMessage("SMS_166095420","15672836617","haha");
    }
}
