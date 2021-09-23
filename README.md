
# Warehouse Management System
Software application designed to support and optimize warehouse functionality and distribution center management.

For now, some of the features included are:

-   A user system with roles and permissions for listing and placing orders.
-   Maintain the stock of a warehouse.
-   Order items from the warehouse.
-   Log actions from users.
-   Reporting.

Author: Klaus Grosser
: GitHub: https://github.com/KlausGrosser

Project Web Page:
: https://klausgrosser.github.io/Warehouse-Management-System/public/

Development Setup:
```
Step 1: Open Terminal and install IntelliJ IDE through the Homebrew command:
		brew install --cask intellij-idea-ce

Step 2: Open IntelliJ IDE.

Step 3: Download and install a JDK by:
		- Create a New Project.
		- Select Java from the right sidebar.
		- Under "Project SDK" select "Download JDK".
		- Select Amazon Corretto version 11.

Step 4: Continue without selecting a template.

Step 5: Assign a name and address to the project.

Step 6: Through the Terminal, create a local git repo and connect it to the remote  Github repo.
		Pull the remote repo to download all the current files and be updated.

Step 7: Through the Terminal, create folders with the command:
		
		mkdir -p src/{main,test}/{java,resources}

Step 8: Inside IntelliJ, select the "java" folder inside the "main" folder.

Step 9: Go to File and create a new Java Class, and name it "main".

Step 10: Inside the "main" Java Class create the Hello World app with the next code:
		
		public class main {
    			public static void main(String[] args){
        				System.out.println("Hello World !!!");
    				}
		}

Step 11: Commit the changes and push them to the remote repo.
```


License: 
: This project is licensed under the terms of the GNU General Public License v3.0.
https://github.com/KlausGrosser/Warehouse-Management-System/blob/main/LICENSE.txt
