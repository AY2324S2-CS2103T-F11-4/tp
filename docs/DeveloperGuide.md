---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# Dormie Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

 _[Codium](https://www.codium.ai/) - Used by [donwong2308](https://github.com/donwong2308) to autocomplete code in some parts of FreeTimeTag and FreeTimeTagTest._

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### **Free Time Tag Feature**
#### Implementation
The implementation consists of two main classes: `Tag` and `FreeTimeTag`.

1. #### Tag Class

- `Tag` is an abstract class representing a general tag in Dormie.
- It contains the common properties and behaviors shared by all types of tags.
- The `tagName` field represents the name of the tag.
- Methods such as `equals`, `hashCode`, and `toString` are declared abstract to be implemented by subclasses.

2. #### FreeTimeTag Class:

- `FreeTimeTag` is a subclass of `Tag` specifically designed for free time tags.
- It adds additional constraints and validation specific to free time tags.
- The `MESSAGE_CONSTRAINTS` constant defines the validation message for free time tags.
- The `VALIDATION_REGEX` constant specifies the regex pattern for valid free time tag format.
- The constructor ensures that the provided tag name meets the required format.
- Additional methods such as `isValidTagName` validate the tag name against the defined regex pattern.

### Add Free Time Command

#### Implementation

The add free time mechanism is a version of the `EditCommand`. Instead of replacing specified field values of a current contact, the `AddTimeCommand` appends to the current freeTimeTags hashset.

Given below is an example usage scenario and how the add free time mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

Step 2. The user executes `add n/Jane …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 3. The user now wants to add another free time for a friend, and does so by executing the `addTime [index] ft/Wed:1000-1100` command. The `addTime` command, after successfully passing the parser, will retrieve the current FreeTimeTags HashSet. It will then append, in order of day, the new free time to the HashSet. That is, the new free time 1000-1100 on Wednesday will be appended just after timings that fall before Wednesday 1000.

Note: The user can add multiple free times at the same time by using multiple `ft/` flags in the command. An example is `addTime [index] ft/Wed:1000-1100 ft/Thu:1200-1400...`. The current code implementation will loop through the freeTime HashSet n times, with n being the number of free time tags to be added.
</box>

The following sequence diagram shows how an addFreeTime operation goes through the `Logic` component:

<puml src="diagrams/AddFreeTimeSequenceDiagram-Logic.puml" alt="AddFreeTimeSequenceDiagram-Logic" />

#### Design Considerations:

**Aspect: How add free time executes:**

* **Alternative 1 (current implementation):** Append to HashSet in order.
    * Pros: Easy to visualise in GUI.
    * Cons: Additional time to loop through current HashSet to append in the current position.

* **Alternative 2:** Append to end of HashSet
    * Pros: Easy to implement because the new free time can just be appended at the end of the HashSet.
    * Cons: Difficult to visualise in GUI (free time in Monday may appear after Tuesday's).

### Delete Free Time Command

#### Implementation

The delete free time mechanism is a version of the `addFreeTimeCommand`. Instead of adding free time tags to the contact's freeTimeTags HashSet, the `DeleteTimeCommand` removes tags that match the requested tag from the current freeTimeTags hashset.

Given below is an example usage scenario and how the delete free time mechanism behaves at each step.

Step 1. The user has launched the application and added a new person called Jane using the add command `add n/Jane …​`.

Step 2. Then, the user adds free time to Jane's freeTimeTags HashSet using the command `addTime [index of Jane] ft/Wed:1000-1100`.

Step 3. However, the user realises a mistake has been made and needs to remove the recently added free time. The user can do so using the command  `deleteTime [index of Jane] ft/Wed:1000-1100`.

Note: The user can delete multiple free times at the same time by using multiple `ft/` flags in the command. An example is `deleteTime [index] ft/Wed:1000-1100 ft/Thu:1200-1400...`. The current code implementation will loop through the freeTime HashSet n times, with n being the number of free time tags to be deleted.
</box>

#### Design Considerations:

**Aspect: How delete free time executes:**

* **Alternative 1 (current implementation):** Delete from HashSet only if time interval exactly matches a current free time in the freeTimeTags HashSet. For example, `deleteTime [index] ft/Wed:1000-1100` will delete the `Wed:1000-1100` free time tag, but not the `Wed:1000-1200` free time tag.
    * Pros: Causes fewer bugs and the main functionality is still met.
    * Cons: User input will need to accurately match the free time tag to be deleted.

* **Alternative 2:** Delete from the HashSet and
    * Pros: More convenient if the user wants to delete multiple free times at once. For example, `deleteTime [index] ft/Wed:1000-1200` will delete the `Wed:1000-1100` and `Wed:1100-1200` free time tags.
    * Cons: Could introduce more bugs that are more challenging to resolve in a short period of time (Given the current time constraints).

#### Design considerations:

**Aspect: How add free time executes:**

* **Alternative 1 (current choice):** Append to HashSet in order.
    * Pros: Easy to visualise in GUI.
    * Cons: Additional time to loop through current HashSet to append in the current position.

* **Alternative 2:** Append to end of HashSet
    * Pros: Easy to implement because the new free time can just be appended at the end of the HashSet.
    * Cons: Difficult to visualise in GUI (free time in Monday may appear after Tuesday's).

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* an undergraduate student staying in a dorm
* wants to network with other students who stay in the same dorm as him
* often looks for his dorm friends to hang out or do some work
* enjoys celebrating milestones such as birthdays

**Value proposition**: Jim will be able to create and update student contacts quickly. 
He will be able to store important dorm details in his contacts. 
He can also schedule meetups with peers with more convenience.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                   | I can …​                                                | So that …​                                                                                      |
|----------|-------------------------------------------|---------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `* * *`  | student who just started living in dorm   | create a new contact                                    | I can remember the particulars of a new dorm mate                                               |
| `* * *`  | student living in dorm                    | choose to specify the room number upon contact creation | I do not need to update my dorm mate’s room number separately                                   |
| `* * *`  | student living in dorm                    | choose to specify the birthday upon contact creation    | I do not need to update my dorm mate’s birthday separately                                      |
| `* * *`  | student living in dorm                    | delete a contact                                        | I can stay updated on who no longer resides in the dorm                                         |
| `* * *`  | student living in dorm                    | edit a contact’s name                                   | I can change the name if it was initially created incorrectly or the name has been changed      |
| `* * *`  | student living in dorm                    | edit a contact’s room number                            | I can stay updated if my dorm mate changes room                                                 |
| `* * *`  | student living in dorm                    | view all contacts                                       | I can keep track of how to find my dorm mates if I need their help AND remember their birthdays |
| `* *`    | student living in dorm                    | add when my friends are free                            | I can remember when my friends are available                                                    |
| `* *`    | student living in dorm                    | view which of my friends are free at a certain time     | I know who I can ask to meet for leisure or work                                                |
| `* *`    | student living in dorm                    | update when my friends are free                         | I ensure that the free times stored are accurate                                                |
| `* *`    | new user of this application              | view allowed commands when the application launches     | I am aware of what functions I can use in the application                                       |
| `* *`    | user exploring this application           | get autocomplete when typing commands                   | I can quickly give my commands                                                                  |
| `* *`    | student living in dorm with many contacts | search a contact by name                                | I can quickly find details of my dorm mates                                                     |
| `* *`    | student living in dorm with many contacts | filter contacts by dorm room number                     | I can find where are my dorm mates                                                              |
| `* *`    | student living in dorm with many contacts | search a contact by birthday                            | I know whose birthday is in which month                                                         |
| `* *`    | student living in dorm                    | add a profile picture for each contact                  | I can recognise and identify the contact person                                                 |
| `* *`    | student living in dorm                    | add telegram handle for each contact                    | I can contact them on telegram as an alternative to their phone number                          |
| `*`      | student living in dorm who enjoys events  | add upcoming events                                     | I can remember what events are coming up                                                        |
| `*`      | student living in dorm who enjoys events  | view upcoming events                                    | I can plan for them accordingly                                                                 |
| `*`      | student living in dorm who enjoys events  | view upcoming birthdays                                 | I can plan a celebration and wish them happy birthday                                           |
| `*`      | new user of this application              | enter a command to get help for using this application  | I can find the command I want to use                                                            |
| `*`      | user exploring this application           | toggle between dark and light mode                      | I can make my view of the application more comfortable to the eye                               |
| `*`      | user exploring this application           | apply custom background                                 | I can personalise the application to my liking                                                  |
| `*`      | expert user of this application           | export contact details                                  | I can save and share the contacts in a backup location                                          |
| `*`      | expert user of this application           | import contact details                                  | I can duplicate contacts into another copy of the application on another device                 |

### Use cases

(For all use cases below, the **System** is the `Dormie` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a contact**

**MSS**

1.  User launches Dormie.
2.  User enters details to add a contact
3.  Dormie adds the contact

    Use case ends.

**Extensions**

* 2a. Dormie detects that the given command format is invalid.

    * 2a1. Dormie shows an error message.

      Use case resumes at step 2.

**Use case: Edit a contact's name**

**Use case: Delete a contact**

**MSS**

1.  User requests to list contacts
2.  Dormie shows a list of contacts
3.  User requests to delete a specific contact in the list
4.  Dormie deletes the contact

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. Dormie detects that given index is invalid.

    * 3a1. Dormie shows an error message.

      Use case resumes at step 2.

* 3b. Dormie detects that the command format is invalid.

    * 3b1. Dormie shows an error message.

      Use case resumes at step 2.

**Use case: Edit a contact's name**

**MSS**

1.  User requests to list contacts
2.  Dormie shows a list of contacts
3.  User requests to edit a specific contact's name in the list
4.  Dormie updates the contact with new edited name

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. Dormie detects that the given index is invalid.

    * 3a1. Dormie shows an error message.

      Use case resumes at step 2.

* 3b. Dormie detects that the given name is invalid.

    * 3b1. Dormie shows an error message.

      Use case resumes at step 2.

* 3c. Dormie detects that the given command format is invalid.

    * 3c1. Dormie shows an error message.

      Use case resumes at step 2.

**Use case: Add a contact's free time**

**MSS**

1.  User requests to list contacts
2.  Dormie shows a list of contacts
3.  User requests to add a free time tag for a specific contact's name in the list
4.  Dormie updates the contact with a new free time tag

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. Dormie detects that the given index is invalid.

    * 3a1. Dormie shows an error message.

      Use case resumes at step 2.

* 3b. Dormie detects that the specified free time tag is of an invalid format.

    * 3b1. Dormie shows an error message.

      Use case resumes at step 2.

* 3c. Dormie detects that the specified free time tag is already exists for the specified contact.

    * 3c1. Dormie shows an error message.

      Use case resumes at step 2.

* 3d. Dormie detects that the given command format is invalid.

    * 3d1. Dormie shows an error message.

      Use case resumes at step 2.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 10 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The user interface should be intuitive enough for users who are not IT-savvy.
5.  The product should respond within one second.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **GUI**: Graphical User Interface, the visual interface through which users interact with the application.
* **Performance**: The speed at which the application responds to user input.
* **Command**: A text-based instruction given to the application to perform a specific task.
* **Telegram**: A messaging app.
* **Telegram handle**: A unique identifier for a user in Telegram.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Important note when performing manual testing

When testing commands, testers are advised to begin with a valid command (i.e., a command with the correct format and parameters) 
and then modify the command format slightly to check whether invalid cases are handled properly. 

If there are multiple possible invalid parts in the given command, the error message may not address all of them at once. 
Instead, the error messages will change (e.g., as a tester modifies the command to correct one of the invalid parts), 
eventually guiding the user toward executing a correct command.

For example, for a given command `deleteTime 1 2 ft/Mon:1300-1400`, there can be two possible invalid parts:
1. `deleteTime` does not support multiple INDEX.
2. `ft/Mon:1300-1400` does not match any free time tag of the chosen person.

In this case, the error message shown will be "Invalid command format!", as `deleteTime` does not support multiple INDEX.
After user modifies the command to match the given command format (e.g. executing `deleteTime 1 ft/Mon:1300-1400`),
a new error message may be shown, which is "No matching free time to be deleted for the chosen person."
And after user modifies the command accordingly to provide a matching free time, the command will execute successfully.

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Adding a person

1. Adding a person

    1. Prerequisites: Open the application

       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data to JSON

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_
    1. _{ explain how the room number formatting requires dates }_
    1. _{ tbc }_

1. _{ more test cases …​ }_

## **Appendix: Effort**
1. Free Time Functionality
   * Originally, AB3 was developed with a tag-based system to categorize information pertaining to contacts, including attributes like relationships or interests. However, we recognised the adaptability of this tagging mechanism to efficiently manage temporal data, specifically a person's availability. As such, we modified the tag structure within AB3 to encode and organize individuals' free time. This involved transforming tags to represent availability in a standardized format 'day:time_interval'.
Furthermore, to optimise the platform's functionality for finding individuals based on specific time constraints, we introduced the `whoisfree` command. Calling this command will systematically traverse each contact's tagged availability, which is stored as a HashSet, to idenfity matches based on temporal alignment.
To accommodate changes in a person's free time, we also created the `addTime` and `deleteTime` commands to allow the user to modify a person's availability in the HashSet, ensuring improved accuracy in the availability information.
   * The main challenge faced in implementing this free time functionality was the proper adding and deleting of free time such that it can be easily searched when the `whoisfree` command is used. To overcome this, we focused on accuracy rather than efficiency, and decided to search the free time HashSet iteratively, although we understand that it could take up a bit more time.
2. Icons
   * To enhance user experience, we aimed to incorporate icons that serve as visual cues to prompt users to update outdated dorm room numbers for contacts and to highlight contacts whose birthdays coincide with the current date.
   * However, implementing this feature posed a challenge when testing if the icons will appear at the appropriate times, given the dynamic nature of dates within the application. To address this challenge, we adopted a solution involving the use of static dates for manual testing. By simulating specific dates, we were able to verify that the icons functioned as intended and appeared at the correct moments.


## **Appendix: Planned Enhancements**

Team size: 5

1. **Enable cross day free time interval:**
   * Currently, a free time interval can only be specified based on a single day. There is no way to specify a free time interval that spans multiple day.
   * To improve user experience, it could be useful to enable cross day free time interval, such as `Mon:2000-Tue:0800`.

2. **Enable free time interval merging:**
   * Currently, a person can have overlapping free time intervals.
     * For example, a person can have free time intervals of `Mon:1000-1200` and `Mon:1100-1300`, or `Mon:2000-2359` and `Tue:0000-0800`.
   * To improve user experience, it could be useful to merge free time tags that have overlapping time intervals.
     * For example, `Mon:1000-1200` and `Mon:1100-1300` can be combined as `Mon:1000-1300`.
     * For example, `Mon:2000-2359` and `Tue:0000-0800` can be combined as `Mon:2000-Tue:0800`.
   * The merging process could be done when new free time tag is added to the person, e.g. via `add` command, `addTime` command, or `edit` command.

3. **Delete free time within an interval:**
   * Additionally, it could be useful to be able to delete free times that fall within a time interval, e.g. via `deleteTime` command.
   * For example, an initial freeTimeTag could be `Mon:1000-1400`. Executing `deleteTime INDEX ft/Mon:1100-1200` will result in the new freeTimeTags `Mon:1000-1100` and `Mon:1200-1400`.

4. **Make command prefixes case-insensitive:**
   * Currently, the command prefixes are case-sensitive.
     * For example, `n/` is the prefix for "name" parameter, but `N/` is not acceptable by the command parser.
   * It may provide more convenience to user by disabling case sensitivity for prefixes. For example, allowing `n/` and `N/` as the prefix for "name" parameter.

5. **Supporting more forms of name:**
   * Currently, only name form that consist of alphanumeric characters and spaces in between is allowed.
   * It may improve the diversity of the application, by supporting name in different forms, including names in different languages and names that contain non-alphanumeric symbols.

6. **Supporting more forms of room number:**
   * Currently, the room number format is {block}-{floor}-{room number}, where block and room number are at least 2 alphanumeric characters and floor is strictly 2 alphanumeric characters. E.g. `nw-12-12`.
   * To accommodate more room number format, it will be good to change the minimum length of block and room number as 1.

7. **Make day format in free time tag case-insensitive:**
   * Currently, free time tag only accept forms of `DDD:HHmm-HHmm`, e.g. `Mon:1200-1300`.
   * It may provide more convenience to user by disabling case sensitivity on the day part, e.g. allowing `mon:1000-1200`, `Mon:1000-1200`, `MON:1000-1200`.

8. **Support more accurate free time:**
   * A person may not always be free for the same time interval every week. 
     * For example, a person can be free at `Mon:1000-1200` for every odd week of the semester, and busy at the time for every even week of the semester.
   * Hence, it will be useful for user to optionally specify an actual date as part of the free time interval, e.g. `01/01:Mon:1000-1200`.
     * If the date is not specified, e.g. `Mon:1000-1200`, the free time tag should be interpreted as recurring weekly.
     
9. **Make "Invalid command format" error messages more specific:**
   * When user provides a command with invalid format, the "Invalid command format" error messages are too general. 
     * For example, when executing `add n/Alice d/pgpr`, the error messages will suggest that the command format is invalid, and display the correct format together with a usage example. 
     * In this case, the command is invalid as some mandatory parameters are omitted, but new users may not expect that some parameters are mandatory.
   * Hence, error messages can be enhanced to be more specific, such that user can easily identify the reason of command failure.

10. **Make the `whoisfree` command to search for a range of time:**
    * When user perform a `whoisfree` command, it might be useful to ascertain that a person would be free for the whole duration.
    * For example, `whoisfree Mon:1200-1400` will display all person that are free for the time interval.

11. **Allow `edit` command to clear optional field:**
    * Currently, the `edit` command cannot clear the value of some optional fields (i.e. email, room number, telegram handle, birthday, dorm tag).
    * It will be useful to allow this, as the optional field, as its name suggests, should be able to take no value.



