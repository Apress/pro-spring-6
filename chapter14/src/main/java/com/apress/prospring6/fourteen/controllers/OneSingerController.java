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
package com.apress.prospring6.fourteen.controllers;

import com.apress.prospring6.fourteen.entities.Singer;
import com.apress.prospring6.fourteen.services.SingerService;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


/**
 * Created by iuliana on 26/12/2022
 */
@Controller
@RequestMapping("/singer/{id}")
public class OneSingerController {

    private final Logger LOGGER = LoggerFactory.getLogger(OneSingerController.class);
    private final SingerService singerService;
    private final MessageSource messageSource;

    public OneSingerController(SingerService singerService, MessageSource messageSource) {
        this.singerService = singerService;
        this.messageSource = messageSource;
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String showSingerData(@PathVariable("id") Long id, Model uiModel) {
        Singer singer = singerService.findById(id);
        uiModel.addAttribute("singer", singer);

        return "singers/show";
    }

    //@RequestMapping(path = "/edit", method = RequestMethod.GET)
    @GetMapping(path = "/edit")
    public String showEditForm(@PathVariable("id") Long id, Model uiModel) {
        Singer singer = singerService.findById(id);

        uiModel.addAttribute("singer", singer);
        return "singers/edit";
    }

    @GetMapping(path = "/upload")
    public String showPhotoUploadForm(@PathVariable("id") Long id, Model uiModel) {
        Singer singer = singerService.findById(id);

        uiModel.addAttribute("singer", singer);
        return "singers/upload";
    }

    @PutMapping
    public String updateSingerInfo(@Valid Singer singer, BindingResult bindingResult, Model uiModel, Locale locale) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", messageSource.getMessage("singer.save.fail", new Object[]{}, locale));
            uiModel.addAttribute("singer", singer);
            return "singers/edit";
        } else {
            uiModel.asMap().clear();

            var fromDb = singerService.findById(singer.getId());

            fromDb.setFirstName(singer.getFirstName());
            fromDb.setLastName(singer.getLastName());
            fromDb.setBirthDate(singer.getBirthDate());

            singerService.save(fromDb);
            return "redirect:/singer/" + singer.getId();
        }
    }

    @GetMapping( "/photo")
    //@RequestMapping(value = "/photo", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Singer singer = singerService.findById(id);

        if (singer.getPhoto() != null) {
            LOGGER.info("Downloading photo for id: {} with size: {}", singer.getId(), singer.getPhoto().length);
            return singer.getPhoto();
        }
        return null;
    }


    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestParam(value ="file", required=false) MultipartFile file, @PathVariable("id") Long id) throws IOException {
        var fromDb = singerService.findById(id);

        // Process file  upload
        if (!file.isEmpty()) {
            setPhoto(fromDb, file);
            singerService.save(fromDb);
        }

        return "redirect:/singer/" + id;
    }

    //@RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping
    public String deleteSinger(@PathVariable("id") Long id) {
        singerService.findById(id);
        singerService.delete(id);
        return "redirect:/singers/list";
    }

    static void setPhoto(Singer singer, MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        var fileContent = IOUtils.toByteArray(inputStream);
        singer.setPhoto(fileContent);
    }

}
