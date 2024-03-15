package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class BirthdayTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Birthday(null));
    }

    @Test
    public void constructor_invalidRoomNumber_throwsIllegalArgumentException() {
        String invalidBirthday = "";
        assertThrows(IllegalArgumentException.class, () -> new Birthday(invalidBirthday));
    }

    @Test
    public void isValidBirthday() {
        // null address
        assertThrows(NullPointerException.class, () -> Birthday.isValidBirthday(null));

        // invalid addresses
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only

        // valid addresses
        assertTrue(Birthday.isValidBirthday("12-12"));
        assertTrue(Birthday.isValidBirthday("01-02"));
        assertTrue(Birthday.isValidBirthday("03-01"));
    }

    @Test
    public void equals() {
        Birthday birthday = new Birthday("01/01/2001");

        // same values -> returns true
        assertTrue(birthday.equals(new Birthday("01/01/2001")));

        // same object -> returns true
        assertTrue(birthday.equals(birthday));

        // null -> returns false
        assertFalse(birthday.equals(null));

        // different types -> returns false
        assertFalse(birthday.equals(5.0f));

        // different values -> returns false
        assertFalse(birthday.equals(new Birthday("01/02/2001")));
    }
}
