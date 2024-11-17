package com.example.demo.controller;

import com.example.demo.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @PostMapping("/coll1/{filterValue}/{updateValue}")
    public void updateColl1(@PathVariable int filterValue, @PathVariable int updateValue) {
        updateService.updateColl1(filterValue, updateValue);
    }

    @PostMapping("/coll2/{filterValue}/{updateValue}")
    public void updateColl2(@PathVariable int filterValue, @PathVariable int updateValue) {
        updateService.updateColl2(filterValue, updateValue);
    }

    @PostMapping("/coll3/{filterValue}/{updateValue}")
    public void updateColl3(@PathVariable int filterValue, @PathVariable int updateValue) {
        updateService.updateColl3(filterValue, updateValue);
    }

    @PostMapping("/coll4/{filterValue}/{updateValue}")
    public void updateColl4(@PathVariable int filterValue, @PathVariable int updateValue) {
        updateService.updateColl4(filterValue, updateValue);
    }

    @PostMapping("/coll5/{filterValue}/{updateValue}")
    public void updateColl5(@PathVariable int filterValue, @PathVariable int updateValue) {
        updateService.updateColl5(filterValue, updateValue);
    }
}
