Run this vscode terminal command to run the project: 
java --module-path javafx-sdk-26/lib --add-modules javafx.controls Main.java

(btw, if your computer uses JDK 21 or older, you will have to install a older (21.0.10 LTS) version of the javafx sdk, so the command will be different) 

Step 1 - Compile (do this once, or after any code changes):
javac --module-path <your-javafx-folder>/lib --add-modules javafx.controls Main.java

Step 2 - Run:
java --module-path <your-javafx-folder>/lib --add-modules javafx.controls Main

Replace <your-javafx-folder> with your actual SDK folder name, e.g. javafx-sdk-21.0.10
Make sure you are using JavaFX 21 LTS, not JavaFX 26. See README.md for setup instructions.


-IMPORTANT INFO REGARDING JAVAFX SDK-

JavaFX Setup Guide:

Everyone on the team needs to do this manually on their own computer. The SDK is never uploaded to GitHub.
1. Check your Java version

In your terminal, run java -version. You need Java 21 or higher. If you're below that, download Temurin JDK 21 from adoptium.net.

2. Download JavaFX 21 LTS

Go to gluonhq.com/products/javafx. Select Version 21 LTS, SDK, and your OS. For Mac, pick aarch64 if Apple Silicon, or x64 if Intel. Download and unzip it. Rename the folder to javafx-sdk-21 and drag it into your project folder, right next to your .java files.

3. Run the project

In terminal, navigate to your project folder and run:
java --module-path javafx-sdk-21/lib --add-modules javafx.controls Main.java
//Important: you may have a higher JDK version, but its probably JDK 21, the one the VSCODE gave to you.

4. Why the SDK is not on GitHub

The SDK folder is over 100MB which exceeds GitHub's file limit, and everyone needs a different version for their OS anyway. It is already listed in .gitignore so Git will never accidentally track it. Never manually git add the SDK folder.

5. If you get a "major.minor version" error

That means your JavaFX version doesn't match your Java version. Make sure you downloaded JavaFX 21 specifically, not JavaFX 26 or another version.

6. For users of Java SDK 26: you will need to do extra things to reduce errors, as the vscode and computer frameworks don't natively handle usage of javafx. Press cmd + shift + p to and search for configure classpath, go to libraries tab, select add libraries. 
Then go into your Javafx-sdk-26 folder, go into the lib folder, to highlight all the .jar folders (command A works). rerun/exit out of vscode, should be good. 


