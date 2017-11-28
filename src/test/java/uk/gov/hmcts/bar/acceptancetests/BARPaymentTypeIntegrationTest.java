package uk.gov.hmcts.bar.acceptancetests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.bar.acceptancetests.dsl.BARTestDsl;
import uk.gov.hmcts.bar.api.data.model.AllPayPaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.CashPaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.ChequePaymentInstruction;
import uk.gov.hmcts.bar.api.data.model.PostalOrderPaymentInstruction;

import static uk.gov.hmcts.bar.api.data.model.AllPayPaymentInstruction.allPayPaymentInstructionWith;
import static uk.gov.hmcts.bar.api.data.model.PostalOrderPaymentInstruction.postalOrderPaymentInstructionWith;


import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.bar.api.data.model.CashPaymentInstruction.cashPaymentInstructionWith;
import static uk.gov.hmcts.bar.api.data.model.ChequePaymentInstruction.chequePaymentInstructionWith;
public class BARPaymentTypeIntegrationTest extends IntegrationTestBase {

    @Autowired
    private BARTestDsl scenario;

    private CashPaymentInstruction.CashPaymentInstructionBuilder cashPaymentToCreate = cashPaymentInstructionWith()
            .payerName("Krishna")
            .currency("GBP")
            .amount(100);

    @Test
    public void cashCreatePayments201() throws IOException {
        scenario.given()
                .when().cashCreatePayment(cashPaymentToCreate)
                .then().cashCreated((paymentInstruction -> {
                    assertThat(paymentInstruction.getPayerName()).isEqualTo("Krishna");
                })
        );
    }

    private CashPaymentInstruction.CashPaymentInstructionBuilder cashPaymentToCreate400 = cashPaymentInstructionWith()
            .payerName("Krishna")
            .currency("USD")
            .amount(100);

    @Test
    public void cashCreatePayments400() throws IOException {
        scenario.given()
                .when().cashCreatePayment(cashPaymentToCreate400)
                .then().BadRequest();
    }

    private ChequePaymentInstruction.ChequePaymentInstructionBuilder chequesPaymentToCreate = chequePaymentInstructionWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .chequeNumber("000000");


    @Test
    public void ChequeCreatePayments201() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate)
                .then().chequeCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Viswanadh");
                })
        );
    }

    private ChequePaymentInstruction.ChequePaymentInstructionBuilder chequesPaymentToCreate400 = chequePaymentInstructionWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .chequeNumber("000000");


    @Test
    public void ChequeCreatePayments500() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400)
                .then().BadRequest();
    }

    private ChequePaymentInstruction.ChequePaymentInstructionBuilder chequesPaymentToCreate400AN = chequePaymentInstructionWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .chequeNumber("000000");


    @Test
    public void ChequeCreatePayments500AN() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400AN)
                .then().BadRequest();
    }

    private ChequePaymentInstruction.ChequePaymentInstructionBuilder chequesPaymentToCreate400IN = chequePaymentInstructionWith()
            .payerName("Viswanadh")
            .amount(200)
            .currency("GBP")
            .chequeNumber("0000001");


    @Test
    public void ChequeCreatePayments500IN() throws IOException {
        scenario.given()
                .when().chequeCreatePayment(chequesPaymentToCreate400IN)
                .then().BadRequest();
    }

    private PostalOrderPaymentInstruction.PostalOrderPaymentInstructionBuilder postalOrderPaymentToCreate = postalOrderPaymentInstructionWith()
            .payerName("Anish")
            .amount(200)
            .currency("GBP")
            .postalOrderNumber("000000");

    @Test
    public void PostalOrderCreatePayments201() throws IOException {
        scenario.given()
                .when().postalOrdersCreatePayment(postalOrderPaymentToCreate)
                .then().postalOrdersCreated((paymentDto -> {
                    assertThat(paymentDto.getPayerName()).isEqualTo("Anish");
                })
        );
    }

    private PostalOrderPaymentInstruction.PostalOrderPaymentInstructionBuilder postalOrderPaymentToCreate400 = postalOrderPaymentInstructionWith()
            .payerName("Anish")
            .amount(200)
            .currency("GBP")
            .postalOrderNumber("0000001");

    @Test
    public void PostalOrderCreatePayments500() throws IOException {
        scenario.given()
                .when().postalOrdersCreatePayment(postalOrderPaymentToCreate400)
                .then().BadRequest();
    }

    private AllPayPaymentInstruction.AllPayPaymentInstructionBuilder allPayPaymentToCreate = allPayPaymentInstructionWith()
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

    private AllPayPaymentInstruction.AllPayPaymentInstructionBuilder allPayPaymentToCreate400 = allPayPaymentInstructionWith()
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
