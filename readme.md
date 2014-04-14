## CollageMaker-Android
##

# Support OS:
2.3+

# Support devices:
all

# Comments:
In ImageSelectionFromUserFeedFragment class we use aggressive user feed variable cache, but it can be done by storing data in SQLite database.
We do so because it is simple (we shouldn't create and manage DB) and will not use a lot of memory in this test.
Loading can be done by two scenarios:
1. Two loaders - one for retrieving data from DB and other to load it from network and store into DB.
2. One loader and service. Loader for retrieving data from DB and service to load it from network and store in to DB.

We load 100 image posts from user feed. Here is the algorithm:
Ask server to get 100 posts. Server returns some results (may be not 100). We get only image posts and if we have less then 100 and there is an other page to load, we load it. If we add 100 posts, we stop loading.

# Libraries licences:
Azoft-Collage/libs/licences.txt