# PowerConstructs

An Assignment app of powerplay. 

## Flow of use:
1. First Screen contains the list of added Drawing and a floating Action button to add Drawing.
2. Tap the drawing to view its profile, added markers and to add Markers.
3. Pinch the image to zoom in and zoom out.
4. Explore the image and Double tap on the image to Add a new marker.
5. Single tap on the existing marker on the image to view its details in BottomSheet.
6. Tap the Floating Action Button on the Drawing Profile to view the detail list of all the added marker to that drawing.

## Tech Used:

1. Firebase firestore to store the data.
2. Firebase Storage to store the images.
3. Hilt for Dependency Injection.
4. Flow for smooth and continuous states.
5. Clean MVVM Pattern.
6. Glide for image downloading.
7. subsampling-scale-image-view library for zoom.
8. ViewBinding

## Bonus Features Mentioned are also implemented.
1. All markers of a drawing are shown in a list with title, description and creation time
2. Shown all times in social format like few minutes ago , an hour ago , a day ago etc
