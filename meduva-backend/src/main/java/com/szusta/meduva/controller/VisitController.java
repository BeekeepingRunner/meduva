package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.payload.WeekBoundaries;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.service.ServicesService;
import com.szusta.meduva.service.user.UserService;
import com.szusta.meduva.service.visit.VisitService;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    VisitService visitService;
    UserService userService;
    ServicesService servicesService;
    ScheduleChecker scheduleChecker;

    @Autowired
    public VisitController(VisitService visitService,
                           UserService userService,
                           ServicesService servicesService,
                           ScheduleChecker scheduleChecker) {
        this.visitService = visitService;
        this.userService = userService;
        this.servicesService = servicesService;
        this.scheduleChecker = scheduleChecker;
    }

    @GetMapping("/{visitId}")
    public Visit findById(@PathVariable Long visitId) {
        return visitService.findById(visitId);
    }

    @GetMapping("/get-worker-available-days-in-month")
    public List<Date> getWorkerAvailableDaysOfMonth(@RequestParam Long workerId,
                                                    @RequestParam Long serviceId,
                                                    @RequestParam String anyDayFromMonth) throws ParseException {
        Date anyDayOfMonth = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(anyDayFromMonth);
        User worker = userService.findById(workerId);
        Service service = servicesService.findById(serviceId);
        return visitService.getWorkerAvailableDaysOfMonth(worker, service, anyDayOfMonth);
    }

    @GetMapping("/get-available-worker-terms-for-day")
    public List<Term> getWorkerAvailableTermsForDay(@RequestParam Long workerId,
                                                    @RequestParam Long serviceId,
                                                    @RequestParam String dayDate) throws ParseException {
        Date day = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dayDate);
        User worker = userService.findById(workerId);
        Service service = servicesService.findById(serviceId);
        return visitService.getWorkerAvailableTermsForDay(worker, service, day);
    }

    @PostMapping
    public ResponseEntity<Visit> saveNewVisit(@RequestBody Term term) {
        return ResponseEntity.of(visitService.saveNewVisit(term));
    }

    @GetMapping("/all-as-client-by-user-id/{userId}")
    public List<Visit> findAllWhereUserIsClient(@PathVariable Long userId) {
        User client = userService.findById(userId);
        return visitService.findAllWhereUserIsClient(client);
    }

    @GetMapping("/all-as-worker-by-id/{workerId}")
    public List<Visit> findAllWhereUserIsWorker(@PathVariable Long workerId) {
        User worker = userService.findById(workerId);
        return visitService.findAllWhereUserIsWorker(worker);
    }

    @GetMapping("/all-of-unregistered-client/{unregisteredClientId}")
    public List<Visit> findAllOfUnregisteredClient(@PathVariable Long unregisteredClientId) {
        return visitService.findAllOfUnregisteredClient(unregisteredClientId);
    }

    @PostMapping("/get-week-not-cancelled-visits-as-worker/{workerId}")
    public List<Visit> findAllBookedWhereUserIsWorkerBetween(@PathVariable Long workerId, @RequestBody WeekBoundaries weekBoundaries) {
        Date startTime = TimeUtils.getDayStart(weekBoundaries.getFirstWeekDay());
        Date endTime = TimeUtils.getDayEnd(weekBoundaries.getLastWeekDay());
        return visitService.findAllNotCancelledWhereUserIsWorkerBetween(workerId, startTime, endTime);
    }

    @PostMapping("/get-week-not-cancelled-visits-as-client/{workerId}")
    public List<Visit> findAllNotCancelledWhereUserIsClientBetween(@PathVariable Long workerId, @RequestBody WeekBoundaries weekBoundaries) {
        Date startTime = TimeUtils.getDayStart(weekBoundaries.getFirstWeekDay());
        Date endTime = TimeUtils.getDayEnd(weekBoundaries.getLastWeekDay());
        return visitService.findAllNotCancelledWhereUserIsClientBetween(workerId, startTime, endTime);
    }

    @PostMapping("/get-week-not-cancelled-room-visit/{roomId}")
    public List<Visit> findAllNotCancelledWeeklyRoomVisits(@PathVariable Long roomId, @RequestBody WeekBoundaries weekBoundaries){
        Date startTime = TimeUtils.getDayStart(weekBoundaries.getFirstWeekDay());
        Date endTime = TimeUtils.getDayEnd(weekBoundaries.getLastWeekDay());
        return visitService.findAllNotCancelledWeeklyRoomVisits(roomId, startTime, endTime);
    }

    @PostMapping("/get-week-not-cancelled-item-visit/{itemId}")
    public List<Visit> findAllNotCancelledWeeklyItemVisits(@PathVariable Long itemId, @RequestBody WeekBoundaries weekBoundaries) {
        Date startTime = TimeUtils.getDayStart(weekBoundaries.getFirstWeekDay());
        Date endTime = TimeUtils.getDayEnd(weekBoundaries.getLastWeekDay());
        return visitService.findAllNotCancelledWeeklyItemVisits(itemId, startTime, endTime);
    }

    @DeleteMapping("/all-of-unregistered-client/{unregisteredClientId}")
    public void deleteAllOfUnregisteredClient(@PathVariable Long unregisteredClientId) {
       visitService.deleteAllOfUnregisteredClient(unregisteredClientId);
    }

    @DeleteMapping("/all-as-client-by-user-id/{userId}")
    public void deleteAllWhereUserIsClient(@PathVariable Long userId) {
        User userAsClient = userService.findById(userId);
        visitService.deleteAllWhereUserIsClient(userAsClient);
    }
    @DeleteMapping("/all-as-worker-by-user-id/{userId}")
    public void deleteAllWhereUserIsWorker(@PathVariable Long userId) {
        User userAsWorker = userService.findById(userId);
        visitService.deleteAllWhereUserIsWorker(userAsWorker);
    }

    @PutMapping("/{visitId}/mark-as-done")
    public Visit markVisitAsDone(@PathVariable Long visitId) {
        Visit visit = visitService.findById(visitId);
        return visitService.markAsDone(visit);
    }

    @PutMapping("/{visitId}/mark-as-paid")
    public Visit markVisitAsPaid(@PathVariable Long visitId) {
        Visit visit = visitService.findById(visitId);
        return visitService.markAsPaid(visit);
    }

    @PutMapping("/mark-as-deleted/{visitId}")
    public void markVisitAsDeleted(@PathVariable Long visitId) {
        visitService.markAsDeleted(visitId);
    }

    @PutMapping("/{visitId}/cancel")
    public Visit cancelVisit(@PathVariable Long visitId) {
        Visit visit = visitService.findById(visitId);
        return visitService.cancel(visit);

    }

    @PutMapping("/cancel-all-of-unregistered-client/{unregisteredClientId}")
    public List<Visit> cancelAllOfUnregisteredClient(@PathVariable Long unregisteredClientId) {
        return visitService.cancelAllOfUnregisteredClient(unregisteredClientId);
    }

    @PutMapping("/cancel-all-as-client-by-user-id/{userId}")
    public List<Visit> cancelAllWhereUserIsClient(@PathVariable Long userId) {
        User userAsClient = userService.findById(userId);
        return visitService.cancelAllWhereUserIsClient(userAsClient);
    }

    @PutMapping("/cancel-all-as-worker-by-user-id/{userId}")
    public List<Visit> cancelAllWhereUserIsWorker(@PathVariable Long userId) {
        User userAsWorker = userService.findById(userId);
        return visitService.cancelAllWhereUserIsWorker(userAsWorker);
    }
}
