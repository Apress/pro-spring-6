package com.apress.prospring6.eighteen.boot.controllers;

import com.apress.prospring6.eighteen.boot.entities.Singer;
import com.apress.prospring6.eighteen.boot.services.SingerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
        var singer = singerService.findById(id);
        if (singer != null) {
            int msec = 10;
            try {
                Thread.sleep(Duration.ofMillis(msec * id));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return singer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="/")
    public Singer create(@RequestBody @Valid Singer singer) {
        LOGGER.info("Creating singer: {}" , singer);
        return singerService.save(singer);
    }

    @PutMapping(value="/{id}")
    public Singer update(@RequestBody/* @Valid*/ Singer singer, @PathVariable Long id) {
        LOGGER.info("Updating singer: {}" , singer);
        return singerService.update(id, singer);
    }

    @DeleteMapping(value="/{id}")
    public void delete(@PathVariable Long id) {
        LOGGER.info("Deleting singer with id: {}" , id);
        singerService.delete(id);
    }
}
