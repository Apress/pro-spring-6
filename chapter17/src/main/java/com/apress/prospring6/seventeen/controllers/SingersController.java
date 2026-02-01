/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.seventeen.controllers;

import com.apress.prospring6.seventeen.entities.AbstractEntity;
import com.apress.prospring6.seventeen.entities.Singer;
import com.apress.prospring6.seventeen.problem.InvalidCriteriaException;
import com.apress.prospring6.seventeen.services.SingerService;
import com.apress.prospring6.seventeen.util.CriteriaDto;
import com.apress.prospring6.seventeen.util.SingerForm;
import com.apress.prospring6.seventeen.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.apress.prospring6.seventeen.controllers.OneSingerController.setPhoto;

/**
 * Created by iuliana on 27/12/2022
 */
@Controller
@RequestMapping("/singers")
public class SingersController {
    public static Comparator<AbstractEntity> COMPARATOR_BY_ID = Comparator.comparing(AbstractEntity::getId);

    private final SingerService singerService;
    private final MessageSource messageSource;

    public SingersController(SingerService singerService, MessageSource messageSource) {
        this.singerService = singerService;
        this.messageSource = messageSource;
    }

    @GetMapping
    //@RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        List<Singer> singers = singerService.findAll();
        singers.sort(COMPARATOR_BY_ID);
        uiModel.addAttribute("singers", singers);

        return "singers/list";
    }

    // --------------- create  -------------------
    @GetMapping(value = "/create")
    public String showCreateForm( Model uiModel) {
        uiModel.addAttribute("singerForm", new SingerForm());
        return "singers/create";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(@Valid SingerForm singerForm, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest,
                         Locale locale) throws IOException {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", messageSource.getMessage("singer.save.fail", new Object[]{}, locale));
            uiModel.addAttribute("singerForm", singerForm);
            return "singers/create";
        } else {
            uiModel.asMap().clear();

            var s = new Singer();
            s.setFirstName(singerForm.getFirstName());
            s.setLastName(singerForm.getLastName());
            s.setBirthDate(singerForm.getBirthDate());
            // Process file  upload
            if (!singerForm.getFile().isEmpty()) {
                setPhoto(s, singerForm.getFile());
            }

            var created = singerService.save(s);
            return "redirect:/singer/" + UrlUtil.encodeUrlPathSegment(created.getId().toString(),
                    httpServletRequest);
        }
    }

    // --------------- search  -------------------
    @GetMapping(value = "/search")
    public String showSearchform(CriteriaDto criteria) {
        return "singers/search";
    }

    @GetMapping(value = "/go")
    public String processSubmit(@Valid @ModelAttribute("criteriaDto") CriteriaDto criteria,
                                BindingResult result, Model model, Locale locale) {
        if (result.hasErrors()) {
            return "singers/search";
        }
        try {
            List<Singer> singers = singerService.getByCriteriaDto(criteria);
            if (singers.size() == 0) {
                result.addError(new FieldError("criteriaDto", "noResults", messageSource.getMessage("NotEmpty.criteriaDto.noResults", null, locale)));
                return "singers/search";
            } else if (singers.size() == 1) {
                return "redirect:/singer/" + singers.get(0).getId();
            } else {
                model.addAttribute("singers", singers);
                return "singers/list";
            }
        } catch (InvalidCriteriaException ice) {
            result.addError(new FieldError("criteriaDto", ice.getFieldName(),
                    messageSource.getMessage(ice.getMessageKey(), null, locale)));
            return "singers/search";
        }
    }

}
