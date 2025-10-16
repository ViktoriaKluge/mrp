package at.technikum.application.service;

import at.technikum.application.exception.EntityNotFoundException;
import at.technikum.application.repository.FavoritesRepository;

public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    public FavoritesService(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    public void add(String id) {

        if (exists(id)) {
            this.favoritesRepository.add(id);
        }
    }

    public void delete(String id) {

        if (exists(id)) {
            this.favoritesRepository.delete(id);
        }
    }

    private boolean exists(String id) {
        boolean exists = false;

        exists = this.favoritesRepository.find(id);
        if (!exists) {
            throw (EntityNotFoundException) new EntityNotFoundException();
        }

        return exists;
    }
}
