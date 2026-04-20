package microarch.delivery.adapters.in.http.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

/**
 * CreateCourierResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-20T14:50:53.620457041+03:00[Europe/Moscow]")
public class CreateCourierResponse {

  private UUID courierId;

  public CreateCourierResponse courierId(UUID courierId) {
    this.courierId = courierId;
    return this;
  }

  /**
   * Get courierId
   * @return courierId
  */
  @Valid 
  @Schema(name = "courierId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("courierId")
  public UUID getCourierId() {
    return courierId;
  }

  public void setCourierId(UUID courierId) {
    this.courierId = courierId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCourierResponse createCourierResponse = (CreateCourierResponse) o;
    return Objects.equals(this.courierId, createCourierResponse.courierId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(courierId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCourierResponse {\n");
    sb.append("    courierId: ").append(toIndentedString(courierId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

