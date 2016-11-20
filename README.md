# WolfVilla
CSC440 Final Project
Implemented via Spring 4, Thymeleaf 3, Boostrap, DataTables.js and Oracle sqlplus

Members:
Xiaohang Miao (Team Lead, Tech Lead, Sysadmin)
Joshua Cook (Developer, DBA)
Edward Gilliam (Developer)
Adam Crowell (Developer, QA)

## Environment Setup
0. Fork this repo and clone to your local machine
1. Download and install [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/)
2. In IntelliJ, `File > New > Project From Existing Source`
3. Select the cloned repo
4. Select `Import project from external model > Maven`
5. Check `Import Maven projects automatically`
6. Click `Next` or `Ok` until the project is created

## Running/Debugging the project
0. In Intellij, press `ctrl+shift+A`, type in `maven projects`. (Alternatively, you can click on `Maven Projects` tab on the upper right side of the IDE.)
1. Right click on `WolfVilla Maven Webapp > Plugins > jetty > jetty:run`, select `debug`
2. Open web browser, go to `localhost:8888/wolfvilla`

## Pushing changes
0. Organize and push changes to your origin dev branch
1. Create a pull request from origin dev to upstream dev
2. Assign others to review your code
3. Whoever approves the review can merge it to upstream dev branch. (Let's be disciplined and not push unreviewed code.) 
