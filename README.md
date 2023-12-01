# Recommendation System

## Overview
This model implements a recommendation system using a collaborative filtering approach. It's designed to predict user preferences based on past activities and their effectiveness. The script utilizes the `surprise` library, particularly the KNNBasic algorithm, for generating recommendations.

## Features
- **Data Loading and Preprocessing:** The script loads a dataset of user activities and preprocesses it for the recommendation system.
- **Model Training:** Uses the KNNBasic algorithm from the `surprise` library to train the model on the dataset.
- **Prediction and Evaluation:** The script makes predictions on a test set and evaluates the model's performance using Root Mean Square Error (RMSE).
- **User-specific Recommendations:** Ability to generate recommendations for individual users based on their past activities.

## Requirements
- Python 3.x
- Pandas
- Surprise library

## Installation
1. Ensure Python 3.x is installed.
2. Install required libraries using pip:
   ```
   pip install pandas surprise
   ```

## Usage
1. Place the dataset (CSV format) in the same directory as the script or modify the script to point to the dataset's location.
2. Run the script using Python:
   ```
   python recommendation_system.py
   ```
3. To get recommendations for a specific user, replace the `user_id` variable in the script with the desired user ID.

## Dataset Format
The expected dataset format is a CSV file with columns: `UserID`, `PastActivities`, and `ActivityEffectiveness`, where:
- `UserID` is the unique identifier for each user.
- `PastActivities` is the identifier for the activities engaged in by the user.
- `ActivityEffectiveness` is the effectiveness rating of the activity for the user.
