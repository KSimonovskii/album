package telran.album.dao;

import telran.album.model.Photo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlbumImpl implements Album {

    private Photo[] photos;
    private int currSize;

    public AlbumImpl(int capacity) {
        this.photos = new Photo[capacity];
    }

    @Override
    public boolean addPhoto(Photo photo) {

        if (photo == null
                || photos.length == currSize
                || getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbumId()) != null) {
            return false;
        }

        photos[currSize++] = photo;
        return true;

    }

    @Override
    public boolean removePhoto(int photoId, int albumId) {

        Predicate<Photo> predicate = (p) -> p.getAlbumId() == albumId && p.getPhotoId() == photoId;
        for (int i = 0; i < currSize; i++) {
            if (!predicate.test(photos[i])) {
                continue;
            }

            System.arraycopy(photos, i + 1, photos, i, currSize - 1 - i);
            photos[--currSize] = null;

            return true;
        }

        return false;
    }

    @Override
    public boolean updatePhoto(int photoId, int albumId, String url) {

        Photo photo = getPhotoFromAlbum(photoId, albumId);
        if (photo == null) {
            return false;
        }

        if (!photo.getUrl().equals(url)) {
            photo.setUrl(url);
        }
        return true;
    }

    @Override
    public Photo getPhotoFromAlbum(int photoId, int albumId) {

        Predicate <Photo> predicate = photo -> photo.getPhotoId() == photoId && photo.getAlbumId() == albumId;

        for (int i = 0; i < currSize; i++) {
            if (predicate.test(photos[i])) {
                return photos[i];
            }

        }
        return null;
    }

    @Override
    public Photo[] getAllPhotoFromAlbum(int albumId) {
        return getSearchResult(p -> p.getAlbumId() == albumId);
    }

    @Override
    public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
        return getSearchResult(p -> p.getDate().isAfter(dateFrom.atStartOfDay()) && p.getDate().isBefore(dateTo.atStartOfDay()));
    }

    private Photo[] getSearchResult(Predicate<Photo> predicate){

        Photo[] res = new Photo[currSize];

        int count = 0;
        for (int i = 0; i < currSize; i++) {
            if(!predicate.test(photos[i])){
                continue;
            }

            res[count++] = photos[i];
        }

        return count > 0? Arrays.copyOfRange(res, 0, count) : null;

    }

    @Override
    public int size() {
        return currSize;
    }
}
