package unit.com.ntnn;

import com.ntnn.api.Request;
import com.ntnn.api.Response;
import com.ntnn.builder.RequestHeaderBuilder;
import com.ntnn.controller.AccountController;
import com.ntnn.service.CreateAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.Callable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;

    @Mock
    private CreateAccountService createAccountService;

    private HttpHeaders httpRequest;

    @BeforeEach
    public void init() {
        httpRequest = RequestHeaderBuilder.defaultBuilder().build();
    }

    @Test
    public void createUser_Success() throws Exception {
        Response response = new Response();
        response.setStatus("Success");
        response.setCode(200);
        Request request = new Request();
        request.setUsername("nam0102ht");
        request.setPhone("+8491929238");
        Mockito.when(createAccountService.createAccount(request)).thenReturn(response);

        Assertions.assertDoesNotThrow(() -> {
            Callable<ResponseEntity<Response>> res = accountController
                    .createUser(httpRequest, request);
            ResponseEntity<Response> result = res.call();
            Assertions.assertEquals( "200 OK", result.getStatusCode().toString());
            Assertions.assertNotNull(result.getBody());
            Assertions.assertEquals(200, result.getBody().getCode());
            Assertions.assertEquals("Success", result.getBody().getStatus());
        });
    }
}
