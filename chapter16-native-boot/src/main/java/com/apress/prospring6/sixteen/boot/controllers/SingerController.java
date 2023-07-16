package com.apress.prospring6.sixteen.boot.controllers;

import com.apress.prospring6.sixteen.boot.entities.Singer;

import com.apress.prospring6.sixteen.boot.repos.SingerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/singer")
public class SingerController {

    final Logger LOGGER = LoggerFactory.getLogger(SingerController.class);

    private final SingerRepo singerRepo;

    public SingerController(SingerRepo singerRepo) {
        this.singerRepo = singerRepo;
    }

    @GetMapping(path={"/", ""})
    public List<Singer> all() {
        return singerRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public Singer findSingerById(@PathVariable Long id) {
        return singerRepo.findById(id).orElse(null);
    }

    @PostMapping(path="/")
    public Singer  create(@RequestBody Singer singer) {
        LOGGER.info("Creating singer: {}" , singer);
        return singerRepo.save(singer);
    }

    @PutMapping(value="/{id}")
    public Singer update(@RequestBody Singer singer, @PathVariable Long id) {
        LOGGER.info("Updating singer: {}" , singer);
        return singerRepo.save(singer);
    }

    @DeleteMapping(value="/{id}")
    public void delete(@PathVariable Long id) {
        LOGGER.info("Deleting singer with id: {}" , id);
        singerRepo.deleteById(id);
    }
}
