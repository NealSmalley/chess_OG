# Textbook link:
https://github.com/danagow/books/blob/master/Core%20Java%20SE%209%20for%20the%20Impatient%2C%202nd%20Edition.pdf

# Fail_of_Default_Terminals_Commands
This should fix the issue:
Correct the path using the following command:
export PATH="/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$PATH"

# Terminal_InteliJ_Shortcut
How to create a terminal shortcut in intellijay?

### Step 1: Check if the idea command already works

Type idea into the terminal
if it gives the error "zsh: command not found: idea" or another error that says it doesn't exist move to step 2.
### Step 2: Find he Configuring Command-Line Launcher

open intelliJ idea
click on tools
click on "Create Command Line Launcher"
### Step 3: Copy the path given into the terminal

Doing step 2 will lead to the following pop up below. Exa: To make the IDE accessible from the command line, please add 'random path information will be here' to the $PATH variable and use 'idea' to run commands.
### Step 4: Add command to the terminal in this format

use the path that was in quotes and add some addtional information in the quotes and outside the quotes. export PATH="/Applications/IntelliJ IDEA.app/Contents/MacOS:$PATH"
If the error "export: not valid in this context: /Applications/IntelliJ IDEA.app/Contents/MacOS:/Users/nealsmalley/miniconda3/bin:/Users/nealsmalley/miniconda3/condabin:/opt/homebrew/bin:/opt/homebrew/sbin:/Users/nealsmalley/.nvm/versions/node/v20.11.1/bin:/Library/Frameworks/Python.framework/Versions/3.11/bin:/usr/local/bin:/System/Cryptexes/App/usr/bin:/usr/bin:/bin:/usr/sbin:/sbin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/local/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/bin:/var/run/com.apple.security.cryptexd/codex.system/bootstrap/usr/appleinternal/bin" appears that means you spelled something wrong.
### Step 5: update the run commands without restarting the terminal:

run this command in the terminal: source ~/.zshrc
### Step 6: open intelliJ with the terminal

type idea and the intelliJ should open up.


# How to delete a Github repository?
## Steps:
- Open the repository that you want to delete.
- Click on "Settings" at the top of the page.
- Scroll to the bottom of the page to the "Danger Zone".
- Click on "Delete this repository" and do the following authentication processes.

# How to get escape the Github log?
- click q

# How to generate a toString() function in intelliJ idea?
- right click on a class name
- click on "Generate"
- click on "toString"
- click on "ok"

# How to generate a override for equals() and hashCode() in IntelliJ
- click command n
- override methods
- hashCode() and equals()
