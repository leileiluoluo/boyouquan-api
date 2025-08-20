package com.boyouquan.controller;

import com.boyouquan.model.DomainNameChange;
import com.boyouquan.service.DomainNameChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/new-domain-names")
public class DomainNameChangeController {

    @Autowired
    private DomainNameChangeService domainNameChangeService;

    @GetMapping("")
    public ResponseEntity<?> get(@RequestParam("domainName") String domainName) {
        boolean exists = domainNameChangeService.existsByOldDomainName(domainName);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        // get
        DomainNameChange domainNameChange = domainNameChangeService.getByOldDomainName(domainName);

        return ResponseEntity.ok(domainNameChange);
    }

}
