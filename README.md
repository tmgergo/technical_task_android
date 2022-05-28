# Sliide Android developer challenge

:warning: **Note:** to test all features of the app, first you need to provide your own access token by overwriting the placeholder value in [`UserDataSource`](https://github.com/tmgergo/technical_task_android/blob/579ebc0fca59511b1c67ee70246eefd38538f27c/app/src/main/java/uk/co/tmgergo/userstechtest/userRepository/UserDataSource.kt#L28):
```kotlin
const val ACCESS_TOKEN = "[your access token here]"
```
(Without a valid access token the app will display a non-personalised user list, adding and removing users is going to fail.)

## My approach

### Planning
Given the fixed time frame (3 hours) and 'cost' (1 person) I planned my work focusing on scope:
- I created a prioritised backlog and maintained it throughout the test
- I identified the 'Displaying list of users' feature as the vertical slice MVP as I was confident I will be able to deliver this within the set time frame
- I focused on the 'Removing existing user' feature next as it was less complex (simpler UI/UX, no input validation need)
- Finally. I worked on the 'Adding new user' feature
- I also left future improvement ideas on my backlog, see below in the [Future improvements](https://github.com/tmgergo/technical_task_android#future-improvements) section

I continued working on the test after 3 hours, but I put a [`3-hour-mark` tag](https://github.com/tmgergo/technical_task_android/releases/tag/3-hour-mark) on the last commit that was still within the 3-hour time frame too.

The app was tested on a Pixel2 XL API 30 physical device, a Pixel 2 API 31 and a Pixel C API 28 emulator. 

### Methodologies, technology selection decisions
- All data layer, business and presentation logic parts have been implemented following TDD with BDD-style test scenarios
- I used hexagonal architecture with the data source (`UserDataSource`), an abstract view (`UserListView`) and text provider (`TextProvider`) being the main ports
- The presentation layer uses MVVM+C with a listener based observer pattern
- The core of the - very simple - application and business logic are the `UserListViewModel` and the `UserRepository`
- The network layer uses Retrofit2, OkHttp3 and Gson. To aid testing this layer I used MockWebServer
- I implemented a more traditional, 'manual' dependency injection approach backed by a service locator
- Although there is only a single screen in the app I took a single activity + Android Navigation approach. While this might look like an overkill for a single screen, I would switch to this solution if I had to add more screens to the app, so I decided this is not YAGNI here.  

### Assumptions
I made a few assumptions that in normal circumstances I would have verified with product and UX.
- The API doesn't provide a creation time property, I omitted this
- The API provides the additional `gender` and `status` properties. I didn't display them on the screen and hardcoded them for newly added users
- I assumed that the last page requirement meant the latest page (page 1), so I did not handle paging
- Orientation change handling: The app renders the previously fetched user list again if available, or fetches list data otherwise (e.g. when it's not available due to a previous error)

## Future improvements
As mentioned before, I didn't tick off all my backlog items. Here are a few improvements I would consider.
### Functional improvements, new or missing features
- There is currently no input validation on the name and email inputs when adding a new user
- The gender and status properties of users could be handled
- Paging could be implemented
- Pull to refresh would be a nice feature
- Potentially a local data sync for offline usage
- The current UI design is quite minimalistic. A UX person could probably greatly improve this. There is no tablet specific design either. Also, I didn't test accessibility.

### Technical improvements
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


*  *  *  *  *


## Congratulations, you have reached the next stage which is solving a Sliide practical test.
We’d like to you to write simple Android application for managing users.

### Description
When we have reviewed your test, and any accompanying documents you feel necessary, if we like what we see, we’ll invite you to join us for a video conversation during which we’ll ask you to go through your test, explaining any decisions that you made.

### Implementation
For implementation we use https://gorest.co.in/ public API

### Functional requirement
Feel free to use whatever flare you can to show off your skills.

You shouldn't spend more than 1 day on implementation, but if you need more time to show the best quality, feel free to use it. We prefer finished, clean, production ready implementation with unit tests, rather than half done solution.

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

### Technical requirements
- Application must be developed in Kotlin with minimum Android SDK version of 21
- You are free to use whatever frameworks or tools you see fit
- Application needs to support device rotation
- Design should follow Material design guidelines
- RxJava or Coroutines
- Architecture one of MVP/MVVM/MVI
- Dependency injection with Dagger 2 or Hilt
- Unit tests

### Evaluation Criteria
- You create testable code
- You pay attention to detail
- Code should be production ready

### Deliverables
- The forked version of this repo


