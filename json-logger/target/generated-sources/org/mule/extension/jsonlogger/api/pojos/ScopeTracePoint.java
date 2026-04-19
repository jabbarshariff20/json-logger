
package org.mule.extension.jsonlogger.api.pojos;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ScopeTracePoint {

    DATA_TRANSFORM_SCOPE("DATA_TRANSFORM_SCOPE"),
    OUTBOUND_REQUEST_SCOPE("OUTBOUND_REQUEST_SCOPE"),
    FLOW_LOGIC_SCOPE("FLOW_LOGIC_SCOPE");
    private final String value;
    private final static Map<String, ScopeTracePoint> CONSTANTS = new HashMap<String, ScopeTracePoint>();

    static {
        for (ScopeTracePoint c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ScopeTracePoint(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static ScopeTracePoint fromValue(String value) {
        ScopeTracePoint constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
