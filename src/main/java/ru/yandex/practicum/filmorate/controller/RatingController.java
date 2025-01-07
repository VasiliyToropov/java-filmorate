package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DalRatingStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final DalRatingStorage dalRatingStorage;

    @GetMapping
    public List<Rating> getAllRatings() {
        return dalRatingStorage.getAllRatings();
    }

    @GetMapping("/{id}")
    public Rating getRating(@PathVariable long id) {
        return dalRatingStorage.getRating(id);
    }
}
