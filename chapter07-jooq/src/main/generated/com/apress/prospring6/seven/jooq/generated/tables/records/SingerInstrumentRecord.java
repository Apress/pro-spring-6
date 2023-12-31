/*
 * This file is generated by jOOQ.
 */
package com.apress.prospring6.seven.jooq.generated.tables.records;


import com.apress.prospring6.seven.jooq.generated.tables.SingerInstrument;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SingerInstrumentRecord extends UpdatableRecordImpl<SingerInstrumentRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>musicdb.SINGER_INSTRUMENT.SINGER_ID</code>.
     */
    public void setSingerId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>musicdb.SINGER_INSTRUMENT.SINGER_ID</code>.
     */
    public Integer getSingerId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>musicdb.SINGER_INSTRUMENT.INSTRUMENT_ID</code>.
     */
    public void setInstrumentId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>musicdb.SINGER_INSTRUMENT.INSTRUMENT_ID</code>.
     */
    public String getInstrumentId() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Integer, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return SingerInstrument.SINGER_INSTRUMENT.SINGER_ID;
    }

    @Override
    public Field<String> field2() {
        return SingerInstrument.SINGER_INSTRUMENT.INSTRUMENT_ID;
    }

    @Override
    public Integer component1() {
        return getSingerId();
    }

    @Override
    public String component2() {
        return getInstrumentId();
    }

    @Override
    public Integer value1() {
        return getSingerId();
    }

    @Override
    public String value2() {
        return getInstrumentId();
    }

    @Override
    public SingerInstrumentRecord value1(Integer value) {
        setSingerId(value);
        return this;
    }

    @Override
    public SingerInstrumentRecord value2(String value) {
        setInstrumentId(value);
        return this;
    }

    @Override
    public SingerInstrumentRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SingerInstrumentRecord
     */
    public SingerInstrumentRecord() {
        super(SingerInstrument.SINGER_INSTRUMENT);
    }

    /**
     * Create a detached, initialised SingerInstrumentRecord
     */
    public SingerInstrumentRecord(Integer singerId, String instrumentId) {
        super(SingerInstrument.SINGER_INSTRUMENT);

        setSingerId(singerId);
        setInstrumentId(instrumentId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised SingerInstrumentRecord
     */
    public SingerInstrumentRecord(com.apress.prospring6.seven.jooq.generated.tables.pojos.SingerInstrument value) {
        super(SingerInstrument.SINGER_INSTRUMENT);

        if (value != null) {
            setSingerId(value.getSingerId());
            setInstrumentId(value.getInstrumentId());
            resetChangedOnNotNull();
        }
    }
}
