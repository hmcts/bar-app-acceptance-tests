package uk.gov.hmcts.bar.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.bar.acceptancetests.dsl.BARTestDsl;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildInfoIntegrationTest extends IntegrationTestBase{

    @Autowired
    private BARTestDsl scenario;

    @Ignore
    @Test
    public void buildInfoShouldBePresent() throws IOException, NoSuchFieldException {
        scenario.given()
                .when().getBuildInfo()
                .then().got(JsonNode.class, response -> {
           assertThat(response.at("/git/commit/id").asText()).isNotEmpty();
           assertThat(response.at("/build/version").asText()).isNotEmpty();
        });
    }
}

