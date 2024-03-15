package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the logbook.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final RoomNumber roomNumber;
    private final Telegram telegram;
    private final Birthday birthday;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, RoomNumber roomNumber, Telegram telegram, Birthday birthday) {
        requireAllNonNull(name, phone, email, roomNumber, telegram, birthday);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.roomNumber = roomNumber;
        this.telegram = telegram;
        this.birthday = birthday;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public Birthday getBirthday() {
        return birthday;
    }


    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && roomNumber.equals(otherPerson.roomNumber)
                && telegram.equals(otherPerson.telegram)
                && birthday.equals(otherPerson.birthday);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, roomNumber, telegram, birthday);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("roomNumber", roomNumber)
                .add("telegram", telegram)
                .add("birthday", birthday)
                .toString();
    }

}
