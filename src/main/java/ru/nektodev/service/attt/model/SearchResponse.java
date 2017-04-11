package ru.nektodev.service.attt.model;

import com.google.common.base.Objects;

import java.util.List;

/**
 * @author nektodev
 * @date 10/04/2017
 */
public class SearchResponse {
    List<FoundedTorrent> rutracker;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("rutracker", rutracker)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponse that = (SearchResponse) o;
        return Objects.equal(rutracker, that.rutracker);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rutracker);
    }

    public List<FoundedTorrent> getRutracker() {

        return rutracker;
    }

    public void setRutracker(List<FoundedTorrent> rutracker) {
        this.rutracker = rutracker;
    }
}
