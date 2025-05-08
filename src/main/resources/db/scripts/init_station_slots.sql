DO $$
DECLARE
    station_id INTEGER;
    hour_counter INTEGER;
    today DATE := CURRENT_DATE;
BEGIN
    -- Перебор всех нужных заправок
    FOR station_id IN SELECT id FROM gas_stations LOOP
        hour_counter := 0;
        WHILE hour_counter < 24 LOOP
            INSERT INTO station_slots (gas_station_id, slot_time, available_slots)
            VALUES (
                station_id,                                -- ID заправки
                today + INTERVAL '1 hour' * hour_counter,  -- Слот времени
                5                                          -- Стартовое количество слотов
            );
            hour_counter := hour_counter + 1;
        END LOOP;
    END LOOP;
END $$;