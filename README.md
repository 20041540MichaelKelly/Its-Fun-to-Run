# Its-Fun-to-Run
This is an application that brings friends together in a positive way. Allowing the users to interact as they track their runs and compete against each other.

# Introduction
The project that I embarked on as part of my Mobile App Development was called “It’s Fun to Run”. The app is a social running app that allows users to interlink with one and other. The application was something that I was planning during lockdown. The whole concept was the ability to enjoy and monitor one of the only luxuries that we could have while in lockdown; “Running”, and on the other spectrum of this was the inability to be social. The idea was to stay in contact with friends and leave comments on each other’s runs, for a bit of banter. The goal would be to keep all runners spirits up while in this awful unprecedented situation.

# Prerequisites
Login Information that will allow you to login:
Email: myles@gmail.com password: secret123
I was using my Samsung S22 to test my app, This phone has an Android version of Also the emulator with Tiramisu Android 13 will run the application.

# Flow of the Project (Use Case)
The flow a user will do:
1.	User will see a splash screen of “It’s Fun to Run” logo.
2.	User can register an account manually or login with their Google account.
3.	If a user did not fill out the required fields for a successful registration. The user will be notified and prompted to re-enter the validated entries.
4.	User is displayed the landing page. This is the run list page that will display the users runs.
5.	If a user would like to record a run. User can navigate to the map icon. On this Fragment, A user will have the ability to record their run. Tracking the users speed, caloriesBurned and Distance and a Polyline to display the movement that the user had taken. Once the user would like to stop recording. The user can click the save button and will be brought to the run fragment. In this fragment the user can edit the information or add an image. 
6.	Once happy the user can add run and will return to the run list.
7.	User can click on this run and edit also if needed.
8.	The user can store as many images as the user likes and will be presented out in a gallery for the user.
9.	If the user is feeling social the user can click the add friend icon and view all users. If the user wants to add a friend, click on the potential friend’s profile card and the user can view their entire information. If the user likes what they see. Click on the add button and now this user is your friend.
10.	 Returning to the run list. The user sees their runs but not can hit the toggle button and see the runs that their friends have also done. 
11.	The user can click on the friends run and leave a message for the other user to view.
12.	If the user wants to mix up their training. The user can navigate to the exercise section where they can search an exercise API for an exercise for any body part. The default that is set is Hamstrings. User can search from this list:
a.	abdominals
b.	abductors
c.	adductors
d.	biceps
e.	calves
f.	chest
g.	forearms
h.	glutes
i.	hamstrings
j.	lats
k.	lower_back
l.	middle_back
m.	neck
n.	quadriceps
o.	traps
p.	triceps
13.	The user can select the statistics tab that will display a bar chart of there distance they have performed.
14.	Also the user can select a pie chart displaying the amount of calories burned for each day.

![Screenshot](screenshot.png)

# 3rd party applications
1.firebase
2. api-ninjas (API)
3. Google sign in
4. PhilJay Library
5. Google Maps
6. Firebase storage

## 1.	Firebase
I have implemented a real time database from firebase that uses authentication to create an auth instance of the users email and password and login activity. Through registration a user object is created through registration or google login. Then the Id from this is being used in the authentication process is stored in a separate database called user-runs, there is also a runs database that is just a map of all the details stored for the run item object. So, the user-database is used the most as it is to check the credentials of the user and retrieve the correct data. This is done by referencing the database and having liveFireBaseUser that retrieves the userid that can be compared and then validated against the data stored. 
 
The above tables are all interlinked and can be referenced by each other. By the following table:
 

## 2.	Google sign in
This is achieved my implementing this into the grade.build and then creating a onClickListener that starts up the list of available Google accounts on the phone if there are any. This is convenient as your account will be auto generated from existing credentials associated with your Google account. Google provides all the methods required for this; it is just a matter of putting all the pieces together.
## 3.	Api-ninjas (Free API)
This is a free API to a certain extent depending on how many calls are made a month I have adapted this feature into the app so the user can retrieve different exercises for any part of the body. It is as important to keep all your muscles strong for improved performance of running. This feature will give the user the option of filtering through the data to find what exercise that best suits them. 
## 4. Google Maps
Google Maps is the centre point of this project; I had implemented an earlier version of this using an Activity but ultimately decided to go with the fragment MVVM architecture. The map records your trajectory by recording this through the form of a Polyline. The user’s speed is displayed to the user interface but when recording the run the average is stored. This is achieved by storing all the speeds into a Array List and then getting the average. Distance and Calories burned are also recorded. There is a toggle button that will appear when the user pauses the run and once the user hits save it will save the run out to the Run Fragment View where the user can add an image that is done in a separate fragment for modularity. 
## 5.	PhilJay Library
This library allows all different charts to be displayed based on data that is provided. For instance, Distance can be displayed back out in the form of a bar chart.
## 6. Firebase Storage
This feature was introduced so I could upload the images and then get a reference in the form of a url  of the storage destination. This photoUrl was store in the realtime firebase.

# UX/UI Design
The user experience and design I went with was a simple design and intuitive for the user with self-explanatory navigation, if app is downloaded on the phone first time it will be prompted to sign up by manual or Google which is in line with other modern applications, ease of sign up and login. I tried to base the whole project on ease of use. The less work the user must do to accomplish what they need the better. The run list will load first when the user signs up/in. If the user has already undertaken a few runs, it is nice to review for personal growth. It is created with a recycler view that is fed from the firebase database. The map is the centre piece to the project and is the base for collection of rich data from the user that has performed a run. The style and User interface had changed numerous times before I finally settled with this design. 

# Fragments
The first version of this project was created using activities as the framework but in the second part being this project, I have created It on Fragments. Fragments are a convenient way of having multiple activities being controlled from the 1 activity. It makes the application more modular. The process in which these were implemented was by following the labs but then turning to my already implemented activities and then converting them freestyle. All features are implemented as fragments in the MVVM architecture.

# Nav Bar
The Nav Bar contains multiple options for the user, and it has its own style in the xml layout but also has its own menu layout. This in turn is called from the mothership being the Home Activity. All fragments are referenced in the main navigation menu layout and from here they reference their own individual xml layout files. So, no matter where you are you can bring up the navigation drawer from any of the features in the application.

