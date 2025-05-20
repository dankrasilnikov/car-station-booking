package com.zephyra.station.models;

import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.ZonedDateTime;


@Entity
@Table(name = "reservation")
public class Reservation {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) private User user;
    @ManyToOne(fetch = FetchType.LAZY) private Connector connector;

    /** Диапазон времени [start, end) – PostgreSQL tstzrange */
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    private Range<ZonedDateTime> period;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.HOLD;


    public Reservation() {}

    public Reservation(Long id, User user, Connector connector, Range<ZonedDateTime> period, ReservationStatus status) {
        this.id = id;
        this.user = user;
        this.connector = connector;
        this.period = period;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Range<ZonedDateTime> getPeriod() {
        return period;
    }

    public void setPeriod(Range<ZonedDateTime> period) {
        this.period = period;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
