package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.*;
import de.neuefische.backend.api.exception.ApiResponseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsagenturApiService {

    private final RestClient restClient;

    public ArbeitsagenturApiService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot")
                .build();
    }

    public List<ApiResponseOrganization> loadAllOrganizations() {
        Optional<ApiResponse> optionalResponse = Optional.ofNullable(
                restClient.get()
                        .uri("")
                        .header("X-API-Key", "infosysbub-wbsuche")
                        .retrieve()
                        .body(ApiResponse.class));

        List<ApiResponseDetails> details = optionalResponse.map(ApiResponse::responseContent)
                .map(ApiResponseContent::details)
                .orElseThrow(ApiResponseException::new);

        return details.stream().map(ApiResponseDetails::courseOffer)
                .map(ApiResponseCourseOffer::apiResponseOrganization).distinct().toList();


    }
}
