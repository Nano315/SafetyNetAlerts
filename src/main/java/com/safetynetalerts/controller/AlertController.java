package com.safetynetalerts.controller;

import com.safetynetalerts.dto.*;
import com.safetynetalerts.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * GET /childAlert?address=<address>
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        logger.info("GET /childAlert?address={}", address);
        List<ChildAlertDTO> children = alertService.getChildAlert(address);
        logger.info("Response => {} child(ren) found", children.size());
        return children;
    }

    /**
     * GET /phoneAlert?firestation=<station_number>
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneAlert(@RequestParam String firestation) {
        logger.info("GET /phoneAlert?firestation={}", firestation);
        List<String> phones = alertService.getPhoneAlert(firestation);
        logger.info("Response => {} phone number(s) found", phones.size());
        return phones;
    }

    /**
     * GET /fire?address=<address>
     */
    @GetMapping("/fire")
    public FireDTO getFire(@RequestParam String address) {
        logger.info("GET /fire?address={}", address);
        FireDTO fireResponse = alertService.getFire(address);
        logger.info("Response => stationNumber={}, {} occupant(s)",
                fireResponse.getStationNumber(),
                fireResponse.getPersons().size());
        return fireResponse;
    }

    /**
     * GET /flood/stations?stations=<list_of_station_numbers>
     * ex: /flood/stations?stations=1,2
     */
    @GetMapping("/flood/stations")
    public List<HouseholdDTO> getFloodStations(@RequestParam List<String> stations) {
        logger.info("GET /flood/stations?stations={}", stations);
        List<HouseholdDTO> households = alertService.getFloodStations(stations);
        logger.info("Response => {} household(s)", households.size());
        return households;
    }

    /**
     * GET /personInfo?lastName=<lastName>
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDetailsDTO> getPersonInfo(@RequestParam String lastName) {
        logger.info("GET /personInfo?lastName={}", lastName);
        List<PersonInfoDetailsDTO> personInfos = alertService.getPersonInfo(lastName);
        logger.info("Response => {} person(s) found", personInfos.size());
        return personInfos;
    }

    /**
     * GET /communityEmail?city=<city>
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        logger.info("GET /communityEmail?city={}", city);
        List<String> emails = alertService.getCommunityEmail(city);
        logger.info("Response => {} email(s) found", emails.size());
        return emails;
    }
}
