
package org.mule.extension.jsonlogger.api.pojos;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;


/**
 * Definition for fields used in the logger message processor
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scopeTracePoint"
})
public class LoggerScopeProcessor {

    @JsonProperty("scopeTracePoint")
    @Parameter
    @Optional(defaultValue = "OUTBOUND_REQUEST_SCOPE")
    @Summary("Current processing stage")
    private ScopeTracePoint scopeTracePoint;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scopeTracePoint")
    public ScopeTracePoint getScopeTracePoint() {
        return scopeTracePoint;
    }

    @JsonProperty("scopeTracePoint")
    public void setScopeTracePoint(ScopeTracePoint scopeTracePoint) {
        this.scopeTracePoint = scopeTracePoint;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scopeTracePoint).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LoggerScopeProcessor) == false) {
            return false;
        }
        LoggerScopeProcessor rhs = ((LoggerScopeProcessor) other);
        return new EqualsBuilder().append(scopeTracePoint, rhs.scopeTracePoint).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
