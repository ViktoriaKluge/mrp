package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;

public class MemoryMediaRepository implements MediaRepository {
    @Override
    public Optional<Media> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Media> findAll() {
        return List.of();
    }

    @Override
    public void save(Media media) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(Media media) {

    }

    @Override
    public void add(String id) {

    }

    @Override
    public boolean find(String id) {
        return false;
    }
}
