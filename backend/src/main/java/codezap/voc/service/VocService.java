package codezap.voc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import codezap.voc.dto.VocRequest;

@Service
public class VocService {

    private final RestClient restClient;

    public VocService(RestClient.Builder builder) {
        restClient = builder.build();
    }

    public void create(VocRequest request) {
        restClient.post()
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
