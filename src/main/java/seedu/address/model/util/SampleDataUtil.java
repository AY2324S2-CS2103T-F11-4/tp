package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RoomNumber;
import seedu.address.model.person.Telegram;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new RoomNumber("06-40"), new Telegram("alexYeoh"), new Birthday("03/02/2000")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new RoomNumber("07-18"), new Telegram("berniceYu"), new Birthday("21/02/1999")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new RoomNumber("11-04"), new Telegram("charlotteOliveiro"), new Birthday("02/04/2000")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new RoomNumber("16-43"), new Telegram("davidLi"), new Birthday("21/04/1998")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new RoomNumber("17-35"), new Telegram("irfanIbrahim"), new Birthday("16/04/1993")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new RoomNumber("11-31"), new Telegram("royBalakrishnan"), new Birthday("18/09/1992"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
