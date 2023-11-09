# Wordle Clone - Java Edition

A Wordle-inspired word puzzle game where you have the task of guessing a secret five-letter word. With an implementation that adheres to the Model-View-Controller (MVC) architecture, this clone provides both Command-Line Interface (CLI) and Graphical User Interface (GUI) gameplay.

## How to Play

You are given **six chances** to guess the secret word. Each guess must be a legitimate five-letter English word. Press the 'Enter' button to submit. After each guess, the color of the tiles will change to reflect how close your guess was to the word.

### Rules

- **Green**: If a letter is in the word and in the correct position.
- **Yellow**: If a letter is in the word but in the wrong position.
- **Grey**: If a letter is not in the word in any position.

Invalid guesses (non-English words or improper lengths) are rejected and will not count against your chances.

## Features

- **CLI and GUI**: Choose your preferred mode of gameplay.
- **Input Validation**: Ensures only valid English words are accepted.
- **Color-Coded Feedback**: Receive immediate color-coded feedback after each guess.

## Starting the Game

Clone the repository to your local machine to start playing. Make sure Java is installed and configured properly on your system.

git clone https://github.com/genuine695/Wordle.git
