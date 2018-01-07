# PopularMovies-1

# IMPORTANT NOTE:
Add your API key for themoviedatabase inside the variable "String key" in the MainActivity.java file. It has been also directed to do so in a comment.

Android app which lets the user see a list of movies downloaded from themoviedatabase API.
The movies can be seen on basis of "Most Popular" or "Top Rated" categories by selecting the apt option from the menu.

# Main Activity
The main activity contains the list of all the movies. 
All movie posters are displayed in a Grid Layout using a GridLayoutManager and RecyclerView classes. ViewHolders have been used to efficiently reduce the CPU load while displayinh the grid by recycling views. 

Every poster in the MainActivity has a OnClickListener applied, which takes the user to the DetailActivity on a click.

# Detail Activity
Upon clicking on any poster image, a Detail Activity is opened which shows other details about the respective movie. These include:
 - A bigger sized poster image
 - Title of the movie
 - Rating average
 - Release date
 - Synopsis
 
Rating average is displayed using the RatingBar UI component.

Connectivity Managers have been used in various places to make checks about the user's internet connection so the app handles situations of no connectivity smoothly. ProgressBar has been used to improve the UI/UX.

Minimum API level : 15
Test devices:
Nexus 5X (Android 8.0) - Emulator
Samsung Galaxy S7 Edge (Android 7.0) - Physical Device.
