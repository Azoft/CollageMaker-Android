## Collage Maker for Android

This code allows to import 100 photos from any Instagram user feed into your app, make a collage out of several (1-4) randomly selected pieces, and save the result as an image; the number of collage images is scalable, as the project uses the Fabric. The app user can manually rotate images of the collage.

# Features

Searches an Instagram user by a nickname; 
Builds photo list from 100 first user’s photos sorted by Likes; 
Renders collage preview in runtime.

# Support

Android 2.3+
Deviñes: All

# The Image Loading Algorithm

Ask server to get 100 photos.
Server returns some results.
If server returns less than 100 photos and there is another page to load, load it. 
If server returns 100 posts, stop loading.

# Loading and Storing Images

We used the user feed variable cache in the ImageSelectionFromUserFeedFragment class, but the data can be also stored in an SQLite database. We chose to use cache because in our case little memory was required, so there was no need to create and manage a DB. 

If you'd like to use a DB, there are two scenarios for loading images:

1. To use two loaders – one for retrieving data from a DB and the other for loading photos from a network and storing in the DB. 
2. To use a loader and a service – the loader for retrieving data from a DB and the service for loading data from a network and storing in the DB.

# Licence
Azoft-Collage/libs/licences.txt