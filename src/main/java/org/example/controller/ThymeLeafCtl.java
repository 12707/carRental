package org.example.controller;

import org.example.dto.CarInventoryDto;
import org.example.dto.CarRentalDto;
import org.example.dto.CarRentalHisDto;
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

    private void init(Model model) {
        model.addAttribute("msg", "Car Rental Center");
        List<CarInventoryDto> carInventoryDtoList = carsRentalService.findInStockCount();
        model.addAttribute("cars", carInventoryDtoList);
        model.addAttribute("timeUnits", Arrays.stream(TimeUnit.values()).map(TimeUnit::name).collect(Collectors.toList()));
        List<CarRentalDto> carRentalDtoList = carsRentalService.findAllRentCars();
        model.addAttribute("carRents", carRentalDtoList);
        List<CarRentalHisDto> carRentalHisDtoList = carsRentalService.findAllHisRentCars();
        model.addAttribute("carRentsHis", carRentalHisDtoList);
    }
}
