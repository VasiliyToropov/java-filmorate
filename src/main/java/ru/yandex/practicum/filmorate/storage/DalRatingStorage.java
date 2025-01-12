package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DalRatingStorage {

    private final RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.getAllRatings();
    }

    public Rating getRating(Long id) {
        Optional<Rating> result = ratingRepository.getRating(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new NotFoundException("Пользователь с таким ID не найден");
    }
}
