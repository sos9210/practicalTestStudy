package sample.cafekiosk.spring.api.service.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    //@Mock을 사용한 주입은 @ExtendWith(MockitoExtension.class)를 사용해줘야 한다.
    @Mock
    //@Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    //@InjectMocks을 사용한 객체의 생성자 파라미터에 @Mock을 사용한 객체들을 이용해서 생성한다.
    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트1")
    @Test
    void sendMail1() {
        //given

        //위 필드주입방식의 @Mock과 같다
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

 //       MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

//        Mockito.when(mailSendClient.sendEmail(
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class)
//                )
//        ).thenReturn(true);

        //@Spy를 사용하면 원하는 부분만 스터빙하고싶은 아래처럼 사용한다
        Mockito.doReturn(true)
                .when(mailSendClient)
                .sendEmail(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(String.class)
                );



        //when
        boolean result = mailService.sendMail("", "", "", "");

        //then
        Assertions.assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, Mockito.times(1))
                .save(Mockito.any(MailSendHistory.class));
    }
    @DisplayName("메일 전송 테스트2")
    @Test
    void sendMail2() {
        //given
//        Mockito.when(mailSendClient.sendEmail(
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class),
//                        ArgumentMatchers.any(String.class)
//                )
//        ).thenReturn(true);

        BDDMockito.given(mailSendClient.sendEmail(
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class)
        )).willReturn(true);



        //when
        boolean result = mailService.sendMail("", "", "", "");

        //then
        Assertions.assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, Mockito.times(1))
                .save(Mockito.any(MailSendHistory.class));
    }

}