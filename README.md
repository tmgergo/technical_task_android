### Functional requirement

Use https://gorest.co.in/ public API

:warning: **Note:** to test all features of the app, first you need to provide your own access token by overwriting the placeholder value in [`UserDataSource`](https://github.com/tmgergo/technical_task_android/blob/579ebc0fca59511b1c67ee70246eefd38538f27c/app/src/main/java/uk/co/tmgergo/userstechtest/userRepository/UserDataSource.kt#L28):
```kotlin
const val ACCESS_TOKEN = "[your access token here]"
```
(Without a valid access token the app will display a non-personalised user list, adding and removing users is going to fail.)

#### 1 Displaying list of users
- After app is open list of users is displayed (only users from last page of the endpoint)
- Each entry contains name, email address and creation time (relative to now)
- Loading and error state are welcome

#### 2 Adding new user
- After + button is clicked pop up dialog is displayed with name and email entries
- After confirmation and successful user creation (201 response code) item is added to the list

#### 3 Removing existing user
- After item long press pop up dialog is displayed with question “Are you sure you want to remove this user?“
- After OK is clicked and user is removed (204 response code) item is deleted from the list

#### Assumptions
I made a few assumptions that in normal circumstances I would have verified with product and UX.
- The API doesn't provide a creation time property, I omitted this
- The API provides the additional `gender` and `status` properties. I didn't display them on the screen and hardcoded them for newly added users
- I assumed that the last page requirement meant the latest page (page 1), so I did not handle paging
- Orientation change handling: The app renders the previously fetched user list again if available, or fetches list data otherwise (e.g. when it's not available due to a previous error)

#### Functional improvements, new or missing features
- There is currently no input validation on the name and email inputs when adding a new user
- The gender and status properties of users could be handled
- Paging could be implemented
- Pull to refresh would be a nice feature
- Potentially a local data sync for offline usage
- The current UI design is quite minimalistic. A UX person could probably greatly improve this. There is no tablet specific design either. Also, I didn't test accessibility.

#### Technical improvements
While I consider my technical approach 'good enough' that fits this simple project, if the project grew bigger I would *consider* the following improvements:
- A more abstract repository and data source solution: I am not fully satisfied with the slightly inconsistent data source API, an additional layer of abstraction could solve this and also support more detailed error handling (see below)
- More informative error types and messages: the app displays very generic error messages at the moment, this could be improved by additional error codes and messages. Ideally there could be an error code (payload or status code) based contract between the API and the client (instead of the current human readable errors in JSON)
- A more secure way to use access tokens
- Modularisation
- Using Kotlin serialization instead of Gson
- A service locator factory
- Presentation layer specific model types, instead of using e.g. the `User` business model
- Jetpack Compose to implement the UI
- A few UI tests and integration tests
- I could have been more consistent with using the verbs 'delete' and 'remove'. I would probably refactor related names to support a more ubiquitous language (DDD)
