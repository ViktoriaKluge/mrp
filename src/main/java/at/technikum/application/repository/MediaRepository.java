package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.List;
import java.util.Optional;

public interface MediaRepository {

    Optional<Media> findById(String id);
    List<Media> findAll();
    void save(Media media);
    void delete(String id);
    void update(Media media);

    void add(String id);

    boolean find(String id);
}
