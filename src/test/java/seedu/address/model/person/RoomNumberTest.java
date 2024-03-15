package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoomNumberTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoomNumber(null));
    }

    @Test
    public void constructor_invalidRoomNumber_throwsIllegalArgumentException() {
        String invalidRoomNumber = "";
        assertThrows(IllegalArgumentException.class, () -> new RoomNumber(invalidRoomNumber));
    }

    @Test
    public void isValidRoomNumber() {
        // null address
        assertThrows(NullPointerException.class, () -> RoomNumber.isValidRoomNumber(null));

        // invalid addresses
        assertFalse(RoomNumber.isValidRoomNumber("")); // empty string
        assertFalse(RoomNumber.isValidRoomNumber(" ")); // spaces only

        // valid addresses
        assertTrue(RoomNumber.isValidRoomNumber("12-12"));
        assertTrue(RoomNumber.isValidRoomNumber("01-02"));
        assertTrue(RoomNumber.isValidRoomNumber("03-01"));
    }

    @Test
    public void equals() {
        RoomNumber roomNumber = new RoomNumber("01-01");

        // same values -> returns true
        assertTrue(roomNumber.equals(new RoomNumber("01-01")));

        // same object -> returns true
        assertTrue(roomNumber.equals(roomNumber));

        // null -> returns false
        assertFalse(roomNumber.equals(null));

        // different types -> returns false
        assertFalse(roomNumber.equals(5.0f));

        // different values -> returns false
        assertFalse(roomNumber.equals(new RoomNumber("02-02")));
    }
}
