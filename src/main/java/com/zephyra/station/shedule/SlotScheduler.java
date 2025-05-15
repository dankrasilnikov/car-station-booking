package com.zephyra.station.shedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class SlotScheduler {

    @PersistenceContext
    private EntityManager em;

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в 00:00
    @Transactional
    public void resetSlots() {
        em.createQuery("DELETE FROM StationSlot").executeUpdate();

        // 2. Получаем все заправки
        List<Long> gasStationIds = em.createQuery("SELECT gs.id FROM GasStation gs", Long.class).getResultList();

        for (Long stationId : gasStationIds) {
            LocalDateTime currentHour = LocalDateTime.now().with(LocalTime.MIN); // сегодня 00:00
            for (int i = 0; i < 24; i++) {
                em.createNativeQuery("INSERT INTO station_slots (gas_station_id, slot_time, available_slots) VALUES (?, ?, ?)")
                        .setParameter(1, stationId)
                        .setParameter(2, currentHour)
                        .setParameter(3, 2)
                        .executeUpdate();
                currentHour = currentHour.plusHours(1);
            }
        }
    }
}