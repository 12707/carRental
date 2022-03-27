package org.example.controller;

import org.example.dto.CarInventoryDto;
import org.example.service.ICarsRentalService;
import org.example.utils.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ThymeLeafCtl {
    @Autowired
    private ICarsRentalService carsRentalService;

    @RequestMapping("/")
    public String endpoint(Model model) {
        init(model);
        return "index";
    }

    @RequestMapping("/book")
    public String submit(Model model, String customer, String carModel, int rentTime, String unit) {
        init(model);
        carsRentalService.rentCar(customer, carModel, rentTime, unit);

        return "index";
    }

    private void init(Model model) {
        model.addAttribute("msg", "Car Rental Center");
        List<CarInventoryDto> list = carsRentalService.findInStockCount();
        model.addAttribute("cars", list);
        model.addAttribute("timeUnits", Arrays.stream(TimeUnit.values()).map(TimeUnit::name).collect(Collectors.toList()));
    }
}
