package com.boyouquan.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.Date;

public class CustomDateSerializer extends ValueSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeString(CommonUtils.dateHourSecondCommonFormatDisplay(date));
    }

}