package de.neuefische.backend.api.service;

import de.neuefische.backend.api.exception.ApiResponseException;
import de.neuefische.backend.model.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ArbeitsagenturApiServiceIntegrationTest {

    private final RestClient.Builder builder = RestClient.builder();
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    private final ArbeitsagenturApiService apiService = new ArbeitsagenturApiService(builder);

    @Test
    void loadAllOrganizations_shouldReturnOrganizationsFromApi() {
        server.expect(requestTo("https://rest.arbeitsagentur" + ".de/infosysbub/wbsuche/pc/v2/bildungsangebot" +
                                "?page=0&size=20")).andExpect(header("X-API-Key", "infosysbub-wbsuche"))

                .andRespond(withSuccess("""
                        {
                        "_embedded": {
                        	"termine": [
                                {
                                 "angebot":{
                                     "bildungsanbieter":
                                                { "id": 54545,
                                                "name": "IU Akademie IU Internationale Hochschule",
                                                  "homepage": "https://www.iu-akademie.de/",
                                                  "email": "weiterbildung@iu-akademie.de",
                                                    "adresse": {
                                                        "strasse": "Juri-Gagarin-Ring 152",
                                                        "ortStrasse": {
                                                           "plz": "99084",
                                                           "name": "Erfurt"
                                                                        }
                                                                }
                                                }
                                             }
                                 }
                                 ]
                                 },
                                 "page": {
                                         "size": 20,
                                         "totalElements": 10000,
                                         "totalPages": 500,
                                         "number": 0
                                     }
                        }
                        """, MediaType.APPLICATION_JSON));

        server.expect(requestTo("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot?page=1&size" + "=20")).andExpect(header("X-API-Key", "infosysbub-wbsuche")).andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        List<Organization> organizations = apiService.loadAllOrganizations();
        server.verify();
        assertEquals("IU Akademie IU Internationale Hochschule", organizations.getFirst().name());

    }

    @Test
    void loadAllOrganizations_shouldThrowApiResponseExceptionOnServerError() {
        server.expect(requestTo("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot?page=0&size" +
                                "=20")).andExpect(method(HttpMethod.GET)).andRespond(withServerError());

        org.junit.jupiter.api.Assertions.assertThrows(ApiResponseException.class, apiService::loadAllOrganizations);

        server.verify();
    }
}

