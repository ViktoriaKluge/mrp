package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;

public class MemoryMediaRepository implements MediaRepository {
    @Override
    public Media findById(String id) {
        return null;
    }

    @Override
    public List<Media> mediaList() {
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

}
