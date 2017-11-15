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

           public BARWhenDsl cashCreatePayment(CashPaymentInstructionDto.CashPaymentInstructionDtoBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/cash");
                return this;
            }

            public BARWhenDsl chequeCreatePayment(ChequePaymentInstructionDto.ChequePaymentInstructionDtoBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/cheques");
                return this;
            }

            public BARWhenDsl postalOrdersCreatePayment(PostalOrderPaymentInstructionDto.PostalOrderPaymentInstructionDtoBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/postal-orders");
                return this;
            }

            public BARWhenDsl allPayCreatePayment(AllPayPaymentInstructionDto.AllPayPaymentInstructionDtoBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/allpay");
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

            public BARThenDsl then() {
                return new BARThenDsl();
            }
        }

        public class BARThenDsl {
            public BARThenDsl notFound() {
                response.then().statusCode(404);
                return this;
            }

            public BARThenDsl BadRequest() {
                response.then().statusCode(400);
                return this;
            }

            public BARThenDsl cashCreated(Consumer<CashPaymentInstructionDto> barAssertions) {
                CashPaymentInstructionDto cashPaymentDto = response.then().statusCode(201).extract().as(CashPaymentInstructionDto.class);
                barAssertions.accept(cashPaymentDto);
                return this;
            }

            public BARThenDsl chequeCreated(Consumer<ChequePaymentInstructionDto> barAssertions) {
                ChequePaymentInstructionDto chequePaymentDto = response.then().statusCode(201).extract().as(ChequePaymentInstructionDto.class);
                barAssertions.accept(chequePaymentDto);
                return this;
            }

            public BARThenDsl postalOrdersCreated(Consumer<PostalOrderPaymentInstructionDto> barAssertions) {
                PostalOrderPaymentInstructionDto postalOrdersPaymentDto = response.then().statusCode(201).extract().as(PostalOrderPaymentInstructionDto.class);
                barAssertions.accept(postalOrdersPaymentDto);
                return this;
            }

            public BARThenDsl allPayCreated(Consumer<AllPayPaymentInstructionDto> barAssertions) {
                AllPayPaymentInstructionDto AllPayPaymentInstructionDto = response.then().statusCode(201).extract().as(AllPayPaymentInstructionDto.class);
                barAssertions.accept(AllPayPaymentInstructionDto);
                return this;
            }

            public <T> BARThenDsl got(Class<T> type, Consumer<T> assertions) {
                T dto = response.then().statusCode(200).extract().as(type);
                assertions.accept(dto);
                return this;
            }
        }

    }

