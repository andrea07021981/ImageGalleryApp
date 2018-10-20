# IMAGE GALLERY APP

## Introduction

This app allows the users to get a list of images from flickr. User can write a query param into the edit and start the online request by the search button.
This is only the first step for this app. I prepare an abstract class for activities which need a single fragment, I think it will be use usefull for the next steps.
I used recycleview and a custom adapter, I've not implemented any click action yet, it depends on the next steps.
I prefered to use the AsyncTaskLoader for getting the images and the simple HttpUrlConnection for the network job.
I could have used libraries such as Volley or Retrofit with the converter, but for an easy single shot call the HttUrlconnection is not bad.
 
If I had more time I would have used the viewmodel for saving the fragment state. Moreover I think it would be great 
using the room with viewmodel and livedata if we want to save informations about our last researches and have a dynamic UI.
I set up the thumbnail format, but we could add a new button for saving the format that the user would like to request.
This option would also change the item style for the ImageView because we should expect different formats.

## Documentation
No documentation needed at this moment.
