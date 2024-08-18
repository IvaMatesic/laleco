# Laleco - Language learning companion
This is backend of an application that will help you learn German and possibly other languages in the future. 

It is a passion project where I try to implement things that are missing or could be done in another way that fits more with my style of learning and contain support for my learning materials. It is also a kind of playground for me where I will be trying out different approaches and technologies but I will still follow best practices and keep the code clean and maintainable as possible. If you are interested what will be devloped in the future you can check this document: https://docs.google.com/document/d/1aLhBvf_6QBomgpJvhJjOIfKzRqpLalGMyi0LJQIBZpM/edit?usp=sharing

## Features
- Bulk create of word translation pairs by copying different formats:
   - excel file (two columns expected, 1st for german word, 2nd for translation)
   - Easy German vocabulary for the calls of Conversation Membership
   - Seedlang worksheet vocabulary
- Fetch of words in random order
- Words are devided into lessons that you provide (title, url)


New features, dockerization and deploy to live website is expected in the future...

# Setup instructions
### Prerequisites
- Java 21
- Git


1. **Clone the repository**:

   ```
   git clone https://github.com/IvaMatesic/laleco.git
   cd laleco
   ```

2. **Run the application using Gradle**:

   ```
   ./gradlew bootRun
   ```
3. **Connect with Postman or using laleco-ui**

