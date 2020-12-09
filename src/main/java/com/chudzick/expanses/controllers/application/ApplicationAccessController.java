package com.chudzick.expanses.controllers.application;

import com.chudzick.expanses.domain.application.ApplicationAccess;
import com.chudzick.expanses.domain.application.dto.ApplicationAccessDto;
import com.chudzick.expanses.services.application.ApplicationAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("application/access")
public class ApplicationAccessController {

    @Autowired
    private ApplicationAccessService applicationAccessService;

    @GetMapping
    public String getApplicationAccess(Model model) {
        List<ApplicationAccess> applicationAccess = applicationAccessService.findByCurrentUser();

        model.addAttribute("applicationAccessDto", new ApplicationAccessDto());
        model.addAttribute("applicationAccess", applicationAccess);
        return "application/access";
    }

    @PostMapping
    public String saveApplicationAccess(@ModelAttribute @Valid ApplicationAccessDto applicationAccessDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "redirect:/application/access";
        }
        ApplicationAccess applicationAccess = applicationAccessService.prepareNew(applicationAccessDto.getDescription());
        applicationAccessService.save(applicationAccess);
        return "redirect:/application/access";
    }

}
