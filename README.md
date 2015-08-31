A photo sharing android application called PicShare which will have the following features:

1. User Login: A login page lets the user login with their Facebook Account. The user should authorize and allow the application to use Facebook credentials to login to the application.

2. Sharing Preference: PicShare will then ask the user to select friends from Facebook to whom the user can send invites to join the App. User can select a single friend or multiple friends to send the invite.

3. Photo Collection: Users in PicShare can create albums which will have a mandatory title field and an optional description for the album. The user can upload photos from his device to the album. Furthermore, the user can add new albums and update existing albums as well.

4. Photo Information: Every photo in the album can have optional fields to indicate description of the photo and the . These is considered to be metadata information for the photos and will be stored in a backend datastore.

5. Photo Visibility: Albums can be shared with individual friends as well as group of friends in PicShare. The albums and its corresponding photos can only be viewed by the owner and those friends with whom the albums were shared. 

6. Photo Search: Every use in PicShare can browse through his owned and shared albums and browse photos inside the albums as well. Users with owner or shared permissions can comment on any photo they have access to.

##Developing

1. Android SDK  with Eclipse
2. Facebook SDK 4.0
3. Parse DB to store the user, photo and album details
