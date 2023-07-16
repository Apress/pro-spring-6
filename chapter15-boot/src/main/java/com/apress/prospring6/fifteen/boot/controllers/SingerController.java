package com.apress.prospring6.fifteen.boot.controllers;

import com.apress.prospring6.fifteen.boot.entities.Singer;
import com.apress.prospring6.fifteen.boot.services.SingerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/singer")
public class SingerController {

    final Logger LOGGER = LoggerFactory.getLogger(SingerController.class);

    private final SingerService singerService;

    public SingerController(SingerService singerService) {
        this.singerService = singerService;
    }

    @GetMapping(path={"/", ""})
    public List<Singer> all() {
        return singerService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Singer findSingerById(@PathVariable Long id) {
        return singerService.findById(id);
    }

    @PostMapping(path="/")
    public Singer  create(@RequestBody @Valid Singer singer) {
        LOGGER.info("Creating singer: {}" , singer);
        return singerService.save(singer);
    }

    @PutMapping(value="/{id}")
    public Singer update(@RequestBody @Valid Singer singer, @PathVariable Long id) {
        LOGGER.info("Updating singer: {}" , singer);
        return singerService.update(id, singer);
    }

    @DeleteMapping(value="/{id}")
    public void delete(@PathVariable Long id) {
        LOGGER.info("Deleting singer with id: {}" , id);
        singerService.delete(id);
    }
}
