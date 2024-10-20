package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int nextId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    //Получаем фильмы
    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    private int getNextId() {
        return nextId++;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            throw new ValidationException("Фильм по такому id не найден");
        }
        Film film = films.get(newFilm.getId());
        if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
            film.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) {
            film.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            film.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() >= 0) {
            film.setDuration(newFilm.getDuration());
        }
        log.info("Фильм обновлен: {}", film);
        films.put(film.getId(), film);
        return film;
    }
}
