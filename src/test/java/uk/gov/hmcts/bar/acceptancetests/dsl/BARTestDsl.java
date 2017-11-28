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
import uk.gov.hmcts.bar.api.data.model.AllPayPaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.CashPaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.ChequePaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.PostalOrderPaymentInstruction;

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

           public BARWhenDsl cashCreatePayment(CashPaymentInstruction.CashPaymentInstructionBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/cash");
                return this;
            }

            public BARWhenDsl chequeCreatePayment(ChequePaymentInstruction.ChequePaymentInstructionBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/cheques");
                return this;
            }

            public BARWhenDsl postalOrdersCreatePayment(PostalOrderPaymentInstruction.PostalOrderPaymentInstructionBuilder requestDto) {
                response = newRequest().body(requestDto.build()).post("/postal-orders");
                return this;
            }

            public BARWhenDsl allPayCreatePayment(AllPayPaymentInstruction.AllPayPaymentInstructionBuilder requestDto) {
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

            public BARThenDsl cashCreated(Consumer<CashPaymentInstruction> barAssertions) {
                CashPaymentInstruction cashPaymentInstruction = response.then().statusCode(201).extract().as(CashPaymentInstruction.class);
                barAssertions.accept(cashPaymentInstruction);
                return this;
            }

            public BARThenDsl chequeCreated(Consumer<ChequePaymentInstruction> barAssertions) {
                ChequePaymentInstruction chequePaymentInstruction = response.then().statusCode(201).extract().as(ChequePaymentInstruction.class);
                barAssertions.accept(chequePaymentInstruction);
                return this;
            }

            public BARThenDsl postalOrdersCreated(Consumer<PostalOrderPaymentInstruction> barAssertions) {
                PostalOrderPaymentInstruction postalOrdersPaymentInstruction = response.then().statusCode(201).extract().as(PostalOrderPaymentInstruction.class);
                barAssertions.accept(postalOrdersPaymentInstruction);
                return this;
            }

            public BARThenDsl allPayCreated(Consumer<AllPayPaymentInstruction> barAssertions) {
                AllPayPaymentInstruction allPayPaymentInstruction = response.then().statusCode(201).extract().as(AllPayPaymentInstruction.class);
                barAssertions.accept(allPayPaymentInstruction);
                return this;
            }

            public <T> BARThenDsl got(Class<T> type, Consumer<T> assertions) {
                T dto = response.then().statusCode(200).extract().as(type);
                assertions.accept(dto);
                return this;
            }
        }

    }

