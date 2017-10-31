package uk.gov.hmcts.bar.acceptancetests.dsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.bar.api.contract.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Scope("prototype")
public class BARTestDsl {
    private final ObjectMapper objectMapper;
    private final Map<String, String> headers = new HashMap<>();
    private final String baseUri;
    private Response response;
//    private final UserTokenFactory userTokenFactory;

    @Autowired
    public BARTestDsl(@Value("${base-urls.bar}") String baseUri ){

        this.objectMapper = new ObjectMapper();
        this.baseUri = baseUri;

    }

    public BARGivenDsl given() {

        return new BARGivenDsl();
    }

    public class BARGivenDsl {

        public BARWhenDsl when() {

            return new BARWhenDsl();
        }
    }
        public class BARWhenDsl {
            private RequestSpecification newRequest() {
                return RestAssured.given().baseUri(baseUri).contentType(ContentType.JSON).headers(headers);
            }

            public BARWhenDsl createPayment(PaymentUpdateDto.PaymentUpdateDtoBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/payments");
                return this;
            }

            public BARWhenDsl getPayments(SearchDto.SearchDtoBuilder requestDto) {
                response = newRequest().body(requestDto.userId("user01").fromDate(LocalDateTime.parse("2017-10-31T00:00:00")).toDate(LocalDateTime.now()).build()).get("/payments");
                return this;
            }

            public BARWhenDsl getBuildInfo() {
                response = newRequest().get("/info");
                return this;
            }

            public BARWhenDsl getPaymentTypes() {
                response = newRequest().get("/payment-types");
                return this;
            }

            public BARWhenDsl getServiceTypes() {
                response = newRequest().get("/services");
                return this;
            }

            public BARThenDsl then() {
                return new BARThenDsl();
            }
        }

        public class BARThenDsl {
            public BARThenDsl notFound() {
                response.then().statusCode(404);
                return this;
            }

            public BARThenDsl created(Consumer<PaymentDto> barAssertions) {
                PaymentDto paymentDto = response.then().statusCode(200).extract().as(PaymentDto.class);
                barAssertions.accept(paymentDto);
                return this;
            }
            public <T> BARThenDsl got(Class<T> type, Consumer<T> assertions) {
                T dto = response.then().statusCode(200).extract().as(type);
                assertions.accept(dto);
                return this;
            }
        }

    }

