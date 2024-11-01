package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class CountComparator implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        int numberOfFilmsO1 = o1.getWhoLiked().size();
        int numberOfFilmsO2 = o2.getWhoLiked().size();
        return Integer.compare(numberOfFilmsO1, numberOfFilmsO2);
    }
}
