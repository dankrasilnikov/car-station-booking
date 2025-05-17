package com.zephyra.station.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
public class BookingService {
    @Autowired
    GasStationRepository gasStationRepository;
    @Autowired
    StationSlotRepository stationSlotRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Transactional
    public void reserveStation(String supabaseId, Long gasStationId, LocalDateTime startTime) {
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BookingException("Cannot book in the past");
        }

        LocalDateTime slotTime = startTime.withMinute(0).withSecond(0).withNano(0);

        GasStation station = gasStationRepository.findById(gasStationId)
                .orElseThrow(StationNotFoundException::new);

        StationSlot slot = stationSlotRepository.findByGasStationAndSlotTime(station, slotTime)
                .filter(s -> s.getAvailableSlots() > 0)
                .orElseThrow(() -> new BookingException("No available slot at this time"));

        User user = userRepository.findBySupabaseId(supabaseId)
                .orElseThrow(UserNotFoundException::new);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setGasStation(station);
        reservation.setStartTime(slotTime);
        reservation.setEndTime(slotTime.plusHours(1));

        reservationRepository.save(reservation);

        slot.setAvailableSlots(slot.getAvailableSlots() - 1);
        stationSlotRepository.save(slot);
    }
}
