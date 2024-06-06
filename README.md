**Anonymous Grading App**


Welcome to the Anonymous Grading App, an app designed to transform the way instructors assess written exams. This app ensures complete anonymity in grading, promoting fairness and integrity in academic evaluations.

**Key Features:**


**Secure Instructor Login:** Ensures that only authorized personnel have access to the grading system, enhancing security. For this, AWS Amplify and AWS Cognito are used to provide secure authentication and user management. AWS Amplify simplifies the integration process, while AWS Cognito offers robust security features, including multi-factor authentication and secure token storage.

**Barcode Generation:** Generates unique barcodes for each student, maintaining their anonymity throughout the grading process. The barcodes are stored in AWS DynamoDB using AWS Amplify and a REST API, ensuring that all data is securely stored and easily retrievable. DynamoDB provides a scalable and highly available NoSQL database solution, perfect for handling the unique barcode data.

**Class Roster Management:** Allows instructors to efficiently manage and organize class rosters. Any changes made in the app are pushed to the database in real-time using the REST API, ensuring that the data is always up-to-date and consistent across the platform.

**Exam Management:** Seamlessly add and manage exams within the app. This feature allows instructors to create, edit, and organize exams, making the grading process smoother and more organized.

**Efficient Grade Mapping:** Utilize scanned barcodes to map grades efficiently and accurately. The scanned data is processed and stored securely, ensuring that the grades are accurately mapped to the correct students while maintaining anonymity.
