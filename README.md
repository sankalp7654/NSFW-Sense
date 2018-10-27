# NSFW-Sense
The ‘NSFW’ (Not Safe For Work) model returns probability scores on the likelihood that an image contains nudity. This model is great for anyone trying to automatically moderate or filter offensive content from their platform.

**REQUEST**             
You can call the Predict API with the 'NSFW' model. Simply pass in an image input with a publicly accessible URL or by directly sending image bytes.                  

**RESPONSE**                 
The Predict API returns probabilities for nsfw (Not Safe For Work) and sfw (Safe For Work) that sum to 1.0. Generally, if the nsfw probability is less than 0.15, it is most likely Safe For Work. If the nsfw probability is greater than 0.85, it is most likely Not Safe For Work.

**API USED**                               
Clarifai API is used in this project.
