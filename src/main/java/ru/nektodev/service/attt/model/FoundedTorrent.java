package ru.nektodev.service.attt.model;

import com.google.common.base.Objects;

/**
 * @author nektodev
 * @date 11/04/2017
 */
public class FoundedTorrent {

    private String id;
    private String name;
    private String url;
    private String category;

    private int seeders;
    private long size;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("url", url)
                .add("category", category)
                .add("seeders", seeders)
                .add("size", size)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoundedTorrent that = (FoundedTorrent) o;
        return seeders == that.seeders &&
                size == that.size &&
                Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(url, that.url) &&
                Objects.equal(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, url, category, seeders, size);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSeeders() {
        return seeders;
    }

    public void setSeeders(int seeders) {
        this.seeders = seeders;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
