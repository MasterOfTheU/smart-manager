# Smart Manager
[![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](https://forthebadge.com) 
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) <br>
![Contributions welcome](https://img.shields.io/badge/contributions-welcome-orange.svg)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

An Android application for time management that was created as a result of diploma project about time management problem solving and helps user to beat procrastination.
The project is a combination of different time management methodologies and techniques that are combined in a single application:
 * Getting Things Done 
 * Pomodoro Technique
 * Don't break the chain
 
The main goal was to create an intelligent time management system that teaches user how to set priorities and categorizes tasks for him while the user can teach the system and edit tasks manually if it has made a mistake. The intelligent component was a previously trained neural network that uses supervised learning algorithm. The model was created using Google Cloud AutoML Natural Language service. All the text examples were categorized manually. 

![tasks](https://user-images.githubusercontent.com/15348166/61200808-511ea900-a6eb-11e9-9e48-0b379f223c7e.png)

## Business Requirements
![usecase](https://user-images.githubusercontent.com/15348166/61199945-79f16f00-a6e8-11e9-9967-5c2716793997.png)

## App Architecture
![architecture](https://user-images.githubusercontent.com/15348166/61200022-c76ddc00-a6e8-11e9-8998-7c273e1ac698.png)

### Demo
Examples of adding a task with intelligent classification, marking task as complete and setting a timer
![add_with_classification](https://user-images.githubusercontent.com/15348166/61201146-47e20c00-a6ec-11e9-8353-35ba1bda587b.gif)
![complete](https://user-images.githubusercontent.com/15348166/61201154-4f091a00-a6ec-11e9-89cc-28d6b1900f82.gif)
![set_timer](https://user-images.githubusercontent.com/15348166/61201156-50d2dd80-a6ec-11e9-92e2-8d26b818d304.gif)
