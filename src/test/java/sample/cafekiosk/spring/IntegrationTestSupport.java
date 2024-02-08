package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.mail.MailSendClient;

//서버가 새로 띄워지는 시간적 비용을 줄이기위해 상위클래스로 생성한다.
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
    @MockBean
    protected MailSendClient mailSendClient;
}
