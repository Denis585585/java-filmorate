package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;
    private final Class<T> entityType;

    protected T findOne(String query, Object... params) {
        List<T> result = jdbc.query(query, mapper, params);
        if (result.isEmpty()) {
            throw new InternalServerException("Не удалось найти данные");
        }
        return result.getFirst();
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.queryForList(query, entityType, params);
    }

    public boolean delete(String query, long id) {
        int rowsDeleted = jdbc.update(query, id);
        return rowsDeleted > 0;
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected void batchUpdateBase(String query, BatchPreparedStatementSetter bps) {
        int[] rowUpdate = jdbc.batchUpdate(query, bps);
        if(rowUpdate.length == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }
}
