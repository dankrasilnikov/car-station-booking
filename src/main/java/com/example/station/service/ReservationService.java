package com.example.station.service;

import com.example.station.models.*;
import com.example.station.repository.ReservationRepository;
import com.example.station.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    SlotRepository slotRepository;

//    public ReservationService(ReservationRepository reservationRepository, SlotRepository slotRepository) {
//        this.reservationRepository = reservationRepository;
//        this.slotRepository = slotRepository;
//    }

    public Reservation reserveSlot(User user, Long slotId, LocalDateTime desiredStartTime) {
        if (desiredStartTime.getMinute() % 30 != 0) {
            throw new IllegalArgumentException(); // рудимент
        }
        LocalDateTime desiredEndTime = desiredStartTime.plusMinutes(30);
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(slotId, desiredStartTime, desiredEndTime);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Этот слот уже занят в указанное время.");
        }
        ChargingSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Слот не найден."));
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSlot(slot);
        reservation.setStartTime(desiredStartTime);
        reservation.setEndTime(desiredEndTime);
        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);
    }

    public List<TimeSlotAvailability> getSlotAvailability(Long slotId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<Reservation> reservations = reservationRepository.findReservationsForSlotOnDay(slotId, startOfDay, endOfDay);
        Set<LocalDateTime> reservedTimes = reservations.stream()
                .map(Reservation::getStartTime)
                .collect(Collectors.toSet());
        List<TimeSlotAvailability> availability = new ArrayList<>();
        LocalDateTime currentTime = startOfDay;
        while (currentTime.isBefore(endOfDay)) {
            boolean isAvailable = !reservedTimes.contains(currentTime);
            availability.add(new TimeSlotAvailability(currentTime, currentTime.plusMinutes(30), isAvailable));
            currentTime = currentTime.plusMinutes(30);
        }
        return availability;
    }
}
