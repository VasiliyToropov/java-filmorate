package ru.yandex.practicum.filmorate.interfaces;

public interface Validator<T> {
    void validate(T value);
}
