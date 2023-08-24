NOTES:
- Same model for GUI and CLI
- MVC

GAME RULES
- Letters can occur more than once
- Must be a valid English word
- Getting an invalid word doens't count as an attempt

GUI & CLI
- [ ] Flag: randomly select the word (otherwise, it's fixed)
- [ ] Flag: display target word for testing purposes
- [ ] Flag: show error message if word is not in list of known words.
- [x] GUI and CLI are 2 separate main methods in different files

CLI
- [x] Must show whether player has won or lost
- [x] Indicate letter choices by listing them alphabetically within each group (non-guessed, guessed and wrong, yellow, green)
- [x] Uses GUI model but doesn't define a view or controller

GUI
- [x] Respond to letter presses, backspace, and enter
- [ ] Keyboard with colored letters
- [x] New Game button that's enabled afetr the first valid guess
- [x] Implements Observer
- [x] Has an update method that'ts called when the model changes

- [ ] Win/loss conditions?

MODEL:
Files are provided
- [x] Load list of 5-letter "target" pool and longer list of 5-letter "acceptable guesses" pool
- [x] Implements the 3 ARG flags
- [x] Number of allowable guesses in a constant
- [x] No reference to 2 classes. May consist of many classes but must have one called Model.
- [x] Extends Observable
- [x] Does the file reading
- [ ] Asserts: invariants for the class, pre- and post- conditions for all public methods.

CONTROLLER:
- [ ] Forwards only valid requests to the Model (query the model to check a request is valid)
- [ ] Enables/disables GUI elements
- [ ] No GUI code.

DOCUMENTATION:
- [ ] Asserts
- [ ] Unit testing
- [ ] Class diagram
- [ ] Comments

TESTING:
- [ ] 3 JUnit tests, significantly different
- [ ] Comment each one with the scenario being tested
- [ ] Set model into the correct state for the test -- this should be clear by rading the code
- [ ] Testing MUST be of the Model class, not one of its component classes


https://stackoverflow.com/questions/24622279/laying-out-a-keyboard-in-swing