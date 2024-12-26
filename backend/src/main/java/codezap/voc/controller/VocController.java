package codezap.voc.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codezap.voc.dto.VocRequest;
import codezap.voc.service.VocService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VocController implements SpringDocVocController {

    private final VocService vocService;

    @PostMapping("/contact")
    public ResponseEntity<Void> create(@Valid @RequestBody VocRequest request) {
        vocService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
