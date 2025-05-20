package com.zephyra.station.dto;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.models.Reservation;

import java.time.*;

public class ReservationDTO {
    private String title;      // название станции
    private Integer seqNum;    // номер коннектора
    private Long start;        // timestamp начала (в секундах)
    private Duration duration; // длительность

    public ReservationDTO() {}

    public ReservationDTO(String title, Integer seqNum, Long start, Duration duration) {
        this.title = title;
        this.seqNum = seqNum;
        this.start = start;
        this.duration = duration;
    }

    public static ReservationDTO from(Reservation reservation) {
        Range<ZonedDateTime> period = reservation.getPeriod();

        ZonedDateTime start = period.lower();
        ZonedDateTime end = period.upper();
        Duration duration = Duration.between(start, end);

        return new ReservationDTO(
                reservation.getConnector().getStation().getTitle(),
                reservation.getConnector().getSeqNum(),
                start.toInstant().getEpochSecond(),
                duration
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
