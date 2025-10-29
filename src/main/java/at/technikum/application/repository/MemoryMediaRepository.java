package at.technikum.application.repository;

import at.technikum.application.model.Media;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryMediaRepository implements MediaRepository {
    private List<Media> mediaList;

    public MemoryMediaRepository() {
        Media media = new Media();
        this.mediaList = media.createMockMedia() ;
    }

    @Override
    public Media findById(String id) {
        for (Media media : mediaList) {
            if (media.getId().equals(id)) {
                return media;
            }
        }
        return null;
    }

    @Override
    public List<Media> mediaList() {
        return mediaList;
    }

    @Override
    public Media save(Media media) {
        for (Media m : mediaList) {
            if (m.getTitle().equals(media.getTitle()) &&
                m.getMediaType().equals(media.getMediaType())) {
                return null;
            }
        }
        this.mediaList.add(media);
        return media;
    }

    @Override
    public String delete(String id) {
        for (Media media : mediaList) {
            if (media.getId().equals(id)) {
                String title = media.getTitle();
                mediaList.remove(media);
                return title;
            }
        }
        return null;
    }

    @Override
    public Media update(Media media) {
        return null;
    }

    @Override
    public Media addRating(String id) {
        for (Media media : mediaList) {
            if (media.getId().equals(id)) {
              //  media.setRating
            }
        }

        return null;
    }

    @Override
    public Media removeRating(String id) {
        return null;
    }

    @Override
    public Media addFavorite(String id) {
        return null;
    }

    @Override
    public Media removeFavorite(String id) {
        return null;
    }
}
