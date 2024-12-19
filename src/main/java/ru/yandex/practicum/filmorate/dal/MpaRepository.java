package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private final static String QUERY_ALL_MPA = "SELECT * FROM mpa_ratings";
    private final static String QUERY_FIND_MPA_BY_ID = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper, Class<Mpa> entityType) {
        super(jdbc, mapper, entityType);
    }

    public Collection<Mpa> getAllMpa() {
        return findMany(QUERY_ALL_MPA);
    }

    public Mpa getMpaById(Long mpaId) {
        return findOne(QUERY_FIND_MPA_BY_ID, mpaId);
    }
}
