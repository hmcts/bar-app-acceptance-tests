package uk.gov.hmcts.bar.acceptancetests;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.bar.acceptancetests.dsl.BARTestDsl;
import uk.gov.hmcts.bar.api.contract.AllPayPaymentInstructionDto;
import uk.gov.hmcts.bar.api.contract.CashPaymentInstructionDto;
import uk.gov.hmcts.bar.api.contract.ChequePaymentInstructionDto;
import uk.gov.hmcts.bar.api.contract.PostalOrderPaymentInstructionDto;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.bar.api.contract.AllPayPaymentInstructionDto.allPayPaymentInstructionDtoWith;
import static uk.gov.hmcts.bar.api.contract.CashPaymentInstructionDto.cashPaymentInstructionDtoWith;
import static uk.gov.hmcts.bar.api.contract.ChequePaymentInstructionDto.chequePaymentInstructionDtoWith;
import static uk.gov.hmcts.bar.api.contract.PostalOrderPaymentInstructionDto.postalOrderPaymentInstructionDtoWith;

public class BARPaymentTypeIntegrationTest extends IntegrationTestBase{

    @Autowired
    private BARTestDsl scenario;

    private CashPaymentInstructionDto.CashPaymentInstructionDtoBuilder cashPaymentToCreate = cashPaymentInstructionDtoWith()
            .payerName("Krishna")
            .currency("GBP")
            .amount(100);

   @Test
    public void cashCreatePayments201() throws IOException {
        scenario.given()
                .when().cashCreatePayment(cashPaymentToCreate)
                .then().cashCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Krishna");
                })
        );
    }

    private CashPaymentInstructionDto.CashPaymentInstructionDtoBuilder cashPaymentToCreate400 = cashPaymentInstructionDtoWith()
            .payerName("Krishna")
            .currency("USD")
            .amount(100);

    @Test
    public void cashCreatePayments400() throws IOException {
        scenario.given()
                .when().cashCreatePayment(cashPaymentToCreate400)
                .then().BadRequest();
    }

    private ChequePaymentInstructionDto.ChequePaymentInstructionDtoBuilder chequesPaymentToCreate = chequePaymentInstructionDtoWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .sortCode("000000")
            .accountNumber("00000000")
            .instrumentNumber("000000");


    @Test
    public void ChequeCreatePayments201() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate)
                .then().chequeCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Viswanadh");
                })
        );
    }

    private ChequePaymentInstructionDto.ChequePaymentInstructionDtoBuilder chequesPaymentToCreate400 = chequePaymentInstructionDtoWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .sortCode("0000001")
            .accountNumber("00000000")
            .instrumentNumber("000000");


    @Test
    public void ChequeCreatePayments500() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400)
                .then().BadRequest();
    }

    private ChequePaymentInstructionDto.ChequePaymentInstructionDtoBuilder chequesPaymentToCreate400AN = chequePaymentInstructionDtoWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .sortCode("000000")
            .accountNumber("000000001")
            .instrumentNumber("000000");


    @Test
    public void ChequeCreatePayments500AN() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400AN)
                .then().BadRequest();
    }

    private ChequePaymentInstructionDto.ChequePaymentInstructionDtoBuilder chequesPaymentToCreate400IN = chequePaymentInstructionDtoWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .sortCode("000000")
            .accountNumber("00000000")
            .instrumentNumber("0000001");


    @Test
    public void ChequeCreatePayments500IN() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400IN)
                .then().BadRequest();
    }

    private PostalOrderPaymentInstructionDto.PostalOrderPaymentInstructionDtoBuilder postalOrderPaymentToCreate = postalOrderPaymentInstructionDtoWith()
            .payerName("Anish")
            .amount(200)
            .currency("GBP")
            .instrumentNumber("000000");

    @Test
    public void PostalOrderCreatePayments201() throws IOException {
        scenario.given()
                .when().postalOrdersCreatePayment(postalOrderPaymentToCreate)
                .then().postalOrdersCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Anish");
                })
        );
    }

    private PostalOrderPaymentInstructionDto.PostalOrderPaymentInstructionDtoBuilder postalOrderPaymentToCreate400 = postalOrderPaymentInstructionDtoWith()
            .payerName("Anish")
            .amount(200)
            .currency("GBP")
            .instrumentNumber("0000001");

    @Test
    public void PostalOrderCreatePayments500() throws IOException {
        scenario.given()
                .when().postalOrdersCreatePayment(postalOrderPaymentToCreate400)
                .then().BadRequest();
    }

    private AllPayPaymentInstructionDto.AllPayPaymentInstructionDtoBuilder allPayPaymentToCreate = allPayPaymentInstructionDtoWith()
            .payerName("Krishna Viswanadh")
            .amount(2000)
            .currency("GBP")
            .allPayTransactionId("12345678901234567890");

    @Test
    public void AllPayCreatePayments201() throws IOException {
        scenario.given()
                .when().allPayCreatePayment(allPayPaymentToCreate)
                .then().allPayCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Krishna Viswanadh");
                })
        );
    }

    private AllPayPaymentInstructionDto.AllPayPaymentInstructionDtoBuilder allPayPaymentToCreate400 = allPayPaymentInstructionDtoWith()
            .payerName("Krishna Viswanadh")
            .amount(2000)
            .currency("GBP")
            .allPayTransactionId("12345678901234567890988888888");

    @Test
    public void AllPayCreatePayments400() throws IOException {
        scenario.given()
                .when().allPayCreatePayment(allPayPaymentToCreate400)
                .then().BadRequest();
    }

    @Test
    public void findAllPaymentTypes() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getPaymentTypes()
                .then().got(List.class, (result -> assertThat(result).isNotEmpty()));
    }
}
