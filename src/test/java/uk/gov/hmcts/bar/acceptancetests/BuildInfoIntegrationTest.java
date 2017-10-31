package uk.gov.hmcts.bar.acceptancetests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.bar.acceptancetests.dsl.BARTestDsl;
import uk.gov.hmcts.bar.api.contract.*;

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

