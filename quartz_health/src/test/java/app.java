import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 0:54
 * @description: 测试类
 */
public class app {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("application-jobs.xml");
    }
}
