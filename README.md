Group 345 Final Project
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





Chessly with 450mg electrolytes
