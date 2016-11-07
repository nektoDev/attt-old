package ru.nektodev.service.attt.model;

import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author nektodev
 * @date 07/11/2016
 */
public class TrackedTorrent implements Serializable {
    @Id
    private String id;
    private String torrentInfoId;
    private Date lastCheckDate;
    private Date lastUpdateDate;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("torrentInfoId", torrentInfoId)
                .add("lastCheckDate", lastCheckDate)
                .add("lastUpdateDate", lastUpdateDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedTorrent that = (TrackedTorrent) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(torrentInfoId, that.torrentInfoId) &&
                Objects.equal(lastCheckDate, that.lastCheckDate) &&
                Objects.equal(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, torrentInfoId, lastCheckDate, lastUpdateDate);
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTorrentInfoId() {
        return torrentInfoId;
    }

    public void setTorrentInfoId(String torrentInfoId) {
        this.torrentInfoId = torrentInfoId;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
