package com.zephyra.station.controllers;

import com.zephyra.station.dto.ReservationRequest;
import com.zephyra.station.errors.BookingException;
import com.zephyra.station.errors.StationNotFoundException;
import com.zephyra.station.errors.UserNotFoundException;
import com.zephyra.station.models.GasStation;
import com.zephyra.station.models.Reservation;
import com.zephyra.station.models.StationSlot;
import com.zephyra.station.models.User;
import com.zephyra.station.repository.GasStationRepository;
import com.zephyra.station.repository.ReservationRepository;
import com.zephyra.station.repository.StationSlotRepository;
import com.zephyra.station.repository.UserRepository;
import com.zephyra.station.service.BookingService;
import com.zephyra.station.service.StationAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gas-stations")
public class GasStationController {
    @Autowired
    GasStationRepository gasStationRepository;
    @Autowired
    StationSlotRepository stationSlotRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    StationAvailabilityService stationAvailabilityService;

    @GetMapping("/available")
    public ResponseEntity<List<GasStation>> getAvailableStations(@RequestParam("timestamp") LocalDateTime timestamp) {
        List<GasStation> availableStations = stationAvailabilityService.getAvailableStations(timestamp);
        return  ResponseEntity.ok(availableStations);
    }


//@PostMapping("/book")
//public ResponseEntity<String> reserveStation(@RequestParam LocalDateTime startTime,
//                                             @RequestBody ReservationRequest request) {
//
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Jwt jwt = (Jwt) authentication.getPrincipal();
//
//    String supabaseId = jwt.getClaimAsString("sub"); //
//
//    if (startTime.isBefore(LocalDateTime.now())) {
//        throw new BookingException("Cannot book in the past");
//    }
//
//    LocalDateTime slotTime = startTime.withMinute(0).withSecond(0).withNano(0);
//
//    Optional<GasStation> stationOpt = gasStationRepository.findById(request.getGasStationId());
//
//    if (stationOpt.isEmpty()) {
//        throw new StationNotFoundException();
//    }
//
//    GasStation station = stationOpt.get();
//    Optional<StationSlot> slotOpt = stationSlotRepository.findByGasStationAndSlotTime(station, slotTime);
//
//    if (slotOpt.isEmpty() || slotOpt.get().getAvailableSlots() <= 0) {
//        throw new BookingException("No available slot at this time");
//    }
//
//    StationSlot slot = slotOpt.get();
//
//    Optional<User> userOpt = userRepository.findBySupabaseId(supabaseId);
//    if (userOpt.isEmpty()) {
//        throw new UserNotFoundException();
//    }
//
//    User user = userOpt.get();
//
//    Reservation reservation = new Reservation();
//    reservation.setUser(user);
//    reservation.setGasStation(station);
//    reservation.setStartTime(slotTime);
//    reservation.setEndTime(slotTime.plusHours(1));
//
//    reservationRepository.save(reservation);
//
//    // уменьшаем количество слотов
//    slot.setAvailableSlots(slot.getAvailableSlots() - 1);
//    stationSlotRepository.save(slot);
//
//    return ResponseEntity.ok("Reservation successful!");
//}
@PostMapping("/book")
public ResponseEntity<String> reserveStation(@RequestParam LocalDateTime startTime,
                                             @RequestBody ReservationRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String supabaseId = jwt.getClaimAsString("sub");

    bookingService.reserveStation(supabaseId, request.getGasStationId(), startTime);

    return ResponseEntity.ok("Reservation successful!");
}
}
