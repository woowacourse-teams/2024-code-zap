package codezap.voc.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import codezap.voc.dto.VocRequest;
import codezap.voc.service.VocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VocController implements SpringDocVocController {

    private final VocService vocService;

    @PostMapping("/contact")
    public void contact(@Valid VocRequest request) {
        vocService.contact(request);
    }
}
