package microarch.delivery.core.domain.model.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Location Value Object Tests")
class LocationTest {

    @Test
    @DisplayName("Should create location with valid coordinates")
    void shouldCreateLocationWithValidCoordinates() {
        Location location = Location.create(5, 5);
        assertNotNull(location);
    }

    @Test
    @DisplayName("Should create location at minimum boundaries (1,1)")
    void shouldCreateLocationAtMinimumBoundaries() {
        assertDoesNotThrow(() -> Location.create(1, 1));
    }

    @Test
    @DisplayName("Should create location at maximum boundaries (10,10)")
    void shouldCreateLocationAtMaximumBoundaries() {
        assertDoesNotThrow(() -> Location.create(10, 10));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 5, 10 })
    @DisplayName("Should accept valid X coordinates")
    void shouldAcceptValidXCoordinates(int x) {
        assertDoesNotThrow(() -> Location.create(x, 5));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 5, 10 })
    @DisplayName("Should accept valid Y coordinates")
    void shouldAcceptValidYCoordinates(int y) {
        assertDoesNotThrow(() -> Location.create(5, y));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when X is less than 1")
    void shouldThrowExceptionWhenXIsLessThanOne() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Location.create(0, 5));
        assertEquals("x must be between 1 and 10", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when X is greater than 10")
    void shouldThrowExceptionWhenXIsGreaterThanTen() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Location.create(11, 5));
        assertEquals("x must be between 1 and 10", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when Y is less than 1")
    void shouldThrowExceptionWhenYIsLessThanOne() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Location.create(5, 0));
        assertEquals("y must be between 1 and 10", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when Y is greater than 10")
    void shouldThrowExceptionWhenYIsGreaterThanTen() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Location.create(5, 11));
        assertEquals("y must be between 1 and 10", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate X first when both coordinates are invalid")
    void shouldValidateXFirstWhenBothCoordinatesAreInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Location.create(0, 11));
        assertEquals("x must be between 1 and 10", exception.getMessage());
    }

    @Test
    @DisplayName("Should be equal to another location with same coordinates")
    void shouldBeEqualToAnotherLocationWithSameCoordinates() {
        Location location1 = Location.create(3, 7);
        Location location2 = Location.create(3, 7);

        assertEquals(location1, location2);
        assertEquals(location1.hashCode(), location2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        Location location = Location.create(5, 5);
        assertNotEquals(null, location);
    }

    @Test
    @DisplayName("Should not be equal when X differs")
    void shouldNotBeEqualWhenXDiffers() {
        Location location1 = Location.create(3, 5);
        Location location2 = Location.create(4, 5);
        assertNotEquals(location1, location2);
    }

    @Test
    @DisplayName("Should not be equal when Y differs")
    void shouldNotBeEqualWhenYDiffers() {
        Location location1 = Location.create(5, 3);
        Location location2 = Location.create(5, 4);
        assertNotEquals(location1, location2);
    }

    @Test
    @DisplayName("Should maintain symmetry in equality")
    void shouldMaintainSymmetryInEquality() {
        Location location1 = Location.create(5, 5);
        Location location2 = Location.create(5, 5);
        assertEquals(location1.equals(location2), location2.equals(location1));
    }

    @Test
    @DisplayName("Should maintain transitivity in equality")
    void shouldMaintainTransitivityInEquality() {
        Location location1 = Location.create(5, 5);
        Location location2 = Location.create(5, 5);
        Location location3 = Location.create(5, 5);

        assertEquals(location1, location2);
        assertEquals(location2, location3);
        assertEquals(location1, location3);
    }

    @Test
    @DisplayName("Should calculate zero distance to itself")
    void shouldCalculateZeroDistanceToItself() {
        Location location = Location.create(5, 5);
        assertEquals(0, location.distanceTo(location));
    }

    @Test
    @DisplayName("Should calculate zero distance to equal location")
    void shouldCalculateZeroDistanceToEqualLocation() {
        Location location1 = Location.create(5, 5);
        Location location2 = Location.create(5, 5);
        assertEquals(0, location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should calculate distance only in X direction")
    void shouldCalculateDistanceOnlyInXDirection() {
        Location location1 = Location.create(3, 5);
        Location location2 = Location.create(7, 5);
        assertEquals(4, location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should calculate distance only in Y direction")
    void shouldCalculateDistanceOnlyInYDirection() {
        Location location1 = Location.create(5, 2);
        Location location2 = Location.create(5, 8);
        assertEquals(6, location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should calculate distance in both X and Y directions")
    void shouldCalculateDistanceInBothDirections() {
        Location location1 = Location.create(1, 1);
        Location location2 = Location.create(4, 5);
        assertEquals(7, location1.distanceTo(location2));
    }

    @Test
    @DisplayName("Should calculate distance symmetrically")
    void shouldCalculateDistanceSymmetrically() {
        Location location1 = Location.create(2, 3);
        Location location2 = Location.create(8, 9);

        int distance1to2 = location1.distanceTo(location2);
        int distance2to1 = location2.distanceTo(location1);

        assertEquals(distance1to2, distance2to1);
        assertEquals(12, distance1to2);
    }
}
