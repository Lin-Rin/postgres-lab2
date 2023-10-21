package org.example.model;

public class Row {
    private Long userId;
    private Long counter;
    private Long version;

    public Row(Long counter, Long version) {
        this.counter = counter;
        this.version = version;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVersion() {
        return version;
    }

    public Long getCounter() {
        return counter;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Row{" +
                "userId=" + userId +
                ", counter=" + counter +
                ", version=" + version +
                '}';
    }
}
