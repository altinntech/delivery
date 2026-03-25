package microarch.delivery.core.domain.model.general;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@DisplayName("Location Value Object Tests")
public class LocationTest {

    @Test
    @DisplayName("Should create location with valid coordinates")
    public void shouldCreateLocationWithValidCoordinates() {

        var  location = Location.create(5, 5);

        assertThat(location.isSuccess()).isTrue();
        assertThat(location.getValue().getX()).isEqualTo(5);
        assertThat(location.getValue().getY()).isEqualTo(5);
    }

    @ParameterizedTest
    @DisplayName("Should not create invalid object: less 1 or bigger than 10")
    @CsvSource ({"0,5","11,5","4,0","4,11","0,11"})
    public void shouldNotCreateInvalidObject (int x, int y) {

        var location =  Location.create(x,y);

        assertThat(location.isFailure()).isTrue();

    }

    @Test
    @DisplayName("Should be equal to another location with same coordinates")
    public void shouldBeEqualToAnotherLocationWithSameCoordinates() {

        Location location1 = Location.create(3, 7).getValue();
        Location location2 = Location.create(3, 7).getValue();

        assertEquals(location1, location2);
        assertEquals(location1.hashCode(), location2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when X differs")
    public void shouldNotBeEqualWhenXDiffers() {
        Location location1 = Location.create(3, 5).getValue();
        Location location2 = Location.create(4, 5).getValue();
        assertNotEquals(location1, location2);
    }

    @Test
    @DisplayName("Should not be equal when Y differs")
    public void shouldNotBeEqualWhenYDiffers() {
        Location location1 = Location.create(4, 6).getValue();
        Location location2 = Location.create(4, 5).getValue();
        assertNotEquals(location1, location2);
    }

    @Test
    @DisplayName("Should calculate zero distance to itself")
    public void shouldCalculateZeroDistanceToItself() {
        Location location = Location.create(5, 5).getValue();
        assertEquals(0, location.distanceTo(location));
    }

    @Test
    @DisplayName("Should calculate zero distance to equal location")
    public void shouldCalculateZeroDistanceToEqualLocation() {
        Location location1 = Location.create(5, 5).getValue();
        Location location2 = Location.create(5, 5).getValue();
        assertEquals(0, location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should calculate distance by requirements |X1-X2| + |Y1-Y2|")
    public void shouldCalculateDistance() {
        Location location1 = Location.create(2, 6).getValue();
        Location location2 = Location.create(4, 9).getValue();
        assertEquals(2+3, location1.distanceTo(location2));
    }

}
