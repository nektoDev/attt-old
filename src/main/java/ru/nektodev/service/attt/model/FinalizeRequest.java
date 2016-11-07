package ru.nektodev.service.attt.model;

import com.google.common.base.Objects;

/**
 * @author nektodev
 * @date 07/11/2016
 */
public class FinalizeRequest {
    private String hash;
    private String name;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("hash", hash)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalizeRequest that = (FinalizeRequest) o;
        return Objects.equal(hash, that.hash) &&
                Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash, name);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
