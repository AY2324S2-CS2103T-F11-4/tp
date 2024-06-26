package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREETIMETAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RoomNumber;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.DormTag;
import seedu.address.model.tag.FreeTimeTag;

/**
 * Deletes a free time to an existing person in the address book.
 */
public class DeleteTimeCommand extends Command {

    public static final String COMMAND_WORD = "deleteTime";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a free time from the person identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_FREETIMETAG + "FREE TIME TAG...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_FREETIMETAG + "Mon:1300-1400";

    public static final String MESSAGE_DELETE_FREETIME_SUCCESS = "Deleted free time from person: %1$s";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public DeleteTimeCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_FREETIME_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Set<FreeTimeTag> freeTimeTags = personToEdit.getTags();
        Set<FreeTimeTag> updatedFreeTimeTags = new HashSet<>();

        // Case 1: No free time are specified
        if (editPersonDescriptor.getTags() == null) {
            throw new CommandException(Messages.MESSAGE_NO_FREETIME_SPECIFIED);
        }

        Set<FreeTimeTag> deleteFreeTimeTags = editPersonDescriptor.getTags();

        if (deleteFreeTimeTags == null) {
            throw new CommandException(Messages.MESSAGE_NO_FREETIME_SPECIFIED);
        }

        if (freeTimeTags != null) {
            if (deleteFreeTimeTags.isEmpty()) {
                throw new CommandException(FreeTimeTag.MESSAGE_CONSTRAINTS);
            }
            for (FreeTimeTag freeTimeTag : freeTimeTags) {
                if (!deleteFreeTimeTags.contains(freeTimeTag)) {
                    updatedFreeTimeTags.add(freeTimeTag);
                }
            }
        }

        // Case 2: None of the specified free time(s) match any of the person's free time
        if (freeTimeTags.size() == updatedFreeTimeTags.size()) {
            throw new CommandException(Messages.MESSAGE_NO_MATCHING_FREE_TIME);
        }

        // Case 3: At least one of the specified free time(s) match any of the person's free time
        return createdEditedPersonHelper(personToEdit, updatedFreeTimeTags);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * with the updated free time tags {@code updatedFreeTimeTags}.
     */
    private static Person createdEditedPersonHelper(Person personToEdit, Set<FreeTimeTag> updatedFreeTimeTags) {
        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        RoomNumber updatedRoomNumber = personToEdit.getRoomNumber();
        Telegram updatedTelegram = personToEdit.getTelegram();
        Birthday updatedBirthday = personToEdit.getBirthday();
        DormTag updatedDormTag = personToEdit.getDormTag();

        return new Person(updatedName, updatedPhone, updatedEmail, updatedRoomNumber, updatedTelegram,
                updatedBirthday, updatedDormTag, updatedFreeTimeTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTimeCommand)) {
            return false;
        }

        DeleteTimeCommand otherDeleteTimeCommand = (DeleteTimeCommand) other;
        return index.equals(otherDeleteTimeCommand.index)
                && editPersonDescriptor.equals(otherDeleteTimeCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Set<FreeTimeTag> tags;
        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setTags(toCopy.tags);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<FreeTimeTag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Set<FreeTimeTag> getTags() {
            return tags;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(tags, otherEditPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("freeTimeTags", tags)
                    .toString();
        }
    }
}
