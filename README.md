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


# 3rd party applications
1.firebase
2. api-ninjas (API)
3. Google sign in
4. PhilJay Library
5. Google Maps
6. Firebase storage

## 1.	Firebase
I have implemented a real time database from firebase that uses authentication to create an auth instance of the users email and password and login activity. Through registration a user object is created through registration or google login. Then the Id from this is being used in the authentication process is stored in a separate database called user-runs, there is also a runs database that is just a map of all the details stored for the run item object. So, the user-database is used the most as it is to check the credentials of the user and retrieve the correct data. This is done by referencing the database and having liveFireBaseUser that retrieves the userid that can be compared and then validated against the data stored.
![firebase](firebase.png)

The above tables are all interlinked and can be referenced by each other. By the following table:
![tables](table_db.png)

## 2.	Google sign in
This is achieved my implementing this into the grade.build and then creating a onClickListener that starts up the list of available Google accounts on the phone if there are any. This is convenient as your account will be auto generated from existing credentials associated with your Google account. Google provides all the methods required for this; it is just a matter of putting all the pieces together.
![google](google_signin.png)

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

# GitHub Process
The style I used for GitHub, is a feature, develop, release and hotfix. This is the practise that I implemented in work and was on the rubric.  I would create a branch and name it after the feature I was implementing and then pull into the develop branch. Some feature examples would be map-feature or api-retro etc. Some stages I would be working on multiple branches depending on which I had an answer come to me. Funny enough if I have been stuck on getting the code to work. When I run, I can envision the code and try to work out different scenarios that could resolve the issue and then test these theories when I return from my run. Merging conflicts and losing good code did happen but 90% of the time was resolved. The release branch will be the master branch as this is the final product. Tags were also pushed up and can be downloaded from the GitHub Repository.

# MVVM
The MVVM architecture was used for this project. This proved to be convenient in working with Firebase as it can handle call-backs and return to code that is executing and finish the execution. The process is to create a Mutable object and then create an observable live object, according to the documentation provided by google; it is best practise to name this get() with an underscore _exampleObject. The object is then passed to a method for example load() which will call on the firebase database and assign the _exampleObject the data returned from the firebase. Then in the view call the observable object and loop through and perform whatever task on the data that is required. 

## Login Page
This is the first point of contact for the user. The user will have the option of signing in (if the user has an account already and signed out previously). Firebase remembers a user that has not signed out. The user can also sign in with their google account and this is the most convenient way for the user to signup.
![google](google_signin.png)

## Register Page
This page asks the user for their name but requires the user to fill out their email address and password. This page was designed using the logo at the top on a card background and a scrollable view to allow for profile picture to be displayed. Once the user has entered their information then and registered successfully. The user will be sent to the landing page.
![register](register.png)

## Run List Fragment
This fragment is the landing page. This page displays all the running data that the user had recorded previously. It is a snippet of what you see compared to when you click into the individual run. The user can view an image in this recycler view as a memory of a great run or something that happened on the run out of the ordinary.  The Run List was created using an adapter that I followed from the labs with a few tweaks to match my project. If the User hits the toggle button at the top it will display the users’ friends runs also.
![runs](run_shown.png)

## Run Details Fragment
Access to this fragment is done by clicking on one of the run sessions. This is done by passing the id of the run through in an action from the Run List Fragment. On the other side the Run Details Fragment, it takes this args that was sent over. Then using our observableRunList we can retrieve the run by matching off to the runid. The other variation of this run Details fragment is clicking into a friends run and having the ability to view there running but not capable of editing this data. The user can add a comment to this run.
![tables](table_db.png)

## Maps Fragment
The soul of the project.  The user clicks the start button to start, from this action the data starts getting displayed back to the user. This data contains the speed the user is travelling, and the calories burned, and distance travelled. The on-Location Request that got Current Location and all the inbuilt function such as speed and distance that tracked the LatLng(Lattitude, Longtitude). As the user is running, a polyline is drawn to mark the progress of the user. Once the user has completed their run, they can hit the pause button and hit the save button that appears when paused. The data stored is the distance travelled during the run and calories burned and the Average speed, which was obtained by add all the speed values to an array list and looping over and dividing by the size of the array. It automatically brings the user to the Run Fragment.

## Run Fragment
The Run Fragment is where the user can manually add a run that they may have forgotten to add, or they can edit the data that was passed through a sharedViewModel. The user can also add an image in another fragment specifically for this, then automatically return to the Run Fragment for review of all the data before adding the run.

## User List Fragment 
The User List Fragment is the social adaption of the project. On this page hit the floating action button and it will retrieve all the users’ friends. The user can view all the other users and if they view someone that they know or just like the appearance of their profile they can add the user as a friend by clicking on the users’ profile.
![users](list_of_users.png)

## User Details Fragment
This fragment appears when the user clicks on a profile. It displays all the information of a potential friend. The user then can hit add as a friend. Now that this action has been undertaking. The user can view their friends runs and comment on their runs but also their data is compared against yours in the form of bar charts and pie charts.

## Bar Chart Fragment
This fragment uses the PhilJay library that creates nice charts. The charts that I chose for this project is the bar chart and pie chart. The bar chart compares the distance travelled by the user and their friends.

## Pie Chart Fragment
The Pie Chart Fragment also utilises the PhilJay library. The pie chart fragment displays the number of calories that was burned by the user and their friends in a competitive environment.

## Exercise Fragment 
Retrofit was used to create a call to the Api-Ninjas. This api supplier supplies all exercise data for different body parts as stated above. A search feature was also implemented on this page to allow a dynamic feel. The user can select traps for example and the api will return exercises the user can perform for that body part.


# My own Personal Experience
This class is by far my favourite of all the subject I have done to get this far in college.  Back in 2017 when I created my first mobile application which you thought also and then we met again in the Higher Diploma course set me on a path that I have achieved great success with.  I didn’t quiet get the MVVM concept during the Higher Diploma class but during this master’s class it was my mission to achieve a great understanding of this architecture and that I have. A lot of new tricks of the trade I learned in the Higher Diploma class. During this class I obtained a deep understanding of these tricks that I just knew how to use. Time is your worst enemy with a young family and doing your masters full time but there is a great feeling of accomplishment when a project that you worked hard on for months gets completed. Thanks for everything and hopefully our paths will cross some day again.


