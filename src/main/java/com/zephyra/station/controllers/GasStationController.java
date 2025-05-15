package com.zephyra.station.controllers;

import com.zephyra.station.dto.ReservationRequest;
import com.zephyra.station.models.GasStation;
import com.zephyra.station.models.Reservation;
import com.zephyra.station.models.StationSlot;
import com.zephyra.station.models.User;
import com.zephyra.station.repository.GasStationRepository;
import com.zephyra.station.repository.ReservationRepository;
import com.zephyra.station.repository.StationSlotRepository;
import com.zephyra.station.repository.UserRepository;
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
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StationSlotRepository stationSlotRepository;

    @GetMapping("/available")
    public List<GasStation> getAvailableStations(@RequestParam("timestamp") LocalDateTime timestamp) {
        List<GasStation> allStations = gasStationRepository.findAll();

        return allStations.stream()
                .filter(station -> {
                    LocalDateTime slotTime = timestamp.withMinute(0).withSecond(0).withNano(0); // округляем до начала часа
                    Optional<StationSlot> slotOpt = stationSlotRepository.findByGasStationAndSlotTime(station, slotTime);
                    return slotOpt.isPresent() && slotOpt.get().getAvailableSlots() > 0;
                })
                .collect(Collectors.toList());
    }


@PostMapping("/book")
public ResponseEntity<String> reserveStation(@RequestParam LocalDateTime startTime,
                                             @RequestBody ReservationRequest request) {

    // Получаем информацию о текущем пользователе из токена
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal(); // Получаем токен

    // Достаем supabaseId из токена
    String supabaseId = jwt.getClaimAsString("sub"); // 'sub' — это UUID пользователя в Supabase

    if (startTime.isBefore(LocalDateTime.now())) {
        return ResponseEntity.badRequest().body("Cannot book in the past");
    }

    LocalDateTime slotTime = startTime.withMinute(0).withSecond(0).withNano(0); // округляем до начала часа

    Optional<GasStation> stationOpt = gasStationRepository.findById(request.getGasStationId());

    if (stationOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Station not found");
    }

    GasStation station = stationOpt.get();
    Optional<StationSlot> slotOpt = stationSlotRepository.findByGasStationAndSlotTime(station, slotTime);

    if (slotOpt.isEmpty() || slotOpt.get().getAvailableSlots() <= 0) {
        return ResponseEntity.badRequest().body("No available slot at this time");
    }

    StationSlot slot = slotOpt.get();

    // Ищем пользователя по supabaseId, а не по обычному id
    Optional<User> userOpt = userRepository.findBySupabaseId(supabaseId);
    if (userOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found in database");
    }

    User user = userOpt.get();

    // Всё ок: создаём бронь
    Reservation reservation = new Reservation();
    reservation.setUser(user);
    reservation.setGasStation(station);
    reservation.setStartTime(slotTime);
    reservation.setEndTime(slotTime.plusHours(1)); // бронируем ровно на час

    reservationRepository.save(reservation);

    // уменьшаем количество слотов
    slot.setAvailableSlots(slot.getAvailableSlots() - 1);
    stationSlotRepository.save(slot);

    return ResponseEntity.ok("Reservation successful!");
}
}
