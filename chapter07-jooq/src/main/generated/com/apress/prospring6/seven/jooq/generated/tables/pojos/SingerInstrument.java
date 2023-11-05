/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SingerInstrument implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer singerId;
    private String instrumentId;

    public SingerInstrument() {}

    public SingerInstrument(SingerInstrument value) {
        this.singerId = value.singerId;
        this.instrumentId = value.instrumentId;
    }

    public SingerInstrument(
        Integer singerId,
        String instrumentId
    ) {
        this.singerId = singerId;
        this.instrumentId = instrumentId;
    }

    /**
     * Getter for <code>musicdb.SINGER_INSTRUMENT.SINGER_ID</code>.
     */
    public Integer getSingerId() {
        return this.singerId;
    }

    /**
     * Setter for <code>musicdb.SINGER_INSTRUMENT.SINGER_ID</code>.
     */
    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    /**
     * Getter for <code>musicdb.SINGER_INSTRUMENT.INSTRUMENT_ID</code>.
     */
    public String getInstrumentId() {
        return this.instrumentId;
    }

    /**
     * Setter for <code>musicdb.SINGER_INSTRUMENT.INSTRUMENT_ID</code>.
     */
    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SingerInstrument other = (SingerInstrument) obj;
        if (this.singerId == null) {
            if (other.singerId != null)
                return false;
        }
        else if (!this.singerId.equals(other.singerId))
            return false;
        if (this.instrumentId == null) {
            if (other.instrumentId != null)
                return false;
        }
        else if (!this.instrumentId.equals(other.instrumentId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.singerId == null) ? 0 : this.singerId.hashCode());
        result = prime * result + ((this.instrumentId == null) ? 0 : this.instrumentId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SingerInstrument (");

        sb.append(singerId);
        sb.append(", ").append(instrumentId);

        sb.append(")");
        return sb.toString();
    }
}
