package uk.gov.hmcts.bar.acceptancetests;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.bar.acceptancetests.dsl.BARTestDsl;
import uk.gov.hmcts.bar.api.contract.PaymentUpdateDto;
import uk.gov.hmcts.bar.api.contract.SearchDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.bar.api.contract.PaymentUpdateDto.CaseUpdateDto.caseUpdateDtoWith;
import static uk.gov.hmcts.bar.api.contract.PaymentUpdateDto.paymentUpdateDtoWith;
import static uk.gov.hmcts.bar.api.contract.SearchDto.searchDtoWith;

public class BARPaymentTypeIntegration extends IntegrationTestBase{

    @Autowired
    private BARTestDsl scenario;

   private PaymentUpdateDto.PaymentUpdateDtoBuilder paymentToCreate = paymentUpdateDtoWith()
           .payeeName("Krishna")
            .paymentReceiptType("post")
            .counterCode("string")
            .eventType("string")
            .feeCode("string")
            .sortCode("000000")
            .accountNumber("00000000")
            .chequeNumber("000000")
            .currency("GBP")
            .paymentTypeId(1)
            .paymentDate("2017-10-26T00:00:00")
            .amount(122)
            .createdByUserId("anish")
            .updatedByUserId("string")
            .cases(Arrays.asList(
                   caseUpdateDtoWith().jurisdiction1("one").jurisdiction2("two").reference("case1").subServiceId(1).build())
           );


    @Test
    public void createPayments201() throws IOException {
        scenario.given()
                .when().createPayment(paymentToCreate)
                .then().created((paymentDto -> {
                    assertThat(paymentDto.getCreatedByUserId()).isEqualTo("anish");
                })
       );
    }

    private SearchDto.SearchDtoBuilder searchCriteria = searchDtoWith()
            .userId("anish")
            .fromDate(LocalDateTime.parse("2017-09-14T10:11:30"))
            .toDate(LocalDateTime.parse("2017-10-31T16:11:30"));
    @Ignore
    @Test
    public void getPayments() throws IOException {
        scenario.given()
                .when().createPayment(paymentToCreate)
                .getPayments(searchCriteria)
                .then().got(List.class, (result -> assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllPaymentTypes() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getPaymentTypes()
                .then().got(List.class, (result -> assertThat(result).isNotEmpty()));
    }

    @Test
    public void findAllServiceTypes() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getServiceTypes()
                .then().got(List.class, (result -> assertThat(result).isNotEmpty()));
    }


}
