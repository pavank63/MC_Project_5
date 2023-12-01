# Import necessary libraries
from surprise import Dataset, Reader, KNNBasic
from surprise.model_selection import train_test_split
from surprise.accuracy import rmse
import pandas as pd

# Load your dataset
data = pd.read_csv('./mental_health_activities.csv')

# Preprocessing

# Load the dataset into Surprise
reader = Reader(rating_scale=(1, 10))
dataset = Dataset.load_from_df(data[['UserID', 'PastActivities', 'ActivityEffectiveness']], reader)

# Split the dataset into the train and test set
trainset, testset = train_test_split(dataset, test_size=0.25)

# Use KNN
algo = KNNBasic()

# Train the model
algo.fit(trainset)

# Make predictions
predictions = algo.test(testset)

# Calculate RMSE
accuracy = rmse(predictions)

# To make a recommendation for a specific user
user_id = 10001  # Replace with the actual user ID
user_items = trainset.ur[trainset.to_inner_uid(user_id)]
predicted_ratings = [(algo.predict(user_id, item[0]).est, item) for item in user_items]
recommended_items = sorted(predicted_ratings, key=lambda x: x[0], reverse=True)

# Output top 5 recommendations
top_5_recommendations = recommended_items[0]

print('Top Recommendation: {}'.format(data.loc[top_5_recommendations[1][0], 'PastActivities']))
