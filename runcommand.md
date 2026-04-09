Run this vscode terminal command to run the project: 
java --module-path javafx-sdk-26/lib --add-modules javafx.controls Main.java

(btw, if your computer uses JDK 21 or older, you will have to install a older (21.0.10 LTS) version of the javafx sdk, so the command will be different) 

Step 1 - Compile (do this once, or after any code changes):
javac --module-path <your-javafx-folder>/lib --add-modules javafx.controls Main.java

Step 2 - Run:
java --module-path <your-javafx-folder>/lib --add-modules javafx.controls Main

Replace <your-javafx-folder> with your actual SDK folder name, e.g. javafx-sdk-21.0.10
Make sure you are using JavaFX 21 LTS, not JavaFX 26. See README.md for setup instructions.
